package Physics;

import Main.Main;

import java.awt.*;

public abstract class Shape {
    private double mass = 1;
    private Vector velocity = new Vector(0,0);
    private double rpm = 0;
    public static final Vector GRAVITY = new Vector(270, 9.81);
    private boolean isPassive = false;
    private boolean hasGravity = true;
    private double restitution = 1;
    public static double maxAng = 0;

    public abstract Circle getCircleOfInfluence();
    public abstract Point getCenter();
    public abstract void moveCenter(Point delta);
    public abstract void paint(Graphics g);
    public abstract void rotate(double degrees);

    /*
    Gets the point of intersection between 2 bodies, located on the "Parent" object
    The parent object is the one the method is called from, the child is passed through the parameter
     */
    public abstract Point intersect(Shape s);// Get point of intersection between 2 bodies

    public CollisionManifold collide(Shape s){
        double dist = new Vector(this.getCenter(),s.getCenter()).getMagnitude();
        if (dist < this.getCircleOfInfluence().getRadius()+s.getCircleOfInfluence().getRadius()){
            return collide(this.intersect(s), s.intersect(this), this.getRelVelocity(s));
        }
        else
            return null;
    }

    public CollisionManifold collide(Point p1, Point p2, Vector relV) {
        if (p1 != null && p2!=null){
            Vector v = new Vector(p1,p2);
            double depth = v.getMagnitude();
            Vector collNormal;

            double hor = (v.getH()-relV.getH());
            double vert = (v.getV()-relV.getV());
            double angle = Math.toDegrees(Math.atan2(vert,hor));
            collNormal = new Vector(angle,(1/ Main.step)*depth);
            return new CollisionManifold(p1,depth,collNormal);
        }
        else
            return null;
    }

    public void addForce(Vector acceleration, double mass){
        Vector a = acceleration.clone();
        a.setMagnitude(acceleration.getMagnitude()*(mass/this.getMass()));
        addAcceleration(a);
    }

    public void addAcceleration(Vector acceleration){
        double horizontal = this.velocity.getH()+acceleration.getH();
        double vertical = this.velocity.getV()+acceleration.getV();

        this.velocity.setMagnitude(Math.sqrt(Math.pow(horizontal,2) + Math.pow(vertical, 2)));
        this.velocity.setAngle(Math.toDegrees(Math.atan2(vertical,horizontal)));
    }

    public Shape isColliding(){
        for(Shape s: Main.shapes)
            if (s != this)
                if (this.intersect(s)!=null)
                    return s;

        return null;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public void update(Double timestep){
        if (!this.isPassive){
            if (this.hasGravity) this.addAcceleration(GRAVITY);
            Point delta = new Point(this.velocity.getH()*Main.step,this.velocity.getV()*Main.step);
            this.moveCenter(delta);

            this.rotate(Math.toDegrees(this.rpm)*Main.step);

            for (Shape s:Main.shapes){
                if(s!=this){
                    CollisionManifold cm1 = this.collide(s);
                    CollisionManifold cm2 = s.collide(this);
                    if (cm1 != null){
                        double momentumH = this.getVelocity().getH()*this.mass + s.getVelocity().getH()*s.getMass();
                        double momentumV = this.getVelocity().getV()*this.mass + s.getVelocity().getV()*s.getMass();
                        double angularMomentum = s.getRpm()*s.getMass() + this.getRpm()*this.getMass();
                        double h2 = (2*this.getMass()*this.getVelocity().getH())/(this.getMass() + s.getMass());
                        double v2 = (2*this.getMass()*this.getVelocity().getV())/(this.getMass() + s.getMass());
                        Vector v = new Vector(this.getCenter(), cm1.getPoint());
                        v.setAngle(v.getAngle()+90*Math.signum(this.getRpm()));
                        System.out.println(this.getClass());
                        System.out.println(h2+", "+v2);
                        System.out.println(v.getAngle());
                        h2 += Math.cos(Math.toRadians(v.getAngle()))*(v.getMagnitude()*this.getRpm());
                        v2 += Math.sin(Math.toRadians(v.getAngle()))*(v.getMagnitude()*this.getRpm());
                        System.out.println(h2+", "+v2);

                        double h1 = (momentumH - h2*s.getMass())/this.getMass();
                        double v1 = (momentumV - v2*s.getMass())/this.getMass();
                        Vector f2 = new Vector(
                                Math.toDegrees(Math.atan2(v2,h2)),
                                Math.sqrt(h2*h2+v2*v2));
                        Vector f1 = new Vector(
                                Math.toDegrees(Math.atan2(v1,h1)),
                                Math.sqrt(h1*h1+v1*v1));

                        Point delta1 = new Point(cm1.getCollNormal().getH()*Main.step,cm1.getCollNormal().getV()*Main.step);
                        Point delta2 = new Point(cm2.getCollNormal().getH()*Main.step,cm2.getCollNormal().getV()*Main.step);

                        this.moveCenter(delta1);
                        s.moveCenter(delta2);

                        this.setVelocity(new Vector(0,0));

                        s.addVelocity(f2, cm2.getPoint());
                        this.addAcceleration(f1);
                        this.rpm = (angularMomentum - (s.getRpm()*s.getMass()))/this.getMass();
                        maxAng = Math.max(Math.abs(maxAng),Math.abs(this.rpm));
                        System.out.println();
                    }
                }
            }
        }
    }

    public void addVelocity(Vector velocity, Point pos){
        Point centre = this.getCenter();
        Vector v = new Vector(centre, pos);
        double tangSpeed = Math.cos(Math.toRadians(velocity.getAngle() - (v.getAngle()+90)))*velocity.getMagnitude();
        this.rpm += tangSpeed / v.getMagnitude();
        Vector perp = new Vector(v.getAngle()+90,tangSpeed);

        double hor = velocity.getH() - perp.getH();
        double vert = velocity.getV() - perp.getV();

        this.addAcceleration(new Vector(
                Math.toDegrees(Math.atan2(vert,hor)),
                Math.sqrt(hor*hor + vert*vert)
        ));
    }

    public Vector getRelVelocity(Shape s){
        Vector relV = this.getVelocity().clone();
        double h = relV.getH() - s.getVelocity().getH();
        double v = relV.getV() - s.getVelocity().getV();
        relV = new Vector(
                Math.toDegrees(Math.atan2(v,h)),
                Math.sqrt(Math.pow(h,2) + Math.pow(v, 2)));
        return relV;
    }

    public double getAngularMomentum(Point p){
        double d = new Vector(this.getCenter(),p).getMagnitude();
        return this.rpm*this.getMass()*d*d;
    }

    public boolean getHasGravity() {
        return hasGravity;
    }

    public void setHasGravity(boolean hasGravity) {
        this.hasGravity = hasGravity;
    }
    public boolean getIsPassive() {
        return isPassive;
    }

    public void setIsPassive(boolean isPassive) {
        this.isPassive = isPassive;
    }

    public double getRestitution() {
        return restitution;
    }

    public void setRestitution(double restitution) {
        this.restitution = restitution;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public double getRpm() {
        return rpm;
    }

    public void setRpm(double rpm) {
        this.rpm = rpm;
    }

    public void addRPM(Double degrees){
        this.rpm += Math.toRadians(degrees);
    }
}
