package terrapeer.vui.j3dui.control.actuators;

import javax.vecmath.*;
import java.util.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;

/**
Splits (multicasts) the source event to multiple event targets.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class ActuationSplitter
 implements ActuationTarget, EnableTarget {
	
	// public interface =========================================

	/**
	Constructs an ActuationSplitter with no event targets.
	*/
	public ActuationSplitter() {}

	/**
	Adds event target <target> to the list of event targets
	to receive events.
	@param target Event target to be added.
	@return Returns true if the event target list actually
	changed.
	*/
	public boolean addEventTarget(ActuationTarget target) {
		return _eventTargets.add(target);
	}

	/**
	Removes event target <target> from the event target list.
	@param target Event target to be removed.
	@return Returns true if the event target list actually
	changed.
	*/
	public boolean removeEventTarget(ActuationTarget target) {
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

	/**
	Gets the enable state.
	@return True if enabled.
	*/
	public boolean getEnable() {
		return _enable;
	}
	
	// EnableTarget implementation

	public void setEnable(boolean enable) {
		_enable = enable;
		
if(Debug.getEnabled()){
Debug.println(this, "ActuationSplitter.enable",
"ENABLE:ActuationSplitter.setEnable:" + 
" enable=" + _enable);}

	}
	
	// ActuationTarget implementation
	
	public void initActuation(Tuple4d value) {
		
if(Debug.getEnabled()){
Debug.println(this, "ActuationSplitter",
"SPLIT:ActuationSplitter.initActuation:" + 
" enable=" + _enable + 
" lockout=" + _lockout +
" value=" + value);}
		
		if(!getEnable()) return;

		if(_lockout) return;
		_lockout = true;

		Iterator targetI = getEventTargets();
		while(targetI.hasNext()) {
			ActuationTarget target =
			 (ActuationTarget)targetI.next();
		
			target.initActuation(value);
		}

		_lockout = false;
	}
	
	public void updateActuation(Tuple4d value) {
		
if(Debug.getEnabled()){
Debug.println(this, "ActuationSplitter",
"SPLIT:ActuationSplitter.updateActuation:" + 
" enable=" + _enable + 
" lockout=" + _lockout +
" value=" + value);}

		if(!getEnable()) return;
		
		if(_lockout) return;
		_lockout = true;
		
		Iterator targetI = getEventTargets();
		while(targetI.hasNext()) {
			ActuationTarget target =
			 (ActuationTarget)targetI.next();
		
			target.updateActuation(value);
		}

		_lockout = false;
	}
	
	public void syncActuation() {
		
if(Debug.getEnabled()){
Debug.println(this, "ActuationSplitter",
"SPLIT:ActuationSplitter:syncActuation:" + 
" enable=" + _enable + 
" lockout=" + _lockout);}

		if(!getEnable()) return;
		
		if(_lockout) return;
		_lockout = true;
		
		Iterator targetI = getEventTargets();
		while(targetI.hasNext()) {
			ActuationTarget target =
			 (ActuationTarget)targetI.next();
		
			target.syncActuation();
		}

		_lockout = false;
	}
			
	// personal body ============================================
	
	/** If true input events are ignored. */
	private boolean _lockout = false;
	
	/** List of event targets. */
	private ArrayList _eventTargets = new ArrayList();	
	
	/** True if actuator enabled. */
	private boolean _enable = true;

}