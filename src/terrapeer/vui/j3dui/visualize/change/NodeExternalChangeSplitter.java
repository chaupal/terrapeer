package terrapeer.vui.j3dui.visualize.change;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
Splits (multicasts) the source event to multiple event targets.

@author Jon Barrilleaux,
copyright (c) 2000 Jon Barrilleaux,
All Rights Reserved.
*/

public class NodeExternalChangeSplitter implements
 NodeExternalChangeTarget {
	
	// public interface =========================================

	/**
	Constructs an NodeExternalChangeSplitter with no event
	targets.
	*/
	public NodeExternalChangeSplitter() {}

	/**
	Adds an event target to the list of event targets
	to receive events.
	@param target Event target to be added.
	@return Returns true if the event target list actually
	changed.
	*/
	public boolean addEventTarget(
	 NodeExternalChangeTarget target) {
		return _eventTargets.add(target);
	}

	/**
	Removes an event target from the event target list.
	@param target Event target to be removed.
	@return Returns true if the event target list actually
	changed.
	*/
	public boolean removeEventTarget(
	 NodeExternalChangeTarget target) {
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

	// NodeExternalChangeTarget implementation
	
	public void setNodeExternal(Node source, Point3d pos,
	 Transform3D xform) {
		if(_lockout) return;
		_lockout = true;
		
if(Debug.getEnabled()){
Debug.println("NodeExternalChangeSplitter",
"SPLIT:NodeExternalChangeSplitter:setViewExternal:" +
" source=" + source +
" pos=" + pos + 
" xform=" + xform);}

		Iterator targetI = getEventTargets();
		while(targetI.hasNext()) {
			NodeExternalChangeTarget target =
			 (NodeExternalChangeTarget)targetI.next();
		
			target.setNodeExternal(source, pos, xform);
		}
		
		_lockout = false;
	}
				
	// personal body ============================================
	
	/** If true input events are ignored. */
	private boolean _lockout = false;
	
	/** List of event targets. */
	private ArrayList _eventTargets = new ArrayList();	

}