package paint;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import paint.constant.ToolMode;
import paint.controller.MainController;
import paint.util.CanvasManager;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest extends ApplicationTest {

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

    private CanvasManager canvasManager;
    private MainController controller;

    @Override
    public void start(Stage stage) throws Exception {
        // TODO: Fix so only paint_app.fxml needs to be passed
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/paint/fxml/paint_app.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        canvasManager = controller.getCanvasManager();

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setTitle("Paint - CS250");
        stage.setScene(scene);
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        // Note: This is NOT triggered by stage.close().
        // To trigger this event manually, use Stage#fireEvent()
        stage.show();
    }

    @Before
    public void setup() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void test1() {
        canvasManager.setLineWidth(3);
        assertEquals(3.0, canvasManager.getCanvas().getGraphicsContext2D().getLineWidth());
    }

    @Test
    public void test2() {
        Platform.runLater(() -> controller.getColorPicker().setValue(Color.GREEN));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        controller.handleColorPicker();
        assertTrue(controller.getColorPicker().getValue().equals(canvasManager.getPrimaryColor()));
    }

    @Test
    public void test3() {
        canvasManager.setToolMode(ToolMode.LINE);
        assertEquals(ToolMode.LINE, canvasManager.getToolMode());
    }
}