package terrapeer.vui.j3dui.control.mappers.intuitive;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;
import terrapeer.vui.j3dui.control.mappers.*;

/**
A WRM drag plugin that appears to perform WRM but involves no
source node picking.  Instead it uses target node picking to
establish a source space picking plane, similar to that used for
quasi-WRM.  During the drag the source hit point is computed
using the virtual picking plane that passes through the picked
target node hit point and is perpendicular to the specified
source space Z-axis.
<P>
Mouse-over is reported for the target node instead of the
source node as in true and quasi WRM.
<P>
During a drag this is more efficient than true WRM and does not
require that the mouse be over a source node at any time.
However, there can be only a single source node and motion is
always planar rather than conforming to some arbitrary
multi-node source space as is possible with true WRM.
<P>
The second half of WRM is performed by a SourceDragMapperPlugin.

@see SourceDragMapperPlugin 

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class PseudoWrmDragPlugin extends QuasiWrmDragPlugin {
	
	// public interface =========================================

	/**
	Constructs a PseudoWrmDragPlugin with a source space
	reference node and a pick engine containing a list of target
	nodes.  The source space node capability bits will be set
	for live use.
	@param sourceSpace Source space reference node.  If null the
	world space will be used as the source space.
	@param targetPicker Target node pick engine.  Never null.
	*/
	public PseudoWrmDragPlugin(Node sourceSpace,
	 PickEngine targetPicker) {
	
		_usePickTarget = true;
		
		_sourceSpace = sourceSpace;
		if(_sourceSpace != null) {
			_sourceSpace.setCapability(
			 Node.ALLOW_LOCAL_TO_VWORLD_READ);
		}
	
		if(targetPicker==null) throw new
		 IllegalArgumentException("'targetPicker' is null.");
		_pickEngine = targetPicker;
	
		// make target nodes usable
		AbstractList targetList = _pickEngine.getTargets();

		Iterator iter = targetList.iterator();
		while(iter.hasNext()) {
			Object target = iter.next();
			
			if(!(target instanceof Node)) throw new
			 IllegalArgumentException(
			 "'targetList' must be type Node.");
			 
			((Node)target).setCapability(
			 Node.ALLOW_LOCAL_TO_VWORLD_READ);
		}
	}

	/**
	Constructs a PseudoWrmDragPlugin with a source and a target
	space reference node.  The target space origin will be used
	as the target hit point for pick plane construction.  The
	source and target node capability bits will be set for live
	use.
	@param sourceSpace Source space reference node.  If null the
	world space will be used as the source space.
	@param targetSpace Target space reference node.  If null the
	world space will be used as the target space.
	*/
	public PseudoWrmDragPlugin(Node sourceSpace,
	 Node targetSpace) {
	
		_usePickTarget = false;
		
		_sourceSpace = sourceSpace;
		if(_sourceSpace != null) {
			_sourceSpace.setCapability(
			 Node.ALLOW_LOCAL_TO_VWORLD_READ);
		}
	
		_targetSpace = targetSpace;
		if(_targetSpace != null) {
			_targetSpace.setCapability(
			 Node.ALLOW_LOCAL_TO_VWORLD_READ);
		}
	}

	/**
	Returns a node defining the current reference target space.
	Depending on how the target space is defined and the type
	of plugin this may change each time a drag starts and a
	new target node is picked.
	@return Reference to the target space node.  If null the
	target space is the world space.
	*/
	public Node getTargetSpace() {
		return _targetSpace;
	}

	// IntuitiveDragMapperPlugin implementation
	
	public String toString() {
		return "PseudoWrmDragPlugin";
	}

	public Node getSourceSpace() {
		return _sourceSpace;
	}
			
	// personal body ============================================
	
	/** Current source space.  World space if null. */
	private Node _sourceSpace;
	
	/** Current target space.  World space if null. */
	private Node _targetSpace;
	
	/** If true picked target node is used for target space. */
	private boolean _usePickTarget;
	
	/** Target pick engine.  Null if none. */
	private PickEngine _pickEngine = null;

	/**
	Builds the picking plane based on the world hit position
	and the source node.
	@param hitPos World hit position.
	@param hitNode Node actually hit.
	@param plane Copy of the output pick plane coefficients.
	Never null.
	*/	
	protected void buildPickPlane(Point3d hitPoint,
	 Node hitNode, Vector4d pickPlane) {

		Mapper.buildPickPlane(hitPoint, getSourceSpace(),
		 pickPlane);
	}

	/**
	Projects the input drag position into the scene and returns
	the hit target node and world hit point on the node.
	Updates the target node state.
	@param source Source display canvas.
	@param pos Input display position.
	@param hitPoint Container for the copied hit point in
	the world space.  If null then none.
	@return Hit target node.  Null if none.
	*/
	protected Node pickNode(Canvas3D source,
	 Vector2d pos, Point3d hitPoint) {

		// pick target node and world hit	 
		Node hitNode;
		
		if(_usePickTarget) {
			// pick target node, get world hit point
			int index = _pickEngine.pickTarget(source, pos,
			 hitPoint);
			hitNode = (Node)_pickEngine.getPickTarget(index);

			// update target node			
			_targetSpace = hitNode;
		} else {
			// use fixed target origin as world hit point
			hitNode = _targetSpace;
			hitPoint.set(0, 0, 0);	 	
			Mapper.toTargetSpace(hitPoint, getTargetSpace(),
			 null, hitPoint);
		}
		
		return hitNode;
	}
	
}