package paint.draw.special;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import paint.draw.Drawable;

public class Selection extends Drawable {

    private Image selection;
    private boolean isGrabbed;
    private double grabbedDeltaX, grabbedDeltaY, mouseX, mouseY;

    public Selection(double x0, double y0) {
        super(x0, y0, x0, y0);
        isGrabbed = false;
    }

    public void setSelection(Image image) {
        this.selection = image;
    }

    @Override
    public void drawFinal(GraphicsContext context) {
        if(isGrabbed) {
            context.drawImage(selection, mouseX + grabbedDeltaX, mouseY + grabbedDeltaY);
        }
    }

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

    public boolean isInSelection(double x, double y) {
        boolean xPositive = x1 - x0 >= 0;
        boolean yPositive = y1 - y0 >= 0;
        double x0Temp = xPositive ? x0 : x1;
        double x1Temp = xPositive ? x1 : x0;
        double y0Temp = yPositive ? y0 : y1;
        double y1Temp = yPositive ? y1 : y0;

        return x >= x0Temp && x <= x1Temp && y >= y0Temp && y <= y1Temp;
    }

    public boolean isGrabbed() {
        return isGrabbed;
    }

    public void setGrabbed(boolean grabbed) {
        isGrabbed = grabbed;
    }

    public void setGrabbedX(double grabbedX) {
        this.grabbedDeltaX = getXTopLeft() - grabbedX;
    }

    public void setGrabbedY(double grabbedY) {
        this.grabbedDeltaY = getYTopLeft() - grabbedY;
    }

    public void setMouseX(double mouseX) {
        this.mouseX = mouseX;
    }

    public void setMouseY(double mouseY) {
        this.mouseY = mouseY;
    }
}
