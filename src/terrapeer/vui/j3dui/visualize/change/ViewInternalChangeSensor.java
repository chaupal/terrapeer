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
A sensor that monitors a view for internal changes (FOV, display
size, etc.).
<P>
The source object must be an AppView to assure that internal
state changes are detected and handled by the change system.   
*/

public class ViewInternalChangeSensor extends ChangeSensor {
	
	// public interface =========================================

	/**
	Constructs a ViewInternalChangeSensor that monitors the
	specified source object for changes.
	@param target Event target.  Never null.
	@param source Source object.  Never null.
	@param host (not used)
	*/
	public ViewInternalChangeSensor(
	 ViewChangeTarget target, AppView source) {
	
		if(target==null) throw new
		 IllegalArgumentException("'target' is null.");
		if(source==null) throw new
		 IllegalArgumentException("'source' is null.");
		 
		_eventTarget = target;
		_source = source;
	}

	// ChangeSensor implementation

	public Object getSource() {
		return _source;
	}

	public void init() {
		// force known bad old state
		_oldVsf = 0.0;
		_oldDsf = 0.0;
		
		processChange();
	}

	public ArrayList getActuators(ArrayList actuators) {
		// add source as only actuator
		actuators.add(_source);
		
		return actuators;
	}
	
	public void processChange() {
		// sample state  
		double fov = _source.getViewFov();
		double vsf = _source.getViewScale();
		double dsf = _source.getDisplayScale();
		_source.getDisplayViewOffset(_dvo);
		_source.getDisplay().getDisplaySize(_ds);
		_source.getDisplay().getScreenSize(_ss);
		_source.getDisplay().getScreenResolution(_sr);
			 
if(Debug.getEnabled()){
Debug.println("ViewInternalChangeSensor.verbose",
"VIEW:ViewInternalChangeSensor:processChange:" +
" source=" + _source +
" view=" + _source.getView() +
" fov=" + fov +
" vsf=" + vsf +
" dsf=" + dsf +
" dvo=" + _dvo +
" ds=" + _ds +
" ss=" + _ss +
" sr=" + _sr);}

		// check for state change		
		if(fov!=_oldFov || vsf!=_oldVsf || dsf!=_oldDsf ||
		 !_dvo.equals(_oldDvo) || !_ds.equals(_oldDs) ||
		 !_ss.equals(_oldSs) || !_sr.equals(_oldSr)) {
			// state changed: save new state, tell event target
			_oldFov = fov;
			_oldVsf = vsf;
			_oldDsf = dsf;
			_oldDvo.set(_dvo);
			_oldDs.set(_ds);
			_oldSs.set(_ss);
			_oldSr.set(_sr);
				
if(Debug.getEnabled()){
Debug.println("ViewInternalChangeSensor.change",
"VIEW-INT:ViewInternalChangeSensor:processChange:" +
" source=" + _source);}
				
			_eventTarget.setViewInternal(_source.getView(),
			 fov, vsf, dsf, _dvo, _ds, _ss, _sr);
		}
	}
			
	// personal body ============================================
	
	/** Event target. Never null. */
	private ViewChangeTarget _eventTarget;

	/** Source object.  Never null. */
	private AppView _source;
	
	/** Internal view field of view reference. */
	private double _oldFov;
	
	/** Internal view view scale factor reference. */
	private double _oldVsf;
	
	/** Internal view display scale factor reference. */
	private double _oldDsf;
	
	/** Internal view display-view offset reference. */
	private Vector2d _oldDvo = new Vector2d();
	
	/** Internal view display size reference. */
	private Vector2d _oldDs = new Vector2d();
	
	/** Internal view screen size reference. */
	private Vector2d _oldSs = new Vector2d();
	
	/** Internal view screen resolution reference. */
	private Vector2d _oldSr = new Vector2d();
	
	/** Dummy display-view offset.  (for GC) */
	private Vector2d _dvo = new Vector2d();
	
	/** Dummy display size.  (for GC) */
	private Vector2d _ds = new Vector2d();
	
	/** Dummy screen size.  (for GC) */
	private Vector2d _ss = new Vector2d();
	
	/** Dummy screen resolution.  (for GC) */
	private Vector2d _sr = new Vector2d();

}