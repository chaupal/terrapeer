package terrapeer.vui.j3dui.control.actuators;

import javax.vecmath.*;

/**
This interface serves as a target for input actuation events.
Actuations can occur with a fixed or cumulative offset.
Depending on the actuation involved, not all of the dimensions
in the actuation value may be used (the value could be
1D, 2D, 3D, or 4D), and values may be interpreted in different
coordinates spaces (translation, rotation, scale, color, etc.).

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/
public interface ActuationTarget {
	
	// public interface =========================================

	/**
	Initializes the actuation reference value and target object
	actuation state to the specified value.  The default
	reference value is specific to the target.
	@param value Actuation reference value.
	*/
	public void initActuation(Tuple4d value);

	/**
	Updates the target object actuation state to the specified
	value relative to the actuation reference value.  The manner
	in which the new value is applied to the reference (additive,
	logarithmic, etc.) is specific to the target.
	@param value Actuation update value.
	*/
	public void updateActuation(Tuple4d value);

	/**
	Synchronizes the actuation reference value with the current
	target object actuation state.  Typically called at the end
	of a drag operation so that the actuation offset will be
	cumulative.
	*/
	public void syncActuation();
	
}