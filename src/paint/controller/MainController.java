package paint.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import paint.util.CanvasManager;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    /**
     * The canvas that will have the image on it
     */
    @FXML private Canvas canvas;
    /**
     * The menuBar across the top of the screen
     */
    @FXML private MenuBar menuBar;
    /**
     * The currently open file
     */
    private File openedFile;
    /**
     * A manager that handles canvas operations
     */
    private CanvasManager canvasManager;
    /**
     * The fileChooser used to open and save files
     */
    private FileChooser fileChooser;
    /**
     * The colorPicker used to choose the color to draw with
     */
    @FXML private ColorPicker colorPicker;
    /**
     * Toggle button to toggle drawing lines
     */
    @FXML private ToggleButton toggleDrawLine;

    /**
     * Runs when File -> Open is clicked
     */
    @FXML
    public void handleOpen() {
        File chosenFile = fileChooser.showOpenDialog(null);
        if(chosenFile == null)
            return;
        openedFile = chosenFile;
        canvasManager.loadImageFromFile(openedFile);
    }

    /**
     * Runs when File -> Save is clicked
     */
    @FXML
    public void handleSave() {
        if(openedFile == null) {
            handleSaveAs();
            return;
        }
        canvasManager.saveCanvasToFile(openedFile);
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
        // TODO: Reassess if this is the best way to close the window
        Stage stage = (Stage)canvas.getScene().getWindow();
        stage.close();
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colorPicker.setValue(Color.BLACK);
        fileChooser = new FileChooser();
        // TODO: implement this for more file types and without hardcoded values
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files (*.png)", "*.png"));
        canvasManager = new CanvasManager(canvas);
    }
}
