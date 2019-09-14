package paint.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import paint.Main;
import paint.util.CanvasManager;

import java.net.URL;
import java.util.ResourceBundle;

public class ResizeController extends BaseController {

    /**
     * Field that holds the user's chosen horizontal resizing
     */
    @FXML TextField horizontalField;
    /**
     * Field that holds the user's chosen vertical resizing
     */
    @FXML TextField verticalField;
    private CanvasManager canvasManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        canvasManager = Main.mainController.getCanvasManager();
        horizontalField.textProperty().setValue("" + (int)canvasManager.getCanvas().getWidth());
        verticalField.textProperty().setValue("" + (int)canvasManager.getCanvas().getHeight());

        // Init TextField listeners
        horizontalField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Prevent non-numeric values from being entered
            if(newValue.matches(".*\\D.*")) {
                horizontalField.textProperty().setValue(oldValue);
            }
        });
        verticalField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Prevent non-numeric values from being entered
            if(newValue.matches(".*\\D.*")) {
                verticalField.textProperty().setValue(oldValue);
            }
        });
    }
}
