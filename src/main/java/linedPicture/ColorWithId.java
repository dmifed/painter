package linedPicture;

import java.awt.*;

/**
 * @author dmifed
 */
public class ColorWithId{
    private int RGBValue;
    private int id;

    public ColorWithId(int RGBValue, int id) {
        this.RGBValue = RGBValue;
        this.id = id;
    }

    public int getRGB() {
        return RGBValue;
    }

    public void setRGB(int RGBValue) {
        this.RGBValue = RGBValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
