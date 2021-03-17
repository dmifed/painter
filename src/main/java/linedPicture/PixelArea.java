package linedPicture;

import linedPicture.exception.PixelOutOfImageException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author dmifed
 */
public class PixelArea {
    private static int areaId = 0;
    private int colorId;
    private List<Pixel> pixels;
    private int minRow;
    private int maxRow;
    private int minCol;
    private int maxCol;
    private final int pixelsBetweenText = 400;
    private int textFreq;
    private Set<Integer> neighboursColors;


    private List<Pixel> bottomRightPixelsForText;
    private List<Pixel> centerPixels;
    private List<Pixel> textCenter;
    private Pixel firstPixel;
    private int nearestIdColor;
    private double deltaColor;


    public PixelArea(int colorId, List<Pixel> pixels, int minRow, int maxRow, int minCol, int maxCol, Set<Integer> neighboursColors) {
        areaId++;
        this.colorId = colorId;
        this.pixels = pixels;
        this.minRow = minRow;
        this.maxRow = maxRow;
        this.minCol = minCol;
        this.maxCol = maxCol;
        int maxWidth = maxCol - minCol;
        int maxHeight = maxRow - minRow;
        textFreq = 1 + (Math.max(maxHeight, maxWidth) / pixelsBetweenText);
        bottomRightPixelsForText = new ArrayList<>();
        this.neighboursColors = neighboursColors;
    }

    @Override
    public String toString() {
        return "area :" + areaId + ", color: " + colorId + ", size:" + (maxCol - minCol + 1) + "x" + (maxRow - minRow + 1) + "px (" + pixels.size() + ")";
    }

    private void countingPreviousPixelsWithSameColorId(){
        for(int row = minRow; row <= maxRow; row++){
            for (int col = minCol; col <= maxCol; col++){
                Pixel currentPixel = Image.getPixel(row, col);

                if(currentPixel != null){
                    Pixel topPixel = Image.getPixel(row-1, col);
                    if(topPixel != null){
                        if(currentPixel.getColorId() == colorId && topPixel.getColorId() == colorId){
                            currentPixel.setCountPixelAbove(1 + topPixel.getCountPixelAbove());
                        }
                    }

                    Pixel leftPixel = Image.getPixel(row, col-1);
                    if(leftPixel != null){
                        if(currentPixel.getColorId() == colorId && leftPixel.getColorId() == colorId){
                            currentPixel.setCountPixelLeft(1 + leftPixel.getCountPixelLeft());
                        }
                    }

                    Pixel leftAbovePixel = Image.getPixel(row-1, col-1);
                    if(leftAbovePixel != null){
                        if(currentPixel.getColorId() == colorId && leftAbovePixel.getColorId() == colorId){
                            currentPixel.setCountPixelLeftAbove(1 + leftAbovePixel.getCountPixelLeftAbove());
                        }
                    }
                }
            }
        }
    }

    List<Pixel> getListOfBottomRightPixelsForText(){
        List<Pixel> bottomRightPixelsForText = new ArrayList<>();
        countingPreviousPixelsWithSameColorId();
        for(Pixel p : pixels){
            if(p.isRightBottomPixelForText()){
                bottomRightPixelsForText.add(p);
            }
        }
        return bottomRightPixelsForText;
    }

    public List<Pixel> getPixels() {
        return pixels;
    }

    public int getColorId() {
        return colorId;
    }

    public Set<Integer> getNeighboursColors() {
        return neighboursColors;
    }

}
