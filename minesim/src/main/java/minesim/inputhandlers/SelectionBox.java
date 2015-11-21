package minesim.inputhandlers;

import java.awt.geom.Rectangle2D;

import javafx.scene.paint.Color;
import minesim.entities.WorldEntity;

/*
 * Represents a bounded quadrilateral area. Intended to be used with dragged left click events.
 * @see MouseHandler
  */
public class SelectionBox {
    /**
     * A rectangle which will always contain nothing
     */
    public static final Rectangle2D.Double NOTHING = new Rectangle2D.Double(0, 0, 0, 0);
    private double startX;
    /**
     * top left point - x
     */
    private double startY;
    /**
     * top left point - y
     */
    private double width;
    /**
     * width of selected area
     */
    private double height;
    /**
     * height of selected area
     */
    private double endX;
    /**
     * bottom right point - x
     */
    private double endY;
    /**
     * bottom right point - y
     */
    private Rectangle2D rectangle;
    

    /**
     * Default constructor for SelectionBox
     *
     * @param x1 x component of start point
     * @param y1 y component of start point
     * @param x2 x component of end point
     * @param y2 y component of end point
     */
    public SelectionBox(double x1, double y1, double x2, double y2) {
        rectangle = new Rectangle2D.Double(0, 0, 0, 0);
        setSelectedArea(x1, y1, x2, y2);
    }

    /**
     * Update the bounds of the SelectionBox. Similar to constructor
     *
     * @param x1 x component of start point
     * @param y1 y component of start point
     * @param x2 x component of end point
     * @param y2 y component of end point
     */
    public void setSelectedArea(double x1, double y1, double x2, double y2) {
        /** work out points */
        calculateBounds(x1, y1, x2, y2);

        /** create new rectangle if it doesn't currently exist, otherwise adjust it */
        if (rectangle == null) {
            rectangle = new Rectangle2D.Double(startX, startY, width, height);
        } else {
            rectangle.setFrame(startX, startY, width, height);
        }
    }

    /**
     * Helper method to translate whatever points were provided into 'top left' to 'bottom right'
     *
     * @param x1 x component of top left point
     * @param y1 y component of top left point
     * @param x2 x component of bottom right point
     * @param y2 y component of bottom right point
     */
    private boolean calculateBounds(double x1, double y1, double x2, double y2) {
        /** check bounds, adjust 2x coordinates to start co-ord & height & width */
        if (x1 <= x2 && y1 <= y2) {
            startX = x1;
            startY = y1;
            width = x2 - x1;
            height = y2 - y1;
        } else if (x1 <= x2 && y1 >= y2) {
            startX = x1;
            startY = y2;
            width = x2 - x1;
            height = y1 - y2;
        } else if (x1 >= x2 && y1 <= y2) {
            startX = x2;
            startY = y1;
            width = x1 - x2;
            height = y2 - y1;
        } else if (x1 >= x2 && y1 >= y2) {
            startX = x2;
            startY = y2;
            width = x1 - x2;
            height = y1 - y2;
        }
        endX = startX + width;
        endY = startY + height;
        return true;
    }

    /**
     * Gets a generic representation of the selected arae
     *
     * @return the selected area
     */
    public Rectangle2D getRect() {
        return rectangle;
    }

    /**
     * clear the area to begin next select
     */
    public void clearArea() {
        setSelectedArea(0, 0, 0, 0);
    }

    /**
     * Returns whether an entity is within a selection box or not
     *
     * @param entity whose position is to be determined
     * @return whether it is in the box
     */
    public boolean contains(WorldEntity entity) {
        return !(entity.getYpos() < startY || entity.getYpos() > endY ||
                entity.getXpos() < startX || entity.getXpos() > endX);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SelectionBox that = (SelectionBox) o;

        return Double.compare(that.startX, startX) == 0 && Double.compare(that.startY, startY) == 0
                && Double.compare(that.endX, endX) == 0 && Double.compare(that.endY, endY) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(startX);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(startY);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(endX);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(endY);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
