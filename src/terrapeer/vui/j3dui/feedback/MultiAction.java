package terrapeer.vui.j3dui.feedback;

import javax.media.j3d.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
A multishape associated with "action" (e.g. normal, over).
@see MultiShape
@see MultiStatus
@see MultiSelect

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/
public class MultiAction extends MultiShape {	
	
	// public interface =========================================

	/**
	Constructs a MultiAction that is empty. 
	*/
	public MultiAction() {
		super(Feedback.ACTION_COUNT);
	}
	
	/**
	Associates the node with the specified action states.  Any
	previous node association with these states will be lost.
	@param flags Action state flags (Feedback.ACTION_???).
	@param node Object shape, possibly a MultiShape.  If null
	no shape will appear for the corresponding states.
	*/
	public void setActionNode(int flags, Node node) {
		int slot = -1;
		int count = Feedback.ACTION_COUNT;

		for(int stateI=0; stateI<count; stateI++) {
			slot = setNode(stateI, Feedback.
			 toActionState(stateI), flags, slot, node);
		}
	}
}