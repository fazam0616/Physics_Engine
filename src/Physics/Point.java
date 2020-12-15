package Physics;

import GUI.Window;
import Main.Main;

public class Point {
    private double x,y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Point mathToScreen(){
        Window window = Main.window;
        double x = this.x+window.getWidth()/2.0;
        double y = window.getHeight()/2.0-this.y;

        return new Point(x,y);
    }

    public Point clone(){
        return new Point(x,y);
    }

    public String toString(){
        return "("+this.getX()+", "+this.getY()+")";
    }
}
