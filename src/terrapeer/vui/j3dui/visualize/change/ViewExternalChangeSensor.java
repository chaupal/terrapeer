package terrapeer.vui.j3dui.visualize.change;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.utils.app.*;
import terrapeer.vui.j3dui.control.inputs.*;
import terrapeer.vui.j3dui.visualize.*;

/**
A sensor that monitors a view for external changes (position,
rotation, etc.).
<P>
The source object must be an AppView to assure that external
state changes are detected and handled by the change system.

@author Jon Barrilleaux,
copyright (c) 2000 Jon Barrilleaux,
All Rights Reserved.
*/

public class ViewExternalChangeSensor extends ChangeSensor {

	// public interface =========================================

	/**
	Constructs a ViewExternalChangeSensor that monitors the
	specified source object for changes.
	@param target Event target.  Never null.
	@param source Source object.  Never null.
	*/
	public ViewExternalChangeSensor(
	 ViewChangeTarget target, AppView source) {

		if(target==null) throw new
		 IllegalArgumentException("'target' is null.");
		if(source==null) throw new
		 IllegalArgumentException("'source' is null.");

		_eventTarget = target;
		_source = source;

// Bug in j3d 1.2 makes canvas vworld xform out of sync.
// Use the vworld xform of the source itself and ignore differences
// due to internal view geometry.
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
// Bug in j3d 1.2 makes canvas vworld xform out of sync.
// Use the vworld xform of the source itself and ignore differences
// due to internal view geometry.
		return ChangeSensor.traceTransformGroups(
		 _source, actuators);
	}

	public void processChange() {
		// testing state may fail in dead scenegraph
		try {
			// test external view state
// Bug in j3d 1.2 makes canvas vworld xform out of sync.
// Use the vworld xform of the source itself and ignore differences
// due to internal view geometry.
//			Visualize.getViewExternal(_source.getView(),
//			 _pos, _xform);
			Visualize.getNodeExternal(_source, _pos, _xform);

if(Debug.getEnabled()){
Debug.println("ViewExternalChangeSensor.verbose",
"VIEW:ViewExternalChangeSensor:processChange:" +
" source=" + _source +
" view=" + _source.getView() +
" pos=" + _pos +
" \nxform=" + _xform);}

			if(!_xform.equals(_oldXform)) {
				// state changed: save new state, tell event target
				_oldXform.set(_xform);

if(Debug.getEnabled()){
Debug.println("ViewExternalChangeSensor.change",
"VIEW-EXT:ViewExternalChangeSensor:processChange:" +
" source=" + _source);}

				_eventTarget.setViewExternal(_source.getView(),
				 _pos, _xform);
			}
		}
		catch(Exception ex) {}
	}

	// personal body ============================================

	/** Event target. Never null. */
	private ViewChangeTarget _eventTarget;

	/** Source object.  Never null. */
	private AppView _source;

	/** External view transform reference. */
	private Transform3D _oldXform = new Transform3D();

	/** Dummy position.  (for GC) */
	private Point3d _pos = new Point3d();

	/** Dummy transform.  (for GC) */
	private Transform3D _xform = new Transform3D();

}
