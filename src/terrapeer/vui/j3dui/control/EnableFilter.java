package terrapeer.vui.j3dui.control;

import java.util.*;

/**
An enable event filter that can change the sense of its input
enable event (i.e. logical "not").

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class EnableFilter extends EnableTrigger
 implements EnableTarget {
	
	// public interface =========================================

	/**
	Constructs an EnableFilter with no event targets.
	*/
	public EnableFilter(EnableTarget target) {
		super(target);
	}
	
	// EnableTarget implementation
	
	public void setEnable(boolean enable) {
		if(enable)
			sendTriggerTrue();
		else
			sendTriggerFalse();
	}
			
	// personal body ============================================

}
