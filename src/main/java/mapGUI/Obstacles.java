package mapGUI;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

public class Obstacles extends JPanel{
    private class MyLine {
        private final int x1;
        private final int y1;
        private final int x2;
        private final int y2;

        public MyLine(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
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
    private int squareSide;
    LinkedList<MyPoint> points;
    LinkedList<MyLine> lines;
    LinkedList<Polygon> polygons;

    public Obstacles(int windowWidth, int windowHeight, int squareSide) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.squareSide = squareSide;

        points = new LinkedList<>();
        lines = new LinkedList<>();
        polygons = new LinkedList<>();
    }

    public void addPoint(int x, int y, int v) {
        MyPoint point = new MyPoint(x, y, v);
        points.add(point);
    }

    public void addLine(int x1, int y1, int x2, int y2) {
        MyLine line = new MyLine(x1, y1, x2, y2);
        lines.add(line);
    }

    public void addPolygon(Vector<Integer> xs, Vector<Integer> ys) {
        int n = xs.size();
        Polygon poly = new Polygon();
        for(int i = 0; i < n; i++)
            poly.addPoint(xs.get(i), ys.get(i));
        polygons.add(poly);
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

        Iterator<MyPoint> pointIterator = points.iterator();
        MyPoint point;
        while(pointIterator.hasNext()) {
            point = pointIterator.next();
            if(point.value == 1) {
                g.setColor(Color.RED);
                g.fillOval(point.x, point.y, 5, 5);
            }
        }
    }

    @Override public Dimension getPreferredSize() {
        return new Dimension(this.windowWidth, this.windowHeight);
    }
}