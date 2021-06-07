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

            Color teamColor, moraleColor;

            for (Agent agent : agents){
                int colorMag = (int)(255 * (1 - Math.pow(Math.abs(agent.color), 0.5)));
                if(agent.color < 0)
                    teamColor = new Color(255, colorMag, colorMag);
                else
                    teamColor = new Color(colorMag, colorMag,255);
                moraleColor = new Color((int)(255*agent.morale), (int)(255*agent.morale), (int)(255*agent.morale));

                switch(agent.typeA) {
                    case 0: {
                        g.setColor(teamColor);
                        g.fillRect(agent.x * squareSide, agent.y * squareSide, squareSide, squareSide);

                        int [] xTr1 = {agent.x * squareSide, (agent.x+1) * squareSide, agent.x * squareSide};
                        int [] yTr1 = {agent.y * squareSide, (agent.y+1) * squareSide, (agent.y+1) * squareSide};
                        Polygon tr1 = new Polygon(xTr1, yTr1, 3);

                        g.setColor(moraleColor);
                        g.fillPolygon(tr1);

                        break;
                    }
                    case 1:
                        g.setColor(teamColor);
                        g.fillOval(agent.x * squareSide, agent.y * squareSide, squareSide, squareSide);

                        g.setColor(moraleColor);
                        g.fillArc(agent.x * squareSide, agent.y * squareSide, squareSide, squareSide, 0, 180);

                        break;
                    case 2:
                        int [] xKw = {agent.x * squareSide + squareSide/2, (agent.x+1) * squareSide, agent.x * squareSide + squareSide/2, agent.x * squareSide};
                        int [] yKw = {agent.y * squareSide, agent.y * squareSide + squareSide/2, (agent.y+1) * squareSide, agent.y * squareSide + squareSide/2};
                        Polygon kw = new Polygon(xKw, yKw, 4);

                        g.setColor(teamColor);
                        g.fillPolygon(kw);

                        int [] xTr2 = {agent.x * squareSide + squareSide/2, (agent.x+1) * squareSide, agent.x * squareSide + squareSide/2};
                        int [] yTr2 = {agent.y * squareSide, agent.y * squareSide + squareSide/2, (agent.y+1) * squareSide};
                        Polygon tr2 = new Polygon(xTr2, yTr2, 3);

                        g.setColor(moraleColor);
                        g.fillPolygon(tr2);

                        break;
                    case 3:
                        int [] xTr3 = {agent.x * squareSide, agent.x * squareSide, (agent.x+1) * squareSide};
                        int [] yTr3 = {agent.y * squareSide, (agent.y+1) * squareSide, agent.y * squareSide + squareSide/2};
                        Polygon tr3 = new Polygon(xTr3, yTr3, 3);

                        g.setColor(teamColor);
                        g.fillPolygon(tr3);

                        int [] xTr4 = {agent.x * squareSide, agent.x * squareSide, (agent.x+1) * squareSide};
                        int [] yTr4 = {agent.y * squareSide, agent.y * squareSide + squareSide/2, agent.y * squareSide + squareSide/2};
                        Polygon tr4 = new Polygon(xTr4, yTr4, 3);

                        g.setColor(moraleColor);
                        g.fillPolygon(tr4);

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