package terrapeer.vui.j3dui.control.inputs;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
An input drag event filter whose action is defined by a
personality plugin.
<P>
This type of filter can only handle the simplest of filtering
operations, where each input event method call results in a
like output event method call.  State and time dependant
generation of events can not be handled.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class InputDragFilter implements InputDragTarget {
	
	// public interface =========================================

	/**
	Constructs an InputDragFilter with personality plugin
	<plugin> and event target <target>.
	@param target Event target.  Never null.
	@param plugin Personality plugin.  Never null.
	*/
	public InputDragFilter(InputDragTarget target,
	 InputDragFilterPlugin plugin) {
	
		if(target==null) throw new
		 IllegalArgumentException("<target> is null.");
		_eventTarget = target;
		
		if(plugin==null) throw new
		 IllegalArgumentException("<plugin> is null.");
		_plugin = plugin;
	}

	/**
	Gets the personality plugin.
	@return Reference to the plugin.
	*/
	public InputDragFilterPlugin getPlugin() {
		return _plugin;
	}

	/**
	Gets the event target.
	@return Reference to the event target.
	*/
	public InputDragTarget getEventTarget() {
		return _eventTarget;
	}

	// InputDragTarget implementation
	
	public void startInputDrag(Canvas3D source, Vector2d pos) {
	
		_plugin.startInputDrag(source, pos);
		_plugin.toTargetValue(source, pos, _pos);
 
if(Debug.getEnabled()){		
Debug.println("InputDragFilter",
"DRAG:InputDragFilter:startInputDrag:" +
" inPos=" + pos +
" source=" + source +
" outPos=" + _pos);}

		_eventTarget.startInputDrag(source, _pos);
	}
	
	public void doInputDrag(Canvas3D source, Vector2d pos) {
	
		_plugin.toTargetValue(source, pos, _pos);
 
if(Debug.getEnabled()){		
Debug.println("InputDragFilter",
"DRAG:InputDragFilter:doInputDrag:" +
" inPos=" + pos +
" source=" + source +
" outPos=" + _pos);}

		_eventTarget.doInputDrag(source, _pos);
	}
	
	public void stopInputDrag(Canvas3D source, Vector2d pos) {
	
		_plugin.toTargetValue(source, pos, _pos);
		_plugin.stopInputDrag(source, pos);
	
if(Debug.getEnabled()){		
Debug.println("InputDragFilter",
"DRAG:InputDragFilter:stopInputDrag:" +
" inPos=" + pos +
" source=" + source +
" outPos=" + _pos);}

		_eventTarget.stopInputDrag(source, _pos);
	}
			
	// personal body ============================================
	
	/** Event target. */
	private InputDragTarget _eventTarget;
	
	/** Personality plugin.  Never null. */
	private InputDragFilterPlugin _plugin;
	
	/** Dummy position value.  (for GC) */
	private final Vector2d _pos = new Vector2d();
	
}