package terrapeer.vui.j3dui.control.inputs;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;

/**
 A cancel trigger that monitors modifier events for the
 escape key.

 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */

public class InputCancelTrigger implements InputModifierTarget
{

  // public interface =========================================

  /**
    Constructs an InputCancelTrigger with event target <target>.
    @param target Event target.  Never null.
   */
  public InputCancelTrigger(InputCancelTarget target)
  {
    if (target == null)
    {
      throw new
          IllegalArgumentException("'target' is null.");
    }
    _eventTarget = target;
  }

  /**
    Gets the event target.
    @return Reference to event target.
   */
  public InputCancelTarget getEventTarget()
  {
    return _eventTarget;
  }

  // InputModifierTarget implementation

  public void setInputModifier(int modifier)
  {
    boolean cancel = ((modifier & Input.MODIFIER_ESC) ==
                      Input.MODIFIER_ESC);

    if (Debug.getEnabled())
    {
      Debug.println("InputCancelTrigger",
                    "CANCEL:InputCancelTrigger:setInputModifier:" +
                    " modifier=" + modifier +
                    " cancel=" + cancel);
    }

    if (cancel)
    {
      _eventTarget.setInputCancel();
    }
  }

  // personal body ============================================

  /** Event target. */
  private InputCancelTarget _eventTarget;

}
