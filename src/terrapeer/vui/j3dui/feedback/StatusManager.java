package terrapeer.vui.j3dui.feedback;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;
import terrapeer.vui.j3dui.control.inputs.*;
import terrapeer.vui.j3dui.control.mappers.*;

/**
A manager that oversees status interaction for a group of
targets.  Management includes checking for multiple triggers
on the same target and notification to collateral event
targets of any status events.
<P>
Target interaction is monitored directly on the multishape
target, which means that interaction can originate from any
source, even a different manager.  See FeedbackManager for
details about adding a target for management.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class StatusManager extends FeedbackManager {

	// public interface =========================================

	/**
	Constructs a StatusManager with no event targets.  Use
	addTarget() to add event targets.
	*/
	public StatusManager() {
		super(Feedback.TYPE_STATUS);
	}
			
	// personal body ============================================

	// FeedbackManager implementation

	protected void manageFeedback(FeedbackMinion minion,
	 int state, boolean isTarget) {
	
if(Debug.getEnabled()){
Debug.println("StatusManager",
"MANAGE:StatusManager:manageFeedback:" +
" isTarget=" + isTarget +
" state=" + Feedback.toStatusString(state) +
" minion=" + minion);}

		// if trigger event, relay to target
		if(!isTarget) {
			minion.updateTargetState(state);
		}
	}		
	
}