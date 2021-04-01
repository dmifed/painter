package linedPicture;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author dmifed
 */
public class ImageLiner {
    private Pixel firstUnused;
    private PixelImage image;
    private List<ColorWithId> basicColors;
    private List<Pixel> commonTextList;

    public ImageLiner(ColorWithId[][] colorWithIds) {
        image = new PixelImage(colorWithIds);
        basicColors = BasicColors.getBasicColors();
        commonTextList = new ArrayList<>();
        firstUnused = image.getPixel(0, 0);
    }

    //Find 1st unUsed pixel from Pixel[][]
    private int findFirstUnusedPixelRowIndex(){
        for(int indexRow = firstUnused.getRow(); indexRow < image.getDownBorder(); indexRow++) {
            for (int indexCol = 0; indexCol < image.getRightBorder(); indexCol++){
                Pixel pixel = image.getPixel(indexRow, indexCol);
                if (!pixel.isUsed()) {
                    return pixel.getRow();
                }
            }
        }
        return -1;
    }

    //Find all linked pixels with same color
    private PixelArea createPixelArea(Pixel startPixel){
        List<Pixel> areaList = new ArrayList<>();
        Set<ColorWithId> neighbourColors = new HashSet<>();
        areaList.add(startPixel);
        startPixel.setSelected(true);
        int size = areaList.size();
        for (int i = 0; i < size; i++){
            Pixel currentPixel = areaList.get(i);
            size = addNeighboursPixels(currentPixel, areaList, neighbourColors);
        }
        return new PixelArea(areaList, neighbourColors, image);
    }

    private int addNeighboursPixels(Pixel currentPixel, List<Pixel> areaList, Set<ColorWithId> neighbourColors){
        int size = areaList.size();
        Pixel neighbourPixel;

        neighbourPixel = getRightPixelWithSameColorId(currentPixel);
        if(addPixel(currentPixel, neighbourPixel, areaList, neighbourColors)) ++size;

        neighbourPixel = getLeftPixelWithSameColorId(currentPixel);
        if(addPixel(currentPixel, neighbourPixel, areaList, neighbourColors)) ++size;

        neighbourPixel = getUpPixelWithSameColorId(currentPixel);
        if(addPixel(currentPixel, neighbourPixel, areaList, neighbourColors)) ++size;

        neighbourPixel = getDownPixelWithSameColorId(currentPixel);
        if(addPixel(currentPixel, neighbourPixel, areaList, neighbourColors)) ++size;

        return size;
    }

    private boolean addPixel(Pixel currentPixel, Pixel nextPixel, List<Pixel> areaList, Set<ColorWithId> neighbourColors){
        if(nextPixel != null){
            if (nextPixel.getColorWithId().getId() == currentPixel.getColorWithId().getId()) {
                if(!nextPixel.isSelected()){
                    areaList.add(nextPixel);
                    nextPixel.setSelected(true);
                    return true;
                }

            }else{
                //current pixel is border
                currentPixel.setBorder(true);
                if(!nextPixel.isUsed()){
                    neighbourColors.add(nextPixel.getColorWithId());
                    return false;
                }
            }
        }
        return false;
    }

    private Pixel getRightPixelWithSameColorId(Pixel current){
        int currentCol = current.getCol();
        if(currentCol < image.getRightBorder()-1){
            return image.getPixel(current.getRow(), current.getCol() + 1);
        }
        return null;
    }
    private Pixel getLeftPixelWithSameColorId(Pixel current){
        int currentCol = current.getCol();
        if(currentCol > 1){
            return image.getPixel(current.getRow(), current.getCol() - 1);
        }
        return null;
    }
    private Pixel getDownPixelWithSameColorId(Pixel current){
        int currentRow = current.getRow();
        if(currentRow < image.getRightBorder() - 1){
            return image.getPixel(current.getRow() + 1,current.getCol());
        }
        return null;
    }
    private Pixel getUpPixelWithSameColorId(Pixel current){
        int currentRow = current.getRow();
        if(currentRow > 1) {
            return image.getPixel(current.getRow() - 1,current.getCol());
        }
        return null;
    }




    //for all pixels in commonTextList set text pixel color id
    private void setColorIdToLinedImage(BufferedImage linedImage, Color textColor){
        String text;
        Graphics graphics = linedImage.getGraphics();
        graphics.setColor(textColor);
        graphics.setFont(new Font("Arial", Font.PLAIN, 27));
        for(Pixel p : commonTextList){
            text = String.valueOf(p.getColorWithId().getId());
            int leftPosition = p.getCol() - 14;
            int downPosition = p.getRow() + 14;
            graphics.drawString(text, leftPosition, downPosition);  //left down conner of start text
            graphics.dispose();
        }
    }

    private BufferedImage createLinedImage(){
        BufferedImage linedImage = new BufferedImage(image.getRightBorder(), image.getDownBorder(), BufferedImage.TYPE_INT_RGB);
        for(int row = 0; row < linedImage.getHeight(); row++){
            for(int col = 0; col < linedImage.getWidth(); col++){
                int colorRGB = image.getPixel(row, col).getRGBValue();
                linedImage.setRGB(col, row, colorRGB);
            }
        }
        return linedImage;
    }

    //Mark pixels from list as used
    private void setPixelsAsUsed(List<Pixel> areaList){
        for(Pixel p : areaList){
            p.setUsed(true);
        }
    }

    //Mark pixels from list as unselected
    private void setPixelsAsUnselected(List<Pixel> areaList){
        for(Pixel p : areaList){
            p.setSelected(false);
        }
    }

    private Pixel getPixel (int row, int col) {
        if(row >= 0 && col >= 0 && row < image.getDownBorder() && col < image.getRightBorder()){
            return image.getPixel(row, col);
        }
        return null;
    }



    //Set pixel color
    private void changePixelColorToGray(Set<Pixel> pixels, double tone){
        int channelsValue = (int) (255*(1-tone));
        Color color = new Color(channelsValue, channelsValue, channelsValue);

        ColorWithId colorWithId = new ColorWithId(color.getRGB(), -1);
        for(Pixel p : pixels){
            p.setColorWithId(colorWithId);
        }
    }

    private void changePixelColorToWhite(Set<Pixel> pixels){
        changePixelColorToGray(pixels, 0);
    }
    private void changePixelColorToBlack(Set<Pixel> pixels){
        changePixelColorToGray(pixels, 1);
    }


}
