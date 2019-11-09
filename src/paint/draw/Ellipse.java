package paint.draw;

import javafx.scene.canvas.GraphicsContext;

/**
 * A drawable ellipse
 * @author Colin Braun
 */
public class Ellipse extends Drawable {

    /**
     * Construct the drawable Circle by describing its top left and bottom right coordinates
     * @param x0 the left-most part of this ellipse
     * @param y0 the top-most part of this ellipse
     * @param x1 the right-most part of this ellipse
     * @param y1 the bottom-most part of this ellipse
     */
    public Ellipse(double x0, double y0, double x1, double y1) {
        super(x0, y0, x1, y1);
    }

    /**
     * Set the ending point of the ellipse
     * @param x the ending x value
     * @param y the ending y value
     */
    public Ellipse(double x, double y) {
        this(x, y, x, y);
    }

    /**
     * Draw this ellipse given the {@link GraphicsContext}
     * @param context the context used to draw
     */
    @Override
    public void drawFinal(GraphicsContext context) {
        boolean xPositive = x1 - x0 >= 0;
        boolean yPositive = y1 - y0 >= 0;
        context.fillOval(xPositive ? x0 : x1, yPositive ? y0 : y1, xPositive ? x1-x0 : x0-x1, yPositive ? y1-y0 : y0-y1);
    }
}
