package terrapeer.vui.j3dui.control.actuators;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.mappers.Mapper;

/**
An abstract base class for actuator personality plugins.  Also
defines the format of the input actuation value. Includes
basic vector processing of the actuation value including scale,
offset, and clamping (in that order).
<P>
The target node controlled by this plugin should only be
manipulated through this plugin.  Setting the target node's
state directly will invalidate the state of this plugin and
its actuator.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public abstract class ActuatorPlugin {
	
	// public interface =========================================

	/**
	Gets the target node.
	@return Reference to the target node.
	*/
	public abstract Node getTargetNode();

	/**
	Returns a string naming this plugin.
	@return Plugin type string.
	*/
	public abstract String toString();

	/**
	Sets the scaling factor applied to the input source-side
	actuation value.
	@param value Actuation scaling value.
	@return Reference to the actuation scaling value.
	*/
	public Tuple4d setSourceScale(Tuple4d value) {
		_scale.set(value);
		return value;
	}

	/**
	Gets the scaling factor applied to the source-side
	actuation value.
	@return Reference to the actuation scaling value.
	*/
	public Tuple4d getSourceScale() {
		return _scale;
	}

	/**
	Sets the constant offset applied to the source-side
	actuation value.
	@param value Actuation offset value.
	@return Reference to the actuation offset value.
	*/
	public Tuple4d setSourceOffset(Tuple4d value) {
		_offset.set(value);
		return value;
	}

	/**
	Gets the constant offset applied to the source-side
	actuation value.
	@return Reference to actuation offset value.
	*/
	public Tuple4d getSourceOffset() {
		return _offset;
	}

	/**
	Sets the limits applied to the specified target-side
	actuation value dimensions.
	@param flags The actuation dimensions (Mapper.DIM_???)
	whose clamp will be set.
	@param clamp Actuation value limits (min, max) for the
	specified dimensions.
	@return Reference to the actuation limit values.
	*/
	public Tuple2d setTargetClamp(int flags, Tuple2d clamp) {
		if((flags&Mapper.DIM_X)!=0) _clamps[0].set(clamp);
		if((flags&Mapper.DIM_Y)!=0) _clamps[1].set(clamp);
		if((flags&Mapper.DIM_Z)!=0) _clamps[2].set(clamp);
		if((flags&Mapper.DIM_W)!=0) _clamps[3].set(clamp);
		
		return clamp;
	}

	/**
	Gets the limits applied to the target-side actuation value
	dimensions.
	@return Reference to the actuation limit value array (0=X,
	3=W).
	*/
	public Vector2d[] getTargetClamps(int index) {
		return _clamps;
	}
			
	// personal body ============================================
	
	/** Source-side actuation scaling value. */
	private Vector4d _scale =
	 new Vector4d(1, 1, 1, 1);
	
	/** Source-side actuation offset value. */
	private Vector4d _offset =
	 new Vector4d(0, 0, 0, 0);
	
	/** Target-side actuation value limits (min,max) by dim. */
	private Vector2d _clamps[] = {
		new Vector2d(Double.NEGATIVE_INFINITY,
		 Double.POSITIVE_INFINITY),
		new Vector2d(Double.NEGATIVE_INFINITY,
		 Double.POSITIVE_INFINITY),
		new Vector2d(Double.NEGATIVE_INFINITY,
		 Double.POSITIVE_INFINITY),
		new Vector2d(Double.NEGATIVE_INFINITY,
		 Double.POSITIVE_INFINITY)
	};
	
	/**
	Performs the source-side vector processing, which includes
	scale and offset (in that order).  Should be used by
	subclasses to process all input event values.
	@param value Actuation value.
	@param copy Container for the copied return value.
	@return Reference to <copy>.
	*/
	protected Tuple4d toActuationSource(Tuple4d value,
	 Tuple4d copy) {
	 
		copy.set(
		 _scale.x*value.x, _scale.y*value.y,
		 _scale.z*value.z, _scale.w*value.w
		);
		
		copy.add(_offset);
		
		return copy;
	}
	
	/**
	Performs the target-side vector processing, which includes
	clamping.  Should be used by subclasses to process all
	output target state values.  Note that clamping can not
	be applied in plugins where the state is not be maintained
	as a vector value.
	@param value Actuation value.
	@param copy Container for the copied return value.
	@return Reference to <copy>.
	*/
	protected Tuple4d toActuationTarget(Tuple4d value,
	 Tuple4d copy) {
	 
		copy.set(
		 Math.max(Math.min(copy.x, _clamps[0].y), _clamps[0].x),
		 Math.max(Math.min(copy.y, _clamps[1].y), _clamps[1].x),
		 Math.max(Math.min(copy.z, _clamps[2].y), _clamps[2].x),
		 Math.max(Math.min(copy.w, _clamps[3].y), _clamps[3].x)
		);
		
		return copy;
	}

	/**
	Handles the initiating actuation action with input actuation
	value <value>.
	@param value Input actuation value.
	*/
	protected abstract void initActuation(Tuple4d value);

	/**
	Transforms the updating actuation action with input actuation
	value <value>.
	@param value Input actuation value.
	*/
	protected abstract void updateActuation(Tuple4d value);

	/**
	Handles the syncing actuation action.
	*/
	protected abstract void syncActuation();
	
}