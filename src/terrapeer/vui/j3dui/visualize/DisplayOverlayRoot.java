package terrapeer.vui.j3dui.visualize;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.utils.app.*;
import terrapeer.vui.j3dui.control.actuators.*;
import terrapeer.vui.j3dui.control.actuators.groups.*;
import terrapeer.vui.j3dui.visualize.change.*;

/**
This class is a branch group that helps support "display
overlay" visualization behavior.  It encapsulates a transform
chain that defines a 2D display overlay space parallel to the
view display plane at a normalized distance of 1.0 from the eye.
The space is scaled such that child objects are positioned in
units of display pixels.  The space is offset such that the
view space origin is offset from the display space origin
(center of display window) by the view's display-view offset.
As a branch group it can also be used as a root for picking
objects in display overlay.
<P>
This group attaches itself directly to a host view.
To place an object in display overlay the object can be added
as a child of the DisplayOverlayRoot if overlap order is not
important, or it can be added as a child of a
DisplayOverlayGroup.  Non-planar child objects may
cause unpredictable results.
<P>
For this object to work properly all of its event inputs must
be connected and they must have received initial events.
<UL>
<LI>ViewInternalChangeTarget: Internal state of the view
observing this node.  Only works reliably for one view.
</UL>
<P>
Having to use separate nodes for the overlay "root" and "group"
is a kludge necessitated by a bug (feature?)in Java 3D 1.1.2
where overlapping transparent objects, such as those using
transparent textures, will only render correctly if 1) they are
under an OrderedGroup in proper render order and 2) they are in
correct Z position relative to the eye for correct overlap.

@author Jon Barrilleaux,
copyright (c) 1999-200 Jon Barrilleaux,
All Rights Reserved.
*/

public class DisplayOverlayRoot extends BranchGroup
 implements ViewChangeTarget {
 
	// public interface =========================================

	/**
	Constructs a DisplayOverlayRoot.
	@param view Host view.  Never null.
	*/
	public DisplayOverlayRoot(AppView view) {
		this(view, null);
	}

	/**
	Constructs a DisplayOverlayRoot with a child node.
	@param view Host view.  Never null.
	@param node First child node of this group.  Null if none.
	*/
	public DisplayOverlayRoot(AppView view, Node node) {
				
		if(view==null) throw new
		 IllegalArgumentException("<view> is null.");
		_view = view;
		
		// build overlay core
		_core = new DisplayOverlayCore();
	
		// build transform chain
		_chain = new TransformChain(2, node);
		 
		_scale = new Actuator(new SMTransformGroupPlugin(
		 new ScalePlugin(_chain.getTransform(0))));
		 
		_translation = new Actuator(new SMTransformGroupPlugin(
		 new TranslationPlugin(_chain.getTransform(1))));

		// attach chain to this and this to view
		this.addChild(_chain);
		_view.addNode(this);
	}
	
	/**
	Gets this root's parent view.
	@return Reference to the view.
	*/
	public AppView getView() {
		return _view;
	}
	
	/**
	Adds the specified node to this root's display overlay
	space (i.e. the tail group in it's transform chain).
	@param child Node added.  Ignored if null.
	@return Reference to child node.
	*/
	public Node addNode(Node child) {
		return _chain.addNode(child);
	}

	// ViewChangeTarget implementation
	
	public void setViewExternal(View source, Point3d pos,
	 Transform3D xform) {
		// do nothing
	}
	
	public void setViewInternal(View source, double fov,
	 double vsf, double dsf, Vector2d dvo, Vector2d ds,
	 Vector2d ss, Vector2d sr) {
	
if(Debug.getEnabled()){
Debug.println("DisplayOverlayRoot",
"VIEW:DisplayOverlayRoot.setViewExternal:" +
" source=" + source +
" fov=" + fov +
" vsf=" + vsf +
" dsf=" + dsf +
" dvo=" + dvo +
" ds=" + ds +
" ss=" + sr);}
	
		_core.setViewInternal(source, fov, vsf, dsf, dvo, ds,
	 	 ss, sr);

		updateSpace();
	}
			
	// personal body ============================================
	
	/** Parent view. Never null. */
	private AppView _view;
	
	/** Display overlay core. Never null. */
	private DisplayOverlayCore _core;
	
	/** Transform chain. Never null. */
	private TransformChain _chain;
	
	/** Scale actuator in chain. Never null. */
	private Actuator _scale;
	
	/** Translation actuator in chain. Never null. */
	private Actuator _translation;
	
	/** Dummy vector.  (for GC) */
	private Vector2d _vector2D = new Vector2d();
	
	/** Dummy actuation.  (for GC) */
	private Vector4d _actuation = new Vector4d();

	/**
	Updates display space scale factor and X-Y origin position
	for the base display overlay Z plane.  Z position is set to
	the normalized display distance.
	*/	
	protected void updateSpace() {

		// get normalized display parameters
		double scale = _core.getNormalScale();
		_core.getNormalOffset(_vector2D);
		
if(Debug.getEnabled()){
Debug.println("DisplayOverlayRoot.verbose",
"SPACE:DisplayOverlayRoot.updateSpace:" +
" scale=" + scale +
" offset=" + _vector2D);}

		// update display normalization
		/// only scale X-Y; Z set by group in world space
		_actuation.set(scale, scale, 1.0, 0);
		_scale.initActuation(_actuation);

		// offset X-Y; Z set to normal distance		
		_actuation.set(_vector2D.x, _vector2D.y,
		 -_core.getNormalDist(), 0);
		_translation.initActuation(_actuation);
	}
}