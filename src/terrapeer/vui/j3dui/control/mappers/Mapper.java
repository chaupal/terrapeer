package terrapeer.vui.j3dui.control.mappers;

import javax.vecmath.*;
import javax.media.j3d.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.inputs.Input;

/**
Provides mapper related constants and utilities, such as for
picking and spatial transformation.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class Mapper {
 
 	// public constants =========================================

	/** Bit flag for no dimensions. */
	public static final int DIM_NONE = 0x00;
	
	/** Bit flag for X dimension. */
	public static final int DIM_X = 0x01;
	
	/** Bit flag for Y dimension. */
	public static final int DIM_Y = 0x02;
	
	/** Bit flag for Z dimension. */
	public static final int DIM_Z = 0x04;
	
	/** Bit flag for W dimension. */
	public static final int DIM_W = 0x08;
	
	/** Bit flag for Angle. */
	public static final int DIM_ANGLE = 0x10;
	
	/** Bit flags for all dimensions. */
	public static final int DIM_ALL =
	 DIM_X|DIM_Y|DIM_Z|DIM_W|DIM_ANGLE;

	// public utilities =========================================

	/**
	Transforms the input point (position) value from the space
	defined by source node to the space defined by target node.
	The source and target space are arbitrary so this utility
	can be used to go from any one space to another.
	@param input Value to be transformed.  Never null.
	@param source Node defining source space.  Its capability
	bits must be set for ALLOW_LOCAL_TO_VWORLD_READ.  If null
	then the source space is assumed to be the world space.
	@param target Node defining target space.  Its capability
	bits must be set for ALLOW_LOCAL_TO_VWORLD_READ.  If null
	then the source space is assumed to be the world space.
	@param output Container for the copied output value.
	Never null.
	@return Reference to the output value.
	*/
	public static Point3d toTargetSpace(Point3d input,
	 Node source, Node target, Point3d output) {
	 
		if(source!=null && target!=null) {
			source.getLocalToVworld(_sourceXform);
			target.getLocalToVworld(_targetXform);
			_targetXform.invert();
			_targetXform.mul(_sourceXform);
			_targetXform.transform(input, output);
		} else if(source!=null) {
			source.getLocalToVworld(_sourceXform);
			_sourceXform.transform(input, output);
		} else if(target!=null) {
			target.getLocalToVworld(_targetXform);
			_targetXform.invert();
			_targetXform.transform(input, output);
		} else {
			output.set(input);
		}
		
if(Debug.getEnabled() &&
 Debug.hasTag("Mapper.space")) {

	Debug.print(
	"SPACE:Mapper:toTargetSpace:point..." +
	"\n" +
	" input=" + input +
	" output=" + output +
	"\n" +
	" source=" + source +
	" target=" + target);

	if(source!=null && target!=null) {
		source.getLocalToVworld(_xform);
		Debug.print("\n srcXform=...\n" + _xform);
		target.getLocalToVworld(_xform);
		Debug.print("\n trgXform=...\n" + _xform);
		Debug.print("\n xform=...\n" + _targetXform);
	} else if(source!=null) {
		Debug.println("\n srcXform=...\n" + _sourceXform);
	} else if(target!=null) {
		target.getLocalToVworld(_xform);
		Debug.print("\n trgXform=...\n" + _xform);
		Debug.print("\n xform=...\n" + _targetXform);
	}
	
	Debug.print("\n");
} // Debug

		return output;
	}

	/**
	Transforms the input vector (direction) value from the space
	defined by source node to the space defined by target node.
	The source and target space are arbitrary so this utility
	can be used to go from any one space to another.
	@param input Value to be transformed.  Never null.
	@param source Node defining source space.  Its capability
	bits must be set for ALLOW_LOCAL_TO_VWORLD_READ.  If null
	then the source space is assumed to be the world space.
	@param target Node defining target space.  Its capability
	bits must be set for ALLOW_LOCAL_TO_VWORLD_READ.  If null
	then the source space is assumed to be the world space.
	@param output Container for the copied output value.
	Never null.
	@return Reference to the ouput value.
	*/
	public static Vector3d toTargetSpace(Vector3d input,
	 Node source, Node target, Vector3d output) {
	 
		if(source!=null && target!=null) {
			source.getLocalToVworld(_sourceXform);
			target.getLocalToVworld(_targetXform);
			_targetXform.invert();
			_targetXform.mul(_sourceXform);
			_targetXform.transform(input, output);
		} else if(source!=null) {
			source.getLocalToVworld(_sourceXform);
			_sourceXform.transform(input, output);
		} else if(target!=null) {
			target.getLocalToVworld(_targetXform);
			_targetXform.invert();
			_targetXform.transform(input, output);
		} else {
			output.set(input);
		}
		
if(Debug.getEnabled() &&
 Debug.hasTag("Mapper.space")) {

	Debug.print(
	"SPACE:Mapper:toTargetSpace:vector..." +
	"\n" +
	" input=" + input +
	" output=" + output +
	"\n" +
	" source=" + source +
	" target=" + target);

	if(source!=null && target!=null) {
		source.getLocalToVworld(_xform);
		Debug.print("\n srcXform=...\n" + _xform);
		target.getLocalToVworld(_xform);
		Debug.print("\n trgXform=...\n" + _xform);
		Debug.print("\n xform=...\n" + _targetXform);
	} else if(source!=null) {
		Debug.println("\n srcXform=...\n" + _sourceXform);
	} else if(target!=null) {
		target.getLocalToVworld(_xform);
		Debug.print("\n trgXform=...\n" + _xform);
		Debug.print("\n xform=...\n" + _targetXform);
	}
	
	Debug.print("\n");
} // Debug

		return output;
	}
	
	/**
	Comptes and returns the origin and direction of a pick ray
	in world space.  The ray origin is at the center eye in the
	display canvas and its direction is from the eye through
	the pick cursor display position. 
	@param canvas The pick canvas containing the pick eye and
	cursor position.  Never null.
	@param pos Cooked position of pick cursor in canvas. 
	Never null.
	@param rayOrg Copy of the output ray origin.  Never null.
	@param rayDir Copy of the output ray direction.  Never null.
	*/
	public static void buildPickRay(Canvas3D canvas,
	 Tuple2d pos, Point3d rayOrg, Vector3d rayDir) {
 
		// get eye and cursor positions 		
		canvas.getCenterEyeInImagePlate(rayOrg);
		canvas.getPixelLocationInImagePlate(
		 Input.uncookMouseX(pos), Input.uncookMouseY(pos),
		 _point);

		// correct for parallel projection		 
		if(canvas.getView().getProjectionPolicy() ==
		 View.PARALLEL_PROJECTION) {
			_point.x = Input.uncookMouseX(pos);
			_point.y = Input.uncookMouseY(pos);
		}

		// transform to world space 		
		canvas.getImagePlateToVworld(_xform);
		_xform.transform(rayOrg);
		_xform.transform(_point);
		
		// build world ray direction
		rayDir.sub(_point, rayOrg);
		rayDir.normalize();
	}
	
	/**
	Computes and returns a pick plane in world space that
	contains the hit point and whose normal is defined by the
	Z axis in the local space of the hit node.
	@param hitPoint Hit point on the hit node in world space.
	Never null.
	@param hitNode The hit node containing the hit point.  Its
	capability bits must be set for ALLOW_LOCAL_TO_VWORLD_READ.
	If null the hit node is assumed to be in the world space.
	@param plane Copy of the output pick plane coefficients,
	with XYZ defining the normal and W defining the D
	coefficient.  Never null.
	*/
	public static void buildPickPlane(Point3d hitPoint,
	 Node hitNode, Vector4d pickPlane) {
	
		// compute plane normal vector
		if(hitNode == null) {
			_vector.x = _hitNormal.x;
			_vector.y = _hitNormal.y;
			_vector.z = _hitNormal.z;
		} else {
			hitNode.getLocalToVworld(_xform);
			_xform.transform(_hitNormal, _vector);
		}

		// save normal as A, B, C coefficients		
		pickPlane.x = _vector.x;
		pickPlane.y = _vector.y;
		pickPlane.z = _vector.z;
 
		// compute D coefficient
		pickPlane.w = -(_vector.x*hitPoint.x +
		 _vector.y*hitPoint.y + _vector.z*hitPoint.z);  		
	}
	
	/**
	Computes the hit point on a plane as the intersection
	of a pick ray with the plane.
	@param rayOrg Pick ray origin in world space.  Never null.
	@param rayDir Pick ray direction in world space.  Never null.
	@param pickPlane Pick plane coefficients, with XYZ defining
	the normal and W defining the D coefficient.  Never null.
	@param hitPoint If not null, copy of the output hit point on
	the plane in world space.  Only valid if the returned hit
	distance is non-negative.  Never null.
	@return Hit distance from the ray origin to the hit point.
	Equals Double.NEGATIVE_INFINITY if no hit, <0 if hit is
	behind the ray origin, and 0 if at the ray origin.
	*/
	public static double hitPickPlane(Point3d rayOrg,
	 Vector3d rayDir, Vector4d pickPlane, Point3d hitPoint) {

		// compute distance to ray-plane intersection	
		double kDir = pickPlane.x*rayDir.x +
		 pickPlane.y*rayDir.y + pickPlane.z*rayDir.z;
		
		if(kDir==0.0) return Double.NEGATIVE_INFINITY;
		 
		double kOrg = pickPlane.x*rayOrg.x +
		 pickPlane.y*rayOrg.y + pickPlane.z*rayOrg.z +
		 pickPlane.w;
		 
		double dist = -kOrg / kDir;

		// if valid hit, compute hit point		
		if(hitPoint!=null && dist>=0.0) {
			hitPoint.scaleAdd(dist, rayDir, rayOrg);
		}
		
		return dist;
		
	}

	/**
	Searches the entire scene graph path to see if the target
	object is in the scene graph path.  The search starts at
	the path root (Locale) and ends with the leaf shape
	(Object).
	@param path SceneGraphPath to be searched. Null if none.
	@param target Object that might be in the path.  Null if none.
	@returns False if the target is not in the path or if
	the path or target is null.
	*/
	public static boolean isInPath(SceneGraphPath path,
	 Object target) {
		if(path==null || target==null) return false;

if(Debug.getEnabled()){
Debug.println("Mapper.isInPath",
"Mapper.isInPath:" +
" target=" + target + " path=" + path);}

		// check root

if(Debug.getEnabled()){
Debug.println("Mapper.isInPath",
" root=" + path.getLocale());}

		if(path.getLocale() == target) return true;
			
		// check groups
		for(int nodeI=0; nodeI<path.nodeCount(); nodeI++) {

if(Debug.getEnabled()){
Debug.println("Mapper.isInPath",
" group=" + path.getNode(nodeI));}

			if(path.getNode(nodeI) == target) return true ;
		}
		
		// check leaf

if(Debug.getEnabled()){
Debug.println("Mapper.isInPath",
" leaf=" + path.getObject());}

		if(path.getObject() == target) return true;
		
		return false; 
	}

	// personal body ============================================

	/** Hit normal in local space.  Always Z-axis. */	
	private static final Vector3d _hitNormal =
	 new Vector3d(0, 0, 1);

	/** Dummy point.  (for GC) */	
	private static final Point3d _point = new Point3d();

	/** Dummy vector.  (for GC) */	
	private static final Vector3d _vector = new Vector3d();
	
	/** Dummy transform.  (for GC) */	
	private static final Transform3D _xform = new Transform3D();
	
	/** Dummy source transform.  (for GC) */	
	private static final Transform3D _sourceXform =
	 new Transform3D();
	
	/** Dummy target transform.  (for GC) */	
	private static final Transform3D _targetXform =
	 new Transform3D();

}