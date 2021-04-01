package linedPicture;

/**
 * @author dmifed
 */
public class BoundCorners {
    private int topRow;
    private int bottomRow;
    private int leftColumn;
    private int rightColumn;

    public BoundCorners(int topRow, int bottomRow, int leftColumn, int rightColumn) {
        this.topRow = topRow;
        this.bottomRow = bottomRow;
        this.leftColumn = leftColumn;
        this.rightColumn = rightColumn;
    }

    public int getTopRow() {
        return topRow;
    }

    public void setTopRow(int topRow) {
        this.topRow = topRow;
    }

    public int getBottomRow() {
        return bottomRow;
    }

    public void setBottomRow(int bottomRow) {
        this.bottomRow = bottomRow;
    }

    public int getLeftColumn() {
        return leftColumn;
    }

    public void setLeftColumn(int leftColumn) {
        this.leftColumn = leftColumn;
    }

    public int getRightColumn() {
        return rightColumn;
    }

    public void setRightColumn(int rightColumn) {
        this.rightColumn = rightColumn;
    }
}
