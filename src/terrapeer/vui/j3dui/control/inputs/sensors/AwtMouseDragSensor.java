package terrapeer.vui.j3dui.control.inputs.sensors;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.inputs.*;

/**
An input sensor that monitors a display for mouse drags.  Use
setButtons() to specify which mouse buttons can initiate a
drag.  Events are generated when a mouse drag initiates,
terminates, or is active and the mouse moves.
<P>
A drag operation initiates when the mouse button goes down and
either the mouse is moved a minimum deadband distance or the
button is held down for a minimum start time.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class AwtMouseDragSensor {
	
	// public interface =========================================

	/**
	Constructs an AwtMouseDragSensor.
	*/
	public AwtMouseDragSensor() {
		// build drag start timer, set start delay
		_startTimer = new Timer(333, new ActionListener() {
			public void actionPerformed(ActionEvent event) {
 
if(Debug.getEnabled()){		
Debug.println(this, "AwtMouseDragSensor.timer",
"AwtMouseDragSensor:actionPerformed:" +
" startPos=" + _startX + " " + _startY);}

		 		startInputDrag(_startTimerSource,
		 		 _startX, _startY);
			}    
		});
		_startTimer.setRepeats(false);
	}

	/**
	Constructs an AwtMouseDragSensor with an event source
	and target.
	@param source Event source.  Null if none.
	@param target Event target.  Null if none.
	*/
	public AwtMouseDragSensor(Canvas3D source,
	 InputDragTarget target) {

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
Debug.println(this, "AwtMouseDragSensor",
"AwtMouseDragSensor:addEventSource:" +
" source=" + source);}
		
		source.addMouseMotionListener(_motionListener);
		source.addMouseListener(_buttonListener);
	}
	
	/**
	Gets the event output splitter.  Use it to add and
	remove event targets.
	@return Reference to the event out splitter.  Never null.
	*/
	public InputDragSplitter getEventOut() {
		return _eventOut;
	}

	/**
	Sets which mouse buttons will activate this sensor.
	This sensor is activated if any of the specified mouse
	buttons are down.
	@param buttons Button flags (Input.BUTTON_???).  Defaults to
	Input.BUTTON_FIRST  If BUTTON_NONE drag will not activate.
	*/
	public void setButtons(int buttons) {
	   	_buttons = buttons;
	}
			
	// personal body ============================================
	
	/** Event out splitter. Never null. */
	private InputDragSplitter _eventOut =
	 new InputDragSplitter();
	
	/** Button flags (BUTTON_???) triggering this sensor. */
	private int _buttons = Input.BUTTON_FIRST;
	
	/** True if drag is active. */
	private boolean _isDrag = false;
	
	/** Drag start timer. */
	private Timer _startTimer;
	
	/** Source display used when drag start timer fires. */
	private Canvas3D _startTimerSource;
	
	/** Drag start X position for dead band. */
	private int _startX = 0;
	
	/** Drag start Y position for dead band. */
	private int _startY = 0;
	
	/** Current cooked input position in display. */
	private Vector2d _pos = new Vector2d();
	
	/**
	Mouse motion event listener proxy for this sensor.  Must be
	personal to enforce correct event source type.
	*/
	protected MouseMotionListener _motionListener =
	 new MouseMotionListener() {
		public void mouseDragged(MouseEvent event) {
			// check for activation/deactivation, reject repeats
			if(isStartButton(event)) {
				if(!_isDrag && isStartDrag(event)) {
					_startTimer.stop();
					startInputDrag((Canvas3D)event.getSource(),
					 event.getX(), event.getY());
				} else if(_isDrag) {
					doInputDrag((Canvas3D)event.getSource(),
					 event.getX(), event.getY());
				}
			} else {
				_startTimer.stop();
				if(_isDrag) stopInputDrag(
				 (Canvas3D)event.getSource());
			}
		}
		
		public void mouseMoved(MouseEvent event) {}
	};

	/**
	Mouse button event listener proxy for this sensor.  Must be
	personal to enforce correct event source type.
	*/
	protected MouseListener _buttonListener =
	 new MouseListener() {
		public void mousePressed(MouseEvent event) {
			// check for activation/deactivation, reject repeats
			if(isStartButton(event)) {
				if(!_isDrag) {
					// setup drag start tests
					_startX = event.getX();
					_startY = event.getY();

					_startTimerSource = (Canvas3D)event.getSource();	
					_startTimer.restart();
		
					// test for start
					if(isStartDrag(event))
						startInputDrag((Canvas3D)event.getSource(),
						 event.getX(), event.getY());
				}
			} else {
				_startTimer.stop();
				if(_isDrag) stopInputDrag(
				 (Canvas3D)event.getSource());
			}
		}
		
		public void mouseReleased(MouseEvent event) {
			// check for deactivation, reject repeats
			if(isStartButton(event)) {
				_startTimer.stop();
				if(_isDrag) stopInputDrag(
				 (Canvas3D)event.getSource());
			}
		}
	
		public void mouseClicked(MouseEvent event) {}
		
		public void mouseEntered(MouseEvent event) {}
	
		public void mouseExited(MouseEvent event) {}
	};
		
	/**
	Determines if the button in the event was the trigger
	button.  It does not determine if the button is down or up.
	@return True if trigger button.
	*/
	protected boolean isStartButton(InputEvent event) {
		boolean start;
		if(_buttons == Input.BUTTON_IGNORE)
			start = false;
		else
			start = ((_buttons & Input.whichButton(event)) != 0);
		 
if(Debug.getEnabled()){
Debug.println(this, "AwtMouseDragSensor",
 "AwtMouseDragSensor:isStartButton:" +
 " btns=" + _buttons +
 " whichBtns=" + Input.whichButton(event) +
 " start=" + start);}
 
 		return start;
	}
		
	/**
	Determines if a drag should start according to the mouse
	buttons and mouse position in the specified event.
	@return True if a drag should start.
	*/
	protected boolean isStartDrag(MouseEvent event) {
		// start if trigger button down and beyond deadband		
		boolean start = isStartButton(event) && (
		 Math.abs(event.getX() - _startX) >= 2 ||
		 Math.abs(event.getY() - _startY) >= 2);
	
if(Debug.getEnabled()){
Debug.println(this, "AwtMouseDragSensor",
 "AwtMouseDragSensor:isStartDrag:" +
 " pos=" + event.getX() + " " + event.getY() +
 " startPos=" + _startX + " " + _startY +
 " btns=" + _buttons +
 " whichBtns=" + Input.whichButton(event) +
 " start=" + start);}
 
		return start;
	}

	/**
	Call when a drag must start (becomes active).
	*/	
	protected void startInputDrag(Canvas3D source,
	 int rawX, int rawY) {
		
		_isDrag = true;
		Input.cookMouse(rawX, rawY, _pos);
		
if(Debug.getEnabled()){
Debug.println(this, "AwtMouseDragSensor",
"AwtMouseDragSensor:startInputDrag:" +
" cookedPos=" + _pos +
" source=" + source);}

		_eventOut.startInputDrag(source, _pos);
		_eventOut.doInputDrag(source, _pos);
	}
		
	/**
	Call when the active drag position changes.
	*/	
	protected void doInputDrag(Canvas3D source,
	 int rawX, int rawY) {
 
		Input.cookMouse(rawX, rawY, _pos);
				
if(Debug.getEnabled()){
Debug.println(this, "AwtMouseDragSensor",
"AwtMouseDragSensor:doInputDrag:" +
" cookedPos=" + _pos +
" source=" + source);}
		
		_eventOut.doInputDrag(source, _pos);
	}
		
	/**
	Call when the active drag stops.
	*/	
	protected void stopInputDrag(Canvas3D source) {
		
		_isDrag = false;
				
if(Debug.getEnabled()){
Debug.println(this, "AwtMouseDragSensor",
"AwtMouseDragSensor:stopInputDrag:" +
" cookedPos=" + _pos +
" source=" + source);}
		
		_eventOut.doInputDrag(source, _pos);
		_eventOut.stopInputDrag(source, _pos);
	}

}