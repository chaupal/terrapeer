package terrapeer.vui.j3dui.control.inputs;

/**
This interface serves as a target for user interface modifier
input events.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/
public interface InputModifierTarget {
	
	// public interface =========================================

	/**
	Called when the modifier state changes.
	@param modifier New modifier selection
	(InputSensor.MODIFIER_???).
	*/
	public void setInputModifier(int modifier);
	
}
