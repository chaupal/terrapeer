package  terrapeer.vui.j3dui.navigate;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.utils.objects.*;
import terrapeer.vui.j3dui.control.mappers.*;
import terrapeer.vui.j3dui.control.actuators.*;
import terrapeer.vui.j3dui.control.actuators.groups.*;

/**
Creates an actuator group that serves as a camera that orbits
about the LAP.  The camera contains an actuator group chain
with LAP translation, orbit angle (elevation, heading, twist),
and LFO translation groups, in that order from group head to
tail.

@author Jon Barrilleaux,
copyright (c) 2000 Jon Barrilleaux,
All Rights Reserved.
*/
public class OrbitCamera extends ActuatorGroup {
	
	// public interface =========================================

	/**
	Constructs an OrbitCamera.
	*/
	public OrbitCamera() {
		this(null);
	}

	/**
	Constructs an OrbitCamera with a child node.
	@param node First child node of this camera.  Typically the
	view.  Null if none.
	*/
	public OrbitCamera(Node node) {
		// empty chain
		super(0, null);

		// build group chain
		Group[] chain = new Group[3];
		
		chain[2] = _lfo = new AffineGroup(node);
		chain[1] = _orbit = new OrbitGroup(_lfo);
		chain[0] = _lap = new AffineGroup(_orbit);
		
		setChain(chain, 3);

		// limit ranges		
		getOrbitActuator().getElevation().getPlugin().setTargetClamp(
		 Mapper.DIM_W, new Vector2d(-Math.PI/2, Math.PI/2));
		getOrbitActuator().getTwist().getPlugin().setTargetClamp(
		 Mapper.DIM_W, new Vector2d(-Math.PI/2, Math.PI/2));
		 
		getLfoActuator().getPlugin().setTargetClamp(
		 Mapper.DIM_Z, new Vector2d(0, Double.POSITIVE_INFINITY));
		
		// init states
		getLapActuator().initActuation(new Vector4d(0, 0, 0, 0));
		getOrbitActuator().initActuation(new Vector4d(0, 0, 0, 0));
		getLfoActuator().initActuation(new Vector4d(0, 0, 10, 0));
	}

	/**											
	Gets the LAP actuator (see TranslationPlugin
	for actuation value definition), which is used to position
	the camera view LAP.
	@return Reference to the actuator.
	*/
	public Actuator getLapActuator() {
		return _lap.getTranslation();
	}

	/**
	Gets the orbit actuator group, which is used to orbit
	the camera view about the LAP.
	@return Reference to the group.
	*/
	public OrbitGroup getOrbitActuator() {
		return _orbit;
	}

	/**
	Gets the LFO actuator (see TranslationPlugin
	for actuation value definition), which is used to position
	the camera view LFO relative to the LAP.
	@return Reference to the actuator.
	*/
	public Actuator getLfoActuator() {
		return _lfo.getTranslation();
	}

	/**											
	Gets the LAP tail group, which can be used for adding
	children.
	@return Reference to the LAP group.
	*/
	public Group getLapGroup() {
		return _lap.getTail();
	}

	/**											
	Gets the Orbit tail group, which can be used for adding
	children.
	@return Reference to the orbit group.
	*/
	public Group getOrbitGroup() {
		return _orbit.getTail();
	}

	/**											
	Gets the LFO tail group, which can be used for adding
	children.  Since this is the tail of this group, adding a
	child to it has the same effect as using addNode().
	@return Reference to the LFO group.
	*/
	public Group getLfoGroup() {
		return _lfo.getTail();
	}
			
	// personal body ============================================
	
	/** LAP group. Never null. */
	private AffineGroup _lap;
	
	/** Orbit group. Never null. */
	private OrbitGroup _orbit;
	
	/** LFO group. Never null. */
	private AffineGroup _lfo;

	// ActuatorGroup implementation
	
	protected void setEnableGroup(boolean enable) {
		_lap.setEnable(enable);
		_orbit.setEnable(enable);
		_lfo.setEnable(enable);
	}
	
}
