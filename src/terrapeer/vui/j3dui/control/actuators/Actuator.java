package terrapeer.vui.j3dui.control.actuators;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;
import terrapeer.vui.j3dui.visualize.change.*;

/**
 A class that controls an attached target node through an
 actuation value.  The target node type, control action and
 actuation value format are defined by personality plugins.
 To facilitate status feedback the enable state can be relayed
 to collateral event targets from an event splitter.
 <P>
 An Actuator is similar in concept to an Interpolator, but the
 Actuator is controlled programmatically instead of by a time
 driven alpha value.
 <P>
 The target node controlled by this Actuator should only be
 manipulated through this Actuator.  Setting the target node's
 state directly will invalidate the state of this Actuator.

 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */

public class Actuator implements ActuationTarget, EnableTarget
{

  // public interface =========================================

  /**
    Constructs a Actuator with the personality
    plugin <plugin>.
    @param plugin Personality plugin.  Never null.
   */
  public Actuator(ActuatorPlugin plugin)
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
  public ActuatorPlugin getPlugin()
  {
    return _plugin;
  }

  /**
    Gets the actuator target node as a generic group.
    @return Reference to the personality plugin's target node.
   */
  public Node getTargetNode()
  {
    return _plugin.getTargetNode();
  }

  /**
    Gets the collateral target "actuation" event source,
    which is notified when the actuation state changes.
    @return Reference to the splitter.
   */
  public ActuationSplitter getActuationSource()
  {
    // lazy build
    if (_actuationSplitter == null)
    {
      _actuationSplitter = new ActuationSplitter();

    }
    return _actuationSplitter;
  }

  /**
    Gets the collateral target "enable" event source,
    which is notified when the enable state changes.
    @return Reference to the splitter.
   */
  public EnableSplitter getEnableSource()
  {
    // lazy build
    if (_enableSplitter == null)
    {
      _enableSplitter = new EnableSplitter();

    }
    return _enableSplitter;
  }

  /**
    Gets the enable state.
    @return True if enabled.
   */
  public boolean getEnable()
  {
    return _enable;
  }

  // EnableTarget implementation

  public void setEnable(boolean enable)
  {
    _enable = enable;

    if (Debug.getEnabled())
    {
      Debug.println(this, "Actuator.enable",
                    "ENABLE:Actuator.setEnable:" +
                    " enable=" + _enable +
                    " enableSplitter=" + _enableSplitter);
    }

    if (_enableSplitter != null)
    {
      _enableSplitter.setEnable(_enable);

    }
  }

  // ActuationTarget implementation

  public void initActuation(Tuple4d value)
  {

    if (Debug.getEnabled())
    {
      Debug.println(this, "Actuator",
                    "ACTUATOR:Actuator.initActuation:" +
                    " enable=" + _enable +
                    " value=" + value);
    }

    if (!_enable)
    {
      return;
    }

    _plugin.initActuation(value);

    if (_actuationSplitter != null)
    {
      _actuationSplitter.initActuation(value);

    }
    ChangeSensor.processAllChanges(getTargetNode());
  }

  public void updateActuation(Tuple4d value)
  {

    if (Debug.getEnabled())
    {
      Debug.println(this, "Actuator",
                    "ACTUATOR:Actuator:updateActuation:" +
                    " enable=" + _enable +
                    " value=" + value);
    }

    if (!_enable)
    {
      return;
    }

    _plugin.updateActuation(value);

    if (_actuationSplitter != null)
    {
      _actuationSplitter.updateActuation(value);

    }
    ChangeSensor.processAllChanges(getTargetNode());
  }

  public void syncActuation()
  {

    if (Debug.getEnabled())
    {
      Debug.println(this, "Actuator",
                    "ACTUATOR:Actuator:syncActuation:" +
                    " enable=" + _enable +
                    " -");
    }

    if (!_enable)
    {
      return;
    }

    _plugin.syncActuation();

    if (_actuationSplitter != null)
    {
      _actuationSplitter.syncActuation();

    }
    ChangeSensor.processAllChanges(getTargetNode());
  }

  // personal body ============================================

  /** Personality plugin. */
  private ActuatorPlugin _plugin;

  /** True if actuator enabled. */
  private boolean _enable = true;

  /** "actuation" event splitter.  Null until lazy build. */
  private ActuationSplitter _actuationSplitter = null;

  /** "enable" event splitter.  Null until lazy build. */
  private EnableSplitter _enableSplitter = null;

}
