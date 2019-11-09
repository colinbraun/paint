package paint.draw;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * A drawable Eraser
 * @author Colin Braun
 */
public class Eraser extends FreeDraw {

    /**
     * Construct a drawable Eraser
     * @param x the starting x of the erase draw
     * @param y the starting y of the erase draw
     */
    public Eraser(double x, double y) {
        super(x, y);
    }

    /**
     * Draw (erase) given the {@link GraphicsContext}
     * @param context the context to draw with
     */
    @Override
    public void drawFinal(GraphicsContext context) {
        Paint original = context.getStroke();
        context.setStroke(Color.WHITE);
        super.drawFinal(context);
        context.setStroke(original);
    }
}
