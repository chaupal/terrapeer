package terrapeer.vui.j3dui.control.actuators.groups;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.actuators.*;

/**
 An actuator group that supports spherical rotations, like
 rolling a ball.  The transformation is expressed as a set of
 axis-angle rotations applied incrementally to a single
 multi-matrix transform.  All rotation axes are relative to
 group, not the rotated target node.
 <P>
 Although the rotations are named X, Y and Z, as with any
 axis-angle rotation the rotation axes are arbitrary.

 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */

public class AxisAngleSphereGroup extends ActuatorTransformGroup
{

  // public interface =========================================

  /**
    Constructs an AxisAngleSphereGroup.
   */
  public AxisAngleSphereGroup()
  {
    this(null);
  }

  /**
    Constructs an AxisAngleSphereGroup with a child node.
    @param node First child node of this group.  Null if none.
   */
  public AxisAngleSphereGroup(Node node)
  {
    super(1, node);

    _rotateX = new Actuator(new MMTransformGroupPlugin(
        new AxisAnglePlugin(getTransform(0))));

    _rotateY = new Actuator(new MMTransformGroupPlugin(
        new AxisAnglePlugin(getTransform(0))));

    _rotateZ = new Actuator(new MMTransformGroupPlugin(
        new AxisAnglePlugin(getTransform(0))));
  }

  /**
    Gets the X axis-angle actuator (see AxisAnglePlugin
    for actuation value definition).
    @return Reference to the actuator.
   */
  public Actuator getAxisAngleX()
  {
    return _rotateX;
  }

  /**
    Gets the Y axis-angle actuator (see AxisAnglePlugin
    for actuation value definition).
    @return Reference to the actuator.
   */
  public Actuator getAxisAngleY()
  {
    return _rotateY;
  }

  /**
    Gets the Z axis-angle actuator (see AxisAnglePlugin
    for actuation value definition).
    @return Reference to the actuator.
   */
  public Actuator getAxisAngleZ()
  {
    return _rotateZ;
  }

  // personal body ============================================

  /** AxisAngle X actuator. Never null. */
  private Actuator _rotateX;

  /** AxisAngle Y actuator. Never null. */
  private Actuator _rotateY;

  /** AxisAngle Z actuator. Never null. */
  private Actuator _rotateZ;

  // ActuatorGroup implementation

  protected void setEnableGroup(boolean enable)
  {
    _rotateX.setEnable(enable);
    _rotateY.setEnable(enable);
    _rotateZ.setEnable(enable);
  }

}
