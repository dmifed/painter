package linedPicture.exception;

/**
 * @author dmifed
 */
public class PixelOutOfImageException extends Exception{
    private int row;
    private int col;

    public PixelOutOfImageException(String message, int row, int col) {
        super(message);
        this.row = row;
        this.col = col;
    }
}
