package paint.shape;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class FreeDraw extends Drawable {

    private List<Point2D> path;

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
    public void draw(GraphicsContext context) {
        for(int i = 1; i < path.size(); i++) {
            Point2D from = path.get(i-1);
            Point2D to = path.get(i);
            context.strokeLine(from.getX(), from.getY(), to.getX(), to.getY());
        }
    }
}
