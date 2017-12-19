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
import static javafx.scene.paint.Color.*;

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

    private double maxValue;
    private double minValue;
    private double normalizer;
    private boolean isRendered;
    private boolean isNormalized;

    //Layers
    private SVGPath graphPath;
    private SVGPath pointLines;
    private Pane pointDots;

    //Styling values
    private double smoothing;
    private Circle circleStyle;
    private boolean close;


    public LineGraph(double width, double height) {
        this.setScaleY(-1);
        this.interval = 1;
        this.currentX = 0;
        this.setWidth(width);
        this.setHeight(height);
        this.setMinWidth(width);
        this.setMinHeight(height);
        this.points = new ArrayList<>();
        this.isRendered = false;
        this.isNormalized = false;

        //Layers
        this.graphPath = new SVGPath();
        this.pointLines = new SVGPath();
        this.pointDots = new Pane();

        //Styling values
        this.smoothing = 0.2;
        this.circleStyle = new Circle();
        this.close = true;
        graphPath.setStrokeWidth(1);
        graphPath.setStroke(BLACK);
        graphPath.setFill(TRANSPARENT);
        graphPath.setStrokeLineJoin(StrokeLineJoin.ROUND);
        pointLines.setStrokeWidth(0.5);
        pointLines.setStroke(BLACK);
        circleStyle.setRadius(2);
        circleStyle.setFill(BLACK);
    }

    public void render(Render value){
        if(!isNormalized)
            normalize();
        isRendered = true;
        switch (value){
            case GRAPH:
                path = "M0,0";
                renderGraph();
                path += close? "L"+String.valueOf(this.currentX-interval)+",0": "";
                graphPath.setContent(path);
                this.getChildren().add(graphPath);
                break;
            case LINES:
                pointLinesPath = "M"+points.get(0).getX() +","+points.get(0).getY();
                renderLines();
                pointLines.setContent(pointLinesPath);
                this.getChildren().add(pointLines);
                break;
            case POINTS:
                renderPoints();
                this.getChildren().add(pointDots);
                break;
            case ALL:
                path = "M0,0";
                pointLinesPath = "M"+points.get(0).getX() +","+points.get(0).getY();
                renderGraph();
                renderLines();
                renderPoints();

                path += close? "L"+String.valueOf(this.currentX-interval)+",0": "";

                graphPath.setContent(path);
                pointLines.setContent(pointLinesPath);
                this.getChildren().addAll(graphPath, pointLines, pointDots);
        }

    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getInterval() {
        return interval;
    }

    public double getSmoothing() {
        return smoothing;
    }

    public void setSmoothing(double smoothing) {
        this.smoothing = smoothing;
    }

    public int getNumberOfPoints(){
        return points.size();
    }

    public double getMaxValue() {
        return maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    public SVGPath getGraphPath() {
        return graphPath;
    }

    public SVGPath getPointLines() {
        return pointLines;
    }

    public Circle getCircleStyle() {
        return circleStyle;
    }

    public boolean isRendered() {
        return isRendered;
    }

    public void addValue(double value){
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
        isNormalized = false;
    }

    public void renderGraph(){
        //Calculate smooth curve
        //see: https://medium.com/@francoisromain/smooth-a-svg-path-with-cubic-bezier-curves-e37b49d46c74
        Point2D controlStart;
        Point2D controlEnd;
        path += "L"+points.get(0).getX()+","+points.get(0).getY();

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

        int last = points.size() - 1;
        controlStart = getControlPoint(points.get(last - 1), points.get(last), null, false);
        controlEnd = getControlPoint(points.get(last), null , null, true);
        path += "C" + controlStart.getX() + "," + controlStart.getY() + "," + controlEnd.getX() + "," + controlEnd.getY() + "," +
                points.get(last).getX() + "," + points.get(last).getY();
    }

    private void renderLines(){
        for(int i = 0; i<points.size(); i++){
            pointLinesPath += "M"+points.get(i).getX() +","+points.get(i).getY();
            pointLinesPath +="V0";
        }
    }

    private void renderPoints(){
        for(int i = 0; i<points.size(); i++){
            Circle c = new Circle(points.get(i).getX(), points.get(i).getY(),circleStyle.getRadius(), circleStyle.getFill());
            c.setStroke(circleStyle.getStroke());
            c.setStrokeWidth(circleStyle.getStrokeWidth());
            c.setStrokeType(circleStyle.getStrokeType());
            c.setStrokeLineJoin(circleStyle.getStrokeLineJoin());
            c.setStrokeLineCap(circleStyle.getStrokeLineCap());
            c.setStyle(circleStyle.getStyle());
            pointDots.getChildren().add(c);
        }
    }

    @NotNull
    private Point2D getControlPoint(@Nullable Point2D prevPoint, @Nullable Point2D point, @Nullable Point2D nextPoint, boolean reverse){
        if(point == null)
            point = prevPoint;
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

    private void normalize(){
        isNormalized = true;
        maxValue = points.get(0).getY();
        minValue = points.get(0).getY();
        for(Point2D p : points){
            Point2D translated = translate(p);
            if(maxValue < translated.getY())
                maxValue = translated.getY();
            if(minValue > translated.getY())
                minValue = translated.getY();
        }

        normalizer = (this.getHeight()-50)/maxValue ;

        for(int i =0; i< points.size(); i++){
            points.set(i, new Point2D(points.get(i).getX(), points.get(i).getY()* normalizer));
        }
    }

    @NotNull
    private Point2D translate(Point2D point2D){
        return new Point2D(point2D.getX(), point2D.getY());
    }

}
