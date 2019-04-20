import javax.swing.*;
import java.awt.*;


public class MyColor extends JComponent implements Observer {
    private Color c;
    private boolean selected;
    private Model model;



    public MyColor(Color cs, Model model) {
        c = cs;
        selected = false;
        if(cs == Color.BLACK) {
            selected = true;
        }

        this.model = model;

        this.setPreferredSize(new Dimension(50,50));
        this.setBackground(Color.WHITE);

        model.addObserver(this);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        g2.setColor(c);
        g2.fillRect(1,1,50,50);

        if(selected) {
            g2.setColor(Color.GRAY);
            g2.setStroke(new BasicStroke(3));
            g2.drawRect(1,1,47,47);
        }

    }

    public Color getColor() {
        return c;
    }


    public void update(Object observable) {
       // System.out.println("Model changed!");
        Color cur = this.model.getCurColor();
        if (cur == c) {
            selected = true;
        } else {
            selected = false;
        }
        repaint();
    }

    public void changeColor(Color newc) {
        c = newc;
        repaint();
    }
}