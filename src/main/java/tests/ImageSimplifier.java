package tests;

import ij.IJ;
import ij.ImagePlus;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.*;
import java.util.List;

/**
 * @author dmifed
 */
public class ImageSimplifier {
    BufferedImage bufferedImage;
    public static void main(String[] args) throws IOException{
        String pathToOrigFile = "src/main/resources/image1/93148480844683950860.jpg";
        String pathSimplifiedOrig = pathToOrigFile.substring(0, pathToOrigFile.length()-4) + "-simple" + ".jpg";

        //ImageSimplifier isOrig = new ImageSimplifier(pathToOrigFile);

        ImageSimplifier isBlur = new ImageSimplifier(pathToOrigFile, 5);

        //isOrig.createSimplifiedImage(isOrig.bufferedImage, pathSimplifiedOrig);

        pathSimplifiedOrig = pathToOrigFile.substring(0, pathToOrigFile.length()-4) + "-Blur_x5-simple" + ".jpg";
        long timeStart = System.currentTimeMillis();
        isBlur.createSimplifiedImage(isBlur.bufferedImage, pathSimplifiedOrig);
        long timeFinish = System.currentTimeMillis();
        long elapsedSeconds = (timeFinish - timeStart) / 1000;
        System.out.println("elapsedTime: " + elapsedSeconds + "s");
    }

    public ImageSimplifier(String pathToOrigFile) {
        this.bufferedImage = getImage(pathToOrigFile);
    }

    public ImageSimplifier(String pathToOrigFile, double blurValue) {
        ImagePlus imp = IJ.openImage(pathToOrigFile);
        imp.getProcessor().blurGaussian(blurValue);
        this.bufferedImage = imp.getBufferedImage();
    }

    public void createSimplifiedImage(BufferedImage image, String outPath){
        HashMap<Color, Integer> usedColors = new HashMap<>();
        List<Color> basicColors = BasicColors.getBasicColors();
        Utils.printColorTable();
        for(int x = 0; x < image.getWidth(); x++){
            for(int y = 0; y < image.getHeight(); y++){
                Color color = new Color(image.getRGB(x, y));
                Color replaceColor = getNearestColor(color, basicColors);
                image.setRGB(x, y, replaceColor.getRGB());
                addColorToMap(replaceColor, usedColors);
            }
        }
        File simplifiedOrig = new File(outPath);
        try {
            ImageIO.write(image, "jpg", simplifiedOrig);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        printColorMap(sort(usedColors));
    }

    public BufferedImage getImage(String path){
        File in = new File(path);
        BufferedImage image = null;
        try {
            image = ImageIO.read(in); //IOException
        }catch (IOException e){
            System.out.println("IOException in getOriginalImage");
            e.printStackTrace();
        }
        return image;
    }

    public BufferedImage createGaussianBlurImage(String pathOrig, double sigma){
        ImagePlus imp = IJ.openImage(pathOrig);
        imp.getProcessor().blurGaussian(sigma);
        return imp.getBufferedImage();

    }

    public void printColorMap(Map<Color, Integer> map){
        int maybeReplaced = 0;
        int total = 0;
        for(Map.Entry<Color, Integer> entry : map.entrySet()){
            total++;
            Color color = entry.getKey();
/*            System.out.println(color.getRed() + " " +
                                color.getGreen() + " " +
                                color.getBlue() + " : " +
                                entry.getValue());*/
            if(entry.getValue() < 400) maybeReplaced++;
        }
        System.out.println("total colors: " + total + ", has less than 400px: " + maybeReplaced);
    }

    public double calcColorDistance(Color c1, Color c2){
        int red1 = c1.getRed();
        int green1 = c1.getGreen();
        int blue1 = c1.getBlue();
        int red2 = c2.getRed();
        int green2 = c2.getGreen();
        int blue2 = c2.getBlue();

        int deltaRed = red1 - red2;
        int deltaGreen = green1 - green2;
        int deltaBlue = blue1 - blue2;
        double middleRedDiff = ((double)(red1 + red2)) / 2;


        double distance = Math.sqrt(
                (2 + middleRedDiff/256) * deltaRed * deltaRed
                + 4 * deltaGreen * deltaGreen
                + (2 + (255 - middleRedDiff)/256)*deltaBlue*deltaBlue
        );

        /*double distance = Math.sqrt((red1 - red2)*(red1-red2) +
                                    (green1-green2)*(green1-green2) +
                                    (blue1-blue2)*(blue1-blue2));*/

        return distance;
    }

    public Color getNearestColor(Color orig, List<Color> basicColors){
        double minDistance = Integer.MAX_VALUE;
        Color nearestColor = Color.BLACK;
        for(Color basicColor : basicColors){
            double distance = calcColorDistance(orig, basicColor);
            if(distance < 10) return basicColor;
            if(distance < minDistance){
                minDistance = distance;
                nearestColor = basicColor;
            }
        }
        return nearestColor;
    }

    public void addColorToMap(Color c, HashMap<Color, Integer> usedColors){
        if(usedColors.containsKey(c)){
            usedColors.put(c, usedColors.get(c) + 1);
        }else {
            usedColors.put(c, 1);
        }
    }

    public <K,V extends Comparable<V>> Map<K, V> sort(Map<K, V> map){
        TreeMap<K, V> treeMap = new TreeMap<>((o1, o2) -> {
            V v1 = map.get(o1);
            V v2 = map.get(o2);
            return v1.compareTo(v2);
        });
        treeMap.putAll(map);
        return treeMap;
    }



}
