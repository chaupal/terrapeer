package terrapeer.vui.j3dui.control.mappers.intuitive;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
An abstract base class for source drag filter plugins.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public abstract class SourceDragFilterPlugin {
	
	// public interface =========================================

	/**
	Constructs a SourceDragFilterPlugin.
	*/
	public SourceDragFilterPlugin() {}

	/**
	Returns a string naming this plugin.
	@return Plugin type string.
	*/
	public String toString() {
		return "SourceDragFilterPlugin";
	}
			
	// personal body ============================================

	/**
	Override for notification of drag start.  Called before
	toTargetValue().
	@param source. Source node.  Null if input value is invalid.  
	@param value Source value.
	*/
	protected void startInputDrag(Node source,
	 Vector3d value) {}

	/**
	Performs the filter operation.
	@param source. Source node.  Null if input value is invalid.  
	@param value Source value.
	@param copy Container for the copied return value.
	@return Reference to copy.
	*/
	protected abstract Vector3d toTargetValue(Node source,
	 Vector3d value, Vector3d copy);
	
}