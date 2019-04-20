import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Palette extends JPanel implements Observer {

    private Model model;

    private JButton ColorChooser = new JButton("Color Chooser");


    private ArrayList<MyColor> mycolors = new ArrayList<>();
    private ArrayList<StrokeThickness> mystrokes = new ArrayList<>();

    public Palette(Model model) {

        this.model = model;

        JPanel colorP = new JPanel();
        colorP.setLayout(null);

        MyColor bl = new MyColor(Color.BLACK, model);
        bl.setBounds(1,1,50,50);
        mycolors.add(bl);

        MyColor blue = new MyColor(Color.BLUE, model);
        blue.setBounds(52,1,50,50);
        mycolors.add(blue);

        MyColor red = new MyColor(Color.red, model);
        red.setBounds(1,52,50,50);
        mycolors.add(red);

        MyColor pik = new MyColor(Color.pink, model);
        pik.setBounds(52,52,50,50);
        mycolors.add(pik);

        MyColor yl = new MyColor(Color.yellow, model);
        yl.setBounds(1,103,50,50);
        mycolors.add(yl);

        MyColor cy = new MyColor(Color.cyan, model);
        cy.setBounds(52,103,50,50);
        mycolors.add(cy);


        for(MyColor myc : mycolors) {
            myc.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if(e.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                        Color cur = JColorChooser.showDialog(null, "Select a Color", Color.BLACK);
                        if (cur != null) {
                            myc.changeColor(cur);
                        }
                    }

                    model.ChangeCurColor(myc.getColor());
                }
            });
            colorP.add(myc);
        }

        ColorChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color cur = JColorChooser.showDialog(null, "Select a Color", Color.BLACK);
                if (cur != null) {
                    model.ChangeCurColor(cur);
                }
            }
        });

        ColorChooser.setBounds(1,155,104,30);
        colorP.add(ColorChooser);


        StrokeThickness one = new StrokeThickness(1, model);
        one.setBounds(1, 185, 100, 30);
        mystrokes.add(one);

        StrokeThickness three = new StrokeThickness(3, model);
        three.setBounds(1, 210, 100, 30);
        mystrokes.add(three);

        StrokeThickness six = new StrokeThickness(6, model);
        six.setBounds(1,235,100,30);
        mystrokes.add(six);

        StrokeThickness nine = new StrokeThickness(9, model);
        nine.setBounds(1,260,100,30);
        mystrokes.add(nine);


        for(StrokeThickness p : mystrokes) {
            p.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    model.ChangeCurStroke(p.getThickness());
                }
            });
            colorP.add(p);
        }



        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(colorP);
        JLabel test = new JLabel("");

        test.setPreferredSize(new Dimension(104,20));
        this.add(test);

    }

    public void update(Object observable) {
    }
}