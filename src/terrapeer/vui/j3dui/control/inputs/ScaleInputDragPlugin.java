package terrapeer.vui.j3dui.control.inputs;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.mappers.Mapper;

/**
An input drag filter plugin that scales the drag position
value.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class ScaleInputDragPlugin
 extends InputDragFilterPlugin {
	
	// public interface =========================================

	/**
	Constructs a ScaleInputDragPlugin.
	@param scale Source scaling value.
	*/
	public ScaleInputDragPlugin() {}

	/**
	Constructs a ScaleInputDragPlugin.
	@param scale Source scaling value.
	*/
	public ScaleInputDragPlugin(Vector2d scale) {
		setScale(scale);
	}

	/**
	Sets the scaling factor applied to source values.
	@param scale Source scaling value.
	*/
	public Vector2d setScale(Vector2d scale) {
		_scale.set(scale);
		return _scale;
	}

	/**
	Gets the scaling factor applied to source values.
	@return Source scaling value.
	*/
	public Vector2d getScale() {
		return _scale;
	}

	// InputDragFilterPlugin implementation
	
	public String toString() {
		return "ScaleInputDragPlugin";
	}
			
	// personal body ============================================
	
	/** Scale factor applied to source values. */
	private Vector2d _scale = new Vector2d(1, 1);

	// InputDragFilterPlugin implementation

	protected Vector2d toTargetValue(Canvas3D source,
	 Vector2d value, Vector2d copy) {
		
		copy.set(_scale.x * value.x, _scale.y * value.y);
 
if(Debug.getEnabled()){
Debug.println("ScaleInputDragPlugin",
"SCALE:ScaleInputDragPlugin:toTargetValue:" +
" inVal=" + value +
" scale=" + _scale +
" outVal=" + copy +
" source=" + source);}

		return copy;
	}
	
}