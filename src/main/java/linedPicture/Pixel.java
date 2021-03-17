package linedPicture;

import java.awt.*;

/**
 * @author dmifed
 */
public class Pixel {
    private final int row;
    private final int col;
    private int colorId;
    private int countPixelAbove;
    private int countPixelLeft;
    private int countPixelLeftAbove;
    private Color color;
    private boolean isUsed;
    private boolean isMarked;

    public Pixel(int row, int col, int colorId) {
        this.row = row;
        this.col = col;
        this.colorId = colorId;
        countPixelAbove = 0;
        countPixelLeft = 0;
        countPixelLeftAbove = 0;
        isUsed = false;
        isMarked = false;
    }

    @Override
    public String toString() {
        return "y=" + row + " x=" + col + " id=" + colorId;
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

    public boolean isRightBottomPixelForText(){
        return countPixelAbove > 19 && countPixelLeft > 19 && countPixelLeftAbove > 19;
    }

    public int getCountPixelAbove() {        return countPixelAbove;    }
    public int getCountPixelLeft() {        return countPixelLeft;    }
    public int getCountPixelLeftAbove() {        return countPixelLeftAbove;    }
    public void setCountPixelAbove(int countPixelAbove) {        this.countPixelAbove = countPixelAbove;    }
    public void setCountPixelLeft(int countPixelLeft) {        this.countPixelLeft = countPixelLeft;    }
    public void setCountPixelLeftAbove(int countPixelLeftAbove) {        this.countPixelLeftAbove = countPixelLeftAbove;    }

    public void setUsed(boolean used) {        isUsed = used;    }
    public boolean isUsed() {        return isUsed;    }

    public int getRow() {        return row;    }
    public int getCol() {        return col;    }

    public void setColor(Color color) {        this.color = color;    }
    public Color getColor() {        return color;    }

    public int getColorId() {
        return colorId;
    }
    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public boolean isMarked() {        return isMarked;    }
    public void setMarked(boolean marked) {        isMarked = marked;    }
}
