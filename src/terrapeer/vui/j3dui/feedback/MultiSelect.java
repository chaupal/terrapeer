package terrapeer.vui.j3dui.feedback;

import javax.media.j3d.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
A multishape associated with "selection" (e.g. normal, single).
@see MultiShape
@see MultiStatus
@see MultiAction

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/
public class MultiSelect extends MultiShape {	
	
	// public interface =========================================

	/**
	Constructs a MultiSelect that is empty. 
	*/
	public MultiSelect() {
		super(Feedback.SELECT_COUNT);
	}
	
	/**
	Associates the node with the specified select states.  Any
	previous node association with these states will be lost.
	@param flags Select state flags (Feedback.SELECT_???).
	@param node Object shape, possibly a MultiShape.  If null
	no shape will appear for the corresponding states.
	*/
	public void setSelectNode(int flags, Node node) {
		int slot = -1;
		int count = Feedback.SELECT_COUNT;

		for(int stateI=0; stateI<count; stateI++) {
			slot = setNode(stateI, Feedback.
			 toSelectState(stateI), flags, slot, node);
		}
	}
}