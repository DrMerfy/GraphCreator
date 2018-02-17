package graph;

import graph.theme.Theme;
import graph.theme.Themes;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineJoin;

import java.util.UUID;

import static java.lang.Math.*;
import static javafx.scene.paint.Color.*;

public class LineGraph extends Region {
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
    private ObservableList<Point2D> points;

    private boolean isRendered;
    private boolean isManualEntry;

    //Layers
    private SVGPath graphPath;
    private SVGPath pointLines;
    private Pane pointDots;

    //Styling values
    private double smoothing;
    private Circle circleStyle;
    private boolean close;
    private boolean startAtZero;
    private Render renderer;

    //Normalizer
    private Normalizer normalizer;

    //Rendering (will be factorisized)
    private BooleanProperty onRenderCompleted;

    /**
     * Constructs a LineGraph object based on the specified theme. The dimensions of the graph are calculated based on
     * the screen that the application is plotted.
     * @param theme the specified theme.
     */
    public LineGraph(Theme theme) {
        this(Screen.getWidth()/1.5, Screen.getHeight()/2, theme);
    }

    /**
     *  Constructs a LineGraph object based on the specified theme and dimensions.
     * @param width the width of the graph in pixels.
     * @param height the height of the graph in pixels.
     * @param theme the specified theme.
     */
    public LineGraph(double width, double height, Theme theme){
        initialize(width, height);
        Themes.applyTheme(theme, this, theme.getColor());
    }

    /**
     *  Constructs a LineGraph object based on the specified theme and dimensions.
     * @param width the width of the graph in pixels.
     * @param height the height of the graph in pixels.
     */
    public LineGraph(double width, double height) {
        initialize(width,height);
    }

    private void initialize(double width, double height){
        this.setScaleY(-1);
        this.interval = 1;
        this.currentX = 0;
        this.setMaxWidth(width);
        this.setMaxHeight(height);
        this.setWidth(width);
        this.setHeight(height);
        this.setMinWidth(width);
        this.setMinHeight(height);
        this.points = FXCollections.observableArrayList();
        this.isRendered = false;
        this.isManualEntry = false;

        //Unique identification
        this.setId(UUID.randomUUID().toString());

        //Layers
        this.graphPath = new SVGPath();
        this.pointLines = new SVGPath();
        this.pointDots = new Pane();

        //Styling values
        this.smoothing = 0.2;
        this.circleStyle = new Circle();
        this.close = true;
        this.startAtZero = false;
        graphPath.setStrokeWidth(1);
        graphPath.setStroke(BLACK);
        graphPath.setFill(TRANSPARENT);
        graphPath.setStrokeLineJoin(StrokeLineJoin.ROUND);
        pointLines.setStrokeWidth(0.5);
        pointLines.setStroke(BLACK);
        circleStyle.setRadius(2);
        circleStyle.setFill(BLACK);

        //Normalization
        normalizer = new Normalizer(points, true, true, this);

        this.onRenderCompleted = new SimpleBooleanProperty();
    }

    public void render(Render value) throws RenderException {
        //Check if graph is valid
        if (points.size() < 3)
            throw new RenderException("Insufficient points in graph.", new Throwable("Graph was tried to be rendered using "+points.size()+" points."));

        //Sort points based on x-axis
        if(isManualEntry){
            points.sort((t1, t2) -> (int)(t1.getX() - t2.getX()));
            this.setClose(false);
        }

        //Render only when the normalization is completed
        normalizer.normalizationCompletedProperty().addListener((observable, oldValue, normalized) -> {
            if(!normalized)
                return;
            //Configure starting point
            Point2D startingPoint = new Point2D(points.get(0).getX(), points.get(0).getY());
            if(startAtZero){
                startingPoint = new Point2D(0,0);
            }

            isRendered = true;
            renderer = value;
            switch (value){
                case GRAPH:
                    path = "M"+startingPoint.getX()+","+startingPoint.getY();
                    renderGraph();
                    path += close? "L"+String.valueOf(this.getWidth())+",0": "";
                    graphPath.setContent(path);
                    this.getChildren().add(graphPath);
                    break;
                case LINES:
                    pointLinesPath = "M"+startingPoint.getX() +","+startingPoint.getY();
                    renderLines();
                    pointLines.setContent(pointLinesPath);
                    this.getChildren().add(pointLines);
                    break;
                case POINTS:
                    renderPoints();
                    this.getChildren().add(pointDots);
                    break;
                case ALL:
                    path = "M"+startingPoint.getX() +","+startingPoint.getY();
                    pointLinesPath = "M"+startingPoint.getX() +","+startingPoint.getY();
                    renderGraph();
                    renderLines();
                    renderPoints();

                    path += close? "L"+String.valueOf(this.getWidth())+",0": "";

                    graphPath.setContent(path);
                    pointLines.setContent(pointLinesPath);
                    this.getChildren().addAll(graphPath, pointLines, pointDots);
            }
        });

        //Normalize x and y-axis values
        normalizer.normalize();

    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS SETTERS
    ///////////////////////////////////////////////////////////////////////////

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

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    public boolean isStartAtZero() {
        return startAtZero;
    }

    public void startAtZero(boolean startAtZero) {
        this.startAtZero = startAtZero;
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

    public Render getRenderer() {
        return renderer;
    }

    public Normalizer getNormalizer() {
        return normalizer;
    }

    public BooleanProperty onRenderCompletedProperty() {
        return onRenderCompleted;
    }

    ///////////////////////////////////////////////////////////////////////////
    // POINT ADDERS
    ///////////////////////////////////////////////////////////////////////////
    public void addValue(double value) throws RenderException {
        addPointLocal(new Point2D(currentX, value));
        currentX+= interval;
    }

    /**
     * Adds a point to the graph.
     * @param x The x cartesian coordinates of the point.
     * @param y The y cartesian coordinates of the point.
     */
    public void addPoint(double x, double y) throws RenderException {
        addPointLocal(new Point2D(x,y));
        isManualEntry = true;
    }

    private void addPointLocal(Point2D point) throws RenderException {
        if(point.getX() == Double.NaN)
            throw new RenderException("Invalid value.", new Throwable("X value must be a number."));
        if(point.getY() == Double.NaN)
            throw new RenderException("Invalid value.", new Throwable("Y value must be a number."));
        points.add(point);
    }

    ///////////////////////////////////////////////////////////////////////////
    // RENDERERS
    ///////////////////////////////////////////////////////////////////////////
    private void renderGraph(){
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
            //System.out.println(points.get(i).getX()+","+points.get(i).getY());
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

    ///////////////////////////////////////////////////////////////////////////
    // SMOOTHERS
    ///////////////////////////////////////////////////////////////////////////
    private Point2D getControlPoint(Point2D prevPoint, Point2D point, Point2D nextPoint, boolean reverse){
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

    private double[] line(Point2D prevPoint, Point2D point){
        double lengthX = point.getX() - prevPoint.getX();
        double lengthY = point.getY() - prevPoint.getY();

        double length = sqrt(pow(lengthX,2) + pow(lengthY,2));
        double angle = atan2(lengthX, lengthY);

        return new double[]{length, angle};
    }
}
