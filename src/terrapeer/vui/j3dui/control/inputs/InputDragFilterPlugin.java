package terrapeer.vui.j3dui.control.inputs;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
An abstract base class for input drag filter plugins.

@author Jon Barrilleaux,
copyright (c) 1999-2000 Jon Barrilleaux,
All Rights Reserved.
*/

public abstract class InputDragFilterPlugin {
	
	// public interface =========================================

	/**
	Constructs a InputDragFilterPlugin.
	*/
	public InputDragFilterPlugin() {}

	/**
	Returns a string naming this plugin.
	@return Plugin type string.
	*/
	public String toString() {
		return "InputDragFilterPlugin";
	}
			
	// personal body ============================================

	/**
	Override for notification of drag start.  Called before the
	first toTargetValue() call of the drag.
	@param source Source display canvas.  Never null.
	@param value Source value.
	*/
	protected void startInputDrag(Canvas3D source,
	 Vector2d value) {}

	/**
	Override for notification of drag stop.  Called after the
	last toTargetValue() call of the drag.
	@param source Source display canvas.  Never null.
	@param value Source value.
	*/
	protected void stopInputDrag(Canvas3D source,
	 Vector2d value) {}

	/**
	Performs the filter operation.
	@param source Source display canvas.  Never null.
	@param value Source value.
	@param copy Container for the copied return value.
	@return Reference to copy.
	*/
	protected abstract Vector2d toTargetValue(Canvas3D source,
	 Vector2d value, Vector2d copy);
	
}
