package terrapeer.vui.j3dui.control.inputs;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
 Splits (multicasts) the source event to multiple event targets.

 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */

public class InputDragSplitter implements InputDragTarget
{

  // public interface =========================================

  /**
    Constructs an InputDragSplitter with no event targets.
   */
  public InputDragSplitter()
  {}

  /**
    Adds event target <target> to the list of event targets
    to receive events.
    @param target Event target to be added.
    @return Returns true if the event target list actually
    changed.
   */
  public boolean addEventTarget(InputDragTarget target)
  {
    return _eventTargets.add(target);
  }

  /**
    Removes event target <target> from the event target list.
    @param target Event target to be removed.
    @return Returns true if the event target list actually
    changed.
   */
  public boolean removeEventTarget(InputDragTarget target)
  {
    return _eventTargets.remove(target);
  }

  /**
    Removes all event targets from the event target list.
   */
  public void clearEventTargets()
  {
    _eventTargets.clear();
  }

  /**
    Gets an iterator for the event target list.
    @return Event target list iterator.
   */
  public Iterator getEventTargets()
  {
    return _eventTargets.iterator();
  }

  // InputDragTarget implementation

  public void startInputDrag(Canvas3D source, Vector2d pos)
  {
    if (_lockout)
    {
      return;
    }
    _lockout = true;

    if (Debug.getEnabled())
    {
      Debug.println("InputDragSplitter",
                    "SPLIT:InputDragSplitter:startInputDrag:" +
                    " source=" + source +
                    " pos=" + pos);
    }

    Iterator targetI = getEventTargets();
    while (targetI.hasNext())
    {
      InputDragTarget target =
          (InputDragTarget)targetI.next();

      target.startInputDrag(source, pos);
    }

    _lockout = false;
  }

  public void doInputDrag(Canvas3D source, Vector2d pos)
  {
    if (_lockout)
    {
      return;
    }
    _lockout = true;

    if (Debug.getEnabled())
    {
      Debug.println("InputDragSplitter",
                    "SPLIT:InputDragSplitter:doInputDrag:" +
                    " source=" + source +
                    " pos=" + pos);
    }

    Iterator targetI = getEventTargets();
    while (targetI.hasNext())
    {
      InputDragTarget target =
          (InputDragTarget)targetI.next();

      target.doInputDrag(source, pos);
    }

    _lockout = false;
  }

  public void stopInputDrag(Canvas3D source, Vector2d pos)
  {
    if (_lockout)
    {
      return;
    }
    _lockout = true;

    if (Debug.getEnabled())
    {
      Debug.println("InputDragSplitter",
                    "SPLIT:InputDragSplitter:stopInputDrag:" +
                    " source=" + source +
                    " pos=" + pos);
    }

    Iterator targetI = getEventTargets();
    while (targetI.hasNext())
    {
      InputDragTarget target =
          (InputDragTarget)targetI.next();

      target.stopInputDrag(source, pos);
    }

    _lockout = false;
  }

  // personal body ============================================

  /** If true input events are ignored. */
  private boolean _lockout = false;

  /** List of event targets. */
  private ArrayList _eventTargets = new ArrayList();

}
