package terrapeer.vui.j3dui.control.mappers;

import java.util.*;
import javax.vecmath.*;
import javax.media.j3d.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.utils.PickUtils;
import terrapeer.vui.j3dui.control.inputs.Input;

/**
Encapsulates the logic for picking a target node in the scene.
For a target node to be pickable it must lie under the pick
root in the scene graph, its ENABLE_PICK_REPORTING capability
must be set, its leaf shape nodes must be set pickable
(which is true by default), and for geometry picking, the leaf
shape geometry must be intersectable.
<P>
Picking can occur using the target node bounds or its actual
geometry.  Geometry picking is generally more precise but
slower.  Geometry picking must be used in order to compute
the hit point.  Picking can occur on only the closest pickable
object, whether in the target list or not, or it can be
performed on all pickable objects.  If the latter then the
closest object in the target list will be hit even if it lies
behind a non-target object that is pickable.
<P>
The order of pickable targets in the target list can be
significant if more than one target is in a given scene graph
path, such as when a parent and a descendant are pick candidates.
The target list is searched starting from the beginning, which
is the order of pick precedence.  Thus, for the child to be
picked instead of its parent, the child must be in the list
before the parent.
<P>
Some attention has been given to efficiency since
this utility provides the basis of continuous "is over" picking
(i.e. which node is the mouse over) and not just discrete "is
active" picking (i.e. which node did the mouse click on).

<P><B>Notes:</B><BR>
Those parameters that can change rapidly as a result of mouse
movement are exposed as method arguments in pickTarget().  The
rest of the parameters needed for picking are maintained as
object state.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class PickEngine {

	// public interface =========================================

	/**
	Constructs a PickEngine for determining if the mouse is
	over one of the pick target in the target list.  For a target
	node to be pickable it must lie under the pick root in the
	scene graph it must be configured for picking, which is done
	automatically.  The order of targets in the list can be
	significant.
	@param pickRoot Pick root.  Never null.
	@param targets List of target Nodes.  Never null but can be
	empty.
	*/
	public PickEngine(BranchGroup root, AbstractList targets) {
		setRoot(root);
		setTargets(targets);
	}

	/**
	Sets the root node for picking.  For a target node to be
	picked it must lie under this root in the scene graph.
	@param root Pick root.  Never null.
	*/
	public void setRoot(BranchGroup root) {
		if(root==null) throw new
		 IllegalArgumentException("'root' is null.");

		_root = root;
	}

	/**
	Gets the root node.
	@return List of targets of type Object.
	*/
	public BranchGroup getRoot() {
		return _root;
	}

	/**
	Sets a reference to the target node list.  Any targets
	found in the list will be automatically enabled for pick
	reporting.  As such, the targets must not be in a live scene
	graph or must have been configured for live configuration
	of pick reporting.  The user is responsible for assuring
	that target leaf nodes are also pickable (such as with
	PickUtils.setGeoPickable()) and that targets added
	subsequently to the list are enabled for pick reporting.
	@param targets List of targets of type Object (but only
	targets of type Node are pickable).  Must not be null but
	can be empty.
	*/
	public void setTargets(AbstractList targets) {
		if(targets==null) throw new
		 IllegalArgumentException("'targets' is null.");

		_targets = targets;

		Iterator iter = _targets.iterator();
		while(iter.hasNext()) {
			Node target = (Node)iter.next();
			target.setCapability(Node.ENABLE_PICK_REPORTING);
		}
	}

	/**
	Gets the list of target nodes.
	@return List of targets of type Object.
	*/
	public AbstractList getTargets() {
		return _targets;
	}

	/**
	Sets whether or not picking is performed using "bounds"
	or "geometry".  The default is "geometry".  In general,
	bounds picking is faster but often much less precise.
	@param useBounds If true, the object bounds is tested.  If
	false, the object geometry is tested.
	*/
	public void setUseBounds(boolean useBounds) {
		_useBounds = useBounds;
	}

	/**
	Sets whether or not target picking is performed using the
	"closest" or "all" hit objects under the pick cursor.  The
	default is "closest".  Using "all" is more expensive than
	"closest".
	@param hitAll If true, all hit objects are tested for a
	target hit.  If false only the closest hit object is tested.
	*/
	public void setHitAll(boolean hitAll) {
		_hitAll = hitAll;
	}

	/**
	Sets wether or not picking is performed using a "cheat".
	@param useCheat If true, picking sometimes cheats.
	@see PickEngine#pickTarget
	@see PickEngine#pickTargetCheat
	*/
	public void setUseCheat(boolean useCheat) {
		_useCheat = useCheat;
	}

	/**
	Returns the pick target corresponding to target index
	<index> in the pick target list.
	@param index Target object index.
	@return The target object corresponding to <index> or null
	if <index> is -1 or out of range.
	*/
	public Object getPickTarget(int index) {
		if(index < 0 || index >= _targets.size())
			return null;
		else
			return _targets.get(index);
	}

	/**
	Determines which target node, if any, the pick cursor is
	over in the pick canvas, and returns its index and possibly
	the hit point.  If cheating is enabled pickTargetCheat()
	is used instead of pickTargetFair() if the pick cursor is
	in the left quarter of the display.
	@see PickEngine#pickTargetFair
	@see PickEngine#pickTargetCheat
	@see PickEngine#setUseCheat
	*/
	public int pickTarget(Canvas3D canvas, Tuple2d pos,
	 Point3d hitPoint) {

if(Debug.getEnabled()){
Debug.println("PickEngine",
"PickEngine:pickTarget: pos=" + pos);}

		if(_useCheat && (pos.x < canvas.getSize().width/4)) {
			return pickTargetCheat(canvas, pos, hitPoint);
		} else {
			return pickTargetFair(canvas, pos, hitPoint);
		}

	}

	/**
	Determines which target node, if any, the mouse is "over"
	in the pick canvas, and returns it and the hit point on
	the target.

	<P><B>Notes:</B><BR>
	Throws a pick ray from the canvas "center eye" through the
	mouse position in the pick canvas into the scene and tests
	if the ray hits a pickable target node under the pick root.

	@param canvas Canvas that the pick cursor is in.  Never null.
	@param pos Cooked position of pick cursor in pick canvas.
	@param hitPoint If not null, geometry picking is used, and
	a target was hit, will be set to the point on the target
	in the world space that the pick cursor is over.
	@return The index of the target node in the pick list that
	the mouse is over, or -1 if none.
	*/
	public int pickTargetFair(Canvas3D canvas, Tuple2d pos,
	 Point3d hitPoint) {

		// build pick ray
		Mapper.buildPickRay(canvas, pos, _pickRayOrg,
		 _pickRayDir);
		_pickRay.set(_pickRayOrg, _pickRayDir);

if(Debug.getEnabled()){
Debug.println("PickEngine",
"PickEngine:pickTargetFair:" +
" pickOrg=" + _pickRayOrg +
" pickDir=" + _pickRayDir +
" pickRay=" + _pickRay);}

		// do the pick
		int targetI = -1;

		if(_useBounds) {
			targetI = pickTargetBounds(_pickRay);
		} else {
			targetI = pickTargetGeometry(_pickRay, _hitDist);

			// compute the hit point from the hit distance
			if(hitPoint!=null) {
				hitPoint.scaleAdd(_hitDist[0],
				 _pickRayDir, _pickRayOrg);
			}
		}

if(Debug.getEnabled()){
Debug.println("PickEngine",
"PickEngine:pickTargetFair:" +
" targetI=" + targetI +
" hitPoint=" + hitPoint);}

		return targetI;
	}

	/**
	Similar in use to pickTargetFair() but cheats instead of
	actually performing a pick.  The hit target is simply
	determined as a function of the pick cursor Y position.
	The hit point will not be set.

	<P><B>Notes:</B><BR>
	This form of pickTarget() is useful for side-by-side
	evaluation of the efficiency of the java3D picking process.
	@see PickEngine#setUseCheat
	@see PickEngine#pickTargetFair
	*/
	public int pickTargetCheat(Canvas3D canvas, Tuple2d pos,
	 Point3d hitPoint) {

		int trgI = (int)((double)_targets.size() *
		 (Math.abs(pos.y) / (double)canvas.getSize().height));

if(Debug.getEnabled()){
Debug.println("PickEngine.targets",
"TARGETS:PickEngine:pickTargetCheat:" +
" trgI=" + trgI +
" pos=" + pos +
" height=" + canvas.getSize().height +
" count=" + _targets.size());}

		if(trgI<0)
			trgI = 0;
		else if(trgI>_targets.size()-1)
			trgI = _targets.size() - 1;

if(Debug.getEnabled()){
Debug.println("PickEngine.targets",
"    hit a target=" + trgI + " trg=" + _targets.get(trgI));}

		return trgI;
	}

	// personal body ============================================

	/** Pick root for pick targets.  Never null. */
	private BranchGroup _root;

	/** Pick targets.  Never null */
	private AbstractList _targets = null;

	/** True if pick using target bounds instead of geometry. */
	private boolean _useBounds = false;

	/** True if pick using all hit instead of closest. */
	private boolean _hitAll = false;

	/** True if pick can sometimes cheat instead of being fair. */
	private boolean _useCheat = false;

	/** Dummy pick ray.  (for GC) */
	private final PickRay _pickRay = new PickRay();

	/** Dummy pick ray origin.  (for GC) */
	private final Point3d _pickRayOrg = new Point3d();

	/** Dummy pick ray direction.  (for GC) */
	private final Vector3d _pickRayDir = new Vector3d();

	/** Dummy single hit distance.  (for GC) */
	private final double[] _hitDist = {0.0};

	/** Dummy single hit path.  (for GC) */
	private final SceneGraphPath[] _oneHitPath =
	 {new SceneGraphPath()};

	/**
	The guts of bounds picking.
	@param pickRay The pick ray.
	@return Index of the hit target, or -1 if none.
	*/
	protected int pickTargetBounds(PickRay pickRay) {

		SceneGraphPath[] hitPaths = _oneHitPath;

		// throw pick ray at object bounds under pick root
		if(_hitAll) {
			hitPaths = _root.pickAllSorted(pickRay);

			// if nothing hit, quit
			if(hitPaths==null) return -1;
		} else {
			hitPaths[0] = _root.pickClosest(pickRay);

			// if nothing hit, quit
			if(hitPaths[0]==null) return -1;
		}

if(Debug.getEnabled()){
Debug.println("PickEngine.hits",
"HITS:PickEngine:pickTargetBounds:" +
" hitCount=" + hitPaths.length);}

		// test hits against targets
		for(int hitI=0; hitI<hitPaths.length; hitI++) {

if(Debug.getEnabled()){
Debug.println("PickEngine.hits",
"  hitI=" + hitI + " hit=" + hitPaths[hitI].getObject());}

			int targetI = findHitTarget(hitPaths[hitI]);

			if(targetI == -1) {
				// missed a target
				if(!_hitAll) {
					// quit after closest hit
					return -1;
				}
			} else {
				// hit a target, quit

if(Debug.getEnabled()){
Debug.println("PickEngine.hits",
"    hit a target!");}

				return targetI;
			}
		}

		return -1;
	}

	/**
	The guts of geometry picking.
	@param pickRay The pick ray.
	@ hitDist Copy of the hit point distance.  Never null.
	@return Index of the hit target, or -1 if none.
	*/
	protected int pickTargetGeometry(PickRay pickRay,
	 double[] hitDist) {

		// throw pick ray at object bounds under pick root
		SceneGraphPath hitPaths[] = _root.pickAllSorted(
		 pickRay);

		/// if nothing hit, quit
		if(hitPaths==null) {

if(Debug.getEnabled()){
Debug.println("PickEngine.hits",
"HITS:PickEngine:pickTargetGeometry:" +
" hitCount=0");}

			return -1;
		}

if(Debug.getEnabled()){
Debug.println("PickEngine.hits",
"HITS:PickEngine:pickTargetGeometry:" +
" hitCount=" + hitPaths.length);}

		/// test hits against geometry and targets
		for(int hitI=0; hitI<hitPaths.length; hitI++) {

			SceneGraphPath hitPath = hitPaths[hitI];
			Node hitNode = hitPath.getObject();
			boolean isHit = false;

if(Debug.getEnabled()){
Debug.println("PickEngine.hits",
"  hitI=" + hitI + " hit=" + hitNode);}

try
{
        // intersect pick ray with hit geometry, get distance
        if (hitNode instanceof Shape3D)
        {
          isHit = ((Shape3D)hitNode).intersect(
              hitPath, pickRay, hitDist);
        }
        else if (hitNode instanceof Morph)
        {
          isHit = ((Morph)hitNode).intersect(
              hitPath, pickRay, hitDist);
        }
        else
        {
          throw new RuntimeException(
              "Unknown Node type.");
        }
}
catch(javax.media.j3d.CapabilityNotSetException cnse)
{
  //System.err.println("PickEngine javax.media.j3d.CapabilityNotSetException");
}

if(hitDist[0]>1e3 && Debug.getEnabled()){
Debug.println("PickEngine.alert",
"ALERT: PickEngine:pickTargetGeometry: big dist!" +
" isHit=" + isHit +
" hitI=" + hitI +
" dist=" + hitDist[0] +
" ray=" + pickRay +
" node=" + hitNode);
}

			if(isHit) {

if(Debug.getEnabled()){
Debug.println("PickEngine.hits",
"    hit a shape!");}

				// hit a shape, check for target
				int targetI = findHitTarget(hitPath);

				if(targetI != -1) {

if(Debug.getEnabled()){
Debug.println("PickEngine.hits",
"      hit a target!");}

					// hit a target, quit
					return targetI;
				} else {
					// missed a target
					if(!_hitAll) {
						// quit after first (closest) hit
						return -1;
					}
				}
			} // isHit
		} // hitI
		return -1;
	}

	/**
	Tests if a target in the target list is in the hit
	path <hitPath>.
	@return Index of the hit target, or -1 if no target
	was hit.
	*/
	protected int findHitTarget(SceneGraphPath hitPath) {

if(Debug.getEnabled()){
Debug.println("PickEngine.targets",
"TARGETS:PickEngine:findHitTarget:" +
" trgCount=" + _targets.size() +
" hitPath=" + hitPath);}

		Iterator iter = _targets.iterator();
		int targetI = -1;
		while(iter.hasNext()) {
			targetI++;
			Object target = iter.next();

if(Debug.getEnabled()){
Debug.println("PickEngine.targets",
"  trgI=" + targetI + " trg=" + target);}

			if(Mapper.isInPath(hitPath, target)) {

if(Debug.getEnabled()){
Debug.println("PickEngine.targets",
"    hit a target!");}

				return targetI;
			}
		}

if(Debug.getEnabled()){
Debug.println("PickEngine.targets", "");}

		return -1;
	}

}
