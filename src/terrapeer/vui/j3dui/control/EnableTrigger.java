package terrapeer.vui.j3dui.control;

import terrapeer.vui.j3dui.utils.Debug;

/**
A base class that monitors input events for specified
conditions and notifies an EnableTarget when conditions are
met or not met.
<P>
When the downstream enable chain is done use initEventTarget()
to initialize its state.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class EnableTrigger implements EnableTarget {
	
	// public interface =========================================

	/**
	Constructs a EnableTrigger with event target <target>.
	@param target Event target.  Never null.
	*/
	public EnableTrigger(EnableTarget target) {
		if(target==null) throw new
		 IllegalArgumentException("'target' is null.");
		_eventTarget = target;
	}

	/**
	Gets the event target.
	@return Reference to event target.
	*/
	public EnableTarget getEventTarget() {
		return _eventTarget;
	}

	/**
	Initializes the event target as if the trigger condition
	was satisfied or not.  The output enable value is determined
	according to the "sense" value.  An event is sent regardless
	of setOnStateChange().
	@param conditionMet Specifies whether or not the condition
	is met.
	*/
	public void initEventTarget(boolean conditionMet) {
		boolean onStateChange = _onStateChange;
		_onStateChange = false;
		
		if(conditionMet)
			sendTriggerTrue();
		else
			sendTriggerFalse();
		
		_onStateChange = onStateChange;
	}

	/**
	Sets the sense of the target enable value.  This is the
	enable value (true or false) output when the trigger
	condition is met.  The default is "true".
	@param sense Value output when condition is met.
	*/
	public void setTargetSense(boolean sense) {
		_targetSense = sense;
	}

	/**
	Gets the target enable sense.
	@return Target enable sense.
	*/
	public boolean getTargetSense() {
		return _targetSense;
	}

	/**
	If true, an output event is only sent if the state
	changes.  If false, an output event is sent when an
	input event is received.  The default is "true".
	@param enable Value output when condition is met.
	*/
	public void setOnStateChange(boolean enable) {
		_onStateChange = enable;
	}
	
	// EnableTarget implementation
	
	/**
	Indicates whether or not the trigger condition is met.  The
	output enable value is determined according to the "sense"
	value.  If setOnStateChange() is enabled the output will
	only be sent if it changes the enable state.
	@param enable True if the condition is met.  False if it
	is not.
	*/
	public void setEnable(boolean enable) {
		if(enable)
			sendTriggerTrue();
		else
			sendTriggerFalse();
	}
			
	// personal body ============================================
	
	/** Event target. */
	private EnableTarget _eventTarget;
	
	/** Output value when condition met. */
	private boolean _targetSense = true;
	
	/** If true events are only sent when state changes. */
	private boolean _onStateChange = true;
	
	/** Current enable state. */
	private boolean _state = false;

	/**
	Call this method to send an output event when the trigger
	condition is met.  Sends the output "sense" value.  If
	setOnStateChange() is enabled the output will only be sent
	if it changes the enable state.
	*/
	protected void sendTriggerTrue() {
		
if(Debug.getEnabled()){Debug.println("EnableTrigger.verbose",
"VERBOSE:EnableTrigger:sendTriggerTrue:" +
" onStateChange=" + _onStateChange +
" targetSense=" + _targetSense +
" state=" + _state);}

		if(_onStateChange && _targetSense==_state) return;
		_state = _targetSense;
		
if(Debug.getEnabled()){Debug.println("EnableTrigger",
"TRIGGER:EnableTrigger:sendTriggerTrue:" +
" output=" + _state);}

		_eventTarget.setEnable(_state);
	}

	/**
	Call this method to send an output event when the trigger
	condition is not met.  Send the negation of the output
	"sense" value.  If setOnStateChange() is enabled the output
	will only be sent if it changes the enable state.
	*/
	protected void sendTriggerFalse() {
		
if(Debug.getEnabled()){Debug.println("EnableTrigger.verbose",
"VERBOSE:EnableTrigger:sendTriggerFalse:" +
" onStateChange=" + _onStateChange +
" targetSense=" + _targetSense +
" state=" + _state);}

		if(_onStateChange && (!_targetSense)==_state) return;
		_state = !_targetSense;
		
if(Debug.getEnabled()){Debug.println("EnableTrigger",
"TRIGGER:EnableTrigger:sendTriggerFalse:" +
" output=" + _state);}

		_eventTarget.setEnable(_state);
	}
	
}