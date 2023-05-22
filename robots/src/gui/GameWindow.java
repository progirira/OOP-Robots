package gui;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JPanel;

public class GameWindow extends WindowWithPathState
{
    private final GameVisualizer m_visualizer;
    public GameWindow(GameLogic gameLogic)
    {
        super("Игровое поле", new File(".",  "gameFile.bin"), true, true, true, true);
        m_visualizer = new GameVisualizer(gameLogic);
        m_visualizer.setSize(400, 400);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

}
