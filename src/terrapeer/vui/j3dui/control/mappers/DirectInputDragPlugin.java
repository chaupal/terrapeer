package terrapeer.vui.j3dui.control.mappers;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.actuators.*;

/**
An input drag mapper plugin that directly maps input drag
value dimensions to target actuation value dimensions.  All
actuation value dimensions are affected.
<P>
The output actuation value is defined as follows:
<UL>
<LI>X - Generic X actuation value.
<LI>Y - Generic Y actuation value.
<LI>Z - Generic Z actuation value.
<LI>W - Generic W actuation value
</UL>

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class DirectInputDragPlugin
 extends InputDragMapperPlugin {
	
	// public interface =========================================

	/**
	Constructs a DirectInputDragPlugin with default source
	mapping (i.e. X to X, Y to Y).
	*/
	public DirectInputDragPlugin() {}

	/**
	Constructs a DirectInputDragPlugin with the specified source
	mapping.
	@param scale Source scaling factor.
	@param mapX The target dimensions (Mapper.DIM_???)
	controlled by the source X dimension.
	@param mapY The target dimensions (Mapper.DIM_???)
	controlled by the source Y dimension.
	@param offset Target offset value.
	*/
	public DirectInputDragPlugin(Vector2d scale, int mapX,
	 int mapY, Vector4d offset) {
	 
		setSourceScale(scale);
		setSourceMap(mapX, mapY);
		setTargetOffset(offset);
	}

	/**
	Sets the scaling factor applied to source values.
	@param scale Source scaling value.
	*/
	public Vector2d setSourceScale(Vector2d scale) {
		_scale.set(scale);
		return _scale;
	}

	/**
	Gets the scaling factor applied to source values.
	@return Reference to the source scaling value.
	*/
	public Vector2d getSourceScale() {
		return _scale;
	}

	/**
	Sets the mapping from source dimension to target
	dimension by source dimension according to target
	dimension flags (Mapper.DIM_???).
	@param mapX The target dimensions (Mapper.DIM_???)
	controlled by the source X dimension.
	@param mapY The target dimensions (Mapper.DIM_???)
	controlled by the source Y dimension.
	*/
	public void setSourceMap(int mapX, int mapY) {
		_mapX[0] = ((mapX & Mapper.DIM_X) == 0) ? false : true;
		_mapX[1] = ((mapX & Mapper.DIM_Y) == 0) ? false : true;
		_mapX[2] = ((mapX & Mapper.DIM_Z) == 0) ? false : true;
		_mapX[3] = ((mapX & Mapper.DIM_W) == 0) ? false : true;
		
		_mapY[0] = ((mapY & Mapper.DIM_X) == 0) ? false : true;
		_mapY[1] = ((mapY & Mapper.DIM_Y) == 0) ? false : true;
		_mapY[2] = ((mapY & Mapper.DIM_Z) == 0) ? false : true;
		_mapY[3] = ((mapY & Mapper.DIM_W) == 0) ? false : true;
	}

	/**
	Gets the mapping from source X dimension to target
	dimensions.
	@return Reference to an array of flags by target dimension.
	*/
	public boolean[] getSourceMapX() {
		return _mapX;
	}

	/**
	Gets the mapping from source Y dimension to target
	dimensions.
	@return Reference to an array of flags by target dimension.
	*/
	public boolean[] getSourceMapY() {
		return _mapY;
	}

	/**
	Sets the offset constant applied to target values.
	@param offset Target offset value.
	@return Target offset value.
	*/
	public Vector4d setTargetOffset(Vector4d offset) {
		_offset.set(offset);
		return _offset;
	}

	/**
	Gets the offset constant applied to target values.
	@return Reference to the target offset value.
	*/
	public Vector4d getTargetOffset() {
		return _offset;
	}
	
	// InputDragMapperPlugin implementation
	
	public String toString() {
		return "DirectInputDragPlugin";
	}
			
	// personal body ============================================
	
	/** Scale factor applied to source values. */
	private Vector2d _scale = new Vector2d(1, 1);
	
	/** Offset factor applied to target values. */
	private Vector4d _offset = new Vector4d(0, 0, 0, 0);
	
	/** If true, the target dimension gets source X value. */
	private boolean _mapX[] = {true, false, false, false};
	
	/** If true, the target dimension gets source Y value. */
	private boolean _mapY[] = {false, true, false, false};
	
	/** Dummy source value.  (for GC) */
	private final Vector2d _value = new Vector2d();
	
	/**
	Directly maps the 2D XY source space input value into a
	4D XYZW target space actuation value, which affects all
	output dimensions.  Output values are accumulated by
	dimension.  Includes source scaling and target offset.
	@param value Source value.
	@param copy Container for the copied return value.
	*/
	protected void toTargetValue(Tuple2d value,
	 Tuple4d copy) {

		_value.set(_scale.x * value.x,
		 _scale.y * value.y);
		 		
		copy.set(_offset);

		if(_mapX[0]) copy.x += _value.x;
		if(_mapX[1]) copy.y += _value.x;
		if(_mapX[2]) copy.z += _value.x;
		if(_mapX[3]) copy.w += _value.x;

		if(_mapY[0]) copy.x += _value.y;
		if(_mapY[1]) copy.y += _value.y;
		if(_mapY[2]) copy.z += _value.y;
		if(_mapY[3]) copy.w += _value.y;
		
if(Debug.getEnabled()){
Debug.println("DirectInputDragPlugin",
"DRAG:DirectInputDragPlugin:toTargetValue:" +
" inVal=" + value +
" srcScale=" + _scale +
" trgOffset=" + _offset +
" outVal=" + copy);}

	}
	
	// InputDragMapperPlugin implementation
	
	protected void startInputDrag(Canvas3D source,
	 Tuple2d pos, Tuple4d copy) {
	 	// do nothing
	}
	
	protected void doInputDrag(Canvas3D source,
	 Tuple2d pos, Tuple4d copy) {
		// map coordinates	
		toTargetValue(pos, copy);
	}
	
	protected void stopInputDrag(Canvas3D source,
	 Tuple2d pos, Tuple4d copy) {
	 	// do nothing
	}
	
}