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
An AWT input sensor that monitors arrow key activity as a
relative drag (i.e. always starts at (0,0)).  Events are
generated when any arrow key goes down, any
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

@author Jon Barrilleaux,
copyright (c) 1999-2000 Jon Barrilleaux,
All Rights Reserved.
*/

public class AwtKeyboardDragSensor {
	
	// public interface =========================================

	/**
	Constructs an AwtKeyboardDragSensor.
	*/
	public AwtKeyboardDragSensor() {
		
		// build timer for initial and repeat key delay
		_timer = new Timer(100, new ActionListener() {
		 
			public void actionPerformed(ActionEvent evt) {
 
if(Debug.getEnabled()){		
Debug.println(this, "AwtKeyboardDragSensor.timer",
"AwtKeyboardDragSensor:actionPerformed:" +
" initDelay=" + _timer.getInitialDelay() +
" repeatDelay=" + _timer.getDelay());}

				doInputDrag();
			}    
		});
		
		setTimeDelays(500, 50);
	}

	/**
	Constructs an AwtKeyboardDragSensor with an event source
	and target.
	@param source Event source.  Null if none.
	@param target Event target.  Null if none.
	*/
	public AwtKeyboardDragSensor(Canvas3D source,
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
Debug.println(this, "AwtKeyboardDragSensor",
"AwtKeyboardDragSensor:addEventSource:" +
" source=" + source);}
		
		source.addKeyListener(_listener);
		source.addMouseListener(_focuser);
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
	Sets the initial time delay to the first key repeat and the
	time delay between subsequent repeats.
	@param initDelay Initial delay in milliseconds.  Defaults
	to 0.5.
	@param repeatDelay Repeat delay in milliseconds.  Defaults
	to 0.05.
	*/
	public void setTimeDelays(int initDelay, int repeatDelay) {
		_timer.setInitialDelay(initDelay);
		_timer.setDelay(repeatDelay);
	}
			
	// personal body ============================================
	
	/** Event out splitter. Never null. */
	private InputDragSplitter _eventOut =
	 new InputDragSplitter();
	
	/** init and repeat key delay timer. */
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

	/**
	Key event listener proxy for this sensor.  Must be
	personal to enforce correct event source type.
	*/
	protected KeyListener _listener = new KeyListener() {
		public void keyPressed(KeyEvent event) {
		
			int arrow = Input.whichArrow(event);
			
			// reject repeats and non arrow keys
			if(arrow==_lastArrow || arrow==Input.ARROW_NONE)
				return;
	
if(Debug.getEnabled()){
Debug.println(this, "AwtKeyboardDragSensor.listener",
"AwtKeyboardDragSensor:keyPressed:" + event);}

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
	
if(Debug.getEnabled()){
Debug.println(this, "AwtKeyboardDragSensor.listener",
"AwtKeyboardDragSensor:keyReleased:" + event);}

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
	Listens for mouse entry into a source display, and gets
	it focus for keyboard events.
	*/
	protected MouseListener _focuser = new MouseAdapter() {
		public void mouseEntered(MouseEvent event) {
		
if(Debug.getEnabled()){
Debug.println(this, "AwtKeyboardDragSensor.listener",
"AwtKeyboardDragSensor:mouseEntered:" +
" event=" + event);}
		
			// get focus
			((Component)event.getSource()).requestFocus();
		}
	};
		
	/**
	Call when a drag starts (becomes active).
	*/
	protected void startInputDrag() {
		_isDrag = true;
		_dragPos.set(0, 0);
		
if(Debug.getEnabled()){
Debug.println(this, "AwtKeyboardDragSensor",
"AwtKeyboardDragSensor:startInputDrag:" +
" cookedPos=" + _dragPos +
" source=" + _dragSource);}
		
		_eventOut.startInputDrag(_dragSource, _dragPos);
		_eventOut.doInputDrag(_dragSource, _dragPos);

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
Debug.println(this, "AwtKeyboardDragSensor",
"AwtKeyboardDragSensor:doInputDrag:" +
" cookedPos=" + _dragPos +
" delta=" + _dragDelta +
" source=" + _dragSource);}
		
		_eventOut.doInputDrag(_dragSource, _dragPos);
	}
		
	/**
	Call when the active drag stops.
	*/
	protected void stopInputDrag() {
		
		_timer.stop();
		_isDrag = false;
				
if(Debug.getEnabled()){
Debug.println(this, "AwtKeyboardDragSensor",
"AwtKeyboardDragSensor:stopInputDrag:" +
" cookedPos=" + _dragPos +
" source=" + _dragSource);}
		
		_eventOut.stopInputDrag(_dragSource, _dragPos);
	}

}