package paint.draw;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Eraser extends FreeDraw {

    public Eraser(double x, double y) {
        super(x, y);
    }

    @Override
    public void drawFinal(GraphicsContext context) {
        Paint original = context.getStroke();
        context.setStroke(Color.WHITE);
        super.drawFinal(context);
        context.setStroke(original);
    }
}
