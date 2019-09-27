package paint.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import paint.Main;
import paint.constant.ToolMode;
import paint.popup.FieldPopup;
import paint.util.CanvasManager;
import paint.util.ToggleGroup;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController extends BaseController {

    /**
     * The canvas that will have the image on it
     */
    @FXML private Canvas canvas;
    /**
     * The menuBar across the top of the screen
     */
    @FXML private MenuBar menuBar;
    /**
     * A manager that handles canvas operations
     */
    private CanvasManager canvasManager;
    /**
     * The fileChooser used to open and save files
     */
    private FileChooser fileChooser;

    public ColorPicker getColorPicker() {
        return colorPicker;
    }

    /**
     * The colorPicker used to choose the color to draw with
     */
    @FXML private ColorPicker colorPicker;
    /**
     * Toggle buttons to handle tool modes
     */
    @FXML private ToggleButton toggleDrawLine;
    @FXML private ToggleButton toggleDrawEllipse;
    @FXML private ToggleButton toggleDrawRectangle;
    @FXML private ToggleButton toggleDrawSquare;
    @FXML private ToggleButton toggleDrawCircle;
    @FXML private ToggleButton toggleColorPicker;
    @FXML private ToggleButton togglePencil;
    @FXML private ToggleButton toggleText;
    @FXML private ToggleButton toggleEraser;
    @FXML private ToggleButton togglePolygon;
    @FXML private ToggleButton toggleSelect;
    @FXML private ToggleButton toggleDrawTriangle;
    private ToggleGroup tools;
    @FXML private HBox toolBarRow1;
    /**
     * Zoom field. Requires pressing enter to apply change
     */
    @FXML private TextField zoomField;
    /**
     * Control for font when drawing text
     */
    @FXML private ComboBox<String> fontChooser;
    /**
     * Slider that controls the width of drawn lines/shapes
     */
    @FXML private Slider lineWidthSlider;
    @FXML private Label lineWidthLabel;
    public MainController() {
        tools = new ToggleGroup();
        Main.mainController = this;
    }

    public CanvasManager getCanvasManager() {
        return canvasManager;
    }

    /**
     * Runs when File -> Open is clicked
     */
    @FXML
    public void handleOpen() {
        File chosenFile = fileChooser.showOpenDialog(null);
        if(chosenFile == null)
            return;
        canvasManager.loadImageFromFile(chosenFile);
    }

    /**
     * Runs when File -> Save is clicked
     */
    @FXML
    public void handleSave() {
        if(canvasManager.getOpenedFile() == null) {
            handleSaveAs();
            return;
        }
        canvasManager.saveCanvasToFile(canvasManager.getOpenedFile());
    }

    /**
     * Runs when File -> Save is clicked
     */
    @FXML
    public void handleSaveAs() {
        File file = fileChooser.showSaveDialog(null);
        if(file == null)
            return;
        canvasManager.saveCanvasToFile(file);
    }

    /**
     * Runs when File -> Exit is clicked
     */
    @FXML
    public void handleExit() {
        Stage stage = (Stage)canvas.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    /**
     * Runs when Help -> About is clicked
     */
    @FXML
    public void handleAbout() {
        Stage stage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/paint/fxml/help.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("About");
        stage.show();
    }

    /**
     * Runs when something happens to the color picker
     */
    @FXML
    public void handleColorPicker() {
        canvasManager.setSelectedColor(colorPicker.getValue());
    }

    /**
     * Runs when the line ToggleButton is clicked
     */
    @FXML
    public void handleToggleDrawLine() {
        tools.unToggleAllBut(toggleDrawLine);
        if(toggleDrawLine.isSelected())
            canvasManager.setToolMode(ToolMode.LINE);
        else
            canvasManager.setToolMode(null);
    }

    /**
     * Runs when the ellipse ToggleButton is clicked
     */
    @FXML
    public void handleToggleDrawEllipse() {
        tools.unToggleAllBut(toggleDrawEllipse);
        if(toggleDrawEllipse.isSelected())
            canvasManager.setToolMode(ToolMode.ELLIPSE);
        else
            canvasManager.setToolMode(null);
    }

    /**
     * Runs when the rectangle ToggleButton is clicked
     */
    @FXML
    public void handleToggleDrawRectangle() {
        tools.unToggleAllBut(toggleDrawRectangle);
        if(toggleDrawRectangle.isSelected())
            canvasManager.setToolMode(ToolMode.RECTANGLE);
        else
            canvasManager.setToolMode(null);
    }

    /**
     * Runs when the square ToggleButton is clicked
     */
    @FXML
    public void handleToggleDrawSquare() {
        tools.unToggleAllBut(toggleDrawSquare);
        if(toggleDrawSquare.isSelected())
            canvasManager.setToolMode(ToolMode.SQUARE);
        else
            canvasManager.setToolMode(null);
    }

    /**
     * Runs when the circle ToggleButton is clicked
     */
    @FXML
    public void handleToggleDrawCircle() {
        tools.unToggleAllBut(toggleDrawCircle);
        if(toggleDrawCircle.isSelected())
            canvasManager.setToolMode(ToolMode.CIRCLE);
        else
            canvasManager.setToolMode(null);
    }

    /**
     * Runs when the color picker (dropper) ToggleButton is clicked
     */
    @FXML
    public void handleToggleColorPicker() {
        tools.unToggleAllBut(toggleColorPicker);
        if(toggleColorPicker.isSelected())
            canvasManager.setToolMode(ToolMode.COLOR_PICKER);
        else
            canvasManager.setToolMode(null);
    }

    /**
     * Runs when the pencil ToggleButton is clicked
     */
    @FXML
    public void handleTogglePencil() {
        tools.unToggleAllBut(togglePencil);
        if(togglePencil.isSelected())
            canvasManager.setToolMode(ToolMode.PENCIL);
        else
            canvasManager.setToolMode(null);
    }

    /**
     * Runs when the text ToggleButton is clicked
     */
    @FXML
    public void handleToggleText() {
        tools.unToggleAllBut(toggleText);
        if(toggleText.isSelected()) {
            FieldPopup popup = new FieldPopup("Text");
            TextField textField = popup.addField("Text: ");
            TextField sizeField = popup.addField("Font size: ", "12");
            ComboBox<String> comboBox = (ComboBox)popup.addComponent(fontChooser);
            popup.addSubmitButton("Submit");
            popup.showAndWait();
            try {
                canvasManager.setTextFont(new Font(comboBox.getValue(), Integer.parseInt(sizeField.getText())));
                canvasManager.setDrawText(textField.getText());
            } catch(Exception e) {return;}
            canvasManager.setToolMode(ToolMode.TEXT);
        }
        else
            canvasManager.setToolMode(null);
    }

    /**
     * Runs when the eraser ToggleButton is clicked
     */
    @FXML
    public void handleToggleEraser() {
        tools.unToggleAllBut(toggleEraser);
        if(toggleEraser.isSelected())
            canvasManager.setToolMode(ToolMode.ERASER);
        else
            canvasManager.setToolMode(null);
    }

    @FXML
    public void handleTogglePolygon() {
        tools.unToggleAllBut(togglePolygon);
        if(togglePolygon.isSelected()) {
            FieldPopup popup = new FieldPopup("Polygon");
            TextField field = popup.addField("Num Sides: ", "3");
            canvasManager.setPolygonSides(Integer.parseInt(field.getText()));
            field.textProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue.matches(".*\\D.*")) {
                    field.textProperty().setValue(oldValue);
                }
                else if(!newValue.equals("")) {
                    canvasManager.setPolygonSides(Integer.parseInt(newValue));
                }
            });
            popup.addSubmitButton("Submit");
            popup.showAndWait();
            canvasManager.setToolMode(ToolMode.POLYGON);
        }
        else
            canvasManager.setToolMode(null);
    }

    @FXML
    public void handleToggleSelect() {
        tools.unToggleAllBut(toggleSelect);
        if(toggleSelect.isSelected()) {
            canvasManager.setToolMode(ToolMode.SELECT);
            canvasManager.setSelectionMade(false);
        }
        else
            canvasManager.setToolMode(null);
    }

    @FXML
    public void handleToggleDrawTriangle() {
        tools.unToggleAllBut(toggleDrawTriangle);
        if(toggleDrawTriangle.isSelected()) {
            canvasManager.setToolMode(ToolMode.TRIANGLE);
        }
        else
            canvasManager.setToolMode(null);
    }

    /**
     * Runs when Edit -> Resize is clicked
     * Note that all the button handling in the resize window happens in {@link ResizeController}
     */
    @FXML
    public void handleResize() {
        Stage stage = new Stage();
        Parent root = null;
            try {
            root = FXMLLoader.load(getClass().getResource("/paint/fxml/resize.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Resize Canvas");
        stage.setResizable(false);
        stage.showAndWait();
    }

    /**
     * Runs when Edit -> Undo is clicked
     */
    @FXML
    public void handleUndo() {
        canvasManager.undo();
    }

    /**
     * Runs when Edit -> Redo is clicked
     */
    @FXML
    public void handleRedo() {
        canvasManager.redo();
    }

    /**
     * Runs when enter is hit while the zoom field is selected
     */
    @FXML
    public void handleZoomField() {
        canvasManager.setZoom(Integer.parseInt(zoomField.textProperty().getValue()));
    }

    // This will run AFTER all the component fields have been initialized, unlike the constructor
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lineWidthSlider.valueProperty().addListener((event) -> canvasManager.setLineWidth(lineWidthSlider.getValue()));
        colorPicker.setValue(Color.BLACK);
        fileChooser = new FileChooser();
        // TODO: implement this for more file types and without hardcoded values
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files (*.png)", "*.png"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("BMP Files (*.bmp", "*.bmp"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG Files (*.jpg)", "*.jpg"));
        // Get all the toolbar toggleables
        for(Node node : findRootChildrenInPane(toolBarRow1)) {
            if(node instanceof ToggleButton)
                tools.addToggles((ToggleButton)node);
        }
        canvasManager = new CanvasManager(canvas);


        // Set up zoom value listener
        zoomField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Prevent non-numeric values from being entered
            if(newValue.matches(".*\\D.*")) {
                zoomField.textProperty().setValue(oldValue);
            }
        });

        fontChooser = new ComboBox<>();
        fontChooser.getItems().setAll(Font.getFamilies());
        fontChooser.setValue("Comic Sans MS");
    }

    /**
     * Find all of the non-pane children of the given pane
     * @param pane the pane to find the children of
     * @param <T> W.I.P. Intended to find a specific type of child. Currently only returns a list of {@link Node}
     * @return
     */
    public static <T extends Node> List<T> findRootChildrenInPane(Pane pane) {
        List<T> list = new ArrayList<>();
        findRootChildrenInPaneOfTypeHelper(pane, list);
        return list;
    }

    // Internal helper method for findRootChildrenInPane
    private static <T extends Node> void findRootChildrenInPaneOfTypeHelper(Pane pane, List<T> out) {
        for(Node node : pane.getChildren()) {
            if(node instanceof Pane) {
                findRootChildrenInPaneOfTypeHelper((Pane)node, out);
            }
            else {
                try {
                    T t = (T)node;
                    out.add(t);
                } catch(ClassCastException e) {
                    // This is expected to happen some times
                }
            }
        }
    }
}
