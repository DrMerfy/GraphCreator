package graph.plotter;

import graph.LineGraph;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Plotter extends Application {
    private static LineGraph[] graphs;
    private static boolean plotted = false;

    //Axis related fields
    private static boolean xMapped = false;
    private static double xStart;
    private static double xEnd;
    private static double xIncrement;
    private static double mappedXStart;
    private static double mappedXEnd;

    private static boolean yMapped = false;
    private static double yStart;
    private static double yEnd;
    private static double yIncrement;
    private static double mappedYStart;
    private static double mappedYEnd;

    private static double values =0;

    public static void plot(LineGraph... graphs){
        if(plotted)
            throw new RuntimeException("Plot can be called only once. Instead put all graphs in one call.");
        plotted = true;

        Plotter.graphs = graphs;
        try{
            Application.launch();
        }catch (IllegalStateException e) {
            for (LineGraph graph : graphs) {
                plotGraph(new Stage(), graph);
            }
        }
    }

    public static void mapXAxis(double start, double end){
        mappedXStart = start;
        mappedXEnd = end;
        xMapped = true;
    }

    public static void mapYAxis(double start, double end){
        mappedYStart = start;
        mappedYEnd = end;
        yMapped = true;
    }

    public static void clear(){
        xMapped = false;
        yMapped = false;
        values = 0;
    }

    @Override
    public void start(Stage stage){
        for (LineGraph graph : graphs) {
            plotGraph(new Stage(), graph);
        }
    }

    private static void handleMapping(LineGraph graph){
        if(xMapped){
            double prevV = graph.getNumberOfPoints();
            double newV = Math.abs(mappedXEnd - mappedXStart);
            xIncrement = newV/prevV;
            xStart = mappedXStart;
            xEnd = mappedXEnd;
            xMapped = false;
        }else{
            xIncrement = graph.getInterval();
            xStart = 0;
            xEnd = graph.getNumberOfPoints()*xIncrement;
        }

        if(yMapped){
            double newV = Math.abs(mappedYEnd - mappedYStart);
            double v = newV/10;
            yStart = mappedYStart;
            yEnd = mappedYEnd;
            yIncrement = v;
            yMapped = false;

        }else {
            yStart = graph.getMinValue();
            yEnd = graph.getMaxValue();

            if(yStart > 0)
                yStart = 0;
            if(yEnd > 10) {
                int tens = 10;
                for (int i = 0; i < String.valueOf(yEnd).length() - 2; i++)
                    tens *= 10;
                tens /= 100;
                while (yEnd % tens != 0) {
                    yEnd++;
                }
            }
            yIncrement = (yEnd - yStart)/10;
        }
    }

    private static void plotGraph(Stage stage, LineGraph graph){
        if(!graph.isRendered()) 
            graph.render(LineGraph.Render.ALL);
        handleMapping(graph);
        //The holder
        AnchorPane pane = new AnchorPane();
        pane.setMaxSize(graph.getWidth(), graph.getHeight());
        pane.setPrefWidth(graph.getWidth());

        //Add x-axis
        NumberAxis xAxis = new NumberAxis(xStart,xEnd,xIncrement);

        pane.getChildren().add(xAxis);
        AnchorPane.setLeftAnchor(xAxis,40.0);
        AnchorPane.setRightAnchor(xAxis,5.0);
        AnchorPane.setBottomAnchor(xAxis,0.0);

        //Add y-axis
        NumberAxis yAxis = new NumberAxis(yStart, yEnd, yIncrement);
        yAxis.setSide(Side.LEFT);

        pane.getChildren().add(yAxis);
        AnchorPane.setLeftAnchor(yAxis,0.0);
        AnchorPane.setTopAnchor(yAxis, graph.getHeight() - graph.getRealMaxHeight());
        AnchorPane.setBottomAnchor(yAxis,26.0);

        //Add the graph
        pane.getChildren().add(graph);
        AnchorPane.setLeftAnchor(graph, 40.0);
        AnchorPane.setBottomAnchor(graph, 30.0);
        Scene scene = new Scene(pane);

        stage.setScene(scene);
        stage.setTitle("Graph");
        stage.setResizable(false);
        stage.show();
        clear();
    }
}
