package terrapeer.vui.j3dui.visualize.change;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.utils.app.*;
import terrapeer.vui.j3dui.control.inputs.*;

/**
Abstract base class for sensors that detect change events.
<P>
This class maintains a global list of change sensors as well
as a mapping of change sensors by the actuators that can
effect changes in them.  Whenever a potential change occurs,
as detected by cooperating actuator classes, the affected
change sensors are notified to test their source objects for
any change.  If a sensor's source object has actually changed,
the sensor's event target is notified accordingly.
<P>
For the system to work, all changes to monitored source
objects, direct or indirect, must be handled through
cooperating actuator classes.  Source object changes that
occur through other means will not be sensed and reported
as change events.   
<P>
Use of this class and its subclasses streamlines and
eliminates the original ChangePoster class, which was a quick
kludge developed to address several bugs in Java 3D 1.1.2.
Because of continuing deficiencies in the Java 3D API with 1.2,
this class and its subclasses also takeover the functionality
originally planned for implementation with the
WakeupOnCollisionMovement behavior, thereby cutting all ties
with the Java 3D behavior system.  

@author Jon Barrilleaux,
copyright (c) 2000 Jon Barrilleaux,
All Rights Reserved.
*/

public abstract class ChangeSensor {
	
	// public utilities =========================================

	/**
	Traces the ancestory of the target node and adds any
	ancestral transform groups to the trace list.  The scenegraph
	must be dead or prepared for live access. Typically, the
	trace list should be cleared before using this method.
	@param target Target node whose ancestory is to be traced.
	@param list Reference to a list of Node.  Used for
	the return trace list.  Never null.
	@return A reference to "list".  Can be empty.  Never null. 
	*/
	public static ArrayList traceTransformGroups(Node target,
	 ArrayList list) {
		// trace target ancestors, by parent
		Node node = target;
		
		for(;;) {
			// if node is a transform, add it to list
			if(node instanceof TransformGroup)
				list.add(node);

			// get parent, quit if null		
			node = node.getParent();
			if(node==null) break;
		}	
			
		return list;
	}

	/**
	Builds the actuator to sensor mapping for efficiently
	handling changes.  Typically called by AppWorld just before
	making the world live, presumably after the scenegraph and
	all sensor-related event chains are completed.  Can (and
	should) also be called by the user after making modifications
	to the scenegraph or to event chains that can affect any
	source objects monitored by change sensors.  Typically, for
	this method to work, the scenegraph must be dead or properly
	configured for probing by the sensors while the scenegraph
	is live. 
	*/		
	public static final void initActuators() {

if(Debug.getEnabled()){
Debug.println("ChangeSensor.verbose",
"INIT:ChangeSensor:init: begin");}

		// create new actuator map and null (default) entry
		_actuators = new WeakHashMap();
		_actuators.put(null, new ArrayList());
		
		// build actuator map, by sensor
		ArrayList actuators = new ArrayList();

		Iterator sensorI = _sensors.iterator();
		while(sensorI.hasNext()) {
			// get the sensor and its actuators
			ChangeSensor sensor =
			 (ChangeSensor)sensorI.next();

if(Debug.getEnabled()){
Debug.println("ChangeSensor.init",
"INIT:ChangeSensor:init:" +
 " sensor=" + sensor);}

			actuators.clear();			
			sensor.getActuators(actuators);

			// add this sensor to its actuators' sensor lists			
			ArrayList sensors;
			
			if(actuators.isEmpty()) {
				// add sensor to null actuator's list (never null)
				sensors = (ArrayList)_actuators.get(null);

if(Debug.getEnabled()){
Debug.println("ChangeSensor.init",
"INIT:ChangeSensor:init:" +
 " actuator=null");}

				sensors.add(sensor);
			} else {
				// add sensor to actuator lists, by actuator
				Iterator actuatorI = actuators.iterator();
				while(actuatorI.hasNext()) {
					Object actuator = actuatorI.next();
					sensors = (ArrayList)_actuators.get(actuator);
					if(sensors==null) {
						// actuator's entry is null, create one
						sensors = new ArrayList();
						_actuators.put(actuator, sensors);
					}

if(Debug.getEnabled()){
Debug.println("ChangeSensor.init",
"INIT:ChangeSensor:init:" +
 " actuator=" + actuator);}

					sensors.add(sensor);
				}
			} 
		}

if(Debug.getEnabled()){
Debug.println("ChangeSensor.verbose",
"INIT:ChangeSensor:init: end");}

	}

