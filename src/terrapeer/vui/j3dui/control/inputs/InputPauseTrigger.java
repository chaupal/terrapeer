package terrapeer.vui.j3dui.control.inputs;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;

/**
A mouse-over pause trigger that monitors move events for a pause
in movement.  An enable input event enables or disables the
trigger timer and output events (i.e. it controls when pausing
is "allowed").

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class InputPauseTrigger
 implements InputMoveTarget, EnableTarget {
	
	// public interface =========================================

	/**
	Constructs an InputPauseTrigger with event target <target>.
	@param target Event target.  Never null.
	*/
	public InputPauseTrigger(InputPauseTarget target) {
		if(target==null) throw new
		 IllegalArgumentException("<target> is null.");
		_eventTarget = target;

		// build pause start timer, set start delay
		_startTimer = new Timer(666, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
 
if(Debug.getEnabled()){		
Debug.println("InputPauseTrigger.timer",
"TIMER:InputPauseTrigger:actionPerformed:" +
" isPause=" + _isPause);}

				if(!_isPause) {
					_isPause = true;
					_eventTarget.setInputPause(_isPause);
				}
			}
		});
		_startTimer.setRepeats(false);
	}

	/**
	Gets the event target.
	@return Reference to event target.
	*/
	public InputPauseTarget getEventTarget() {
		return _eventTarget;
	}

	// InputMoveTarget implementation
	
	public void setInputMove(Canvas3D source, Vector2d pos) {
		if(!_enable) return;
		
if(Debug.getEnabled()){
Debug.println("InputPauseTrigger",
"PAUSE:InputPauseTrigger:setInputMove:" +
" isPause=" + _isPause);}

		if(_isPause) {
			_isPause = false;
			_eventTarget.setInputPause(_isPause);
		}
		
		_startTimer.restart();
	}

	// EnableTarget implementation

	public void setEnable(boolean enable) {
		if(enable == _enable) return;
		_enable = enable;
		
if(Debug.getEnabled()){
Debug.println("InputPauseTrigger",
"PAUSE:InputPauseTrigger:setInputMove:" +
" enable=" + enable +
" isPause=" + _isPause);}

		if(_enable) {
			_startTimer.restart();
		} else {
			_startTimer.stop();
			
			if(_isPause) {
				_isPause = false;
				_eventTarget.setInputPause(_isPause);
			}
		}
	}
			
	// personal body ============================================
	
	/** Event target. */
	private InputPauseTarget _eventTarget;
	
	/** True if timer and events enabled. */
	private boolean _enable = true;
	
	/** True if pause is active. */
	private boolean _isPause = false;
	
	/** Pause start timer. */
	private Timer _startTimer;
	
}