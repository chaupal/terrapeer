package terrapeer.vui.j3dui.control.actuators.groups;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.actuators.*;

/**
An actuator group similar to AffineGroup but that also
includes provisions for specifying a center position for
rotation and scale relative to the translated origin.  The
order of transforms in the chain starting with the head are:
translation, center, rotation,
scale, -center.  This group is similar in function to that
of a VRML Transform node.
<P>
The transforms in this group are for single-user use and
should not be accessed or manipulated externally. 

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class CenterAffineGroup extends ActuatorTransformGroup {
	
	// public interface =========================================

	/**
	Constructs a CenterAffineGroup.
	*/
	public CenterAffineGroup() {
		this(null);
	}

	/**
	Constructs a CenterAffineGroup with a child node.
	@param node First child node of this group.  Null if none.
	*/
	public CenterAffineGroup(Node node) {
		super(5, node);

		// build actuators		
		_translation = new Actuator(new SVTransformGroupPlugin(
		 new TranslationPlugin(getTransform(0))));
		
		_center = new Actuator(new SVTransformGroupPlugin(
		 new TranslationPlugin(getTransform(1))));
		 
		_rotation = new Actuator(new SMTransformGroupPlugin(
		 new AxisAnglePlugin(getTransform(2))));
		 
		_scale = new Actuator(new SVTransformGroupPlugin(
		 new ScalePlugin(getTransform(3))));
		
		_centerNeg = new Actuator(new SVTransformGroupPlugin(
		 new NegTranslationPlugin(getTransform(4))));

		// build center splitter
		_centerSplit = new ActuationSplitter();
		_centerSplit.addEventTarget(_center);
		_centerSplit.addEventTarget(_centerNeg);
	}

	/**
	Constructs a CenterAffineGroup with a child node and the
	specified geometry.
	@param pos Position.
	@param rot Rotation.
	@param scl Scale.
	*/
    public CenterAffineGroup(Node node, Vector3d pos,
     AxisAngle4d rot, Vector3d scl) {
     	this(node);
     	
     	getTranslation().initActuation(
     	 TranslationPlugin.toActuation(pos));
     	getAxisAngle().initActuation(
     	 AxisAnglePlugin.toActuation(rot));
     	getScale().initActuation(
     	 ScalePlugin.toActuation(scl));
	}

	/**
	Gets the translation actuator (see TranslationPlugin
	for actuation value definition).
	@return Reference to the actuator.
	*/
	public Actuator getTranslation() {
		return _translation;
	}

	/**
	Gets the rotation actuator. (see AxisAnglePlugin
	for actuation value definition).
	@return Reference to the actuator.
	*/
	public Actuator getAxisAngle() {
		return _rotation;
	}

	/**
	Gets the scale actuator. (see ScalePlugin
	for actuation value definition).
	@return Reference to the actuator.
	*/
	public Actuator getScale() {
		return _scale;
	}

	/**
	Gets the center actuator. (see TranslationPlugin
	for actuation value definition).
	@return Reference to the actuator.
	*/
	public ActuationSplitter getCenter() {
		return _centerSplit;
	}
			
	// personal body ============================================
	
	/** Translation actuator. Never null. */
	private Actuator _translation;
	
	/** Rotation actuator. Never null. */
	private Actuator _rotation;
	
	/** Scale actuator. Never null. */
	private Actuator _scale;
	
	/** Center actuator. Never null. */
	private Actuator _center;
	
	/** Negative center actuator. Never null. */
	private Actuator _centerNeg;
	
	/** Center splitter. Never null. */
	private ActuationSplitter _centerSplit;

	// ActuatorGroup implementation
	
	protected void setEnableGroup(boolean enable) {
		_translation.setEnable(enable);
		_rotation.setEnable(enable);
		_scale.setEnable(enable);
		_centerSplit.setEnable(enable);
	}
	
}