package terrapeer.vui.j3dui.control.actuators;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
A geometry plugin that performs geometric rotation.
Rotation occurs in 3D about an arbitrary but fixed axis
defined relative to the target transform.  The rotation
axis defaults to the Y axis.
<P>
Also provides a set of utilities for converting between 3D and
and actuation values.
<P>
The input actuation value is defined as follows:
<UL>
<LI>X - (ignored)
<LI>Y - (ignored)
<LI>Z - (ignored)
<LI>W - Rotation angle value in radians.
</UL>

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class RotationPlugin extends TGGeometryPlugin {

	// public utilities =========================================
	
	/**
	Converts rotation angle value <value> into an actuation
	value.
	@param value Rotation angle value.
	@param copy Container for the copied return value.  Unused
	dimensions are unaffected.
	@return Reference to <copy>.
	*/
	public static Tuple4d toActuation(double value,
	 Tuple4d copy) {
		copy.w = value;
		return copy;
	}
	
	/**
	Converts rotation angle value <value> into an actuation
	value.
	@param value Rotation angle value.
	@return New actuation value.
	*/
	public static Tuple4d toActuation(double value) {
		return toActuation(value, new Vector4d());
	}
	
	/**
	Converts an actuation value <value> into an angle value.
	@param value Actuation value.
	@return Rotation angle.
	*/
	public static double fromActuation(Tuple4d value) {
		return value.w;
	}
	
	/**
	Converts an actuation value <value> and rotation axis <axis>
	into an axis-angle value.
	@param value Actuation value.
	@param copy Container for the copied return value.
	@return Reference to <copy>.
	*/
	public static AxisAngle4d fromActuation(Tuple4d value,
	 Vector3d axis, AxisAngle4d copy) {
		copy.set(axis.x, axis.y, axis.z, value.w);
		return copy;
	}
 
	// public interface =========================================

	/**
	Constructs a RotationPlugin with target node <target>.
	Any existing target node state may be lost.
	@param target Target node.  Never null.
	*/
	public RotationPlugin(TransformGroup target) {
		super(target);
	}

	/**
	Constructs a RotationPlugin with target node <target> and
	rotation axis <axis>.
	Any existing target node state may be lost.
	@param target Target node.  Never null.
	@param axis Axis of rotation.  Never null.
	*/
	public RotationPlugin(TransformGroup target,
	 Vector3d axis) {
		super(target);
		_axis.set(axis);
	}

	// TGGeometryPlugin implementation
	
	public String toString() {
		return "RotationPlugin";
	}
			
	// personal body ============================================
	
	/** Initial actuation value. */
	private final Vector4d _init = new Vector4d(0, 0, 0, 0);
	
	/** Rotation axis. */
	private Vector3d _axis = new Vector3d(0, 1, 0);
	
	/** Dummy rotation value. (for GC) */
	private final AxisAngle4d _rotate = new AxisAngle4d();

	// TGGeometryPlugin implementation
	
	protected Tuple4d getActuationInit() {
		return _init;
	}
	
	protected Transform3D toActuationTransform(Tuple4d value,
	 Transform3D copy) {
		fromActuation(value, _axis, _rotate);
		copy.set(_rotate);
		
if(Debug.getEnabled()){
Debug.println("RotationPlugin",
"GEO:RotationPlugin:toActuationTransform:" +
" in=" + value +
" out=" + _rotate);}
		
		return copy;
	}
	
	protected Tuple4d toActuationUpdate(Tuple4d value,
	 Tuple4d reference, Tuple4d copy) {
		copy.add(value, reference);
		
if(Debug.getEnabled()){
Debug.println("RotationPlugin",
"GEO:RotationPlugin:toActuationUpdate:" +
" in=" + value +
" ref=" + reference +
" out=" + copy);}
		
		return copy;
	}
	
	protected Tuple4d toActuationDelta(Tuple4d newValue,
	 Tuple4d oldValue, Tuple4d copy) {
	 	copy.x = 0;
	 	copy.y = 0;
	 	copy.z = 0;
	 	copy.w = newValue.w - oldValue.w;
		
if(Debug.getEnabled()){
Debug.println("RotationPlugin",
"GEO:RotationPlugin:toActuationDelta:" +
" inNew=" + newValue +
" inOld=" + oldValue +
" out=" + copy);}
		
		return copy;
	}
	
}