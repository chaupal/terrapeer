package terrapeer.vui.j3dui.control.mappers.intuitive;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;

/**
A source drag event filter whose action is defined by a
personality plugin.
<P>
This type of filter can only handle the simplest of filtering
operations, where each input event method call results in a
like output event method call.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class SourceDragFilter implements SourceDragTarget {
	
	// public interface =========================================

	/**
	Constructs a SourceDragFilter with personality plugin
	<plugin> and event target <target>.
	@param plugin Personality plugin.  Never null.
	@param target Event target.  Never null.
	*/
	public SourceDragFilter(SourceDragTarget target,
	 SourceDragFilterPlugin plugin) {
		
		if(plugin==null) throw new
		 IllegalArgumentException("<plugin> is null.");
		_plugin = plugin;
		
		if(target==null) throw new
		 IllegalArgumentException("<target> is null.");
		_eventTarget = target;
	}

	/**
	Gets the personality plugin.
	@return Reference to the plugin.
	*/
	public SourceDragFilterPlugin getPlugin() {
		return _plugin;
	}

	/**
	Gets the event target.
	@return Reference to the event target.
	*/
	public SourceDragTarget getEventTarget() {
		return _eventTarget;
	}


	// SourceDragTarget implementation
	
	public void startSourceDrag(Node source, Vector3d pos) {
	
		_plugin.startInputDrag(source, pos);
		_plugin.toTargetValue(source, pos, _pos);
 
if(Debug.getEnabled()){		
Debug.println("SourceDragFilter",
"DRAG:SourceDragFilter:startInputDrag:" +
" inPos=" + pos +
" source=" + source +
" outPos=" + _pos);}

		_eventTarget.startSourceDrag(source, _pos);
	}
	
	public void doSourceDrag(Node source, Vector3d pos) {
	
		_plugin.toTargetValue(source, pos, _pos);
 
if(Debug.getEnabled()){		
Debug.println("SourceDragFilter",
"DRAG:SourceDragFilter:doInputDrag:" +
" inPos=" + pos +
" source=" + source +
" outPos=" + _pos);}

		_eventTarget.doSourceDrag(source, _pos);
	}
	
	public void stopSourceDrag(Node source, Vector3d pos) {
	
		_plugin.toTargetValue(source, pos, _pos);
	
if(Debug.getEnabled()){		
Debug.println("SourceDragFilter",
"DRAG:SourceDragFilter:stopInputDrag:" +
" inPos=" + pos +
" source=" + source +
" outPos=" + _pos);}

		_eventTarget.stopSourceDrag(source, _pos);
	}
			
	// personal body ============================================
	
	/** Event target. */
	private SourceDragTarget _eventTarget;
	
	/** Personality plugin.  Never null. */
	private SourceDragFilterPlugin _plugin;
	
	/** Dummy position value.  (for GC) */
	private final Vector3d _pos = new Vector3d();
	
}