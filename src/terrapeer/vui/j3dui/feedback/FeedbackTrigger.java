package terrapeer.vui.j3dui.feedback;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;
import terrapeer.vui.j3dui.control.inputs.*;
import terrapeer.vui.j3dui.control.mappers.*;

/**
A feedback trigger base class that tracks the feedback state and
selectively passes input and internal feedback events to the
event target.  Repeat action and status events are rejected.
Repeat select events are allowed.  The trigger conditions
apply to their respective event types independantly (i.e. like
an output event filter for each feedback event type).
<P>
The FeedbackTarget event interface is included as input in
feedback triggers so that the target feedback state can be
initialized and so that the trigger can be slaved to another
trigger such as a master feedback or selection manager.  To
prevent looping input events are ignored during output event
generation. 

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class FeedbackTrigger implements FeedbackTarget {

	// public interface =========================================

	/**
	Constructs a FeedbackTrigger with the specified event
	target.
	@param eventTarget Event target.  Never null.
	*/
	public FeedbackTrigger(FeedbackTarget eventTarget) {
		if(eventTarget==null) throw new
		 IllegalArgumentException("<eventTarget> is null.");
		_eventTarget = eventTarget;
	}

	/**
	Gets the event target.
	@return Reference to the event target.
	*/
	public FeedbackTarget getEventTarget() {
		return _eventTarget;
	}

	/**
	Initializes this object and the event target as if the
	feedback state had changed as specified.
	@param status Status state (Feedback.STATUS_???).
	@param select Select state (Feedback.SELECT_???).
	@param action Action state (Feedback.ACTION_???).
	*/
	public void initEventTarget(int status, int select,
	 int action) {
	
		setFeedbackStatus(status);
		setFeedbackSelect(select);
		setFeedbackAction(action);
	}

	/**
	Sets the trigger condition status state flags.  The
	default is Feedback.STATUS_ALL.  Only these events
	will be output.
	@param flags Status state flags (Feedback.STATUS_???).
	*/
	public void setStatusFlags(int flags) {
		_statusFlags = flags;
	}

	/**
	Sets the trigger condition select state flags.  The
	default is Feedback.SELECT_ALL.  Only these events
	will be output.
	@param flags Select state flags (Feedback.SELECT_???).
	*/
	public void setSelectFlags(int flags) {
		_selectFlags = flags;
	}

	/**
	Sets the trigger condition action state flags.  The
	default is Feedback.ACTION_ALL.  Only these events
	will be output.
	@param flags Action state flags (Feedback.ACTION_???).
	*/
	public void setActionFlags(int flags) {
		_actionFlags = flags;
	}
	
	/**
	Gets the feedback status state.
	@return Feedback state.
	*/
	public int getFeedbackStatus() {
		return _status;
	}

	/**
	Gets the feedback select state.
	@return Feedback state.
	*/
	public int getFeedbackSelect() {
		return _select;
	}

	/**
	Gets the feedback action state.
	@return Feedback state.
	*/
	public int getFeedbackAction() {
		return _action;
	}
	
	// FeedbackTarget implementation

	public void setFeedbackStatus(int status) {
	
if(Debug.getEnabled()){		
Debug.println(this, "FeedbackTrigger",
"FEEDBACK:FeedbackTrigger:setFeedbackStatus:" + 
" repeat=" + (status==_status) + 
" status=" + Feedback.toStatusString(status&_statusFlags));}

		// reject repeats and outcasts
//		if(status==_status) return;
		if((status & _statusFlags)==0) return;

		// update state
		_status = status;	 
	
if(Debug.getEnabled()){		
Debug.println(this, "FeedbackTrigger",
"  status=" + Feedback.toStatusString(_status));}

		_eventTarget.setFeedbackStatus(_status);
	}

	public void setFeedbackSelect(int select) {
	
if(Debug.getEnabled()){		
Debug.println(this, "FeedbackTrigger",
"FEEDBACK:FeedbackTrigger:setFeedbackSelect:" + 
" repeat=" + (select==_select) + 
" select=" + Feedback.toSelectString(select&_selectFlags));}

		// reject outcasts
		if((select & _selectFlags)==0) return;

		// update state
		_select = select;	 
	
if(Debug.getEnabled()){		
Debug.println(this, "FeedbackTrigger",
"  select=" + Feedback.toSelectString(_select));}

		_eventTarget.setFeedbackSelect(_select);
	}

	public void setFeedbackAction(int action) {
	
if(Debug.getEnabled()){		
Debug.println(this, "FeedbackTrigger",
"FEEDBACK:FeedbackTrigger:setFeedbackAction:" + 
" repeat=" + (action==_action) + 
" action=" + Feedback.toActionString(action&_actionFlags));}

		// reject repeats and outcasts
//		if(action==_action) return;
		if((action & _actionFlags)==0) return;

		// update state
		_action = action;
	
if(Debug.getEnabled()){		
Debug.println(this, "FeedbackTrigger",
"  action=" + Feedback.toActionString(_action));}

		_eventTarget.setFeedbackAction(_action);
	}
			
	// personal body ============================================
	
	/** Event target.  Never null. */
	private FeedbackTarget _eventTarget;
	
	/** Current status state (Feedback.STATUS_???). */
	private int _status = Feedback.STATUS_NORMAL;
	
	/** Current select state (Feedback.SELECT_???). */
	private int _select = Feedback.SELECT_NORMAL;
	
	/** Current action state (Feedback.ACTION_???). */
	private int _action = Feedback.ACTION_NORMAL;
	
	/** Status flags. */
	private int _statusFlags = Feedback.STATUS_ALL;
	
	/** Select flags. */
	private int _selectFlags = Feedback.SELECT_ALL;
	
	/** Action flags. */
	private int _actionFlags = Feedback.ACTION_ALL;
		
}