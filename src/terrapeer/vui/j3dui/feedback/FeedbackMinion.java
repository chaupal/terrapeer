package terrapeer.vui.j3dui.feedback;

import java.util.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
A proxy for a feedback target and trigger managed by a feedback
manager.  Also the factory for the trigger.  Use getTrigger() to
retrieve the trigger constructed by the minion.  Through a
minion the manager monitors trigger and target state, but it can
only change the target state.  The manager is responsible for
passing on trigger state to the target.
<P>
The action trigger is constructed with the feedback target as
the single mouse-over pick target.  If a different pick target
is required use the trigger's setPickTargets().
<P>
A minion is constructed with a particular feedback type (status,
select, action).  It is only capable of handling events of that
type.  During input event processing from targets and triggers
all input events are blocked to avoid event loops.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class FeedbackMinion {

	// public interface =========================================
	
	/**
	Gets this minion's feedback type.
	@return Minion feedback type (TYPE_???).
	*/
	public int getType() {
		return _type;
	}
	
	/**
	Gets this minion's feedback target.
	@return Feedback target.
	*/
	public MultiShape getTarget() {
		return _target;
	}
	
	/**
	Gets this minion's feedback trigger.
	@return Feedback trigger.
	*/
	public FeedbackTrigger getTrigger() {
		return _trigger;
	}
	
	/**
	Sets the feedback target state.
	@param state New feedback state of the same type as the
	minion.
	*/
	public void setTargetState(int state) {
		switch(_type) {			
			case Feedback.TYPE_STATUS:
				getTarget().setFeedbackStatus(state);
				break;
			case Feedback.TYPE_SELECT:
				getTarget().setFeedbackSelect(state);
				break;
			case Feedback.TYPE_ACTION:
				getTarget().setFeedbackAction(state);
				break;
		}
	}
	
	/**
	Gets the feedback target state.
	@return Current feedback target state of the same type as
	the minion.
	*/
	public int getTargetState() {
		int state = -1;
		
		switch(_type) {			
			case Feedback.TYPE_STATUS:
				state = getTarget().getFeedbackStatus();
				break;
			case Feedback.TYPE_SELECT:
				state = getTarget().getFeedbackSelect();
				break;
			case Feedback.TYPE_ACTION:
				state = getTarget().getFeedbackAction();
				break;
		}
		
		return state;
	}
	
	/**
	Gets the feedback trigger state.
	@return Current feedback trigger state of the same type as
	the minion.
	*/
	public int getTriggerState() {
		int state = -1;
		
		switch(_type) {			
			case Feedback.TYPE_STATUS:
				state = getTrigger().getFeedbackStatus();
				break;
			case Feedback.TYPE_SELECT:
				state = getTrigger().getFeedbackSelect();
				break;
			case Feedback.TYPE_ACTION:
				state = getTrigger().getFeedbackAction();
				break;
		}
		
		return state;
	}
	
	/**
	Sets the feedback target state but only if the new state
	is different.
	@param state New feedback state of the same type as the
	minion.
	*/
	public void updateTargetState(int state) {
		if(getTargetState()!=state) setTargetState(state);
	}
			
	// personal body ============================================

	/** Feedback type (TYPE_???). */
	private int _type;

	/** Feedback manager.  Never null. */
	private FeedbackManager _manager;
	
	/** Feedback target.  Never null. */
	private MultiShape _target;
	
	/** Feedback trigger.  Never null. */
	private FeedbackTrigger _trigger;

	/** Target proxy.  Never null. */
	private TargetProxy _targetProxy;

	/** Trigger proxy.  Never null. */
	private TriggerProxy _triggerProxy;
		
	/**
	Constructs a FeedbackMinion for the specified event type and
	target.  Use getTrigger() to get the newly constructed
	feedback trigger, which must be connected to event sources
	to monitor the target for event state changes.  The manager
	will be notified when either the target or the trigger state
	changes and is responsible for sending trigger feedback
	events to the target as needed.
	@param type Type of feedback event handled by this minion
	(Feedback.TYPE_???).
	@param manager Feedback manager that owns this minion.
	@param target Feedback target managed by the manager.  Never
	null.
	*/
	protected FeedbackMinion(int type, FeedbackManager manager,
	 MultiShape target) {
	 
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
	 
		if(manager==null) throw new
		 IllegalArgumentException("<manager> is null.");
		_manager = manager;
	 
		if(target==null) throw new
		 IllegalArgumentException("<target> is null.");
		_target = target;
		
		_targetProxy = new TargetProxy();		
		_triggerProxy = new TriggerProxy();
	}
	
	/**
	Proxy for the feedback target.  Listens for feedback events
	from the target that match the minion's type.  During input
	event processing all input events are blocked to avoid loops.
	*/
	private class TargetProxy implements FeedbackTarget {
	
		/** If false input events are ignored. */
		private boolean _inputEnable = true;
		
		/**
		Constructs a TargetProxy and connects itself to the
		target as an event target.
		*/
		private TargetProxy() {
			_target.addEventTarget(this);
		}

		// FeedbackTarget implementation
				
		public void setFeedbackStatus(int status) {
			if(_type != Feedback.TYPE_STATUS) return;
			
			// lockout inputs, update state 
			if(!_inputEnable) return;
			_inputEnable = false;
						
if(Debug.getEnabled()){		
Debug.println("FeedbackMinion",
"STATUS:FeedbackMinion.TargetProxy:setFeedbackStatus:" +
" status=" + Feedback.toStatusString(status) +
" target=" + _target);}

			_manager.manageMinion(FeedbackMinion.this,
			 status, true);
				
			_inputEnable = true;
		}
		
		public void setFeedbackSelect(int select) {
			if(_type != Feedback.TYPE_SELECT) return;
			
			// lockout inputs, update state 
			if(!_inputEnable) return;
			_inputEnable = false;
						
if(Debug.getEnabled()){		
Debug.println("FeedbackMinion",
"SELECT:FeedbackMinion.TargetProxy:setFeedbackSelect:" +
" select=" + Feedback.toSelectString(select) +
" target=" + _target);}

			_manager.manageMinion(FeedbackMinion.this,
			 select, true);
				
			_inputEnable = true;
		}
		
		public void setFeedbackAction(int action) {
			if(_type != Feedback.TYPE_ACTION) return;
			
			// lockout inputs, update state 
			if(!_inputEnable) return;
			_inputEnable = false;
						
if(Debug.getEnabled()){		
Debug.println("FeedbackMinion",
"ACTION:FeedbackMinion.TargetProxy:setFeedbackAction:" +
" action=" + Feedback.toActionString(action) +
" target=" + _target);}

			_manager.manageMinion(FeedbackMinion.this,
			 action, true);

			_inputEnable = true;
		}
	}
	
	/**
	Proxy for the feedback trigger.  Listens for feedback events
	from the trigger that match the minion's type.  During input
	event processing all input events are blocked to avoid loops.
	*/
	private class TriggerProxy implements FeedbackTarget {
	
		/** If true input events are ignored. */
		private boolean _lockout = false;
		
		/**
		Constructs a TriggerProxy and a feedback trigger and
		connects itself to the trigger as an event target.  For
		an action trigger the target is used as the over target.
		*/
		private TriggerProxy() {
			switch(_type) {			
				case Feedback.TYPE_STATUS:
					_trigger = new StatusTrigger(this);
					break;
				case Feedback.TYPE_SELECT:
					_trigger = new SelectTrigger(this);
					break;
				case Feedback.TYPE_ACTION:
					_trigger = new ActionTrigger(this, _target);
					break;
			}
		}

		// FeedbackTarget implementation
				
		public void setFeedbackStatus(int status) {
			if(_lockout) return;
			_lockout = true;
		
			if(_type != Feedback.TYPE_STATUS) return;
						
if(Debug.getEnabled()){		
Debug.println("FeedbackMinion",
"STATUS:FeedbackMinion.TriggerProxy:setFeedbackStatus:" +
" status=" + Feedback.toStatusString(status) +
" trigger=" + _trigger);}

			_manager.manageMinion(FeedbackMinion.this,
			 status, false);
		
			_lockout = false;
		}
		
		public void setFeedbackSelect(int select) {
			if(_lockout) return;
			_lockout = true;
		
			if(_type != Feedback.TYPE_SELECT) return;
						
if(Debug.getEnabled()){		
Debug.println("FeedbackMinion",
"SELECT:FeedbackMinion.TriggerProxy:setFeedbackSelect:" +
" select=" + Feedback.toSelectString(select) +
" trigger=" + _trigger);}

			_manager.manageMinion(FeedbackMinion.this,
			 select, false);
		
			_lockout = false;
		}
		
		public void setFeedbackAction(int action) {
			if(_lockout) return;
			_lockout = true;
		
			if(_type != Feedback.TYPE_ACTION) return;
						
if(Debug.getEnabled()){		
Debug.println("FeedbackMinion",
"ACTION:FeedbackMinion.TriggerProxy:setFeedbackAction:" +
" action=" + Feedback.toActionString(action) +
" trigger=" + _trigger);}

			_manager.manageMinion(FeedbackMinion.this,
			 action, false);
		
			_lockout = false;
		}
	}
	
}