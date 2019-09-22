package paint.draw;

import javafx.scene.canvas.GraphicsContext;

/**
 * A class to represent a line
 */
public class Line extends Drawable {

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

    @Override
    public void draw(GraphicsContext context) {
        context.strokeLine(x0, y0, x1, y1);
    }
}
