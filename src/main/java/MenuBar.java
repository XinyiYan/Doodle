
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class MenuBar extends JMenuBar implements Observer {

    private Model model;

    private JMenuItem newdoodle = new JMenuItem("New");
   // private JMenuItem saveAs = new JMenuItem("Save As");
    private JMenuItem save = new JMenuItem("Save");
    private JMenuItem load = new JMenuItem("Load");
    private JMenuItem exit = new JMenuItem("Exit");


    /**
     * Create a new View.
     */
    public MenuBar(Model model) {

        this.model = model;
        JMenu file = new JMenu("File");

        newdoodle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(model.MadeNewChanges()) {
                    int options = JOptionPane.YES_NO_OPTION;
                    int dialogResult = JOptionPane.showConfirmDialog (null, "Do you want to save changes?","Warning",options);
                    if(dialogResult == JOptionPane.YES_OPTION){
                        model.save();
                    }
                    model.createNew();
                } else {
                    model.createNew();
                }
            }
        });

        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(model.MadeNewChanges()) {
                    int options = JOptionPane.YES_NO_OPTION;
                    int dialogResult = JOptionPane.showConfirmDialog (null, "Do you want to save changes?","Warning",options);
                    if(dialogResult == JOptionPane.YES_OPTION){
                        model.save();
                    }
                    model.load();
                } else {
                    model.load();
                }
            }
        });


        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.save();
            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(model.MadeNewChanges()) {
                    int options = JOptionPane.YES_NO_OPTION;
                    int dialogResult = JOptionPane.showConfirmDialog (null, "Do you want to save changes?","Warning",options);
                    if(dialogResult == JOptionPane.YES_OPTION){
                     model.save();
                    }
                    System.exit(0);
                } else {
                    System.exit(0);
                }
        }});


        file.add(newdoodle);
        file.add(load);
        file.add(save);
        file.add(exit);


        this.add(file);

    }

    /**
     * Update with data from the model.
     */
    public void update(Object observable) {
      // System.out.println("Model changed!");
    }
}
