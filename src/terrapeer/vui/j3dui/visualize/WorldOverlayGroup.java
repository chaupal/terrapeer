package terrapeer.vui.j3dui.visualize;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.utils.app.*;
import terrapeer.vui.j3dui.control.actuators.*;
import terrapeer.vui.j3dui.control.actuators.groups.*;
import terrapeer.vui.j3dui.visualize.change.*;

/**
An actuator group that supports "world overlay"
visualization behavior, where this group will appear to overlay
or underlay other objects in the world.  Will not work with
parallel projection: overlay occurs but the object is the wrong
size.
<P>
For this group to work properly all of its event inputs must
be connected and they must have received initial events.
<UL>
<LI>ViewChangeTarget: External state of the view
observing this node.  Internal state is ignored.  Only works
reliably for one view.
</UL>
A node external change sensor is automatically created for
this group.   

@author Jon Barrilleaux,
copyright (c) 1999-2000 Jon Barrilleaux,
All Rights Reserved.
*/

public class WorldOverlayGroup extends ActuatorTransformGroup
 implements ViewChangeTarget {
	
	// public interface =========================================

	/**
	Constructs an WorldOverlayGroup.
	*/
	public WorldOverlayGroup() {
		this(null);
	}

	/**
	Constructs a WorldOverlayGroup with a child node.
	@param node First child node of this group.  Null if none.
	*/
	public WorldOverlayGroup(Node node) {
		super(3, node);

		// build chain actuators		
		_center = new Actuator(new SVTransformGroupPlugin(
		 new TranslationPlugin(getTransform(0))));
		 
		_scale = new Actuator(new SVTransformGroupPlugin(
		 new ScalePlugin(getTransform(1))));
		
		_centerNeg = new Actuator(new SVTransformGroupPlugin(
		 new NegTranslationPlugin(getTransform(2))));

		// build center splitter
		_centerSplit = new ActuationSplitter();
		_centerSplit.addEventTarget(_center);
		_centerSplit.addEventTarget(_centerNeg);

		// build node change sensor		
		_sensorTarget = new NodeExternalChangeTarget() {
			public void setNodeExternal(Node source,
			 Point3d pos, Transform3D xform) {

				_thisPos.set(pos);
				Visualize.getOrientation(xform, _thisNegRot,
				 null, null);
				_thisNegRot.angle *= -1;
	
if(Debug.getEnabled()){
Debug.println("WorldOverlayGroup",
"NODE:WorldOverlayGroup.setNodeMove:" +
" thisPos=" + _thisPos +
" thisNegRot=" + _thisNegRot);}

				updateCenter();
			}
		};
		
		_sensor = new NodeExternalChangeSensor(_sensorTarget,
		 this);
	}

	/**
	Constructs a WorldOverlayGroup with a child node and the
	specified geometry.
	@param overlay Overlay scale factor.  <1 for overlay, >1
	for underlay, and 1 for normal.
	*/
    public WorldOverlayGroup(Node node, double overlay) {
     	this(node);
     	setOverlay(overlay);
	}

	/**
	Gets the node change sensor that monitors external change
	to this group.
	@return Node change sensor.  Never null.
	*/
	public NodeExternalChangeSensor getSensor() {
		return _sensor;
	}

	/**
	Sets the overlay scale factor, which determines how close
	(overlay) or far (underlay) the child objects are from the
	display.  The scale factor is applied to the distance
	between the view and this group.
	@param overlay Overlay scale factor.  <1 for overlay, >1
	for underlay, and 1 for normal.
	@return Overlay scale factor.
	*/
	public double setOverlay(double overlay) {
		_actuation.set(overlay, overlay, overlay, 0);
		_scale.initActuation(_actuation);
		return overlay;
	}

	// ViewChangeTarget implementation
	
	public void setViewExternal(View source, Point3d pos,
	 Transform3D xform) {
	
		_viewPos.set(pos);
	
if(Debug.getEnabled()){
Debug.println("WorldOverlayGroup",
"VIEW:WorldOverlayGroup.setViewExternal:" +
" viewPos=" + _viewPos);}

		updateCenter();
	}
	
	public void setViewInternal(View source, double fov,
	 double vsf, double dsf, Vector2d dvo, Vector2d ds,
	 Vector2d ss, Vector2d sr) {
	 	// do nothing
	}
			
	// personal body ============================================
	
	/** Scale actuator. Never null. */
	private Actuator _scale;
	
	/** Center actuator. Never null. */
	private Actuator _center;
	
	/** Negative center actuator. Never null. */
	private Actuator _centerNeg;
	
	/** Center splitter. Never null. */
	private ActuationSplitter _centerSplit;
	
	/** Internal node change sensor. Never null. */
	private NodeExternalChangeSensor _sensor;
	
	/** Internal node change sensor target. Never null. */
	private NodeExternalChangeTarget _sensorTarget;
	
	/** View position. */
	private Point3d _viewPos = new Point3d();
	
	/** This group position. */
	private Point3d _thisPos = new Point3d();
	
	/** This group negative rotation. */
	private AxisAngle4d _thisNegRot = new AxisAngle4d();
	
	/** Dummy point.  (for GC) */
	private Point3d _point = new Point3d();
	
	/** Dummy transform.  (for GC) */
	private Transform3D _xform = new Transform3D();
	
	/** Dummy actuation.  (for GC) */
	private Vector4d _actuation = new Vector4d();

	/**
	Updates this group's center position based on configuration
	parameters and the current view and node geometry.
	*/	
	protected void updateCenter() {
		// move center to view relative to this
		_point.sub(_viewPos, _thisPos);
		_xform.set(_thisNegRot);
		_xform.transform(_point, _point);
	
if(Debug.getEnabled()){
Debug.println("WorldOverlayGroup.verbose",
"CENTER:WorldOverlayGroup.updateCenter:" +
" thisPos=" + _thisPos + 
" viewPos=" + _viewPos +
" cntrPos=" + _point);}

		_actuation.set(_point.x, _point.y, _point.z, 0);
		_centerSplit.initActuation(_actuation);
	}

	// ActuatorGroup implementation
	
	protected void setEnableGroup(boolean enable) {
		_scale.setEnable(enable);
		_centerSplit.setEnable(enable);
	}
	
}