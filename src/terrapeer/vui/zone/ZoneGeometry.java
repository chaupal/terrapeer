package terrapeer.vui.zone;

import terrapeer.*;
import terrapeer.vui.helpers.*;
import java.io.Serializable;
import javax.vecmath.*;

/**
* Encapsulates all geometric data relevant to the Zone, including:
* - Geometric Type
* - Position
* - Width and Height (X/Y span of plane)
* A zone is usually a rectangle placed on the ground level of the virtual world.
 * Extends VGeometry for position information
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */
public class ZoneGeometry extends VGeometry implements Serializable
{
  private int zone_GeoType;
  private javax.vecmath.Vector3d twoPoint_NW;
  private javax.vecmath.Vector3d twoPoint_SE;
  private Vector3d fourPoint_NW;
  private Vector3d fourPoint_NE;
  private Vector3d fourPoint_SE;
  private Vector3d fourPoint_SW;
  private java.util.Vector multiPoint;
  private javax.vecmath.Color3f color;
  private double height;
  private double depth;
  private int zone_BorderType;
  private int twoPoint_X1;
  private int twoPoint_Y1;
  private int twoPoint_X2;
  private int twoPoint_Y2;
  private int twoPoint_W;
  private int twoPoint_H;
  private Vector4d twoPoint_V4d;

  /**
   * Constructor sets default values
   */
  public ZoneGeometry()
  {
    zone_GeoType = vars.GEOTYPE_2P;
    color = vars.ZONE_DEFAULT_COLOR;
    height = vars.ZONE_DEFAULT_HEIGHT;
    depth = vars.ZONE_DEFAULT_DEPTH;
    twoPoint_W = vars.ZONE_DEFAULT_PLANE_WIDTH;
    twoPoint_H = vars.ZONE_DEFAULT_PLANE_HEIGHT;
    super.setPosition(new Vector3d(0, 0, 0));
    twoPoint_V4d = new Vector4d(0, 0, 0, 0);

    calcXY2();
    calcVec();
  }

  public int getZone_GeoType()
  {
    return zone_GeoType;
  }
  public void setZone_GeoType(int zone_GeoType)
  {
    this.zone_GeoType = zone_GeoType;
  }

  private void calcSize()
  {
    twoPoint_W = twoPoint_X2 - twoPoint_X1;
    twoPoint_H = twoPoint_Y2 - twoPoint_Y1;
    if(twoPoint_W<0) twoPoint_W *= -1;
    if(twoPoint_H<0) twoPoint_H *= -1;
  }

  private void calcXY2()
  {
    twoPoint_X1 = ((int)(super.getPosition().x - (twoPoint_W /2)));
    twoPoint_Y1 = ((int)(super.getPosition().z - (twoPoint_H /2)));
    twoPoint_X2 = twoPoint_X1 + twoPoint_W;
    twoPoint_Y2 = twoPoint_Y1 + twoPoint_H;
  }

  private void calcVec()
  {
    twoPoint_V4d.set(twoPoint_X1, twoPoint_Y1, twoPoint_X2, twoPoint_Y2);
  }

  public void setPosition(Vector3d position)
  {
    super.setPosition(position);
    calcXY2();
  }

  public void set2Points(Vector3d nw, Vector3d sw)
  {
    this.twoPoint_NW = nw;
    this.twoPoint_SE = sw;
  }

  public void set4Points(Vector3d nw, Vector3d ne, Vector3d se, Vector3d sw)
  {
    this.fourPoint_NE = ne;
    this.fourPoint_NW = nw;
    this.fourPoint_SE = se;
    this.fourPoint_SW = sw;
  }

  public void setMultiPoints()
  {
    //this.multiPoint = ;
  }

  public javax.vecmath.Color3f getColor()
  {
    return color;
  }
  public void setColor(javax.vecmath.Color3f color)
  {
    this.color = color;
  }
  public double getHeight()
  {
    return height;
  }
  public void setHeight(double height)
  {
    this.height = height;
  }
  public double getDepth()
  {
    return depth;
  }
  public void setDepth(double depth)
  {
    this.depth = depth;
  }
  public int getZone_BorderType()
  {
    return zone_BorderType;
  }
  public void setZone_BorderType(int zone_BorderType)
  {
    this.zone_BorderType = zone_BorderType;
  }
  public int getTwoPoint_X1()
  {
    return twoPoint_X1;
  }
  public int getTwoPoint_Y1()
  {
    return twoPoint_Y1;
  }
  public int getTwoPoint_W()
  {
    return twoPoint_W;
  }
  public void setTwoPoint_W(int twoPoint_W)
  {
    this.twoPoint_W = twoPoint_W;
    calcXY2();
  }
  public int getTwoPoint_H()
  {
    return twoPoint_H;
  }
  public void setTwoPoint_H(int twoPoint_H)
  {
    this.twoPoint_H = twoPoint_H;
    calcXY2();
  }
  public int getTwoPoint_X2()
  {
    return twoPoint_X2;
  }
  public int getTwoPoint_Y2()
  {
    return twoPoint_Y2;
  }
  public Vector4d getTwoPoint_V4d()
  {
    return twoPoint_V4d;
  }

}
