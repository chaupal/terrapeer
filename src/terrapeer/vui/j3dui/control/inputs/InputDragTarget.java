package terrapeer.vui.j3dui.control.inputs;

import javax.media.j3d.*;
import javax.vecmath.*;

/**
This interface serves as a target for user interface input
drag events.
<P>
All display coordinates are in a right-handed coordinate system
with Y increasing "up".

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/
public interface InputDragTarget {
	
	// public interface =========================================

	/**
	Called when a drag starts.
	@param source Source display canvas.  Never null.
	@param pos Source position relative to source.
	*/
	public void startInputDrag(Canvas3D source, Vector2d pos);

	/**
	Called when the drag coordinate changes during a drag.
	Also called when a drag starts but after startDrag() and
	when a drag stops but before stopDrag().
	@param source Source display canvas.  Never null.
	@param pos Source position relative to source.
	*/
	public void doInputDrag(Canvas3D source, Vector2d pos);

	/**
	Called when a drag stops.
	@param source Source display canvas.  Never null.
	@param pos Source position relative to source.
	*/
	public void stopInputDrag(Canvas3D source, Vector2d pos);
	
}