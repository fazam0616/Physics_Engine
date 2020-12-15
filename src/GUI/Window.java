package GUI;

import javax.swing.*;

public class Window extends JFrame {
    public Window(){
        this.setSize(1300,700);
        this.setTitle("Physics Engine");
        this.setVisible(true);
        this.setContentPane(new Panel(this));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
