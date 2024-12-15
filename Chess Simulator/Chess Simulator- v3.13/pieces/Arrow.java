package pieces;

import res.Variables;

import java.awt.*;

public class Arrow {

    public Point startPoint = null;
    public Point endPoint = null;

    public int startCenterX;
    public int startCenterY;
    public int endCenterX;
    public int endCenterY;

    public Arrow(Point startPoint, Point endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;

        this.startCenterX= (((startPoint.x / 85 ) * Variables.tileSize) + (Variables.tileSize / 2));
        this.startCenterY= (((startPoint.y / 85 ) * Variables.tileSize) + (Variables.tileSize / 2));
        this.endCenterX  = (((endPoint.x   / 85 ) * Variables.tileSize) + (Variables.tileSize / 2));
        this.endCenterY  = (((endPoint.y   / 85 ) * Variables.tileSize) + (Variables.tileSize / 2));

    }

    public void drawArrow(Graphics2D g2d) {

        /* The Arrowhead points:
         * The arrowhead is a triangle formed by
         *   -The endpoint of the arrow (the tip): (encCenterX, endCenterY)
         *   -Two additional points calculated as x1,y1 and x2,y2 which are offset from the tip by the size of the arrowhead
         *
         * Offset Calculation:
         * the offsets use trigonometry to position the other two points of the triangle
         * Math.cos and Math.sin determine the x and y distances, respectively based on the desired arrowHeadSize and an angle offset of 30 (Math.PI / 6) from the shaft's angle (angle)
         *
         * The Logic is to move backward (-arrowHeadSize) along the line's direction and then rotate +-30 degree
         * to get the two side points of the arrowhead triangle.
         *
         *
         * */

        //calculate direction for arrowhead
        double dx = endCenterX - startCenterX;
        double dy = endCenterY - startCenterY;
        double angle = Math.atan2(dy, dx);  //arctan :D is giving us the angle. Actually the angle here
        //represents the direction of the arrow shaft, calculated using the difference in x and y between the start and end points.


        int arrowHeadSize = 50; //size of the arrowhead
        int x1 = (int) (endCenterX - arrowHeadSize * Math.cos(angle - Math.PI / 6));
        int y1 = (int) (endCenterY - arrowHeadSize * Math.sin(angle - Math.PI / 6));
        int x2 = (int) (endCenterX - arrowHeadSize * Math.cos(angle + Math.PI / 6));
        int y2 = (int) (endCenterY - arrowHeadSize * Math.sin(angle + Math.PI / 6));

        //Basic Styling
        g2d.setColor(new Color(127, 204, 198));
        g2d.setStroke(new BasicStroke(10));


        double shaftLengthReduction = arrowHeadSize * 0.8; // Fine-tune the reduction factor as needed
        int shortenedEndX = (int) (endCenterX - shaftLengthReduction * Math.cos(angle));
        int shortenedEndY = (int) (endCenterY - shaftLengthReduction * Math.sin(angle));

        //Draw the arrow shaft (straight line)
        g2d.drawLine(startCenterX, startCenterY, shortenedEndX, shortenedEndY);




        // Draw the arrowhead (triangle)
        int[] xPoints = {endCenterX, x1, x2};
        int[] yPoints = {endCenterY, y1, y2};
        g2d.fillPolygon(xPoints, yPoints, 3);
    }
}
