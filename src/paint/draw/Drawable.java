package paint.draw;

import javafx.scene.canvas.GraphicsContext;

public abstract class Drawable {
    /**
     * The starting and ending points of the drawable
     */
    protected double x0, y0, x1, y1;

    public Drawable(double x0, double y0, double x1, double y1) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
    }

    /**
     * Set the ending point of the drawable
     * @param x1 the ending x value
     * @param y1 the ending y value
     */
    public void setEnd(double x1, double y1) {
        this.x1 = x1;
        this.y1 = y1;
    }

    /**
     * Method that when called should draw the desired drawing given the context
     * @param context the context used to draw
     */
    public abstract void draw(GraphicsContext context);
}
