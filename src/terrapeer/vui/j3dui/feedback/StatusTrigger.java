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
and generates a feedback status state event whenever the status
state changes.  The feedback state is affected by input events
as follows:
<UL>
<LI> Enable: Sets status to NORMAL or DISABLE.
<LI> Feedback: Relayed to feedback output but does not affect
the internal state.
</UL>
The feedback state will only be correct under all conditions
if all input interfaces are connected and synchronized with
the target object manipulation.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class StatusTrigger extends FeedbackTrigger
 implements EnableTarget {

	// public interface =========================================

	/**
	Constructs a StatusTrigger with the specified event
	target.
	@param eventTarget Event target.  Never null.  Except for
	FeedbackTarget, input events only generate calls to
	setFeedbackStatus().
	*/
	public StatusTrigger(FeedbackTarget eventTarget) {
		super(eventTarget);
	}
	
	// EnableTarget implementation

	public void setEnable(boolean enable) {
		if(enable)
			setFeedbackStatus(Feedback.STATUS_NORMAL);
		else
			setFeedbackStatus(Feedback.STATUS_DISABLE);
			
if(Debug.getEnabled()){		
Debug.println("StatusTrigger",
"STATUS:StatusTrigger:setEnable:" +
" enable=" + enable +
" status=" + Feedback.toStatusString(getFeedbackStatus()));}

	}
			
	// personal body ============================================
		
}