package terrapeer.vui.j3dui.control.mappers.intuitive;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.mappers.*;

/**
An abstract base class for intuitive drag mapper plugins. 
<P>
By definition, input to source space transformation is
point not vector-based, which means that position is mapped
instead of direction and magnitude.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public abstract class IntuitiveDragMapperPlugin {
	
	// public interface =========================================

	/**
	Constructs an IntuitiveDragMapperPlugin.
	*/
	public IntuitiveDragMapperPlugin() {}

	/**
	Returns a string naming this plugin.
	@return Plugin type string.
	*/
	public String toString() {
		return "IntuitiveDragMapperPlugin";
	}

	/**
	Returns a node defining the current reference source space.
	Depending on how the source space is defined and the type
	of plugin this may change each time the cursor moves and a
	new source node is picked.
	@return Reference to the source space node.  If null the
	source space is the world space.
	*/
	public abstract Node getSourceSpace();
			
	// personal body ============================================
	
	/**
	Transforms the starting input drag position into a source 
	space drag position defined relative to the reference source
	space.
	@param source Source display canvas.
	@param pos Input display position.
	@param copy Container for the copied source space return
	value.
	@return True if the return position is valid.
	*/
	protected abstract boolean startInputDrag(Canvas3D source,
	 Vector2d pos, Vector3d copy);
	
	/**
	Transforms the continuing input drag position into a source
	space drag position defined relative to the reference source
	space.
	@param source Source display canvas.
	@param pos Input display position.
	@param copy Container for the copied source space return
	value.
	@return True if the return position is valid.
	*/
	protected abstract boolean doInputDrag(Canvas3D source,
	 Vector2d pos, Vector3d copy);
	
	/**
	Transforms the stopping input drag position into a source
	space drag position defined relative the reference source
	space.
	@param source Source display canvas.
	@param pos Input display position.
	@param copy Container for the copied source space return
	value.
	@return True if the return position is valid.
	*/
	protected abstract boolean stopInputDrag(Canvas3D source,
	 Vector2d pos, Vector3d copy);
	
}