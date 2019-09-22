package paint.draw;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

public class Text extends Drawable {

    public Text(double x, double y) {
        super(x, y, x, y);
    }

    @Override
    public void draw(GraphicsContext context) {
        context.fillText("Hi", x1, y1);
    }
}
