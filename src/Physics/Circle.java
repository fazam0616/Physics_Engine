package Physics;


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
        Vector v = new Vector(this.center.clone(),s.center.clone()); //Get direction vector

        //Get point of collision on this circle
        double x = this.center.getX()+Math.cos(Math.toRadians(v.getAngle()))*this.radius;
        return new Point(x,this.center.getY() + Math.sin(Math.toRadians(v.getAngle()))*this.radius);
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
        Point pos = this.center.mathToScreen();
        pos.setX(pos.getX()-radius);
        pos.setY(pos.getY()-radius);


        g.setColor(Color.BLACK);

        g.drawOval((int)pos.getX(),(int)pos.getY(),2*(int)this.radius,2*(int)this.radius);

        g.setColor(Color.red);
        g.drawLine((int)(pos.getX()+radius),(int)(pos.getY()+radius),
                (int)(pos.getX()+radius+radius*Math.cos(Math.toRadians(this.rot))),
                (int)(pos.getY()+radius+radius*Math.sin(Math.toRadians(this.rot))));
    }

    @Override
    public void rotate(double degrees) {
        this.rot += degrees;
        this.rot = this.rot >= 0 ? this.rot % 360 : 360-(-this.rot % 360);
    }

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
