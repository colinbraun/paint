package paint.draw;

import javafx.scene.canvas.GraphicsContext;

/**
 * A drawable circle
 * @author Colin Braun
 */
public class Circle extends Ellipse {

    /**
     * Construct the drawable Circle by describing its top left and bottom right coordinates
     * @param x0 the left-most part of this circle
     * @param y0 the top-most part of this circle
     * @param x1 the right-most part of this circle
     * @param y1 the bottom-most part of this circle
     */
    public Circle(double x0, double y0, double x1, double y1) {
        super(x0, y0, x1, y1);
    }

    /**
     * Construct the drawable Circle with the top-left corner being the same as the bottom-right corner
     * @param x the left and right-most part of this circle
     * @param y the top and bottom-most part of this circle
     */
    public Circle(double x, double y) {
        this(x, y, x, y);
    }

    /**
     * Draw the final version of this circle. Will work the same as drawPreview(), which calls this if no override exists.
     * @param context the {@link GraphicsContext} to draw with
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
