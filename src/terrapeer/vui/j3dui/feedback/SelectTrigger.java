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
and generates a feedback select state event whenever the select
state changes.  The feedback state is affected by input events
as follows:
<UL>
<LI> Input button: If mouse-over is true the click count
determines the selection state (SINGLE, DOUBLE).
<LI> Input cancel: If mouse-over is true cancels any current
selection (NORMAL).
<LI> Enable: Indicates when mouse-over occurs.
<LI> Feedback: Relayed to feedback output but does not affect
the internal state, such as mouse-over.
</UL> 
The feedback state will only be correct under all conditions
if all input interfaces are connected and synchronized with
the target object manipulation.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class SelectTrigger extends FeedbackTrigger
 implements EnableTarget, InputButtonTarget, InputCancelTarget {

	// public interface =========================================

	/**
	Constructs a SelectTrigger with the specified event
	target.
	@param eventTarget Event target.  Never null.  Except for
	FeedbackTarget, input events only generate calls to
	setFeedbackSelect().
	*/
	public SelectTrigger(FeedbackTarget eventTarget) {
		super(eventTarget);
	}

	// EnableTarget implementation

	/**
	If true the mouse is over the selection target.
	*/	
	public void setEnable(boolean enable) {
		_isOver = enable;
			
if(Debug.getEnabled()){		
Debug.println("SelectTrigger",
"SELECT:SelectTrigger:setEnable:" +
" isOver=" + _isOver);}

	}

	// InputButtonTarget implementation
	
	public void setInputButton(int button) {
		// do nothing
	}
	
	/**
	If over, select is determined according to the click state
	(NORMAL, SINGLE, DOUBLE, etc.).
	*/	
	public void setInputClick(int click) {
		if(_isOver) {
			switch(click) {
				case Input.CLICK_NONE:
					setFeedbackSelect(Feedback.SELECT_NORMAL);
					break;
				case Input.CLICK_SINGLE:
					setFeedbackSelect(Feedback.SELECT_SINGLE);
					break;
				case Input.CLICK_DOUBLE:
					setFeedbackSelect(Feedback.SELECT_DOUBLE);
					break;
				case Input.CLICK_TRIPLE:
					setFeedbackSelect(Feedback.SELECT_TRIPLE);
					break;
				case Input.CLICK_MANY:
					setFeedbackSelect(Feedback.SELECT_MANY);
					break;
			}
		}
		
if(Debug.getEnabled()){		
Debug.println("SelectTrigger",
"SELECT:SelectTrigger:setInputClick:" +
" click=" + click +
" isOver=" + _isOver +
" select=" + Feedback.toSelectString(getFeedbackSelect()));}

	}

	// InputCancelTarget implementation

	/**
	If over, select will be made NORMAL.
	*/	
	public void setInputCancel() {
		if(_isOver) {
			setFeedbackSelect(Feedback.SELECT_NORMAL);
		}
			
if(Debug.getEnabled()){		
Debug.println("SelectTrigger",
"SELECT:SelectTrigger:setInputCancel:" +
" isOver=" + _isOver +
" select=" + Feedback.toSelectString(getFeedbackSelect()));}

	}
			
	// personal body ============================================
	
	/** True if the mouse is over the select target. */
	private boolean _isOver = false;
		
}