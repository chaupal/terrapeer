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

public class InputButtonSplitter implements InputButtonTarget {
	
	// public interface =========================================

	/**
	Constructs an InputButtonSplitter with no event targets.
	*/
	public InputButtonSplitter() {}

	/**
	Adds event target <target> to the list of event targets
	to receive events.
	@param target Event target to be added.
	@return Returns true if the event target list actually
	changed.
	*/
	public boolean addEventTarget(InputButtonTarget target) {
		return _eventTargets.add(target);
	}

	/**
	Removes event target <target> from the event target list.
	@param target Event target to be removed.
	@return Returns true if the event target list actually
	changed.
	*/
	public boolean removeEventTarget(InputButtonTarget target) {
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
	
	// InputButtonTarget implementation
	
	public void setInputButton(int button) {
		if(_lockout) return;
		_lockout = true;
		
if(Debug.getEnabled()){
Debug.println("InputButtonSplitter",
"SPLIT:InputButtonSplitter:setInputButton:" +
" button=" + button);}

		Iterator targetI = getEventTargets();
		while(targetI.hasNext()) {
			InputButtonTarget target =
			 (InputButtonTarget)targetI.next();
		
			target.setInputButton(button);
		}
		
		_lockout = false;
	}
	
	public void setInputClick(int click) {
		if(_lockout) return;
		_lockout = true;
		
if(Debug.getEnabled()){
Debug.println("InputButtonSplitter",
"SPLIT:InputButtonSplitter:setInputClick:" +
" click=" + click);}

		Iterator targetI = getEventTargets();
		while(targetI.hasNext()) {
			InputButtonTarget target =
			 (InputButtonTarget)targetI.next();
		
			target.setInputClick(click);
		}
		
		_lockout = false;
	}
				
	// personal body ============================================
	
	/** If true input events are ignored. */
	private boolean _lockout = false;
	
	/** List of event targets. */
	private ArrayList _eventTargets = new ArrayList();	

}