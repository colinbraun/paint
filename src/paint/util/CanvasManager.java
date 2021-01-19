package paint.util;

import com.sun.istack.internal.NotNull;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import paint.Main;
import paint.constant.ToolMode;
import paint.constant.SaveChoice;
import paint.controller.LossyFilePopupController;
import paint.controller.SavePopupController;
import paint.draw.*;
import paint.draw.Selection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Stack;

/**
 * A utility class intended to make working with the canvas easier
 * @author Colin Braun
 */
public class CanvasManager {
    /**
     * The canvas that this will do work on
     */
    private Canvas canvas;
    /**
     * The draw mode the canvas is in
     */
    private ToolMode toolMode;
    /**
     * The canvas' GraphicsContext, avoids constantly having to call canvas.getGraphicsContext2D()
     */
    private GraphicsContext context;
    /**
     * The item being currently drawn (I.E. a line)
     */
     private Drawable currentDrawing;
    /**
     * The primary color (left click)
     */
    private Paint primaryColor;
    /**
     * The secondary color (right click)
     */
    private Paint secondaryColor;
    /**
     * Used to facilitate previewing effects before they actually apply to the image.
     */
    private Image redrawImage;
    /**
     * The file that is loaded onto the canvas, if any
     */
    private File openedFile;
    /**
     * True if a change has been made that hasn't saved.
     */
    private boolean changeMadeNotSaved;
    /**
     * A stack containing images to load when an undo is requested
     */
    private Stack<Image> undoStack;
    /**
     * A stack containing images to load when a redo is request
     */
    private Stack<Image> redoStack;
    /**
     * The text that will be drawn
     */
    private String drawText;
    /**
     * The number of sides to draw the polygon with when the Polygon tool is selected
     */
    private int polygonSides;
    /**
     * Whether or not a selection has been made yet (is it time to move it?)
     */
    private boolean selectionMade;
    /**
     * A copy of what the canvas looks like right before a selection is made (for the sake of undoing)
     */
    private Image preSelectImage;
    /**
     * Whether or not ctrl-c has been pressed or not
     */
    private boolean ctrl_c_pressed;
    /**
     * Whether or not the tool has been changed recently. (should be set to false externally using #setToolChanged)
     */
    private boolean toolChanged;

    /**
     * Default Constructor
     * @param canvas the {@link Canvas} that this class will manage
     */
    public CanvasManager(@NotNull Canvas canvas) {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        this.canvas = canvas;
        primaryColor = Color.BLACK;
        secondaryColor = Color.WHITE;
        context = canvas.getGraphicsContext2D();
        initEvents();
        redrawImage = canvas.snapshot(null, null);
        redraw();
    }

