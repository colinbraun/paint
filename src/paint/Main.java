package paint;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

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
     * Enable this for println debugging (not many)
     */
    public static boolean DEBUG = false;

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
        stage.show();
    }
}
