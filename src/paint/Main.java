package paint;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import paint.constant.SaveChoice;
import paint.controller.MainController;

public class Main extends Application {

    /**
     * The initial width of the scene
     */
    public static final int WIDTH = 900;
    /**
     * The initial height of the scene
     */
    public static final int HEIGHT = 700;
    /**
     * The minimum width of the stage
     */
    public static final int MIN_WIDTH = 200;
    /**
     * The minimum height of the stage
     */
    public static final int MIN_HEIGHT = 200;
    /**
     * A reference to the main controller currently in use.
     */
    public static MainController mainController;
    public static Stage mainStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // TODO: Fix so only paint_app.fxml needs to be passed
        Parent root = FXMLLoader.load(getClass().getResource("/paint/fxml/paint_app.fxml"));
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            // Check to see if ctrl-c is pressed
            if(event.isControlDown() && event.getCode() == KeyCode.C)
                mainController.getCanvasManager().setCtrl_c_pressed(true);
        });
        stage.setTitle("Paint - CS250");
        stage.setScene(scene);
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        // Note: This is NOT triggered by stage.close().
        // To trigger this event manually, use Stage#fireEvent()
        stage.setOnCloseRequest((event) -> {
            if(mainController.getCanvasManager().isChangeMadeNotSaved()) {
                if(mainController.getCanvasManager().showSavePopup() == SaveChoice.CANCEL)
                    event.consume();
            }
        });
        mainStage = stage;
        stage.show();
    }
}