    /**
     * Initialize how all the events for the canvas are handled. For internal use.
     */
    private void initEvents() {
        // Handle mouse pressed event
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, (event) -> {
            if(toolMode == null)
                return;
            if(event.isSecondaryButtonDown()) {
                context.setFill(secondaryColor);
                context.setStroke(secondaryColor);
            }
            else {
                context.setFill(primaryColor);
                context.setStroke(primaryColor);
            }


            if(!selectionMade)
                redrawImage = canvas.snapshot(null, null);

            switch(toolMode) {
                case LINE:
                    currentDrawing = new Line(event.getX(), event.getY());
                    break;
                case ELLIPSE:
                    currentDrawing = new Ellipse(event.getX(), event.getY());
                    break;
                case RECTANGLE:
                    currentDrawing = new Rectangle(event.getX(), event.getY());
                    break;
                case CIRCLE:
                    currentDrawing = new Circle(event.getX(), event.getY());
                    break;
                case SQUARE:
                    currentDrawing = new Square(event.getX(), event.getY());
                    break;
                case TRIANGLE:
                    currentDrawing = new Triangle(event.getX(), event.getY());
                    break;
                case PENCIL:
                    currentDrawing = new FreeDraw(event.getX(), event.getY());
                    break;
                case TEXT:
                    currentDrawing = new Text(event.getX(), event.getY(), drawText);
                    break;
                case ERASER:
                    currentDrawing = new Eraser(event.getX(), event.getY());
                    break;
                case POLYGON:
                    currentDrawing = new Polygon(event.getX(), event.getY(), polygonSides);
                    break;
                case SELECT:
                    if(!selectionMade) {
                        preSelectImage = canvas.snapshot(null, null);
                        currentDrawing = new Selection(event.getX(), event.getY());
                    }
                    else {
                        Selection selection = (Selection)currentDrawing;
                        if(selection.isInSelection(event.getX(), event.getY())) {
                            selection.setGrabbed(true);
                            selection.setMouseX(event.getX());
                            selection.setMouseY(event.getY());
                            selection.setGrabbedX(event.getX());
                            selection.setGrabbedY(event.getY());
                            // If ctrl-c is not pressed, we are cutting (setting background of selected area to white).
                            if(!ctrl_c_pressed) {
                                PixelWriter pixelWriter = ((WritableImage) redrawImage).getPixelWriter();
                                for (int i = (int) selection.getXTopLeft(); i < (int) (selection.getXTopLeft() + selection.getWidth()); i++) {
                                    for (int j = (int) selection.getYTopLeft(); j < (int) (selection.getYTopLeft() + selection.getHeight()); j++) {
                                        pixelWriter.setArgb(i, j, 0xFFFFFFFF);
                                    }
                                }
                            }
                            // Whether ctrl-c was pressed or not, redraw.
                            redraw();
                            selection.drawPreview(context);
                        }
                        else {
                            selectionMade = false;
                            currentDrawing = new Selection(event.getX(), event.getY());
                        }
                    }
                    break;
                case COLOR_PICKER:
                    Color color = redrawImage.getPixelReader().getColor((int)event.getX(), (int)event.getY());
                    Main.mainController.getColorPicker().setValue(color);
                    setPrimaryColor(color);
                    break;
                }
            changeMadeNotSaved = true;
        });

        //Handle mouse dragged event (button held down and moved)
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            if(toolMode == null)
                return;
            // For special cases
            switch(toolMode) {
                case COLOR_PICKER:
                    return;
                case SELECT:
                    Selection selection = (Selection)currentDrawing;
                    if(!selectionMade) {
                        selection.setEnd(event.getX(), event.getY());
                        redraw();
                        selection.drawPreview(context);
                        return;
                    }
                    else if(selection.isGrabbed()) {
                        selection.setMouseX(event.getX());
                        selection.setMouseY(event.getY());
                        redraw();
                        selection.drawPreview(context);
                    }
                    return;
            }

