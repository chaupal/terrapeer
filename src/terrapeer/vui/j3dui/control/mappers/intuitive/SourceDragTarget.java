package terrapeer.vui.j3dui.control.mappers.intuitive;

import javax.media.j3d.*;
import javax.vecmath.*;

/**
This interface serves as a target for source drag events, where
the drag position is defined relative to a space defined by a
source node.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/
public interface SourceDragTarget {
	
	// public interface =========================================

	/**
	Called when a drag starts, whether or not there is a valid
	source node.
	@param source Source node.  Null if none.
	@param pos Source position relative to &LTsource&GT.
	Invalid if source is null.
	*/
	public void startSourceDrag(Node source, Vector3d pos);

	/**
	Called when the drag coordinate changes during a drag.
	Typically only called when the source node is valid.
	Also called when a drag starts but after startSourceDrag().
	@param source Source node.  Null if none.
	@param pos Source position relative to &LTsource&GT.
	Invalid if source is null.
	*/
	public void doSourceDrag(Node source, Vector3d pos);

	/**
	Called when a drag stops, whether or not there is a valid
	source node.
	@param source Source node.  Null if none.
	@param pos Source position relative to &LTsource&GT.
	Invalid if source is null.
	*/
	public void stopSourceDrag(Node source, Vector3d pos);
	
}
