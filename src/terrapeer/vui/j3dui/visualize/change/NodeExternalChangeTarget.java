package terrapeer.vui.j3dui.visualize.change;

import javax.media.j3d.*;
import javax.vecmath.*;

/**
This interface serves as a target for node external change
events.

@author Jon Barrilleaux,
copyright (c) 2000 Jon Barrilleaux,
All Rights Reserved.
*/
public interface NodeExternalChangeTarget {
	
	// public interface =========================================

	/**
	Called when the source node external state (position,
	orientation, etc.) changes.
	@param source Reference to the source node.  Null if none.
	@param pos Reference to the node position.  Never null.
	@param xform Reference to the node local-to-vworld transform
	of the source.  Never null.
	*/
	public void setNodeExternal(Node source, Point3d pos,
	 Transform3D xform);
	
}