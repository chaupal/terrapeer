package terrapeer.vui.helpers;

import terrapeer.*;
import javax.vecmath.*;
import javax.media.j3d.*;

/**
 * Extendable Geometry object that contains virtual world geometry information,
 * such as position and scale.
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */

//todo: missing align methods
public class VGeometry
{
  private Transform3D transform3D;
  private Vector3d position;
  private Vector3d translation;
  private double scale;
  private Vector3d rotation;
  private long alphaCoords;

  public VGeometry()
  {
    transform3D = new Transform3D();
    transform3D.set(new Vector3d(0, 0, 0));
    position = new Vector3d(0, 0, 0);
    translation = new Vector3d(0, 0, 0);
    rotation = new Vector3d(0, 0, 0);
    scale = 0.0;
    alphaCoords = 0;
  }

  private void alignTransformation()
  {
    transform3D.setTranslation(position);
  }

  private void alignT3D()
  {
    transform3D.get(position);
    scale = transform3D.getScale();
  }

  public long getAlphaCoords()
  {
    return alphaCoords;
  }
  public void setAlphaCoords(long alphaCoords)
  {
    this.alphaCoords = alphaCoords;
  }
  public javax.vecmath.Vector3d getTranslation()
  {
    return translation;
  }
  public void setTranslation(javax.vecmath.Vector3d translation)
  {
    this.translation = translation;
  }
  public double getScale()
  {
    return scale;
  }
  public void setScale(double scale)
  {
    this.scale = scale;
  }
  public Vector3d getPosition()
  {
    return position;
  }
  public void setPosition(Vector3d position)
  {
    alignTransformation();
    this.position = position;
  }
  public Vector3d getRotation()
  {
    return rotation;
  }
  public void setRotation(Vector3d rotation)
  {
    this.rotation = rotation;
  }
  public Transform3D getTransform3D()
  {
    return transform3D;
  }
  public void setTransform3D(Transform3D transform3D)
  {
    alignT3D();
    this.transform3D = transform3D;
  }

}
