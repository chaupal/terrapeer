package terrapeer.vui.j3dui.control;

/**
This interface serves as a target for enable events.  The
enable event can also be used as a general purpose boolean
event (e.g. isOver, isPause, etc.).

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/
public interface EnableTarget {
	
	// public interface =========================================

	/**
	Called when the target enable state needs to change.
	@param enable True to enable the target, false to disable it.
	*/
	public void setEnable(boolean enable);
	
}
