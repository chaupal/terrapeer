package terrapeer.vui.j3dui.control.actuators;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
 An abstract base class for personality plugins used by
 TransformGroupPlugins to define the transform geometry.
 Also defines the format of the input actuation value.
 <P>
 Unless otherwise noted, all methods are actually used by all
 TransformGroupPlugins that host this plugin.

 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */

public abstract class TGGeometryPlugin
{

  // public interface =========================================

  /**
    Constructs a TGGeometryPlugin with target node <target>.
    Sets the target node's capability bits for live use.
    Any existing target node state may be lost.
    @param target Target node.  Never null.
   */
  public TGGeometryPlugin(TransformGroup target)
  {
    if (target == null)
    {
      throw new
          IllegalArgumentException("'target' is null.");
    }
    _targetNode = target;

    _targetNode.setCapability(
        TransformGroup.ALLOW_TRANSFORM_WRITE);
  }

  /**
    Gets the event target.
    @return Reference to the event target.
   */
  public TransformGroup getTargetNode()
  {
    return _targetNode;
  }

  /**
    Returns a string naming this plugin.
    @return Plugin type string.
   */
  public abstract String toString();

  // personal body ============================================

  /** Target node.  (direct for speed) */
  TransformGroup _targetNode;

  /**
    Must be overridden to return the initial reference
    actuation value.
    @return Reference to an actuation value.
   */
  protected abstract Tuple4d getActuationInit();

  /**
    Must be overridden to translate the actuation value
    into an equivalent transform matrix.
    @param value Actuation value.
    @param copy Container for the copied return value.
    @return Reference to <copy>.
   */
  protected abstract Transform3D toActuationTransform(
      Tuple4d value, Transform3D copy);

  /**
    Must be overridden to translate the actuation input value
    together with the actuation reference value into a new
    actuation state value.  Only used by SVTransformGroupPlugins.
    @param value Input actuation value.
    @param reference Reference actuation value.
    @param copy Container for the copied return value.  Unused
    dimensions are unaffected.
    @return Reference to <copy>.
   */
  protected abstract Tuple4d toActuationUpdate(Tuple4d value,
                                               Tuple4d reference, Tuple4d copy);

  /**
    Must be overridden to compute a delta actuation value
    given the new actuation value and the old actuation value.
    Only used by SM and MMTransformGroupPlugins.
    @param newValue New actuation value.
    @param oldValue Old actuation value.
    @param copy Container for the copied return value.  Unused
    dimensions are unaffected.
    @return Reference to <copy>.
   */
  protected abstract Tuple4d toActuationDelta(
      Tuple4d newValue, Tuple4d oldValue, Tuple4d copy);

}
