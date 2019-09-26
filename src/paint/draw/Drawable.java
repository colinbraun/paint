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
    public abstract void drawFinal(GraphicsContext context);

    /**
     * Allows children class to overload and draw a preview (i.e. what it looks like as it's being dragged)
     * If this the sub-class does not override this, drawFinal is called instead.
     * Note that this might not be necessary to create a preview.
     * @param context the context used to draw
     */
    public void drawPreview(GraphicsContext context) {
        drawFinal(context);
    }

    public double getXTopLeft() {
        return Math.min(x0, x1);
    }

    public double getYTopLeft() {
        return Math.min(y0, y1);
    }

    public double getWidth() {
        return Math.abs(x1-x0);
    }

    public double getHeight() {
        return Math.abs(y1-y0);
    }
}
