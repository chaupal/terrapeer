package terrapeer.vui.j3dui.visualize;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.utils.app.*;
import terrapeer.vui.j3dui.control.actuators.*;
import terrapeer.vui.j3dui.control.actuators.groups.*;
import terrapeer.vui.j3dui.visualize.change.*;

/**
This class is an actuator group that helps supports "display
overlay" visualization behavior.  It together with a
DisplayOverlayRoot defines a 2D space parallel to the
view display plane where objects are positioned in X-Y in
units of pixels relative to this groups origin.  Objects are
placed in separate "overlay planes" that define their Z overlay
order, with "0" as the base plane, +Z in front towards the eye,
and -Z in back away from the eye.
<P>
Use this group's setRelativeOrigin() to position its origin in
the display space relative to the display origin (center of
display window); and, use its setAbsoluteOffset() to offset its
children relative to this group's origin.  The former setting
allows this group to maintain its relative position in the
display window in spite of resizing, and the latter specifies
the point of reference in the child node space that will
maintain this relative display position, such as for widgets
in a dashboard.
<P>
This group attaches itself directly to a host display overlay
root.  To place an object in display overlay the object must be
added as a child of a DisplayOverlayGroup, which in turn is a
child of a DisplayOverlayRoot.  Non-planar child objects may
cause unpredictable results.
<P>
For this object to work properly all of its event inputs must
be connected and they must have received initial events.
<UL>
<LI>ViewChangeTarget: Internal state of the view
observing this node.  External state is ignored.  Only works
reliably for one view.
</UL>
<P>
Having to use separate nodes for the overlay "root" and "group"
is a kludge necessitated by a bug in Java 3D 1.1.2 where
overlapping transparent objects, such as those using transparent
textures, will only render correctly if 1) they are under an
OrderedGroup in proper render order and 2) they are in correct
Z position relative to the eye for correct overlap.

@author Jon Barrilleaux,
copyright (c) 1999-2000 Jon Barrilleaux,
All Rights Reserved.
*/

