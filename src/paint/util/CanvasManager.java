package paint.util;

import com.sun.istack.internal.NotNull;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import paint.Main;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A utility class intended to make working with the canvas easier
 */
public class CanvasManager {
    /**
     * The canvas that this will do work on
     */
    private Canvas canvas;

    public CanvasManager(Canvas canvas) {
        this.canvas = canvas;
    }

    /**
     * Load an image onto the canvas from a file
     * @param imageFile The file to be loaded
     */
    public void loadImageFromFile(@NotNull File imageFile) {
        Image image = null;
        try {
            // TODO: Do this better. Not great to use the initial window size to determine the image's size. Try to make more dynamic.
            image = new Image(new FileInputStream(imageFile.getAbsolutePath()), Main.WIDTH * 0.8, Main.HEIGHT * 0.8, true, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        canvas.setHeight(image.getHeight());
        canvas.setWidth(image.getWidth());
        canvas.getGraphicsContext2D().drawImage(image, 0, 0, image.getWidth(), image.getHeight());
    }

    /**
     * Save the contents of the canvas to a file
     * @param file The file to be saved to
     */
    public void saveCanvasToFile(@NotNull File file) {
        WritableImage image = canvas.snapshot(new SnapshotParameters(), null);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
