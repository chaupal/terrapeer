package terrapeer.vui.j3dui.control.mappers;

import java.util.*;

/**
Splits (multicasts) the source event to multiple event targets.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class ObjectPickSplitter implements ObjectPickTarget {
	
	// public interface =========================================

	/**
	Constructs an ObjectPickSplitter with no event targets.
	*/
	public ObjectPickSplitter() {}

	/**
	Adds event target <target> to the list of event targets
	to receive events.
	@param target Event target to be added.
	@return Returns true if the event target list actually
	changed.
	*/
	public boolean addEventTarget(ObjectPickTarget target) {
		return _eventTargets.add(target);
	}

	/**
	Removes event target <target> from the event target list.
	@param target Event target to be removed.
	@return Returns true if the event target list actually
	changed.
	*/
	public boolean removeEventTarget(ObjectPickTarget target) {
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
	
	// ObjectPickTarget implementation
	
	public void setObjectPick(int index, Object object) {
		if(_lockout) return;
		_lockout = true;
		
		Iterator targetI = getEventTargets();
		while(targetI.hasNext()) {
			ObjectPickTarget target =
			 (ObjectPickTarget)targetI.next();
		
			target.setObjectPick(index, object);
		}
		
		_lockout = false;
	}
			
	// personal body ============================================
	
	/** If true input events are ignored. */
	private boolean _lockout = false;
	
	/** List of event targets. */
	private ArrayList _eventTargets = new ArrayList();	

}