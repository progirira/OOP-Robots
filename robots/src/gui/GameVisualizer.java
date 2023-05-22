package gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import static java.lang.Math.min;

public class GameVisualizer extends JPanel
{
    GameLogic gameLogic;
    private final Timer m_timer = initTimer();

    private static Timer initTimer() 
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    public GameVisualizer(GameLogic gameLogic1)
    {
        gameLogic = gameLogic1;
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onRedrawEvent();
            }
        }, 0, 50);
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                gameLogic.onModelUpdateEvent();
            }
        }, 0, 10);
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                Point point = e.getPoint();
                double resolution = Toolkit.getDefaultToolkit().getScreenResolution() / 150.0;
                point.x = (int) (point.x / resolution);
                point.y = (int) (point.y / resolution);
                gameLogic.setTargetPosition(point);
                repaint();
            }
        });
        setDoubleBuffered(true);
    }

    
    protected void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }

    private static int round(double value)
    {
        return (int)(value + 0.5);
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        Point currentPoint = gameLogic.getRobotPosition();
        drawRobot(g2d, round(currentPoint.x), round(currentPoint.y), gameLogic.getRobotDirection());
        Point targetPoint = gameLogic.getTargetPosition();
        drawTarget(g2d, targetPoint.x, targetPoint.y);
    }
    
    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private void drawRobot(Graphics2D g, int x, int y, double direction)
    {
//        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
//        int width = dimension.width;
//        if (x < 0)
//            x = 0;
//        int robotCenterX = min(x, width);
//
//        int height = dimension.height;
//        if (y < 0)
//            y = 0;
//        int robotCenterY = min(y, height);

        gameLogic.modifyDimension();
        int width = gameLogic.getWidth();
        if (x < 0)
            x = 0;
        int robotCenterX = min(x, width);

        int height = gameLogic.getHeight();
        if (y < 0)
            y = 0;
        int robotCenterY = min(y, height);

        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY); 
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
    }

    
    private void drawTarget(Graphics2D g, int x, int y)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0); 
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }
}