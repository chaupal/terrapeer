package terrapeer.vui.j3dui.control.inputs.sensors;

import javax.media.j3d.*;
import javax.vecmath.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.inputs.*;

/**
An input sensor that monitors a display for modifier key
activity.  Use setModifiers() to specify which modifier states
are recognized, defaulting to Input.MODIFIER_ALL.  Events are
generated only when modifier state changes, and not for key
repeats.
<P>
Typically, because of the inconsistent handling of modifier key
activity by the AWT during drags and the nature of modifier keys
in general the display should be specified as null so that all
displays are monitored.

@deprecated Replaced by AwtKeyboardModifierSensor to conform with
new pattern for AWT-based event sensors.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class KeyboardModifierSensor extends InputSensor {
	
	// public interface =========================================

	/**
	Constructs a MouseDragSensor for the specified display.
	@param target Event target.  Never null.
	@param display Source display.  If null events from all
	displays will be reported.
	@param host Group node to host this Behavior.  Null if none,
	but this sensor must be added to the scene graph to work.
	*/
	public KeyboardModifierSensor(InputModifierTarget target,
	 Canvas3D display, Group host) {
	 
		super(display, host);
		
		if(target==null) throw new
		 IllegalArgumentException("<target> is null.");
		_eventTarget = target;
	}

	/**
	Gets the event target.
	@return Reference to the event target.
	*/
	public InputModifierTarget getEventTarget() {
		return _eventTarget;
	}

	/**
	Sets which modifier states will activate this sensor.
	This sensor is activated if any of the specified modifier
	states change.
	@param modifiers Modifier flags (Input.MODIFIER_???).  If
	MODIFIER_NONE or MODIFIER_IGNORE no events will be
	generated.
	*/
	public void setModifiers(int modifiers) {
	   	_modifiers = modifiers;
	}

	// InputSensor implementation
	
	public void keyPressed(KeyEvent event) {
		super.keyPressed(event);
		
		int modifier = Input.whichModifier(event);
		
		// reject outcasts
		if((_modifiers==Input.MODIFIER_IGNORE) ||
		 (modifier & _modifiers)==0) return;
		
		setInputModifier(modifier);
	}
	
	public void keyReleased(KeyEvent event) {
		if(Debug.getEnabled()) super.keyReleased(event);

		int modifier = Input.whichModifier(event);
		
		// reject outcasts
		if(_modifiers==Input.MODIFIER_IGNORE) return;
		modifier &= _modifiers;
		
		setInputModifier(modifier);
	}
			
	// personal body ============================================
	
	/** Event target. Never null. */
	private InputModifierTarget _eventTarget;
	
	/** Modifier flags (MODIFIER_???) triggering this sensor. */
	private int _modifiers = Input.MODIFIER_ALL;
	
	/** Last modifier choice for repeat rejection. */
	private int _lastModifier = Input.MODIFIER_NONE;

	// sensor event handling
		
	/**
	Call to change modifier state.
	*/
	protected void setInputModifier(int modifier) {
		
		// reject repeats
		if(_lastModifier==modifier) return;
		_lastModifier = modifier;
		
if(Debug.getEnabled()){
Debug.println("KeyboardModifierSensor",
"MODIFIER:KeyboardModifierSensor:setInputModifier:" +
" modifier=" + Input.toModifierString(modifier) +
" this=" + this);}
		
		_eventTarget.setInputModifier(modifier);
	}

	// InputSensor implementation
	
	protected WakeupCondition buildWakeup() {
		return new WakeupOr(new WakeupCriterion[] {
			new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED),
			new WakeupOnAWTEvent(KeyEvent.KEY_RELEASED),
		});
	}

}