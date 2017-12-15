package graph.plotter;

import graph.LineGraph;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Plotter extends Application {
    private static LineGraph graph;

    private static int stageInitialization = 0;
    private static double values =0;

    public static void plot(LineGraph graph){
        Plotter.graph = graph;
        try{
            Application.launch();
        }catch (IllegalStateException e){
            plotGraph(new Stage());
        }
    }

    @Override
    public void start(Stage stage) {
        plotGraph(stage);
    }

    private static void plotGraph(Stage stage){
        if(!graph.isRendered())
            graph.render(LineGraph.Render.ALL);
        //The holder
        AnchorPane pane = new AnchorPane();
        pane.setMaxSize(graph.getWidth(), graph.getHeight());
        pane.setPrefWidth(graph.getWidth());

        //Add x-axis
        GridPane xAxis = new GridPane();
        int column = 0;
        for(int i = 0; i<graph.getNumberOfPoints() * graph.getInterval(); i += graph.getInterval()){
            StackPane st = new StackPane();
            st.setPrefSize(graph.getInterval(), 10);
            Label label = new Label(String.valueOf(i));
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
        double min = graph.getMinValue();
        double max = graph.getMaxValue();
        //Calculate show it starts at 0
        if(min > 0)
            min = 0;
        int tens = 0;
        for(int i = 0; i<String.valueOf(max).length(); i++)
            tens +=10;
        while (max % tens != 0){
            max++;
        }
        //Populate y-axis
        for(double i = max ; i >= min; i-=50){
            StackPane st = new StackPane();
            st.setPrefSize(40, 20);
            Label label = new Label(String.valueOf((int)i));
            st.getChildren().add(label);
            StackPane.setAlignment(label, Pos.CENTER);
            yAxis.getChildren().add(st);
            values++;
        }

        yAxis.setMaxWidth(40);
        yAxis.setMaxHeight(0);
        yAxis.setSpacing(graph.getMaxValue()/(values)-15);
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
