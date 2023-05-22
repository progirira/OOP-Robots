package gui;

import gui.points.Position;
import gui.points.Target;

public class GameData {

    Position m_robotPosition = new Position(100, 100, 0);

    Target m_targetPosition = new Target(150, 100);

    static final double maxVelocity = 0.1;
    static final double maxAngularVelocity = 0.001;

    volatile double width = 150;
    volatile double height = 150;

}
