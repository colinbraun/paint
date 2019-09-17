package paint.controller;

import javafx.fxml.Initializable;
import paint.Main;
import paint.util.CanvasManager;

import java.net.URL;
import java.util.ResourceBundle;

public class BaseController implements Initializable {

    /**
     * Most controllers will want access to the canvas manager.
     * This means they must call super.initialize() to have a copy of it
     */
    protected CanvasManager canvasManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        canvasManager = Main.mainController.getCanvasManager();
    }
}
