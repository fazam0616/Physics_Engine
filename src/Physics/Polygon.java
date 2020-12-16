package Physics;

import java.awt.*;
import java.util.LinkedList;

public class Polygon extends Shape {
    double rot = 0;
    private Line[] lines;
    private Line[] OGlines;
    private Point OGCenter;

    public Polygon(Line[] lines){
        this.lines = lines;
        this.OGlines = new Line[lines.length];
        OGCenter = this.getCenter().clone();

        for (int i = 0; i < lines.length; i++) {
            OGlines[i] = lines[i].clone();
        }
    }

    public Polygon(Point[] points){
        lines = new Line[points.length];
        for (int i = 0; i < lines.length-1; i++) {
            lines[i] = new Line(points[i],points[i+1]);
        }
        lines[lines.length-1] = new Line(points[points.length-1],points[0]);
        this.OGlines = new Line[lines.length];
        OGCenter = this.getCenter().clone();

        for (int i = 0; i < lines.length; i++) {
            OGlines[i] = lines[i].clone();
        }
    }

    @Override
    public Circle getCircleOfInfluence() {
        Point center = this.getCenter();
        double r = 0;

        for (Line L:lines){
            r = Math.max(r, new Vector(center,L.getPointA()).getMagnitude());
        }

        return new Circle(r,center);
    }

    public Point intersect(Shape s) {
        Point closestPoint = null;
        for(Line L:this.lines){
            Point p = L.intersec(s);
            if (p !=null){
                if (closestPoint == null)
                    closestPoint = p;
                else{
                    /*
                    Seing if the collision point on the other object is the closest,
                    or if it's a different part of the line
                     */
                    double closestDist = new Vector(s.getCenter(),closestPoint).getMagnitude();
                    if (new Vector(s.getCenter(),p).getMagnitude() < closestDist)
                        closestPoint = p;
                    if (new Vector(s.getCenter(),L.getPointA()).getMagnitude() < closestDist)
                        closestPoint = L.getPointA();
                    if (new Vector(s.getCenter(),L.getPointB()).getMagnitude() < closestDist)
                        closestPoint = L.getPointB();
                }
            }
        }

        return closestPoint;
    }

    @Override
    public Point getCenter() {
        double x = 0;
        double y = 0;

        //Calculates the average position of all the points in this Shape
        for (Line L:lines){
            x += L.getPointA().getX();
            y += L.getPointA().getY();
        }

        x /= lines.length;
        y /= lines.length;

        return new Point(x,y);
    }

    public void setCenter(Point newCenter){
        Point center = getCenter();
        Point delta = new Point(newCenter.getX() - center.getX(), newCenter.getY() - center.getY());

        for (Line L:lines){
            L.getPointA().setX(L.getPointA().getX() + delta.getX());
            L.getPointA().setY(L.getPointA().getY() + delta.getY());
        }
    }

    @Override
    public void moveCenter(Point delta){
        for (Line L:lines){
            L.getPointA().setX(L.getPointA().getX() + delta.getX());
            L.getPointA().setY(L.getPointA().getY() + delta.getY());
        }
    }

    public void rotate(double angle){
        double currentAngle;
        Vector v;
        Point center = this.getCenter();
        rot += angle;
        rot = rot - (rot*((int)rot/360)); //Making sure the rotation is less than 360

        /*
        Only point A is edited, as it refers to the Start of one line, and the end of the other, meaning
        all points are affected this way
         */
        for(int i = 0; i<lines.length;i++){
            Line l = lines[i];
            v = new Vector(OGCenter.clone(), OGlines[i].getPointA().clone());
            currentAngle = v.getAngle() + rot;
            l.getPointA().setX(Math.cos(Math.toRadians(currentAngle))*v.getMagnitude());
            l.getPointA().setY(Math.sin(Math.toRadians(currentAngle))*v.getMagnitude());
        }
        this.setCenter(center);
    }

    public Line[] getLines() {
        return lines;
    }

    @Override
    public void paint(Graphics g) {
        java.awt.Polygon p = new java.awt.Polygon();
        Point convert;
        for (Line L:this.lines){
            convert = L.getPointA().mathToScreen();
            p.addPoint((int)convert.getX(),(int)convert.getY());
        }

        g.setColor(Color.BLACK);
        if (this.isColliding() != null)
            g.setColor(Color.red);
        g.drawPolygon(p);
    }
}
