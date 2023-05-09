package gui;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JPanel;

public class GameWindow extends WindowWithPathState
{
    private final GameVisualizer m_visualizer;
    public GameWindow() 
    {
        super("Игровое поле", new File(".",  "gameFile.bin"), true, true, true, true);
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

}
