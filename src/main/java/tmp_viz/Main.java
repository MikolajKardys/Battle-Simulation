package tmp_viz;

import java.awt.*;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Main {
    static final int side = 10;

    static class MyCanvas extends JComponent {
        private final int rows;
        private final int cols;

        private final double[][] colors;
        private final double[][] morale;

        private final JFrame window;

        public MyCanvas(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;

            this.colors = new double[rows][cols];
            this.morale = new double[rows][cols];

            for (int i = 0; i < rows; i++)
                for (int j = 0; j < cols; j++)
                    this.colors[i][j] = 0;

            window = new JFrame();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setBounds(rows, cols, (side + 1) * cols, (side + 1) * rows);
            window.getContentPane().add(this);
            window.setVisible(true);
        }

        public void setElements(int[] xTab, int[] yTab, double[] values, double[] morale) {
            for (int i = 0; i < rows; i++)
                for (int j = 0; j < cols; j++)
                    this.colors[i][j] = 0;

            int len = xTab.length;
            for (int i = 0; i < len; i++){
                this.colors[xTab[i]][yTab[i]] = values[i];
                this.morale[xTab[i]][yTab[i]] = morale[i];
            }
        }

        public void paint(Graphics g) {
            int rows = this.rows;
            int cols = this.cols;

            int width = getSize().width;
            int height = getSize().height;

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    double currColor = this.colors[i][j];
                    Color fieldColor;
                    int fade = (int)(255 * (1 - Math.sqrt(Math.abs(currColor))));
                    if (currColor == 0)
                        fieldColor = new Color(255, 255, 255);
                    else {
                        if (currColor > 0)
                            fieldColor = new Color(255, fade, fade);
                        else
                            fieldColor = new Color(fade, fade, 255);
                    }
                    g.setColor(fieldColor);
                    g.fillRect(j * side, i * side, side, side);

                    if (currColor != 0){
                        g.setColor(new Color(0, (int)(255 * morale[i][j]), 0));

                        int [] x = {j * side, (j + 1) * side, j * side};
                        int [] y = {i * side, (i + 1) * side, (i + 1) * side};
                        Polygon tr = new Polygon(x, y, 3);
                        g.fillPolygon(tr);
                    }
                }
            }

            g.setColor(Color.BLACK);
            // draw the rows
            for (int i = 0; i < rows; i++)
                g.drawLine(0, i * side, width, i * side);

            // draw the columns
            for (int i = 0; i < cols; i++)
                g.drawLine(i * side, 0, i * side, height);
        }
    }

    private final MyCanvas canvas;

    public Main (int rows, int cols){
        canvas = new MyCanvas(rows, cols);
    }

    public void repaint(int [] xCords, int [] yCords, double [] colors, double [] morale){
        canvas.setElements(xCords, yCords, colors, morale);

        canvas.window.repaint();
    }
}