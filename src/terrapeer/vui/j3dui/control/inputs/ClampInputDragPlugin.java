package terrapeer.vui.j3dui.control.inputs;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.mappers.Mapper;

/**
An input drag filter plugin that limits or clamps the the drag
position value.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class ClampInputDragPlugin
 extends InputDragFilterPlugin {
	
	// public interface =========================================

	/**
	Constructs a ClampInputDragPlugin.
	*/
	public ClampInputDragPlugin() {}

	/**
	Constructs a ClampInputDragPlugin.
	@param clamp Source value limits (min, max).  Applied to all
	value dimensions.
	*/
	public ClampInputDragPlugin(Vector2d clamp) {
		setClamp(Mapper.DIM_ALL, clamp);
	}

	/**
	Sets the limits applied to the specified source value
	dimensions.
	@param dims Dimensions (Mapper.DIM_???) whose clamp will
	be set.
	@param clamp Source value limits (min, max).
	*/
	public Vector2d setClamp(int dims, Vector2d clamp) {
		if((dims&Mapper.DIM_X)!=0) _clamps[0].set(clamp);
		if((dims&Mapper.DIM_Y)!=0) _clamps[1].set(clamp);
		
		return clamp;
	}

	/**
	Gets the limits applied to the the source value dimensions.
	@return Reference to the target value limit array (0=X, 1=X).
	*/
	public Vector2d[] getClamps(int index) {
		return _clamps;
	}

	// InputDragFilterPlugin implementation
	
	public String toString() {
		return "ClampInputDragPlugin";
	}
			
	// personal body ============================================
	
	/** Clamps (min,max) applied to source values, by dim. */
	private Vector2d _clamps[] = {
		new Vector2d(Double.NEGATIVE_INFINITY,
		 Double.POSITIVE_INFINITY),
		new Vector2d(Double.NEGATIVE_INFINITY,
		 Double.POSITIVE_INFINITY),
	};

	// InputDragFilterPlugin implementation

	protected Vector2d toTargetValue(Canvas3D source,
	 Vector2d value, Vector2d copy) {
		
		copy.set(
		 Math.max(Math.min(value.x, _clamps[0].y), _clamps[0].x),
		 Math.max(Math.min(value.y, _clamps[1].y), _clamps[1].x)
		);
		
		return copy;
	}
	
}
