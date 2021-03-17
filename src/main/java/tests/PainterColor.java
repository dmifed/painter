package tests;

import java.awt.*;

/**
 * @author dmifed
 */
public class PainterColor extends Color {
    private int id;
    public PainterColor(int r, int g, int b, int id) {
        super(r, g, b);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
