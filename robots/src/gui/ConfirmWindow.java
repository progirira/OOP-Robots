package gui;

import javax.swing.*;
import java.awt.*;

public class ConfirmWindow {
    public static int confirmExit(Component component){
        return JOptionPane.showOptionDialog(component, "Хотите выйти?", "Выход",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, new Object[]{"Да", "Нет"}, "Да");
    }
    public static int confirmRestore(Component component) {
        return JOptionPane.showOptionDialog(component, "Восстановить?",
                "Восстановить сохранённое состояние приложения?", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,
                new Object[]{"Да", "Нет"}, "Да");
    }
}