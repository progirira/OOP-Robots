package gui;

import java.awt.*;
import java.io.File;

import javax.swing.JPanel;

public class LogCoordinatesWindow extends WindowWithPathState {
    private StringBuffer logs;
    private TextArea textArea;
    private Integer maxNumberOfSymbols;

    public LogCoordinatesWindow() {
        super("Координаты робота", new File(".", "robotCoordinatesFile.bin"), true, true, true, true);
        maxNumberOfSymbols = 100;
        logs = new StringBuffer(maxNumberOfSymbols);;
        textArea = new TextArea("");
        textArea.setSize(400, 500);
        textArea.setText(logs.toString());

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(textArea, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    public void update(Point p) {

        logs.append(p.x).append(", ").append(p.y).append("\n");
        if (logs.length() >= maxNumberOfSymbols)
            logs = new StringBuffer();

        textArea.setText(logs.toString());
        textArea.invalidate();

    }
}
