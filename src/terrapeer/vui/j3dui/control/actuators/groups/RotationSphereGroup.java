package terrapeer.vui.j3dui.control.actuators.groups;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.actuators.*;

/**
An actuator group that supports spherical rotations, like rolling
a ball.  The transformation is expressed as a set of right-handed
Euler rotations applied incrementally to a single multi-matrix
transform.  All rotation axes are relative to group, not the
rotated target node.
<P>
The input actuation value is defined as follows:
<UL>
<LI>X - Rotation X angle in radians.
<LI>Y - Rotation Y angle in radians.
<LI>Z - Rotation Z angle in radians.
<LI>W - (ignored)
</UL>

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class RotationSphereGroup extends ActuatorTransformGroup
 implements PseudoActuator {
	
	// public interface =========================================

	/**
	Constructs a RotationSphereGroup.
	*/
	public RotationSphereGroup() {
		this(null);
	}

	/**
	Constructs a RotationSphereGroup with child node <node>.
	@param node First child node of this group.  Null if none.
	*/
	public RotationSphereGroup(Node node) {
		super(1, node);
		
		_rotateX = new Actuator(new MMTransformGroupPlugin(
		 new RotationPlugin(getTransform(0),
		 new Vector3d(1, 0, 0))));
		 
		_rotateY = new Actuator(new MMTransformGroupPlugin(
		 new RotationPlugin(getTransform(0),
		 new Vector3d(0, 1, 0))));
		 
		_rotateZ = new Actuator(new MMTransformGroupPlugin(
		 new RotationPlugin(getTransform(0),
		 new Vector3d(0, 0, 1))));
	}

	/**
	Gets the X rotation actuator (see RotationPlugin
	for actuation value definition).
	@return Reference to the actuator.
	*/
	public Actuator getRotationX() {
		return _rotateX;
	}

	/**
	Gets the Y rotation actuator (see RotationPlugin
	for actuation value definition).
	@return Reference to the actuator.
	*/
	public Actuator getRotationY() {
		return _rotateY;
	}

	/**
	Gets the Z rotation actuator (see RotationPlugin
	for actuation value definition).
	@return Reference to the actuator.
	*/
	public Actuator getRotationZ() {
		return _rotateZ;
	}
	
	// ActuationTarget implementation

	public void initActuation(Tuple4d value) {
		getRotationX().initActuation(
		 RotationPlugin.toActuation(value.x, _actuation));
		getRotationY().initActuation(
		 RotationPlugin.toActuation(value.y, _actuation));
		getRotationZ().initActuation(
		 RotationPlugin.toActuation(value.z, _actuation));
	}

	public void updateActuation(Tuple4d value) {
		getRotationX().updateActuation(
		 RotationPlugin.toActuation(value.x, _actuation));
		getRotationY().updateActuation(
		 RotationPlugin.toActuation(value.y, _actuation));
		getRotationZ().updateActuation(
		 RotationPlugin.toActuation(value.z, _actuation));
	}
	
	public void syncActuation() {
		getRotationX().syncActuation();
		getRotationY().syncActuation();
		getRotationZ().syncActuation();
	}
			
	// personal body ============================================
	
	/** Rotation X actuator. Never null. */
	private Actuator _rotateX;
	
	/** Rotation Y actuator. Never null. */
	private Actuator _rotateY;
	
	/** Rotation Z actuator. Never null. */
	private Actuator _rotateZ;
	
	/** Dummy actuation value.  (for GC). */
	private final Vector4d _actuation = new Vector4d();

	// ActuatorGroup implementation
	
	protected void setEnableGroup(boolean enable) {
		_rotateX.setEnable(enable);
		_rotateY.setEnable(enable);
		_rotateZ.setEnable(enable);
	}
	
}