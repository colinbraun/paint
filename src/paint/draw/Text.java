package paint.draw;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

/**
 * A drawable Text object
 * @author Colin Braun
 */
public class Text extends Drawable {

    /**
     * The text to be drawn
     */
    private String text;

    /**
     * Construct the drawable Text
     * @param x the left-most part of where to the draw text
     * @param y the top-most part of where to draw the text
     * @param text the text to be drawn
     */
    public Text(double x, double y, String text) {
        super(x, y, x, y);
        this.text = text;
    }

    /**
     * Draw the Text
     * @param context the context used to draw
     */
    @Override
    public void drawFinal(GraphicsContext context) {
        context.fillText(text, x1, y1);
    }
}
