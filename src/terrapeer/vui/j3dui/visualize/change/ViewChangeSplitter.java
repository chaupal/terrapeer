package terrapeer.vui.j3dui.visualize.change;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
Splits (multicasts) the source event to multiple event targets.

@author Jon Barrilleaux,
copyright (c) 1999-2000 Jon Barrilleaux,
All Rights Reserved.
*/

public class ViewChangeSplitter implements ViewChangeTarget {
	
	// public interface =========================================

	/**
	Constructs an ViewChangeSplitter with no event targets.
	*/
	public ViewChangeSplitter() {}

	/**
	Adds event target <target> to the list of event targets
	to receive events.
	@param target Event target to be added.
	@return Returns true if the event target list actually
	changed.
	*/
	public boolean addEventTarget(ViewChangeTarget target) {
		return _eventTargets.add(target);
	}

	/**
	Removes event target <target> from the event target list.
	@param target Event target to be removed.
	@return Returns true if the event target list actually
	changed.
	*/
	public boolean removeEventTarget(ViewChangeTarget target) {
		return _eventTargets.remove(target);
	}

	/**
	Removes all event targets from the event target list.
	*/
	public void clearEventTargets() {
		_eventTargets.clear();
	}

	/**
	Gets an iterator for the event target list.
	@return Event target list iterator.
	*/
	public Iterator getEventTargets() {
		return _eventTargets.iterator();
	}

	// ViewChangeTarget implementation
	
	public void setViewExternal(View source, Point3d pos,
	 Transform3D xform) {
		if(_lockout) return;
		_lockout = true;
		
if(Debug.getEnabled()){
Debug.println("ViewChangeSplitter",
"SPLIT:ViewChangeSplitter:setViewExternal:" +
" source=" + source +
" pos=" + pos + 
" xform=" + xform);}

		Iterator targetI = getEventTargets();
		while(targetI.hasNext()) {
			ViewChangeTarget target =
			 (ViewChangeTarget)targetI.next();
		
			target.setViewExternal(source, pos, xform);
		}
		
		_lockout = false;
	}
	
	public void setViewInternal(View source, double fov,
	 double vsf, double dsf, Vector2d dvo, Vector2d ds,
	 Vector2d ss, Vector2d sr) {
		if(_lockout) return;
		_lockout = true;
		
if(Debug.getEnabled()){
Debug.println("ViewChangeSplitter",
"SPLIT:ViewChangeSplitter:setViewInternal:" +
" source=" + source +
" fov=" + fov + 
" vsf=" + vsf + 
" dsf=" + dsf + 
" dvo=" + dvo + 
" ds=" + ds + 
" ss=" + ss + 
" sr=" + sr);}

		Iterator targetI = getEventTargets();
		while(targetI.hasNext()) {
			ViewChangeTarget target =
			 (ViewChangeTarget)targetI.next();
		
			target.setViewInternal(source, fov, vsf, dsf, dvo,
			 ds, ss, sr);
		}
		
		_lockout = false;
	}
				
	// personal body ============================================
	
	/** If true input events are ignored. */
	private boolean _lockout = false;
	
	/** List of event targets. */
	private ArrayList _eventTargets = new ArrayList();	

}