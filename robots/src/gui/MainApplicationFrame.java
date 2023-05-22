package gui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

import log.Logger;


public class MainApplicationFrame extends JFrame {
    GameLogic gameLogic;
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame() {
        gameLogic = new GameLogic();
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);

        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = createGameWindow(gameLogic);
        addWindow(gameWindow);

        LogCoordinatesWindow logCoorsWindow = createlogCoorsWindow();
        addWindow(logCoorsWindow);

        if (confirmRestore()){
            gameWindow.load();
            logWindow.load();
            logCoorsWindow.load();
        }
        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private Boolean confirmRestore() {
        return JOptionPane.showOptionDialog(this, "Восстановить?",
                "Восстановить сохранённое состояние приложения?", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,
                new Object[]{"Да", "Нет"}, "Да") == 0;
    }

    private static int confirmExit(Component component){
        return JOptionPane.showOptionDialog(component, "Хотите выйти?", "Выход",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, new Object[]{"Да", "Нет"}, "Да");
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected GameWindow createGameWindow(GameLogic gameLogic) {
        return new GameWindow(gameLogic);
    }

    protected LogCoordinatesWindow createlogCoorsWindow() {
        LogCoordinatesWindow logCoorsWindow = new LogCoordinatesWindow();
        gameLogic.addObserver(logCoorsWindow);
        return logCoorsWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
// 
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
// 
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        return menuBar;
//    }

    private JMenuItem generateMenuItem(String text, int key, ActionListener listener){

        JMenuItem menuItem = new JMenuItem(text, key);
        menuItem.addActionListener(listener);
        return menuItem;
    }

    private JMenu generateLookAndFeelMenu() {
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_Q);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        lookAndFeelMenu.add(generateMenuItem("Системная схема", KeyEvent.VK_W, (event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        }));
        lookAndFeelMenu.add(generateMenuItem("Универсальная схема", KeyEvent.VK_E, (event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        }));


        return lookAndFeelMenu;
    }

    private JMenu generateTestBar() {
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_R);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        testMenu.add(generateMenuItem("Сообщение в лог", KeyEvent.VK_T, (event) -> {
            Logger.debug("Новая строка");
        }));

        return testMenu;
    }

    private JMenu generateActionsBar() {
        JMenu actionsMenu = new JMenu("Действия");
        actionsMenu.setMnemonic(KeyEvent.VK_Y);
        actionsMenu.getAccessibleContext().setAccessibleDescription("Действия над приложением");

        actionsMenu.add(generateMenuItem("Закрыть", KeyEvent.VK_U, (event) -> {
            if (confirmExit(actionsMenu) == JOptionPane.YES_OPTION) {
                saveWindows();
                System.exit(0);
            }
        }));
        return actionsMenu;
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(generateLookAndFeelMenu());
        menuBar.add(generateTestBar());
        menuBar.add(generateActionsBar());

        return menuBar;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }

    private void saveWindows(){
        for (JInternalFrame window: desktopPane.getAllFrames()) {
            ((IObjectState) window).save();
        }
    }

}

