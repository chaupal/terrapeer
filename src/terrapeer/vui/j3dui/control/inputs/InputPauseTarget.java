package terrapeer.vui.j3dui.control.inputs;

/**
This interface serves as a target for user interface mouse-over
pause input events.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/
public interface InputPauseTarget {
	
	// public interface =========================================

	/**
	Called when a mouse-over pause begins or ends.
	@param pause True when pause begins.  False when it ends.
	*/
	public void setInputPause(boolean pause);
	
}