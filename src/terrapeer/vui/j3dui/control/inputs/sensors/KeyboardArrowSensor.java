package terrapeer.vui.j3dui.control.inputs.sensors;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.inputs.*;

/**
An input sensor that monitors a display for arrow key activity,
which are interpreted as a relative drag (i.e. always starts at
(0,0)).  Events are generated when an arrow key goes down, up,
or repeats.  A single key press generates a delta of +/-1.  Key
repeats accumulate simulating a drag in a single direction.
<P>
Unlike the pure AWT version this sensor often sees spurious
key release events, which translate into spurious drag stops and
starts in the middle of an arrow drag.

@deprecated Replaced by AwtKeyboardDragSensor for naming
consistency and to conform with new pattern for AWT-based event
sensors.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class KeyboardArrowSensor extends InputSensor {
	
	// public interface =========================================

	/**
	Constructs a KeyboardArrowSensor for the specified display.
	@param target Event target.  Never null.
	@param display Source display.  If null events from all
	displays will be reported.
	@param host Group node to host this Behavior.  Null if none,
	but this sensor must be added to the scene graph to work.
	*/
	public KeyboardArrowSensor(InputDragTarget target,
	 Canvas3D display, Group host) {
	 
		super(display, host);
		_awtSensor = new AwtKeyboardArrowSensor(target, display);		
	}

	/**
	Gets the event target.
	@return Reference to the event target.
	*/
	public InputDragTarget getEventTarget() {
		return _awtSensor.getEventTarget();
	}
			
	// personal body ============================================
	
	/** Event target. Never null. */
	private AwtKeyboardArrowSensor _awtSensor;

	// InputSensor implementation
	
	protected WakeupCondition buildWakeup() {
		return new WakeupOr(new WakeupCriterion[] {
			new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED),
			new WakeupOnAWTEvent(KeyEvent.KEY_RELEASED),
		});
	}
	
	protected void keyPressed(KeyEvent event) {
		if(Debug.getEnabled()) super.keyPressed(event);
		_awtSensor._keyListener.keyPressed(event);
	}
	
	protected void keyReleased(KeyEvent event) {
		if(Debug.getEnabled()) super.keyReleased(event);
		_awtSensor._keyListener.keyReleased(event);
	}

}