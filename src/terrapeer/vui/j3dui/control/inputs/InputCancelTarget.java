package terrapeer.vui.j3dui.control.inputs;

/**
This interface serves as a target for user interface cancel
input events.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/
public interface InputCancelTarget {
	
	// public interface =========================================

	/**
	Called when the cancel command occurs (e.g. Escape key
	pressed).
	*/
	public void setInputCancel();
	
}