package paint.draw;

import javafx.scene.canvas.GraphicsContext;

public class Triangle extends Polygon {

    public Triangle(double x, double y) {
        super(x, y, 3);
    }

    @Override
    public void drawFinal(GraphicsContext context) {
        super.drawFinal(context);
    }
}
