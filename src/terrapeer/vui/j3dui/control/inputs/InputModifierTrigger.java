package terrapeer.vui.j3dui.control.inputs;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;

/**
An enable trigger that monitors modifier events.  The trigger
condition is met if any of the setModifiers() modifier flags
are true.
<P>
When the downstream enable chain is completed use
initEventTarget() to initialize its state.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class InputModifierTrigger extends EnableTrigger
 implements InputModifierTarget {
	
	// public interface =========================================

	/**
	Constructs an InputModifierTrigger with event target <target>.
	@param target Event target.  Never null.
	*/
	public InputModifierTrigger(EnableTarget target) {
		super(target);
	}

	/**
	Constructs an InputModifierTrigger with event target <target>,
	modifier flags <modifiers>, and output sense <negate>.
	@param target Event target.  Never null.
	@param modifiers Modifier flags (Input.MODIFIER_???).
	@param sense Value output when condition is met.
	*/
	public InputModifierTrigger(EnableTarget target,
	 int modifiers, boolean sense) {
		super(target);
		setModifiers(modifiers);
		setTargetSense(sense);
		initEventTarget(modifiers==Input.MODIFIER_NONE);
	}

	/**
	Sets the modifier flags.  The default is MODIFIER_ALL.
	@param modifiers Modifier flags (Input.MODIFIER_???).  If
	MODIFIER_IGNORE no events will be generated.
	*/
	public void setModifiers(int modifiers) {
		_modifiers = modifiers;
	}

	// InputModifierTarget implementation
	
	public void setInputModifier(int modifier) {
		
if(Debug.getEnabled()){Debug
.println("InputModifierTrigger",
"ENABLE:InputModifierTrigger:setInputModifier:" +
" modifier=" + modifier +
" modifierFlags=" + _modifiers +
" sense=" + getTargetSense());}

		if(_modifiers==Input.MODIFIER_IGNORE) return;
		
		if((modifier==Input.MODIFIER_NONE &&
		 _modifiers==Input.MODIFIER_NONE) ||
		 ((modifier&_modifiers) != 0)) {
		  	// condition met
			sendTriggerTrue();
		} else {
			// condition not met
			sendTriggerFalse();
		}
	}
			
	// personal body ============================================
	
	/** Modifier flags. */
	private int _modifiers = Input.MODIFIER_ALL;
	
}