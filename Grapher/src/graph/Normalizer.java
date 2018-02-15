package graph;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;

import java.util.ArrayList;


/**
 * The normalizer class adds a normalization function to the graph. The default behaviour will
 * normalize all points for each axis (both x and y) so that the max point matches the
 * 90% of the window's height and the y axis will match the window's width.
 */
public class Normalizer {
    public enum Type {
        OBJECTIVE,
        NONOBJECTIVE
    }

    private static Type normalizationType = Type.OBJECTIVE;

    //Non objective normalization values
    private static double generalMaxValueY = Double.MIN_VALUE;
    private static double generalMaxValueX = Double.MIN_VALUE;

    private static double catholicNormalizerX = 1;
    private static double catholicNormalizerY = 1;

    private static ArrayList<ObservableList<Point2D>> allGraphPoints = new ArrayList<>();
    //This is evaluated to true when all the graphs are normalized.
    private static BooleanProperty catholicCompletion = new SimpleBooleanProperty(false);
    //Objective normalization values

    private ObservableList<Point2D> points;

    private double maxValue;
    private double minValue;
    private double normalizerY;
    private double normalizerX;
    private boolean isNormalized;


    private boolean normalizeX;
    private boolean normalizeY;
    private BooleanProperty normalizationCompleted;

    private LineGraph self;

    /**
     *
     * @param points represents the arraylist of points to be normalized. The normalization doesn't happen here
     *               so the arraylist could be just the empty instance.
     * @param normalizeX if the normalization of the x axis is wanted.
     * @param normalizeY if the normalization of the y axis is wanted.
     * @param self the graph in which the normalizer will be applied.
     */
    Normalizer(ObservableList<Point2D> points, boolean normalizeX, boolean normalizeY, LineGraph self) {
        this.points = points;
        this.normalizeX = normalizeX;
        this.normalizeY = normalizeY;
        this.self = self;
        //In the beginning no normalization has occurred.
        this.isNormalized = false;
        this.normalizationCompleted = new SimpleBooleanProperty(false);

        //Every time a new point is added the graph is not normalized
        points.addListener((ListChangeListener.Change<? extends Point2D> obs) -> {
            //isNormalized = false;
        });

    }

    public void normalize(){
        //Don't normalize twice.
        if (isNormalized)
            return;

        switch (normalizationType) {
            case OBJECTIVE:
                normalizeObjectively();
                break;
            case NONOBJECTIVE:
                normalizeNonObjectively();
                break;
        }

    }


    /**
     * Specifies the normalization type.
     * @param type OBJECTIVE: Each graph will be normalized with each own parameters
     *             NONOBJECTIVE: All the graphs will have the same normalization amount.
     */
    public void normalizationType(Type type) {
        normalizationType = type;

        if ( type.equals(Type.NONOBJECTIVE)){
            //Non objective initialization
            //Keep track of all the points of all the graphs
            allGraphPoints.add(this.points);

            //Add a listener so when the normalizers are calculated, then implement the normalization.

            catholicCompletion.addListener((observable, oldValue, newValue) -> {
                //Normalize X axis
                    for (int i = 0; i < points.size(); i++) {
                        points.set(i, new Point2D(points.get(i).getX() * catholicNormalizerX, points.get(i).getY()));
                    }

                //Normalize Y axis
                for (int i = 0; i < points.size(); i++) {
                    points.set(i, new Point2D(points.get(i).getX(), points.get(i).getY() * catholicNormalizerY));
                }


                this.normalizationCompleted.setValue(true);

            });
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS - SETTERS
    ///////////////////////////////////////////////////////////////////////////
    public double getMaxValue() {
        return maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getRealMaxHeight() {
        return this.maxValue*normalizerY;
    }

    public boolean isNormalizeX() {
        return normalizeX;
    }

    public void normalizeX(boolean normalizeX) {
        this.normalizeX = normalizeX;
    }

    public boolean isNormalizeY() {
        return normalizeY;
    }

    public void normalizeY(boolean normalizeY) {
        this.normalizeY = normalizeY;
    }

    public BooleanProperty normalizationCompletedProperty() {
        return normalizationCompleted;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Normalizers
    ///////////////////////////////////////////////////////////////////////////

    private void normalizeObjectively() {
        isNormalized = true;
        maxValue = points.get(0).getY();
        minValue = points.get(0).getY();

        double maxValueX = findMinMax();
        //Assume no normalization
        normalizerX = 1;
        normalizerY = 1;

        //Calculate normalizers accordingly
        if (normalizeX)
            normalizerX = (this.self.getWidth())/maxValueX;
        if (normalizeY)
            normalizerY = (0.9*this.self.getHeight())/maxValue;

        for(int i =0; i< points.size(); i++){
            points.set(i, new Point2D(points.get(i).getX()* normalizerX, points.get(i).getY()* normalizerY));
        }

        normalizationCompleted.setValue(true);
    }

    private void normalizeNonObjectively() {
        //Calculate the min-maxes
        double maxValueX = findMinMax();

        //If the max value is larger than the max value of the previous graphs then the normalizer of this graph
        //must be used
        if (maxValueX > generalMaxValueX) {
            generalMaxValueX = maxValueX;

            normalizerX = 1;
            if (normalizeX)
                normalizerX = (self.getWidth())/maxValueX;

            //normalizerXProperty.setValue(normalizerX);
            catholicNormalizerX = normalizerX;
        }

        if (maxValue > generalMaxValueY) {
            generalMaxValueY = maxValue;

            normalizerY = 1;
            if (normalizeY)
                normalizerY = (0.9*self.getHeight())/maxValue ;

            catholicNormalizerY = normalizerY;
            //normalizerYProperty.setValue(normalizerY);
        }

        //If it's the last graph to in the normalization list, the normalization of all graphs is complete
        if(allGraphPoints.indexOf(this.points) == allGraphPoints.size() -1) {
            catholicCompletion.setValue(true);
        }
    }


    private double findMinMax() {
        for(Point2D p : points){
            if(maxValue < p.getY())
                maxValue = p.getY();
            if(minValue > p.getY())
                minValue = p.getY();
        }

        double maxValueX = points.get(0).getX();
        //Find max for x-axis
        for(Point2D p : points){
            if(maxValueX < p.getX())
                maxValueX = p.getX();
        }
        return maxValueX;
    }


}
