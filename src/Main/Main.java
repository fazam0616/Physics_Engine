package Main;

import GUI.Window;
import Physics.*;
import java.util.LinkedList;
import java.util.Random;

public class Main {
    public static Window window;
    public static LinkedList<Shape> shapes = new LinkedList<>();
    public static double step = 0.000001;
    public static double fps = 50000;
    

    public static void main(String[] args) throws InterruptedException {
        window = new Window();
        Random r = new Random();

        Circle c = new Circle(10, new Point(0,50));
        c.setHasGravity(false);
        shapes.add(c);
        //c.setMass(r.nextDouble()*0.2+0.9);

        c = new Circle(10, new Point(22,50));
        c.setHasGravity(false);
        shapes.add(c);
        //c.setMass(r.nextDouble()*0.2+0.9);

        c = new Circle(10, new Point(-22,50));
        c.setHasGravity(false);
        shapes.add(c);
        //c.setMass(r.nextDouble()*0.2+0.9);

        c = new Circle(10, new Point(44,50));
        c.setHasGravity(false);
        shapes.add(c);
        //c.setMass(r.nextDouble()*0.2+0.9);

        c = new Circle(10, new Point(-44,50));
        c.setHasGravity(false);
        shapes.add(c);
        //c.setMass(r.nextDouble()*0.2+0.9);

        c = new Circle(10, new Point(11,28));
        c.setHasGravity(false);
        shapes.add(c);
        //c.setMass(r.nextDouble()*0.2+0.9);

        c = new Circle(10, new Point(-11,28));
        c.setHasGravity(false);
        shapes.add(c);
        //c.setMass(r.nextDouble()*0.2+0.9);

        c = new Circle(10, new Point(33,28));
        c.setHasGravity(false);
        shapes.add(c);
        //c.setMass(r.nextDouble()*0.2+0.9);

        c = new Circle(10, new Point(-33,28));
        c.setHasGravity(false);
        shapes.add(c);
        //c.setMass(r.nextDouble()*0.2+0.9);

        c = new Circle(10, new Point(0,6));
        c.setHasGravity(false);
        shapes.add(c);
        //c.setMass(r.nextDouble()*0.2+0.9);

        c = new Circle(10, new Point(22,6));
        c.setHasGravity(false);
        shapes.add(c);
        //c.setMass(r.nextDouble()*0.2+0.9);

        c = new Circle(10, new Point(-22,6));
        c.setHasGravity(false);
        shapes.add(c);
        //c.setMass(r.nextDouble()*0.2+0.9);

        c = new Circle(10, new Point(11,-16));
        c.setHasGravity(false);
        shapes.add(c);
        //c.setMass(r.nextDouble()*0.2+0.9);

        c = new Circle(10, new Point(-11,-16));
        c.setHasGravity(false);
        shapes.add(c);
        //c.setMass(r.nextDouble()*0.2+0.9);

        c = new Circle(10, new Point(30,-110));
        c.setHasGravity(false);
        //shapes.add(c);
        c.setMass(1);
        //c.setRpm(5);
        Polygon p = new Polygon(new Point[]{
                new Point(0,100),
                new Point(-50,0),
                new Point(50,0)
        });
        p.setHasGravity(false);
        p.moveCenter(new Point(0,-200));
        p.rotate(50);
        shapes.add(p);
        p.setMass(10);
        p.setRpm(-80);
        p.addAcceleration(new Vector(90, 10000));

        while (true){
            Thread.sleep((long) (1000/fps));
            window.repaint();
        }
    }
}
