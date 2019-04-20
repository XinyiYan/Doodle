import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class PlayBack extends JPanel implements Observer {

    private Model model;
    private JButton play = new JButton("Play");
    private JButton playback = new JButton("Start(PlayBack)");

    private ImageIcon playIcon = new ImageIcon("src/playBtn.png");
    private ImageIcon playBackIcon = new ImageIcon("src/playBack.png");
    private ImageIcon endIcon = new ImageIcon("src/endBtn.png");

    private JButton end = new JButton("End");

    private JSlider slider = new JSlider(JSlider.HORIZONTAL,0, 100, 100);

    public PlayBack(Model model) {
        this.model = model;

        play.setIcon(playIcon);

        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("Click play");
                model.setPlayOrder(true);

                Thread p = new Thread(model);
                p.start();


            }
        });

        playback.setIcon(playBackIcon);
        playback.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setPlayOrder(false);
                Thread p = new Thread(model);
                p.start();

            }
        });

        end.setIcon(endIcon);
        end.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                model.setPlayOrder(true);
                model.drawUpTo(100);

            }
        });


        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(slider);
        this.add(Box.createRigidArea(new Dimension(120,25)));
        this.add(play);
        this.add(Box.createRigidArea(new Dimension(15,25)));
        this.add(playback);
        this.add(Box.createRigidArea(new Dimension(15,25)));
        this.add(end);

        slider.setValue(0);
        slider.setPaintTicks(true);

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = slider.getValue();
                model.setPlayOrder(true);
               // System.out.println(value);
                model.drawUpTo(value);
            }
        });

        if (model.totalItems() == 0) {
            play.setEnabled(false);
            playback.setEnabled(false);
            end.setEnabled(false);
        }

    }



    public void update(Object observable) {
        // XXX Fill this in with the logic for updating the view when the model
        // changes.
        // model.
      //  System.out.println("Model changed!");

        int items = this.model.totalItems();
        if (items >= 1) {
          //  int num = (int)Math.ceil(100.f / items);
            slider.setMajorTickSpacing(100 / items);
        } else {
            slider.setMajorTickSpacing(0);
        }

        if (items == 0) {
            play.setEnabled(false);
            playback.setEnabled(false);
            end.setEnabled(false);
        }

        if (model.getStopTime() >= 100) {
            end.setEnabled(false);
        } else if (items > 0 && !this.model.isIsplayingback() && !this.model.isIsplaying()) {
            end.setEnabled(true);
        }

        if (this.model.isIsplaying()) {
            playback.setEnabled(false);

        } else if (items > 0) {
            playback.setEnabled(true);

        }

        if (this.model.isIsplayingback()) {
            play.setEnabled(false);

        } else if (items > 0) {
            play.setEnabled(true);

        }

        slider.setValue(this.model.getStopTime());
        repaint();
    }
}