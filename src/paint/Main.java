package paint;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import paint.controller.MainController;

public class Main extends Application {

    /**
     * The initial width of the scene
     */
    public static final int WIDTH = 800;
    /**
     * The initial height of the scene
     */
    public static final int HEIGHT = 600;
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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // TODO: Fix so only paint_app.fxml needs to be passed
        Parent root = FXMLLoader.load(getClass().getResource("/paint/fxml/paint_app.fxml"));
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setTitle("Paint - CS250");
        stage.setScene(scene);
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.show();
    }
}
