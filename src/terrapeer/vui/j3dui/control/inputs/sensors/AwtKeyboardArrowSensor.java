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
An AWT input sensor that monitors arrow key activity,
which is interpreted as a relative drag (i.e. always starts at
(0,0)).  Events are generated when any arrow key goes down, any
arrow keys go up, and at a regular time interval while any arrow
key is down.  A single key press generates a delta of +/-1.  Key
repeats accumulate simulating a drag.  Multiple arrows keys can
be used at the same time.
<P>
Relying on key repeats is unreliable when multiple arrow keys
are used since the repeat is often prematurely terminated.  As
such an internal timer is used for key repeats.  Use
setTimeDelays() to set the initial and key repeat times.
<P>
Only a single event source can be handled at a time.  If a new
source occurs during an active drag the old drag will be stopped
and a new one started with the new source.

@deprecated Replaced by AwtKeyboardDragSensor for naming
consistency and to conform with new pattern for AWT-based event
sensors.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class AwtKeyboardArrowSensor {
	
	// public interface =========================================

	/**
	Constructs a AwtKeyboardArrowSensor for the specified event
	target.  Must use addSourceDisplay() to add an event source
	for this sensor.
	@param target Event target.  Never null.
	*/
	public AwtKeyboardArrowSensor(InputDragTarget target) {
		if(target==null) throw new
		 IllegalArgumentException("<target> is null.");
		_eventTarget = target;

		_timer = new Timer(100, new ActionListener() {
		 
			public void actionPerformed(ActionEvent evt) {
 
if(Debug.getEnabled()){		
Debug.println("RateInputDragFilter.timer",
"TIMER:AwtKeyboardArrowSensor:actionPerformed:" +
" initDelay=" + _timer.getInitialDelay() +
" repeatDelay=" + _timer.getDelay());}

				doInputDrag();
			}    
		});
		
		setTimeDelays(500, 50);
	}

	/**
	Constructs a AwtKeyboardArrowSensor for the specified event
	target with the specified event source display.
	@param target Event target.  Never null.
	@param display Source display.  Never null.
	*/
	public AwtKeyboardArrowSensor(InputDragTarget target,
	 Canvas3D source) {

		this(target);	 
		addEventSource(source);
	}
	
	/**
	Gets the event target.
	@return Reference to the event target.
	*/
	public InputDragTarget getEventTarget() {
		return _eventTarget;
	}

	/**
	Sets the initial time delay to the first key repeat and the
	time delay between subsequent repeats.  The default is
	0.5 sec for the initial delay and 0.05 sec for repeats.
	@param initDelay Initial delay in milliseconds.
	@param repeatDelay Repeat delay in milliseconds.
	*/
	public void setTimeDelays(int initDelay, int repeatDelay) {
		_timer.setInitialDelay(initDelay);
		_timer.setDelay(repeatDelay);
	}

	/**
	Adds a display as an event source for this sensor.
	@param source  Source display.  Never null.
	*/
	public void addEventSource(Canvas3D source) {
		if(source==null) throw new
		 IllegalArgumentException("<source> is null.");
		
		source.addKeyListener(_keyListener);
	}
			
	// personal body ============================================
	
	/** Event target. Never null. */
	private InputDragTarget _eventTarget;
	
	/** Rate timer. */
	private Timer _timer;
	
	/** True if drag is active. */
	private boolean _isDrag = false;
	
	/** Source for current drag. */
	private Canvas3D _dragSource;
	
	/** Current cooked drag position relative to start. */
	private Vector2d _dragPos = new Vector2d();
	
	/** Dummy for drag delta. (for GC) */
	private Vector2d _dragDelta = new Vector2d();
	
	/** Last arrow key. */
	private int _lastArrow = Input.ARROW_NONE;
	
	/** Current arrow keys down. */
	private int _arrows = Input.ARROW_NONE;

	/** Key event listener proxy for this sensor. */
	protected KeyListener _keyListener = new KeyListener() {
		public void keyPressed(KeyEvent event) {
		
			int arrow = Input.whichArrow(event);
			
			// reject repeats and non arrow keys
			if(arrow==_lastArrow || arrow==Input.ARROW_NONE)
				return;
	
if(Debug.getEnabled()){
Debug.println("AwtKeyboardArrowSensor",
"EVENT:AwtKeyboardArrowSensor:keyPressed:" + event);}

			// update arrow key state and drag events
			_arrows |= arrow;
			_lastArrow = arrow;
			Canvas3D source = (Canvas3D)event.getSource();
						
			if(!_isDrag) {
				// start a new drag
				_dragSource = source;
				startInputDrag();
			} else {
				if(source != _dragSource) {
					// new source: stop old and start new drag
					stopInputDrag();
					_dragSource = source;
					startInputDrag();
				} else {
					// continue old drag
					doInputDrag();
				}
			}
		}
		
		public void keyReleased(KeyEvent event) {
		
			int arrow = Input.whichArrow(event);
			Canvas3D source = (Canvas3D)event.getSource();

			// ignore if no drag, wrong source, or not an arrow			
			if(!_isDrag || source!=_dragSource ||
			 arrow==Input.ARROW_NONE) return;
	
if(Debug.getEnabled()){Debug.println("AwtKeyboardArrowSensor",
"EVENT:AwtKeyboardArrowSensor:keyReleased:" + event);}

			// update arrow key state and drag events
			_arrows &= ~arrow;

			if((_arrows&Input.ARROW_IS_DOWN) == 0) {
				_lastArrow = Input.ARROW_NONE;
				stopInputDrag();
			} else {
				doInputDrag();
			}
		}
	
		public void keyTyped(KeyEvent event) {}
	};
		
	/**
	Call when a drag starts (becomes active).
	*/
	protected void startInputDrag() {
		_isDrag = true;
		_dragPos.set(0, 0);
		
if(Debug.getEnabled()){
Debug.println("AwtKeyboardArrowSensor",
"ARROW:AwtKeyboardArrowSensor:startInputDrag:" +
" cookedPos=" + _dragPos +
" source=" + _dragSource);}
		
		getEventTarget().startInputDrag(_dragSource, _dragPos);
		getEventTarget().doInputDrag(_dragSource, _dragPos);

		doInputDrag();
		_timer.start();
	}
		
	/**
	Call when the active drag position changes.
	*/
	protected void doInputDrag() {

		// determine drag delta by arrow	
		_dragDelta.set(0, 0);
		
		if((_arrows&Input.ARROW_UP) != 0)
			_dragDelta.y++;
		if((_arrows&Input.ARROW_DOWN) != 0)
			_dragDelta.y--;
		if((_arrows&Input.ARROW_LEFT) != 0)
			_dragDelta.x--;
		if((_arrows&Input.ARROW_RIGHT) != 0)
			_dragDelta.x++;
		
		_dragPos.add(_dragDelta);
				
if(Debug.getEnabled()){
Debug.println("AwtKeyboardArrowSensor",
 "ARROW:AwtKeyboardArrowSensor:doInputDrag:" +
" cookedPos=" + _dragPos +
" delta=" + _dragDelta +
" source=" + _dragSource);}
		
		getEventTarget().doInputDrag(_dragSource, _dragPos);
	}
		
	/**
	Call when the active drag stops.
	*/
	protected void stopInputDrag() {
		
		_timer.stop();
		_isDrag = false;
				
if(Debug.getEnabled()){
Debug.println("AwtKeyboardArrowSensor",
 "ARROW:AwtKeyboardArrowSensor:stopInputDrag:" +
" cookedPos=" + _dragPos +
" source=" + _dragSource);}
		
		getEventTarget().stopInputDrag(_dragSource, _dragPos);
	}

}