import javax.swing.*;
import java.awt.*;

public class StrokeThickness extends JComponent implements Observer {
    private int thickness;
    private Color colour = Color.BLACK;
    private Model model;
    private boolean selected;

    public StrokeThickness(int th, Model model) {
        thickness = th;
        this.model = model;
        selected = false;

        if(thickness == 1) {
            selected = true;
        }

        this.setPreferredSize(new Dimension(40,40));
        model.addObserver(this);
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(thickness));
        g2.setColor(colour);
        g2.drawLine(10,10,80,10);

        if(selected) {
            g2.setColor(Color.gray);
            g2.setStroke(new BasicStroke(3));
            g2.drawRect(1,1,90,20);
        }
    }

    public int getThickness() {
        return thickness;
    }

    public void update(Object observable) {
        // XXX Fill this in with the logic for updating the view when the model
        // changes.
        // model.
        // System.out.println("Model changed!");
        int cur = this.model.getCurThickness();
        colour = this.model.getCurColor();

        if (cur == thickness) {
            selected = true;
        } else {
            selected = false;
        }
        repaint();
    }
}