package terrapeer.vui.j3dui.feedback;

import java.util.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;

/**
A manager that oversees action interaction for a group of
targets.  Management includes checking for multiple triggers
on the same target and notification to collateral event
targets of any action events.
<P>
Target interaction is monitored directly on the multishape
target, which means that interaction can originate from any
source, even a different manager.  See FeedbackManager for
details about adding a target for management.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class ActionManager extends FeedbackManager {

	// public interface =========================================

	/**
	Constructs an ActionManager with no event targets.  Use
	addTarget() to add event targets.
	*/
	public ActionManager() {
		super(Feedback.TYPE_ACTION);
	}

	/**
	Gets the collateral target "over target" event source,
	which is notified when the mouse-over state changes on
	any target trigger.
	<P>
	Note that trigger state changes are detected on the
	target itself and are not a result of any external
	feedback input to the target.
	@return Reference to the splitter.
	*/
	public EnableSplitter getOverTargetSource() {
		// lazy build
		if(_overSplitter==null) {
			_overSplitter = new EnableSplitter();

			// connect target triggers			
			FeedbackMinion minionI;
			Iterator iter = getMinions().iterator();
			
			while(iter.hasNext()) {
				minionI = (FeedbackMinion)iter.next();
				
				((ActionTrigger)minionI.getTrigger()).
				 getOverTargetSource().addEventTarget(
				 _overSplitter);
			}
		}
		
		return _overSplitter;
	}

	/**
	Gets the collateral target "allow pause" event source,
	which is notified when the allow-pause state changes on
	any trigger.
	<P>
	Note that trigger state changes are detected on the
	target itself and are not a result of any external
	feedback input to the target.
	@return Reference to the splitter.
	*/
	public EnableSplitter getAllowPauseSource() {
		// lazy build
		if(_pauseSplitter==null) {
			_pauseSplitter = new EnableSplitter();

			// connect target triggers			
			FeedbackMinion minionI;
			Iterator iter = getMinions().iterator();
			
			while(iter.hasNext()) {
				minionI = (FeedbackMinion)iter.next();
				
				((ActionTrigger)minionI.getTrigger()).
				 getOverTargetSource().addEventTarget(
				 _pauseSplitter);
			}
		}
		
		return _pauseSplitter;
	}
			
	// personal body ============================================
	
	/** "over target" event splitter.  Null until lazy build. */
	private EnableSplitter _overSplitter = null;
	
	/** "allow pause" event splitter.  Null until lazy build. */
	private EnableSplitter _pauseSplitter = null;

	// FeedbackManager implementation
	
	protected void manageFeedback(FeedbackMinion minion,
	 int state, boolean isTarget) {
	
if(Debug.getEnabled()){
Debug.println("ActionManager",
"MANAGE:ActionManager:manageFeedback:" +
" isTarget=" + isTarget +
" state=" + Feedback.toActionString(state) +
" minion=" + minion);}

		// if trigger event, relay to target
		if(!isTarget) {
			minion.updateTargetState(state);
		}
	}		
	
}