package terrapeer.vui.j3dui.visualize;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.utils.app.*;
import terrapeer.vui.j3dui.control.actuators.*;
import terrapeer.vui.j3dui.control.actuators.groups.*;
import terrapeer.vui.j3dui.visualize.change.*;

/**
Used by display overlay groups to provide common view event
handling and computations for display space normalization
computations.
<P>
For this object to work properly all of its event inputs must
be connected and they must have received an initial event.
<UL>
<LI>ViewChangeTarget: Internal state of the view
observing this node.  External state is ignored.  Only works
reliably for one view.
</UL>

@author Jon Barrilleaux,
copyright (c) 1999-2000 Jon Barrilleaux,
All Rights Reserved.
*/

public class DisplayOverlayCore implements ViewChangeTarget {
 
	// public interface =========================================

	/**
	Converts Z plane overlay index into Z plane array index.
	The array index also corresponds to the render order, with
	0 being the backmost plane and therefore the first plane
	rendered.
	@param zPlane Z-plane overlay index (+Z in front, -Z in
	back).  If out of range uses the closest plane.
	Z_PLANE_MIN <= zPlane <= Z_PLANE_MAX.
	@return Z plane array index.  0 is the back most plane.
	*/
	public static final int getArrayIndex(int zPlane) {
		if(zPlane<Z_PLANE_MIN) zPlane = Z_PLANE_MIN;
		if(zPlane>Z_PLANE_MAX) zPlane = Z_PLANE_MAX;
		
		int planeI = zPlane-Z_PLANE_MIN; // 0 is back-most
		
if(Debug.getEnabled()){
Debug.println("DisplayOverlayCore.verbose",
"INDEX:DisplayOverlayCore.getArrayIndex:" +
" zPlane=" + zPlane +
" planeI=" + planeI +
" planeMin=" + Z_PLANE_MIN +
" planeMax=" + Z_PLANE_MAX +
" planeDel=" + Z_PLANE_DEL);}

		return planeI;
	}

	/**
	Converts Z plane array index into Z plane overlay index .
	The array index also corresponds to the render order, with
	0 being the backmost plane and therefore the first plane
	rendered.
	@param planeI Z plane render/array index.  0 is the backmost
	plane.  If out of range uses the closest plane. 0 <= planeI
	<= Z_PLANE_MAX-Z_PLANE_MIN.
	@return Z-plane overlay index (+Z in front, -Z in back).
	*/
	public static final int getOverlayIndex(int planeI) {
		if(planeI<0) planeI = 0;
		if(planeI>Z_PLANE_MAX-Z_PLANE_MIN) planeI =
		 Z_PLANE_MAX-Z_PLANE_MIN;
		
		int zPlane = planeI+Z_PLANE_MIN; // Z_MIN is backmost
		
if(Debug.getEnabled()){
Debug.println("DisplayOverlayCore.verbose",
"INDEX:DisplayOverlayCore.getOverlayIndex:" +
" zPlane=" + zPlane +
" planeI=" + planeI +
" planeMin=" + Z_PLANE_MIN +
" planeMax=" + Z_PLANE_MAX +
" planeDel=" + Z_PLANE_DEL);}

		return zPlane;
	}

	/**
	Gets the number of display overlay Z planes.
	@return Number of Z planes. >0.
	*/
	public static final int getPlaneCount() {
		return Z_PLANE_MAX-Z_PLANE_MIN+1;
	}

	/**
	Constructs a DisplayOverlayCore with a child node.
	@param node First child node of this group.  Null if none.
	*/
	public DisplayOverlayCore() {}

	// ViewChangeTarget implementation
	
	public void setViewExternal(View source, Point3d pos,
	 Transform3D xform) {
		// do nothing
	}
	
