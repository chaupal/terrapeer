package terrapeer.vui.j3dui.control.mappers;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;

/**
Maps object pick event inputs into enable event outputs that
indicate to the event target when the pick cursor is over its
corresponding pick target.  An event target receives an event
only when the over state of its corrsponding pick target
changes.
<P>
The "sense" of all enable outputs is "true", meaning that
the value "true" is output when the condition is met.  To be
sure all output targets are synced use initEventTragets().
<P>
The entries in the this mapper's target list and those in the
pick source generating input events to this mapper must match
otherwise the results are undefined.  The input pick event must
contain a valid pick index (pick object is ignored), otherwise
the results are undefined.  

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class OverEnableMapper implements ObjectPickTarget {
	
	// public interface =========================================

	/**
	Constructs an OverEnableMapper with no event targets.  Use
	setEventTargets() and setEventTarget???() to set event
	targets.
	*/
	public OverEnableMapper() {}

	/**
	Sets a reference to the list of event targets.  The target
	in the list corresponding to the pick index will be enabled.
	@param targets List of event targets of type EnableTarget.
	Null if none. 
	*/
	public void setEventTargets(AbstractList targets) {
		_eventTargets = targets;
		if(_eventTargets==null) return;

		// test target type
		Iterator iter = _eventTargets.iterator();
		while(iter.hasNext()) {
			Object target = iter.next();
			
			if(!(target instanceof EnableTarget)) throw new
			 IllegalArgumentException(
			 "<targets> must be type EnableTarget.");
		}
		
	}

	/**
	Sets the event target that will be enabled when "no target"
	is picked.
	@param target Event target.  Null if none.
	@return Reference to target.
	*/
	public EnableTarget setEventTargetNone(EnableTarget target) {
		_eventTargetNone = target;
		return target;
	}

	/**
	Sets the event target that will be enabled when "any target"
	is picked.
	@param target Event target.  Null if none.
	@return Reference to target.
	*/
	public EnableTarget setEventTargetAny(EnableTarget target) {
		_eventTargetAny = target;
		return target;
	}

	/**
	Clears all event targets, including the ones for
	"pick none" and "pick any".
	*/
	public void clearEventTargets() {
		if(_eventTargets!=null) _eventTargets.clear();
		_eventTargetNone = null;
		_eventTargetAny = null;
	}

	/**
	Initializes all event targets as if the specified target had
	been picked.
	@param index Index of the "picked" target.  <0 if none.
	*/
	public void initEventTargets(int index) {
		if(_eventTargetNone!=null) {
			if(index < 0)
				_eventTargetNone.setEnable(true);
			else
				_eventTargetNone.setEnable(false);
		}
		
		if(_eventTargetAny!=null) {
			if(index >= 0)
				_eventTargetAny.setEnable(true);
			else
				_eventTargetAny.setEnable(false);
		}
		
		if(_eventTargets!=null) {
			Iterator iter = _eventTargets.iterator();
			int iterI = 0;
			while(iter.hasNext()) {
				EnableTarget target = (EnableTarget)iter.next();
				if(target!=null) {
					if(iterI == index)
						target.setEnable(true);
					else
						target.setEnable(false);
				}
				
				iterI++;
			}
		}
	}

	// ObjectPickTarget implementation
	
	public void setObjectPick(int index, Object target) {
	
if(Debug.getEnabled()){
Debug.println("OverEnableMapper",
"OVER:OverEnableMapper:setObjectPick:" +
" oldIndex=" + _overIndex + " newIndex=" + index +
" target=" + target);}

		EnableTarget eventTarget = null;

		// tell old event targets
		if(_overIndex==-1) {
			if(_eventTargetNone!=null)
				_eventTargetNone.setEnable(false);
		} else {
			if(_eventTargets!=null) {
				eventTarget = (EnableTarget)_eventTargets.get(
				 _overIndex);
				if(eventTarget!=null)
					eventTarget.setEnable(false);
			}
			if(_eventTargetAny!=null)
				_eventTargetAny.setEnable(false);
		}

		// tell new targets
		_overIndex = index;
		
		if(_overIndex==-1) {
			if(_eventTargetNone!=null)
				_eventTargetNone.setEnable(true);
		} else {
			if(_eventTargets!=null) {
				eventTarget = (EnableTarget)_eventTargets.get(
				 _overIndex);
				if(eventTarget!=null)
					eventTarget.setEnable(true);
			}
			if(_eventTargetAny!=null)
				_eventTargetAny.setEnable(true);
		}
	}
			
	// personal body ============================================
	
	/** Index of the pick target the cursor was last over. */
	private int _overIndex = -1;
	
	/** Event target for "pick none". Null if none. */
	private EnableTarget _eventTargetNone = null;
	
	/** Event target for "pick any". Null if none. */
	private EnableTarget _eventTargetAny = null;
	
	/** Event targets matching pick targets. Null if none. */
	private AbstractList _eventTargets = null;
	
}