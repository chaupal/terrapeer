package terrapeer.vui.j3dui.control.inputs;

import javax.media.j3d.*;
import javax.vecmath.*;

/**
This interface serves as a target for user interface move input
events. Depending on the target the input position values may be
absolute (e.g. mouse) or relative (e.g. arrow keys).
<P>
All display coordinates are in a right-handed coordinate system
with Y increasing "up".

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/
public interface InputMoveTarget {
	
	// public interface =========================================

	/**
	Called when the source moves.
	@param source Source display canvas.  Never null.
	@param pos Source position relative to source.
	*/
	public void setInputMove(Canvas3D source, Vector2d pos);
	
}