package terrapeer.vui.j3dui.control.mappers.intuitive;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.actuators.*;

/**
A dimension mapper that translates a 3D source drag into an
actuation output as defined by a source drag mapper personality
plugin.  The plugin also defines the format of the output
actuation value.
<P>
By definition, source to actuation space transformation is
vector not point-based, which means that direction and
magnitude are mapped but not position.

@author Jon Barrilleaux,
copyright (c) 1999-2000 Jon Barrilleaux,
All Rights Reserved.
*/

public class SourceDragMapper implements SourceDragTarget {
	
	// public interface =========================================

	/**
	Constructs a SourceDragMapper with a personality plugin.
	@param plugin Drag mapper plugin.  Never null.
	*/
	public SourceDragMapper(SourceDragMapperPlugin plugin) {
		if(plugin==null) throw new
		 IllegalArgumentException("'plugin' is null.");
		_plugin = plugin;
	}

	/**
	Constructs a SourceDragMapper with an event target and
	personality plugin.
	@param target Event target.  Null if none.
	@param plugin Drag mapper plugin.  Never null.
	*/
	public SourceDragMapper(ActuationTarget target,
	 SourceDragMapperPlugin plugin) {
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
	public SourceDragMapperPlugin getPlugin() {
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
	@param enabled True if cumulative.
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

	// SourceDragTarget implementation
	
	public void startSourceDrag(Node source, Vector3d pos) {
		_oldSource = source;	
		_plugin.startSourceDrag(source, pos, _actuation);
 
if(Debug.getEnabled()){
Debug.println(this, "SourceDragMapper",
"DRAG:SourceDragMapper:startSourceDrag:" +
" sourcePos=" + pos +
" source=" + source);}
	}
	
	public void doSourceDrag(Node source, Vector3d pos) {
		if(source != _oldSource) {
			// different source, start new actuation
			if(getCumulative()) _eventOut.syncActuation();
			_oldSource = source;
		}
		
		_plugin.doSourceDrag(source, pos, _actuation);
 
if(Debug.getEnabled()){
Debug.println(this, "SourceDragMapper",
"DRAG:SourceDragMapper:doSourceDrag:" +
" sourcePos=" + pos +
" source=" + source +
" actuation=" + _actuation);}

		_eventOut.updateActuation(_actuation);
	}
	
	public void stopSourceDrag(Node source, Vector3d pos) {
		_plugin.stopSourceDrag(source, pos, _actuation);
	
if(Debug.getEnabled()){
Debug.println(this, "SourceDragMapper",
"DRAG:SourceDragMapper:stopSourceDrag:" +
" sourcePos=" + pos +
" source=" + source +
" cumulative=" + getCumulative());}

		if(_cumulative) _eventOut.syncActuation();
	}
			
	// personal body ============================================
	
	/** Event out splitter. Never null. */
	private ActuationSplitter _eventOut =
	 new ActuationSplitter();
	
	/** Personality plugin.  Never null. (direct for speed) */
	protected SourceDragMapperPlugin _plugin;
	
	/** If true, drag effect is cumulative. */
	private boolean _cumulative = true;
	
	/** Previous source object. */
	private Node _oldSource = null;
	
	/** Dummy actuation value.  (for GC) */
	private final Vector4d _actuation = new Vector4d();
	
}