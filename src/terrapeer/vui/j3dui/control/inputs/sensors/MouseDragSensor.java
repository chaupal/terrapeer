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
drag, defaulting to Input.BUTTON_FIRST.  Events are generated
when a mouse drag initiates, terminates, or is active and
the mouse moves.
<P>
A drag operation initiates when the mouse button goes down and
either the mouse is moved a minimum deadband distance or the
button is held down for a minimum start time.

@deprecated Replaced by AwtMouseDragSensor to conform with
new pattern for AWT-based event sensors.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class MouseDragSensor extends InputSensor {
	
	// public interface =========================================

	/**
	Constructs a MouseDragSensor for the specified display.
	@param target Event target.  Never null.
	@param display Source display.  If null events from all
	displays will be reported.
	@param host Group node to host this Behavior.  Null if none,
	but this sensor must be added to the scene graph to work.
	*/
	public MouseDragSensor(InputDragTarget target,
	 Canvas3D display, Group host) {
	 
		super(display, host);
		
		if(target==null) throw new
		 IllegalArgumentException("<target> is null.");
		_eventTarget = target;

		// build drag start timer, set start delay
		_startTimer = new Timer(333, new ActionListener() {
			public void actionPerformed(ActionEvent event) {
 
if(Debug.getEnabled()){		
Debug.println("MouseDragSensor.timer",
"TIMER:MouseDragSensor:actionPerformed:" +
" startPos=" + _startX + " " + _startY);}

		 		startInputDrag(_startTimerSource,
		 		 _startX, _startY);
			}    
		});
		_startTimer.setRepeats(false);
	}

	/**
	Gets the event target.
	@return Reference to the event target.
	*/
	public InputDragTarget getEventTarget() {
		return _eventTarget;
	}

	/**
	Sets which mouse buttons will activate this sensor.
	This sensor is activated if any of the specified mouse
	buttons are down.
	@param buttons Button flags (Input.BUTTON_???).  If
	BUTTON_NONE drag will not activate.
	*/
	public void setButtons(int buttons) {
	   	_buttons = buttons;
	}
			
	// personal body ============================================
	
	/** Event target. Never null. */
	private InputDragTarget _eventTarget;
	
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
Debug.println("MouseDragSensor",
 "START:MouseDragSensor:isStartButton:" +
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
Debug.println("MouseDragSensor",
 "START:MouseDragSensor:isStartDrag:" +
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
Debug.println("MouseDragSensor",
"DRAG:MouseDragSensor:startInputDrag:" +
" cookedPos=" + _pos +
" source=" + source);}

		_eventTarget.startInputDrag(source, _pos);
		_eventTarget.doInputDrag(source, _pos);
	}
		
	/**
	Call when the active drag position changes.
	*/	
	protected void doInputDrag(Canvas3D source,
	 int rawX, int rawY) {
 
		Input.cookMouse(rawX, rawY, _pos);
				
if(Debug.getEnabled()){
Debug.println("MouseDragSensor",
"DRAG:MouseDragSensor:doInputDrag:" +
" cookedPos=" + _pos +
" source=" + source);}
		
		_eventTarget.doInputDrag(source, _pos);
	}
		
	/**
	Call when the active drag stops.
	*/	
	protected void stopInputDrag(Canvas3D source) {
		
		_isDrag = false;
				
if(Debug.getEnabled()){
Debug.println("MouseDragSensor",
"DRAG:MouseDragSensor:stopInputDrag:" +
" cookedPos=" + _pos +
" source=" + source);}
		
		_eventTarget.doInputDrag(source, _pos);
		_eventTarget.stopInputDrag(source, _pos);
	}

	// InputSensor implementation
	
	protected WakeupCondition buildWakeup() {
		return new WakeupOr(new WakeupCriterion[] {
			new WakeupOnAWTEvent(MouseEvent.MOUSE_DRAGGED),
			new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED),
			new WakeupOnAWTEvent(MouseEvent.MOUSE_RELEASED),
		});
	}
	
	protected void mousePressed(MouseEvent event) {
		if(Debug.getEnabled()) super.mousePressed(event);

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
	
	protected void mouseReleased(MouseEvent event) {
		if(Debug.getEnabled()) super.mouseReleased(event);

		// check for deactivation, reject repeats
		if(isStartButton(event)) {
			_startTimer.stop();
			if(_isDrag) stopInputDrag(
			 (Canvas3D)event.getSource());
		}
	}
	
	protected void mouseDragged(MouseEvent event) {
		if(Debug.getEnabled()) super.mouseDragged(event);

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

}