	public void setViewInternal(View source, double fov,
	 double vsf, double dsf, Vector2d dvo, Vector2d ds,
	 Vector2d ss, Vector2d sr) {
	
		_vsf = vsf;
		_dvo.set(dvo);
		_sr.set(sr);
		_isParallel = (fov==0.0) ? true : false;
	
if(Debug.getEnabled()){
Debug.println("DisplayOverlayCore",
"VIEW:DisplayOverlayCore.setViewInternal:" +
" source=" + source +
" vsf=" + _vsf +
" sr=" + _sr +
" isParallel=" + _isParallel);}

	}
			
	// personal body ============================================
	
	/** Index of far Z plane.  <=0 */ 
	private static final int Z_PLANE_MIN = -5; 
	
	/** Index of near Z plane.  >=0 */ 
	private static final int Z_PLANE_MAX = 5; 
	
	/** World space delta between Z planes.  >0 */ 
	private static final double Z_PLANE_DEL = 0.1; 
	
	/** View to display scale factor. */
	private double _vsf;
	
	/** Display to view offset in pixels. */
	private Vector2d _dvo = new Vector2d();
	
	/** Screen resolution in pixels/meter. */
	private Vector2d _sr = new Vector2d();
	
	/** True if parallel projection. */
	private boolean _isParallel;
	
	/**
	Gets the world space distance from the eye to the specified
	display overlay Z plane.
	@param zPlane The target Z plane. 
	@return Distance to the Z plane in world space.
	*/
	protected double getPlaneDist(int zPlane) {
		// ideally, this should account for near clip pos
		double baseDist = 1.0;
		double dist = baseDist - (zPlane * Z_PLANE_DEL);
	
if(Debug.getEnabled()){
Debug.println("DisplayOverlayCore.verbose",
"DisplayOverlayCore.getPlaneDist:" +
" zPlane=" + zPlane +
" baseDist=" + baseDist +
" dist=" + dist);}

		return dist;
	}
	
	/**
	Gets the scale factor relative to the normalized display
	space to re-normalize the display overlay Z plane at the
	specified eye distance.
	@param dist Distance of the target Z plane from the eye,
	such as that returned by getPlaneDist(). >0. 
	@return Z plane scale factor in normalized display space.
	1.0 if dist out of range.
	*/
	protected double getPlaneScale(double dist) {
		double scale;
		if(dist<=0.0) return 1.0;
		
		if(_isParallel) {
			scale = 1.0;
		} else {
			scale = dist;
		}
	
if(Debug.getEnabled()){
Debug.println("DisplayOverlayCore.verbose",
"DisplayOverlayCore.getPlaneScale:" +
" dist=" + dist +
" isParallel=" + _isParallel +
" scale=" + scale);}

		return scale;
	}
	
	/**
	Gets the world space distance from the eye to the normal
	display overlay plane. 
	@return Normal display plane distance.
	*/
	protected double getNormalDist() {
		// eye dist=1 for normal display space
		double dist = 1.0;
	
if(Debug.getEnabled()){
Debug.println("DisplayOverlayCore.verbose",
"DisplayOverlayCore.getNormalDist:" +
" dist=" + dist);}

		return 1.0;
	}
	
	/**
	Gets the world space scale factor for transforming the view
	space into a normalized display space expressed in units of
	pixels.  Use getNormalOffset() to complete the transformation. 
	@return Normalization scale factor in world space.
	*/
	protected double getNormalScale() {
		// eye dist=1 for normal display space
		
		double scale = 1.0 / (_sr.x * _vsf);
	
if(Debug.getEnabled()){
Debug.println("DisplayOverlayCore.verbose",
"DisplayOverlayCore.getNormalScale:" +
" scale=" + scale);}

		return scale;
	}
	
	/**
	Gets the display space offset in pixels needed to position
	the normalized display space relative to the view origin.
	@param offset Return Z plane translation in display pixels.
	Never null.
	@return Reference to offset.
	*/
	protected Tuple2d getNormalOffset(Tuple2d offset) {
		// eye dist=1 for normal display space

		offset.set(-_dvo.x, -_dvo.y);
	
if(Debug.getEnabled()){
Debug.println("DisplayOverlayCore.verbose",
"DisplayOverlayCore.getNormalOffset:" +
" offset=" + offset);}

		return offset;
	}
	
}