package graph;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The normalizer class adds a normalization function to the graph. The default behaviour will
 * normalize all points for each axis (both x and y) so that the max point matches the
 * 90% of the window's height and the y axis will match the window's width.
 */
public class Normalizer {
    private ObservableList<Point2D> points;

    private double maxValue;
    private double minValue;
    private double normalizerY;
    private double normalizerX;
    private boolean isNormalized;


    private boolean normalizeX;
    private boolean normalizeY;

    private double width;
    private double height;

    /**
     *
     * @param points represents the arraylist of points to be normalized. The normalization doesn't happen here
     *               so the arraylist could be just the empty instance.
     * @param normalizeX if the normalization of the x axis is wanted.
     * @param normalizeY if the normalization of the y axis is wanted.
     * @param width the width of the graph.
     * @param height the height of the graph.
     */
    Normalizer(ObservableList<Point2D> points, boolean normalizeX, boolean normalizeY, double width, double height) {
        this.points = points;
        this.normalizeX = normalizeX;
        this.normalizeY = normalizeY;
        this.width = width;
        this.height = height;
        //In the beginning no normalization has occured.
        this.isNormalized = false;


        //Every time a new point is added the graph is not normalized
        points.addListener((ListChangeListener.Change<? extends Point2D> obs) -> {
            isNormalized = false;
        });
    }

    void normalize(){
        //Don't normalize twice.
        if (isNormalized)
            return;

        isNormalized = true;
        maxValue = points.get(0).getY();
        minValue = points.get(0).getY();
        for(Point2D p : points){
            if(maxValue < p.getY())
                maxValue = p.getY();
            if(minValue > p.getY())
                minValue = p.getY();
        }

        //Find max and min for x-axis
        double maxValueX = points.get(0).getX();
        double minValueX = points.get(0).getX();
        for(Point2D p : points){
            if(maxValueX < p.getX())
                maxValueX = p.getX();
            if(minValueX > p.getX())
                minValueX = p.getX();
        }

        //Assume no normalization
        normalizerX = 1;
        normalizerY = 1;

        //Calculate normalizers accordingly
        if (normalizeX)
            normalizerX = (this.width)/maxValueX;
        if (normalizeY)
            normalizerY = (0.9*this.height)/maxValue ;

        for(int i =0; i< points.size(); i++){
            points.set(i, new Point2D(points.get(i).getX()* normalizerX, points.get(i).getY()* normalizerY));
        }
    }

    void deNormalize() {
        isNormalized = false;

        if (! normalizeX )
            for(int i =0; i< points.size(); i++){
                points.set(i, new Point2D(points.get(i).getX() / normalizerX, points.get(i).getY() / normalizerY));
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


}
