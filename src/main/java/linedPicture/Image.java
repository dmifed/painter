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
public class Image {
    //private List<Pixel> unUsedPixels;
    private int firstRowUnUsedPixelIndex;
    private int firstColumnUnUsedPixelIndex;
    private Set<Pixel> recoloredPixels;
    private int[][] colorsId;
    private static Pixel[][] imagePixels;
    private static int rightBorder;
    private static int downBorder;
    private static int MIN_LIMIT_OF_PIXELS_IN_AREA = 400;
    private List<Color> basicColors;

    public Image(int[][] colorsId) {
        this.colorsId = colorsId;
        imageInit(colorsId);
    }

    private void imageInit(int[][] colorsId){
        rightBorder = colorsId[0].length;
        downBorder = colorsId.length;
        recoloredPixels = new HashSet<>();
        imagePixels = new Pixel[colorsId.length][colorsId[0].length];
        for(int row = 0; row < colorsId.length; row++){
            for(int col = 0; col < colorsId[0].length; col++){
                Pixel pixel = new Pixel(row, col, colorsId[row][col]);
                imagePixels[row][col] = pixel;
            }
        }
        basicColors = BasicColors.getBasicColors();
        firstRowUnUsedPixelIndex = 0;
        firstColumnUnUsedPixelIndex = 0;
    }

    private PixelArea getPixelAreaWithSameColorId(int firstRowUnUsedPixelIndex, int firstColumnUnUsedPixelIndex){
        Set<Pixel> pixelSet = new HashSet<>();
        List<Pixel> areaPixelsList = new ArrayList<>();
        Set<Integer> neighboursColors = new HashSet<>();
        Pixel startPixel = imagePixels[firstRowUnUsedPixelIndex][firstColumnUnUsedPixelIndex];
        int colorId = startPixel.getColorId();
        int minRow = startPixel.getRow();
        int maxRow = startPixel.getRow();
        int minCol = startPixel.getCol();
        int maxCol = startPixel.getCol();
        areaPixelsList.add(startPixel);
        pixelSet.add(startPixel);
        int size = areaPixelsList.size();
        for (int i = 0; i < size; i++){
            Pixel prev = areaPixelsList.get(i);
            if(prev.getRow() > maxRow) maxRow = prev.getRow();
            if(prev.getRow() < minRow) minRow = prev.getRow();
            if(prev.getCol() > maxCol) maxCol = prev.getCol();
            if(prev.getCol() < minCol) minCol = prev.getCol();
            size = addNeighboursPixels(prev, areaPixelsList, pixelSet, neighboursColors);
        }
        if(areaPixelsList.size() >= MIN_LIMIT_OF_PIXELS_IN_AREA){
            setPixelsToUsed(areaPixelsList);
            return new PixelArea(colorId, areaPixelsList, minRow, maxRow, minCol, maxCol, neighboursColors);
        }else {
            repaintPixelsToNearestColor(areaPixelsList, neighboursColors, colorId);
            return null;
        }
    }

    private void repaintPixelsToNearestColor(List<Pixel> areaPixelsList, Set<Integer> neighboursColors, int colorId) {
        Color currentColor = basicColors.get(colorId);
        int nearestColorId = 0;
        double currentColorDistance = Double.MAX_VALUE;
        for(int id : neighboursColors){
            if(id > 0){
                Color candidate = basicColors.get(id);
                double newColorDistance = Utils.calcColorDistance(candidate, currentColor);
                if(newColorDistance < currentColorDistance){
                    nearestColorId = id;
                    currentColorDistance = newColorDistance;
                }
            }


        }
        System.out.println("neighboursColorsId " + nearestColorId);
        System.out.println("small area. repaint " + areaPixelsList.size() + "px from id " + colorId + " to id " + nearestColorId);
        for(Pixel p : areaPixelsList){
            p.setColorId(nearestColorId);
        }
    }

    private int addNeighboursPixels(Pixel prev, List<Pixel> areaPixels, Set<Pixel> pixelSet, Set<Integer> neighboursColors){
        int size = areaPixels.size();
        Pixel neighbour;

        neighbour = getRightPixelWithSameColorId(prev);
        size += addPixel(prev, neighbour, areaPixels, pixelSet, neighboursColors);

        neighbour = getLeftPixelWithSameColorId(prev);
        size += addPixel(prev, neighbour, areaPixels, pixelSet, neighboursColors);

        neighbour = getUpPixelWithSameColorId(prev);
        size += addPixel(prev, neighbour, areaPixels, pixelSet,neighboursColors);

        neighbour = getDownPixelWithSameColorId(prev);
        size += addPixel(prev, neighbour, areaPixels, pixelSet, neighboursColors);

        return size;
    }

