import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;

public class Canvas extends JPanel implements Observer {
    private Model model;


    public Canvas(Model model) {
        super();
        this.model = model;

        this.setPreferredSize(new Dimension(800, 500));
        this.setBackground(Color.white);

        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        MouseInputListener mil = new MController();
        this.addMouseListener(mil);
        this.addMouseMotionListener(mil);

    }



    public void update(Object observable) {
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        // System.out.println("paint component");
        this.model.drawItems(g2);
    }

    private class MController extends MouseInputAdapter {
        public void mousePressed(MouseEvent e) {
            model.drawStart(e.getPoint());
        }

        public void mouseDragged(MouseEvent e) {
          //  System.out.println("mouse draged");
            model.drawContinue(e.getPoint());
        }

        public void mouseReleased(MouseEvent e) {
            model.drawEnd(e.getPoint());
        }
    }
}