package terrapeer.vui.j3dui.control.actuators;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.control.*;
import terrapeer.vui.j3dui.utils.Debug;

/**
 Abstract base class for an actuator plugin that manipulates a
 TransformGroup target node.  The manner in which the transform
 geometry is manipulated is specified by a geometry plugin,
 which also defines the format of the input actuation value.

 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */

public abstract class TransformGroupPlugin extends ActuatorPlugin
{

  // public interface =========================================

  /**
    Constructs a TransformGroupPlugin with the personality
    plugin <plugin>.
    @param plugin Personality plugin.  Never null.
   */
  public TransformGroupPlugin(TGGeometryPlugin plugin)
  {
    if (plugin == null)
    {
      throw new
          IllegalArgumentException("'plugin' is null.");
    }
    _plugin = plugin;
  }

  /**
    Gets the personality plugin.
    @return Reference to the plugin.
   */
  public TGGeometryPlugin getPlugin()
  {
    return _plugin;
  }

  // ActuatorPlugin implementation

  public String toString()
  {
    return "TransformGroupPlugin";
  }

  public Node getTargetNode()
  {
    return _plugin._targetNode;
  }

  // personal body ============================================

  /** Personality plugin.  (direct for speed) */
  TGGeometryPlugin _plugin;

}
