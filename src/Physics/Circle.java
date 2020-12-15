package Physics;

import Main.Main;

import java.awt.*;

public class Circle extends Shape {
    private double radius;
    private Point center;

    public Circle(double radius, Point center) {
        this.radius = radius;
        this.center = center;
    }

    @Override
    public Circle getCircleOfInfluence() {
        return this;
    }

    public Point collide(Circle s) {
        Vector v = new Vector(this.center.clone(),s.center.clone());
        double x = this.center.getX()+Math.cos(Math.toRadians(v.getAngle()))*this.radius;
        Point p = new Point(x,this.center.getY() + Math.sin(Math.toRadians(v.getAngle()))*this.radius);
        return p;
    }
    public Point intersect(Shape s) {
        if (s instanceof Circle)
            return this.collide((Circle)s);
        else
            return this.collide((Polygon)s);
    }

    public Point collide(Polygon s) {
        Point p1 = s.intersect(this);
        if (p1 != null){
            Vector v = new Vector(this.getCenter(),p1);
            double angle = Math.toRadians(v.getAngle());
            return new Point(Math.cos(angle)*this.radius+this.getCenter().getX(), Math.sin(angle)*this.radius+this.getCenter().getY());
        }
        return null;
    }

    @Override
    public Point getCenter() {
        return this.center;
    }

    @Override
    public void paint(Graphics g) {
        try{
            g.setColor(new Color((int) (255*Math.abs(this.getRpm()/(Shape.maxAng+500))),0,0));
        }catch(Exception e){
            g.setColor(Color.red);
        }
        Point pos = this.center.mathToScreen();
        pos.setX(pos.getX()-radius);
        pos.setY(pos.getY()-radius);



        g.fillOval((int)pos.getX(),(int)pos.getY(),2*(int)this.radius,2*(int)this.radius);
    }

    @Override
    public void rotate(double degrees) {}

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public Circle clone(){
        return new Circle(this.radius,this.center.clone());
    }

    @Override
    public void moveCenter(Point delta) {
        this.center.setX(this.center.getX()+delta.getX());
        this.center.setY(this.center.getY()+delta.getY());
    }
}
