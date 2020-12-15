package Physics;

import java.awt.*;
import java.util.LinkedList;

public class Line {
    private Point A,B;

    public Line(Point a, Point b) {
        A = a;
        B = b;
    }

    public Line getPerp(double b){
        double m = -1/this.getM();

        Point pointA = new Point(this.getPointA().getX(), m*this.getPointA().getX()+b);
        Point pointB = new Point(this.getPointB().getX(), m*this.getPointB().getX()+b);
        return new Line(pointA,pointB);
    }

    public double getM(){
        return (A.getY()-B.getY())/(A.getX()-B.getX());
    }

    public double getB(){
        return A.getY() - this.getM()*A.getX();
    }

    public Point intersec (Line that){
        if (this.getM() != that.getM()){
            double x = (that.getB()-this.getB())/(this.getM()-that.getM());
            Point p = new Point(x,this.getY(x));
            if (this.inRange(p)){
                if (that.inRange(p)){
                    return p;
                }
            }
        }
        return null;
    }
    public Point intersec (Shape s){
        if (s instanceof Circle)
            return this.intersec((Circle)s);
        else
            return this.intersec((Polygon)s);
    }
    public Point intersec (Polygon p){
        LinkedList<Line> lines = new LinkedList<>();
        Point closestPoint = p.getLines()[0].getPointA();
        for(Line L:p.getLines()){
            Point point = this.intersec(L);
            if (point !=null){
                if (new Vector(p.getCenter(),point).getMagnitude() < new Vector(p.getCenter(),closestPoint).getMagnitude())
                    closestPoint = point;
            }
        }

        return closestPoint;
    }
    public Point intersec (Circle circle){
        double x;
        double x2;
        Point pointA = this.getPointA().clone();
        Point pointB = this.getPointB().clone();
        Point p;
        Point p2;
        pointA.setX(pointA.getX()-circle.getCenter().getX());
        pointA.setY(pointA.getY()-circle.getCenter().getY());

        pointB.setX(pointB.getX()-circle.getCenter().getX());
        pointB.setY(pointB.getY()-circle.getCenter().getY());
        Line offsetLine = new Line(pointA,pointB);

        if (Double.isFinite(this.getM()) && this.getM() != 0){
            double a = Math.pow(this.getM(),2)+1;
            double b = 2*this.getM()*offsetLine.getB();
            double c = Math.pow(offsetLine.getB(),2) - Math.pow(circle.getRadius(),2);
            double disc = Math.pow(b,2) - (4*a*c);
            if (disc >=0){
                //If the line theoretically touches the circle at one point
                if (disc == 0){
                    x = -b/(2*a);
                    p = new Point(x,offsetLine.getY(x));
                    p.setX(p.getX()+circle.getCenter().getX());
                    p.setY(p.getY()+circle.getCenter().getY());

                    return (this.inRange(p)) ? p:null;
                }
                //If the line theoretically pass through the circle at 2 points
                else if (disc > 0){
                    x = (-b + Math.sqrt(disc)) / (2*a);
                    x2 =(-b - Math.sqrt(disc)) / (2*a);

                    p = new Point(x,offsetLine.getY(x));
                    p2 = new Point(x2,offsetLine.getY(x2));

                    p.setX(p.getX()+circle.getCenter().getX());
                    p.setY(p.getY()+circle.getCenter().getY());
                    p2.setX(p2.getX()+circle.getCenter().getX());
                    p2.setY(p2.getY()+circle.getCenter().getY());

                    //If both points are in range of the line segment
                    if (this.inRange(p) && this.inRange(p2)){
                        return p;
                    }

                    //If only one or no point is in range of the point
                    else{
                        p = (this.inRange(p)) ? p:p2; //If the first point isnt in range, choose the other point
                        return (this.inRange(p)) ? p:null; //If the remaining point is in range, return it, else return null
                    }
                }
            }
            else{
                return null;
            }
        }

        //If the line is vertical
        else if (!Double.isFinite(this.getM())){
            if (Math.abs(offsetLine.getPointA().getX()) <= circle.getRadius()){
                x = offsetLine.getPointA().getX();
                double y1 = +Math.sqrt(circle.getRadius()*circle.getRadius() - x*x);
                double y2 = -y1;
                p = new Point(x,y1);
                p2 = new Point(x,y2);

                p.setX(p.getX()+circle.getCenter().getX());
                p.setY(p.getY()+circle.getCenter().getY());

                p2.setX(p2.getX()+circle.getCenter().getX());
                p2.setY(p2.getY()+circle.getCenter().getY());
                //If both points are in range
                if (this.inRange(p) && this.inRange(p2)){
                    return p;
                }
                else{
                    p = (this.inRange(p)) ? p:p2; //If the first point isnt in range, choose the other point
                    return (this.inRange(p)) ? p:null; //If the remaining point is in range, return it, else return null
                }
            }
            else return null;
        }
        //If the line is horizontal
        else{
            if (Math.abs(offsetLine.getPointA().getY()) <= circle.getRadius()){
                double y = offsetLine.getPointA().getY();
                double x1 = +Math.sqrt(circle.getRadius()*circle.getRadius() - y*y);
                x2 = -x1;
                p = new Point(x1,y);
                p2 = new Point(x2,y);

                p.setX(p.getX()+circle.getCenter().getX());
                p.setY(p.getY()+circle.getCenter().getY());

                p2.setX(p2.getX()+circle.getCenter().getX());
                p2.setY(p2.getY()+circle.getCenter().getY());
                //If both points are in range
                if (this.inRange(p) && this.inRange(p2)){
                    return p;
                }
                else{
                    p = (this.inRange(p)) ? p:p2; //If the first point isnt in range, choose the other point
                    return (this.inRange(p)) ? p:null; //If the remaining point is in range, return it, else return null
                }
            }
        }
        return null;
    }

    public boolean inRange(Point p){
        if (Double.isFinite(this.getM())){
            if (Math.abs(p.getY() - this.getY(p.getX())) < 0.00001)
                return  (p.getX() >= Math.min(A.getX(), B.getX())) &&
                        (p.getX() <= Math.max(A.getX(), B.getX()));
        }
        else{
            if (p.getX() == this.getPointA().getX())
                return  (p.getY() >= Math.min(A.getY(), B.getY())) &&
                        (p.getY() <= Math.max(A.getY(), B.getY()));
        }
        return false;
    }

    public double getY(double x){
        if (!Double.isNaN(this.getM())){
            return this.getM()*x + this.getB();
        }
        else {
            return this.getPointA().getY();
        }
    }

    public Point getPointA() {
        return this.A;
    }

    public Point getPointB() {
        return this.B;
    }

    public Line clone(){
        return new Line(getPointA().clone(), getPointB().clone());
    }

    public void paint(Graphics g){
        Point a = getPointA().mathToScreen();
        Point b = getPointB().mathToScreen();

        g.setColor(Color.BLACK);
        g.drawLine((int)a.getX(),(int)a.getY(),(int)b.getX(),(int)b.getY());
    }
}
