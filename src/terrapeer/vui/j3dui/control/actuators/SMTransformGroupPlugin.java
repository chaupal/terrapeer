package terrapeer.vui.j3dui.control.actuators;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.control.*;
import terrapeer.vui.j3dui.utils.Debug;

/**
(Sigle-user Matrix TransformGroupPlugin)  A transform group
actuator plugin that assumes exclusive control of the target
node and that the actuation state must be maintained by a
matrix (Transform3D).
<P>
The target actuation value can not be clamped (i.e. it is not
a vector).

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class SMTransformGroupPlugin
 extends TransformGroupPlugin {
	
	// public interface =========================================

	/**
	Constructs an SMTransformGroupPlugin with the personality
	plugin <plugin>.
	@param plugin Personality plugin.  Never null.
	*/
	public SMTransformGroupPlugin(TGGeometryPlugin plugin) {
		super(plugin);

		// init actuation reference		
		_plugin.toActuationTransform(_plugin.getActuationInit(),
		 _reference);
	}
	
	// ActuatorPlugin implementation
	
	public String toString() {
		return "SMTransformGroupPlugin";
	}
			
	// personal body ============================================
	
	/** Actuation reference value. */
	private Transform3D _reference = new Transform3D();
	
	/** Actuation state value. */
	private Transform3D _state = new Transform3D();
	
	/** Dummy actuation value.  (for GC) */
	private final Vector4d _value = new Vector4d();
	
	/** Dummy actuation transform.  (for GC) */
	private final Transform3D _xform = new Transform3D();
	
	// ActuatorPlugin implementation
	
	protected void initActuation(Tuple4d value) {
		
if(Debug.getEnabled()){
Debug.println(this, "SMTransformGroupPlugin",
"SMTransformGroupPlugin:initActuation:" +
" in=" + value);}
		
		toActuationSource(value, _value);
		_plugin.toActuationTransform(_value, _state);
		_reference.set(_state);
		_plugin._targetNode.setTransform(_state);
	}

	/**
	Must be overridden if update is anything but matrix
	multiplicative.
	*/
	protected void updateActuation(Tuple4d value) {
		
if(Debug.getEnabled()){
Debug.println(this, "SMTransformGroupPlugin",
"SMTransformGroupPlugin:updateActuation:" +
" in=" + value);}
		
		toActuationSource(value, _value);
		_plugin.toActuationTransform(_value, _xform);
		_state.mul(_xform, _reference);
		_plugin._targetNode.setTransform(_state);
	}

	protected void syncActuation() {
		
if(Debug.getEnabled()){
Debug.println(this, "SMTransformGroupPlugin",
"SMTransformGroupPlugin:syncActuation: -");}
		
		_reference.set(_state);
	}
	
}