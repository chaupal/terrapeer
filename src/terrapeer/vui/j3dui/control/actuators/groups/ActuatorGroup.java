package terrapeer.vui.j3dui.control.actuators.groups;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;
import terrapeer.vui.j3dui.control.actuators.*;

/**
 An abstract group chain with pre-defined actuators that
 provide group-like support for complex and
 constrained geometric operations.  To facilitate status
 feedback the enable state can be relayed to a collateral
 event target.

 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */

public abstract class ActuatorGroup extends GroupChain implements EnableTarget
{

  // public interface =========================================

  /**
    Constructs a ActuatorGroup with the specified chain array.  The
    groups in the chain are assumed to exist and to be connected
    in a parent-child chain.
    @param chain Chain array.  Null if none.
    @param length Number of groups actually used in the chain.
    If 0 this group will be the head and tail.
    @param child First child node of this group.  Null if none.
   */
  public ActuatorGroup(Group[] chain, int length, Node child)
  {
    super(chain, length, child);
  }

  /**
    Constructs an ActuatorGroup with the specified chain length
    and child node.
    @param length Number of groups in chain.  If 0 this group
    will be the head and tail.
    @param child First child node of this group.  Null if none.
   */
  public ActuatorGroup(int length, Node child)
  {
    super(length, child);
  }

  /**
    Sets the "enable" collateral event target, which is
    notified when the enable state changes.
    @param target Collateral event target.  Null if none.
    @return Reference to target.
   */
  public EnableTarget setEventTargetEnable(
      EnableTarget target)
  {

    _targetEnable = target;
    return _targetEnable;
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
      Debug.println("ActuatorGroup.enable",
                    "ENABLE:ActuatorGroup:setEnable:" +
                    " enable=" + _enable +
                    " targetEnable=" + _targetEnable);
    }

    setEnableGroup(enable);

    if (_targetEnable != null)
    {
      _targetEnable.setEnable(enable);

    }
  }

  // personal body ============================================

  /** True if actuator group enabled. */
  private boolean _enable = true;

  /** "enable" collateral event target.  Null if none. */
  private EnableTarget _targetEnable = null;

  /**
    Sets the enable state for all the actuators in the group.
    @param enable True to enable the group, false to disable it.
   */
  protected abstract void setEnableGroup(boolean enable);

}
