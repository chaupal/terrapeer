package terrapeer.vui.j3dui.control.inputs.sensors;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.inputs.*;

/**
An input sensor that monitors a display for mouse moves.
Events are generated only while the mouse is over the display
and no buttons are down.

@author Jon Barrilleaux,
copyright (c) 1999-2000 Jon Barrilleaux,
All Rights Reserved.
*/

public class AwtMouseMoveSensor {
	
	// public interface =========================================

	/**
	Constructs an AwtMouseMoveSensor.
	*/
	public AwtMouseMoveSensor() {}

	/**
	Constructs an AwtMouseMoveSensor with an event source
	and target.
	@param source Event source.  Null if none.
	@param target Event target.  Null if none.
	*/
	public AwtMouseMoveSensor(Canvas3D source,
	 InputMoveTarget target) {

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
Debug.println(this, "AwtMouseMoveSensor",
"AwtMouseMoveSensor:addEventSource:" +
" source=" + source);}
		
		source.addMouseMotionListener(_listener);
	}
	
	/**
	Gets the event output splitter.  Use it to add and
	remove event targets.
	@return Reference to the event out splitter.  Never null.
	*/
	public InputMoveSplitter getEventOut() {
		return _eventOut;
	}
			
	// personal body ============================================
	
	/** Event out splitter. Never null. */
	private InputMoveSplitter _eventOut =
	 new InputMoveSplitter();
	
	/** Last cooked input position in display. */
	private Vector2d _pos = new Vector2d();

	/**
	Mouse motion event listener proxy for this sensor.  Must be
	personal to enforce correct event source type.
	*/
	protected MouseMotionListener _listener =
	 new MouseMotionListener() {
		public void mouseMoved(MouseEvent event) {
			setInputMove((Canvas3D)event.getSource(),
			 event.getX(), event.getY());
		}
		
		public void mouseDragged(MouseEvent event) {}
	};

	/**
	Call when the mouse moves and no buttons are down.
	*/	
	protected void setInputMove(Canvas3D source,
	 int rawX, int rawY) {
	
		Input.cookMouse(rawX, rawY, _pos);
		
if(Debug.getEnabled()){
Debug.println(this, "AwtMouseMoveSensor",
"AwtMouseMoveSensor:setInputMove:" +
" cookedPos=" + _pos +
" source=" + source);}
		
		_eventOut.setInputMove(source, _pos);
	}

}