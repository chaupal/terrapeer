package terrapeer.vui.j3dui.control.actuators;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.control.*;
import terrapeer.vui.j3dui.utils.Debug;

/**
 (Multi-user Matrix TransformGroupPlugin)  A transform group
 actuator plugin that assumes non-exclusive control of the
 target node and that the actuation state must be maintained
 by a matrix (Transform3D).
 <P>
 The target actuation value can not be clamped (i.e. it is not
 a vector).
 <P>
 To accomodate multiple users updateActuation() updates the
 actuation state relative to the target node's actual state
 rather than the actuator's internal state.  As such updates
 require reading the target's transformation state, which is
 the least efficient form of actuator.  This also means that
 the plugin loses its option to not preserve actuation state.

 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */

public class MMTransformGroupPlugin extends TransformGroupPlugin
{

  // public interface =========================================

  /**
    Constructs an MMTransformGroupPlugin with the personality
    plugin <plugin>.  Sets the plugin's target node capability
    bits for live use.
    @param plugin Personality plugin.  Never null.
   */
  public MMTransformGroupPlugin(TGGeometryPlugin plugin)
  {
    super(plugin);

    // needed for incremental updates
    _plugin._targetNode.setCapability(
        TransformGroup.ALLOW_TRANSFORM_READ);
  }

  // ActuatorPlugin implementation

  public String toString()
  {
    return "MMTransformGroupPlugin";
  }

  // personal body ============================================

  /** Old actuation value.  (direct for speed)*/
  Vector4d _oldValue = new Vector4d();

  /** True if sync is needed.  (direct for speed)*/
  boolean _needSync = true;

  /** Dummy actuation value.  (for GC) */
  private final Vector4d _value = new Vector4d();

  /** Dummy delta actuation value.  (for GC)*/
  private final Vector4d _delta = new Vector4d();

  /** Dummy transform state.  (for GC) */
  private final Transform3D _state = new Transform3D();

  /** Dummy transform reference.  (for GC) */
  private static final Transform3D _reference =
      new Transform3D();

  // ActuatorPlugin implementation

  /**
    Will destroy any existing multi-user state.
   */
  protected void initActuation(Tuple4d value)
  {

    if (Debug.getEnabled())
    {
      Debug.println(this, "MMTransformGroupPlugin",
                    "MMTransformGroupPlugin:initActuation:" +
                    " in=" + value);
    }

    toActuationSource(value, _value);
    _oldValue.set(_value);
    _plugin.toActuationTransform(_value, _state);
    _plugin._targetNode.setTransform(_state);
  }

  /**
    Must be overridden if update is anything but incremental
    matrix multiplicative.
   */
  protected void updateActuation(Tuple4d value)
  {

    if (Debug.getEnabled())
    {
      Debug.println(this, "MMTransformGroupPlugin",
                    "MMTransformGroupPlugin:updateActuation:" +
                    " in=" + value);
    }

    toActuationSource(value, _value);

    // compute actuation delta
    if (_needSync)
    {
      _plugin.toActuationDelta(_value, _value, _delta);
      _needSync = false;
    }
    else
    {
      _plugin.toActuationDelta(_value, _oldValue, _delta);
    }
    _oldValue.set(_value);

    // update target transform by delta
    _plugin.toActuationTransform(_delta, _state);

    _plugin._targetNode.getTransform(_reference);
    _state.mul(_reference);
    _plugin._targetNode.setTransform(_state);
  }

  protected void syncActuation()
  {

    if (Debug.getEnabled())
    {
      Debug.println(this, "MMTransformGroupPlugin",
                    "MMTransformGroupPlugin:syncActuation:" +
                    " -");
    }

    _needSync = true;
  }

}
