package gui;

import javax.swing.*;
import java.awt.*;

public class ConfirmWindow {
    static PropertyResource resources = new PropertyResource();
    public static int confirmExit(Component component){
        return JOptionPane.showOptionDialog(component, resources.getProperty("cancelButton"),
                (String) resources.getProperty("cancelWindow"), JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, new Object[]{resources.getProperty("yesButton"),
                resources.getProperty("noButton")}, resources.getProperty("yesButton"));
    }
}