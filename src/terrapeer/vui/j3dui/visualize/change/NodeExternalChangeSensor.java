package terrapeer.vui.j3dui.visualize.change;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.utils.app.*;
import terrapeer.vui.j3dui.control.inputs.*;
import terrapeer.vui.j3dui.visualize.*;

/**
A sensor that monitors a node for external changes (position,
rotation, etc.).

@author Jon Barrilleaux,
copyright (c) 2000 Jon Barrilleaux,
All Rights Reserved.
*/

public class NodeExternalChangeSensor
 extends ChangeSensor {
	
	// public interface =========================================

	/**
	Constructs a NodeExternalChangeSensor that monitors the
	specified source object for changes.
	@param target Event target.  Never null.
	@param source Source object.  Never null.
	*/
	public NodeExternalChangeSensor(
	 NodeExternalChangeTarget target, Node source) {
	
		if(target==null) throw new
		 IllegalArgumentException("'target' is null.");
		if(source==null) throw new
		 IllegalArgumentException("'source' is null.");
		
		_eventTarget = target;
		_source = source;
		_source.setCapability(Node.ALLOW_LOCAL_TO_VWORLD_READ);
	}

	// ChangeSensor implementation

	public Object getSource() {
		return _source;
	}

	public void init() {
		// force known bad old state
		_oldXform.setZero();
		
		processChange();
	}

	public ArrayList getActuators(ArrayList actuators) {
		return ChangeSensor.traceTransformGroups(
		 _source, actuators);
	}
	
	public void processChange() {
		// testing state may fail in dead scenegraph
		try {
			// test external node state
			Visualize.getNodeExternal(_source, _pos, _xform);
		 
if(Debug.getEnabled()){
Debug.println("NodeExternalChangeSensor.verbose",
"NODE:NodeExternalChangeSensor:processChange:" +
" source=" + _source +
" pos=" + _pos +
" \nxform=" + _xform);}
		
			if(!_xform.equals(_oldXform)) {
				// state changed: save new state, tell target
				_oldXform.set(_xform);
				
if(Debug.getEnabled()){
Debug.println("NodeExternalChangeSensor.change",
"NODE:NodeExternalChangeSensor:processChange:" +
" source=" + _source);}
				
				_eventTarget.setNodeExternal(_source, _pos,
				 _xform);
			}
		}
		catch(Exception ex) {}
	}
				
	// personal body ============================================
	
	/** Event target. Never null. */
	private NodeExternalChangeTarget _eventTarget;

	/** Source object monitored for change. */
	private Node _source;
	
	/** External node transform reference. */
	private Transform3D _oldXform = new Transform3D();
	
	/** Dummy position.  (for GC) */
	private Point3d _pos = new Point3d();

	/** Dummy transform.  (for GC) */
	private Transform3D _xform = new Transform3D();

}