package terrapeer.vui.j3dui.control.actuators;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.control.*;
import terrapeer.vui.j3dui.utils.Debug;

/**
(Single-user Vector TransformGroupPlugin)  A transform group
actuator plugin that assumes exclusive controls of its target
node that the actuation state can be maintained by a vector
(Vector4d). As such this is the most efficient but least
flexible actuator form.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class SVTransformGroupPlugin
 extends TransformGroupPlugin {
	
	// public interface =========================================

	/**
	Constructs a SVTransformGroupPlugin with the personality
	plugin <plugin>.
	@param plugin Personality plugin.  Never null.
	*/
	public SVTransformGroupPlugin(TGGeometryPlugin plugin) {
		super(plugin);
		
		// init actuation reference		
		_reference.set(_plugin.getActuationInit());
	}
	
	// ActuatorPlugin implementation
	
	public String toString() {
		return "SVTransformGroupPlugin";
	}

	public Node getTargetNode() {
		return _plugin._targetNode;
	}
			
	// personal body ============================================
	
	/** Actuation reference value. */
	private Vector4d _reference = new Vector4d();
	
	/** Actuation state value. */
	private Vector4d _state = new Vector4d();
	
	/** Dummy actuation value.  (for GC) */
	private final Vector4d _value = new Vector4d();
	
	/** Dummy actuation transform.  (for GC) */
	private final Transform3D _xform = new Transform3D();
	
	// ActuatorPlugin implementation
	
	protected void initActuation(Tuple4d value) {
		
if(Debug.getEnabled()){
Debug.println(this, "SVTransformGroupPlugin",
"SVTransformGroupPlugin:initActuation:" +
" in=" + value);}
		
		toActuationSource(value, _value);
		_state.set(_value);
		_reference.set(_state);
		toActuationTarget(_state, _state);
		_plugin.toActuationTransform(_state, _xform);
		_plugin._targetNode.setTransform(_xform);
	}

	protected void updateActuation(Tuple4d value) {
		
if(Debug.getEnabled()){
Debug.println(this, "SVTransformGroupPlugin",
"SVTransformGroupPlugin:updateActuation:" +
" in=" + value);}
		
		toActuationSource(value, _value);
		_plugin.toActuationUpdate(_value, _reference, _state);
		toActuationTarget(_state, _state);
		_plugin.toActuationTransform(_state, _xform);
		_plugin._targetNode.setTransform(_xform);
	}

	protected void syncActuation() {
		
if(Debug.getEnabled()){
Debug.println(this, "SVTransformGroupPlugin",
"SVTransformGroupPlugin:syncActuation: -");}
		
		_reference.set(_state);
	}
	
}