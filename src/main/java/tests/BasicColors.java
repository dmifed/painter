package tests;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * @author dmifed
 */
public class BasicColors {
    private static List<Color> basicColors = null;


    private BasicColors() {    }

    public static List<Color> getBasicColors(){
        synchronized (BasicColors.class){
            if(basicColors != null) return basicColors;
            createListOfBasicColors();
            return basicColors;
        }
    }

    private static void createListOfBasicColors(){
        String pathToProperties = "src/main/resources/properties/newColor.properties";
        Properties properties = null;
        try {
            FileInputStream fis = new FileInputStream(pathToProperties);
            properties = new Properties();
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Set<String> colorNames = properties.stringPropertyNames();
        TreeSet<String> treeSet = new TreeSet<>();
        treeSet.addAll(colorNames);

        basicColors = new ArrayList<>();
        for(String colorName : treeSet){
            //System.out.println(colorName);
            String[] chanels = properties.getProperty(colorName).split(" ");
            Color color = new Color(Integer.parseInt(chanels[0]), Integer.parseInt(chanels[1]), Integer.parseInt(chanels[2]));
            basicColors.add(color);
            //System.out.println(colorName + " R:" + color.getRed() + " G:" + color.getGreen() + " B:" + color.getBlue());
        }
    }





}
