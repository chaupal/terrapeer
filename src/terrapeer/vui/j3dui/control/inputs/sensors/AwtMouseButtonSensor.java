package terrapeer.vui.j3dui.control.inputs.sensors;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.inputs.*;

/**
An input sensor that monitors a display for mouse button
actions.  Use setButtons() to specify which button states
are recognized.  Events
are generated only while the mouse is over the display and
when the button state changes.
<P>
Note that a click occurs any time a mouse button is released
thus Input.CLICK_NONE should never occur in setInputClick().

@author Jon Barrilleaux,
copyright (c) 1999-2000 Jon Barrilleaux,
All Rights Reserved.
*/

public class AwtMouseButtonSensor {
	
	// public interface =========================================

	/**
	Constructs an AwtMouseButtonSensor.
	*/
	public AwtMouseButtonSensor() {}

	/**
	Constructs an AwtMouseButtonSensor with an event source
	and target.
	@param source Event source.  Null if none.
	@param target Event target.  Null if none.
	*/
	public AwtMouseButtonSensor(Canvas3D source,
	 InputButtonTarget target) {

		this();
				
		if(source!=null) addEventSource(source);
		if(target!=null) getEventOut().addEventTarget(target);
	}

	/**
	Adds a 3D display as an event source for this sensor.
	@param source  Source display.  Never null.
	*/
	public void addEventSource(Canvas3D source) {
		if(source==null) throw new
		 IllegalArgumentException("'source' is null.");
		
if(Debug.getEnabled()){
Debug.println(this, "AwtMouseButtonSensor",
"AwtMouseButtonSensor:addEventSource:" +
" source=" + source);}
		
		source.addMouseListener(_listener);
	}
	
	/**
	Gets the event output splitter.  Use it to add and
	remove event targets.
	@return Reference to the event out splitter.  Never null.
	*/
	public InputButtonSplitter getEventOut() {
		return _eventOut;
	}

	/**
	Sets which button states will activate this sensor.
	This sensor is activated if any of the specified button
	states change.  Note that typically button clicks should
	only be allowed on the primary button.
	@param buttons Button flags (Input.BUTTON_???).  Defaults
	to Input.BUTTON_ALL  If BUTTON_NONE or BUTTON_IGNORE no
	events will be generated.
	*/
	public void setButtons(int buttons) {
	   	_buttons = buttons;
	}
			
	// personal body ============================================
	
	/** Event out splitter. Never null. */
	private InputButtonSplitter _eventOut =
	 new InputButtonSplitter();
	
	/** Button flags (MODIFIER_???) triggering this sensor. */
	private int _buttons = Input.BUTTON_ALL;
	
	/** Last button choice for repeat rejection. */
	private int _lastButton = Input.BUTTON_NONE;

	/**
	Mouse button event listener proxy for this sensor.  Must be
	personal to enforce correct event source type.
	*/
	protected MouseListener _listener = new MouseListener() {
		public void mouseClicked(MouseEvent event) {
			int button = Input.whichButton(event);
			
			// reject outcasts
			if((_buttons==Input.BUTTON_IGNORE) ||
			 (button & _buttons)==0) return;
			
			setInputClick(Input.whichClick(event));
		}
		
		public void mousePressed(MouseEvent event) {
			int button = Input.whichButton(event);
			
			// reject outcasts
			if((_buttons==Input.BUTTON_IGNORE) ||
			 (button & _buttons)==0) return;
			
			setInputButton(button);
		}
		
		public void mouseReleased(MouseEvent event) {
			int button = Input.whichButton(event);
			
			// reject outcasts
			if((_buttons==Input.BUTTON_IGNORE) ||
			 (button & _buttons)==0) return;
			
			setInputButton(Input.BUTTON_NONE);
		}
	
		public void mouseEntered(MouseEvent event) {}
	
		public void mouseExited(MouseEvent event) {}
	};
			
	/**
	Call to send button state change.
	*/
	protected void setInputButton(int button) {
		
		// reject repeats
		if(_lastButton==button) return;
		_lastButton = button;
		
if(Debug.getEnabled()){
Debug.println(this, "AwtMouseButtonSensor",
"AwtMouseButtonSensor:setInputButton:" +
" button=" + button);}
		
		_eventOut.setInputButton(button);
	}
		
	/**
	Call to send click count.
	*/
	protected void setInputClick(int click) {
		
if(Debug.getEnabled()){
Debug.println(this, "AwtMouseButtonSensor",
"AwtMouseButtonSensor:setInputClick:" +
" click=" + click);}
		
		_eventOut.setInputClick(click);
	}

}