package mapGUI;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class MapGUI extends JPanel {
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

    private class Agent {
        private final int x;
        private final int y;

        private final double color;
        private final int typeA;
        private final double morale;

        public Agent(int x, int y, double color, int typeA, double morale) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.typeA = typeA;
            this.morale = morale;
        }
    }

    private final int windowWidth;
    private final int windowHeight;
    private int squareSide;
    LinkedList<MyPoint> points;
    LinkedList<MyLine> lines;
    LinkedList<Polygon> polygons;
    LinkedList<MyRectangle> rectangles;
    LinkedList<Agent> agents;
    private boolean biom = true;

    public MapGUI(int windowWidth, int windowHeight, int squareSide) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.squareSide = squareSide;

        points = new LinkedList<>();
        lines = new LinkedList<>();
        polygons = new LinkedList<>();
        rectangles = new LinkedList<>();
        agents = new LinkedList<>();
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

    public void paintMap(int[] xs, int[] ys, double[] colors, int[] typeA, double[] morale) {
        agents.clear();

        int agentNum = xs.length;
        Agent agent;
        for(int i = 0; i < agentNum; i++) {
            if(colors[i] != 0) {
                agent = new Agent(xs[i], ys[i], colors[i], typeA[i], morale[i]);
                agents.add(agent);
            }
        }
        repaint();
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

            for (Agent agent : agents){
                int colorMag = (int)(255 * (1 - Math.pow(Math.abs(agent.color), 0.5)));
                switch(agent.typeA) {
                    case 0: {
                        if(agent.color < 0) {
                            color = new Color(255, colorMag, colorMag);
                        }
                        else {
                            color = new Color(colorMag, colorMag,255);
                        }

                        g.setColor(color);
                        g.fillRect(agent.x * squareSide, agent.y * squareSide, squareSide, squareSide);

                        int [] x = {agent.x * squareSide, (agent.x+1) * squareSide, agent.x * squareSide};
                        int [] y = {agent.y * squareSide, (agent.y+1) * squareSide, (agent.y+1) * squareSide};
                        Polygon tr = new Polygon(x, y, 3);

                        color = new Color((int)(255*agent.morale),(int)(255*agent.morale),(int)(255*agent.morale));
                        g.setColor(color);

                        g.fillPolygon(tr);

                        break;
                    }
                    case 1:
                        if(agent.color < 0) {
                            color = new Color(255,255-(int)(-255*agent.color),255-(int)(-255*agent.color));
                        }
                        else {
                            color = new Color(255-(int)(255*agent.color),255-(int)(255*agent.color),255);
                        }
                        g.setColor(color);
                        g.fillOval(agent.x * squareSide + squareSide/2, agent.y * squareSide + squareSide/2, squareSide/2, squareSide/2);


                        color = new Color((int)(255*agent.morale),(int)(255*agent.morale),(int)(255*agent.morale));
                        g.setColor(color);

                        g.fillArc(agent.x * squareSide + squareSide/2, agent.y * squareSide + squareSide/2, squareSide/2, squareSide/2, 0, 180);

                        break;
                    case 2:
                        if(agent.color < 0) {
                            color = new Color(255,255-(int)(-255*agent.color),255-(int)(-255*agent.color));
                        }
                        else {
                            color = new Color(255-(int)(255*agent.color),255-(int)(255*agent.color),255);
                        }
                        g.setColor(color);
                        g.fillOval(agent.x * squareSide + squareSide/2, agent.y * squareSide + squareSide/2, squareSide/2, squareSide/2);


                        color = new Color((int)(255*agent.morale),(int)(255*agent.morale),(int)(255*agent.morale));
                        g.setColor(color);

                        g.fillArc(agent.x * squareSide + squareSide/2, agent.y * squareSide + squareSide/2, squareSide/2, squareSide/2, 0, 180);

                        break;
                    case 3:
                        if(agent.color < 0) {
                            color = new Color(255,255-(int)(-255*agent.color),255-(int)(-255*agent.color));
                        }
                        else {
                            color = new Color(255-(int)(255*agent.color),255-(int)(255*agent.color),255);
                        }
                        g.setColor(color);
                        g.fillOval(agent.x * squareSide + squareSide/2, agent.y * squareSide + squareSide/2, squareSide/2, squareSide/2);


                        color = new Color((int)(255*agent.morale),(int)(255*agent.morale),(int)(255*agent.morale));
                        g.setColor(color);

                        g.fillArc(agent.x * squareSide + squareSide/2, agent.y * squareSide + squareSide/2, squareSide/2, squareSide/2, 0, 180);

                        break;
                    default:
                        break;
                }
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