package graph;

import java.awt.*;

/**
 * Represents the screen in which the app is plotted.
 */
class Screen {
    private static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private static int width = gd.getDisplayMode().getWidth();
    private static int height = gd.getDisplayMode().getHeight();

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }
}
