package paint.draw;

import javafx.scene.canvas.GraphicsContext;

/**
 * A drawable Rectangle
 * @author Colin Braun
 */
public class Rectangle extends Drawable {

    /**
     * Construct a rectangle given to points
     * @param x0 the x value of a corner of the rectangle
     * @param y0 the y value of the corresponding x0 value for the corner of the rectangle
     * @param x1 the x value of a corner of the rectangle
     * @param y1 the y value of the corresponding x0 value for the corner of the rectangle
     */
    public Rectangle(double x0, double y0, double x1, double y1) {
        super(x0, y0, x1, y1);
    }

    /**
     * Construct a rectangle from one point, setting the actual 2 points to the same point
     * Use #setEnd() to change one of these points
     * @param x the x value for the 2 points
     * @param y the y value for the 2 points
     */
    public Rectangle(double x, double y) {
        this(x, y, x, y);
    }

    /**
     * Draw the rectangle
     * @param context the context used to draw
     */
    @Override
    public void drawFinal(GraphicsContext context) {
        boolean xPositive = x1 - x0 >= 0;
        boolean yPositive = y1 - y0 >= 0;
        context.fillRect(xPositive ? x0 : x1, yPositive ? y0 : y1, xPositive ? x1-x0 : x0-x1, yPositive ? y1-y0 : y0-y1);
    }
}
