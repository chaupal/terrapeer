package terrapeer.vui.j3dui.control.inputs;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
An input drag filter plugin that interprets the drag with an
absolute drag origin (i.e. the origin of the drag operation
is specified and fixed in the source drag space).

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class AbsoluteInputDragPlugin
 extends InputDragFilterPlugin {
	
	// public interface =========================================

	/**
	Constructs an AbsoluteInputDragPlugin.
	*/
	public AbsoluteInputDragPlugin() {}

	/**
	Constructs an AbsoluteInputDragPlugin with absolute drag
	origin <origin>.
	@param origin Absolute drag origin.
	*/
	public AbsoluteInputDragPlugin(Vector2d origin) {
		setOrigin(origin);
	}

	/**
	Sets the absolute drag origin in the display space.
	@param origin Absolute drag origin.
	@return Reference to absolute drag origin.
	*/
	public Vector2d setOrigin(Vector2d origin) {
		_origin.set(origin);
		return _origin;
	}

	/**
	Gets the absolute drag origin in the display space.
	@return Reference to absolute drag origin.
	*/
	public Vector2d getOrigin() {
		return _origin;
	}

	// InputDragFilterPlugin implementation
	
	public String toString() {
		return "AbsoluteInputDragPlugin";
	}
			
	// personal body ============================================
	
	/** The input space drag origin. */
	private Vector2d _origin = new Vector2d();

	// InputDragFilterPlugin implementation

	protected Vector2d toTargetValue(Canvas3D source,
	 Vector2d value, Vector2d copy) {
		
		copy.sub(value, _origin);
 
if(Debug.getEnabled()){
Debug.println("AbsoluteInputDragPlugin",
"ABSOLUTE:AbsoluteInputDragPlugin:toTargetValue:" +
" inVal=" + value +
" origin=" + _origin +
" outVal=" + copy +
" source=" + source);}

		return copy;
	}
	
}