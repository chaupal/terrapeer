package terrapeer.vui.j3dui.feedback;

import javax.media.j3d.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
A multishape associated with "status" (e.g. normal, disable).
@see MultiShape
@see MultiSelect
@see MultiAction

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/
public class MultiStatus extends MultiShape {	
	
	// public interface =========================================

	/**
	Constructs a MultiStatus that is empty. 
	*/
	public MultiStatus() {
		super(Feedback.STATUS_COUNT);
	}
	
	/**
	Associates the node with the specified status states.  Any
	previous node association with these states will be lost.
	@param flags Status state flags (Feedback.STATUS_???).
	@param node Object shape, possibly a MultiShape.  If null
	no shape will appear for the corresponding states.
	*/
	public void setStatusNode(int flags, Node node) {
		int slot = -1;
		int count = Feedback.STATUS_COUNT;

		for(int stateI=0; stateI<count; stateI++) {
			slot = setNode(stateI, Feedback.
			 toStatusState(stateI), flags, slot, node);
		}
	}
			
}