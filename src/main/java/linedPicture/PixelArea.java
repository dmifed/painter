package linedPicture;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author dmifed
 */
public class PixelArea {
    private static int areaId = 0;
    private ColorWithId colorWithId;
    private Set<ColorWithId> neighbourColors;
    private Set<Pixel> innerPixels;
    private Set<Pixel> borderPixels;
    private Set<Pixel> pixelsForText;
    private int pixelsBetweenText;
    private BoundCorners boundCorners;
    private PixelImage image;


    public PixelArea(List<Pixel> pixelList, Set<ColorWithId> neighbourColors, PixelImage image) {
        areaId++;
        this.colorWithId = pixelList.get(0).getColorWithId();
        this.neighbourColors = neighbourColors;
        this.image = image;
        innerPixels = new HashSet<>();
        borderPixels = new HashSet<>();
        pixelsForText = new HashSet<>();
        for(Pixel p : pixelList){
            if(p.isBorder()){
                borderPixels.add(p);
            }else {
                innerPixels.add(p);
            }
        }
        boundCornersInit();
        pixelsBetweenText = -1;
        if(innerPixels.size() > 10_000) pixelsBetweenText = 200;
        if(innerPixels.size() > 100_000) pixelsBetweenText = 400;
    }

    private void boundCornersInit(){
        int topCorner = Integer.MAX_VALUE;
        int leftCorner = Integer.MAX_VALUE;
        int bottomCorner = 0;
        int rightCorner = 0;
        for(Pixel p : borderPixels){
            int row = p.getRow();
            int col = p.getCol();
            if(row < topCorner) topCorner = row;
            if(row > bottomCorner) bottomCorner = row;
            if(col < leftCorner) leftCorner = col;
            if(col > rightCorner) rightCorner = col;
        }
        boundCorners = new BoundCorners(topCorner, bottomCorner, leftCorner, rightCorner);

    }

    //find nearest color from colorSet
    private ColorWithId findNearestNeighbourColor(){
        ColorWithId nearestColor = null;
        double minDistance = Double.MAX_VALUE;
        double currentDist;
        for(ColorWithId c : neighbourColors){
            if(minDistance < 15) return nearestColor;
            currentDist = Utils.calcColorDistance(c.getRGB(), colorWithId.getRGB());
            if(currentDist < minDistance){
                minDistance = currentDist;
                nearestColor = c;
            }
        }
        return nearestColor;
    }

    //recolor pixels in innerSet and borderSet to nearest color.
    private void recolorPixels(ColorWithId nearestColor){
        for(Pixel p : innerPixels){
            p.setColorWithId(nearestColor);
        }
        for(Pixel p : borderPixels){
            p.setColorWithId(nearestColor);
            p.setBorder(false);
        }
    }

    //For all pixels in innerSet find distance between it and all pixels in borderSet
    private Set<Pixel> findPixelCanBeUsedForText(Set<Pixel> innerSet, Set<Pixel> borderSet){
        Set<Pixel> textPixels = new HashSet<>();
        int innerRow;
        int innerCol;
        for(Pixel inner : innerSet){
            innerRow = inner.getRow();
            innerCol = inner.getCol();
            int minDistanceRow = Integer.MAX_VALUE;
            int minDistanceCol = Integer.MAX_VALUE;
            for(Pixel border : borderSet){
                int distRow = Math.abs(border.getRow()-innerRow);
                if(distRow < 14) break;
                int distCol = Math.abs(border.getCol()-innerCol);
                if(distCol < 14) break;
                if(distRow < minDistanceRow) minDistanceRow = distRow;
                if(distCol < minDistanceCol) minDistanceCol = distCol;

            }
            inner.setMinDistanceRowToBorderPixel(minDistanceRow);
            inner.setMinDistanceColToBorderPixel(minDistanceCol);
            textPixels.add(inner);
        }
        return textPixels;
    }

    //find pixel from textSet with max distance
    private Pixel addCentralPixel(Set<Pixel> textSet){
        Pixel centralPixel = null;
        int maxDistance = 0;
        for(Pixel p : textSet){
            int distance = p.getRow()*p.getRow() + p.getCol()*p.getCol();
            if(distance > maxDistance){
                maxDistance = distance;
                centralPixel = p;
            }
        }
        return centralPixel;
    }

    //find other pixels with distance each over
    private Set<Pixel> selectPixelForText(Set<Pixel> textPixel, Pixel centralPixel){
        Set<Pixel> selectedTextPixel = new HashSet<>();
        selectedTextPixel.add(centralPixel);
        if(pixelsBetweenText < 0) return selectedTextPixel;
        List<Pixel> pixelList = new ArrayList<>();
        pixelList.add(centralPixel);
        int size = pixelList.size();
        for(int i = 0; i < size; i++){
            int startRow = pixelList.get(i).getRow();
            int startCol = pixelList.get(i).getCol();
            for(int degree = 0; degree < 360; degree += 20){
                int deltaRow = (int)(pixelsBetweenText * Math.cos(degree*0.0175)); // degree to radians
                int deltaCol = (int)(pixelsBetweenText * Math.sin(degree*0.0175));
                int pixelRow = startRow + deltaRow;
                int pixelCol = startCol + deltaCol;
                Pixel nextTextPixel = getPixelFromSet(pixelRow, pixelCol, textPixel);
                if(nextTextPixel != null && !selectedTextPixel.contains(nextTextPixel)){
                    selectedTextPixel.add(nextTextPixel);
                    pixelList.add(nextTextPixel);
                    ++size;
                }
            }
        }
        return selectedTextPixel;
    }

    private Pixel getPixelFromSet(int row, int col, Set<Pixel> pixelSet) {
        if(row >= boundCorners.getTopRow() && col >= boundCorners.getLeftColumn() && row <= boundCorners.getBottomRow() && col <= boundCorners.getRightColumn()){
            Pixel pixel = image.getPixel(row, col);
            if(pixelSet.contains(pixel)) return pixel;
        }
        return null;
    }

    @Override
    public String toString() {
        return "area :" + areaId + ", " +
                "colorId: " + colorWithId.getId() + ", " +
                "size:" + (boundCorners.getRightColumn() - boundCorners.getLeftColumn() + 1) +
                "x" + (boundCorners.getBottomRow() - boundCorners.getTopRow() + 1) + "px" +
                " (" + innerPixels.size()+ borderPixels.size() + ")";
    }




}
