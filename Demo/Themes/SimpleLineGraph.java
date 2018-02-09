import graph.LineGraph;
import graph.theme.Theme;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import plotter.Plotter;

import java.util.Random;

import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.sin;


public class SimpleLineGraph extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        double[] values = new double[50];

        //---->Default graph using plotter<----
        LineGraph graph = new LineGraph(600,300, Theme.SIMPLELINEGRAPH);
        LineGraph graph1 = new LineGraph(600, 300, Theme.SIMPLELINEGRAPH.withColor(Color.valueOf("#03A9F4")));

        //Populating the graph
        for (int i=0; i<30; i++) {
            graph.addValue(0.15*i + abs(sin(new Random().nextInt(180))));
            graph1.addValue(0.17*i + abs(sin(new Random().nextInt(180))));
        }

        Plotter.enableGrid(true);
        Plotter.setTitle("Transfer rate",graph1);
        Plotter.setAxisLabel("day (January)","GB/s",graph);
        Plotter.mapXAxis(1, 31, graph);
        Plotter.plot(true, graph, graph1);
    }
}
