package terrapeer.vui.j3dui.control.mappers.intuitive;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;
import terrapeer.vui.j3dui.control.mappers.*;

/**
An intuitive drag mapper plugin that performs the first half of
World Relative Mapping (WRM) by mapping from 2D input drag space
to 3D source drag space.  The source space is defined by a list
of pickable source nodes in the world (e.g. floor, walls,
terrain) in conjunction with a spatial reference for the source
coordinate system.  The current source node is the closest list
node under the drag cursor.  The current source position is the
closest hit point on the current source node as interpreted in
the context of the source space coordinate system, which is
defined by the hit node itself or an optional source space
reference node.
<P>
Source node mouse-over is reported throughout the drag.  During
a drag motion conforms to the actual source space underlying
the mouse cursor, but the cursor must always be over a source
node.
<P>
The second half of WRM is performed by a SourceDragMapperPlugin.

@see SourceDragMapperPlugin 

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class WrmDragPlugin extends IntuitiveDragMapperPlugin {
	
	// public interface =========================================

	/**
	Constructs a WrmDragPlugin with a pick engine containing a
	list of source nodes.  The source space is defined by each
	picked source node.  The capability bits of the source nodes
	will be set for live use.
	@param sourcePicker Source node pick engine.  Never null.
	*/
	public WrmDragPlugin(PickEngine sourcePicker) {
		_usePickSource = true;
	
		if(sourcePicker==null) throw new
		 IllegalArgumentException("<sourcePicker> is null.");
		_pickEngine = sourcePicker;
	
		// make source nodes usable
		AbstractList sourceList = _pickEngine.getTargets();

		Iterator iter = sourceList.iterator();
		while(iter.hasNext()) {
			Object source = iter.next();
			
			if(!(source instanceof Node)) throw new
			 IllegalArgumentException(
			 "<sourceList> must be type Node.");
			 
			((Node)source).setCapability(
			 Node.ALLOW_LOCAL_TO_VWORLD_READ);
		}
	}

	/**
	Constructs a WrmDragPlugin with a source space reference
	node and a pick engine containing a list of source nodes.
	A drag can occur on any of the picked source nodes, but
	the source space is defined by the specified source node.
	The source space node capability bits will be set for live
	use.
	@param sourceSpace Source space reference node.  If null the
	world space will be used as the source space.
	@param sourcePicker Source node pick engine.  Never null.
	*/
	public WrmDragPlugin(Node sourceSpace,
	 PickEngine sourcePicker) {

		_usePickSource = false;
	
		_sourceSpace = sourceSpace;
		if(_sourceSpace != null) {
			_sourceSpace.setCapability(
			 Node.ALLOW_LOCAL_TO_VWORLD_READ);
		}
	
		if(sourcePicker==null) throw new
		 IllegalArgumentException("'sourcePicker' is null.");
		_pickEngine = sourcePicker;
	}		

	/**
	Sets the "over target" collateral target, which is notified
	as to whether or not the pick cursor is over a source node
	during a drag.  The event target is only notified when the
	over state changes.
	@param target Event target.  Null if none.
	@return Reference to event target.
	*/
	public EnableTarget setEventTargetOverTarget(
	 EnableTarget target) {
	 
		_overTarget = target;
		return _overTarget;
	}

	// IntuitiveDragMapperPlugin implementation
	
	public String toString() {
		return "WrmDragPlugin";
	}

	public Node getSourceSpace() {
		return _sourceSpace;
	}
			
	// personal body ============================================
	
	/** Current source space.  World space if null. */
	private Node _sourceSpace;
	
	/** If true picked source node is used for source space. */
	private boolean _usePickSource;
	
	/** Source pick engine.  Never null. */
	private PickEngine _pickEngine;
	
	/** Over enable target.  Null if none. */
	private EnableTarget _overTarget;
	
	/** Enable target's over state. */
	private boolean _isOver = false;
	
	/** Dummy hit point. (for GC) */
	private final Point3d _point = new Point3d();
	
	/** Dummy hit point. (for GC) */
	private final Point3d _hitPoint = new Point3d();

	/**
	Constructs a WrmDragPlugin base class that does not
	require WRM source picking but nevertheless acts very
	much like a WRM plugin, such as a PseudoWrmDragPlugin.
	pickHitPoint() must be overridden because it will try
	to use the null pick engine.
	*/
	protected WrmDragPlugin() {
		_pickEngine = null;
	}

	/**
	Notifies the over enable target (if any) of the new over
	state <isOver> if it is different from the target's current
	over state.
	@param isOver New over state.  True if a drag is active and
	the drag cursor is over a source target.
	*/
	protected void updateOverTarget(boolean isOver) {
		if(isOver != _isOver) {
			_isOver = isOver;
			
			if(_overTarget!=null) {
				_overTarget.setEnable(_isOver);
			}
		}
	}

	/**
	Performs the mapping from input to source space.
	@param source Source display canvas.  Never null.
	@param pos Input position relative to source display.
	@param copy Container for the copied source space position
	return value.
	@return True if the return position is valid.
	*/	
	protected boolean toTargetValue(Canvas3D source,
	 Vector2d pos, Vector3d copy) {

		// pick source node and world hit, tell over	 
	 	Node hitNode = pickNode(source, pos, _hitPoint);
		updateOverTarget(hitNode!=null);
		
		/// reject bad drag
	 	if(hitNode==null) return false;
	 	
		// map hit point from world to source
		Mapper.toTargetSpace(_hitPoint, null, getSourceSpace(),
		 _point);
		copy.set(_point);
 
if(Debug.getEnabled()){
Debug.println("WrmDragPlugin",
"WRM:WrmDragPlugin:toTargetValue:" +
" sourceDsp=" + source +
" inputPos=" + pos +
" hitPoint=" + _hitPoint +
" sourcePos=" + copy +
" sourceSpc=" + _sourceSpace);}
		
		return true;
	}

	/**
	Projects the input drag position into the scene and returns
	the hit source node and world hit point on the node.
	Updates the source node state.
	@param source Source display canvas.
	@param pos Input display position.
	@param hitPoint Container for the copied hit point in
	the world space.  If null then none.
	@return Hit source node.  Null if none.
	*/
	protected Node pickNode(Canvas3D source,
	 Vector2d pos, Point3d hitPoint) {

		// pick source node, get world hit point
		int index = _pickEngine.pickTarget(source, pos,
		 hitPoint);
		 
		Node hitNode = (Node)_pickEngine.getPickTarget(index);
	 	
		// update source node		
		if(_usePickSource) _sourceSpace = hitNode;
		
		return hitNode;
	}

	// IntuitiveDragMapperPlugin implementation
	
	protected boolean startInputDrag(Canvas3D source,
	 Vector2d pos, Vector3d copy) {

if(Debug.getEnabled()){
Debug.println("WrmDragPlugin",
"WRM:WrmDragPlugin:startInputDrag:...");}
		
		return toTargetValue(source, pos, copy);
	}
	
	protected boolean doInputDrag(Canvas3D source,
	 Vector2d pos, Vector3d copy) {

if(Debug.getEnabled()){
Debug.println("WrmDragPlugin",
"WRM:WrmDragPlugin:doInputDrag:...");}
		
		return toTargetValue(source, pos, copy);
	}
	
	protected boolean stopInputDrag(Canvas3D source,
	 Vector2d pos, Vector3d copy) {

if(Debug.getEnabled()){
Debug.println("WrmDragPlugin",
"WRM:WrmDragPlugin:stopInputDrag:...");}
		
		return toTargetValue(source, pos, copy);
	}
	
}