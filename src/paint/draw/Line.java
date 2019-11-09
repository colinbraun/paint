package paint.draw;

import javafx.scene.canvas.GraphicsContext;

/**
 * A class to represent a line
 * @author Colin Braun
 */
public class Line extends Drawable {

    /**
     *
     * @param startX the starting x value of the line
     * @param startY the starting y value of the line
     * @param endX the ending x value of the line
     * @param endY the ending y value of the line
     */
    public Line(double startX, double startY, double endX, double endY) {
        super(startX, startY, endX, endY);
    }

    /**
     * Constructor for a line with same starting and end points
     * @param x the x-value to start and end at
     * @param y the y-value to start and end at
     */
    public Line(double x, double y) {
        this(x, y, x, y);
    }

    /**
     * Draw the line
     * @param context the context used to draw
     */
    @Override
    public void drawFinal(GraphicsContext context) {
        context.strokeLine(x0, y0, x1, y1);
    }
}
