package terrapeer.vui.j3dui.control.actuators.groups;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.actuators.*;

/**
An actuator group that supports geometric orbiting (e.g.
a view about its look-at-point).  The transformation is
expressed as a series of right-handed Euler rotations
applied in a specific order such that Y is heading about the
original target-relative Y axis (the head transform), X is
elevation about the new X axis, and Z is twist about the new
Z axis (the tail transform).
<P>
The transforms in this group are for single-user use and
should not be accessed or manipulated externally. 
<P>
The input actuation value is defined as follows:
<UL>
<LI>X - Orbit X angle (elevation) value in radians.
<LI>Y - Orbit Y angle (heading) value in radians.
<LI>Z - Orbit Z angle (twist) value in radians.
<LI>W - (ignored)
</UL>

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class OrbitGroup extends ActuatorTransformGroup
 implements PseudoActuator {

	// public utilities =========================================
	
	/**
	Converts orbit Euler angle value <value> into an actuation
	value.
	@param value Orbit Euler angle value.
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
	Converts orbit Euler angle value <value> into an actuation
	value.
	@param value Orbit Euler angle value.
	@return New actuation value.
	*/
	public static Tuple4d toActuation(Tuple3d value) {
		return toActuation(value, new Vector4d());
	}
	
	/**
	Converts an actuation value <value> into an orbit Euler angle
	value.
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
	Converts an actuation value <value> into an orbit Euler angle
	value.
	@param value Actuation value.
	@return New orbit Euler angle value.
	*/
	public static Tuple3d fromActuation(Tuple4d value) {
		return fromActuation(value, new Vector3d());
	}
	
	// public interface =========================================

	/**
	Constructs an OrbitGroup.
	*/
	public OrbitGroup() {
		this(null);
	}

	/**
	Constructs an OrbitGroup with child node <node>.
	@param node First child node of this group.  Null if none.
	*/
	public OrbitGroup(Node node) {
		super(3, node);
		
		_heading = new Actuator(new SVTransformGroupPlugin(
		 new RotationPlugin(getTransform(0),
		 new Vector3d(0, 1, 0))));
		 
		_elevation = new Actuator(new SVTransformGroupPlugin(
		 new RotationPlugin(getTransform(1),
		 new Vector3d(1, 0, 0))));
		 
		_twist = new Actuator(new SVTransformGroupPlugin(
		 new RotationPlugin(getTransform(2),
		 new Vector3d(0, 0, 1))));
	}

	/**
	Gets the elevation actuator (see RotationPlugin
	for actuation value definition).
	@return Reference to the actuator.
	*/
	public Actuator getElevation() {
		return _elevation;
	}

	/**
	Gets the heading actuator (see RotationPlugin
	for actuation value definition).
	@return Reference to the actuator.
	*/
	public Actuator getHeading() {
		return _heading;
	}

	/**
	Gets the twist actuator (see RotationPlugin
	for actuation value definition).
	@return Reference to the actuator.
	*/
	public Actuator getTwist() {
		return _twist;
	}
	
	// ActuationTarget implementation

	public void initActuation(Tuple4d value) {
		getElevation().initActuation(
		 RotationPlugin.toActuation(value.x, _actuation));
		getHeading().initActuation(
		 RotationPlugin.toActuation(value.y, _actuation));
		getTwist().initActuation(
		 RotationPlugin.toActuation(value.z, _actuation));
	}

	public void updateActuation(Tuple4d value) {
		getElevation().updateActuation(
		 RotationPlugin.toActuation(value.x, _actuation));
		getHeading().updateActuation(
		 RotationPlugin.toActuation(value.y, _actuation));
		getTwist().updateActuation(
		 RotationPlugin.toActuation(value.z, _actuation));
	}
	
	public void syncActuation() {
		getElevation().syncActuation();
		getHeading().syncActuation();
		getTwist().syncActuation();
	}
			
	// personal body ============================================
	
	/** Heading actuator. Never null. */
	private Actuator _heading;
	
	/** Elevation actuator. Never null. */
	private Actuator _elevation;
	
	/** Twist actuator. Never null. */
	private Actuator _twist;
	
	/** Dummy actuation value.  (for GC). */
	private final Vector4d _actuation = new Vector4d();

	// ActuatorGroup implementation
	
	protected void setEnableGroup(boolean enable) {
		_heading.setEnable(enable);
		_elevation.setEnable(enable);
		_twist.setEnable(enable);
	}
	
}