
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class Model implements Runnable {
    /** The observers that are watching this model for changes. */
    private List<Observer> observers;
    private boolean newChange = false;

    private ArrayList<Item> items = new ArrayList<Item>();
    private Item curItem;
    private Color curColor = Color.black;
    private int curThickness = 1;
    private boolean inorder = true;
   // private boolean playBack = false;
    private int stopTime = 0;
    private File selectedFile = null;

    private JFileChooser jfc = new JFileChooser();

    private Canvas myCanvas;
    private Dimension initialSize;

    private boolean isplaying = false;
    private boolean isIsplayingback = false;



    /**
     * Create a new model.
     */
    public Model() {

        this.observers = new ArrayList();

        jfc.setDialogTitle("Select a file");
        jfc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Binary file", "bin");
        jfc.addChoosableFileFilter(filter);

        initialSize = new Dimension(600, 350);

    }

    /**
     * Add an observer to be notified when this model changes.
     */
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    /**
     * Remove an observer from this model.
     */
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    /**
     * Notify all observers that the model has changed.
     */
    public void notifyObservers() {
        for (Observer observer: this.observers) {
            observer.update(this);
        }
    }

    public void assignCanvas(Canvas cns) {
        myCanvas = cns;
    }



    public void ChangeCurColor(Color c) {
        curColor = c;
        notifyObservers();
    }

    public Color getCurColor() {
        return curColor;
    }

    public int getCurThickness() {
        return curThickness;
    }

    public void ChangeCurStroke(int sz) {
        curThickness = sz;
        notifyObservers();;
    }

    public boolean MadeNewChanges() {
        return newChange;
    }


    public void createNew() {
        items.clear();
        curItem = null;
        curColor = Color.black;
        curThickness = 1;
        inorder = true;
        stopTime = 0;
        selectedFile = null;
        newChange = false;

        notifyObservers();

    }


    public void load() {
        int ret = jfc.showOpenDialog(null);

        if(ret == JFileChooser.APPROVE_OPTION) {
            selectedFile = jfc.getSelectedFile();

            try {
                ObjectInputStream readIn = new ObjectInputStream(new FileInputStream(selectedFile));

                items = (ArrayList<Item>)readIn.readObject();
                initialSize = (Dimension)readIn.readObject();

                readIn.close();

                newChange = false;
                stopTime = 100;
                inorder = true;

                notifyObservers();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }


        }
    }

    public void save() {


        if (selectedFile != null) {
            try {
                ObjectOutputStream writeTo = new ObjectOutputStream(new FileOutputStream(selectedFile));

                writeTo.writeObject(items);
                writeTo.writeObject(initialSize);
                //  outStream.writeObject(preferredSize);
                writeTo.close();

                newChange = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            int ret = jfc.showSaveDialog(null);
            if (ret == JFileChooser.APPROVE_OPTION) {
                selectedFile = jfc.getSelectedFile();
                String sname = selectedFile.getName();
                if(!sname.endsWith(".bin")) {
                    selectedFile = new File(selectedFile.toString() + ".bin" );
                }

                try {
                    ObjectOutputStream writeTo = new ObjectOutputStream(new FileOutputStream(selectedFile));

                    writeTo.writeObject(items);
                    writeTo.writeObject(initialSize);
                    //  outStream.writeObject(preferredSize);
                    writeTo.close();

                    newChange = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    public void savePoints() {
        Rectangle visible = myCanvas.getVisibleRect();
        int wid = (int)Math.round(visible.getWidth());
        int ht = (int)Math.round(visible.getHeight());

        int initX = (int)Math.round(initialSize.getWidth());
        int initY = (int)Math.round(initialSize.getHeight());

        double tmp = (double)wid;
        double tmp2 = (double)ht;

        double scaleWidth = tmp / initX;
        double scaleHeight = tmp2 / initY;

        for(Item it : items) {
            it.updatePoints(scaleWidth, scaleHeight);
        }


        initialSize = new Dimension(wid,ht);
    }


    public void drawStart(Point p) {

        savePoints();

        if(stopTime > 0 && stopTime < 100) {
            int remain = totalPoints() * stopTime / 100;
            int cut = -1;
            for(int i = 0; i < items.size(); ++i) {
                items.get(i).dropPoints(remain);
                remain -= items.get(i).size();
                if(remain == 0) {
                    cut = i;
                    break;
                }
            }

            while(cut + 1 < items.size()) {
                items.remove(cut + 1);
            }

            stopTime = 100;

        } else if (stopTime <= 0) {
            items.clear();
            stopTime = 100;
        }
        curItem = new Item(p, curColor, curThickness);
        items.add(curItem);
        newChange = true;
        notifyObservers();;

    }

    public void drawContinue(Point p) {
        curItem.addPoint(p);
        notifyObservers();;
    }

    public void drawEnd(Point p) {
        curItem.addPoint(p);
        curItem = null;
        notifyObservers();;

    }

    public int totalPoints() {
        int total = 0;
        for (Item i : items) {
            total += i.size();
        }
        return total;
    }

    public int totalItems() {
        return items.size();
    }


    public void drawItems(Graphics2D g) {


            Rectangle visible = myCanvas.getVisibleRect();
            int wid = (int) Math.round(visible.getWidth());
            int ht = (int) Math.round(visible.getHeight());


            int initX = (int) Math.round(initialSize.getWidth());
            int initY = (int) Math.round(initialSize.getHeight());

            double tmp = (double) wid;
            double tmp2 = (double) ht;

            double scaleWidth = tmp / initX;
            double scaleHeight = tmp2 / initY;


            if (inorder) {

                if(stopTime >= 100) {
                    for (Item i : items) {
                        i.draw(g, true, scaleWidth, scaleHeight);
                    }
                } else {
                    int remain = totalPoints() * stopTime / 100;
                    for(Item i: items) {
                        i.drawUpTo(g, remain, true, scaleWidth, scaleHeight);
                        remain -= i.size();
                        if(remain < 0) {
                            break;
                        }
                    }
                }
            } else {

                int remain = totalPoints() * stopTime / 100;
                remain = totalPoints() - remain;

                for (int i = items.size() - 1; i >= 0; --i) {
                    items.get(i).drawUpTo(g, remain, false, scaleWidth, scaleHeight);
                    remain -= items.get(i).size();
                    if (remain < 0) {
                        break;
                    }
                }
            }

    }

    public void setPlayOrder(boolean order) {
        inorder = order;
    }

    public int getStopTime() {
        return stopTime;
    }

    public void setStopTime(int value) {
        stopTime = value;
    }

    public boolean isIsplaying() {
        return isplaying;
    }

    public boolean isIsplayingback() {
        return isIsplayingback;
    }

    public void run() {
        if (items.size() == 0) {
            return;
        }


        savePoints();


        Item it = items.get(items.size() - 1);
        Point p = it.lastPoint();
        it.addPoint(p);
        it.drop();

        if (inorder) {
            isplaying = true;
            if (stopTime >= 100 || stopTime <= 0) {
                stopTime = 0;
            }

            while (stopTime <= 100) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    System.out.println("not done");
                }

                if(stopTime == 100) {
                    isplaying = false;
                }
                notifyObservers();
                stopTime += 2;
            }



        } else {
            stopTime = 100;
            isIsplayingback = true;



            while(stopTime >= 0) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    System.out.println("not done");
                }

                if (stopTime < 2) {
                    isIsplayingback = false;
                }
                notifyObservers();
                stopTime -= 2;
            }



         //   System.out.println(stopTime);
        }

    }

    public void drawUpTo(int value) {
        savePoints();
     //   System.out.println("drawupto");
        stopTime = value;
        notifyObservers();
    }




}

