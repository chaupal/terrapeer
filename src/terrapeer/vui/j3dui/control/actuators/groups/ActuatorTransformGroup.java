package terrapeer.vui.j3dui.control.actuators.groups;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;
import terrapeer.vui.j3dui.control.actuators.*;

/**
 An abstract base class for an ActuatorGroup based on
 a chain of transform groups.
 <P>
 The transforms in this group are for single-user use and
 should not be accessed or manipulated externally.

 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */

public abstract class ActuatorTransformGroup extends ActuatorGroup
{

  // public interface =========================================

  /**
    Constructs a ActuatorTransformGroup with the specified chain
    array. The groups in the chain are assumed to exist and to be
    connected in a parent-child chain.
    @param chain Chain array.  Null if none.
    @param length Number of groups actually used in the chain.
    If 0 this group will be the head and tail.
    @param child First child node of this group.  Null if none.
   */
  public ActuatorTransformGroup(TransformGroup[] chain,
                                int length, Node child)
  {
    super(chain, length, child);
  }

  /**
    Constructs a ActuatorTransformGroup with a default chain of
    TransformGroup.
    @param length Number of groups in chain.  If this group will
    be the head and tail.
    @param child First child node of this group.  Null if none.
   */
  public ActuatorTransformGroup(int length, Node child)
  {
    super(length, child);
  }

  // GroupChain implementation

  public Group[] buildChain(int length)
  {
    if (length == 0)
    {
      return null;
    }

    TransformGroup[] _chain = new TransformGroup[length];

    for (int xformI = 0; xformI < length; xformI++)
    {
      TransformGroup xform = new TransformGroup();
      _chain[xformI] = xform;

      if (xformI > 0)
      {
        _chain[xformI - 1].addChild(xform);
      }
    }

    return _chain;
  }

  // personal body ============================================

  /**
    Gets the transform at the specified index.  This is a
    personal method to help avoid accidental corruption of the
    transform chain state.
    @param index Transform index (0 = head, length-1 = tail).
    @return The specified transform.  Null if <index> is
    invalid or there are no transforms.
   */
  protected TransformGroup getTransform(int index)
  {
    return (TransformGroup)getGroup(index);
  }

}
