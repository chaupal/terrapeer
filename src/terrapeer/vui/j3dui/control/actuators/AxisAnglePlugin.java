package terrapeer.vui.j3dui.control.actuators;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
 An geometry plugin that performs geometric rotation.
 Rotation occurs in 3D about an arbitrary axis defined
 relative to the target transform.  The rotation axis is
 set by the actuation value.
 <P>
 Will not work properly with an SVTransformGroupPlugin.
 An M or MMTransformGroupPlugin must be used.
 <P>
 Also provides a set of utilities for converting between 3D and
 and actuation values.
 <P>
 The actuation input value is defined as follows:
 <UL>
 <LI>X - X rotation axis value.
 <LI>Y - Y rotation axis value.
 <LI>Z - Z rotation axis value.
 <LI>W - Rotation angle value in radians.
 </UL>

 @author Jon Barrilleaux,
 copyright (c) 1999-2000 Jon Barrilleaux,
 All Rights Reserved.
 */

public class AxisAnglePlugin extends TGGeometryPlugin
{

  // public utilities =========================================

  /**
    Converts axis-angle value <value> into an actuation value.
    @param value Axis-angle value.
    @param copy Container for the copied return value.  Unused
    dimensions are unaffected.
    @return Reference to <copy>.
   */
  public static Tuple4d toActuation(AxisAngle4d value,
                                    Tuple4d copy)
  {
    copy.set(value.x, value.y, value.z, value.angle);
    return copy;
  }

  /**
    Converts axis-angle value <value> into an actuation value.
    @param value Axis-angle value.
    @return New actuation value.
   */
  public static Tuple4d toActuation(AxisAngle4d value)
  {
    return toActuation(value, new Vector4d());
  }

  /**
    Converts a rotation axis <axis> and rotation angle <angle>
    into an actuation value.
    @param axis Rotation axis.
    @param angle Rotation angle.
    @param copy Container for the copied return value.  Unused
    dimensions are unaffected.
    @return Reference to <copy>.
   */
  public static Tuple4d toActuation(Tuple3d axis, double angle,
                                    Tuple4d copy)
  {
    copy.x = axis.x;
    copy.y = axis.y;
    copy.z = axis.z;
    copy.w = angle;
    return copy;
  }

  /**
    Converts an actuation value <value> into a axis-angle value.
    @param value Actuation value.
    @param copy Container for the copied return value.
    @return Reference to <copy>.
   */
  public static AxisAngle4d fromActuation(Tuple4d value,
                                          AxisAngle4d copy)
  {
    copy.set(value.x, value.y, value.z, value.w);
    return copy;
  }

  /**
    Converts an actuation value <value> into an axis-angle value.
    @param value Actuation value.
    @return New axis-angle value.
   */
  public static AxisAngle4d fromActuation(Tuple4d value)
  {
    return fromActuation(value, new AxisAngle4d());
  }

  // public interface =========================================

  /**
    Constructs an AxisAnglePlugin with target node <target>.
    Any existing target node state may be lost.
    @param target Target node.  Never null.
   */
  public AxisAnglePlugin(TransformGroup target)
  {
    super(target);
  }

  // GeometryPlugin implementation

  public String toString()
  {
    return "AxisAnglePlugin";
  }

  // personal body ============================================

  /** Initial actuation value. */
  private final Vector4d _init = new Vector4d(0, 1, 0, 0);

  /** Dummy rotation value. (for GC) */
  private final AxisAngle4d _rotate = new AxisAngle4d();

  // GeometryPlugin implementation

  protected Tuple4d getActuationInit()
  {
    return _init;
  }

  protected Transform3D toActuationTransform(Tuple4d value,
                                             Transform3D copy)
  {
    fromActuation(value, _rotate);
    copy.set(_rotate);

    if (Debug.getEnabled())
    {
      Debug.println("AxisAnglePlugin",
                    "GEO:AxisAnglePlugin:toActuationTransform:" +
                    " in=" + value +
                    " out=" + _rotate);
    }

    return copy;
  }

  protected Tuple4d toActuationUpdate(Tuple4d value,
                                      Tuple4d reference, Tuple4d copy)
  {

    throw new IllegalArgumentException("AxisAnglePlugin " +
                                       "can not be used in an SVTransformGroupPlugin.");
  }

  protected Tuple4d toActuationDelta(Tuple4d newValue,
                                     Tuple4d oldValue, Tuple4d copy)
  {
    copy.set(newValue);
    copy.w -= oldValue.w;

    if (Debug.getEnabled())
    {
      Debug.println("AxisAnglePlugin",
                    "GEO:AxisAnglePlugin:toActuationDelta:" +
                    " inNew=" + newValue +
                    " inOld=" + oldValue +
                    " out=" + copy);
    }

    return copy;
  }

}
