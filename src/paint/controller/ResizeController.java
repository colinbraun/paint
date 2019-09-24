package paint.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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

    /**
     * Runs when the resize button is clicked
     */
    @FXML
    public void handleResizeButton() {
        WritableImage image = canvasManager.getCanvas().snapshot(null, null);
        PixelReader pixelReader = image.getPixelReader();
        if(pixelReader == null)
            return;

        int oldWidth = (int)image.getWidth();
        int oldHeight = (int)image.getHeight();
        int newWidth = getTextFieldValue(horizontalField);
        int newHeight = getTextFieldValue(verticalField);

        WritableImage newImage = new WritableImage(newWidth, newHeight);
        PixelWriter pixelWriter = newImage.getPixelWriter();
        for(int newY = 0; newY < newHeight; newY++) {
            int oldY = (int)Math.round((double)newY / newHeight * oldHeight);
            for(int newX = 0; newX < newWidth; newX++) {
                int oldX = (int)Math.round((double)newX / newWidth * oldWidth);
                if(oldX >= oldWidth)
                    oldX--;
                if(oldY >= oldHeight)
                    oldY--;
                pixelWriter.setArgb(newX, newY, pixelReader.getArgb(oldX, oldY));
            }
        }
        canvasManager.loadImage(newImage);
        ((Stage)horizontalField.getScene().getWindow()).close();
    }

    private int getTextFieldValue(TextField field) {
        try {
            return Integer.parseInt(field.textProperty().getValue());
        } catch(NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
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
