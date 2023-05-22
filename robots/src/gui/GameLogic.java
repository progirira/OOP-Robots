package gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class GameLogic {
    private static final GameData gameData = new GameData();
    private ArrayList<WindowWithPathState> observers;
    private final Timer m_timer = initTimer();

    private Boolean ifGameStarted;

    protected void modifyDimension(){
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        gameData.width = dimension.width;
        gameData.height = dimension.height;
    }

    protected int getWidth(){
        return (int)gameData.width;
    }

    protected int getHeight(){
        return (int)gameData.height;
    }
    private static Timer initTimer()
    {
        Timer timer = new Timer("notifies generator", true);
        return timer;
    }

    GameLogic() {
        observers = new ArrayList<>();
        ifGameStarted = false;
    }

    protected void setTargetPosition(Point p)
    {
        gameData.m_targetPosition.setX(p.x);
        gameData.m_targetPosition.setY(p.y);
        if (!ifGameStarted){
            ifGameStarted = true;
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                notifyObservers();
            }
        }, 0, 100);
        }
    }

    protected Point getTargetPosition()
    {
        return new Point((int)gameData.m_targetPosition.getX(), (int)gameData.m_targetPosition.getY());
    }
    protected Point getRobotPosition()
    {
        return new Point((int) gameData.m_robotPosition.getX(), (int) gameData.m_robotPosition.getY());
    }

    protected double getRobotDirection()
    {
        return gameData.m_robotPosition.getDirection();
    }
    private static double distance(double x1, double y1, double x2, double y2)
    {

        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    private static double asNormalizedRadians(double angle)
    {
        while (angle < 0)
            angle += 2*Math.PI;

        while (angle >= 2*Math.PI)
            angle -= 2*Math.PI;

        return angle;
    }

    protected void onModelUpdateEvent()
    {

        double distance = distance(gameData.m_targetPosition.getX(), gameData.m_targetPosition.getY(),
                gameData.m_robotPosition.getX(), gameData.m_robotPosition.getY());
        if (distance < 0.5)
            return;

        double velocity = gameData.maxVelocity;
        double angleToTarget = angleTo(gameData.m_robotPosition.getX(), gameData.m_robotPosition.getY(), gameData.m_targetPosition.getX(), gameData.m_targetPosition.getY());
        double angularVelocity = 0;
        if (angleToTarget > gameData.m_robotPosition.getDirection())
            angularVelocity = gameData.maxAngularVelocity;

        if (angleToTarget < gameData.m_robotPosition.getDirection())
            angularVelocity = -gameData.maxAngularVelocity;


        moveRobot(velocity, angularVelocity, 10);

    }

    private void notifyObservers(){
        for (WindowWithPathState window : this.observers) {
            Point p = new Point((int) gameData.m_robotPosition.getX(), (int) gameData.m_robotPosition.getY());
            window.update(p);
        }
    }

    private static double applyLimits(double value, double min, double max)
    {
        if (value < min)
            return min;
        return Math.min(value, max);
    }

    private void moveRobot(double velocity, double angularVelocity, double duration)
    {
        velocity = applyLimits(velocity, 0, gameData.maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -gameData.maxAngularVelocity, gameData.maxAngularVelocity);
        double newX = gameData.m_robotPosition.getX() + velocity / angularVelocity *
                (Math.sin(gameData.m_robotPosition.getDirection()  + angularVelocity * duration) -
                        Math.sin(gameData.m_robotPosition.getDirection()));
        if (!Double.isFinite(newX))
            newX = gameData.m_robotPosition.getX() + velocity * duration * Math.cos(gameData.m_robotPosition.getDirection());

        double newY = gameData.m_robotPosition.getY() - velocity / angularVelocity *
                (Math.cos(gameData.m_robotPosition.getDirection() + angularVelocity * duration) -
                        Math.cos(gameData.m_robotPosition.getDirection()));
        if (!Double.isFinite(newY))
            newY = gameData.m_robotPosition.getY() + velocity * duration * Math.sin(gameData.m_robotPosition.getDirection());

        gameData.m_robotPosition.setX(newX);
        gameData.m_robotPosition.setY(newY);
        double newDirection = asNormalizedRadians(gameData.m_robotPosition.getDirection() + angularVelocity * duration);
        gameData.m_robotPosition.setDirection(newDirection);
    }

    public void addObserver(WindowWithPathState window) {
        this.observers.add(window);
    }

    public void removeObserver(WindowWithPathState window) {
        this.observers.remove(window);
    }

}
