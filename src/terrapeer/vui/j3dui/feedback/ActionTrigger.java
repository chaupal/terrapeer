package terrapeer.vui.j3dui.feedback;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;
import terrapeer.vui.j3dui.control.inputs.*;
import terrapeer.vui.j3dui.control.mappers.*;

/**
A feedback trigger that monitors various input sensor events
and generates a feedback action state event whenever the action
state changes relative to none or more pick target objects. The
feedback state is affected by input events as follows:
<UL>
<LI> Input move: Depending on mouse-over sets action to NORMAL
or OVER.  Position is ignored.
<LI> Input drag: If mouse-over is true sets action to DRAG or
DROP.  Position is ignored.
<LI> Input button: Depending on mouse-over sets action to DOWN
OVER, or NORMAL.
<LI> Input pause: If mouse pauses sets action to PAUSE.
<LI> Input cancel: If drag is active sets action to CANCEL.
<LI> Object pick: Determines mouse-over status.
<LI> Feedback: Relayed to feedback output but does not affect
the internal state except for mouse-over.
</UL> 
A collateral "over enable" event target is notified indicating
the mouse-over state.  A collateral "allow pause" event target
is notified indicating when mouse-pausing should be allowed
(i.e. a pause will occur only while it is allowed and only after
the pause conditions have been met). 
<P>
The feedback state will only be correct under all conditions
if all input interfaces are connected and synchronized with
the target object manipulation and the picking is continuous.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class ActionTrigger extends FeedbackTrigger
 implements InputMoveTarget, InputDragTarget, InputButtonTarget,
 InputPauseTarget, InputCancelTarget, ObjectPickTarget {

	// public interface =========================================

	/**
	Constructs an ActionTrigger with the specified event
	target.  Use setPickTargets() to set the pick targets.
	@param eventTarget Event target.  Never null.  Except for
	FeedbackTarget, input events only generate calls to
	setFeedbackAction().
	*/
	public ActionTrigger(FeedbackTarget eventTarget) {
		super(eventTarget);
	}

	/**
	Constructs an ActionTrigger with the specified event
	target and a single over pick target.
	@param eventTarget Event target.  Never null.  Except for
	FeedbackTarget, input events only generate calls to
	setFeedbackAction().
	@param pickTarget Single over target.  Never null. 
	*/
	public ActionTrigger(FeedbackTarget eventTarget,
	 Object pickTarget) {
	
		this(eventTarget);
		
		if(pickTarget==null) throw new
		 IllegalArgumentException("<pickTarget> is null.");
		
		ArrayList pickTargets = new ArrayList();
		pickTargets.add(pickTarget);
		setPickTargets(pickTargets);
	}

	/**
	Sets the list of mouse-over pick targets.
	@param pickTargets List of pick targets that signify
	"mouse-over".  Never null. 
	@return Reference to pick target list. 
	*/
	public AbstractList setPickTargets(
	 AbstractList pickTargets) {
	
		if(pickTargets==null) throw new
		 IllegalArgumentException("<pickTargets> is null.");
		_pickTargets = pickTargets;
		
		return _pickTargets;
	}

	/**
	Gets the list of mouse-over pick targets.
	@return Reference to the list of pick targets of type Object. 
	*/
	public AbstractList getPickTargets() {
		return _pickTargets;
	}

	/**
	Gets the collateral target "over target" event source,
	which is notified when the mouse-over state changes.
	@return Reference to the splitter.
	*/
	public EnableSplitter getOverTargetSource() {
		// lazy build
		if(_overSplitter==null)
			_overSplitter = new EnableSplitter();
		
		return _overSplitter;
	}

	/**
	Gets the collateral target "allow pause" event source,
	which is notified when the allow-pause state changes.
	@return Reference to the splitter.
	*/
	public EnableSplitter getAllowPauseSource() {
		// lazy build
		if(_pauseSplitter==null)
			_pauseSplitter = new EnableSplitter();
		
		return _pauseSplitter;
	}
		
	// InputMoveTarget implementation
	
	public void setInputMove(Canvas3D source, Vector2d pos) {
		if(getFeedbackAction() != Feedback.ACTION_DRAG) {
			if(_rawOver) {
				setFeedbackAction(Feedback.ACTION_OVER);
			} else {
				setFeedbackAction(Feedback.ACTION_NORMAL);
			}
		}
			
if(Debug.getEnabled()){		
Debug.println("ActionTrigger",
"ACTION:ActionTrigger:setInputMove:" +
" isOver=" + _rawOver +
" action=" + Feedback.toActionString(getFeedbackAction()));}

	}

	// InputDragTarget implementation
	
	public void startInputDrag(Canvas3D source, Vector2d pos) {
		if(!_rawOver) return;
		setFeedbackAction(Feedback.ACTION_DRAG);
			
if(Debug.getEnabled()){		
Debug.println("ActionTrigger",
"ACTION:ActionTrigger:startInputDrag:" +
" action=" + Feedback.toActionString(getFeedbackAction()));}

	}
	
	public void doInputDrag(Canvas3D source, Vector2d pos) {
		// do nothing
	}
	
	public void stopInputDrag(Canvas3D source, Vector2d pos) {
		if(getFeedbackAction() == Feedback.ACTION_DRAG) {
			setFeedbackAction(Feedback.ACTION_DROP);
		}
			
if(Debug.getEnabled()){		
Debug.println("ActionTrigger",
"ACTION:ActionTrigger:stopInputDrag:" +
" action=" + Feedback.toActionString(getFeedbackAction()));}

	}

	// InputButtonTarget implementation
	
	public void setInputButton(int button) {
		if(getFeedbackAction()==Feedback.ACTION_DOWN &&
		 button==Input.BUTTON_NONE) {
			if(_rawOver)
				setFeedbackAction(Feedback.ACTION_OVER);
			else
				setFeedbackAction(Feedback.ACTION_NORMAL);
		} else if(getFeedbackAction()!=Feedback.ACTION_DRAG &&
		 button!=Input.BUTTON_NONE) {
			if(_rawOver) {
				setFeedbackAction(Feedback.ACTION_DOWN);
			} else {
				setFeedbackAction(Feedback.ACTION_NORMAL);
			}
		}
		
if(Debug.getEnabled()){		
Debug.println("ActionTrigger",
"ACTION:ActionTrigger:setInputButton:" +
" button=" + button +
" action=" + Feedback.toActionString(getFeedbackAction()));}

	}
	
	public void setInputClick(int click) {
		// do nothing
	}

	// InputPauseTarget implementation
	
	public void setInputPause(boolean pause) {
		// update pause feedback
		if(pause && getFeedbackAction()==Feedback.ACTION_OVER) {
			setFeedbackAction(Feedback.ACTION_PAUSE);
		} else
		if(!pause && getFeedbackAction()==Feedback.ACTION_PAUSE) {
			if(_rawOver) {
				setFeedbackAction(Feedback.ACTION_OVER);
			} else {
				setFeedbackAction(Feedback.ACTION_NORMAL);
			}
		}
			
if(Debug.getEnabled()){		
Debug.println("ActionTrigger",
"ACTION:ActionTrigger:setInputPause:" +
" action=" + Feedback.toActionString(getFeedbackAction()));}

	}

	// InputCancelTarget implementation
	
	public void setInputCancel() {
		if(getFeedbackAction()==Feedback.ACTION_DRAG) {
			setFeedbackAction(Feedback.ACTION_CANCEL);
		} else
		if(getFeedbackAction()==Feedback.ACTION_PAUSE) {
			if(_rawOver) {
				setFeedbackAction(Feedback.ACTION_OVER);
			} else {
				setFeedbackAction(Feedback.ACTION_NORMAL);
			}
		}
			
if(Debug.getEnabled()){		
Debug.println("ActionTrigger",
"ACTION:ActionTrigger:setInputCancel:" +
" action=" + Feedback.toActionString(getFeedbackAction()));}

	}

	// ObjectPickTarget implementation
	
	public void setObjectPick(int index, Object target) {
		if(_pickTargets == null) return;
		if(_rawOver) {
			// old over
			if(_pickTargets.indexOf(target) == -1) {
				// new not over, set normal
				_rawOver = false;
			
if(Debug.getEnabled()){		
Debug.println("ActionTrigger.rawOver",
"OVER:ActionTrigger:setObjectPick:" +
" rawOver=" + _rawOver);}

				setFeedbackAction(Feedback.ACTION_NORMAL);
			} else {
				// still over
				return;
			}
		} else if(_pickTargets.indexOf(target) != -1) {
			// new over, set over and enable pausing
			_rawOver = true;
			
if(Debug.getEnabled()){		
Debug.println("ActionTrigger.rawOver",
"OVER:ActionTrigger:setObjectPick:" +
" rawOver=" + _rawOver);}

			setFeedbackAction(Feedback.ACTION_OVER);
		
			if(_pauseSplitter!=null) {
				_pauseSplitter.setEnable(true);
			}
		} else {
			// still not over
			return;
		}
			
if(Debug.getEnabled()){		
Debug.println("ActionTrigger",
"ACTION:ActionTrigger:setObjectPick:" +
" over=" + _rawOver +
" index=" + index +
" target=" + target +
" action=" + Feedback.toActionString(getFeedbackAction()));}

	}

	// FeedbackTarget implementation

	public void setFeedbackAction(int action) {
		// update over target, ignore repeats
		boolean isOver = ((action & Feedback.ACTION_IS_OVER)!=0);
		
//		if(_overSplitter!=null &&
//		 getFeedbackAction()!=action && isOver!=_isOver) {
// Because over detection is slow and stutters due to GC, and
// because other triggers may be setting the over target state,
// need to update over enable after any state change. 
		if(_overSplitter!=null) {
		 
			_isOver = isOver;
			
if(Debug.getEnabled()){		
Debug.println("ActionTrigger.isOver",
"OVER:ActionTrigger:setObjectPick:" +
" isOver=" + _isOver +
" action=" + Feedback.toActionString(action));}

			_overSplitter.setEnable(_isOver);
		}

		// if anything but over, disable pausing		
		if(_pauseSplitter!=null && action!=Feedback.ACTION_OVER) {
			_pauseSplitter.setEnable(false);
		}
		
		// update feedback target
		super.setFeedbackAction(action);
	}
			
	// personal body ============================================
	
	/** "over target" event splitter.  Null until lazy build. */
	private EnableSplitter _overSplitter = null;
	
	/** "allow pause" event splitter.  Null until lazy build. */
	private EnableSplitter _pauseSplitter = null;
	
	/** Over pick targets.  Null if none. */
	private AbstractList _pickTargets = null;
	
	/** True if the mouse is currently over the target. */
	private boolean _rawOver = false;
	
	/** The state of the over enable target. */
	private boolean _isOver = false;
		
}