            // This will work for your TYPICAL Drawables. It may happen that something special need be done.
            redraw();
            currentDrawing.setEnd(event.getX(), event.getY());
            currentDrawing.drawPreview(context);
            //changeMadeNotSaved = true;
    });

        //Handle mouse released event
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            if(toolMode == null)
                return;
            // For special cases
            switch(toolMode) {
                case COLOR_PICKER:
                    return;
                case SELECT:
                    Selection selection = (Selection)currentDrawing;
                    if(!selectionMade) {
                        redraw();
                        selectionMade = true;
                        selection.setEnd(event.getX(), event.getY());
                        SnapshotParameters params = new SnapshotParameters();
                        params.setViewport(new Rectangle2D(selection.getXTopLeft(), selection.getYTopLeft(), selection.getWidth(), selection.getHeight()));
                        selection.setSelection(canvas.snapshot(params, null));
                        selection.drawPreview(context);
                        // In case it was not reset somewhere else.
                        ctrl_c_pressed = false;
                        return;
                    }
                    else if(selection.isGrabbed()) {
                        // This is not working properly
                        undoStack.add(preSelectImage);
                        redraw();
                        selection.drawFinal(context);
                        selection.setGrabbed(false);
                        redrawImage = canvas.snapshot(null, null);
                        selectionMade = false;
                        return;
                    }
                    break;
            }

            // This will work for your TYPICAL Drawables. It may happen that something special need be done.
            undoStack.add(redrawImage);
            redraw();
            currentDrawing.setEnd(event.getX(), event.getY());
            currentDrawing.drawFinal(context);
            redrawImage = canvas.snapshot(null, null);
            changeMadeNotSaved = true;
        });
    }

    /**
     * Invert the image on the canvas
     */
    public void invert() {
        PixelWriter writer = ((WritableImage)redrawImage).getPixelWriter();
        PixelReader reader = redrawImage.getPixelReader();

        undoStack.add(canvas.snapshot(null, null));
        for(int i = 0; i < redrawImage.getWidth(); i++) {
            for(int j = 0; j < redrawImage.getHeight(); j++) {
                int value = reader.getArgb(i, j);
                int a = (value >> 24) & 0xFF;
                int r = (value >> 16) & 0xFF;
                int g = (value >> 8) & 0xFF;
                int b = value & 0xFF;
                int newA = a;
                int newR = 255 - r;
                int newG = 255 - g;
                int newB = 255 - b;
                newA = newA << 24;
                newR = newR << 16;
                newG = newG << 8;
                int result = newA + newR + newG + newB;

                writer.setArgb(i, j, result);
            }
        }
        redraw();
    }

    /**
     * Set the zoom level on the canvas
     * @param zoom the zooming level in percent
     */
    public void setZoom(int zoom) {
        Parent parent = canvas.getParent();
        if(parent.getTransforms().size() == 0)
            parent.getTransforms().add(new Scale(zoom/100.0, zoom/100.0));
        else
            parent.getTransforms().set(0, new Scale(zoom/100.0, zoom/100.0));
    }

    /**
     * Set the draw mode for the canvas
     * @param mode the draw mode to set the canvas to
     */
    public void setToolMode(ToolMode mode) {
        this.toolMode = mode;
        toolChanged = true;
    }

    /**
     * Get the currently selected {@link ToolMode}
     * @return the selected tool mode
     */
    public ToolMode getToolMode() {
        return toolMode;
    }

    /**
     * Set the width of the line being drawn
     * @param width the chosen line width
     */
    public void setLineWidth(double width) {
        context.setLineWidth(width);
    }
    /**
     * Set the color to draw with
     * @param color the secondary color to draw with
     */
    public void setPrimaryColor(Paint color) {
        this.primaryColor = color;
    }

    /**
     * Set the secondary color to draw with
     * @param color the primary color to draw with
     */
    public void setSecondaryColor(Paint color) {
        this.secondaryColor = color;
    }

    /**
     * Get the primary color (left mouse button color) that is selected
     * @return the {@link Paint} object of the currently selected primary color
     */
    public Paint getPrimaryColor() {
        return primaryColor;
    }

    /**
     * Set the font that will be drawn with
     * @param font the font
     */
    public void setTextFont(Font font) {
        context.setFont(font);
    }

    /**
     * Set the text that will be drawn
     * @param text the text to be drawn
     */
    public void setDrawText(String text) {
        drawText = text;
    }

    /**
     * Set the number of sides when drawing a generic polygon
     * @param sides the number of sides of the polygon
     */
    public void setPolygonSides(int sides) {
        polygonSides = sides;
    }

    /**
     * Set whether or not a selection has been made or not (should be internal use only)
     * @param selectionMade whether or not a selection is made
     */
    public void setSelectionMade(boolean selectionMade) {
        this.selectionMade = selectionMade;
    }

    /**
     * Get whether or not the selected tool has changed
     * @return whether or not the selected tool has changed
     */
    public boolean isToolChanged() {
        return toolChanged;
    }

    /**
     * Set the currently selected tool to not have been changed. Internal use only
     */
    public void setToolNotChanged() {
        toolChanged = false;
    }

    /**
     * Set whether or not ctrl-c is pressed or not. This is only called in {@link Main}.
     * It should NOT be called anywhere else.
     * This does not need to be set to false, it will be done automatically.
     * @param ctrl_c_pressed whether or not ctrl-c is pressed
     */
    public void setCtrl_c_pressed(boolean ctrl_c_pressed) {
        this.ctrl_c_pressed = ctrl_c_pressed;
    }

    /**
     * Undo the last change to the canvas
     */
    public void undo() {
        if(undoStack.isEmpty())
            return;
        redoStack.push(redrawImage);
        redrawImage = undoStack.pop();
        redraw();
    }

    /**
     * Redo the last undo
     */
    public void redo() {
        if(redoStack.isEmpty())
            return;
        undoStack.push(redrawImage);
        redrawImage = redoStack.pop();
        redraw();
    }

    /**
     * Redraw the canvas based on what's stored in the redrawImage field. Internal use only
     */
    private void redraw() {
        context.drawImage(redrawImage, 0, 0, redrawImage.getWidth(), redrawImage.getHeight());
    }

    /**
     * Get the canvas that this CanvasManager is managing
     * @return the canvas
     */
    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * Get the currently opened file
     * @return the currently opened file
     */
    public File getOpenedFile() {
        return openedFile;
    }

    /**
     * Get whether or not a change has been made and a save is required
     * @return whether ot not changes has been saved
     */
    public boolean isChangeMadeNotSaved() {
        return changeMadeNotSaved;
    }

    /**
     * Load an image onto the canvas from a file
     * @param imageFile The file to be loaded
     */
    public void loadImageFromFile(@NotNull File imageFile) {
        if(changeMadeNotSaved) {
            if(showSavePopup() == SaveChoice.CANCEL)
                return;
            changeMadeNotSaved = false;
        }
        Image image = null;
        try {
            // TODO: Do this better. Not great to use the initial window size to determine the image's size. Try to make more dynamic.
            image = new Image(new FileInputStream(imageFile.getAbsolutePath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        openedFile = imageFile;
        loadImage(image);
    }

    /**
     * Load an image onto the canvas from an Image object
     * @param image the image to load onto the canvas
     */
    public void loadImage(Image image) {
        canvas.setHeight(image.getHeight());
        canvas.setWidth(image.getWidth());
        context.drawImage(image, 0, 0, image.getWidth(), image.getHeight());
        redrawImage = canvas.snapshot(null, null);
    }

    /**
     * Save the contents of the canvas to a file
     * @param file The file to be saved to
     */
    public void saveCanvasToFile(@NotNull File file) {
        WritableImage image = canvas.snapshot(null, null);
        // Tried this approach, but this doesn't work. Had to make my own BufferedImage
        //BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        int w = (int)image.getWidth();
        int h = (int)image.getHeight();
        int[] buffer = new int[w*h];
        BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        image.getPixelReader().getPixels(0, 0, w, h, WritablePixelFormat.getIntArgbInstance(), buffer, 0, w);
        newImage.setRGB(0, 0, w, h, buffer, 0, w);
        try {
            System.out.println("Extension: " + getFileExtension(file));
            // If opened file is same as this one, ignore loss of data problem
            if(openedFile != null && openedFile == file)
                System.out.println(ImageIO.write(newImage, getFileExtension(file), file));
            // Else if it's jpg (can add other types if needed), warn user before saving
            else if(getFileExtension(file).equals("jpg")) {
                if(showLossyPopup() == 1)
                    System.out.println(ImageIO.write(newImage, getFileExtension(file), file));
                else
                    return;
            }
            // Else it's a new file that isn't of lossy format, save with no prompt
            else {
                System.out.println(ImageIO.write(newImage, getFileExtension(file), file));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        openedFile = file;
        changeMadeNotSaved = false;
    }

    /**
     * Display the save window and wait.
     * Note that when YES is selected, SAVING IS HANDLED BY THE SAVE POPUP CONTROLLER
     * @return The {@link SaveChoice} that was made.
     */
    public SaveChoice showSavePopup() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/paint/fxml/save_popup.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SavePopupController controller = (SavePopupController) loader.getController();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Save");
        stage.setResizable(false);
        controller.setStage(stage);
        stage.showAndWait();
        return controller.getChoice();
    }

    /**
     * Show the lossy popup and wait for a response
     * @return 1 if Yes was selected, 0 if No was selected
     */
    public int showLossyPopup() {
        System.out.println("Showing popup");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/paint/fxml/lossy_file_popup.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LossyFilePopupController controller = (LossyFilePopupController)loader.getController();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Save");
        stage.setResizable(false);
        controller.setStage(stage);
        stage.showAndWait();
        return controller.getChoice();
    }

    /**
     * Takes an image (intended to be from a snapshot) and sends it to a file
     * @param image the image to send to a file
     */
    public void sendSnapShotToNewFile(Image image) {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        File file = new File("test.png");
        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to find a file's extension
     * @param file the file whose extension will be found
     * @return the file's extension as a {@link String}
     */
    private static String getFileExtension(File file) {
        int index = file.getName().lastIndexOf('.');
        if(index > 0 && index < file.getName().length())
            return file.getName().substring(index + 1);
        System.out.println("Could not find file extension");
        return "";
    }
}
