package terrapeer.vui.j3dui.control.inputs;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;

/**
A special input drag filter that enables and disables the
passage of drag events, defaulting to enabled.  This filter
should only be used with sensors monitoring the same display
canvas otherwise its operation is undefined.  Input events
are used as follows:
<UL>
<LI> InputDragTarget: Input drag.
<LI> InputCancelTarget: Cancels any active drag.  Causes the
output drag position to return to its starting position and
blocks further dragging until drag stop. 
<LI> EnableTarget: Enables and disables dragging, with drag
starts and stops issued as needed.
</UL>
<P>
Because drag events are state sensitive (see below), unlike
with other events, this filter is the sole means for enabling
and disabling input drag events other than disabling the
sensor itself. 
<P>
To accommodate drags being enabled and disabled in mid-stride,
if a drag is active when disable occurs the output drag will
be terminated.  If a drag is active when an enable occurs or
if no drag is active but a "do drag" event occurs then the
output drag will be initiate.  

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class EnableInputDragFilter implements InputDragTarget,
 InputCancelTarget, EnableTarget {
	
	// public interface =========================================

	/**
	Constructs an EnableInputDragFilter with event target
	<target>.
	@param target Event target.  Never null.
	*/
	public EnableInputDragFilter(InputDragTarget target) {
		if(target==null) throw new
		 IllegalArgumentException("<target> is null.");
		_eventTarget = target;
	}

	/**
	Constructs an EnableInputDragFilter with event target
	<target> and enable set to <enable>.
	@param target Event target.  Never null.
	@param enable True to enable events, false to disable them.
	*/
	public EnableInputDragFilter(InputDragTarget target,
	 boolean enable) {
		this(target);
		setEnable(enable);
	}

	/**
	Gets the event target.
	@return Reference to the event target.
	*/
	public InputDragTarget getEventTarget() {
		return _eventTarget;
	}

	/**
	Gets the enable state.
	@return True if enabled.
	*/
	public boolean getEnable() {
		return _enable;
	}
	
	// EnableTarget implementation

	public void setEnable(boolean enable) {

if(Debug.getEnabled()){		
Debug.println(this, "EnableInputDragFilter.enable",
"ENABLE:EnableInputDragFilter:setEnable:" +
" enable=" + enable +
" oldEnable=" + _enable +
" srcDrag=" + _sourceDrag +
" trgDrag=" + _targetDrag);}
	
		if(_enable && !enable && _targetDrag) {
			_targetDrag = false;
			_eventTarget.stopInputDrag(_source, _pos);
		}
		
		if(!_enable && enable && _sourceDrag) {
			_targetDrag = true;
			_eventTarget.startInputDrag(_source, _pos);
		}
		
		_enable = enable;

	}
	
	// InputCancelTarget implementation

	public void setInputCancel() {

if(Debug.getEnabled()){		
Debug.println(this, "EnableInputDragFilter.cancel",
"EnableInputDragFilter:setInputCancel:" +
" enable=" + _enable +
" targetDrag=" + _targetDrag +
" startPos=" + _startPos +
" startSource=" + _startSource);}

		// if drag active, restore start position but no stop
		if(_enable && _targetDrag) {
			_cancel = true;
			_eventTarget.doInputDrag(_startSource, _startPos);
		}	

	}

	// InputDragTarget implementation
	
	public void startInputDrag(Canvas3D source, Vector2d pos) {

		_cancel = false;	 
		_startSource = source;
		_startPos.set(pos);
		 
		_source = source;
		_pos.set(pos);
		_sourceDrag = true;
 
if(Debug.getEnabled()){		
Debug.println(this, "EnableInputDragFilter",
"DRAG:EnableInputDragFilter:startInputDrag:" +
" pos=" + pos +
" source=" + source +
" enable=" + _enable +
" srcDrag=" + _sourceDrag +
" trgDrag=" + _targetDrag);}
		
		if(!_enable) return;
		
		_targetDrag = true;
		_eventTarget.startInputDrag(source, pos);
	}
	
	public void doInputDrag(Canvas3D source, Vector2d pos) {
	 
		_source = source;
		_pos.set(pos);
 
if(Debug.getEnabled()){		
Debug.println(this, "EnableInputDragFilter",
"DRAG:EnableInputDragFilter:doInputDrag:" +
" pos=" + pos +
" source=" + source +
" enable=" + _enable +
" srcDrag=" + _sourceDrag +
" trgDrag=" + _targetDrag);}
		
		if(!_enable) return;
		if(_cancel) return;
		
		if(!_targetDrag) {
			_sourceDrag = true;
			_targetDrag = true;
			_eventTarget.startInputDrag(source, pos);
		}

		getEventTarget().doInputDrag(source, pos);
	}
	
	public void stopInputDrag(Canvas3D source, Vector2d pos) {
	 
		_source = source;
		_pos.set(pos);
		_sourceDrag = false;
 
if(Debug.getEnabled()){		
Debug.println(this, "EnableInputDragFilter",
"DRAG:EnableInputDragFilter:stopInputDrag:" +
" pos=" + pos +
" source=" + source +
" enable=" + _enable +
" srcDrag=" + _sourceDrag +
" trgDrag=" + _targetDrag);}
		
		if(!_enable) return;
		if(!_targetDrag) return;
	
		_targetDrag = false;
		_eventTarget.stopInputDrag(source, pos);
	}
			
	// personal body ============================================
	
	/** Event target. */
	private InputDragTarget _eventTarget;
	
	/** True if events enabled. */
	private boolean _enable = true;
	
	/** True if a source drag is active. */
	private boolean _sourceDrag = false;
	
	/** True if an target drag is active. */
	private boolean _targetDrag = false;
	
	/** Last source. */
	private Canvas3D _source;
	
	/** Last pos. */
	private Vector2d _pos = new Vector2d();
	
	/** True if drag canceled. */
	private boolean _cancel = false;
	
	/** Starting source for cancel. */
	private Canvas3D _startSource;
	
	/** Starting pos for cancel. */
	private Vector2d _startPos = new Vector2d();
	
}