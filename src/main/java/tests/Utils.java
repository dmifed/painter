package tests;

import linedPicture.Pixel;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author dmifed
 */
public class Utils {
    private static final int cellSize = 50; //size in pixels
    public static void printColorTable(){
        String pathToColorTable = "src/main/resources/colorTable.jpg";
        File out = new File(pathToColorTable);
        List<Color> basicColors = BasicColors.getBasicColors();
        int countCell = basicColors.size();
        int columns = 10;
        int rows = countCell/10;
        if(countCell%10 != 0){
            ++rows;
        }


        System.out.printf("All: %d, table %d x %d", countCell, rows, columns);
        System.out.println();



        BufferedImage image = new BufferedImage(columns*cellSize, rows*cellSize, BufferedImage.TYPE_INT_RGB);
        int indexOfColor = 0;
        for(int row = 0; row < rows; row++){
            for(int column = 0; column < columns; column++){
                Color c = null;
                if(indexOfColor >= countCell){
                    c = Color.WHITE;
                }else {
                    c = basicColors.get(indexOfColor);
                }

                for(int y = row*cellSize; y < row*cellSize + cellSize; y++){
                    for(int x = column*cellSize; x < column*cellSize + cellSize; x++){
                        image.setRGB(x, y, c.getRGB());
                    }
                }
                indexOfColor++;
            }
        }

        try {
            JPEGImageWriteParam jpegImageWriteParam = new JPEGImageWriteParam(null);
            jpegImageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            jpegImageWriteParam.setCompressionQuality(0.95f);
            ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("jpg").next();
            imageWriter.setOutput(new FileImageOutputStream(new File(pathToColorTable)));
            imageWriter.write(null, new IIOImage(image, null, null), jpegImageWriteParam);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double calcColorDistance(Color c1, Color c2){
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

    public static void printImagePixels(Pixel[][] pixels, int startRow, int endRow, int startColumn, int endColumn){
        StringBuilder sb = new StringBuilder();
        for(int row = startRow; row < endRow; row++){
            for(int col = startColumn; col < endColumn; col++){
                sb.append(pixels[row][col]).append(" | ");
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    public static int getRGB(List<Color> basicColor, int colorId){
        return basicColor.get(colorId).getRGB();

    }
}