	/**
	Forces all sensors to initialize their state and that of
	their event targets.  Typically called by AppWorld just
	after making the world live, presumably after the scenegraph
	and all sensor-related event chains are completed.  Can
	(and should) also be called by the user after making
	modifications to the scenegraph or to event chains
	that can affect any source objects monitored by change
	sensors.  Typically, for this method to work, the scenegraph
	must be live. 
	*/		
	public static final void initSensors() {

if(Debug.getEnabled()){
Debug.println("ChangeSensor.verbose",
"INIT:ChangeSensor:initSensors: bgn");}

		// notify all sensors of change, by sensor
		if(!_sensors.isEmpty()) {		
			Iterator sensorI = _sensors.iterator();
			while(sensorI.hasNext()) {
				ChangeSensor sensor = (ChangeSensor)sensorI.next();

if(Debug.getEnabled()){
Debug.println("ChangeSensor.init",
"INIT:ChangeSensor:initSensors:" +
 " sensor=" + sensor);}

				sensor.init();
			}
		}

if(Debug.getEnabled()){
Debug.println("ChangeSensor.verbose",
"INIT:ChangeSensor:initSensors: end");}

	}

	/**
	Should never be called directly.  Invoked by a cooperating
	actuator whenever it performs a change that may be monitored
	by a change sensor. 
	@param actuator Actuator that is causing a change.  Only
	those sensors whose change source might be affected by the
	actuator are notified.  Can be null or unrecognized, in which
	case the null actuator map entry is used.
	*/		
	public static final void processAllChanges(Object actuator) {

if(Debug.getEnabled()){
Debug.println("ChangeSensor.verbose",
"PROC:ChangeSensor:processAllChanges:" +
 " bgn actuator=" + actuator);}

		// resolve list of affected sensors
		ArrayList sensors;

		if(_actuators==null) {
			// no actuator map, use all sensors
			sensors = _sensors;
		} else {
			// get sensors for recognized and null actuator
			sensors = (ArrayList)_actuators.get(actuator);
			
			// if actuator unrecognized, use null actuator
			if(sensors == null) {
				sensors = (ArrayList)_actuators.get(null);
			}

if(Debug.getEnabled()){
if(sensors==null)
 Debug.println("ChangeSensor.process",
 "PROC:ChangeSensor:processAllChanges:" +
 " actuator=" + actuator + " Sensors NULL -- error");
else if(sensors.isEmpty())		
 Debug.println("ChangeSensor.process",
 "PROC:ChangeSensor:processAllChanges:" +
 " actuator=" + actuator + " Sensors EMPTY");
else
 Debug.println("ChangeSensor.process",
 "PROC:ChangeSensor:processAllChanges:" +
 " actuator=" + actuator + " Sensors count=" + sensors.size());
}
		}
	 
		// notify affected sensors of change, by sensor
		if(!sensors.isEmpty()) {		
			Iterator sensorI = sensors.iterator();
			while(sensorI.hasNext()) {
				ChangeSensor sensor = (ChangeSensor)sensorI.next();

if(Debug.getEnabled()){
Debug.println("ChangeSensor.process",
"PROC:ChangeSensor:processAllChanges:" +
 " actuator=" + actuator + " sensor=" + sensor);}

				sensor.processChange();
			}
		}

if(Debug.getEnabled()){
Debug.println("ChangeSensor.verbose",
"PROC:ChangeSensor:processAllChanges:" +
 " end actuator=" + actuator);}

	}
		
	// personal utilities =======================================

	/** Global list of ChangeSensor.  Never null. */	
	private static ArrayList _sensors = new ArrayList();

	/** Global map of source actuators to ArrayList of
	affected ChangeSensor.  Null if none (no init). */	
	private static WeakHashMap _actuators = null;

	// public interface =========================================

	/**
	Creates a ChangeSensor that monitors a source object for
	change.
	*/
	public ChangeSensor() {
		// add sensor to global list		
		_sensors.add(this);
	}

	/**
	Removes this sensor from the sensor list.
	*/
	public void finalize() {
		// remove sensor from global list
		_sensors.remove(this);
	}

	/**
	Gets the source object being monitored by this sensor.
	@return Reference to the source object.  Never null.
	*/
	abstract public Object getSource();

	/**
	Called after the scenegraph and target event chain is
	set up and the world is live to force initialization of
	the sensor state and that of its event target.
	*/
	abstract public void init();

	/**
	Gets a list of actuator objects that could cause the
	monitored source object to change.  Typically, the
	world is (or should be) killed before this method is
	called.  If the actuators can not be determined then
	returns null, in which case any change by any null
	actuator causes this sensor to process the change (which
	can be expensive in time).
	@param actuators Reference to a list of Object.  Used for
	efficiency to return the list actuator objects, which are
	added to any already in the list.  typicaally, the list
	should be cleared before use.  Never null.
	@return A reference to the "actuators" list.  Empty if
	no objects can be determined.  Never null. 
	*/
	abstract public ArrayList getActuators(ArrayList actuators);

	/**
	Called whenever an actuator that can affect the monitored
	source object detects a change.  Causes this sensor to
	checks its source object for any changes to its monitored
	state.  If a change occurred, corresponding change events
	are sent to this sensor's event target.
	*/
	abstract public void processChange();

	// personal body ============================================
}