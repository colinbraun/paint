package paint.draw;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * A drawable selection
 * @author Colin Braun
 */
public class Selection extends Drawable {

    /**
     * The current selection
     */
    private Image selection;
    /**
     * Whether or not the selection has been grabbed (with the mouse)
     */
    private boolean isGrabbed;
    /**
     * Info used to determine where to place the image on the canvas while it's dragged
     */
    private double grabbedDeltaX, grabbedDeltaY, mouseX, mouseY;

    /**
     * Set a selection at x0, y0
     * @param x0 the starting x value of the selection
     * @param y0 the starting y value of the selection
     */
    public Selection(double x0, double y0) {
        super(x0, y0, x0, y0);
        isGrabbed = false;
    }

    /**
     * Set the image that is selected.
     * @param image the selected image
     */
    public void setSelection(Image image) {
        this.selection = image;
    }

    /**
     * Draw just the image (assuming it's grabbed, like it should be).
     * @param context the context used to draw
     */
    @Override
    public void drawFinal(GraphicsContext context) {
        if(isGrabbed) {
            context.drawImage(selection, mouseX + grabbedDeltaX, mouseY + grabbedDeltaY);
        }
    }

    /**
     * Draw the border around the current selection (so you can see what's being selected).
     * If it's grabbed, also draw the image based on the mouse's position and where the image was grabbed.
     * @param context the context used to draw
     */
    @Override
    public void drawPreview(GraphicsContext context) {
        double originalWidth = context.getLineWidth();
        Paint originalColor = context.getStroke();
        context.setLineWidth(1.0);
        context.setStroke(Color.BLACK);
        boolean xPositive = x1 - x0 >= 0;
        boolean yPositive = y1 - y0 >= 0;
        if(isGrabbed) {
            context.drawImage(selection, mouseX + grabbedDeltaX, mouseY + grabbedDeltaY);
        }
        context.strokeRect(xPositive ? x0 : x1, yPositive ? y0 : y1, xPositive ? x1-x0 : x0-x1, yPositive ? y1-y0 : y0-y1);

        context.setLineWidth(originalWidth);
        context.setStroke(originalColor);
    }

    /**
     * Check if the point x, y is within the made selection
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return whether or not (x,y) is in the selection
     */
    public boolean isInSelection(double x, double y) {
        boolean xPositive = x1 - x0 >= 0;
        boolean yPositive = y1 - y0 >= 0;
        double x0Temp = xPositive ? x0 : x1;
        double x1Temp = xPositive ? x1 : x0;
        double y0Temp = yPositive ? y0 : y1;
        double y1Temp = yPositive ? y1 : y0;

        return x >= x0Temp && x <= x1Temp && y >= y0Temp && y <= y1Temp;
    }

    /**
     * Whether or not the selection is grabbed
     * @return whether or not the selection is grabbed
     */
    public boolean isGrabbed() {
        return isGrabbed;
    }

    /**
     * Set whether or not the selection is grabbed
     * @param grabbed is the selection grabbed?
     */
    public void setGrabbed(boolean grabbed) {
        isGrabbed = grabbed;
    }

    /**
     * Set where this selection is grabbed horizontally
     * @param grabbedX the x value
     */
    public void setGrabbedX(double grabbedX) {
        this.grabbedDeltaX = getXTopLeft() - grabbedX;
    }

    /**
     * Set where this selection is grabbed vertically
     * @param grabbedY the y value
     */
    public void setGrabbedY(double grabbedY) {
        this.grabbedDeltaY = getYTopLeft() - grabbedY;
    }

    /**
     * Set the x value of where the mouse currently is
     * @param mouseX the x value of the mouse's position
     */
    public void setMouseX(double mouseX) {
        this.mouseX = mouseX;
    }

    /**
     * Set the y value of where the mouse currently is
     * @param mouseY the y value of the mouse's position
     */
    public void setMouseY(double mouseY) {
        this.mouseY = mouseY;
    }
}
