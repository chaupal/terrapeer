package terrapeer.vui.j3dui.control.actuators.groups;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.actuators.*;

/**
 An actuator group that supports geometric translation,
 rotation, and scale.  The order of transforms in the chain
 starting with the head are: translation, rotation, scale.
 <P>
 The transforms in this group are for single-user use and
 should not be accessed or manipulated externally.

 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */

public class AffineGroup extends ActuatorTransformGroup
{

  // public interface =========================================

  /**
    Constructs an AffineGroup.
   */
  public AffineGroup()
  {
    this(null);
  }

  /**
    Constructs an AffineGroup with a child node.
    @param node First child node of this group.  Null if none.
   */
  public AffineGroup(Node node)
  {
    super(3, node);

    _translation = new Actuator(new SVTransformGroupPlugin(
        new TranslationPlugin(getTransform(0))));

    _rotation = new Actuator(new SMTransformGroupPlugin(
        new AxisAnglePlugin(getTransform(1))));

    _scale = new Actuator(new SVTransformGroupPlugin(
        new ScalePlugin(getTransform(2))));
  }

  /**
    Constructs an AffineGroup with a child node and the
    specified geometry.
    @param pos Position.
    @param rot Rotation.
    @param scl Scale.
   */
  public AffineGroup(Node node, Vector3d pos, AxisAngle4d rot,
                     Vector3d scl)
  {
    this(node);

    getTranslation().initActuation(
        TranslationPlugin.toActuation(pos));
    getAxisAngle().initActuation(
        AxisAnglePlugin.toActuation(rot));
    getScale().initActuation(
        ScalePlugin.toActuation(scl));
  }

  /**
    Gets the translation actuator (see TranslationPlugin
    for actuation value definition).
    @return Reference to the actuator.
   */
  public Actuator getTranslation()
  {
    return _translation;
  }

  /**
    Gets the rotation actuator. (see AxisAnglePlugin
    for actuation value definition).
    @return Reference to the actuator.
   */
  public Actuator getAxisAngle()
  {
    return _rotation;
  }

  /**
    Gets the scale actuator. (see ScalePlugin
    for actuation value definition).
    @return Reference to the actuator.
   */
  public Actuator getScale()
  {
    return _scale;
  }

  // personal body ============================================

  /** Translation actuator. Never null. */
  private Actuator _translation;

  /** Rotation actuator. Never null. */
  private Actuator _rotation;

  /** Scale actuator. Never null. */
  private Actuator _scale;

  // ActuatorGroup implementation

  protected void setEnableGroup(boolean enable)
  {
    _translation.setEnable(enable);
    _rotation.setEnable(enable);
    _scale.setEnable(enable);
  }

}
