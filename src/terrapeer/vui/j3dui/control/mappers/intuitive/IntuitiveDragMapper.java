package terrapeer.vui.j3dui.control.mappers.intuitive;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.inputs.*;

/**
A coordinate mapper that translates a 2D input drag space
position into an intuitive 3D source drag space position as
defined by a personality plugin.
<P>
By definition, input to source space transformation is
point not vector-based, which means that position is mapped
instead of direction and magnitude.

@author Jon Barrilleaux,
copyright (c) 1999-2000 Jon Barrilleaux,
All Rights Reserved.
*/

public class IntuitiveDragMapper implements InputDragTarget {
	
	// public interface =========================================

	/**
	Constructs an IntuitiveDragMapper with a personality plugin.
	@param plugin Drag mapper plugin.  Never null.
	*/
	public IntuitiveDragMapper(
	 IntuitiveDragMapperPlugin plugin) {
		
		if(plugin==null) throw new
		 IllegalArgumentException("'plugin' is null.");
		_plugin = plugin;
	}

	/**
	Constructs an IntuitiveDragMapper with an event target and
	personality plugin.
	@param target Event target.  Null if none.
	@param plugin Drag mapper plugin.  Never null.
	*/
	public IntuitiveDragMapper(SourceDragTarget target,
	 IntuitiveDragMapperPlugin plugin) {
	 	this(plugin);
		if(target!=null) getEventOut().addEventTarget(target);
	}
	
	/**
	Gets the event output splitter.  Use it to add and
	remove event targets.
	@return Reference to the event out splitter.  Never null.
	*/
	public SourceDragSplitter getEventOut() {
		return _eventOut;
	}

	/**
	Gets the personality plugin.
	@return Reference to the plugin.
	*/
	public IntuitiveDragMapperPlugin getPlugin() {
		return _plugin;
	}

	// InputDragTarget implementation
	
	public void startInputDrag(Canvas3D source, Vector2d pos) {
	
		boolean isValid = _plugin.startInputDrag(source, pos,
		 _sourcePos);
		
if(Debug.getEnabled()){
Debug.println("IntuitiveDragMapper",
"DRAG:IntuitiveDragMapper:startInputDrag:" +
" sourceDsp=" + source +
" inputPos=" + pos +
" isValid=" + isValid +
" sourcePos=" + _sourcePos +
" sourceSpc=" + _plugin.getSourceSpace());}

//		if(isValid) {
			_eventOut.startSourceDrag(
			 _plugin.getSourceSpace(), _sourcePos);
//		}
	}
	
	public void doInputDrag(Canvas3D source, Vector2d pos) {
	
		boolean isValid = _plugin.doInputDrag(source, pos,
		 _sourcePos);
		
if(Debug.getEnabled()){
Debug.println("IntuitiveDragMapper",
"DRAG:IntuitiveDragMapper:doInputDrag:" +
" sourceDsp=" + source +
" inputPos=" + pos +
" isValid=" + isValid +
" sourcePos=" + _sourcePos +
" sourceSpc=" + _plugin.getSourceSpace());}

		if(isValid) {
			_eventOut.doSourceDrag(
			 _plugin.getSourceSpace(), _sourcePos);
		}
	}
	
	public void stopInputDrag(Canvas3D source, Vector2d pos) {
	
		boolean isValid = _plugin.stopInputDrag(source, pos,
		 _sourcePos);
		
if(Debug.getEnabled()){
Debug.println("IntuitiveDragMapper",
"DRAG:IntuitiveDragMapper:stopInputDrag:" +
" sourceDsp=" + source +
" inputPos=" + pos +
" isValid=" + isValid +
" sourcePos=" + _sourcePos +
" sourceSpc=" + _plugin.getSourceSpace());}

//		if(isValid) {
			_eventOut.stopSourceDrag(
			 _plugin.getSourceSpace(), _sourcePos);
//		}
	}
			
	// personal body ============================================
	
	/** Event out splitter. Never null. */
	private SourceDragSplitter _eventOut =
	 new SourceDragSplitter();
	
	/** Wrm plugin.  Never null. */
	private IntuitiveDragMapperPlugin _plugin;
	
	/** Dummy source position.  (for GC) */
	private final Vector3d _sourcePos = new Vector3d();
	
}