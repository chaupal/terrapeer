package terrapeer.vui.j3dui.visualize;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.utils.app.*;
import terrapeer.vui.j3dui.control.actuators.*;
import terrapeer.vui.j3dui.control.actuators.groups.*;
import terrapeer.vui.j3dui.visualize.change.*;

/**
An actuator group that supports "display facing" visualization
behavior, where this group is oriented to always face
towards the view display (not the view eyepoint).
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

public class DisplayFaceGroup extends ActuatorTransformGroup
 implements ViewChangeTarget {
	
	// public interface =========================================

	/**
	Constructs an DisplayFaceGroup.
	*/
	public DisplayFaceGroup() {
		this(null);
	}

	/**
	Constructs a DisplayFaceGroup with a child node.
	@param node First child node of this group.  Null if none.
	*/
	public DisplayFaceGroup(Node node) {
		super(1, node);
		 
		// build chain actuators		
		_rotation = new Actuator(new SMTransformGroupPlugin(
		 new AxisAnglePlugin(getTransform(0))));
		 
		// build node change sensor		
		_sensorTarget = new NodeExternalChangeTarget() {
			public void setNodeExternal(Node source,
			 Point3d pos, Transform3D xform) {

			Visualize.getOrientation(xform, _thisNegRot, null,
			 null);
			_thisNegRot.angle *= -1;

if(Debug.getEnabled()){
Debug.println("DisplayFaceGroup",
"NODE:DisplayFaceGroup.setNodeMove:" +
" thisNegRot=" + _thisNegRot);}

				updateRotation();
			}
		};
		
		_sensor = new NodeExternalChangeSensor(_sensorTarget,
		 this);
	}
	
	/**
	Sets this object's axis of rotation through its origin.  The
	default is none, in which case the object freely rotates
	about its origin.
	@param axis Axis of rotation.  Null if none.
	@return Reference to axis.
	*/
	public Vector3d setAxis(Vector3d axis) {
		if(axis==null) {
			// rotate about origin
			_axis = null;
		} else {
			// rotate about axis through origin
			_axis = new Vector3d(axis);
			_axis.normalize();
			
			// compute axes of plane perpendicular to axis
			if(_axis.x==0 && _axis.y==0 && _axis.z==0) {
				_axisX.set(1, 0, 0);
				_axisY.set(0, 0, -1);
			} else
			if(_axis.x==0 && _axis.y==0 && _axis.z==-1) {
				_axisX.set(1, 0, 0);
				_axisY.set(0, -1, 0);
			} else
			if(_axis.x==0 && _axis.y==0 && _axis.z==1) {
				_axisX.set(1, 0, 0);
				_axisY.set(0, 1, 0);
			} else {
				_vector.set(0, 0, -1);
				_axisX.cross(_vector, _axis);
				_axisX.normalize();
				
				_axisY.cross(_axis, _axisX);
				_axisY.normalize();
			}
		}
		
		updateRotation();
		return axis;
	}
	
	/**
	Gets the node change sensor that monitors external change
	to this group.
	@return Node change sensor.  Never null.
	*/
	public NodeExternalChangeSensor getSensor() {
		return _sensor;
	}

	// ViewChangeTarget implementation
	
	public void setViewExternal(View source, Point3d pos,
	 Transform3D xform) {
	
		_viewXform.set(xform);
	
if(Debug.getEnabled()){
Debug.println("DisplayFaceGroup",
"VIEW:DisplayFaceGroup.setViewExternal:" +
" viewXform=" + _viewXform);}

		updateRotation();
	}
	
	public void setViewInternal(View source, double fov,
	 double vsf, double dsf, Vector2d dvo, Vector2d ds,
	 Vector2d ss, Vector2d sr) {
	 	// do nothing
	}
			
	// personal body ============================================
	
	/** Rotation actuator. Never null. */
	private Actuator _rotation;
	
	/** Internal node change sensor. Never null. */
	private NodeExternalChangeSensor _sensor;
	
	/** Internal node change sensor target. Never null. */
	private NodeExternalChangeTarget _sensorTarget;
	
	/** This group negative rotation in world. */
	private AxisAngle4d _thisNegRot = new AxisAngle4d();
	
	/** This group rotation axis.  Null if none. */
	private Vector3d _axis = null;
	
	/** X axis of plane perpendicular to rotation axis. */
	private Vector3d _axisX = new Vector3d();
	
	/** Y axis of plane perpendicular to rotation axis. */
	private Vector3d _axisY = new Vector3d();
	
	/** View transform in world. */
	private Transform3D _viewXform = new Transform3D();
	
	/** Dummy 3D vector.  (for GC) */
	private Vector3d _vector = new Vector3d();
	
	/** Dummy rotate.  (for GC) */
	private AxisAngle4d _rotate = new AxisAngle4d();
	
	/** Dummy transform.  (for GC) */
	private Transform3D _xform = new Transform3D();
	
	/** Dummy actuation.  (for GC) */
	private Vector4d _actuation = new Vector4d();

	/**
	Updates this group's rotation based on configuration
	parameters and the current view and node geometry.
	*/	
	protected void updateRotation() {
		// rotate to view relative to this	
		_xform.set(_thisNegRot);
		_xform.mul(_viewXform);
		
		// convert to axis angle
		if(_axis==null) {
			// rotate about origin relative to this
			Visualize.getOrientation(_xform, _rotate, null,
			 null);
		} else {
			// rotate about axis relative to this
			/// compute view vector
			_vector.set(0, 0, -1);
			_xform.transform(_vector);
			_vector.normalize();
	
if(Debug.getEnabled()){
Debug.println("DisplayFaceGroup.axis",
"AXIS:DisplayFaceGroup.updateRotation:" +
" viewVector=" + _vector);}

			/// compute view vector angle in axis plane
			//// project view vector into axis plane (-90 deg)			
			_vector.cross(_vector, _axis);
			_vector.normalize();
	
if(Debug.getEnabled()){
Debug.println("DisplayFaceGroup.axis",
"AXIS:DisplayFaceGroup.updateRotation:" +
" planeVector=" + _vector);}

			//// compute angle from projection on plane axes			
			double angle = 0.0;

			if(_vector.x==0 && _vector.y==0 && _vector.z==0) {
				angle = 0.0;
			} else {
				angle = Math.atan2(_vector.dot(_axisY),
				 _vector.dot(_axisX));
			}
	
if(Debug.getEnabled()){
Debug.println("DisplayFaceGroup.axis",
"AXIS:DisplayFaceGroup.updateRotation:" +
" axisX=" + _axisX +
" axisY=" + _axisY +
" dotX=" + _vector.dot(_axisX) +
" dotY=" + _vector.dot(_axisY) +
" angle=" + angle);}

			_rotate.x = _axis.x; 
			_rotate.y = _axis.y; 
			_rotate.z = _axis.z; 
			_rotate.angle = angle; 
		}
	
if(Debug.getEnabled()){
Debug.println("DisplayFaceGroup.verbose",
"ROTATE:DisplayFaceGroup.updateRotation:" +
" thisNegRot=" + _thisNegRot + 
" axis=" + _axis + 
" rotate=" + _rotate);}

		_actuation.set(_rotate.x, _rotate.y, _rotate.z,
		 _rotate.angle);
		_rotation.initActuation(_actuation);
	}

	// ActuatorGroup implementation
	
	protected void setEnableGroup(boolean enable) {
		_rotation.setEnable(enable);
	}
	
}