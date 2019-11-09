package paint.draw;

import javafx.scene.canvas.GraphicsContext;

/**
 * A drawable Triangle
 * @author Colin Braun
 */
public class Triangle extends Polygon {

    /**
     * Construct a drawable Triangle
     * @param x the center x value of this Triangle
     * @param y the center y value of this Triangle
     */
    public Triangle(double x, double y) {
        super(x, y, 3);
    }

    /**
     * Draw the Triangle
     * @param context the context used to draw
     */
    @Override
    public void drawFinal(GraphicsContext context) {
        super.drawFinal(context);
    }
}
