package terrapeer.vui.j3dui.control.inputs;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
A input drag filter plugin that interprets drags with a
relative drag origin (i.e. the origin of the drag operation
is the drag position at which the drag started).

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class RelativeInputDragPlugin
 extends InputDragFilterPlugin {
	
	// public interface =========================================

	/**
	Constructs an RelativeInputDragPlugin.
	*/
	public RelativeInputDragPlugin() {}

	// InputDragFilterPlugin implementation
	
	public String toString() {
		return "RelativeInputDragPlugin";
	}
			
	// personal body ============================================
	
	/** The input drag origin. */
	private Vector2d _origin = new Vector2d();

	// InputDragFilterPlugin implementation
	
	protected void startInputDrag(Canvas3D source,
	 Vector2d value) {
	 
		_origin.set(value);
	}
	
	protected Vector2d toTargetValue(Canvas3D source,
	 Vector2d value, Vector2d copy) {
		
		copy.sub(value, _origin);
 
if(Debug.getEnabled()){
Debug.println("RelativeInputDragPlugin",
"RELATIVE:RelativeInputDragPlugin:toTargetValue:" +
" inVal=" + value +
" origin=" + _origin +
" outVal=" + copy +
" source=" + source);}

		return copy;
	}
	
}