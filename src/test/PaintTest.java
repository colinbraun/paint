package test;

import javafx.application.Platform;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import paint.Main;
import paint.constant.ToolMode;
import paint.util.CanvasManager;

import static org.junit.jupiter.api.Assertions.*;

class PaintTest {

    private static CanvasManager canvasManager;
    private static Stage stage;

    @BeforeAll
    static void setUp() {
        System.out.println("Set up tests");
        Main.main(null);
        System.out.println("Here");
        canvasManager = Main.mainController.getCanvasManager();
        stage = Main.mainStage;
        System.out.println("Finish set up for tests");
    }


    @AfterAll
    static void tearDown() {
        System.out.println("End tests");
        Platform.runLater(() -> {
            stage.close();
        });
        System.out.println("Finish ending tests");
    }

    @Test
    void test1() {
        System.out.println("Start test 1");
        canvasManager.setToolMode(ToolMode.SQUARE);
        Platform.runLater(() -> {

        });
        System.out.println("End test 1");
    }

    @Test
    void test2() {

    }

    @Test
    void test3() {

    }
}