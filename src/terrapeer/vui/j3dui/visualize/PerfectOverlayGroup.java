package terrapeer.vui.j3dui.visualize;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.utils.app.*;
import terrapeer.vui.j3dui.control.actuators.*;
import terrapeer.vui.j3dui.control.actuators.groups.*;
import terrapeer.vui.j3dui.visualize.change.*;

/**
An actuator group that supports "perfect overlay"
visualization behavior by selectively combining the other
world visualization groups.  The order in which the groups
are combined, from head to tail, are: world overlay,
constant size, display facing. 
<P>
For this group to work properly all of its event inputs must
be connected and they must have received initial events.
<UL>
<LI>ViewChangeTarget: External and internal state of the
view observing this node.  Only works reliably for one view.
</UL>
A node external change sensor is automatically created for
this group.   

@author Jon Barrilleaux,
copyright (c) 1999-2000 Jon Barrilleaux,
All Rights Reserved.
*/

public class PerfectOverlayGroup extends ActuatorGroup
 implements ViewChangeTarget {
	
	// public interface =========================================

	/**
	Constructs a PerfectOverlayGroup with all groups.
	*/
	public PerfectOverlayGroup() {
		this(null, Visualize.DO_ALL);
	}

	/**
	Constructs a PerfectOverlayGroup with a child node and the
	specified groups.
	@param node First child node of this group.  Null if none.
	@param flags Flags specifying the visualization groups to
	include (DO_???).
	*/
	public PerfectOverlayGroup(Node node, int flags) {
		// empty chain
		super(0, null);
	
		// build group chain
		Group[] chain = new Group[3];
		ActuatorGroup parent = null;
		int length = 0;
		
		if((flags & Visualize.DO_OVER) != 0) {
			_over = new WorldOverlayGroup();
			if(parent!=null) parent.addNode(_over);
			chain[length] = parent = _over;
			length++;
		}
		
		if((flags & Visualize.DO_SIZE) != 0) {
			_size = new ConstantSizeGroup();
			if(parent!=null) parent.addNode(_size);
			chain[length] = parent = _size;
			length++;
		}
		
		if((flags & Visualize.DO_FACE) != 0) {
			_face = new DisplayFaceGroup();
			if(parent!=null) parent.addNode(_face);
			chain[length] = parent = _face;
			length++;
		}
		
		setChain(chain, length);
		addNode(node);
	}

	/**
	Constructs a PerfectOverlayGroup with a child node, the
	specified groups, and the specified geometry.
	@param node First child node of this group.  Null if none.
	@param flags Flags specifying the visualization groups to
	include (DO_???).
	@param overlay Overlay scale factor.  <1 for overlay, >1
	for underlay, and 1 for normal.  Ignored if no world overlay
	group.
	@param size Constant size factor.  The apparent size of a
	unit-sized child object expressed as a fraction of screen
	size.  Undefined if <=0.  Ignored if no constant size group.
	*/
    public PerfectOverlayGroup(Node node, int flags,
     double overlay, double size) {

     	this(node, flags);
     	setOverlay(overlay);
     	setSize(size);
	}

	/**
	Sets the overlay scale factor, which determines how close
	(overlay) or far (underlay) the child objects are from the
	display.
	@param overlay Overlay scale factor.  <1 for overlay, >1
	for underlay, and 1 for normal.  Ignored if no world overlay
	group.
	@return Overlay scale factor.
	*/
	public double setOverlay(double overlay) {
		if(_over!=null) _over.setOverlay(overlay);
		return overlay;
	}

	/**
	Sets the constant scale factor, which determines the
	apparent size of the child objects on the display screen
	(not display window).  A size of 1 means that a 1x1
	rectangle centered about the view -Z axis will appear to
	fill the screen horizontally regardless of distance or
	field of view (FOV).
	@param size Constant size factor.  The apparent size of a
	unit-sized child object expressed as a fraction of screen
	size.  Undefined if <=0.  Ignored if no constant size group.
	@return Constant size factor.
	*/
	public double setSize(double size) {
		if(_size!=null) _size.setSize(size);
		return size;
	}
	
	/**
	Sets the display face axis of rotation.  The default is
	none, in which case the object freely rotates about its
	origin.  Ignored if not doing display face.
	@param axis Axis of rotation.  Null if none.
	@return Reference to axis.
	*/
	public Vector3d setAxis(Vector3d axis) {
		if(_face!=null)
			_face.setAxis(axis);
		
		return axis;
	}

	// ViewChangeTarget implementation
	
	public void setViewExternal(View source, Point3d pos,
	 Transform3D xform) {
	
if(Debug.getEnabled()){
Debug.println("PerfectOverlayGroup",
"VIEW:PerfectOverlayGroup.setViewExternal:..." +
" source=" + source +
" pos=" + pos);}
	
		if(_over!=null)
			_over.setViewExternal(source, pos, xform);
		if(_face!=null)
			_face.setViewExternal(source, pos, xform);
		if(_size!=null)
			_size.setViewExternal(source, pos, xform);
	}
	
	public void setViewInternal(View source, double fov,
	 double vsf, double dsf, Vector2d dvo, Vector2d ds,
	 Vector2d ss, Vector2d sr) {
	
if(Debug.getEnabled()){
Debug.println("PerfectOverlayGroup",
"VIEW:PerfectOverlayGroup.setViewInternal:..." +
" source=" + source +
" fov=" + fov);}
	
		if(_size!=null)
			_size.setViewInternal(source, fov, vsf, dsf, dvo,
			 ds, ss, sr);
	}
			
	// personal body ============================================
	
	/** World overlay group.  Null if none. */
	private WorldOverlayGroup _over = null;
	
	/** Display face group.  Null if none. */
	private DisplayFaceGroup _face = null;
	
	/** Constant size group.  Null if none. */
	private ConstantSizeGroup _size = null;
	
	// ActuatorGroup implementation
	
	protected void setEnableGroup(boolean enable) {
	
if(Debug.getEnabled()){
Debug.println("PerfectOverlayGroup.enable",
"ENABLE:PerfectOverlayGroup.setEnableGroup:" +
" enable=" + enable);}
	
		if(_over != null) _over.setEnable(enable);
		if(_face != null) _face.setEnable(enable);
		if(_size != null) _size.setEnable(enable);
	}
	
}