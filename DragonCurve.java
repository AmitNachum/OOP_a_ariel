import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class DragonCurve extends JPanel
{
    public void drawDragonCurve(Graphics2D g , int depth , double x1 , double y1 ,double x2 , double y2 , boolean leftTurn)
    {
        if (depth == 0)
            g.draw(new Line2D.Double(x1,y1,x2,y2));

        else
        {
            double midX = (x1 + x2) / 2 + (leftTurn ? (y2 - y1) / 2 : -(y2 - y1) / 2);
            double midY = (y1 + y2) / 2 + (leftTurn ? -(x2 - x1) / 2 : (x2 - x1) / 2);

            drawDragonCurve(g , depth - 1 , x1, y1 , midX , midY ,true );
            drawDragonCurve(g , depth - 1 , midX, midY , x2 , y2 ,false );
        }

    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setColor(Color.CYAN);

        int depth = 17;
        int startX = 300, startY = 300;
        int endX = 500, endY = 300;
        drawDragonCurve(g2d, depth, startX, startY, endX, endY , true);
    }


    public static void main(String... args)
    {

        JFrame frame = new JFrame("Dragon Curve");
        DragonCurve dragonCurve = new DragonCurve();
        frame.add(dragonCurve);
        frame.pack();
        frame.setSize(800 , 500);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
        frame.repaint();

    }
}
