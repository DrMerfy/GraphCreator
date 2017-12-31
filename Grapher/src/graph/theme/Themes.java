package graph.theme;

import graph.LineGraph;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;

public abstract class Themes {
    private static SVGPath graphPath;
    private static SVGPath lines;
    private static Circle dots;

    public static void applyTheme(Theme theme, LineGraph graph){
        graphPath = graph.getGraphPath();
        lines = graph.getPointLines();
        dots = graph.getCircleStyle();

        switch (theme){
            case TRAFFIC:
                traffic(graph);
                break;
        }
    }

    private static void traffic(LineGraph graph){
        graph.setClose(false);

        graphPath.setStrokeWidth(4);
        graphPath.setStroke(new LinearGradient(0,0,0,1,true, CycleMethod.NO_CYCLE,
                            new Stop(0, Color.valueOf("#03A9F4")),
                            new Stop(0.7,Color.valueOf("#f47536")), new Stop(1,Color.valueOf("#d32f2f"))));

        lines.setStroke(Color.TRANSPARENT);
        dots.setFill(Color.TRANSPARENT);
    }
}
