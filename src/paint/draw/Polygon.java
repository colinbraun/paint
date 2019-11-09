package paint.draw;

import javafx.scene.canvas.GraphicsContext;

/**
 * A generic regular polygon with N sides
 * @author Colin Braun
 */
public class Polygon extends Drawable {

    /**
     * The number of sides the polygon will have
     */
    private int n;

    /**
     * Construct a polygon with n sides centered at x, y
     * @param x The x-center of the polygon
     * @param y The y-center of the polygon
     * @param n The number of sides of the polygon
     */
    public Polygon(double x, double y, int n) {
        super(x, y, x, y);
        this.n = n;
    }

    /**
     * Draw the regular polygon
     * @param context the context used to draw
     */
    @Override
    public void drawFinal(GraphicsContext context) {
        double deltaDegrees = 360.0 / n;
        double startingDegrees = -90.0;
        if(n % 2 == 0)
            startingDegrees += deltaDegrees/2;
        double[] xPoints = new double[n];
        double[] yPoints = new double[n];
        double distance = Math.sqrt((y1-y0)*(y1-y0) + (x1-x0)*(x1-x0));
        for(int i = 0; i < n; i++) {
            xPoints[i] = x0 + distance * Math.cos(Math.toRadians(startingDegrees));
            yPoints[i] = y0 + distance * Math.sin(Math.toRadians(startingDegrees));
            startingDegrees = startingDegrees + deltaDegrees;
        }
        context.fillPolygon(xPoints, yPoints, n);
    }
}
