package linedPicture;

import java.awt.*;

/**
 * @author dmifed
 */
public class ColorWithId{
    private Color color;
    private int id;

    public ColorWithId(Color color, int id) {
        this.color = color;
        this.id = id;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
