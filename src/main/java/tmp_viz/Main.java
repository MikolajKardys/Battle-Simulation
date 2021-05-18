package tmp_viz;

import java.awt.*;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Main {
    static class MyCanvas extends JComponent {
        private final int rows;
        private final int cols;

        private final int[][] elements;

        public MyCanvas(int rows, int cols){
            this.rows = rows;
            this.cols = cols;

            this.elements = new int [rows][cols];

            for (int i = 0; i < rows; i++)
                for (int j = 0; j < cols; j++)
                    this.elements[i][j] = -1;

        }

        public void setElements(int [] xTab, int [] yTab, int [] values){
            for (int i = 0; i < rows; i++)
                for (int j = 0; j < cols; j++)
                    this.elements[i][j] = -1;

            int len = xTab.length;
            for (int i = 0; i < len; i++)
                this.elements[xTab[i]][yTab[i]] = values[i];
        }

        public void paint(Graphics g) {
            int side = 30;

            int rows = this.rows;

            int cols = this.cols;
            int width = getSize().width;
            int height = getSize().height;

            for (int i = 0; i < rows; i++){
                for (int j = 0; j < cols; j++){
                    if (this.elements[i][j] >= 0)
                        g.setColor(Color.RED);
                    else{
                        g.setColor(Color.WHITE);
                    }
                    g.fillRect(i * side, j * side, side, side);
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

    public static void main(String[] a) throws InterruptedException {
        int rows = 20;
        int cols = 20;

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBounds(rows, cols, 30 * cols, 30 * rows);

        MyCanvas canvas = new MyCanvas(rows, cols);
        window.getContentPane().add(canvas);
        window.setVisible(true);

        int [] y = {5};
        int [] values = {1};
        for(int i = 0; i < 10; i++){
            int [] x = {i + 5};

            canvas.setElements(x, y, values);

            window.repaint();

            Thread.sleep(200);
        }
    }
}