package terrapeer.vui.j3dui.feedback;

import java.util.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;

/**
A manager that performs feedback on a group of selected
feedback targets by relaying the group feedback state to all
selected event targets (the group).  This manager is in essence
a meta-manager for feedback managers, which perform the actual
feedback management of the targets.
<P>
This manager's feedback event input is internally connected to
all of the "target any" collateral event outputs of the managed
feedback managers.  Presumably all feedback managers are
connected to the same targets.  If the mouse is over any
selected target (the group) then status and action events are
relayed to all targets in the group.
<P>
The input events are used as follows:
<UL>
<LI> Feedback, Status: Indicates when the status state may
have changed on a selected target.  Currently not used in group
management but simply relayed to event targets.
<LI> Feedback, Select: Indicates when the select state may
have changed on a target thereby possibly changing the makeup
of the selection group.
<LI> Feedback, Action: Indicates when the action state may
have changed on a selected target thereby changing the feedback
of the selection group.
</UL>     

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class FeedbackGroupManager extends FeedbackSplitter {

	// public interface =========================================

	/**
	Constructs an FeedbackGroupManager for the specified
	feedback managers.
	@param statusManager The group status manager.
	Never null.
	@param selectManager The group select manager.
	Never null.
	@param actionManager The group action manager.
	Never null.
	*/
	public FeedbackGroupManager(StatusManager statusManager,
	 SelectManager selectManager, ActionManager actionManager) {
	 
		if(statusManager==null) throw new
		 IllegalArgumentException("<statusManager> is null.");
		if(selectManager==null) throw new
		 IllegalArgumentException("<selectManager> is null.");
		if(actionManager==null) throw new
		 IllegalArgumentException("<actionManager> is null.");
		 
		_statusManager = statusManager;
		_selectManager = selectManager;
		_actionManager = actionManager;
		
		// connect managers to this feedback input
		_statusManager.getTargetAnySource().addEventTarget(this);
		_selectManager.getTargetAnySource().addEventTarget(this);
		_actionManager.getTargetAnySource().addEventTarget(this);
	}

	/**
	Gets the collateral target "target any" event source,
	which is notified when the feedback state changes on
	any target.
	<P>
	Note that target state changes are a result of any changes
	to the target state whether internally or externally induced.
	@return Reference to the splitter.
	*/
	public FeedbackSplitter getTargetAnySource() {
		// lazy build
		if(_targetAnySplitter==null) {
			_targetAnySplitter = new FeedbackSplitter();
			
			_statusManager.getTargetAnySource().
			 addEventTarget(_targetAnySplitter);
			_selectManager.getTargetAnySource().
			 addEventTarget(_targetAnySplitter);
			_actionManager.getTargetAnySource().
			 addEventTarget(_targetAnySplitter);
		}
		
		return _targetAnySplitter;
	}

	/**
	Gets the collateral target "trigger any" event source,
	which is notified when the feedback state changes on
	any trigger.
	<P>
	Note that trigger state changes are detected on the
	target itself and are not a result of any external
	feedback input to the target.
	@return Reference to the splitter.
	*/
	public FeedbackSplitter getTriggerAnySource() {
		// lazy build
		if(_triggerAnySplitter==null) {
			_triggerAnySplitter = new FeedbackSplitter();
			
			_statusManager.getTriggerAnySource().
			 addEventTarget(_triggerAnySplitter);
			_selectManager.getTriggerAnySource().
			 addEventTarget(_triggerAnySplitter);
			_actionManager.getTriggerAnySource().
			 addEventTarget(_triggerAnySplitter);
		}
		
		return _triggerAnySplitter;
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
			
			_actionManager.getOverTargetSource().
			 addEventTarget(_overSplitter);
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
			
			_actionManager.getAllowPauseSource().
			 addEventTarget(_pauseSplitter);
		}
		
		return _pauseSplitter;
	}

	/**
	Gets the managed feedback status manager.
	@return Feedback manager.
	*/
	public StatusManager getStatusManager() {
		return _statusManager;
	}

	/**
	Gets the managed feedback select manager.
	@return Feedback manager.
	*/
	public SelectManager getSelectManager() {
		return _selectManager;
	}

	/**
	Gets the managed feedback action manager.
	@return Feedback manager.
	*/
	public ActionManager getActionManager() {
		return _actionManager;
	}

	/**
	Initializes the managed targets.  If the specified target is
	null all targets are initialized to the specified states.
	If not null only the specified target is set.
	@param target The target to be initialized.  Null if none.
	Ignored if not a target.
	@param status Initial status feedback state (STATUS_???),
	@param select Initial select feedback state (SELECT_???),
	@param action Initial action feedback state (ACTION_???),
	*/
	public void initTargets(MultiShape target, int status,
	 int select, int action) {

		_statusManager.initTargets(target, status);
		_selectManager.initTargets(target, select);
		_actionManager.initTargets(target, action);
	}
	
	// FeedbackTarget implementation

	public void setFeedbackStatus(int status) {
		if(_lockout) return;
		_lockout = true;
		
if(Debug.getEnabled()){		
Debug.println("FeedbackGroupManager",
"GROUP:FeedbackGroupManager:setFeedbackStatus:" +
" (bgn)" +
" status=" + Feedback.toStatusString(status));}

		// tell the world
		super.setFeedbackStatus(status);
				
if(Debug.getEnabled()){		
Debug.println("FeedbackGroupManager",
"GROUP:FeedbackGroupManager:setFeedbackStatus:" +
" (end)");}
		
		_lockout = false;
	}

	public void setFeedbackSelect(int select) {
		if(_lockout) return;
		_lockout = true;
		
if(Debug.getEnabled()){		
Debug.println("FeedbackGroupManager",
"GROUP:FeedbackGroupManager:setFeedbackSelect:" +
" (bgn)" +
" select=" + Feedback.toSelectString(select));}

		// restore action for all action targets 
		Iterator actionIter;
		FeedbackMinion actionI;
		Object overTarget;
		
		actionIter = _actionManager.getMinions().iterator();
		while(actionIter.hasNext()) {
			actionI = (FeedbackMinion)actionIter.next();
			actionI.updateTargetState(actionI.getTriggerState());
		}

		// get new select group
		_selectManager.getGroup(Feedback.SELECT_IS_SELECT,
		 Feedback.SELECT_ALL, _selectGroup);

		// tell the world
		super.setFeedbackSelect(select);
				
if(Debug.getEnabled()){		
Debug.println("FeedbackGroupManager",
"GROUP:FeedbackGroupManager:setFeedbackSelect:" +
" (end)");}
		
		_lockout = false;
	}

	public void setFeedbackAction(int action) {
		if(_lockout) return;
		_lockout = true;
		
if(Debug.getEnabled()){		
Debug.println("FeedbackGroupManager",
"GROUP:FeedbackGroupManager:setFeedbackAction:" +
" (bgn)" +
" groupOver=" + _groupOver +
" action=" + Feedback.toActionString(action));}

		Iterator selectIter;
		FeedbackMinion selectI;

		// check for group over
		_groupOver = false;
		
		Iterator actionIter;
		FeedbackMinion actionI;
		Object overTarget;
		
		actionIter = _actionManager.getMinions().iterator();
		done: while(actionIter.hasNext()) {
			actionI = (FeedbackMinion)actionIter.next();

		// tell the world
		super.setFeedbackAction(action);
			
if(Debug.getEnabled()){		
Debug.println("FeedbackGroupManager.verbose",
"VERBOSE:FeedbackGroupManager:setFeedbackAction:" +
" actionTrigger=" + Feedback.toActionString(actionI.getTriggerState()));}

			// skip if trigger not over
			if((actionI.getTriggerState() &
			 Feedback.ACTION_IS_OVER)==0) continue;
			
			overTarget = actionI.getTarget();

			// check if proxy target matches a select target			
			selectIter = _selectGroup.iterator();
			while(selectIter.hasNext()) {
				selectI = (FeedbackMinion)selectIter.next();
			
if(Debug.getEnabled()){		
Debug.println("FeedbackGroupManager.verbose",
"VERBOSE:FeedbackGroupManager:setFeedbackAction:" +
" overTarget=" + overTarget +
" selectTarget=" + selectI.getTarget());}

				if(overTarget == selectI.getTarget()) {
					_groupOver = true;
					break done;
				}
			}
		}
			
if(Debug.getEnabled()){		
Debug.println("FeedbackGroupManager.verbose",
"VERBOSE:FeedbackGroupManager:setFeedbackAction:" +
" groupOver=" + _groupOver);}

		// determine group action
		int groupAction;
		
		if(_groupOver) {
			// over group
			if((action&Feedback.ACTION_IS_OVER)!=0) {
				// action is an over type, use it
				groupAction = action;
			} else {
				// action not an over type, force over
				groupAction = Feedback.ACTION_OVER;
			}
		} else {
			// not over group, force normal
			groupAction = Feedback.ACTION_NORMAL;
		}
			
if(Debug.getEnabled()){		
Debug.println("FeedbackGroupManager.verbose",
"VERBOSE:FeedbackGroupManager:setFeedbackAction:" +
" groupAction=" + Feedback.toActionString(groupAction));}

		// update group action
		selectIter = _selectGroup.iterator();
		while(selectIter.hasNext()) {
			selectI = (FeedbackMinion)selectIter.next();
			selectI.getTarget().setFeedbackAction(
			 groupAction);
		}
			
if(Debug.getEnabled()){		
Debug.println("FeedbackGroupManager",
"GROUP:FeedbackGroupManager:setFeedbackAction:" +
" (end)");}
		
		_lockout = false;
	}
			
	// personal body ============================================
	
	/** Managed status manager.  Never null. */
	private StatusManager _statusManager;
	
	/** Managed select manager.  Never null. */
	private SelectManager _selectManager;
	
	/** Managed action manager.  Never null. */
	private ActionManager _actionManager;
	
	/** "target any" event splitter.  Null until lazy build. */
	private FeedbackSplitter _targetAnySplitter = null;
	
	/** "trigger any" event splitter.  Null until lazy build. */
	private FeedbackSplitter _triggerAnySplitter = null;
	
	/** "over target" event splitter.  Null until lazy build. */
	private EnableSplitter _overSplitter = null;
	
	/** "allow pause" event splitter.  Null until lazy build. */
	private EnableSplitter _pauseSplitter = null;
	
	/** True if over select group. */
	private boolean _groupOver = false;
	
	/** Minion group with selected targets. */
	private final ArrayList _selectGroup = new ArrayList();
	
	/** If true input events are ignored. */
	private boolean _lockout = false;
	
}