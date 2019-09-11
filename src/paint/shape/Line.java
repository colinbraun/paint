package paint.shape;

import javafx.scene.canvas.GraphicsContext;

/**
 * A class to represent a line
 */
public class Line implements Drawable {
    private double startX;
    private double startY;
    private double endX;
    private double endY;

    public Line(double startX, double startY, double endX, double endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    /**
     * Constructor for a line with same starting and end points
     * @param x the x-value to start and end at
     * @param y the y-value to start and end at
     */
    public Line(double x, double y) {
        this(x, y, x, y);
    }

    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public double getEndX() {
        return endX;
    }

    public double getEndY() {
        return endY;
    }

    public Line setStart(double startX, double startY) {
        this.startX = startX;
        this.startY = startY;
        return this;
    }

    public Line setEnd(double endX, double endY) {
        this.endX = endX;
        this.endY = endY;
        return this;
    }

    @Override
    public void draw(GraphicsContext context) {
        // TODO: Make this dependent on a UI element
        context.setLineWidth(1.0);
        context.strokeLine(startX, startY, endX, endY);
    }
}
