package terrapeer.vui.j3dui.control.inputs;

/**
This interface serves as a target for user interface input
button events.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/
public interface InputButtonTarget {
	
	// public interface =========================================

	/**
	Called when the button state changes.  This event occurs
	in conjunction with click events.
	@param button New button selection (InputSensor.BUTTON_???).
	*/
	public void setInputButton(int button);

	/**
	Called when a button click occurs.  All clicks in a series
	(single, double, etc.) will be reported, not just the final
	click and its count.  This event occurs in conjunction with
	button events. 
	@param click New click selection (InputSensor.CLICK_???).
	*/
	public void setInputClick(int click);
	
}