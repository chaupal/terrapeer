package terrapeer.vui.j3dui.control.mappers;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
An abstract base class for input drag mapper personality plugins.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public abstract class InputDragMapperPlugin {
	
	// public interface =========================================

	/**
	Constructs an InputDragMapperPlugin with default target
	space mapping parameters.
	*/
	public InputDragMapperPlugin() {}

	/**
	Returns a string naming this plugin.
	@return Plugin type string.
	*/
	public String toString() {
		return "InputDragMapperPlugin";
	}
			
	// personal body ============================================

	/**
	Transforms the starting input drag position into the target
	actuation value (copy) relative to the target space.
	@param source Source display canvas.
	@param pos Input source position.
	@param copy Container for the copied return value.
	*/
	protected abstract void startInputDrag(
	 Canvas3D source, Tuple2d pos, Tuple4d copy);

	/**
	Transforms the continuing input drag position into the target
	actuation value (copy) relative to the target space.
	@param source Source display canvas.
	@param pos Input source position.
	@param copy Container for the copied return value.
	*/
	protected abstract void doInputDrag(
	 Canvas3D source, Tuple2d pos, Tuple4d copy);

	/**
	Transforms the stopping input drag position into the target
	actuation value (copy) relative to the target space.
	@param source Source display canvas.
	@param pos Input source position.
	@param copy Container for the copied return value.
	*/
	protected abstract void stopInputDrag(
	 Canvas3D source, Tuple2d pos, Tuple4d copy);
	
}