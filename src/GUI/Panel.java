package GUI;

import Main.Main;

import javax.swing.*;
import java.awt.Graphics;
import Physics.*;
public class Panel extends JPanel {
    private Window parent;

    public Panel(Window w){
        this.parent = w;
    }

    public void paintComponent(Graphics g){
         for(Shape s: Main.shapes){
             s.update(1.0);
             s.paint(g);
         }
    }
}
