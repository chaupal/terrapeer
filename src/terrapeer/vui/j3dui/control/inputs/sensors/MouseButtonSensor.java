package terrapeer.vui.j3dui.control.inputs.sensors;

import javax.media.j3d.*;
import javax.vecmath.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.inputs.*;

/**
An input sensor that monitors a display for mouse button
actions.  Use setButtons() to specify which button states
are recognized, defaulting to Input.BUTTON_ALL.  Events
are generated only while the mouse is over the display and
when the button state changes.
<P>
Note that a click occurs any time a mouse button is released
thus Input.CLICK_NONE should never occur in setInputClick().

@deprecated Replaced by AwtMouseButtonSensor to conform with
new pattern for AWT-based event sensors.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class MouseButtonSensor extends InputSensor {
	
	// public interface =========================================

	/**
	Constructs a MouseButtonSensor for the specified display.
	@param target Event target.  Never null.
	@param display Source display.  If null events from all
	displays will be reported.
	@param host Group node to host this Behavior.  Null if none,
	but this sensor must be added to the scene graph to work.
	*/
	public MouseButtonSensor(InputButtonTarget target,
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
	public InputButtonTarget getEventTarget() {
		return _eventTarget;
	}

	/**
	Sets which button states will activate this sensor.
	This sensor is activated if any of the specified button
	states change.  Note that typically button clicks should
	only be allowed on the primary button.
	@param buttons Button flags (Input.BUTTON_???).  If
	BUTTON_NONE or BUTTON_IGNORE no events will be
	generated.
	*/
	public void setButtons(int buttons) {
	   	_buttons = buttons;
	}
			
	// personal body ============================================
	
	/** Event target. Never null. */
	private InputButtonTarget _eventTarget;
	
	/** Button flags (MODIFIER_???) triggering this sensor. */
	private int _buttons = Input.BUTTON_ALL;
	
	/** Last button choice for repeat rejection. */
	private int _lastButton = Input.BUTTON_NONE;
		
	/**
	Call to change button state.
	*/
	protected void setInputButton(int button) {
		
		// reject repeats
		if(_lastButton==button) return;
		_lastButton = button;
		
if(Debug.getEnabled()){
Debug.println("MouseButtonSensor",
"BUTTON:MouseButtonSensor:setInputButton:" +
" button=" + button);}
		
		_eventTarget.setInputButton(button);
	}
		
	/**
	Call to send click count.
	*/
	protected void setInputClick(int click) {
		
if(Debug.getEnabled()){
Debug.println("MouseButtonSensor",
"BUTTON:MouseButtonSensor:setInputClick:" +
" click=" + click);}
		
		_eventTarget.setInputClick(click);
	}

	// InputSensor implementation
	
	protected WakeupCondition buildWakeup() {
		return new WakeupOr(new WakeupCriterion[] {
			new WakeupOnAWTEvent(MouseEvent.MOUSE_CLICKED),
			new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED),
			new WakeupOnAWTEvent(MouseEvent.MOUSE_RELEASED),
		});
	}
	
	protected void mouseClicked(MouseEvent event) {
		if(Debug.getEnabled()) super.mouseClicked(event);
		
		int button = Input.whichButton(event);
		
		// reject outcasts
		if((_buttons==Input.BUTTON_IGNORE) ||
		 (button & _buttons)==0) return;
		
		setInputClick(Input.whichClick(event));
	}
	
	protected void mousePressed(MouseEvent event) {
		if(Debug.getEnabled()) super.mousePressed(event);
		
		int button = Input.whichButton(event);
		
		// reject outcasts
		if((_buttons==Input.BUTTON_IGNORE) ||
		 (button & _buttons)==0) return;
		
		setInputButton(button);
	}
	
	protected void mouseReleased(MouseEvent event) {
		if(Debug.getEnabled()) super.mouseReleased(event);

		int button = Input.whichButton(event);
		
		// reject outcasts
		if((_buttons==Input.BUTTON_IGNORE) ||
		 (button & _buttons)==0) return;
		
		setInputButton(Input.BUTTON_NONE);
	}

}