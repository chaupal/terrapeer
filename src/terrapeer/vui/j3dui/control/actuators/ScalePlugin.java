package terrapeer.vui.j3dui.control.actuators;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
A geometry plugin that performs geometric scaling.  Scaling
occurs in 3D relative to the target transform along its major
axes.
<P>
Also provides a set of utilities for converting between 3D and
and actuation values.
<P>
The input actuation value is defined as follows:
<UL>
<LI>X - X axis scale factor.
<LI>Y - Y axis scale factor.
<LI>Z - Z axis scale factor.
<LI>W - (ignored)
</UL>

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class ScalePlugin extends TGGeometryPlugin {

	// public utilities =========================================
	
	/**
	Converts scale value <value> into an actuation value.
	@param value Scale value.
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
	Converts scale value <value> into an actuation value.
	@param value Scale value.
	@return New actuation value.
	*/
	public static Tuple4d toActuation(Tuple3d value) {
		return toActuation(value, new Vector4d());
	}
	
	/**
	Converts an actuation value <value> into a scale value.
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
	Converts an actuation value <value> into a scale value.
	@param value Actuation value.
	@return New translation value.
	*/
	public static Tuple3d fromActuation(Tuple4d value) {
		return fromActuation(value, new Vector3d());
	}
	
	// public interface =========================================

	/**
	Constructs a ScalePlugin with target node <target>.
	Any existing target node state may be lost.
	@param target Target node.  Never null.
	*/
	public ScalePlugin(TransformGroup target) {
		super(target);
	}

	// TGGeometryPlugin implementation
	
	public String toString() {
		return "ScalePlugin";
	}
			
	// personal body ============================================
	
	/** Initial actuation value. */
	private final Vector4d _init = new Vector4d(1, 1, 1, 0);
	
	/** Dummy scale value. (for GC) */
	private final Vector3d _scale = new Vector3d();

	// TGGeometryPlugin implementation
	
	protected Tuple4d getActuationInit() {
		return _init;
	}
	
	protected Transform3D toActuationTransform(Tuple4d value,
	 Transform3D copy) {
		fromActuation(value, _scale);
		copy.setIdentity(); // why is this needed?
		copy.setScale(_scale);
		
if(Debug.getEnabled()){
Debug.println("ScalePlugin",
"GEO:ScalePlugin:toActuationTransform:" +
" in=" + value +
" out=" + _scale);}
		
		return copy;
	}
	
	protected Tuple4d toActuationUpdate(Tuple4d value,
	 Tuple4d reference, Tuple4d copy) {
	 	copy.x = reference.x * value.x;
	 	copy.y = reference.y * value.y;
	 	copy.z = reference.z * value.z;
		
if(Debug.getEnabled()){
Debug.println("ScalePlugin",
"GEO:ScalePlugin:toActuationUpdate:" +
" in=" + value +
" ref=" + reference +
" out=" + copy);}
		
		return copy;
	}
	
	protected Tuple4d toActuationDelta(Tuple4d newValue,
	 Tuple4d oldValue, Tuple4d copy) {
		copy.x = newValue.x / oldValue.x;
		copy.y = newValue.y / oldValue.y;
		copy.z = newValue.z / oldValue.z;
		copy.w = 0;
		
if(Debug.getEnabled()){
Debug.println("ScalePlugin",
"GEO:ScalePlugin:toActuationDelta:" +
" inNew=" + newValue +
" inOld=" + oldValue +
" out=" + copy);}
		
		return copy;
	}
	
}