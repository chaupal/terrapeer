package terrapeer.vui.j3dui.control.mappers.intuitive;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;
import terrapeer.vui.j3dui.control.mappers.*;

/**
A WRM drag plugin that picks the source hit point and node
only at the start of the drag instead of throughout the drag.
During the drag the source hit point is computed using a virtual
plane that passes through the picked source node hit point
and is perpendicular to the specified source space Z-axis.
<P>
Source node mouse-over is only reported at the start of the
drag instead of throughout the drag as in true WRM.
<P>
During a drag this is more efficient than true WRM and does not
require that the mouse be over a source node except at the
start.  The motion, however, is always planar rather than
conforming to some arbitrary multi-node source space as is
possible with true WRM.
<P>
The second half of WRM is performed by a SourceDragMapperPlugin.

@see SourceDragMapperPlugin 

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class QuasiWrmDragPlugin extends WrmDragPlugin {
	
	// public interface =========================================

	/**
	Constructs a QuasiWrmDragPlugin with a pick engine
	containing a list of source nodes.  The source space is
	defined by each picked source node.  The capability bits
	of the source nodes will be set for live use.
	@param sourcePicker Source node pick engine.  Never null.
	*/
	public QuasiWrmDragPlugin(PickEngine sourcePicker) {
		super(sourcePicker);
	}

	/**
	Constructs a QuasiWrmDragPlugin with a source space reference
	node and a pick engine containing a list of source nodes.
	The source space node capability bits will be set for live
	use.
	@param sourceSpace Source space reference node.  If null the
	world space will be used as the source space.
	@param sourcePicker Source node pick engine.  Never null.
	*/
	public QuasiWrmDragPlugin(Node sourceSpace,
	 PickEngine sourcePicker) {
	 
		super(sourceSpace, sourcePicker);
	}

	// IntuitiveDragMapperPlugin implementation
	
	public String toString() {
		return "QuasiWrmDragPlugin";
	}
			
	// personal body ============================================
	
	/** True if a valid drag is active. */
	private boolean _isDrag = false;
	
	/** Pick ray origin. */
	private Point3d _pickRayOrg = new Point3d();
	
	/** Pick ray direction. */
	private Vector3d _pickRayDir = new Vector3d();
	
	/** Pick plane. */
	private Vector4d _pickPlane = new Vector4d();
	
	/** Dummy point. (for GC) */
	private final Point3d _point = new Point3d();
	
	/** Dummy hit point. (for GC) */
	private final Point3d _hitPoint = new Point3d();


	/**
	Constructs a QuasiWrmDragPlugin base class that does not
	require WRM source picking but nevertheless acts very
	much like a quasi-WRM plugin, such as a PseudoWrmDragPlugin.
	pickHitPoint() must be overridden because it will try
	to use the null pick engine.
	*/
	protected QuasiWrmDragPlugin() {}

	/**
	Builds the picking plane based on the world hit position
	and the true hit node.
	@param hitPos World hit position.
	@param hitNode Node actually hit.
	@param plane Copy of the output pick plane coefficients.
	Never null.
	*/	
	protected void buildPickPlane(Point3d hitPoint,
	 Node hitNode, Vector4d pickPlane) {

		Mapper.buildPickPlane(hitPoint, hitNode, pickPlane);
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

		// reject bad drag
	 	if(!_isDrag) return false;
		
		// hit world pick plane	 
		Mapper.buildPickRay(source, pos, _pickRayOrg,
		 _pickRayDir);
		double hitDist = Mapper.hitPickPlane(_pickRayOrg,
		 _pickRayDir, _pickPlane, _hitPoint);
		
		if(hitDist <= 0.0) return false; // no hit
		
		// map hit point from world to source	 
		Mapper.toTargetSpace(_hitPoint, null, getSourceSpace(),
		 _point);
		copy.set(_point);
 
if(Debug.getEnabled()){
Debug.println("QuasiWrmDragPlugin",
"WRM:QuasiWrmDragPlugin:toTargetValue:" +
" sourceDsp=" + source +
" inputPos=" + pos +
" hitPoint=" + _hitPoint +
" sourcePos=" + copy +
" sourceSpc=" + getSourceSpace() +
" pickPlane=" + _pickPlane +
" rayOrg=" + _pickRayOrg +
" rayDir=" + _pickRayDir +
" hitDist=" + hitDist);}
		
		return true;
	}

	// IntuitiveDragMapperPlugin implementation
	
	protected boolean startInputDrag(Canvas3D source,
	 Vector2d pos, Vector3d copy) {

	 	_isDrag = false;

		// pick source node and world hit, tell over	 
	 	Node hitNode = pickNode(source, pos, _hitPoint);
		updateOverTarget(hitNode!=null);
		
		/// start drag unless pick is bad
	 	if(hitNode==null) return false;
	 	_isDrag = true;
	 			
		// build picking plane
		buildPickPlane(_hitPoint, hitNode, _pickPlane);	 

		// perform drag using world picking plane		
 
if(Debug.getEnabled()){
Debug.println("QuasiWrmDragPlugin",
"WRM:QuasiWrmDragPlugin:startInputDrag:...");}

		return toTargetValue(source, pos, copy);
	}
	
	protected boolean doInputDrag(Canvas3D source,
	 Vector2d pos, Vector3d copy) {

if(Debug.getEnabled()){
Debug.println("QuasiWrmDragPlugin",
"WRM:QuasiWrmDragPlugin:doInputDrag:...");}
		
		return toTargetValue(source, pos, copy);
	}
	
	protected boolean stopInputDrag(Canvas3D source,
	 Vector2d pos, Vector3d copy) {

if(Debug.getEnabled()){
Debug.println("QuasiWrmDragPlugin",
"WRM:QuasiWrmDragPlugin:stopInputDrag:...");}
		
		return toTargetValue(source, pos, copy);
	}
	
}