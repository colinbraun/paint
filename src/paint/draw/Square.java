package paint.draw;

import javafx.scene.canvas.GraphicsContext;

/**
 * A drawable Square
 * @author Colin Braun
 */
public class Square extends Rectangle {

    /**
     * Construct a square given to points
     * @param x0 the x value of a corner of the square
     * @param y0 the y value of the corresponding x0 value for the corner of the square
     * @param x1 the x value of a corner of the square
     * @param y1 the y value of the corresponding x0 value for the corner of the square
     */
    public Square(double x0, double y0, double x1, double y1) {
        super(x0, y0, x1, y1);
    }

    /**
     * Construct a square from one point, setting the actual 2 points to the same point
     * Use #setEnd() to change one of these points
     * @param x the x value for the 2 points
     * @param y the y value for the 2 points
     */
    public Square(double x, double y) {
        this(x, y, x, y);
    }

    /**
     * Draw the square
     * @param context the context used to draw
     */
    @Override
    public void drawFinal(GraphicsContext context) {
        double maxWidth = Math.min(Math.abs(x1-x0), Math.abs(y1-y0));
        if(Math.abs(y1-y0) > maxWidth)
            if(y1 < y0)
                y1 = y0 - maxWidth;
            else
                y1 = y0 + maxWidth;
        if(Math.abs(x1-x0) > maxWidth)
            if(x1 < x0)
                x1 = x0 - maxWidth;
            else
                x1 = x0 + maxWidth;
        super.drawFinal(context);
    }
}
