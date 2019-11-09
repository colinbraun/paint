package paint.draw;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

/**
 * A drawable that allows for free drawing (pencil).
 * @author Colin Braun
 */
public class FreeDraw extends Drawable {

    /**
     * A list of all the points along the path to be drawn
     */
    private List<Point2D> path;

    /**
     * Construct a FreeDraw starting at x, y
     * @param x the starting x
     * @param y the starting y
     */
    public FreeDraw(double x, double y) {
        super(x, y, x, y);
        path = new ArrayList<>();
        path.add(new Point2D(x, y));
    }

    /**
     * Override of the {@link Drawable}'s setEnd method.
     * Adds a point along the path that has been taken with the drawing
     * @param x1 the ending x value
     * @param y1 the ending y value
     */
    @Override
    public void setEnd(double x1, double y1) {
        super.setEnd(x1, y1);
        path.add(new Point2D(x1, y1));
    }

    /**
     * Draw the result of the drawing, obtained from path that has had values added to it
     * @param context the context used to draw
     */
    @Override
    public void drawFinal(GraphicsContext context) {
        for(int i = 1; i < path.size(); i++) {
            Point2D from = path.get(i-1);
            Point2D to = path.get(i);
            context.strokeLine(from.getX(), from.getY(), to.getX(), to.getY());
        }
    }
}
