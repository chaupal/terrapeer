package terrapeer.vui.j3dui.control.inputs;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.mappers.Mapper;

/**
An input drag filter plugin that offsets the drag position
value.  The offset can be set with setOffset() or through the
InputMoveTarget input event interface. 

@author Jon Barrilleaux,
copyright (c) 1999-2000 Jon Barrilleaux,
All Rights Reserved.
*/

public class OffsetInputDragPlugin extends InputDragFilterPlugin
 implements InputMoveTarget {
	
	// public interface =========================================

	/**
	Constructs a OffsetInputDragPlugin.
	@param scale Source offset value.
	*/
	public OffsetInputDragPlugin() {}

	/**
	Constructs a OffsetInputDragPlugin.
	@param scale Source offset value.
	*/
	public OffsetInputDragPlugin(Vector2d offset) {
		setOffset(offset);
	}

	/**
	Sets the offset constant applied to source values.
	@param offset Source offset value.
	*/
	public Vector2d setOffset(Vector2d offset) {
		if(!_isDrag || (_isDrag && _allowActiveOffset)) {
			_offset.set(offset);
		}
		return _offset;
	}

	/**
	Gets the offset constant applied to source values.
	@return Source offset value.
	*/
	public Vector2d getOffset() {
		return _offset;
	}

	/**
	Sets whether or not the offset can be changed while a
	drag is active.
	@param enable If true, offset can change during a drag.
	Defaults to false.
	*/
	public void allowActiveOffset(boolean enable) {
		_allowActiveOffset = enable;
	}

	// InputDragFilterPlugin implementation
	
	public String toString() {
		return "OffsetInputDragPlugin";
	}
	
	// InputMoveTarget implementation
	
	public void setInputMove(Canvas3D source, Vector2d pos) {
	
if(Debug.getEnabled()){		
Debug.println("OffsetInputDragPlugin",
"OffsetInputDragPlugin:setInputMove:" +
" offset=" + pos);}

		setOffset(pos);
	}
			
	// personal body ============================================
	
	/** Offset factor applied to source values. */
	private Vector2d _offset = new Vector2d(0, 0);
	
	/** True if offset can change during a drag. */
	private boolean _allowActiveOffset = false;
	
	/** True if drag is active. */
	private boolean _isDrag = false;

	// InputDragFilterPlugin implementation

	protected void startInputDrag(Canvas3D source,
	 Vector2d value) {
	 	_isDrag= true;
	}

	protected void stopInputDrag(Canvas3D source,
	 Vector2d value) {
	 	_isDrag= false;
	}
	
	protected Vector2d toTargetValue(Canvas3D source,
	 Vector2d value, Vector2d copy) {
		
		copy.add(value, _offset);
		return copy;
	}
	
}