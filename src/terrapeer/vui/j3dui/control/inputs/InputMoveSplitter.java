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

public class InputMoveSplitter implements InputMoveTarget {
	
	// public interface =========================================

	/**
	Constructs an InputMoveSplitter with no event targets.
	*/
	public InputMoveSplitter() {}

	/**
	Adds event target <target> to the list of event targets
	to receive events.
	@param target Event target to be added.
	@return Returns true if the event target list actually
	changed.
	*/
	public boolean addEventTarget(InputMoveTarget target) {
		return _eventTargets.add(target);
	}

	/**
	Removes event target <target> from the event target list.
	@param target Event target to be removed.
	@return Returns true if the event target list actually
	changed.
	*/
	public boolean removeEventTarget(InputMoveTarget target) {
		return _eventTargets.remove(target);
	}

	/**
	Removes all event targets from the event target list.
	*/
	public void clearEventTargets() {
		_eventTargets.clear();
	}

	/**
	Gets an iterator for the event target list.
	@return Event target list iterator.
	*/
	public Iterator getEventTargets() {
		return _eventTargets.iterator();
	}
	
	// InputMoveTarget implementation
	
	public void setInputMove(Canvas3D source, Vector2d pos) {
		if(_lockout) return;
		_lockout = true;
		
if(Debug.getEnabled()){
Debug.println("InputMoveSplitter",
"SPLIT:InputMoveSplitter:setInputMove:" +
" source=" + source +
" pos=" + pos);}

		Iterator targetI = getEventTargets();
		while(targetI.hasNext()) {
			InputMoveTarget target =
			 (InputMoveTarget)targetI.next();
		
			target.setInputMove(source, pos);
		}
		
		_lockout = false;
	}
				
	// personal body ============================================
	
	/** If true input events are ignored. */
	private boolean _lockout = false;
	
	/** List of event targets. */
	private ArrayList _eventTargets = new ArrayList();	

}