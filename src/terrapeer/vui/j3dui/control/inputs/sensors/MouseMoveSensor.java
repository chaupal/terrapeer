package terrapeer.vui.j3dui.control.inputs.sensors;

import javax.media.j3d.*;
import javax.vecmath.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.inputs.*;

/**
An input sensor that monitors a display for mouse moves.
Events are generated only while the mouse is over the display
and no buttons are down.

@deprecated Replaced by AwtMouseMoveSensor to conform with
new pattern for AWT-based event sensors.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class MouseMoveSensor extends InputSensor {
	
	// public interface =========================================

	/**
	Constructs a MouseMoveSensor for the specified display.
	@param target Event target.  Never null.
	@param display Source display.  If null events from all
	displays will be reported.
	@param host Group node to host this Behavior.  Null if none,
	but this sensor must be added to the scene graph to work.
	*/
	public MouseMoveSensor(InputMoveTarget target,
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
	public InputMoveTarget getEventTarget() {
		return _eventTarget;
	}
			
	// personal body ============================================
	
	/** Event target. Never null. */
	private InputMoveTarget _eventTarget;
	
	/** Last cooked input position in display. */
	private Vector2d _pos = new Vector2d();

	/**
	Call when the mouse moves and no buttons are down.
	*/	
	protected void setInputMove(Canvas3D source,
	 int rawX, int rawY) {
	
		Input.cookMouse(rawX, rawY, _pos);
		
if(Debug.getEnabled()){
Debug.println("MouseMoveSensor",
"MOVE:MouseMoveSensor:setInputMove:" +
" cookedPos=" + _pos +
" source=" + source);}
		
		_eventTarget.setInputMove(source, _pos);
	}

	// InputSensor implementation
	
	protected WakeupCondition buildWakeup() {
		return new WakeupOr(new WakeupCriterion[] {
			new WakeupOnAWTEvent(MouseEvent.MOUSE_MOVED),
		});
	}
	
	protected void mouseMoved(MouseEvent event) {
		if(Debug.getEnabled()) super.mouseMoved(event);
		
		setInputMove((Canvas3D)event.getSource(),
		 event.getX(), event.getY());
	}

}