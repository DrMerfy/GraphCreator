import graph.LineGraph;
import graph.theme.Theme;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Traffic extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        double[] values = new double[]{50,166,200,106,94,211,172,133,132,185,152,131,158,180,172,314,262,160};

        //---->Default graph using plotter<----
        LineGraph graph = new LineGraph(400,200, Theme.TRAFFIC);
        graph.setInterval(20);

        //Populating the graph
        for (double value : values) {
            graph.addValue(value);
        }

        //Background styling
        StackPane background = new StackPane();
        background.setPrefHeight(300);
        background.setPrefWidth(600);
        background.setStyle("-fx-background-color: radial-gradient(center 50% 50%, radius 100%, #2a367f, #1f2756)");

        background.getChildren().add(graph);
        StackPane.setAlignment(graph, Pos.CENTER);

        StackPane axis = new StackPane();
        axis.setMaxSize(500,50);
        Line divider = new Line(50,0,550,0);
        divider.setStroke(Color.WHITE);
        divider.setOpacity(0.3);
        axis.getChildren().add(divider);
        StackPane.setAlignment(divider,Pos.CENTER);
        Label time1 = new Label("8am");
        Label time2 = new Label("5pm");
        time1.setTextFill(Color.WHITE);
        time2.setTextFill(Color.WHITE);
        time1.setOpacity(0.5);
        time2.setOpacity(0.5);
        time1.setFont(Font.font("Calibri",16));
        time2.setFont(Font.font("Calibri",16));
        axis.getChildren().addAll(time1,time2);
        StackPane.setAlignment(time1,Pos.BOTTOM_LEFT);
        StackPane.setMargin(time1, new Insets(0,0,0,30));
        StackPane.setAlignment(time2, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(time2, new Insets(0,30,0,0));

        background.getChildren().add(axis);
        StackPane.setAlignment(axis, Pos.BOTTOM_CENTER);
        StackPane.setMargin(axis, new Insets(0,0,40,0));

        primaryStage.setScene(new Scene(background));
        primaryStage.setTitle("Traffic");
        primaryStage.show();
    }
}
