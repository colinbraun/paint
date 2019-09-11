package paint.shape;

import javafx.scene.canvas.GraphicsContext;

public interface Drawable {
    /**
     * Method that when called should draw the desired drawing given the context
     * @param context the context used to draw
     */
    public void draw(GraphicsContext context);
}
