package terrapeer.vui.j3dui.control.inputs.sensors;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.inputs.*;

/**
An AWT input sensor that monitors a display for modifier key
activity.  Use setModifiers() to specify which modifier states
are recognized.  Events are generated only when modifier state
changes, and not for key repeats.
<P>
Typically, because of the inconsistent handling of modifier key
activity by the AWT during drags and the nature of modifier keys
in general this sensor should monitor all 3D displays used in
the application.

@author Jon Barrilleaux,
copyright (c) 1999-2000 Jon Barrilleaux,
All Rights Reserved.
*/

public class AwtKeyboardModifierSensor {
	
	// public interface =========================================

	/**
	Constructs an AwtKeyboardModifierSensor.
	*/
	public AwtKeyboardModifierSensor() {}

	/**
	Constructs an AwtKeyboardModifierSensor with an event source
	and target.
	@param source Event source.  Null if none.
	@param target Event target.  Null if none.
	*/
	public AwtKeyboardModifierSensor(Canvas3D source,
	 InputModifierTarget target) {

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
Debug.println(this, "AwtKeyboardModifierSensor",
"AwtKeyboardModifierSensor:addEventSource:" +
" source=" + source);}
		
		source.addKeyListener(_listener);
		source.addMouseListener(_focuser);
	}
	
	/**
	Gets the event output splitter.  Use it to add and
	remove event targets.
	@return Reference to the event out splitter.  Never null.
	*/
	public InputModifierSplitter getEventOut() {
		return _eventOut;
	}

	/**
	Sets which modifier states will activate this sensor.
	This sensor is activated if any of the specified modifier
	states change.
	@param modifiers Modifier flags (Input.MODIFIER_???).
	Defaults to Input.MODIFIER_ALL (which does not include
	Escape).  If MODIFIER_NONE or MODIFIER_IGNORE no events
	will be generated.
	*/
	public void setModifiers(int modifiers) {
	   	_modifiers = modifiers;
	}
			
	// personal body ============================================
	
	/** Event out splitter. Never null. */
	private InputModifierSplitter _eventOut =
	 new InputModifierSplitter();
	
	/** Modifier flags (MODIFIER_???) triggering this sensor. */
	private int _modifiers = Input.MODIFIER_ALL;
	
	/** Last modifier choice for repeat rejection. */
	private int _lastModifier = Input.MODIFIER_NONE;

	/**
	Key event listener proxy for this sensor.  Must be
	personal to enforce correct event source type.
	*/
	protected KeyListener _listener = new KeyListener() {
		public void keyPressed(KeyEvent event) {
			int modifier = Input.whichModifier(event);
		
if(Debug.getEnabled()){
Debug.println(this, "AwtKeyboardModifierSensor.listener",
"AwtKeyboardModifierSensor:keyPressed:" +
" modifier=" + Input.toModifierString(modifier) +
" modifiers=" + Input.toModifierString(_modifiers));}
		
			// reject outcasts
			if((_modifiers==Input.MODIFIER_IGNORE) ||
			 (modifier & _modifiers)==0) return;
			
			setInputModifier(modifier);
		}
		
		public void keyReleased(KeyEvent event) {
			int modifier = Input.whichModifier(event);
		
if(Debug.getEnabled()){
Debug.println(this, "AwtKeyboardModifierSensor.listener",
"AwtKeyboardModifierSensor:keyReleased:" +
" modifier=" + Input.toModifierString(modifier) +
" modifiers=" + Input.toModifierString(_modifiers));}
		
			// reject outcasts
			if(_modifiers==Input.MODIFIER_IGNORE) return;
			modifier &= _modifiers;
			
			setInputModifier(modifier);
		}
	
		public void keyTyped(KeyEvent event) {}
	};

	/**
	Listens for mouse entry into a source display, and gets
	it focus for keyboard events.
	*/
	protected MouseListener _focuser = new MouseAdapter() {
		public void mouseEntered(MouseEvent event) {
		
if(Debug.getEnabled()){
Debug.println(this, "AwtKeyboardModifierSensor.listener",
"AwtKeyboardModifierSensor:mouseEntered:" +
" event=" + event);}
		
			// get focus
			((Component)event.getSource()).requestFocus();
		}
	};
		
	/**
	Call to send modifier state change.
	*/
	protected void setInputModifier(int modifier) {
		
		// reject repeats
		if(_lastModifier==modifier) return;
		_lastModifier = modifier;
		
if(Debug.getEnabled()){
Debug.println(this, "AwtKeyboardModifierSensor",
"AwtKeyboardModifierSensor:setInputModifier:" +
" modifier=" + Input.toModifierString(modifier));}
		
		_eventOut.setInputModifier(modifier);
	}

}