public class DisplayOverlayGroup extends ActuatorGroup
 implements ViewChangeTarget {
 
	// public interface =========================================

	/**
	Constructs a DisplayOverlayGroup.
	@param root Host display overlay root.  Never null.
	*/
	public DisplayOverlayGroup(DisplayOverlayRoot root) {
		this(root, null);
	}

	/**
	Constructs a DisplayOverlayGroup with a child node.
	@param root Host display overlay root.  Never null.
	@param node First child node of this group.  Null if none.
	*/
	public DisplayOverlayGroup(DisplayOverlayRoot root,
	 Node node) {
		// empty chain, have to use overrides
		super(0, null);
	
		// kludge so transparent textures render OK
		_planeRoot = new OrderedGroup();
		this.addChild(_planeRoot);		

		// build Z planes
		int planeCount = _core.getPlaneCount();
		_planes = new CenterAffineGroup[planeCount];
		
		for(int planeI=0; planeI<planeCount; planeI++) {
		 	_planes[planeI] = new CenterAffineGroup();
			_planeRoot.addChild(_planes[planeI]);
		}
		
		// build overlay core
		_core = new DisplayOverlayCore();

		// attach this to root, add child node		
		if(root==null) throw new
		 IllegalArgumentException("<root> is null.");
		
		root.addNode(this);
		addNode(node);
	}
	
	/**
	Sets the X-Y position of this group's origin in normalized
	display overlay space (i.e. that of the display overlay
	root).  The position is expressed as a fraction of the
	display size (not pixels) and is relative to the display
	space origin (center of display window).
	@param origin  Position of origin as a fraction of each
	display dimension (i.e. 0.25 is 25% of the width or height
	of the display window).
	@return Reference to offset.
	*/
	public Tuple2d setRelativeOrigin(Tuple2d origin) {
		_origin.set(origin);
	
if(Debug.getEnabled()){
Debug.println("DisplayOverlayGroup",
"OFFSET:DisplayOverlayGroup.setRelativeOrigin:" +
" origin=" + _origin);}

		updatePlanes();
		return origin;
	}
	
	/**
	Gets the relative position of this group's origin
	relative to that of its host display space.  The offset
	is expressed as a fraction of the display window size.
	@return Reference to the relative origin position.
	*/
	public Tuple2d getRelativeOrigin() {
		return _origin;
	}
	
	/**
	Gets the absolute position of this group's origin relative
	to that of its host display space, expressed in pixels.
	@param origin Return copy of the absolute origin position.
	@return Reference to position.
	*/
	public Tuple2d getAbsoluteOrigin(Tuple2d origin) {
		origin.set(_ds.x*_origin.x, _ds.y*_origin.y);
		return origin;
	}
	
	/**
	Sets the X-Y offset of this group's child nodes in
	normalized display overlay space (i.e. that of the display
	overlay root) relative to this group's origin.  The offset
	is absolute and is expressed in display pixels.
	@param offset  Offset of the child nodes.
	@return Reference to offset.
	*/
	public Tuple2d setAbsoluteOffset(Tuple2d offset) {
		_offset.set(offset);
	
if(Debug.getEnabled()){
Debug.println("DisplayOverlayGroup",
"OFFSET:DisplayOverlayGroup.setAbsoluteOffset:" +
" offset=" + _offset);}

		updatePlanes();
		return offset;
	}
	
	/**
	Gets the absolute offset of this group's children relative
	to its origin, expressed in pixels.
	@param offset Return copy of the absolute children offset.
	@return Reference to offset.
	*/
	public Tuple2d getAbsoluteOffset(Tuple2d offset) {
		offset.set(_offset);
		return offset;
	}
	
	/**
	Adds the specified node to the specified Z plane.
	@param node Node added to the chain tail.  Ignored if null.
	@param zPlane Z-plane that the child will be added to.  If
	out of range uses the closest plane. Z_PLANE_MIN <= zPos
	<= Z_PLANE_MAX.
	@return Reference to child node.
	*/
	public Node addNode(Node node, int zPlane) {
		if(node==null) return null;
		int planeI = _core.getArrayIndex(zPlane);
	
if(Debug.getEnabled()){
Debug.println("DisplayOverlayGroup",
"CHILD:DisplayOverlayGroup.addNode:" +
" zPlane=" + zPlane +
" planeI=" + planeI +
" node=" + node);}

		_planes[planeI].addNode(node);
		return node;
	}
	
	/**
	Adds the specified node to the base Z plane (Z=0).
	@param node Node added to the chain tail.  Ignored if null.
	@return Reference to child node.
	*/
	public Node addNode(Node node) {
		if(node==null) return null;
			
		getTail().addChild(node);
		return node;
	}
	
	/**
	Gets the tail group in the chain, which is the tail node
	in the base Z plane (Z=0).
	@return Tail group.  Never null.
	*/
	public Group getTail() {
		int planeI = _core.getArrayIndex(0);
		return _planes[planeI].getTail();
	}

	// ViewChangeTarget implementation
	
	public void setViewExternal(View source, Point3d pos,
	 Transform3D xform) {
		// do nothing
	}
	
	public void setViewInternal(View source, double fov,
	 double vsf, double dsf, Vector2d dvo, Vector2d ds,
	 Vector2d ss, Vector2d sr) {
	
if(Debug.getEnabled()){
Debug.println("DisplayOverlayGroup",
"VIEW:DisplayOverlayGroup.setViewExternal:" +
" source=" + source +
" fov=" + fov +
" vsf=" + vsf +
" dsf=" + dsf +
" dvo=" + dvo +
" ds=" + ds +
" ss=" + sr);}

		_ds.set(ds);
	
		_core.setViewInternal(source, fov, vsf, dsf, dvo, ds,
	 	 ss, sr);

		updatePlanes();
	}
			
	// personal body ============================================
	
	/** Display overlay core. Never null. */
	private DisplayOverlayCore _core;
	
	/** Root for Z-plane groups. Never null. */
	private Group _planeRoot;
	
	/** Z-plane groups. Never null. */
	private CenterAffineGroup _planes[];
	
	/** Display size in pixels. Never null. */
	private Vector2d _ds = new Vector2d();
	
	/** Translation of this in disp size fraction. Never null. */
	private Vector2d _origin = new Vector2d(0, 0);
	
	/** Translation of children in pixels. Never null. */
	private Vector2d _offset = new Vector2d(0, 0);
	
	/** Dummy vector.  (for GC) */
	private Vector2d _vector2D = new Vector2d();
	
	/** Dummy actuation.  (for GC) */
	private Vector4d _actuation = new Vector4d();

	/**
	Updates Z position and constant size scale factor in each
	of the Z planes.
	*/	
	protected void updatePlanes() {
		CenterAffineGroup affine;
		
		// get absolute position of children
		getAbsoluteOrigin(_vector2D);
		_vector2D.add(_offset);

		// update plane offset and scale by plane
		int planeCount = _core.getPlaneCount();
		for(int planeI=0; planeI<planeCount; planeI++) {
			// get Z plane normal display adjustments
			int zPlane = _core.getOverlayIndex(planeI);
			double dist = _core.getPlaneDist(zPlane);
			double scale = _core.getPlaneScale(dist);
		
if(Debug.getEnabled()){
Debug.println("DisplayOverlayGroup.verbose",
"PLANE:DisplayOverlayGroup.updateScale:" +
" planeI=" + planeI +
" zPlane=" + zPlane +
" offset=" + _vector2D +
" dist=" + dist +
" scale=" + scale);}

			// finish Z plane geometry started by root			
			affine = _planes[planeI];

			/// offset X-Y in disp; Z in world rel to normal
			_actuation.set(_vector2D.x, _vector2D.y,
			 _core.getNormalDist() - dist, 0);
			affine.getTranslation().initActuation(_actuation);

			/// re-normalize X-Y scale, set normal Z scale			
			_actuation.set(scale, scale,
			 scale * _core.getNormalScale(), 0);
			affine.getScale().initActuation(_actuation);
			
			/// scale about display origin, not offset
			_actuation.set(-_vector2D.x, -_vector2D.y, 0, 0);
			affine.getCenter().initActuation(_actuation);
		}
	}

	// ActuatorGroup implementation
	
	protected void setEnableGroup(boolean enable) {
		int planeCount = _core.getPlaneCount();
		for(int planeI=0; planeI<planeCount; planeI++) {
		 	_planes[planeI].setEnable(enable);
		}
	}
}