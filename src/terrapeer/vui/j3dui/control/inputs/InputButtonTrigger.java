package terrapeer.vui.j3dui.control.inputs;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;

/**
An enable trigger that monitors button events.  The trigger
condition is met if any of the setButtons() button flags or
if any of the setClicks() click flags are true.
<P>
When the downstream enable chain is completed use
initEventTarget() to initialize its state.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class InputButtonTrigger extends EnableTrigger
 implements InputButtonTarget {
	
	// public interface =========================================

	/**
	Constructs an InputButtonTrigger with event target <target>.
	@param target Event target.  Never null.
	*/
	public InputButtonTrigger(EnableTarget target) {
		super(target);
	}

	/**
	Constructs an InputButtonTrigger with event target <target>,
	modifier flags <modifiers>, and output sense <negate>.
	@param target Event target.  Never null.
	@param buttons Button flags (Input.BUTTON_???).
	@param clicks Click flags (Input.CLICK_???).
	@param sense Value output when condition is met.
	*/
	public InputButtonTrigger(EnableTarget target,
	 int buttons, int clicks, boolean sense) {
		super(target);
		setButtons(buttons);
		setClicks(clicks);
		setTargetSense(sense);
	}

	/**
	Sets the button flags.  The default is BUTTON_ALL.
	@param buttons Button flags (Input.BUTTON_???).  If
	BUTTON_IGNORE no events will be generated.
	*/
	public void setButtons(int buttons) {
		_buttons = buttons;
	}

	/**
	Sets the click flags.  The default is CLICK_ALL.
	@param clicks Click flags (Input.CLICK_???).  If
	CLICK_IGNORE no events will be generated.
	*/
	public void setClicks(int clicks) {
		_clicks = clicks;
	}

	// InputButtonTarget implementation
	
	public void setInputButton(int button) {
		
if(Debug.getEnabled()){Debug
.println("InputButtonTrigger",
"ENABLE:InputButtonTrigger:setInputButton:" +
" button=" + button +
" buttonFlags=" + _buttons +
" sense=" + getTargetSense());}

		if(_buttons==Input.BUTTON_IGNORE) return;
		
		if((button==Input.BUTTON_NONE &&
		 _buttons==Input.BUTTON_NONE) ||
		 ((button&_buttons) != 0)) {
		  	// condition met
			sendTriggerTrue();
		} else {
			// condition not met
			sendTriggerFalse();
		}
	}
	
	public void setInputClick(int click) {
		
if(Debug.getEnabled()){
Debug.println("InputButtonTrigger",
"ENABLE:InputButtonTrigger:setInputClick:" +
" click=" + click +
" clickFlags=" + _clicks +
" sense=" + getTargetSense());}

		if(_clicks==Input.CLICK_IGNORE) return;
		
		if((click==Input.CLICK_NONE &&
		 _clicks==Input.CLICK_NONE) ||
		 ((click&_clicks) != 0)) {
		  	// condition met
			sendTriggerTrue();
		} else {
			// condition not met
			sendTriggerFalse();
		}
	}
			
	// personal body ============================================
	
	/** Button flags. */
	private int _buttons = Input.BUTTON_ALL;
	
	/** Click flags. */
	private int _clicks = Input.CLICK_ALL;
	
}