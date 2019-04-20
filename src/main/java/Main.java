import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Doodle");

        Model model = new Model();

        MenuBar MenuBarview = new MenuBar(model);

        Palette pal = new Palette(model);

        Canvas canvas = new Canvas(model);

        PlayBack pb = new PlayBack(model);

        model.assignCanvas(canvas);


        model.addObserver(MenuBarview);
        model.addObserver(pal);
        model.addObserver(pb);
        model.addObserver(canvas);


        JPanel p = new JPanel(new BorderLayout());
        p.add(pal, BorderLayout.WEST);
        p.add(canvas, BorderLayout.CENTER);
        p.add(pb, BorderLayout.SOUTH);

        frame.setJMenuBar(MenuBarview);
        frame.getContentPane().add(p);

        frame.setMinimumSize(new Dimension(650, 400));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
