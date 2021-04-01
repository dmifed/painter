package linedPicture;

import ij.IJ;
import ij.ImagePlus;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author dmifed
 */
public class ImageApp {
    //BufferedImage bufferedImage;
    public static void main(String[] args) throws IOException {
        String originalImagePath = "src/main/resources/image1/93148480844683950860_2400x1700.jpg";
        String simplifiedImagePath = originalImagePath.substring(0, originalImagePath.length()-4) + "-Blur_x5-simple" + ".jpg";
        String linedImagePath = originalImagePath.substring(0, originalImagePath.length()-4) + "-lined" + ".jpg";

        ImageApp imageApp = new ImageApp();

        BufferedImage blurImage = imageApp.createBlurImage(originalImagePath, 3.0);
        int[][] colorTable = imageApp.createColorTableAndSaveSimplifiedImage(blurImage, simplifiedImagePath);
        imageApp.createLinedImage(colorTable, linedImagePath);


    }

    public BufferedImage createBlurImage(String pathToOrigFile, double blurValue){
        ImagePlus imp = IJ.openImage(pathToOrigFile);
        imp.getProcessor().blurGaussian(blurValue);
        return imp.getBufferedImage();
    }

    public int[][] createColorTableAndSaveSimplifiedImage(BufferedImage image, String outSimplifiedImagePath){
        int[][] colorTable = new int[image.getHeight()][image.getWidth()];
        HashMap<Color, Integer> usedColors = new HashMap<>();
        List<Color> basicColors = BasicColors.getBasicColors();
        Utils.printColorTable();
        for(int x = 0; x < image.getWidth(); x++){
            for(int y = 0; y < image.getHeight(); y++){
                Color color = new Color(image.getRGB(x, y));
                ColorWithId replaceColor = getNearestColor(color, basicColors);
                image.setRGB(x, y, replaceColor.getRGB().getRGB());
                addColorToMap(replaceColor.getRGB(), usedColors);
                colorTable[y][x] = replaceColor.getId();
            }
        }
        saveImage(image, outSimplifiedImagePath);
        printColorMap(sort(usedColors));
        return colorTable;

    }

    public void createLinedImage(int[][] colorTable, String outLinedImagePath){
       Image image = new Image(colorTable);
       image.createLinedImage();
       Pixel[][] imagePixels = Image.getImagePixels();
       int width = colorTable[0].length;
       int height = colorTable.length;
       BufferedImage linedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
       for(int x = 0; x < width; x++ ){
           for(int y = 0; y < height; y++){
               int rgb = BasicColors.getBasicColors().get(imagePixels[y][x].getColorId()).getRGB();
               linedImage.setRGB(x, y, rgb);
           }
       }
       saveImage(linedImage, outLinedImagePath);
    }

    private void saveImage(BufferedImage image, String pathToSave){
        File fileImage = new File(pathToSave);
        try {
            ImageIO.write(image, "jpg", fileImage);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
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



    public ColorWithId getNearestColor(Color orig, List<Color> basicColors){
        double minDistance = Integer.MAX_VALUE;
        Color nearestColor = Color.BLACK;
        int nearestColorId = 0;
        for(int id = 1; id < basicColors.size(); id++){
            Color basicColor = basicColors.get(id);
            double distance = Utils.calcColorDistance(orig, basicColor);
            if(distance < 10){
                return new ColorWithId(basicColor, id);
            }
            if(distance < minDistance){
                minDistance = distance;
                nearestColor = basicColor;
                nearestColorId = id;
            }
        }
        return new ColorWithId(nearestColor, nearestColorId);
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

/*    public BufferedImage getImage(String path){
        File in = new File(path);
        BufferedImage image = null;
        try {
            image = ImageIO.read(in); //IOException
        }catch (IOException e){
            System.out.println("IOException in getOriginalImage");
            e.printStackTrace();
        }
        return image;
    }*/
}
