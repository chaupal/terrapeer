package terrapeer.vui.j3dui.control.mappers;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;
import terrapeer.vui.j3dui.control.inputs.*;

/**
Maps display move and drag event inputs into object pick
events.  Object pick events are generated only when the pick
target changes, not when the pick cursor position changes.
Can be configured for general use or specifically for dragging
or dropping.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class ObjectPickMapper
 implements InputMoveTarget, InputDragTarget, EnableTarget {
	
	// public interface =========================================

	/**
	Constructs a ObjectPickMapper with pick engine <engine>
	with the pickable node list, and event target <eventTarget>.
	@param picker Pick engine.  Never null.
	@param target Event target.  Never null.
	*/
	public ObjectPickMapper(ObjectPickTarget target,
	 PickEngine engine) {
		
		if(engine==null) throw new
		 IllegalArgumentException("'engine' is null.");
		_pickEngine = engine;
		_pickEngine.setUseBounds(false);
		
		if(target==null) throw new
		 IllegalArgumentException("'target' is null.");
		_eventTarget = target;
	}

	/**
	Gets the event target.
	@return Reference to the event target.
	*/
	public ObjectPickTarget getEventTarget() {
		return _eventTarget;
	}

	/**
	Gets the pick engine.
	@return Reference to the pick engine.
	*/
	public PickEngine getPickEngine() {
		return _pickEngine;
	}

	/**
	Specifies the manner of drag picking.  Move picking is
	unaffected.
	@param doPick If true, drag picking is reported when the
	drag starts and "no target" is reported when the drag stops.
	Default is true.
	*/	
	public void setDoPick(boolean doPick) {
		_doPick = doPick;
	}

	/**
	Specifies the manner of drag picking.  Move picking is
	unaffected.
	@param doDrag If true, drag picking is reported when the
	drag starts.  Default is false.
	*/	
	public void setDoDrag(boolean doDrag) {
		_doDrag = doDrag;
	}

	/**
	Specifies the manner of drag picking.  Move picking is
	unaffected.
	@param doDrop If true, drag picking is reported during the
	drag and when it stops (but after any "no target" report).
	Default is false.
	*/	
	public void setDoDrop(boolean doDrop) {
		_doDrop = doDrop;
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
		_enable = enable;
		
if(Debug.getEnabled()){
Debug.println("ObjectPickMapper",
"ObjectPickMapper:setEnable:" +
" enable=" + _enable);}

	}
	
	// InputMoveTarget implementation
	
	public void setInputMove(Canvas3D source, Vector2d pos) {
	 
		if(!_enable) return;
		
if(Debug.getEnabled()){
Debug.println("ObjectPickMapper",
"ObjectPickMapper:setInputMove:...");}

		updatePick(source, pos);
	}
	
	// InputDragTarget implementation
	
	public void startInputDrag(Canvas3D source, Vector2d pos) {
		if(!_enable) return;
		
		if(_doPick || _doDrag) {
	
if(Debug.getEnabled()){
Debug.println("ObjectPickMapper",
"ObjectPickMapper:startInputDrag:...");}

			updatePick(source, pos);
		}
	}
	
	public void doInputDrag(Canvas3D source, Vector2d pos) {
		if(!_enable) return;
		
		if(_doDrop) {
		
if(Debug.getEnabled()){
Debug.println("ObjectPickMapper",
"ObjectPickMapper:doInputDrag:...");}

			updatePick(source, pos);
		}
	}
	
	public void stopInputDrag(Canvas3D source, Vector2d pos) {
		if(!_enable) return;
		
		if(_doPick || _doDrop) {
		
if(Debug.getEnabled()){
Debug.println("ObjectPickMapper",
"ObjectPickMapper:stopInputDrag:...");}

			if(_doPick) updateNoPick();		// do first
			if(_doDrop) updatePick(source, pos);
		}
	}
			
	// personal body ============================================
	
	/** Pick engine.  Never null. */
	private PickEngine _pickEngine;
	
	/** Event target.  Never null. */
	private ObjectPickTarget _eventTarget;
	
	/** True if actuator enabled. */
	private boolean _enable = true;
	
	/** True if drag picking for drag initiation. */
	private boolean _doPick = true;
	
	/** True if drag picking for drag initiation. */
	private boolean _doDrag = false;
	
	/** True if drag picking for drag termination. */
	private boolean _doDrop = false;
	
	/** Index of the last pick target. -1 if none. */
	private int _lastPick = -1;

	/**
	Determines the current pick status according to the
	position in the source space and, if the status changed,
	generates an output event.
	@param source Source display canvas.  Never null.
	@param pos position relative to source space.
	*/
	protected void updatePick(Canvas3D source, Vector2d pos) {		
		int targetI = _pickEngine.pickTarget(source, pos, null);
		
		if(targetI != _lastPick) {
	
if(Debug.getEnabled()){
Debug.println("ObjectPickMapper",
"ObjectPickMapper:updatePick:" +
" pos=" + pos +
" trgIndex=" + targetI +
" trgObject=" + _pickEngine.getPickTarget(targetI) +
" source=" + source);}

			_lastPick = targetI;
			_eventTarget.setObjectPick(targetI,
			 _pickEngine.getPickTarget(targetI));
		}
	}

	/**
	Forces the pick status to "no pick" and, if the status
	changed, generates an output event.
	*/
	protected void updateNoPick() {		
		int targetI = -1;
		
		if(targetI != _lastPick) {
	
if(Debug.getEnabled()){
Debug.println("ObjectPickMapper",
"ObjectPickMapper:updateNoPick:" +
" trgIndex=" + targetI);}

			_lastPick = targetI;
			_eventTarget.setObjectPick(targetI, null);
		}
	}
	
}