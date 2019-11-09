package paint.draw;

import javafx.scene.canvas.GraphicsContext;

/**
 * A generic Drawable, intended to be extended by anything else that is desired to be drawn on a {@link javafx.scene.canvas.Canvas}
 * Note that drawPreview() is intended to be called when in the process of drawing (holding down click and dragging).
 * Likewise, drawFinal() is intended to be called after the drawing process is done (release the mouse button).
 * If the preview is the same as the final, just implementing drawFinal() is sufficient, since drawPreview() will call drawFinal() if it is not overriden
 * @author Colin Braun
 */
public abstract class Drawable {
    /**
     * The starting and ending points of the drawable
     */
    protected double x0, y0, x1, y1;

    /**
     * Constructor of the Drawable that child classes will have to implement
     * @param x0 the x part of the top-left corner of the Drawable
     * @param y0 the y part of the top-left corner of the Drawable
     * @param x1 the x part of the bottom-right corner of the Drawable
     * @param y1 the y part of the bottom-right corner of the Drawable
     */
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

    /**
     * Get the top-left most x value
     * @return the top-left most x value
     */
    public double getXTopLeft() {
        return Math.min(x0, x1);
    }

    /**
     * Get the top-left most y value
     * @return the top-left most y value
     */
    public double getYTopLeft() {
        return Math.min(y0, y1);
    }

    /**
     * Get the width of the drawable
     * @return the width of the drawable
     */
    public double getWidth() {
        return Math.abs(x1-x0);
    }

    /**
     * Get the height of the drawable
     * @return the height of the drawable
     */
    public double getHeight() {
        return Math.abs(y1-y0);
    }
}
