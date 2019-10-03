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
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
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
     * The currently selected color
     */
    private Paint selectedColor;
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

    public CanvasManager(@NotNull Canvas canvas) {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        this.canvas = canvas;
        selectedColor = Color.BLACK;
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

                            PixelWriter pixelWriter = ((WritableImage)redrawImage).getPixelWriter();
                            for(int i = (int)selection.getXTopLeft(); i < (int)(selection.getXTopLeft()+selection.getWidth()); i++) {
                                for(int j = (int)selection.getYTopLeft(); j < (int)(selection.getYTopLeft()+selection.getHeight()); j++) {
                                    pixelWriter.setArgb(i, j, 0xFFFFFFFF);
                                }
                            }
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
                    setSelectedColor(color);
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
     * Set the zoom level on the canvas (in percent)
     * @param zoom
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
     * @param color the color to draw with
     */
    public void setSelectedColor(Paint color) {
        this.selectedColor = color;
        context.setFill(color);
        context.setStroke(color);
    }

    public void setTextFont(Font font) {
        context.setFont(font);
    }

    public void setDrawText(String text) {
        drawText = text;
    }

    public void setPolygonSides(int sides) {
        polygonSides = sides;
    }

    public void setSelectionMade(boolean selectionMade) {
        this.selectionMade = selectionMade;
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
     * @return The SaveChoice that was made.
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

    private static String getFileExtension(File file) {
        int index = file.getName().lastIndexOf('.');
        if(index > 0 && index < file.getName().length())
            return file.getName().substring(index + 1);
        System.out.println("Could not find file extension");
        return "";
    }
}
