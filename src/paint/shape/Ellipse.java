package paint.shape;

import javafx.scene.canvas.GraphicsContext;

public class Ellipse extends Drawable {

    public Ellipse(double x0, double y0, double x1, double y1) {
        super(x0, y0, x1, y1);
    }

    public Ellipse(double x, double y) {
        this(x, y, x, y);
    }

    @Override
    public void draw(GraphicsContext context) {
        boolean xPositive = x1 - x0 >= 0;
        boolean yPositive = y1 - y0 >= 0;
        context.fillOval(xPositive ? x0 : x1, yPositive ? y0 : y1, xPositive ? x1-x0 : x0-x1, yPositive ? y1-y0 : y0-y1);
    }
}
