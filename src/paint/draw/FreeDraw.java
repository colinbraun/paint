package paint.draw;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

/**
 * A drawable that allows for free drawing (pencil).
 */
public class FreeDraw extends Drawable {

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

    @Override
    public void setEnd(double x1, double y1) {
        super.setEnd(x1, y1);
        path.add(new Point2D(x1, y1));
    }

    @Override
    public void drawFinal(GraphicsContext context) {
        for(int i = 1; i < path.size(); i++) {
            Point2D from = path.get(i-1);
            Point2D to = path.get(i);
            context.strokeLine(from.getX(), from.getY(), to.getX(), to.getY());
        }
    }
}
