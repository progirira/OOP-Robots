package gui;

import javax.swing.*;
import java.io.*;

public abstract class WindowWithPathState extends JInternalFrame implements IObjectState{
    final private File fileNameToSave;
    public WindowWithPathState(String title, File fileName, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
        fileNameToSave = fileName;
    }

    private Boolean ifFileExists(){
        return fileNameToSave != null;
    }

    public void save(){
        if (!ifFileExists()) {
            return;
        }
        try (OutputStream os = new FileOutputStream(fileNameToSave);
             ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(os))) {
            oos.writeObject(getName());
            oos.writeObject(getLocation().x);
            oos.writeObject(getLocation().y);
            oos.writeObject(getHeight());
            oos.writeObject(getWidth());
            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void load(){
        if (!ifFileExists()) {
            return;
        }
        try {
            InputStream is = new FileInputStream(fileNameToSave);
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is));
            try {
                setName((String) ois.readObject());
                setLocation((Integer) ois.readObject(), (Integer) ois.readObject());
                setSize((Integer) ois.readObject(), (Integer) ois.readObject());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
