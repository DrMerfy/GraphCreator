package graph.plotter;

import graph.LineGraph;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
            throw new RuntimeException("Cannot call plot 2 times. Instead put all graphs in one call.");
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
        clear();

        if(!graph.isRendered()) 
            graph.render(LineGraph.Render.ALL);
        handleMapping(graph);
        //The holder
        AnchorPane pane = new AnchorPane();
        pane.setMaxSize(graph.getWidth(), graph.getHeight());
        pane.setPrefWidth(graph.getWidth());

        //Add x-axis
        GridPane xAxis = new GridPane();
        int column = 0;
        for(double i = xStart; i < xEnd; i += xIncrement){
            StackPane st = new StackPane();
            st.setPrefSize(graph.getInterval(), 10);
            NumberFormat formatter = new DecimalFormat("#.##");
            Label label = new Label(formatter.format(i));
            st.getChildren().add(label);
            StackPane.setAlignment(label, Pos.CENTER);
            xAxis.add(st,column,1);
            column++;
        }
        xAxis.setMaxHeight(10);
        pane.getChildren().add(xAxis);
        AnchorPane.setLeftAnchor(xAxis,15.0);
        AnchorPane.setBottomAnchor(xAxis,0.0);


        //Add y-axis
        VBox yAxis = new VBox();
        //Populate y-axis
        for(double i = yStart ; i <= yEnd; i+=yIncrement){
            StackPane st = new StackPane();
            st.setRotate(180);
            st.setPrefSize(40, 20);
            NumberFormat formatter = new DecimalFormat("#.##");
            Label label = new Label(formatter.format(i));
            st.getChildren().add(label);
            StackPane.setAlignment(label, Pos.CENTER);
            yAxis.getChildren().add(st);
            values++;
        }
        yAxis.setMaxWidth(40);
        yAxis.setMaxHeight(graph.getHeight());
        yAxis.setSpacing((graph.getHeight()-50)/(values)-18);
        yAxis.setRotate(180);
        pane.getChildren().add(yAxis);
        AnchorPane.setLeftAnchor(yAxis,0.0);
        AnchorPane.setBottomAnchor(yAxis,11.0);

        //Add the graph
        pane.getChildren().add(graph);
        AnchorPane.setLeftAnchor(graph, 40.0);
        AnchorPane.setBottomAnchor(graph, 15.0);
        Scene scene = new Scene(pane);

        stage.setScene(scene);
        stage.setTitle("Graph");
        stage.show();
    }
}
