package terrapeer.vui.j3dui.control.mappers;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.inputs.*;
import terrapeer.vui.j3dui.control.actuators.*;

/**
A coordinate mapper that translates a 2D input drag into an
actuation output as defined by an input drag mapper personality
plugin.  The plugin also defines the format of the output
actuation value.

@author Jon Barrilleaux,
copyright (c) 1999-2000 Jon Barrilleaux,
All Rights Reserved.
*/

public class InputDragMapper implements InputDragTarget {
	
	// public interface =========================================

	/**
	Constructs an InputDragMapper with a personality plugin.
	@param plugin Drag mapper plugin.  Never null.
	*/
	public InputDragMapper(InputDragMapperPlugin plugin) {
		if(plugin==null) throw new
		 IllegalArgumentException("'plugin' is null.");
		_plugin = plugin;
		
	}

	/**
	Constructs an InputDragMapper with a personality plugin and
	event target.
	@param target Event target.  Null if none.
	@param plugin Drag mapper plugin.  Never null.
	*/
	public InputDragMapper(ActuationTarget target,
	 InputDragMapperPlugin plugin) {
	 
	 	this(plugin);
		if(target!=null) getEventOut().addEventTarget(target);
	}
	
	/**
	Gets the event output splitter.  Use it to add and
	remove event targets.
	@return Reference to the event out splitter.  Never null.
	*/
	public ActuationSplitter getEventOut() {
		return _eventOut;
	}

	/**
	Gets the personality plugin.
	@return Reference to the plugin.
	*/
	public InputDragMapperPlugin getPlugin() {
		return _plugin;
	}

	/**
	Gets the event target.
	@return Reference to the event target.
	
	@deprecated Use getEventOut().  This method now always
	returns null.
	*/
	public ActuationTarget getEventTarget() {
		return null;
	}

	/**
	Sets whether or not the effect on the actuation target
	is cumulative between drags.  If not cumulative the
	actuation target will be synced at the end of each
	drag.
	@param enabled True if cumulative.  Defaults to true.
	*/
	public void setCumulative(boolean enabled) {
		_cumulative = enabled;
	}

	/**
	Gets the enable state for cumulative drags.
	@return True if cumulative.
	*/
	public boolean getCumulative() {
		return _cumulative;
	}

	// InputDragTarget implementation
	
	public void startInputDrag(Canvas3D source, Vector2d pos) {
		_plugin.startInputDrag(source, pos, _actuation);
 
if(Debug.getEnabled()){
Debug.println("InputDragMapper",
"DRAG:InputDragMapper:startInputDrag:" +
" inPos=" + pos);}

	}
	
	public void doInputDrag(Canvas3D source, Vector2d pos) {
		_plugin.doInputDrag(source, pos, _actuation);
 
if(Debug.getEnabled()){
Debug.println("InputDragMapper",
"DRAG:InputDragMapper:doInputDrag:" +
" inPos=" + pos +
" actuation=" + _actuation);}

		_eventOut.updateActuation(_actuation);
	}
	
	public void stopInputDrag(Canvas3D source, Vector2d pos) {
		_plugin.stopInputDrag(source, pos, _actuation);
		
if(Debug.getEnabled()){
Debug.println("InputDragMapper",
"DRAG:InputDragMapper:stopInputDrag:" +
" inPos=" + pos +
" cumulative=" + getCumulative());}

		if(_cumulative) _eventOut.syncActuation();
	}
			
	// personal body ============================================
	
	/** Event out splitter. Never null. */
	private ActuationSplitter _eventOut =
	 new ActuationSplitter();
	
	/** Personality plugin.  Never null. */
	private InputDragMapperPlugin _plugin;
	
	/** Event target.  Never null. */
//	private ActuationTarget _eventTarget;
	
	/** If true, drag effect is cumulative. */
	private boolean _cumulative = true;
	
	/** Dummy actuation value.  (for GC) */
	private Vector4d _actuation = new Vector4d();
	
}