package paint.draw;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

public class Text extends Drawable {

    private String text;

    public Text(double x, double y, String text) {
        super(x, y, x, y);
        this.text = text;
    }

    @Override
    public void draw(GraphicsContext context) {
        context.fillText(text, x1, y1);
    }
}
