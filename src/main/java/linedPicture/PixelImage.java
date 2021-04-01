package linedPicture;

import tests.BasicColors;
import tests.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * @author dmifed
 */
public class PixelImage {
    private Pixel[][] imagePixels;
    private int rightBorder;
    private int downBorder;

    public PixelImage(ColorWithId[][] colorWithIds){
        rightBorder = colorWithIds[0].length;
        downBorder = colorWithIds.length;
        imagePixels = new Pixel[colorWithIds.length][colorWithIds[0].length];
        for(int row = 0; row < colorWithIds.length; row++){
            for(int col = 0; col < colorWithIds[0].length; col++){
                Pixel pixel = new Pixel(row, col, colorWithIds[row][col]);
                imagePixels[row][col] = pixel;
            }
        }
    }

    Pixel getPixel (int row, int col) {
        if(row > 0 && col > 0 && row < downBorder && col < rightBorder){
            return imagePixels[row][col];
        }
        return null;
    }

    public Pixel[][] getImagePixels() {
        return imagePixels;
    }

    public int getRightBorder() {
        return rightBorder;
    }

    public int getDownBorder() {
        return downBorder;
    }
}
