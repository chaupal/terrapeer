package terrapeer.vui.j3dui.feedback;

import javax.media.j3d.*;

/**
Provides feedback event constants and utilities.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/
public class Feedback {	
 
 	// public constants =========================================

	/** Ignore feedback type. */
	public static final int TYPE_IGNORE = 0x00;
	/** No feedback type. */
	public static final int TYPE_NONE = 0x01;
	/** Status feedback type. */
	public static final int TYPE_STATUS = 0x02;
	/** Select feedback type. */
	public static final int TYPE_SELECT = 0x04;
	/** Action feedback type. */
	public static final int TYPE_ACTION = 0x08;
	/** All feedback types. */
	public static final int TYPE_ALL =
	 TYPE_STATUS|TYPE_SELECT|TYPE_ACTION;

	/** Ignore target status, switch shape hidden. */
	public static final int STATUS_IGNORE = 0x00;
	/** Interaction enabled state. */
	public static final int STATUS_NORMAL = 0x01;
	/** Interaction disabled state. */
	public static final int STATUS_DISABLE = 0x02;
	/** Interaction disabled but recommended state. */
	public static final int STATUS_RECOMMEND = 0x04;
	/** Interaction disabled but discommended state. */
	public static final int STATUS_DISCOMMEND = 0x08;
	/** number of distinct status states. */
	public static final int STATUS_COUNT = 4;
	/** All status states. */
	public static final int STATUS_ALL =
	 STATUS_NORMAL|STATUS_DISABLE|STATUS_RECOMMEND|
	 STATUS_DISCOMMEND;
	/** Status states for isEnable. */
	public static final int STATUS_IS_ENABLE =
	 STATUS_NORMAL;

	/** Ignore target selection, switch shape hidden. */
	public static final int SELECT_IGNORE = 0x00;
	/** Target not selected. */
	public static final int SELECT_NORMAL = 0x01;
	/** Target auto-selected by system. */
	public static final int SELECT_AUTO = 0x02;
	/** Target single-click selected by user. */
	public static final int SELECT_SINGLE = 0x04;
	/** Target double-click selected by user. */
	public static final int SELECT_DOUBLE = 0x08;
	/** Target double-click selected by user. */
	public static final int SELECT_TRIPLE = 0x10;
	/** Target many-click selected by user. */
	public static final int SELECT_MANY = 0x100;
	/** number of distinct selection states. */
	public static final int SELECT_COUNT = 6;
	/** All select states. */
	public static final int SELECT_ALL =
	 SELECT_NORMAL|SELECT_AUTO|SELECT_SINGLE|SELECT_DOUBLE|
	 SELECT_TRIPLE|SELECT_MANY;
	/** Select states for isSystem. */
	public static final int SELECT_IS_SYSTEM =
	 SELECT_AUTO;
	/** Select states for isUser. */
	public static final int SELECT_IS_USER =
	 SELECT_SINGLE|SELECT_DOUBLE|SELECT_TRIPLE|SELECT_MANY;
	/** Select states for isSelect. */
	public static final int SELECT_IS_SELECT =
	 SELECT_IS_SYSTEM|SELECT_IS_USER;
	
	/** Ignore target action, switch shape hidden. */
	public static final int ACTION_IGNORE = 0x00;
	/** Mouse not over or active on object. */
	public static final int ACTION_NORMAL = 0x01;
	/** Mouse over object. */
	public static final int ACTION_OVER = 0x02;
	/** Mouse paused over object. */
	public static final int ACTION_PAUSE = 0x04;
	/** Mouse button down over object. */
	public static final int ACTION_DOWN = 0x08;
	/** Mouse drag started (over, active). */
	public static final int ACTION_DRAG = 0x10;
	/** Mouse drag stopped (over). */
	public static final int ACTION_DROP = 0x20;
	/** Mouse drag canceled (not over, not active). */
	public static final int ACTION_CANCEL = 0x40;
	/** Number of distinct actions states. */
	public static final int ACTION_COUNT = 7;
	/** All action states. */
	public static final int ACTION_ALL =
	 ACTION_NORMAL|ACTION_OVER|ACTION_PAUSE|ACTION_DOWN|
	 ACTION_DRAG|ACTION_DROP|ACTION_CANCEL;
	/** Action states for isOver. */
	public static final int ACTION_IS_OVER =
	 ACTION_OVER|ACTION_PAUSE|ACTION_DOWN|ACTION_DRAG|
	 ACTION_DROP|ACTION_CANCEL;
	/** Action states for isActive. */
	public static final int ACTION_IS_ACTIVE =
	 ACTION_DRAG;
		
	// public utilities =========================================

	/**
	Coverts the status state flags into a string.
	@param flags Status feedback state flags (STATUS_???). 
	@return String equivalent of state flags.
	*/
	public static String toStatusString(int flags) {
		String string = "";
		
		if(flags == STATUS_IGNORE) {
			string += "STATUS_IGNORE ";
			return string;
		}
			
		if((flags&STATUS_NORMAL) != 0)
			string += "STATUS_NORMAL ";
		if((flags&STATUS_DISABLE) != 0)
			string += "STATUS_DISABLE ";
		if((flags&STATUS_RECOMMEND) != 0)
			string += "STATUS_RECOMMEND ";
		if((flags&STATUS_DISCOMMEND) != 0)
			string += "STATUS_DISCOMMEND ";
		
		return string;
	}

	/**
	Returns the array index corresponding to the specified
	feedback status state flags, which are resolved in order
	of precedence. 
	@param flags Status feedback state flags (STATUS_???). 
	@return MultiStatus array index.  -1 if unrecognized or no
	states.
	*/
	public static int toStatusIndex(int flags) {
		if((flags&STATUS_NORMAL) != 0)
			return 0;
		else if((flags&STATUS_DISABLE) != 0)
			return 1;
		else if((flags&STATUS_RECOMMEND) != 0)
			return 2;
		else if((flags&STATUS_DISCOMMEND) != 0)
			return 3;
		else
			return -1;
	}

	/**
	Returns the status feedback state corresponding to
	the specified state index. 
	@param index MultiStatus state index.
	@return Status feedback state (STATUS_???). 
	*/
	public static int toStatusState(int index) {
		switch(index) {
			case 0: return STATUS_NORMAL;
			case 1: return STATUS_DISABLE;
			case 2: return STATUS_RECOMMEND;
			case 3: return STATUS_DISCOMMEND;
			default: return STATUS_IGNORE;
		}
	}

	/**
	Coverts the select state flags into a string.
	@param flags Select feedback state flags (SELECT_???). 
	@return String equivalent of state flags.
	*/
	public static String toSelectString(int flags) {
		String string = "";
		
		if(flags == SELECT_IGNORE) {
			string += "SELECT_IGNORE ";
			return string;
		}
			
		if((flags&SELECT_NORMAL) != 0)
			string += "SELECT_NORMAL ";
		if((flags&SELECT_AUTO) != 0)
			string += "SELECT_AUTO ";
		if((flags&SELECT_SINGLE) != 0)
			string += "SELECT_SINGLE ";
		if((flags&SELECT_DOUBLE) != 0)
			string += "SELECT_DOUBLE ";
		if((flags&SELECT_TRIPLE) != 0)
			string += "SELECT_TRIPLE ";
		if((flags&SELECT_MANY) != 0)
			string += "SELECT_MANY ";
		
		return string;
	}

	/**
	Returns the array index corresponding to the specified
	feedback select state flags, which are resolved in order
	of precedence. 
	@param flags Select feedback state flags (SELECT_???). 
	@return MultiSelect array index.  -1 if unrecognized or no
	states.
	*/
	public static int toSelectIndex(int flags) {
		if((flags&SELECT_NORMAL) != 0)
			return 0;
		else if((flags&SELECT_AUTO) != 0)
			return 1;
		else if((flags&SELECT_SINGLE) != 0)
			return 2;
		else if((flags&SELECT_DOUBLE) != 0)
			return 3;
		else if((flags&SELECT_TRIPLE) != 0)
			return 4;
		else if((flags&SELECT_MANY) != 0)
			return 5;
		else
			return -1;
	}

	/**
	Returns the select feedback state corresponding to
	the specified state index. 
	@param index MultiSelect state index.
	@return Select feedback state (SELECT_???). 
	*/
	public static int toSelectState(int index) {
		switch(index) {
			case 0: return SELECT_NORMAL;
			case 1: return SELECT_AUTO;
			case 2: return SELECT_SINGLE;
			case 3: return SELECT_DOUBLE;
			case 4: return SELECT_TRIPLE;
			case 5: return SELECT_MANY;
			default: return SELECT_IGNORE;
		}
	}

	/**
	Coverts the action state flags into a string.
	@param flags Action feedback state flags (ACTION_???). 
	@return String equivalent of state flags.
	*/
	public static String toActionString(int flags) {
		String string = "";
		
		if(flags == ACTION_IGNORE) {
			string += "ACTION_IGNORE ";
			return string;
		}
			
		if((flags&ACTION_NORMAL) != 0)
			string += "ACTION_NORMAL ";
		if((flags&ACTION_OVER) != 0)
			string += "ACTION_OVER ";
		if((flags&ACTION_PAUSE) != 0)
			string += "ACTION_PAUSE ";
		if((flags&ACTION_DOWN) != 0)
			string += "ACTION_DOWN ";
		if((flags&ACTION_DRAG) != 0)
			string += "ACTION_DRAG ";
		if((flags&ACTION_DROP) != 0)
			string += "ACTION_DROP ";
		if((flags&ACTION_CANCEL) != 0)
			string += "ACTION_CANCEL ";
		
		return string;
	}

	/**
	Returns the array index corresponding to the specified
	feedback action state flags, which are resolved in order
	of precedence. 
	@param flags Action feedback state flags (ACTION_???). 
	@return MultiSelect array index.  -1 if unrecognized or no
	states.
	*/
	public static int toActionIndex(int flags) {
		if((flags&ACTION_NORMAL) != 0)
			return 0;
		else if((flags&ACTION_OVER) != 0)
			return 1;
		else if((flags&ACTION_PAUSE) != 0)
			return 2;
		else if((flags&ACTION_DOWN) != 0)
			return 3;
		else if((flags&ACTION_DRAG) != 0)
			return 4;
		else if((flags&ACTION_DROP) != 0)
			return 5;
		else if((flags&ACTION_CANCEL) != 0)
			return 6;
		else
			return -1;
	}

	/**
	Returns the action feedback state corresponding to
	the specified state index. 
	@param index MultiAction state index.
	@return Action feedback state (ACTION_???). 
	*/
	public static int toActionState(int index) {
		switch(index) {
			case 0: return ACTION_NORMAL;
			case 1: return ACTION_OVER;
			case 2: return ACTION_PAUSE;
			case 3: return ACTION_DOWN;
			case 4: return ACTION_DRAG;
			case 5: return ACTION_DROP;
			case 6: return ACTION_CANCEL;
			default: return SELECT_IGNORE;
		}
	}
			
}