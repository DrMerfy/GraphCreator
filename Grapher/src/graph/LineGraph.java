package graph;

import com.sun.istack.internal.Nullable;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineJoin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static java.lang.Math.*;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.TRANSPARENT;

public class LineGraph extends Pane {
    public enum Render {
        ALL,
        GRAPH,
        LINES,
        POINTS
    }

    private int interval;
    private int currentX;

    private String path;
    private String pointLinesPath;
    private ArrayList<Point2D> points;

    //Layers
    private SVGPath graphPath;
    private SVGPath pointLines;
    private Pane pointDots;

    //Styling values
    private double smoothing;


    public LineGraph(double width, double height) {
        this.interval = 1;
        this.currentX = 10;
        this.setWidth(width);
        this.setHeight(height);
        this.setMinWidth(width);
        this.setMinHeight(height);
        this.points = new ArrayList<>();

        //Layers
        this.graphPath = new SVGPath();
        this.pointLines = new SVGPath();
        this.pointDots = new Pane();

        //Styling values
        this.smoothing = 0.2;
        graphPath.setStrokeWidth(1);
        graphPath.setStroke(BLACK);
        graphPath.setFill(TRANSPARENT);
        graphPath.setStrokeLineJoin(StrokeLineJoin.ROUND);
    }

    public void render(Render value){
        switch (value){
            case GRAPH:
                path = "M0,"+this.getHeight();
                renderGraph();
                path += "L"+this.getWidth()+","+this.getHeight();
                graphPath.setContent(path);
                break;
            case LINES:
                pointLinesPath = "M"+points.get(0).getX() +","+points.get(0).getY();
                renderLines();
                pointLines.setContent(pointLinesPath);
                break;
            case POINTS:
                renderPoints();
                break;
            case ALL:
                path = "M0,"+this.getHeight();
                pointLinesPath = "M"+points.get(0).getX() +","+points.get(0).getY();
                renderGraph();
                renderLines();

                path += "L"+this.getWidth()+","+this.getHeight();

                graphPath.setContent(path);
                pointLines.setContent(pointLinesPath);
        }


        pointLines.setStroke(BLACK);


        this.getChildren().addAll(graphPath, pointLines, pointDots);
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public double getSmoothing() {
        return smoothing;
    }

    public void setSmoothing(double smoothing) {
        this.smoothing = smoothing;
    }

    public SVGPath getGraphPath() {
        return graphPath;
    }

    public void setGraphPath(SVGPath graphPath) {
        this.graphPath = graphPath;
    }

    public void addValue(int value){
        addPoint(new Point2D(currentX, value));
        currentX+= interval;
    }

    /**
     * Adds a point to the graph.
     * @param point using cartesian coordinates.
     */
    public void addPoint(Point2D point){
        point = translate(point);
        points.add(point);
    }

    public void renderGraph(){
        //Calculate smooth curve
        //see: https://medium.com/@francoisromain/smooth-a-svg-path-with-cubic-bezier-curves-e37b49d46c74
        Point2D controlStart;
        Point2D controlEnd;
        //pointLinesPath +="V"+this.getHeight();

        controlStart = getControlPoint(null, points.get(0), points.get(1), false);
        controlEnd = getControlPoint(points.get(0), points.get(1), points.get(2), true);

        path += "C"+controlStart.getX()+","+controlStart.getY()+","+controlEnd.getX()+","+controlEnd.getY()+","+
                points.get(1).getX()+","+points.get(1).getY();


        for(int i = 2; i<points.size()-1; i++) {
            controlStart = getControlPoint(points.get(i - 2), points.get(i - 1), points.get(i), false);
            controlEnd = getControlPoint(points.get(i - 1), points.get(i), points.get(i + 1), true);
            path += "C" + controlStart.getX() + "," + controlStart.getY() + "," + controlEnd.getX() + "," + controlEnd.getY() + "," +
                    points.get(i).getX() + "," + points.get(i).getY();
        }
    }

    private void renderLines(){
        for(int i = 1; i<points.size(); i++){
            pointLinesPath += "M"+points.get(i).getX() +","+points.get(i).getY();
            pointLinesPath +="V"+this.getHeight();
        }
    }

    private void renderPoints(){
        for(int i = 1; i<points.size(); i++){
            pointDots.getChildren().add(new Circle(points.get(i).getX(), points.get(i).getY(),2, BLACK));
        }
    }

    @NotNull
    private Point2D getControlPoint(@Nullable Point2D prevPoint, Point2D point, @Nullable Point2D nextPoint, boolean reverse){
        if(point == null)
            point = new Point2D(0,0);
        if(prevPoint == null)
            prevPoint = point;
        if(nextPoint == null)
            nextPoint = point;

        double[] opposedLine = line(prevPoint, nextPoint);

        double length = opposedLine[0] * smoothing;
        double angle = reverse? opposedLine[1]+ PI: opposedLine[1];

        double x = point.getX() + sin(angle)*length;
        double y = point.getY() + cos(angle)*length;

        return new Point2D(x,y);
    }

    @NotNull
    private double[] line(Point2D prevPoint, Point2D point){
        double lengthX = point.getX() - prevPoint.getX();
        double lengthY = point.getY() - prevPoint.getY();

        double length = sqrt(pow(lengthX,2) + pow(lengthY,2));
        double angle = atan2(lengthX, lengthY);

        return new double[]{length, angle};
    }

    @NotNull
    private Point2D translate(Point2D point2D){
        return new Point2D(point2D.getX(), this.getHeight()-point2D.getY()+1);
    }

}
