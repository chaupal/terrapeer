package terrapeer.vui.j3dui.control.inputs;

import java.awt.*;
import java.awt.event.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
Provides AWT and input sensor specific event constants and
utilities.

<P><B>Notes:</B><BR>
Here, mouse buttons are defined in their order of precedence.
The "first" button is typically the left one.  The "second"
button the right one.  And, the "third" button the middle one.
Because of cross-platform concerns and because there seems to
not be a reliable way to simulate a third mouse button on
Windows, the first and third button together is a built-in
alias for the third button.
<P>
In the AWT a one-button mouse is assumed, with the other
buttons corresponding to modifier keys.  Specifically,
the "second" button corresponds to the "meta" key and the
"third" button corresponds to the "alt" key.  At least in
Windows, however, the third (middle) button causes both the
"ctrl" and "alt" key modifiers to be true.  Because of this,
the "ctrl" and "alt" modifiers should not be used with the
third button.
<P>
Because of the overloading of keys for mouse buttons, certain
modifiers are not included (i.e. Meta) but certain modifier
combinations are included (i.e. Shift+Ctrl, Shift+Alt)
and others are excluded (e.g. Ctrl+Alt).
<P>
Although the escape key is technically not a modifier key
(i.e. it can not be used in conjunction with other keys) it
is included with the modifier key constants and sensor for
simplicity.
<P>
All "raw" mouse positions are "cooked" as soon as possible
in a sensor.  Cooked mouse position is in a right-handed
coordinate system with Y increasing "up".  For speed, display
coordinates are relative to the display canvas upper left
corner (i.e. Y position in the canvas is negative).

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public abstract class Input {

 	// public constants =========================================
	
	/** Value for ignoring mouse buttons. */
	public static final int BUTTON_IGNORE = 0x00;
	/** Value for no mouse buttons. */
	public static final int BUTTON_NONE = 0x01;
	/** Primary (e.g. left for right-handed) mouse button. */
	public static final int BUTTON_FIRST = 0x02;
	/** Secondary (e.g. right for right-handed) mouse button. */
	public static final int BUTTON_SECOND = 0x04;
	/** Tertiary (i.e. middle) mouse button. */
	public static final int BUTTON_THIRD = 0x08;
	/** Alias for tertiary mouse button. */
	public static final int BUTTON_FIRST_SECOND = BUTTON_THIRD;
	/** Value for all mouse buttons. */
	public static final int BUTTON_ALL =
	 BUTTON_NONE|BUTTON_FIRST|BUTTON_SECOND|BUTTON_THIRD;
	/** Value for any mouse button down. */
	public static final int BUTTON_IS_DOWN =
	 BUTTON_FIRST|BUTTON_SECOND|BUTTON_THIRD;
	
	/** Value for ignoring mouse clicks. */
	public static final int CLICK_IGNORE = 0x00;
	/** Value for no mouse clicks. */
	public static final int CLICK_NONE = 0x01;
	/** First mouse click. */
	public static final int CLICK_SINGLE = 0x02;
	/** Double mouse click. */
	public static final int CLICK_DOUBLE = 0x04;
	/** triple mouse click. */
	public static final int CLICK_TRIPLE = 0x08;
	/** More than three mouse clicks. */
	public static final int CLICK_MANY = 0x100;
	/** Value for all mouse clicks. */
	public static final int CLICK_ALL =
	 CLICK_NONE|CLICK_SINGLE|CLICK_DOUBLE|CLICK_MANY;
	/** Value for any mouse click. */
	public static final int CLICK_IS_CLICKED =
	 CLICK_SINGLE|CLICK_DOUBLE|CLICK_MANY;
	
	/** Value for ignoring modifier keys. */
	public static final int MODIFIER_IGNORE = 0x00;
	/** Value for no modifier keys. */
	public static final int MODIFIER_NONE = 0x01;
	/** Shift key active. */
	public static final int MODIFIER_SHIFT = 0x02;
	/** Ctrl key active. */
	public static final int MODIFIER_CTRL = 0x04;
	/** Alt key active active. */
	public static final int MODIFIER_ALT = 0x08;
//	/** Meta key active active. */
//	public static final int MODIFIER_META = 0x10;
	/** Shift and Ctrl key active. */
	public static final int MODIFIER_SHIFT_CTRL = 0x20;
	/** Shift and Ctrl key active. */
	public static final int MODIFIER_SHIFT_ALT = 0x40;
	/** Value for all modifier keys excluding Esc. */
	public static final int MODIFIER_ALL =
	 MODIFIER_NONE|MODIFIER_SHIFT|MODIFIER_CTRL|MODIFIER_ALT|
	 MODIFIER_SHIFT_CTRL|MODIFIER_SHIFT_ALT;
	/** Escape key active (not a real modifier). */
	public static final int MODIFIER_ESC = 0x100;
	/** Value for all modifier keys including Esc. */
	public static final int MODIFIER_ALL_ESC =
	 MODIFIER_ALL|MODIFIER_ESC;
	/** Value for any modifier key down. */
	public static final int MODIFIER_IS_DOWN =
	 MODIFIER_SHIFT|MODIFIER_CTRL|MODIFIER_ALT|
	 MODIFIER_SHIFT_CTRL|MODIFIER_SHIFT_ALT;
	
	/** Value for no keyboard key pressed. */
	public static final int KEY_NONE = KeyEvent.VK_UNDEFINED;
	
	/** Value for ignoring arrow keys. */
	public static final int ARROW_IGNORE = 0x00;
	/** Value for no arrow. */
	public static final int ARROW_NONE = 0x01;
	/** Up arrow active. */
	public static final int ARROW_UP = 0x02;
	/** Down arrow active. */
	public static final int ARROW_DOWN = 0x04;
	/** Left arrow active. */
	public static final int ARROW_LEFT = 0x08;
	/** Right arrow active. */
	public static final int ARROW_RIGHT = 0x10;
	/** Value for all arrows. */
	public static final int ARROW_ALL =
	 ARROW_NONE|ARROW_UP|ARROW_DOWN|ARROW_LEFT|ARROW_RIGHT;
	/** Value for any arrow key down. */
	public static final int ARROW_IS_DOWN =
	 ARROW_UP|ARROW_DOWN|ARROW_LEFT|ARROW_RIGHT;
		
	// public utilities =========================================
	
	/**
	Cooks the raw mouse position (actually any screen-based
	position) so that it conforms to a right-hand coordinate
	system (Y increasing up) and is output as a floating point
	vector.  The origin remains at the upper left corner of the
	display space.
	@param rawX Raw mouse X position (increasing right).
	@param rawY Raw mouse Y position (increasing down).
	@param copy Container for the copied return value.
	@return Cooked mouse position (right-handed).
	*/
	public static Tuple2d cookMouse(int rawX, int rawY,
	 Tuple2d copy) {
		copy.set(rawX, -rawY);
		return copy;
	}

	/**
	Uncooks the cooked mouse X position so that it conforms to
	the Java screen coordinate system (Y increasing down). 
	The origin remains at the upper left corner of the display
	space.
	@param cooked Cooked mouse position (right-handed).
	@return Raw mouse X position (increasing right).
	*/
	public static int uncookMouseX(Tuple2d cooked) {
		return Math.round((float)cooked.x);
	}

	/**
	Uncooks the cooked mouse Y position so that it conforms to
	the Java screen coordinate system (Y increasing down). 
	The origin remains at the upper left corner of the display
	space.
	@param pos Cooked mouse position (right-handed).
	@return Raw mouse Y position (increasing down).
	*/
	public static int uncookMouseY(Tuple2d cooked) {
		return Math.round((float)-cooked.y);
	}

	/**
	Returns the current button choice from the AWT event.
	See whichButton(int) for details. 
	@param event Input event. 
	@return Mouse button choice (BUTTON_???).
	*/
	public static int whichButton(InputEvent event) {
		return whichButton(event.getModifiers());
	}

	/**
	Returns the current button choice corresponding to the AWT
	event flags.  In the case of multiple buttons being pressed,
	the order of precedence is: First, Second, Third, with the
	exception that the first and second button together is an
	alias for the third button and has the highest precedence. 
	@param awtFlags Bit flags such as those obtained from
	InputEvent.getModifiers(). 
	@return Mouse button choice (BUTTON_???).
	*/
	public static int whichButton(int awtFlags) {
		// resolve button, in order of precedence
		if((awtFlags &
		 (InputEvent.BUTTON1_MASK|InputEvent.BUTTON3_MASK)) ==
		 (InputEvent.BUTTON1_MASK|InputEvent.BUTTON3_MASK))
			return BUTTON_THIRD;
		else if((awtFlags &
		 InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK)
			return BUTTON_FIRST;
		else if((awtFlags &
		 InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK)
			return BUTTON_SECOND;
		else if((awtFlags &
		 InputEvent.BUTTON2_MASK) == InputEvent.BUTTON2_MASK)
			return BUTTON_THIRD;
		else
			return BUTTON_NONE;
	}

	/**
	Coverts the button state flags into a string.
	@param flags Input button state flags (BUTTON_???). 
	@return String equivalent of state flags.
	*/
	public static String toButtonString(int flags) {
		String string = "";
		
		if(flags == BUTTON_IGNORE) {
			string += "BUTTON_IGNORE ";
			return string;
		}
			
		if((flags&BUTTON_NONE) != 0)
			string += "BUTTON_NONE ";
		if((flags&BUTTON_FIRST) != 0)
			string += "BUTTON_FIRST ";
		if((flags&BUTTON_SECOND) != 0)
			string += "BUTTON_SECOND ";
		if((flags&BUTTON_THIRD) != 0)
			string += "BUTTON_THIRD ";
		
		return string;
	}

	/**
	Returns the current click choice from the AWT event. 
	@param event Input event. 
	@return Mouse click choice (CLICK_???).
	*/
	public static int whichClick(MouseEvent event) {
		switch(event.getClickCount()) {
			case 0: return CLICK_NONE;
			case 1: return CLICK_SINGLE;
			case 2: return CLICK_DOUBLE;
			case 3: return CLICK_TRIPLE;
			default: return CLICK_MANY;
		}
	}

	/**
	Coverts the click state flags into a string.
	@param flags Input click state flags (CLICK_???). 
	@return String equivalent of state flags.
	*/
	public static String toClickString(int flags) {
		String string = "";
		
		if(flags == CLICK_IGNORE) {
			string += "CLICK_IGNORE ";
			return string;
		}
			
		if((flags&CLICK_NONE) != 0)
			string += "CLICK_NONE ";
		if((flags&CLICK_SINGLE) != 0)
			string += "CLICK_SINGLE ";
		if((flags&CLICK_DOUBLE) != 0)
			string += "CLICK_DOUBLE ";
		if((flags&CLICK_TRIPLE) != 0)
			string += "CLICK_TRIPLE ";
		if((flags&CLICK_MANY) != 0)
			string += "CLICK_MANY ";
		
		return string;
	}

	/**
	Returns the current modifier key choice from the AWT key
	event, including the escape key, which is exclusive of
	all other modifiers.
	@param event Key event.
	@return Modifier choice (MODIFIER_???).
	*/
	public static int whichModifier(KeyEvent event) {
		if(whichKey(event) == KeyEvent.VK_ESCAPE) {
			return MODIFIER_ESC;
		} else {
			return whichModifier((InputEvent)event);
		}
	}

	/**
	Returns the current modifier key choice from the AWT event.
	See whichModifier(int) for details. 
	@param event Input event.
	@return Modifier choice (MODIFIER_???).
	*/
	public static int whichModifier(InputEvent event) {
		return whichModifier(event.getModifiers());
	}

	/**
	Returns the current modifier key choice corresponding to
	the AWT event flags (does not include the escape key).  Note
	the order of precedence, which starts with Ctrl+Alt.  Note
	also that, to avoid conflicts with the third mouse button
	in Windows (i.e. Ctrl+Alt), Ctrl+Alt returns MODIFIER_NONE
	and Shift+Ctrl+Alt returns MODIFIER_SHIFT.
	@param awtFlags Bit flags such as those obtained from
	InputEvent.getModifiers(). 
	@return Modifier choice (MODIFIER_???).
	*/
	public static int whichModifier(int awtFlags) {
		if((awtFlags &
		 (InputEvent.CTRL_MASK|InputEvent.ALT_MASK)) ==
		 (InputEvent.CTRL_MASK|InputEvent.ALT_MASK)) {
			if((awtFlags & InputEvent.SHIFT_MASK) ==
			 InputEvent.SHIFT_MASK) {
				return MODIFIER_SHIFT;
			} else {
				return MODIFIER_NONE;
			}
		} else if((awtFlags &
		 (InputEvent.SHIFT_MASK|InputEvent.CTRL_MASK)) ==
		 (InputEvent.SHIFT_MASK|InputEvent.CTRL_MASK)) {
			return MODIFIER_SHIFT_CTRL;
		} else if((awtFlags &
		 (InputEvent.SHIFT_MASK|InputEvent.ALT_MASK)) ==
		 (InputEvent.SHIFT_MASK|InputEvent.ALT_MASK)) {
			return MODIFIER_SHIFT_ALT;
		} else if((awtFlags & InputEvent.SHIFT_MASK) ==
		 InputEvent.SHIFT_MASK) {
			return MODIFIER_SHIFT;
		} else if((awtFlags & InputEvent.CTRL_MASK) ==
		 InputEvent.CTRL_MASK) {
			return MODIFIER_CTRL;
		} else if((awtFlags & InputEvent.ALT_MASK) ==
		 InputEvent.ALT_MASK) {
			return MODIFIER_ALT;
		} else {
			return MODIFIER_NONE;
		}
	}

	/**
	Coverts the modifier state flags into a string.
	@param flags Input modifier state flags (MODIFIER_???). 
	@return String equivalent of state flags.
	*/
	public static String toModifierString(int flags) {
		String string = "";
		
		if(flags == MODIFIER_IGNORE) {
			string += "MODIFIER_IGNORE ";
			return string;
		}
			
		if((flags&MODIFIER_NONE) != 0)
			string += "MODIFIER_NONE ";
		if((flags&MODIFIER_ESC) != 0)
			string += "MODIFIER_ESC ";
		if((flags&MODIFIER_SHIFT) != 0)
			string += "MODIFIER_SHIFT ";
		if((flags&MODIFIER_CTRL) != 0)
			string += "MODIFIER_CTRL ";
		if((flags&MODIFIER_ALT) != 0)
			string += "MODIFIER_ALT ";
		if((flags&MODIFIER_SHIFT_CTRL) != 0)
			string += "MODIFIER_SHIFT_CTRL ";
		if((flags&MODIFIER_SHIFT_ALT) != 0)
			string += "MODIFIER_SHIFT_ALT ";
		
		return string;
	}

	/**
	Returns the current key involved in the AWT event.  If a
	key is being released then the key choice is interpreted
	as KEY_NONE.
	@param event Input event.
	@return Key code (KEY_NONE or VK_???).
	*/
	public static int whichKey(KeyEvent event) {
		if(event.getID()==KeyEvent.KEY_RELEASED) {
			return KEY_NONE;
		} else {
			return event.getKeyCode();
		}
	}

	/**
	Returns the current arrow key from the AWT event.
	@param event Input event.
	@return Arrow choice (ARROW_???).
	*/
	public static int whichArrow(KeyEvent event) {
		switch(event.getKeyCode()) {
			case KeyEvent.VK_UP:
			case KeyEvent.VK_KP_UP:
				return ARROW_UP;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_KP_DOWN:
				return ARROW_DOWN;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_KP_LEFT:
				return ARROW_LEFT;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_KP_RIGHT:
				return ARROW_RIGHT;
			default:
				return ARROW_NONE;
		}
	}

	/**
	Coverts the arrow state flags into a string.
	@param flags Input click state flags (ARROW_???). 
	@return String equivalent of state flags.
	*/
	public static String toArrowString(int flags) {
		String string = "";
		
		if(flags == ARROW_IGNORE) {
			string += "ARROW_IGNORE ";
			return string;
		}
			
		if((flags&ARROW_NONE) != 0)
			string += "ARROW_NONE ";
		if((flags&ARROW_UP) != 0)
			string += "ARROW_UP ";
		if((flags&ARROW_DOWN) != 0)
			string += "ARROW_DOWN ";
		if((flags&ARROW_LEFT) != 0)
			string += "ARROW_LEFT ";
		if((flags&ARROW_RIGHT) != 0)
			string += "ARROW_RIGHT ";
		
		return string;
	}

}