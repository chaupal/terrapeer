package terrapeer.vui.j3dui.control.actuators;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
An translation plugin that performs negative translation.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class NegTranslationPlugin extends TranslationPlugin {
	
	// public interface =========================================

	/**
	Constructs a NegTranslationPlugin with target node <target>.
	@param target Target node.  Never null.
	*/
	public NegTranslationPlugin(TransformGroup target) {
		super(target);
	}

	// TGGeometryPlugin implementation
	
	public String toString() {
		return "NegTranslation";
	}
			
	// personal body ============================================
	
	/** Dummy translation value. (for speed) */
	private final Vector3d _xlate = new Vector3d();

	// TGGeometryPlugin implementation
	
	protected Transform3D toActuationTransform(Tuple4d value,
	 Transform3D copy) {
	 	fromActuation(value, _xlate);
	 	_xlate.negate();
		copy.set(_xlate);
		return copy;
	}
	
	protected Tuple4d toActuationDelta(Tuple4d newValue,
	 Vector4d oldValue, Vector4d copy) {
		copy.sub(newValue, oldValue);
		copy.w = 0;
		return copy;
	}
	
}