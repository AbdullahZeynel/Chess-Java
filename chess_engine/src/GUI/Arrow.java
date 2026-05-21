package GUI;

import resources.Variables;

import java.awt.*;

/**
 * Draws analysis arrows on the chess board.
 * Created when the user right-click-drags from one tile to another.
 *
 * The arrow consists of:
 * - A shaft (line) from start to end
 * - An arrowhead (filled triangle) at the endpoint
 *
 * The arrowhead uses trigonometry to calculate two side points
 * offset by ±30° from the shaft direction.
 */
public class Arrow {
    public Point startPoint = null;
    public Point endPoint = null;

    /// Center coordinates of the start and end tiles
    public int startCenterX;
    public int startCenterY;
    public int endCenterX;
    public int endCenterY;

    public Arrow(Point startPoint, Point endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;

        /// Snap to tile center:
        /// Dividing by tileSize truncates to tile origin, multiplying back gives the starting pixel,
        /// then adding tileSize/2 offsets to the center of the tile.
        this.startCenterX= (((startPoint.x / Variables.tileSize ) * Variables.tileSize) + (Variables.tileSize / 2));
        this.startCenterY= (((startPoint.y / Variables.tileSize ) * Variables.tileSize) + (Variables.tileSize / 2));

        this.endCenterX  = (((endPoint.x   / Variables.tileSize ) * Variables.tileSize) + (Variables.tileSize / 2));
        this.endCenterY  = (((endPoint.y   / Variables.tileSize ) * Variables.tileSize) + (Variables.tileSize / 2));
    }

    /// Draws the complete arrow (shaft + arrowhead)
    public void drawArrow(Graphics2D g2d) {
        /// Calculate the angle of the shaft using arctangent
        double dx = endCenterX - startCenterX;
        double dy = endCenterY - startCenterY;
        double angle = Math.atan2(dy, dx);

        int arrowHeadSize = 50;

        /// Calculate the two side points of the arrowhead triangle.
        /// Offset by ±30° (π/6) from the shaft direction, at arrowHeadSize distance from the tip.
        int x1 = (int) (endCenterX - arrowHeadSize * Math.cos(angle - Math.PI / 6));
        int y1 = (int) (endCenterY - arrowHeadSize * Math.sin(angle - Math.PI / 6));
        int x2 = (int) (endCenterX - arrowHeadSize * Math.cos(angle + Math.PI / 6));
        int y2 = (int) (endCenterY - arrowHeadSize * Math.sin(angle + Math.PI / 6));

        /// Set arrow styling
        g2d.setColor(Variables.arrowColor);
        g2d.setStroke(new BasicStroke(10));

        /// Shorten the shaft slightly so it doesn't overlap with the arrowhead
        double shaftLengthReduction = arrowHeadSize * 0.8;
        int shortenedEndX = (int) (endCenterX - shaftLengthReduction * Math.cos(angle));
        int shortenedEndY = (int) (endCenterY - shaftLengthReduction * Math.sin(angle));

        /// Draw the shaft
        g2d.drawLine(startCenterX, startCenterY, shortenedEndX, shortenedEndY);

        /// Draw the arrowhead triangle
        int[] xPoints = {endCenterX, x1, x2};
        int[] yPoints = {endCenterY, y1, y2};
        g2d.fillPolygon(xPoints, yPoints, 3);
    }
}
