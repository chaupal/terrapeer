package terrapeer.vui.j3dui.control.mappers.intuitive;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.mappers.*;

/**
 A source drag mapper plugin that maps an axis-angle rotation
 from source space to target space, where the rotation axis is
 defined in the source space relative to the source node and
 mapped to the target space.
 <P>
 Only the rotation axis is mapped from source to target space.
 The rotation angle is determined from source and target-side
 dimension mapping but without source to target space mapping.
 Typically the target-side mapping alone is used to set which
 source dimension controls the actuation rotation angle.
 <P>
 The output actuation value is defined as follows:
 <UL>
 <LI>X - X rotation axis value.
 <LI>Y - Y rotation axis value.
 <LI>Z - Z rotation axis value.
 <LI>W - Rotation angle value in radians.
 </UL>
 <P>
 By definition, source to actuation space transformation is
 vector not point-based, which means that direction and
 magnitude are mapped but not position.

 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */

public class AxisAngleSourceDragPlugin extends DirectSourceDragPlugin
{

  // public interface =========================================

  /**
    Constructs a AxisAngleSourceDragPlugin with the target space
    defined by the specified node.  Sets the target node's
    capability bits for live use.
    @param targetSpace Target space reference node.  If null the
    world space will be used as the target space.
   */
  public AxisAngleSourceDragPlugin(Node targetSpace)
  {
    super(targetSpace);
  }

  /**
    Sets the axis of rotation.  The axis is defined
    relative to the space set by setUseWorldAxis().
    @param axis Axis of rotation.  Never null.
    @return Reference to <axis>.
   */
  public Vector3d setAxis(Vector3d axis)
  {
    _axis.set(axis);
    return axis;
  }

  /**
    Sets whether or not the axis of rotation is defined in the
    source or world space.  The default is the source space.
    @param useWorldAxis If true, the axis is defined in the
    world space.
   */
  public void setUseWorldAxis(boolean useWorldAxis)
  {
    _useWorldAxis = useWorldAxis;
  }

  // SourceDragMapperPlugin implementation

  public String toString()
  {
    return "AxisAngleSourceDragPlugin";
  }

  // personal body ============================================

  /** Rotation axis in source space. */
  private Vector3d _axis = new Vector3d(0, 1, 0);

  /** True if axis in world instead of source space. */
  private boolean _useWorldAxis = false;

  /** Dummy vector. (for GC) */
  private final Vector3d _vector = new Vector3d();


  public final Vector4d _v4 = new Vector4d();



  // SourceDragMapperPlugin implementation

  protected Vector4d startSourceDrag(Node source, Vector3d pos,
                                     Vector4d copy)
  {
    // do nothing
    return copy;
  }

  protected Vector4d doSourceDrag(Node source, Vector3d pos,
                                  Vector4d copy)
  {

    // map source pos to actuation angle
    toSourceValue(pos, _vector);
    toTargetValue(_vector, copy);


    // map source axis to target actuation axis
    if (_useWorldAxis)
    {
      Mapper.toTargetSpace(_axis, null, getTargetSpace(),
                           _vector);
    }
    else
    {
      Mapper.toTargetSpace(_axis, source, getTargetSpace(),
                           _vector);
    }

    copy.x = _vector.x;
    copy.y = _vector.y;
    copy.z = _vector.z;

    _v4.x = _vector.x;
    _v4.y = _vector.y;
    _v4.z = _vector.z;

    if (Debug.getEnabled())
    {
      Debug.println("AxisAngleSourceDragPlugin",
                    "AXIS:AxisAngleSourceDragPlugin:doSourceDrag:" +
                    " inPos=" + pos +
                    " axis=" + _axis +
                    " copy=" + copy +
                    " useWorld=" + _useWorldAxis +
                    " source=" + source +
                    " target=" + getTargetSpace());
    }

    return copy;
  }

  protected Vector4d stopSourceDrag(Node source, Vector3d pos,
                                    Vector4d copy)
  {
    // do nothing
    return copy;
  }

}
