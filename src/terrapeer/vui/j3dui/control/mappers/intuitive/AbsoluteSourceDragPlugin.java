package terrapeer.vui.j3dui.control.mappers.intuitive;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
A source drag filter plugin that interprets drags with an
absolute drag origin (i.e. the origin of the drag operation
is specified and fixed in the source drag space).

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class AbsoluteSourceDragPlugin
 extends SourceDragFilterPlugin {
	
	// public interface =========================================

	/**
	Constructs an AbsoluteSourceDragPlugin with absolute drag
	origin <origin>.
	@param origin Absolute drag origin.
	*/
	public AbsoluteSourceDragPlugin(Vector3d origin) {
		setOrigin(origin);
	}

	/**
	Sets the absolute drag origin in the source space.
	@param origin Absolute drag origin.
	@return Reference to absolute drag origin.
	*/
	public Vector3d setOrigin(Vector3d origin) {
		_origin.set(origin);
		return _origin;
	}

	/**
	Gets the absolute drag origin in the source space.
	@return Reference to absolute drag origin.
	*/
	public Vector3d getOrigin() {
		return _origin;
	}

	// SourceDragFilterPlugin implementation
	
	public String toString() {
		return "AbsoluteSourceDragPlugin";
	}
			
	// personal body ============================================
	
	/** The source space drag origin. */
	private Vector3d _origin = new Vector3d();

	// SourceDragFilterPlugin implementation

	protected Vector3d toTargetValue(Node source,
	 Vector3d value, Vector3d copy) {
		
		copy.sub(value, _origin);
 
if(Debug.getEnabled()){
Debug.println("AbsoluteSourceDragPlugin",
"ABSOLUTE:AbsoluteSourceDragPlugin:toTargetValue:" +
" inVal=" + value +
" origin=" + _origin +
" outVal=" + copy +
" source=" + source);}

		return copy;
	}
	
}