package terrapeer.vui.j3dui.feedback;

import java.util.*;
import javax.media.j3d.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
Splits (multicasts) the source event to multiple event targets.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class FeedbackSplitter implements FeedbackTarget {
	
	// public interface =========================================

	/**
	Constructs an FeedbackSplitter with no event targets.
	*/
	public FeedbackSplitter() {}

	/**
	Adds an event target to the list of event targets
	to receive events.
	@param target Event target to be added.
	@return Returns true if the event target list actually
	changed.
	*/
	public boolean addEventTarget(FeedbackTarget target) {
		return _eventTargets.add(target);
	}

	/**
	Removes event target <target> from the event target list.
	@param target Event target to be removed.
	@return Returns true if the event target list actually
	changed.
	*/
	public boolean removeEventTarget(
	 FeedbackTarget target) {
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
	
	// FeedbackTarget implementation
	
	public void setFeedbackStatus(int status) {
			
if(Debug.getEnabled()){		
Debug.println(this, "FeedbackSplitter",
"SPLIT:FeedbackSplitter:setFeedbackStatus:" + 
" lockout=" + _lockout +
" status=" + Feedback.toStatusString(status));}

		if(_lockout) return;
		_lockout = true;

		Iterator targetI = getEventTargets();
		while(targetI.hasNext()) {
			FeedbackTarget target =
			 (FeedbackTarget)targetI.next();
		
			target.setFeedbackStatus(status);
		}

		_lockout = false;
	}
	
	public void setFeedbackSelect(int select) {
			
if(Debug.getEnabled()){		
Debug.println(this, "FeedbackSplitter",
"SPLIT:FeedbackSplitter:setFeedbackSelect:" +
" lockout=" + _lockout +
" select=" + Feedback.toSelectString(select));}

		if(_lockout) return;
		_lockout = true;

		Iterator targetI = getEventTargets();
		while(targetI.hasNext()) {
			FeedbackTarget target =
			 (FeedbackTarget)targetI.next();
		
			target.setFeedbackSelect(select);
		}

		_lockout = false;
	}
	
	public void setFeedbackAction(int action) {
			
if(Debug.getEnabled()){		
Debug.println(this, "FeedbackSplitter",
"SPLIT:FeedbackSplitter:setFeedbackAction:" +
" lockout=" + _lockout +
" action=" + Feedback.toActionString(action));}

		if(_lockout) return;
		_lockout = true;

		Iterator targetI = getEventTargets();
		while(targetI.hasNext()) {
			FeedbackTarget target =
			 (FeedbackTarget)targetI.next();

			target.setFeedbackAction(action);
		}

		_lockout = false;
	}
			
	// personal body ============================================
	
	/** If true input events are ignored. */
	private boolean _lockout = false;
	
	/** List of event targets. */
	private ArrayList _eventTargets = new ArrayList();	

}