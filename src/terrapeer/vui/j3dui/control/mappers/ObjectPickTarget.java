package terrapeer.vui.j3dui.control.mappers;

/**
This interface serves as a target for scene object-pick events.
Events are generated only when a change in pick target occurs.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/
public interface ObjectPickTarget {
	
	// public interface =========================================

	/**
	Called when the pick target object changes, which can occur
	when the pick cursor enters over a new target, moves from
	one target to another, or exits from over a target.
	@param index Index of the target object in the pick target
	list, which is presumably known by the event target.  -1 if
	no target.
	@param target The pick target object that the pick cursor
	is over.  Null if no target.
	*/
	public void setObjectPick(int index, Object target);
	
}