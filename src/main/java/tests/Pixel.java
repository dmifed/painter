package tests;

/**
 * @author dmifed
 */
public class Pixel {
    private int row;
    private int column;
    private int idColor;

    public Pixel(int row, int column, int idColor) {
        this.row = row;
        this.column = column;
        this.idColor = idColor;
    }

    public int getRow() {        return row;    }
    public int getColumn() {        return column;    }

    public int getIdColor() {        return idColor;    }
    public void setIdColor(int idColor) {        this.idColor = idColor;    }
}
