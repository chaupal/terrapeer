package terrapeer.vui.j3dui.control.mappers.intuitive;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
A source drag filter plugin that interprets drags with a
relative drag origin (i.e. the origin of the drag operation
is the drag position at which the drag started).

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class RelativeSourceDragPlugin
 extends SourceDragFilterPlugin {
	
	// public interface =========================================

	/**
	Constructs an RelativeSourceDragPlugin.
	*/
	public RelativeSourceDragPlugin() {}

	// SourceDragFilterPlugin implementation
	
	public String toString() {
		return "RelativeSourceDragPlugin";
	}
			
	// personal body ============================================
	
	/** The source space drag origin. */
	private Vector3d _origin = new Vector3d(0, 0, 0);

	// SourceDragFilterPlugin implementation
	
	protected Node startSourceDrag(Node source,
	 Vector3d pos, Vector3d copy) {
	 	if(source!=null) {
			_origin.set(pos);
			copy.sub(pos, _origin);
	 	}
	 	
	 	return source;
	}
	
	protected Node doSourceDrag(Node source,
	 Vector3d pos, Vector3d copy) {
	 	if(source!=null) {
			copy.sub(pos, _origin);
	 	}
	 	
	 	return source;
	}
	
	protected Node stopSourceDrag(Node source,
	 Vector3d pos, Vector3d copy) {
	 	return doSourceDrag(source, pos, copy);
	}

	// SourceDragFilterPlugin implementation
	
	protected void startInputDrag(Node source,
	 Vector3d value) {
	 
		_origin.set(value);
	}
	
	protected Vector3d toTargetValue(Node source,
	 Vector3d value, Vector3d copy) {
		
		copy.sub(value, _origin);
 
if(Debug.getEnabled()){
Debug.println("RelativeSourceDragPlugin",
"RELATIVE:RelativeSourceDragPlugin:toTargetValue:" +
" inVal=" + value +
" origin=" + _origin +
" outVal=" + copy +
" source=" + source);}

		return copy;
	}
	
}