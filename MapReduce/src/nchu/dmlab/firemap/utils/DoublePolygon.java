package nchu.dmlab.firemap.utils;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

/**
 * Used by the Roi classes to return double coordinate arrays and to determine
 * if a point is inside or outside of spline fitted selections.
 */
public class DoublePolygon{
  private Rectangle bounds;
  private double minX, minY, maxX, maxY;

  /** The number of points. */
  public int npoints;

  /* The array of x coordinates. */
  public double xpoints[];

  /* The array of y coordinates. */
  public double ypoints[];

  /** Constructs an empty doublePolygon. */
  public DoublePolygon() {
    npoints = 0;
    xpoints = new double[10];
    ypoints = new double[10];
  }

  /** Constructs a doublePolygon from x and y arrays. */
  public DoublePolygon(double xpoints[], double ypoints[]) {
    if (xpoints.length != ypoints.length)
      throw new IllegalArgumentException("xpoints.length!=ypoints.length");
    this.npoints = xpoints.length;
    this.xpoints = xpoints;
    this.ypoints = ypoints;
  }

  /** Constructs a doublePolygon from x and y arrays. */
  public DoublePolygon(double xpoints[], double ypoints[], int npoints) {
    this.npoints = npoints;
    this.xpoints = xpoints;
    this.ypoints = ypoints;
  }

  /*
   * Constructs a doublePolygon from a Polygon. public doublePolygon(Polygon
   * polygon) { npoints = polygon.npoints; xpoints = new double[npoints];
   * ypoints = new double[npoints]; for (int i=0; i<npoints; i++) { xpoints[i] =
   * polygon.xpoints[i]; ypoints[i] = polygon.ypoints[i]; } }
   */

  /**
   * Returns 'true' if the point (x,y) is inside this polygon. This is a Java
   * version of the remarkably small C program by W. Randolph Franklin at
   * http://
   * www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html#The%20
   * C%20Code
   */
  public boolean contains(double x, double y) {
    boolean inside = false;
    for (int i = 0, j = npoints - 1; i < npoints; j = i++) {
      if (((ypoints[i] > y) != (ypoints[j] > y)) && (x < (xpoints[j] - xpoints[i]) * (y - ypoints[i]) / (ypoints[j] - ypoints[i]) + xpoints[i]))
        inside = !inside;
    }
    return inside;
  }

  public Rectangle getBounds() {
    if (npoints == 0)
      return new Rectangle();
    if (bounds == null)
      calculateBounds(xpoints, ypoints, npoints);
    return bounds.getBounds();
  }

  public Rectangle2D.Double getdoubleBounds() {
    if (npoints == 0)
      return new Rectangle2D.Double();
    if (bounds == null)
      calculateBounds(xpoints, ypoints, npoints);
    return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
  }

  void calculateBounds(double[] xpoints, double[] ypoints, int npoints) {
    minX = Double.MAX_VALUE;
    minY = Double.MAX_VALUE;
    maxX = Double.MIN_VALUE;
    maxY = Double.MIN_VALUE;
    for (int i = 0; i < npoints; i++) {
      double x = xpoints[i];
      minX = Math.min(minX, x);
      maxX = Math.max(maxX, x);
      double y = ypoints[i];
      minY = Math.min(minY, y);
      maxY = Math.max(maxY, y);
    }
    int iMinX = (int) Math.floor(minX);
    int iMinY = (int) Math.floor(minY);
    bounds = new Rectangle(iMinX, iMinY, (int) (maxX - iMinX + 0.5), (int) (maxY - iMinY + 0.5));
  }

  public void addPoint(double x, double y) {
    if (npoints == xpoints.length) {
      double[] tmp = new double[npoints * 2];
      System.arraycopy(xpoints, 0, tmp, 0, npoints);
      xpoints = tmp;
      tmp = new double[npoints * 2];
      System.arraycopy(ypoints, 0, tmp, 0, npoints);
      ypoints = tmp;
    }
    xpoints[npoints] = x;
    ypoints[npoints] = y;
    npoints++;
    bounds = null;
  }

  public DoublePolygon duplicate() {
    int n = this.npoints;
    double[] xpoints = new double[n];
    double[] ypoints = new double[n];
    System.arraycopy(this.xpoints, 0, xpoints, 0, n);
    System.arraycopy(this.ypoints, 0, ypoints, 0, n);
    return new DoublePolygon(xpoints, ypoints, n);
  }

  /* Returns the length of this polygon or line. */
  public double getLength(boolean isLine) {
    double dx, dy;
    double length = 0.0;
    for (int i = 0; i < (npoints - 1); i++) {
      dx = xpoints[i + 1] - xpoints[i];
      dy = ypoints[i + 1] - ypoints[i];
      length += Math.sqrt(dx * dx + dy * dy);
    }
    if (!isLine) {
      dx = xpoints[0] - xpoints[npoints - 1];
      dy = ypoints[0] - ypoints[npoints - 1];
      length += Math.sqrt(dx * dx + dy * dy);
    }
    return length;
  }

  public double PolygonArea() {

    int i, j;
    double area = 0;

    for (i = 0; i < npoints; i++) {
      j = (i + 1) % npoints;
      area += xpoints[i] * ypoints[j];
      area -= ypoints[i] * xpoints[j];
    }

    area /= 2.0;
    return (Math.abs(area));
  }

  public Point polygonCenterOfMass() {

    double cx = 0, cy = 0;
    double A = PolygonArea();
    int i, j;

    double factor = 0;
    for (i = 0; i < npoints; i++) {
      j = (i + 1) % npoints;
      factor = (xpoints[i] * ypoints[j] - xpoints[j] * ypoints[i]);
      cx += (xpoints[i] + xpoints[j]) * factor;
      cy += (ypoints[i] + ypoints[j]) * factor;
    }
    factor = 1.0 / (6.0 * A);
    cx *= factor;
    cy *= factor;

    return new Point((int) Math.abs(Math.round(cx)), (int) Math.abs(Math.round(cy)));
  }


}