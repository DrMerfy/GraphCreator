import graph.LineGraph;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;

import java.util.Random;

public class Demo extends Application {
    public static void Launch(String args[]){
        Application.launch(args);
    }

    @Override
    public void start(Stage stage){
        //---->Simple linear line-graph<----
        LineGraph linearGraph = new LineGraph(1000, 500);
        //Smoothness
        linearGraph.setSmoothing(0);
        //The x axis interval
        linearGraph.setInterval(50);
        //Graph line color
        linearGraph.getGraphPath().setStroke(Color.BLACK);
        //Points size
        linearGraph.getCircleStyle().setRadius(3);
        //Points color
        linearGraph.getCircleStyle().setFill(Color.BLACK);

        populateGraph(linearGraph);
        linearGraph.render(LineGraph.Render.GRAPH);
        linearGraph.render(LineGraph.Render.POINTS);
        showGraph(new Pane(), linearGraph);

        //---->Colored linear line-graph<---
        LineGraph linearGraphColored = new LineGraph(1000, 500);
        //Smoothness
        linearGraphColored.setSmoothing(0);
        //The x axis interval
        linearGraphColored.setInterval(200);
        //Stroke width of the graph
        linearGraphColored.getGraphPath().setStrokeWidth(1.2);
        //Graph line color
        linearGraphColored.getGraphPath().setStroke(Paint.valueOf("#7B1FA2"));
        //Points size
        linearGraphColored.getCircleStyle().setRadius(8);
        //Point stroke
        linearGraphColored.getCircleStyle().setStroke(Paint.valueOf("#E91E63"));
        //Point stroke type
        linearGraphColored.getCircleStyle().setStrokeType(StrokeType.INSIDE);
        //Point stroke width
        linearGraphColored.getCircleStyle().setStrokeWidth(3);
        //Points color
        linearGraphColored.getCircleStyle().setFill(Paint.valueOf("#7B1FA2"));

        populateGraph(linearGraphColored);
        linearGraphColored.render(LineGraph.Render.GRAPH);
        linearGraphColored.render(LineGraph.Render.POINTS);
        showGraph(new Pane(), linearGraphColored);

        //---->Colored smooth line-graph<---
        LineGraph smoothGraphColored = new LineGraph(1000, 500);
        //Smoothness
        smoothGraphColored.setSmoothing(0.2);
        //The x axis interval
        smoothGraphColored.setInterval(50);
        //Don't close the graph
        smoothGraphColored.setClose(false);
        //Stroke width of the graph
        smoothGraphColored.getGraphPath().setStrokeWidth(3);
        //Graph line color
        smoothGraphColored.getGraphPath().setStroke(
                new LinearGradient(0,0,1000,500,false, CycleMethod.NO_CYCLE,
                        new Stop(0,Color.valueOf("#03A9F4")), new Stop(500,Color.valueOf("#E91E63"))));

        populateGraph(smoothGraphColored);
        smoothGraphColored.render(LineGraph.Render.GRAPH);
        showGraph(new Pane(),smoothGraphColored);

        //---->Colored smooth stem-graph<---
        LineGraph smoothStemColored = new LineGraph(1000, 500);
        //Smoothness
        smoothStemColored.setSmoothing(0.2);
        //The x axis interval
        smoothStemColored.setInterval(50);
        //Don't close the graph
        smoothStemColored.setClose(false);
        //Stroke width of the graph
        smoothStemColored.getGraphPath().setStrokeWidth(2);
        //Graph line color
        smoothStemColored.getGraphPath().setStroke(Paint.valueOf("#FB8C00"));
        //Vertical lines stoke width
        smoothStemColored.getPointLines().setStrokeWidth(1);
        //Vertical lines color
        smoothStemColored.getPointLines().setStroke(Paint.valueOf("#FB8C00"));
        //Points size
        smoothStemColored.getCircleStyle().setRadius(1);
        //Points color
        smoothStemColored.getCircleStyle().setFill(Paint.valueOf("#FB8C00"));

        populateGraph(smoothStemColored);
        smoothStemColored.render(LineGraph.Render.ALL);
        showGraph(new Pane(), smoothStemColored);

        //---->Colored smooth graph<---
        LineGraph smoothGraph = new LineGraph(1000, 500);
        //Smoothness
        smoothGraph.setSmoothing(0.2);
        //The x axis interval
        smoothGraph.setInterval(50);
        //Stroke width of the graph
        smoothGraph.getGraphPath().setStrokeWidth(2);
        //Graph line color
        smoothGraph.getGraphPath().setStroke(Paint.valueOf("#F5F5F5"));
        //Graph fill color
        smoothGraph.getGraphPath().setFill(new LinearGradient(0,0,0,500,false,
                CycleMethod.NO_CYCLE, new Stop(0.5,Color.valueOf("#283593")), new Stop(1,Color.valueOf("#0288D1"))));
        //Graph drop shadow
        smoothGraph.getGraphPath().setEffect(new DropShadow(BlurType.GAUSSIAN, Color.valueOf("#1A237E"),8,0,0,0));
        //Points size
        smoothGraph.getCircleStyle().setRadius(3);
        //Points color
        smoothGraph.getCircleStyle().setFill(Paint.valueOf("#F5F5F5"));

        populateGraph(smoothGraph);
        smoothGraph.render(LineGraph.Render.GRAPH);
        smoothGraph.render(LineGraph.Render.POINTS);
        Pane pane2 = new Pane();
        pane2.setStyle("-fx-background-color: #283593");
        showGraph(pane2,smoothGraph);

        //---->Colored multi graphs<---
        Random random = new Random();

        //Graph1
        LineGraph graph1 = new LineGraph(1000, 500);
        //Smoothness
        graph1.setSmoothing(0.2);
        //The x axis interval
        graph1.setInterval(150);
        //Graph line color
        graph1.getGraphPath().setStroke(Color.TRANSPARENT);
        //Graph fill color
        graph1.getGraphPath().setFill(Paint.valueOf("#FF5722"));
        //Graph opacity
        graph1.getGraphPath().setOpacity(0.7);

        //Populate
        for(int i =0; i< 12; i++)
            graph1.addValue(random.nextInt(400)+1);

        graph1.render(LineGraph.Render.GRAPH);

        //Graph2
        LineGraph graph2 = new LineGraph(1000, 500);
        //Smoothness
        graph2.setSmoothing(0.2);
        //The x axis interval
        graph2.setInterval(150);
        //Graph line color
        graph2.getGraphPath().setStroke(Color.TRANSPARENT);
        //Graph fill color
        graph2.getGraphPath().setFill(Paint.valueOf("#4CAF50"));
        //Graph opacity
        graph2.getGraphPath().setOpacity(0.7);

        //Populate
        for(int i =0; i< 12; i++)
            graph2.addValue(random.nextInt(400)+1);

        graph2.render(LineGraph.Render.GRAPH);

        //Graph3
        LineGraph graph3 = new LineGraph(1000, 500);
        //Smoothness
        graph3.setSmoothing(0.2);
        //The x axis interval
        graph3.setInterval(150);
        //Graph line color
        graph3.getGraphPath().setStroke(Color.TRANSPARENT);
        //Graph fill color
        graph3.getGraphPath().setFill(Paint.valueOf("#FFC107"));
        //Graph opacity
        graph3.getGraphPath().setOpacity(0.7);

        //Populate
        for(int i =0; i< 12; i++)
            graph3.addValue(random.nextInt(230)+1);

        graph3.render(LineGraph.Render.GRAPH);

        //Graph4
        LineGraph graph4 = new LineGraph(1000, 500);
        //Smoothness
        graph4.setSmoothing(0.2);
        //The x axis interval
        graph4.setInterval(150);
        //Graph line color
        graph4.getGraphPath().setStroke(Color.TRANSPARENT);
        //Graph fill color
        graph4.getGraphPath().setFill(Paint.valueOf("#2196F3"));
        //Graph opacity
        graph4.getGraphPath().setOpacity(0.7);

        //Populate
        for(int i =0; i< 12; i++)
            graph4.addValue(random.nextInt(180)+1);

        graph4.render(LineGraph.Render.GRAPH);

        Pane pane =new Pane();
        pane.setStyle("-fx-background-color: radial-gradient(center 50% 50%, radius 100%, #303F9F, #191e3f)");
        showGraph(pane,graph1,graph2,graph3,graph4);

        //---->Colored linear multi graphs<---

        //Graph1
        LineGraph linearGraph1 = new LineGraph(1000, 500);
        //Smoothness
        linearGraph1.setSmoothing(0);
        //The x axis interval
        linearGraph1.setInterval(45);
        //Don't close the graph
        linearGraph1.setClose(false);
        //Stroke width of the graph
        linearGraph1.getGraphPath().setStrokeWidth(2);
        //Graph line color
        linearGraph1.getGraphPath().setStroke(Paint.valueOf("#212121"));
        //Points size
        linearGraph1.getCircleStyle().setRadius(3);
        //Points color
        linearGraph1.getCircleStyle().setFill(Paint.valueOf("#212121"));



        //Populate
        for(int i =0; i< 500; i +=30) {
            linearGraph1.addValue(i - random.nextInt(i/3+1));
        }

        linearGraph1.render(LineGraph.Render.GRAPH);
        linearGraph1.render(LineGraph.Render.POINTS);

        //Graph2
        LineGraph linearGraph2 = new LineGraph(1000, 500);
        //Smoothness
        linearGraph2.setSmoothing(0);
        //The x axis interval
        linearGraph2.setInterval(45);
        //Don't close the graph
        linearGraph2.setClose(false);
        //Stroke width of the graph
        linearGraph2.getGraphPath().setStrokeWidth(2);
        //Graph line color
        linearGraph2.getGraphPath().setStroke(Paint.valueOf("#00838F"));
        //Points size
        linearGraph2.getCircleStyle().setRadius(3);
        //Points color
        linearGraph2.getCircleStyle().setFill(Paint.valueOf("#00838F"));

        //Populate
        for(int i =0; i< 500; i +=27) {
            linearGraph2.addValue(i - random.nextInt(i/3+1));
        }

        linearGraph2.render(LineGraph.Render.GRAPH);
        linearGraph2.render(LineGraph.Render.POINTS);

        showGraph(new Pane(), linearGraph1, linearGraph2);

        //---->Colored scatter multi graphs<---

        //Graph1
        LineGraph scatterGraph1 = new LineGraph(1000, 500);
        //Smoothness
        scatterGraph1.setSmoothing(0);
        //The x axis interval
        scatterGraph1.setInterval(25);
        //Don't close the graph
        scatterGraph1.setClose(false);
        //Points size
        scatterGraph1.getCircleStyle().setRadius(4);
        //Points color
        scatterGraph1.getCircleStyle().setFill(Paint.valueOf("#212121"));



        //Populate
        for(int i =0; i< 1000; i++) {
            scatterGraph1.addValue(random.nextInt(400));
        }

        scatterGraph1.render(LineGraph.Render.POINTS);

        //Graph2
        LineGraph scatterGraph2 = new LineGraph(1000, 500);
        //Smoothness
        scatterGraph2.setSmoothing(0);
        //The x axis interval
        scatterGraph2.setInterval(25);
        //Don't close the graph
        scatterGraph2.setClose(false);
        //Points size
        scatterGraph2.getCircleStyle().setRadius(4);
        //Points color
        scatterGraph2.getCircleStyle().setFill(Paint.valueOf("#f44336"));

        //Populate
        for(int i =0; i< 1000; i++) {
            scatterGraph2.addValue(random.nextInt(400));
        }

        scatterGraph2.render(LineGraph.Render.POINTS);

        showGraph(new Pane(), scatterGraph1, scatterGraph2);

    }

    static void populateGraph(LineGraph graph){
        int[] values = new int[]{50, 70, 70, 42, 50, 80, 90, 90, 52, 22, 20, 52, 48, 42, 54, 20, 19, 24, 40, 82, 78, 52, 51, 62, 65};

        for(int v : values)
            graph.addValue(v*5);
    }

    static void populateGraph(LineGraph graph, int factor){

    }

    static void showGraph(Pane pane, LineGraph...graphs){
        pane.getChildren().addAll(graphs);
        Stage stage = new Stage();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }
}
