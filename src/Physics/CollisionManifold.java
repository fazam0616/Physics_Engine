package Physics;

public class CollisionManifold {
    private Point contact;
    private double distance;
    private Vector normal;

    public CollisionManifold(Point contact, double distance, Vector normal) {
        this.contact = contact;
        this.distance = distance;
        this.normal = normal;
    }

    public Vector getCollNormal() {
        return normal;
    }


    public Point getPoint() {
        return this.contact;
    }
}
