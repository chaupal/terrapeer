package terrapeer.vui.j3dui.visualize.change;

import javax.media.j3d.*;
import javax.vecmath.*;

/**
This interface serves as a target for view internal  and
external change events.  Only some targets only need one or the
other, the majority of targets require both so the two are
combined for convenience.

@author Jon Barrilleaux,
copyright (c) 1999-2000 Jon Barrilleaux,
All Rights Reserved.
*/
public interface ViewChangeTarget {
	
	// public interface =========================================
	
	/**
	Called when the source view external geometry (position,
	orientation, etc.) changes.
	@param source Reference to the source view.  Null if none.
	@param pos Reference to the view position.  Never null.
	@param xform Reference to the source view's local-to-vworld
	transform.  Never null.
	*/
	public void setViewExternal(View source, Point3d pos,
	 Transform3D xform);

	/**
	Called when the source view internal geometry (FOV, display
	size, etc.) changes.
	@param source Reference to the source view.  Null if none.
	@param fov The horizontal field of view in radians.  0 for
	parallel projection.
	@param vsf The view scale factor.
	@param dsf The display scale factor.
	@param dvo Reference to the display-to-view offset in pixels.
	Never null.
	@param ds Reference to the display size in pixels.
	Never null.
	@param ss Reference to the screen size in pixels.
	Never null.
	@param sr Reference to the screen resolution in pixels per
	meter.  Never null.
	*/
	public void setViewInternal(View source, double fov,
	 double vsf, double dsf, Vector2d dvo, Vector2d ds,
	 Vector2d ss, Vector2d sr);
	
}