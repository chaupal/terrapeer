package terrapeer.vui.j3dui.visualize;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.utils.app.*;
import terrapeer.vui.j3dui.control.actuators.*;
import terrapeer.vui.j3dui.control.actuators.groups.*;
import terrapeer.vui.j3dui.visualize.change.*;

/**
An actuator group that supports "constant size"
visualization behavior, where this group is scaled to always
appear at the same size on the display screen (not display
window).
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

public class ConstantSizeGroup extends ActuatorTransformGroup
 implements ViewChangeTarget {
	
	// public interface =========================================

	/**
	Constructs a ConstantSizeGroup.
	*/
	public ConstantSizeGroup() {
		this(null);
	}

	/**
	Constructs a ConstantSizeGroup with a child node.
	@param node First child node of this group.  Null if none.
	*/
	public ConstantSizeGroup(Node node) {
		super(1, node);
		 
		// build chain actuators		
		_scale = new Actuator(new SVTransformGroupPlugin(
		 new ScalePlugin(getTransform(0))));
		 
		// build node change sensor		
		_sensorTarget = new NodeExternalChangeTarget() {
			public void setNodeExternal(Node source,
			 Point3d pos, Transform3D xform) {
			
				_thisPos.set(pos);
				_thisScl = xform.getScale();
	
if(Debug.getEnabled()){
Debug.println("ConstantSizeGroup",
"NODE:ConstantSizeGroup.setNodeMove:" +
" thisPos=" + _thisPos);}

				updateScale();
			}
		};
		
		_sensor = new NodeExternalChangeSensor(_sensorTarget,
		 this);
	}

	/**
	Constructs a ConstantSizeGroup with a child node and the
	specified geometry.
	@param size Constant size factor.  The apparent size of a
	unit-sized target expressed as a fraction of screen size
	in meters.  Undefined if <=0.
	*/
    public ConstantSizeGroup(Node node, double size) {
     	this(node);
     	setSize(size);
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
	Sets the constant size scale factor, which determines the
	apparent size of the child objects on the display screen
	(not display window).  A size of 1 means that a 1x1
	rectangle centered about the view -Z axis will appear to
	fill the screen horizontally regardless of distance or
	field of view (FOV).
	@param size Constant size factor.  The apparent size of a
	unit-sized target expressed as a fraction of screen size
	in meters.  Undefined if <=0.
	@return Constant size factor.
	*/
	public double setSize(double size) {
		_size = size;
		updateScale();
		return size;
	}

	// ViewChangeTarget implementation
	
	public void setViewExternal(View source, Point3d pos,
	 Transform3D xform) {

		_viewPos.set(pos);
		Visualize.getOrientation(xform, null, _viewDir, null);
	
if(Debug.getEnabled()){
Debug.println("ConstantSizeGroup",
"VIEW:ConstantSizeGroup.setViewExternal:" +
" viewPos=" + _viewPos + 
" viewDir=" + _viewDir);}

		updateScale();
	}
	
	public void setViewInternal(View source, double fov,
	 double vsf, double dsf, Vector2d dvo, Vector2d ds,
	 Vector2d ss, Vector2d sr) {
	
		_scrnScl= (ss.x / sr.x) / vsf;
		_isParallel = (fov==0.0) ? true : false;
	
if(Debug.getEnabled()){
Debug.println("ConstantSizeGroup",
"VIEW:ConstantSizeGroup.setViewInternal:" +
" scrnScl=" + _scrnScl +
" scrnSiz=" + ss.x/sr.x +
" vsf=" + vsf +
" isParallel=" + _isParallel);}

		updateScale();
	}
			
	// personal body ============================================
	
	/** Scale actuator. Never null. */
	private Actuator _scale;
	
	/** Internal node change sensor. Never null. */
	private NodeExternalChangeSensor _sensor;
	
	/** Internal node change sensor target. Never null. */
	private NodeExternalChangeTarget _sensorTarget;
	
	/** Constant size factor. */
	private double _size = 0.1;
	
	/** This group position. */
	private Point3d _thisPos = new Point3d();
	
	/** This group scale factor. */
	private double _thisScl;
	
	/** View position. */
	private Point3d _viewPos = new Point3d();
	
	/** View direction. */
	private Vector3d _viewDir = new Vector3d();
	
	/** Screen scale factor (screen_size / VSF). */
	private double _scrnScl;
	
	/** True if parallel projection. */
	private boolean _isParallel;
	
	/** Dummy vector.  (for GC) */
	private Vector3d _vector = new Vector3d();
	
	/** Dummy actuation.  (for GC) */
	private Vector4d _actuation = new Vector4d();
	
	/** Dummy transform.  (for GC) */
	private Transform3D _xform = new Transform3D();

	/**
	Updates this group's scale factor based on configuration
	parameters and the current view and node geometry.
	*/	
	protected void updateScale() {
		// compute distance from this to display plane
		_vector.sub(_thisPos, _viewPos);
		double dist = _vector.dot(_viewDir);

		// compute and set scale factor
		double scale = 1.0;
		
		if(_isParallel) {
			scale = (_size * _scrnScl) / _thisScl;
		} else {
			scale = dist * (_size * _scrnScl) / _thisScl;
		}
		
if(Debug.getEnabled()){
Debug.println("ConstantSizeGroup.verbose",
"SIZE:ConstantSizeGroup.updateScale:" +
" thisPos=" + _thisPos + 
" viewPos=" + _viewPos + 
" viewDir=" + _viewDir + 
" dist=" + dist +
" size=" + _size +
" scrnScl=" + _scrnScl +
" thisScl=" + _thisScl +
" scale=" + scale);}

		if(scale > 0.0) {		
			_actuation.set(scale, scale, scale, 0);
			_scale.initActuation(_actuation);
		}
	}

	// ActuatorGroup implementation
	
	protected void setEnableGroup(boolean enable) {
		_scale.setEnable(enable);
	}
	
}