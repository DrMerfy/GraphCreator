import graph.LineGraph;
import graph.theme.Theme;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import plotter.Plotter;

import java.util.Random;

import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.sin;


public class SmoothFillCurve extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        int[] values = new int[]{0, 30, 40, 40, 22, 30, 60, 65, 65, 22, 11, 10, 30, 34, 42, 24, 0};
        int[] values1 = new int[]{0, 20, 52, 48, 42, 54, 20, 19, 24, 40, 82, 78, 52, 51, 62, 65, 0};
        int[] values2 = new int[]{0, 10, 15, 23, 12, 24, 22, 30, 65, 43, 32, 42, 38, 27, 22, 12, 0};

        //---->Default graph using plotter<----
        LineGraph graph = new LineGraph(600,300, Theme.SMOOTHFILL.withColor(Color.valueOf("#03A9F480")));
        LineGraph graph1 = new LineGraph(600,300, Theme.SMOOTHFILL.withColor(Color.valueOf("#FF572260")));
        LineGraph graph2 = new LineGraph(600,300, Theme.SMOOTHFILL.withColor(Color.valueOf("#FFC10790")));


        //Populating the graphs
        for (int i=0; i<values.length; i++) {
            graph.addValue(values[i]);
            graph1.addValue(values1[i]);
            graph2.addValue(values2[i]);
        }

        //Render everything, as the theme will take care of the rest
        graph.render(LineGraph.Render.ALL);
        graph1.render(LineGraph.Render.ALL);
        graph2.render(LineGraph.Render.ALL);

        Plotter.setTitle("Click number", graph);
        Plotter.plot(true, graph2, graph1, graph);
    }
}
