package terrapeer.vui.j3dui.visualize;

import java.util.*;
import java.awt.*;
import javax.vecmath.*;
import javax.media.j3d.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
Provides visualization related constants and utilities, such
as for view geometry determination.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class Visualize {
 
 	// public constants =========================================

	/** Include no techniques (i.e. appears normal). */ 
	public static final int DO_NONE = 0x00;
	/** Include world overlay. */ 
	public static final int DO_OVER = 0x01;
	/** Include display face. */ 
	public static final int DO_FACE = 0x02;
	/** Include constant size. */ 
	public static final int DO_SIZE = 0x04;
	/** Include all techniques. */ 
	public static final int DO_ALL = 
	 DO_OVER | DO_FACE | DO_SIZE;
	/** Include world overlay and constant size. */ 
	public static final int DO_OVER_SIZE = 
	 DO_OVER | DO_SIZE;

	// public utilities =========================================

	/**
	Extracts and returns the external geometry in world space of
	the specified view.  The view position is at the center eye
	in the display canvas and its orientation is that of the
	view platform. 
	@param view The target view.  Never null.
	@param pos Return view position.  Null if none.
	@param xform Return view external transform.  Null if none.
	*/
	public static void getViewExternal(View view,
	 Point3d pos, Transform3D xform) {

		// assume only one canvas in view
		Canvas3D canvas = view.getCanvas3D(0);
		
		// get view geometry		
		if(xform != null)
			canvas.getImagePlateToVworld(xform);
		else
			canvas.getImagePlateToVworld(_xform);

		if(pos != null) { 		
			canvas.getCenterEyeInImagePlate(pos);
			
			if(xform != null)
				xform.transform(pos);
			else
				_xform.transform(pos);
		}
			
if(Debug.getEnabled()){
Debug.println("Visualize.view",
"VIEW:Visualize.getViewExternal:" +
" view=" + view +
" pos=" + pos +
" xform=" + xform);}

	}

	/**
	Extracts and returns the external geometry in world space
	of the specified node.  The node position is at its origin.
	The node must be set with ALLOW_LOCAL_TO_VWORLD_READ. 
	@param node The target node.  Never null.
	@param pos Return node position.  Null if none.
	@param xform Return node external transform. Null if none.
	*/
	public static void getNodeExternal(Node node,
	 Point3d pos, Transform3D xform) {

		// get node geometry		
		if(xform != null)
			node.getLocalToVworld(xform);
		else
			node.getLocalToVworld(_xform);

		if(pos != null) { 		
			pos.set(0, 0, 0);
			
			if(xform != null)
				xform.transform(pos);
			else
				_xform.transform(pos);
		}
			
if(Debug.getEnabled()){
Debug.println("Visualize.view",
"NODE:Visualize.getNodeExternal:" +
" node=" + node +
" pos=" + pos +
" xform=" + xform);}

	}

	/**
	Gets the orientation of an object from its external
	geometric transform. 
	@param xform The external transform. Never null.
	@param rot Copy of the output rotation. Null if none.
	@param dir Copy of the output forward direction
	(along local space -Z axis).  Null if none.
	@param up Copy of the output up direction (along
	local space Y axis).  Null if none.
	*/
	public static void getOrientation(Transform3D xform,
	 AxisAngle4d rot, Vector3d dir, Vector3d up) {
		
		if(rot != null) { 		
			xform.get(_quat);
			rot.set(_quat);
		}
		
		if(dir != null) { 		
			dir.set(0, 0, -1);
			xform.transform(dir);
			dir.normalize();
		}
		
		if(up != null) { 		
			up.set(0, 1, 0);
			xform.transform(up);
			up.normalize();
		}
			
if(Debug.getEnabled()){
Debug.println("Visualize",
"ORIENT:Visualize.getOrientation:" +
" rot=" + rot +
" dir=" + dir +
" up=" + up);}

	}

	// personal body ============================================

	/** Dummy vector.  (for GC) */	
	private static final Vector3d _vector = new Vector3d();

	/** Dummy point.  (for GC) */	
	private static final Quat4d _quat = new Quat4d();
	
	/** Dummy transform.  (for GC) */	
	private static final Transform3D _xform = new Transform3D();
	
	/** Dummy dimension.  (for GC) */	
	private static final Dimension _dim = new Dimension();

}