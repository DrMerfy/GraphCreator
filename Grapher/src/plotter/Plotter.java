package plotter;

import graph.LineGraph;
import graph.Normalizer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that can be used to plot a graph.
 */
public class Plotter extends Application {
    private static LineGraph[] graphs;
    private static boolean plotted = false;
    private static boolean axisPlotted = false;
    private static boolean sameWindow = false;

    //Maps the graph id with the stage title.
    private static Map<String, String> titles = new HashMap<>();
    //Maps the graph id with the axises labels.
    private static Map<String,String[]> labels = new HashMap<>();

    //Axis related fields
    //Maps the graph id with the mapped values of the x axis.
    private static Map<String, AxisMap> xMapped = new HashMap<>();

    private static double xStart;
    private static double xEnd;
    private static double xIncrement;

    //Maps the graph id with the mapped values of the y axis.
    private static Map<String, AxisMap> yMapped = new HashMap<>();

    private static double yStart;
    private static double yEnd;
    private static double yIncrement;

    //Styling
    private static boolean addGrid;


    public static void plot(Boolean sameWindow, LineGraph... graphs) throws Exception {
        Plotter.sameWindow = sameWindow;
        //If more than one graphs are present, make the normalization amount equal for all the graphs.
        for (LineGraph graph : graphs) {
            graph.getNormalizer().normalizationType(Normalizer.Type.NONOBJECTIVE);
        }
        plot(graphs);
    }

    /**
     * Plots the graphs specified. (Also renders them)
     * @param graphs
     */
    public static void plot(LineGraph... graphs) throws Exception {
        //if (plotted)
          //  throw new RuntimeException("Plot can be called only once. Instead put all graphs in one call.");
        //Render the graphs
        for( LineGraph graph : graphs)
            graph.render(LineGraph.Render.ALL);

        plotted = true;

        Plotter.graphs = graphs;

        try {
            Application.launch();
        } catch (IllegalStateException e) {
            showGraphs();
        }
    }


    public static void enableGrid(boolean value) {
        addGrid = value;
    }

    public static void setTitle(String title, LineGraph graph) {
        titles.put(graph.getId(), title);
    }

    public static void setAxisLabel(String x, String y, LineGraph graph) {
        labels.put(graph.getId(),new String[]{x,y});
    }

    public static void mapXAxis(double start, double end, LineGraph graph) {
        AxisMap map = new AxisMap(start, end);
        xMapped.put(graph.getId(), map);
    }

    public static void mapYAxis(double start, double end, LineGraph graph) {
        AxisMap map = new AxisMap(start, end);
        yMapped.put(graph.getId(), map);
    }

    @Override
    public void start(Stage stage) throws Exception {
        showGraphs();
    }

    private static void showGraphs() throws Exception {
        Stage oneStage = new Stage();
        if (!sameWindow) {
            for (LineGraph graph : graphs) {
                oneStage = new Stage();
                oneStage.setTitle(titles.get(graph.getId()));
                oneStage.setScene(new Scene(plotGraph(null, graph)));
                oneStage.setResizable(false);
                oneStage.show();
            }
        } else {
            Pane pane = new Pane();
            for (LineGraph graph : graphs) {
                plotGraph(pane, graph);
                oneStage.setTitle(titles.get(graph.getId()));
            }
            oneStage.setScene(new Scene(pane));
            oneStage.setResizable(false);
            oneStage.show();
        }
    }

    private static void handleMapping(LineGraph graph) {
        if (xMapped.containsKey(graph.getId())) {
            AxisMap mapped = xMapped.get(graph.getId());
            double prevV = graph.getNumberOfPoints();
            double newV = Math.abs(mapped.end - mapped.start);
            xIncrement = newV / prevV;
            xStart = mapped.start;
            xEnd = mapped.end;
            xMapped.remove(graph.getId());
        } else {
            xIncrement = graph.getInterval();
            xStart = 0;
            xEnd = graph.getNumberOfPoints() * xIncrement;
        }

        if (yMapped.containsKey(graph.getId())) {
            AxisMap mapped = yMapped.get(graph.getId());
            double newV = Math.abs(mapped.end - mapped.start);
            double v = newV / 10;
            yStart = mapped.start;
            yEnd = mapped.end;
            yIncrement = v;
            yMapped.remove(graph.getId());

        } else {
            //Find max and min y
            if (sameWindow) {
                double startY = Double.MAX_VALUE;
                double endY = Double.MIN_VALUE;
                for (LineGraph grh : graphs) {
                    if(grh.getNormalizer().getMinValue() < startY)
                        startY = grh.getNormalizer().getMinValue();
                    if(grh.getNormalizer().getMaxValue() > endY)
                        endY = grh.getNormalizer().getMaxValue();
                }
                yStart = startY;
                yEnd = endY;
            }else {
                yStart = graph.getNormalizer().getMinValue();
                yEnd = graph.getNormalizer().getMaxValue();
            }
            yIncrement = (yEnd - yStart) / 10;
        }
    }

    private static Pane plotGraph(Pane root, LineGraph graph) throws Exception {
        if (!graph.isRendered()) {
            graph.render(LineGraph.Render.ALL);
        }
        handleMapping(graph);
        //The holder
        AnchorPane pane = new AnchorPane();

        //Add the graph
        pane.getChildren().add(graph);
        AnchorPane.setLeftAnchor(graph, 68.0);

        //Creates axises

        if(!sameWindow || !axisPlotted ) {
            NumberAxis xAxis = new NumberAxis(xStart, xEnd, xIncrement);
            NumberAxis yAxis = new NumberAxis(yStart, yEnd, yIncrement);
            yAxis.setSide(Side.LEFT);

            pane.getChildren().add(xAxis);
            pane.getChildren().add(yAxis);
            //Add x-axis
            if (labels.containsKey(graph.getId()))
                xAxis.setLabel(labels.get(graph.getId())[0]);
            AnchorPane.setLeftAnchor(xAxis, 68.0);
            AnchorPane.setRightAnchor(xAxis, 0.0);
            AnchorPane.setBottomAnchor(xAxis, 0.0);

            //Add y-axis
            if (labels.containsKey(graph.getId()))
                yAxis.setLabel(labels.get(graph.getId())[1]);
            AnchorPane.setLeftAnchor(yAxis, 0.0);
            AnchorPane.setTopAnchor(yAxis, graph.getHeight() - graph.getNormalizer().getRealMaxHeight());
            //Setting margin of y-axis and graph related to the x-axis height
            xAxis.heightProperty().addListener((observable, oldV, newV) -> {
                AnchorPane.setBottomAnchor(yAxis, newV.doubleValue());
                AnchorPane.setBottomAnchor(graph, newV.doubleValue());
            });
            axisPlotted = true;
        }

        Pane out;
        if (root == null)
            out = new Pane();
        else
            out = root;

        out.getChildren().add(pane);

        out.setPadding(new Insets(20));

        //Add optional background
        if(addGrid) {
            graph.setStyle(" -fx-background-color: #EEEEEE00,\n" +
                    "        linear-gradient(from 0.5px 0px to 10.5px 0px, repeat, #F5F5F5 5%, transparent 5%),\n" +
                    "        linear-gradient(from 0px 0.5px to 0px 10.5px, repeat, #F5F5F5 5%, transparent 5%);");
            out.setStyle("-fx-background-color: #EEEEEE");
            addGrid = false;
        }

        return out;
    }
}
