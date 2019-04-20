import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable {

    private ArrayList<Point> points = new ArrayList<Point>();

    private Color c;
    private int thickness;

    public Item(Point p, Color co, int sz) {
        points.add(p);
        c = co;
        thickness = sz;
    }

    public void addPoint(Point p) {
        points.add(p);
    }

    public Point lastPoint() {
        return points.get(points.size() - 1);
    }

    public void drop() {
        points.remove(points.size() - 1);
    }

    public int size() {
        return points.size();
    }

    public void draw(Graphics2D g, boolean inorder, double scaleX, double scaleY) {
        g.setStroke(new BasicStroke(thickness));
        g.setColor(c);
        if(inorder) {
            for (int i = 0; i < points.size() - 1; ++i) {
                Point p1 = points.get(i);
                Point p2 = points.get(i+1);
                if(scaleX != 1.0 || scaleY != 1.0) {

                    p1 = new Point((int) Math.round(p1.x * scaleX), (int) Math.round(p1.y * scaleY));
                    p2 = new Point((int) Math.round(p2.x * scaleX), (int) Math.round(p2.y * scaleY));

                }
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        } else {
            for (int i = points.size() - 1; i >= 1; --i) {
                Point p1 = points.get(i);
                Point p2 = points.get(i-1);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
    }


    public void drawUpTo(Graphics2D g, int remain, boolean inorder, double scaleX, double scaleY) {
        g.setStroke(new BasicStroke(thickness));
        g.setColor(c);
        int num = 0;

        if(inorder) {
            for (int i = 0; i < points.size() - 1; ++i) {
                if (num >= remain) {
                    break;
                }
                Point p1 = points.get(i);
                Point p2 = points.get(i+1);
                if(scaleX != 1.0 || scaleY != 1.0) {

                    p1 = new Point((int) Math.round(p1.x * scaleX), (int) Math.round(p1.y * scaleY));
                    p2 = new Point((int) Math.round(p2.x * scaleX), (int) Math.round(p2.y * scaleY));

                }
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
                num = num + 1;

            }
        } else {
            //  System.out.println("item up to");
            for (int i = points.size() - 1; i >= 1; --i) {
                if (num >= remain) {
                    break;
                }
                Point p1 = points.get(i);
                Point p2 = points.get(i-1);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
                num = num + 1;

            }
        }
    }

    public void dropPoints(int remain) {
        int sz = points.size() - remain;
        if (sz > 0) {
            int counter = 0;
            while (counter < sz) {
                drop();
                ++counter;
            }
        }
    }

    public void updatePoints(double scaleX, double scaleY) {

        if(scaleX != 1.0 && scaleY != 1.0) {

            for (int i = 0; i < points.size(); ++i) {
                Point p1 = points.get(i);

                p1 = new Point((int) Math.round(p1.x * scaleX), (int) Math.round(p1.y * scaleY));

                points.set(i,p1);

            }
        }

    }

}