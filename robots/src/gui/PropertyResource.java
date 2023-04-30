package gui;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class PropertyResource{
    private static String language = "ru_RU";
    private static Properties properties = null;

    public static void setLanguage(String language){
        PropertyResource.language = language;
    }

    public static String getLanguage(){
        return language;
    }

    public static void setProperties(String propertyFile) {
        properties = new Properties();
        try {
            properties.load(new FileReader(propertyFile));
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
    protected Object getProperty(String key) {
        if (properties == null){
            setProperties("properties/text_" + language + ".properties");
        }
        return properties.getProperty(key);
    }

}