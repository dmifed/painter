package linedPicture;

/**
 * @author dmifed
 */
public class Pixel {
    private final int row;
    private final int col;
    private ColorWithId colorWithId;
    private boolean isUsed;
    private boolean isSelected;
    private boolean isBorder;
    private int minDistanceRowToBorderPixel;
    private int minDistanceColToBorderPixel;

    public Pixel(int row, int col, ColorWithId color) {
        this.row = row;
        this.col = col;
        this.colorWithId = color;
    }

    @Override
    public String toString() {
        return "y=" + row + " x=" + col + " id=" + colorWithId.getId();
    }

    @Override
    public int hashCode() {
        return row*10_000 + col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pixel)) return false;
        Pixel pixel = (Pixel) o;
        if(hashCode() != pixel.hashCode()) return false;
        return row == pixel.row &&
                col == pixel.col;
    }

    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }

    public ColorWithId getColorWithId() {
        return colorWithId;
    }
    public void setColorWithId(ColorWithId colorWithId) {
        this.colorWithId = colorWithId;
    }
    public boolean isUsed() {
        return isUsed;
    }
    public void setUsed(boolean used) {
        isUsed = used;
    }
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    public boolean isBorder() {
        return isBorder;
    }
    public void setBorder(boolean border) {
        isBorder = border;
    }

    public int getMinDistanceRowToBorderPixel() {        return minDistanceRowToBorderPixel;
    }
    public void setMinDistanceRowToBorderPixel(int minDistanceRowToBorderPixel) {
        this.minDistanceRowToBorderPixel = minDistanceRowToBorderPixel;
    }
    public int getMinDistanceColToBorderPixel() {
        return minDistanceColToBorderPixel;
    }
    public void setMinDistanceColToBorderPixel(int minDistanceColToBorderPixel) {
        this.minDistanceColToBorderPixel = minDistanceColToBorderPixel;
    }

    public int getRGBValue(){
        return colorWithId.getRGB();
    }
}
