package mapGUI;

import run.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;

public class MapGUI extends JPanel implements MouseListener, MouseMotionListener {
    private static class MyRectangle extends Rectangle {
        private final double altitude;

        public MyRectangle(int x, int y, int width, int height, double altitude) {
            super(x, y, width, height);
            this.altitude = altitude;
        }
    }

    private static class Agent {
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

    private static class MyPolygon extends Polygon{
        private final String terrain;

        public MyPolygon(String terrain) {
            super();
            this.terrain = terrain;
        }
    }

    private final int windowWidth;
    private final int windowHeight;
    private final int squareSide;
    LinkedList<MyPolygon> polygons;
    LinkedList<MyRectangle> rectangles;
    LinkedList<Agent> agents;

    public MapGUI(int windowWidth, int windowHeight, int squareSide) {
        super();

        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.squareSide = squareSide;

        polygons = new LinkedList<>();
        rectangles = new LinkedList<>();
        agents = new LinkedList<>();
    }

    public void initialize() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().add(this);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setLayout(null);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        this.setLayout(null);
        frame.setVisible(true);
    }

    public void addPolygon(Vector<Integer> xs, Vector<Integer> ys, String terrain) {
        int n = xs.size();
        MyPolygon poly = new MyPolygon(terrain);
        for(int i = 0; i < n; i++)
            poly.addPoint(xs.get(i), ys.get(i));
        polygons.add(poly);
    }

    public void addRectangle(int x, int y, int width, int height, Double altitude) {
        MyRectangle rect = new MyRectangle(x, y, width, height, altitude);
        rectangles.add(rect);
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

        this.repaint();
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setSize(this.windowWidth, this.windowHeight);
        this.setLocation(0, 0);

        Iterator<MapGUI.MyRectangle> rectIterator = rectangles.iterator();
        MapGUI.MyRectangle rect;
        int red, green, blue;
        double rel;
        while (rectIterator.hasNext()) {
            rect = rectIterator.next();

            rel = rect.altitude;

            red = (int)Math.round(rel * 255);
            green = 255;
            blue = 0;

            g.setColor(new Color(red, green, blue));
            g.fillRect(rect.x, rect.y, rect.width, rect.height);
        }

        Iterator<MapGUI.MyPolygon> polyIterator = polygons.iterator();
        MapGUI.MyPolygon poly;
        Color color = new Color(0,0,0);
        while (polyIterator.hasNext()) {
            poly = polyIterator.next();
            if(poly.terrain.equals("forest"))
                color = new Color(0, 120, 0);
            else if(poly.terrain.equals("river"))
                color = new Color(0, 0, 120);
            g.setColor(color);
            g.fillPolygon(poly);
        }

        Color teamColor, moraleColor;

        MapGUI.Agent agent;
        //DO NOT DELETE THIS WARNING
        for(int i = 0; i < agents.size(); i++) {
            agent = agents.get(i);
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
    }

    @Override public Dimension getPreferredSize() {
        return new Dimension(this.windowWidth, this.windowHeight);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        app.addTroop(e.getX()/squareSide, e.getY()/squareSide);
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if ((x < windowWidth) && (x >= 0) && (y < windowHeight) && (y >= 0)) {
            app.addTroop(x/squareSide, y/squareSide);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {}
}