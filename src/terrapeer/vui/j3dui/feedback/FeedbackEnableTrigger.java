package terrapeer.vui.j3dui.feedback;

import javax.media.j3d.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;

/**
An enable trigger that monitors feedback events.  The trigger
condition is met if any of the feedback state flags are true
in all of the feedback state types.  An output event is sent
only if the output state changes.
<P>
When the downstream enable chain is completed use
initEventTarget() to initialize its state.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class FeedbackEnableTrigger extends EnableTrigger
 implements FeedbackTarget {

	// public interface =========================================

	/**
	Constructs a FeedbackEnableTrigger with the specified event
	target.
	@param eventTarget Event target.  Never null.
	*/
	public FeedbackEnableTrigger(EnableTarget eventTarget) {
		super(eventTarget);
	}

	/**
	Sets the trigger condition status state flags.  The
	default is Feedback.STATUS_ALL.
	@param flags Status state flags (Feedback.STATUS_???).
	*/
	public void setStatusFlags(int flags) {
		_statusFlags = flags;
	}

	/**
	Sets the trigger condition select state flags.  The
	default is Feedback.SELECT_ALL.
	@param flags Select state flags (Feedback.SELECT_???).
	*/
	public void setSelectFlags(int flags) {
		_selectFlags = flags;
	}

	/**
	Sets the trigger condition action state flags.  The
	default is Feedback.ACTION_ALL.
	@param flags Action state flags (Feedback.ACTION_???).
	*/
	public void setActionFlags(int flags) {
		_actionFlags = flags;
	}

	/**
	Initializes this object as if the feedback state had
	changed as specified.
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
	
	// FeedbackTarget implementation

	public void setFeedbackStatus(int status) {
		_status = status;
			
if(Debug.getEnabled()){		
Debug.println(this, "FeedbackEnableTrigger",
"ENABLE:FeedbackEnableTrigger:setFeedbackStatus:" +
" status=" + Feedback.toStatusString(_status));}

		updateEventTarget();
	}

	public void setFeedbackSelect(int select) {
		_select = select;
			
if(Debug.getEnabled()){		
Debug.println(this, "FeedbackEnableTrigger",
"ENABLE:FeedbackEnableTrigger:setFeedbackSelect:" +
" select=" + Feedback.toSelectString(_select));}

		updateEventTarget();
	}

	public void setFeedbackAction(int action) {
		_action = action;
			
if(Debug.getEnabled()){		
Debug.println(this, "FeedbackEnableTrigger",
"ENABLE:FeedbackEnableTrigger:setFeedbackAction:" +
" action=" + Feedback.toActionString(_action));}

		updateEventTarget();
	}
			
	// personal body ============================================
	
	/** Status state. */
	private int _status = Feedback.STATUS_NORMAL;
	
	/** Select state. */
	private int _select = Feedback.SELECT_NORMAL;
	
	/** Action state. */
	private int _action = Feedback.ACTION_NORMAL;
	
	/** Status flags. */
	private int _statusFlags = Feedback.STATUS_ALL;
	
	/** Select flags. */
	private int _selectFlags = Feedback.SELECT_ALL;
	
	/** Action flags. */
	private int _actionFlags = Feedback.ACTION_ALL;

	/**
	Checks the trigger conditions and updates the event target
	state accordingly.
	*/
	public void updateEventTarget() {
			
if(Debug.getEnabled()){		
Debug.print(this, "FeedbackEnableTrigger",
"ENABLE:FeedbackEnableTrigger:updateEventTarget:" +
" status=" + Feedback.toStatusString(_status&_statusFlags) +
" select=" + Feedback.toSelectString(_select&_selectFlags) +
" action=" + Feedback.toActionString(_action&_actionFlags));}

		if((_status & _statusFlags) != 0 &&
		 (_select & _selectFlags) != 0 &&
		 (_action & _actionFlags) != 0) {
		  	// conditions met
		  	
if(Debug.getEnabled()){		
Debug.println(this, "FeedbackEnableTrigger",
" triggerTrue=" + getTargetSense());}

			sendTriggerTrue();
		} else {
			// conditions not met
			
if(Debug.getEnabled()){		
Debug.println(this, "FeedbackEnableTrigger",
" triggerFalse=" + !getTargetSense());}

			sendTriggerFalse();
		}
	}
		
}