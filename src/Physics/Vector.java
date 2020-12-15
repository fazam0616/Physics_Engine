package Physics;

public class Vector {
    private double angle, magnitude;

    public Vector(double angle, double magnitude) {
        this.angle = angle % 360;
        this.magnitude = magnitude;
    }

    public Vector(Point A, Point B) {
        this.magnitude = Math.sqrt(Math.pow(A.getX()-B.getX(),2) + Math.pow(A.getY()-B.getY(),2));
        this.angle = Math.toDegrees(Math.atan2(B.getY()-A.getY(),B.getX()-A.getX()));
    }

    public Vector() {
        this.angle = 0;
        this.magnitude = 0;
    }

    public double getAngle() {
        return angle;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setAngle(double angle) {
        this.angle = angle % 360;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public double getH(){
        return this.magnitude*Math.cos(Math.toRadians(this.angle));
    }
    public double getV(){
        return this.magnitude*Math.sin(Math.toRadians(this.angle));
    }

    @Override
    public Vector clone(){
        return new Vector(this.getAngle(), this.getMagnitude());
    }

    public String toString(){
        return this.angle+" degrees @ "+this.magnitude;
    }
}
