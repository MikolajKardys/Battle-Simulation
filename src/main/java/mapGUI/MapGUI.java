package mapGUI;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class MapGUI extends JPanel {
    private class Line {
        private final int x1;
        private final int y1;
        private final int x2;
        private final int y2;

        public Line(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
    }

    private class MyRectangle extends Rectangle {
        private final double altitude;

        public MyRectangle(int x, int y, int width, int height, double altitude) {
            super(x, y, width, height);
            this.altitude = altitude;
        }
    }

    private class MyPoint {
        private final int x;
        private final int y;
        private final int value;

        public MyPoint(int x, int y, int value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }
    }

    private final int windowWidth;
    private final int windowHeight;
    LinkedList<MyPoint> points;
    LinkedList<Line> lines;
    LinkedList<Polygon> polygons;
    LinkedList<MyRectangle> rectangles;
    private boolean biom = true;

    public MapGUI(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        points = new LinkedList<>();
        lines = new LinkedList<>();
        polygons = new LinkedList<>();
        rectangles = new LinkedList<>();
    }

    public void addPoint(int x, int y, int v) {
        MyPoint point = new MyPoint(x, y, v);
        points.add(point);
    }

    public void addLine(int x1, int y1, int x2, int y2) {
        Line line = new Line(x1, y1, x2, y2);
        lines.add(line);
    }

    public void addPolygon(Vector<Integer> xs, Vector<Integer> ys) {
        int n = xs.size();
        Polygon poly = new Polygon();
        for(int i = 0; i < n; i++)
            poly.addPoint(xs.get(i), ys.get(i));
        polygons.add(poly);
    }

    public void addRectangle(int x, int y, int width, int height, Double altitude) {
        MyRectangle rect = new MyRectangle(x, y, width, height, altitude);
        rectangles.add(rect);
    }

    public void drawMap() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().add(this);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setLayout(null);

        this.setLayout(null);
        frame.setVisible(true);
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setSize(this.windowWidth, this.windowHeight);
        this.setLocation(0, 0);

        Iterator<MyRectangle> rectIterator = rectangles.iterator();
        MyRectangle rect;
        int red, green, blue;
        double rel;
        while (rectIterator.hasNext()) {
            rect = rectIterator.next();

            if(rect.altitude > 0.8) {
                rel = 5*(rect.altitude-0.8);

                red = 255;
                green = 255-(int)Math.round(rel * 255);
                blue = 0;
            }
            else {
                rel = rect.altitude * 1.25;

                red = (int)Math.round(rel * 255);
                green = 255;
                blue = 0;
            }
            g.setColor(new Color(red, green, blue));
            g.fillRect(rect.x, rect.y, rect.width, rect.height);
        }

        if(biom) {
            Iterator<Polygon> polyIterator = polygons.iterator();
            Polygon poly;
            Color color = new Color(0, 120, 0);
            while (polyIterator.hasNext()) {
                poly = polyIterator.next();
                g.setColor(color);
                g.fillPolygon(poly);
            }

            /*Iterator<MyPoint> pointIterator = points.iterator();
            MyPoint point;
            while(pointIterator.hasNext()) {
                point = pointIterator.next();
                if(point.value == 1) {
                    g.setColor(Color.BLUE);
                    g.fillOval(point.x, point.y, 5, 5);
                }
                else if(point.value == 2) {
                    g.setColor(Color.RED);
                    g.fillOval(point.x, point.y, 5, 5);
                }
            }*/
        }
        else {
            /*Iterator<MyRectangle> rectIterator = rectangles.iterator();
            MyRectangle rect;
            int red, green, blue;
            double rel;
            while (rectIterator.hasNext()) {
                rect = rectIterator.next();

                if(rect.altitude > 0.8) {
                    rel = 5*(rect.altitude-0.8);

                    red = 255;
                    green = 255-(int)Math.round(rel * 255);
                    blue = 0;
                }
                else {
                    rel = rect.altitude * 1.25;

                    red = (int)Math.round(rel * 255);
                    green = 255;
                    blue = 0;
                }
                g.setColor(new Color(red, green, blue));
                g.fillRect(rect.x, rect.y, rect.width, rect.height);
            }*/
        }
    }

    @Override public Dimension getPreferredSize() {
        return new Dimension(this.windowWidth, this.windowHeight);
    }
}