    private int addPixel(Pixel prev, Pixel nextPixel, List<Pixel> areaPixels, Set<Pixel> pixelSet, Set<Integer> neighboursColors){
        //System.out.println("nextPixel : " + nextPixel);
        if(nextPixel != null){
            if (nextPixel.getColorId() == prev.getColorId()) {
                if(!pixelSet.contains(nextPixel)){
                    areaPixels.add(nextPixel);
                    pixelSet.add(nextPixel);
                    return 1;
                }

            }else if(!nextPixel.isUsed()){
                neighboursColors.add(nextPixel.getColorId());
                return 0;
            }
        }
        return 0;
    }

    private Pixel getRightPixelWithSameColorId(Pixel prev){
        int currentCol = prev.getCol();
        if(currentCol < rightBorder - 1){
            return imagePixels[prev.getRow()][prev.getCol()+1];
        }
        return null;
    }

    private Pixel getLeftPixelWithSameColorId(Pixel prev){
        int currentCol = prev.getCol();
        if(currentCol > 1){
            return imagePixels[prev.getRow()][prev.getCol()-1];
        }
        return null;
    }

    private Pixel getDownPixelWithSameColorId(Pixel prev){
        int currentRow = prev.getRow();
        if(currentRow < downBorder - 1){
            return imagePixels[prev.getRow()+1][prev.getCol()];
        }
        return null;
    }

    private Pixel getUpPixelWithSameColorId(Pixel prev){
        int currentRow = prev.getRow();
        if(currentRow > 1) {
            return imagePixels[prev.getRow() - 1][prev.getCol()];
        }
        return null;
    }

    private void setPixelsToUsed(List<Pixel> usedPixels){
        for(Pixel p : usedPixels){
            p.setUsed(true);
        }
    }

    private void setPixelsToUnused(List<Pixel> pixels){
        for(Pixel p : pixels){
            p.setUsed(false);
        }
    }

    static Pixel getPixel (int row, int col) {
        if(row > 0 && col > 0 && row < downBorder && col < rightBorder){
            return imagePixels[row][col];
        }
        return null;
    }



    private void setTextIntoPixelArea(List<Pixel> bottomRightPixels, int colorID){
        String text = String.valueOf(colorID);
        bottomRightPixels = bottomRightPixels.subList(0,1);
        for(Pixel p : bottomRightPixels){
            int topPosition = p.getRow()-20;
            int leftPosition = p.getCol()-20;
            BufferedImage textRectangle = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = textRectangle.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, 20, 20);
            graphics.setColor(Color.BLACK);
            graphics.setFont(new Font("Arial", Font.PLAIN, 7));
            graphics.drawString(text, 0,0);
            for (int x = 0; x < textRectangle.getWidth(); x++){
                for(int y = 0; y < textRectangle.getHeight(); y++){
                    Pixel pixel =  imagePixels[topPosition + y][leftPosition + x];
                    pixel.setColor(new Color(textRectangle.getRGB(x,y)));
                    recoloredPixels.add(pixel);
                }
            }
        }
    }

    public void createLinedImage(){
        while (true){
            int[] indexes = findFirstUnUsedPixelIndex();
            if(indexes != null){
                firstRowUnUsedPixelIndex = indexes[0];
                firstColumnUnUsedPixelIndex = indexes[1];
                PixelArea pixelArea = getPixelAreaWithSameColorId(firstRowUnUsedPixelIndex, firstColumnUnUsedPixelIndex);
                System.out.println("on work with area " + pixelArea);
                if(pixelArea != null){
                    List<Pixel> bottomRightPixelsForText = pixelArea.getListOfBottomRightPixelsForText();
                    if(bottomRightPixelsForText.size() > 0){
                        System.out.println("set text " + pixelArea.getColorId());
                        setTextIntoPixelArea(bottomRightPixelsForText, pixelArea.getColorId());
                    }else {
                        setPixelsToUnused(pixelArea.getPixels());
                        repaintPixelsToNearestColor(pixelArea.getPixels(), pixelArea.getNeighboursColors(), pixelArea.getColorId());
                    }
                }
            }else {
                break;
            }
        }
    }

    public int[][] getColorsId() {
        return colorsId;
    }

    public static Pixel[][] getImagePixels() {
        return imagePixels;
    }

    private int[] findFirstUnUsedPixelIndex(){
        int[] firstIndexes = new int[2];
        for(int indexRow = firstRowUnUsedPixelIndex; indexRow < imagePixels.length; indexRow++) {
            for (int indexCol = 0; indexCol < imagePixels[0].length; indexCol++){
                if (!imagePixels[indexRow][indexCol].isUsed()) {
                    firstRowUnUsedPixelIndex = indexRow;
                    firstColumnUnUsedPixelIndex = indexCol;
                    firstIndexes[0] = firstRowUnUsedPixelIndex;
                    firstIndexes[1] = firstColumnUnUsedPixelIndex;
                    return firstIndexes;
                }
            }
        }
        return null;
    }

}
