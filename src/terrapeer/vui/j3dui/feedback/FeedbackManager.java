package terrapeer.vui.j3dui.feedback;

import java.util.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
An abstract base class for managers that manage feedback
interaction for a group of targets through feedback triggers.
The manager and its minions are assumed to be of the same
feedback type (status, select, action).  After all targets have
been attached use initTargets() to initialize the feedback
targets.  During input event processing all input events are
blocked to avoid loops.
<P>
Target interaction is monitored directly on the multishape
targets, which means that interaction can originate from any
source, even a different manager.  The state of the trigger is
not intended to be manipulated directly.  It is intended to be
a true indication of user input relative to the target.
<P>
To add a target use getTargetTrigger() to see if a trigger
already exists.  If not, use addTarget() to add a multishape
target for management.  The returned feedback trigger must
be supplied with event sources such that it monitors the target
for user interaction.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public abstract class FeedbackManager {

	// public interface =========================================

	/**
	Adds a new target for feedback management through a minion.
	The returned trigger must be configured with event sources
	to monitor the target for user interaction.  If the target
	is already being managed then a new trigger should not be
	added otherwise race conditions can occur.  Use
	getTargetTrigger() to check for an existing trigger before
	adding a new target.
	<P>
	For action managers the returned action trigger is
	constructed with the feedback target as the single mouse-over
	pick target.  If a different pick target is required use the
	trigger's setPickTargets().
	@param target The feedback target to be managed.  Never
	null.
	@return Feedback trigger constructed by the managed minion.
	Never null.
	*/
	public FeedbackTrigger addTarget(MultiShape target) {
			
		FeedbackMinion minion = new FeedbackMinion(_type,
		 this, target);
		_minions.add(minion);
			
if(Debug.getEnabled()){
Debug.println("FeedbackManager",
"FeedbackManager:addTarget:" +
" target=" + target +
" trigger=" + minion.getTrigger());}

		return minion.getTrigger();
	}

	/**
	Gets the (first) trigger that is managing the specified
	target.  If the target is not being managed returns null.
	@param target The target being managed.  If null ignored.
	@return Existing trigger managing the target.
	Null if none or if the target is not being managed.
	*/
	public FeedbackTrigger getTargetTrigger(MultiShape target) {
		FeedbackTrigger trigger = null;
		
		FeedbackMinion minionI;
		Iterator iter = getMinions().iterator();
		
		while(iter.hasNext()) {
			minionI = (FeedbackMinion)iter.next();
			if(minionI.getTarget() == target) {
				trigger = minionI.getTrigger();
				break;
			}
		}

		return trigger;
	}

	/**
	Gets a list of references to minions that satisfy the
	specified target and trigger state flags.  At least one
	flag must be satisfied in a minion's target and trigger
	to be included in the group.
	@param targetFlags Target state flags of the same event
	type as this manager.  Use Feedback.???_ALL for "don't care".
	@param triggerFlags Trigger state flags of the same event
	type as this manager.  Use Feedback.???_ALL for "don't care".
	@param copy Container for the copied return list, containing
	FeedbackMinion objects.
	@return Reference to copy.
	*/
	public AbstractList getGroup(int targetFlags,
	 int triggerFlags, AbstractList copy) {
	 
		copy.clear();
		
		FeedbackMinion minionI;
		Iterator iter = getMinions().iterator();
		
		while(iter.hasNext()) {
			minionI = (FeedbackMinion)iter.next();
			if(((minionI.getTargetState() & targetFlags)!=0) &&
			 ((minionI.getTriggerState() & triggerFlags)!=0)) {
				copy.add(minionI);
			}
		}

		return copy;
	}
	
	/**
	Gets this manager's feedback type, which is also that of its
	minions.
	@return Manager feedback type (Feedback.TYPE_???).
	*/
	public int getType() {
		return _type;
	}

	/**
	Initializes the managed targets.  If the specified target is
	null all targets are initialized to the specified state.
	If not null only the specified target is set.
	@param target The target to be initialized.  Null if none.
	Ignored if not a target.
	@param state Initial feedback state.  Assumed to be of the
	correct feedback type for the managed minion (STATUS_???,
	SELECT_???, ACTION_???).
	*/
	public void initTargets(MultiShape target, int state) {
	 
		FeedbackMinion minionI;

		if(target == null) {
			// init all targets	
			Iterator iter = getMinions().iterator();
			while(iter.hasNext()) {
				minionI = (FeedbackMinion)iter.next();
				minionI.setTargetState(state);
			}
		} else {
			// init single target, ignore if not a target
			Iterator iter = getMinions().iterator();
			while(iter.hasNext()) {
				minionI = (FeedbackMinion)iter.next();
				if(minionI.getTarget() == target)
					minionI.setTargetState(state);
			}
		}
	}

	/**
	Gets the collateral target "target any" event source, which
	is notified when the feedback state changes on any managed
	target (whether due to external target feedback or internal
	trigger feedback).
	@return Reference to the splitter.
	*/
	public FeedbackSplitter getTargetAnySource() {
		// lazy build
		if(_targetAnySplitter==null)
			_targetAnySplitter = new FeedbackSplitter();
		
		return _targetAnySplitter;
	}

	/**
	Gets the collateral target "trigger any" event source, which
	is notified when the feedback state changes on any managed
	trigger.
	@return Reference to the splitter.
	*/
	public FeedbackSplitter getTriggerAnySource() {
		// lazy build
		if(_triggerAnySplitter==null)
			_triggerAnySplitter = new FeedbackSplitter();
		
		return _triggerAnySplitter;
	}
			
	// personal body ============================================

	/** Feedback type (Feedback.TYPE_???). */
	private int _type;
	
	/** List of managed minions. */
	private ArrayList _minions = new ArrayList();
	
	/** "target any" event splitter.  Null until lazy build. */
	private FeedbackSplitter _targetAnySplitter = null;
	
	/** "trigger any" event splitter.  Null until lazy build. */
	private FeedbackSplitter _triggerAnySplitter = null;

	/**
	Constructs a FeedbackManager of the specified event type.
	Use addTarget() to add feedback targets for management.
	@param type Type of feedback event handled by the minion
	(FeedbackMinion.Feedback.TYPE_???).
	*/
	protected FeedbackManager(int type) {
		switch(type) {			
			case Feedback.TYPE_STATUS:
			case Feedback.TYPE_SELECT:
			case Feedback.TYPE_ACTION:
				_type = type;
				break;
			default:
				throw new IllegalArgumentException(
				 "<type> is unknown.");
		}
	}

	/**
	Gets the feedback minion list.
	@return Reference to the list of FeedbackMinion.
	*/
	protected ArrayList getMinions() {
		return _minions;
	}

	/**
	Calls manageFeedback() and then performs collateral target
	notification.  Called when a feedback event occurs on
	any managed minion. 
	@param minion Minion whose state changed.  Never null.
	@param state New feedback state of the same event type as
	the minion.
	@param isTarget True if the event originated from a target.
	False if it originated from a trigger (i.e. user input).
	*/
	protected void manageMinion(FeedbackMinion minion,
	 int state, boolean isTarget) {
	
if(Debug.getEnabled()){
Debug.println("FeedbackManager",
"MANAGE:FeedbackManager:manageMinion:" +
" isTarget=" + isTarget +
" state=" + state +
" minion=" + minion);}

		// tell subclass to do management 		
		manageFeedback(minion, state, isTarget);
		
		// notify collateral targets (after management done)
		if(isTarget && _targetAnySplitter!=null) {
			switch(_type) {			
				case Feedback.TYPE_STATUS:
					_targetAnySplitter.setFeedbackStatus(state);
					break;
				case Feedback.TYPE_SELECT:
					_targetAnySplitter.setFeedbackSelect(state);
					break;
				case Feedback.TYPE_ACTION:
					_targetAnySplitter.setFeedbackAction(state);
					break;
			}
		}
		
		if(!isTarget && _triggerAnySplitter!=null) {
			switch(_type) {			
				case Feedback.TYPE_STATUS:
					_triggerAnySplitter.setFeedbackStatus(state);
					break;
				case Feedback.TYPE_SELECT:
					_triggerAnySplitter.setFeedbackSelect(state);
					break;
				case Feedback.TYPE_ACTION:
					_triggerAnySplitter.setFeedbackAction(state);
					break;
			}
		}
	}

	/**
	Override this method to perform feedback management
	including relaying trigger events to the target.  Called
	when a feedback event occurs on any managed target or
	trigger. 
	@param minion Minion whose state changed.  Never null.
	@param state New feedback state of the same event type as
	the minion.
	@param isTarget True if the event originated from a target.
	False if it originated from a trigger (i.e. user input).
	*/
	protected abstract void manageFeedback(FeedbackMinion minion,
	 int state, boolean isTarget);
	 		
}