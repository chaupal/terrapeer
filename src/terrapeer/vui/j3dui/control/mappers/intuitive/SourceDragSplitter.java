package terrapeer.vui.j3dui.control.mappers.intuitive;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

/**
Splits (multicasts) the source event to multiple event targets.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class SourceDragSplitter implements SourceDragTarget {
	
	// public interface =========================================

	/**
	Constructs an SourceDragSplitter with no event targets.
	*/
	public SourceDragSplitter() {}

	/**
	Adds event target <target> to the list of event targets
	to receive events.
	@param target Event target to be added.
	@return Returns true if the event target list actually
	changed.
	*/
	public boolean addEventTarget(SourceDragTarget target) {
		return _eventTargets.add(target);
	}

	/**
	Removes event target <target> from the event target list.
	@param target Event target to be removed.
	@return Returns true if the event target list actually
	changed.
	*/
	public boolean removeEventTarget(SourceDragTarget target) {
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
	
	// SourceDragTarget implementation
	
	public void startSourceDrag(Node source, Vector3d pos) {
		if(_lockout) return;
		_lockout = true;
		
		Iterator targetI = getEventTargets();
		while(targetI.hasNext()) {
			SourceDragTarget target =
			 (SourceDragTarget)targetI.next();
		
			target.startSourceDrag(source, pos);
		}
		
		_lockout = false;
	}
	
	public void doSourceDrag(Node source, Vector3d pos) {
		if(_lockout) return;
		_lockout = true;
		
		Iterator targetI = getEventTargets();
		while(targetI.hasNext()) {
			SourceDragTarget target =
			 (SourceDragTarget)targetI.next();
		
			target.doSourceDrag(source, pos);
		}
		
		_lockout = false;
	}
	
	public void stopSourceDrag(Node source, Vector3d pos) {
		if(_lockout) return;
		_lockout = true;
		
		Iterator targetI = getEventTargets();
		while(targetI.hasNext()) {
			SourceDragTarget target =
			 (SourceDragTarget)targetI.next();
		
			target.stopSourceDrag(source, pos);
		}
		
		_lockout = false;
	}
			
	// personal body ============================================
	
	/** If true input events are ignored. */
	private boolean _lockout = false;
	
	/** List of event targets. */
	private ArrayList _eventTargets = new ArrayList();	

}