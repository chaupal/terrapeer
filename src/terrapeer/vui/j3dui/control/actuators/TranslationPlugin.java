package terrapeer.vui.j3dui.control.actuators;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
A geometry plugin that performs geometric translation.
Translation occurs in 3D relative to the target transform
along its major axes.
<P>
Also provides a set of utilities for converting between 3D and
and actuation values.
<P>
The input actuation value is defined as follows:
<UL>
<LI>X - X axis translation value.
<LI>Y - Y axis translation value.
<LI>Z - Z axis translation value.
<LI>W - (ignored)
</UL>

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class TranslationPlugin extends TGGeometryPlugin {

	// public utilities =========================================
	
	/**
	Converts translation value <value> into an actuation value.
	@param value Translation value.
	@param copy Container for the copied return value.  Unused
	dimensions are unaffected.
	@return Reference to <copy>.
	*/
	public static Tuple4d toActuation(Tuple3d value,
	 Tuple4d copy) {
		copy.set(value.x, value.y, value.z, 0);
		return copy;
	}
	
	/**
	Converts translation value <value> into an actuation value.
	@param value Translation value.
	@return New actuation value.
	*/
	public static Tuple4d toActuation(Tuple3d value) {
		return toActuation(value, new Vector4d());
	}
	
	/**
	Converts an actuation value <value> into a translation value.
	@param value Actuation value.
	@param copy Container for the copied return value.
	@return Reference to <copy>.
	*/
	public static Tuple3d fromActuation(Tuple4d value,
	 Tuple3d copy) {
		copy.set(value.x, value.y, value.z);
		return copy;
	}
	
	/**
	Converts an actuation value <value> into a translation value.
	@param value Actuation value.
	@return New translation value.
	*/
	public static Tuple3d fromActuation(Tuple4d value) {
		return fromActuation(value, new Vector3d());
	}
	
	// public interface =========================================

	/**
	Constructs a TranslationPlugin with target node <target>.
	Any existing target node state may be lost.
	@param target Target node.  Never null.
	*/
	public TranslationPlugin(TransformGroup target) {
		super(target);
	}

	// TGGeometryPlugin implementation
	
	public String toString() {
		return "TranslationPlugin";
	}
			
	// personal body ============================================
	
	/** Initial actuation value. */
	private final Vector4d _init = new Vector4d(0, 0, 0, 0);
	
	/** Dummy translation value. (for GC) */
	private final Vector3d _xlate = new Vector3d();

	// TGGeometryPlugin implementation
	
	protected Tuple4d getActuationInit() {
		return _init;
	}
	
	protected Transform3D toActuationTransform(Tuple4d value,
	 Transform3D copy) {
		fromActuation(value, _xlate);
		copy.set(_xlate);
		
if(Debug.getEnabled()){
Debug.println("TranslationPlugin",
"GEO:TranslationPlugin:toActuationTransform:" +
" in=" + value +
" out=\n" + copy);}
		
		return copy;
	}
	
	protected Tuple4d toActuationUpdate(Tuple4d value,
	 Tuple4d reference, Tuple4d copy) {
		copy.add(value, reference);
		
if(Debug.getEnabled()){
Debug.println("TranslationPlugin",
"GEO:TranslationPlugin:toActuationUpdate:" +
" in=" + value +
" ref=" + reference +
" out=" + copy);}
		
		return copy;
	}
	
	protected Tuple4d toActuationDelta(Tuple4d newValue,
	 Tuple4d oldValue, Tuple4d copy) {
		copy.sub(newValue, oldValue);
		copy.w = 0;
		
if(Debug.getEnabled()){
Debug.println("TranslationPlugin",
"GEO:TranslationPlugin:toActuationDelta:" +
" inNew=" + newValue +
" inOld=" + oldValue +
" out=" + copy);}
		
		return copy;
	}
	
}