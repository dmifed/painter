package tests;

import ij.IJ;
import ij.ImagePlus;
import linedPicture.Image;
import linedPicture.ImageApp;
import linedPicture.Pixel;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author dmifed
 */
public class TestAddTextOnImage {
    public static void main(String[] args) {
        TestAddTextOnImage testAddTextOnImage = new TestAddTextOnImage();
        testAddTextOnImage.setTextIntoPixelArea(33);

    }
    private void setTextIntoPixelArea(int colorID){
        String text = String.valueOf(colorID);
        String originalImagePath = "src/main/resources/image1/testRed_50x50.jpg";
        ImagePlus imp = IJ.openImage(originalImagePath);
        BufferedImage image = imp.getBufferedImage();

        Iterator<ImageWriter> iterator = ImageIO.getImageWritersByFormatName("jpeg");
        ImageWriter imageWriter = iterator.next();
        ImageWriteParam param = imageWriter.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(0.95f);
        linedPicture.Pixel bottomPixel = new Pixel(49,49, 0);
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Arial", Font.PLAIN, 27));
       // graphics.setFont(graphics.getFont().deriveFont(8f));
        graphics.drawString(text, bottomPixel.getCol()-30,bottomPixel.getRow());  //left down conner of start text
        graphics.dispose();

        String pathToSave = "src/main/resources/image1/testRed_50x50_Text.jpg";
        File fileImage = new File(pathToSave);
        try {
            imageWriter.setOutput(ImageIO.createImageOutputStream(fileImage));
            imageWriter.write(null, new IIOImage(image, null, null), param);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

/*        List<linedPicture.Pixel> bottomRightPixels = new ArrayList<>();
        linedPicture.Pixel bottomPixel = new Pixel(49,49, 0);
        bottomRightPixels.add(bottomPixel);


        for(linedPicture.Pixel p : bottomRightPixels){
            int topPosition = p.getRow()-20;
            int leftPosition = p.getCol()-20;
            System.out.println("topPos=" + topPosition + " leftPos=" + leftPosition);
            //BufferedImage textRectangle = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = image.getGraphics();
            //graphics.setColor(Color.WHITE);
            //graphics.fillRect(0, 0, 20, 20);
            //graphics.setColor(Color.BLACK);
            //graphics.getFont().deriveFont(7f);
            //graphics.setFont(new Font("Arial", Font.PLAIN, 7));
            graphics.setFont(graphics.getFont().deriveFont(7f));
            graphics.drawString(text, 0,0);
            graphics.dispose();
            //System.out.println("rect=" + textRectangle.getHeight() + "x" + textRectangle.getWidth());
*//*            for (int x = 0; x < textRectangle.getWidth(); x++){
                for(int y = 0; y < textRectangle.getHeight(); y++){
                    Pixel pixel =  imagePixels[topPosition + y][leftPosition + x];
                    List<Color> colors = BasicColors.getBasicColors();
                    int rgb = colors.get(pixel.getColorId()).getRGB();
                    System.out.println(colors.get(pixel.getColorId()).getRed() + " "+
                            colors.get(pixel.getColorId()).getGreen() + " "+
                            colors.get(pixel.getColorId()).getBlue());
                    image.setRGB(x, y, rgb);
                }
            }*//*
        }*/

    }
}
