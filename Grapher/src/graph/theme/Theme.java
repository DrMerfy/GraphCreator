package graph.theme;


import javafx.scene.paint.Color;

public enum Theme {
    SIMPLELINEGRAPH,
    SMOOTHFILL,
    TRAFFIC;

    private Color color;

    public Theme withColor(Color c) {
        color = c;
        return this;
    }

    public Color getColor() {
        return color;
    }

}
