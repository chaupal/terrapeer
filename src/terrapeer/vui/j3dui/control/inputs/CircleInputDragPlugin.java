package terrapeer.vui.j3dui.control.inputs;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
An input drag filter plugin that converts a planar position
into a circular gesture about the source space origin.
<P>
The output drag value is defined as follows:
<UL>
<LI>X - Angle of the gesture, in radians.
<LI>Y - Radius of the gesture (distance from origin).
</UL>

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class CircleInputDragPlugin
 extends InputDragFilterPlugin {
	
	// public interface =========================================

	/**
	Constructs a CircleInputDragPlugin.
	*/
	public CircleInputDragPlugin() {}

	// InputDragFilterPlugin implementation
	
	public String toString() {
		return "CircleInputDragPlugin";
	}
			
	// personal body ============================================

	// InputDragFilterPlugin implementation
	
	protected Vector2d toTargetValue(Canvas3D source,
	 Vector2d value, Vector2d copy) {
		
		copy.set(Math.atan2(value.y, value.x), value.length());
		return copy;
	}
	
}
