package terrapeer.vui.j3dui.feedback;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;
import terrapeer.vui.j3dui.control.inputs.*;
import terrapeer.vui.j3dui.control.mappers.*;

/**
A manager that oversees select interaction for a group of
targets.  Management includes checking for multiple triggers
on the same target, enforcement of selection limits through
automatic de-selection, and notification to collateral event
targets of any select events.
<P>
In single-selection mode at most one target can be selected
at a time.  In multi-selection mode selected targets are
accumulated.  In any mode selecting a target that is already
selected de-selects it.  The selection mode is controlled via
the input modifier event interface and mode flags.  If
setOnlyManageUser() is enabled system selections are not
managed for exclusion.    
<P>
Target interaction is monitored directly on the multishape
target, which means that interaction can originate from any
source, even a different manager.  See FeedbackManager for
details about adding a target for management.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class SelectManager extends FeedbackManager
 implements InputModifierTarget {

	// public interface =========================================

	/**
	Constructs a SelectManager with no event targets.  Use
	addTarget() to add event targets.
	*/
	public SelectManager() {
		super(Feedback.TYPE_SELECT);
	}

	/**
	If true, only user selections are managed with automatic
	de-selection.  If false, all selections are managed.  The
	default is "true".
	@param enable Enable state for this mode.
	*/
	public void setOnlyManageUser(boolean enable) {
		_onlyManageUser = enable;
	}

	/**
	Sets the modifier flags that enable single-selection.
	The default is MODIFIER_NONE.  Single-selection has
	precedence over multi-selection.
	@param modifiers Modifier flags (Input.MODIFIER_???).  If
	MODIFIER_IGNORE single-selection is disabled.
	*/
	public void setSingleSelect(int modifiers) {
		_singleMods = modifiers;
	}

	/**
	Sets the modifier flags that enable multi-selection.
	The default is MODIFIER_CTRL.  Single-selection has
	precedence over multi-selection.
	@param modifiers Modifier flags (Input.MODIFIER_???).  If
	MODIFIER_IGNORE multi-selection is disabled.
	*/
	public void setMultiSelect(int modifiers) {
		_multiMods = modifiers;
	}

	// InputModifierTarget implementation
	
	public void setInputModifier(int modifier) {

		// single-selection has a higher precedence
		_selectMode = MODE_NONE;
		
		if((_singleMods==Input.MODIFIER_NONE &&
		 modifier==_singleMods) || (modifier&_singleMods)!=0) {
			_selectMode = MODE_SINGLE;
		} else
		if((_multiMods==Input.MODIFIER_NONE &&
		 modifier==_multiMods) || (modifier&_multiMods)!=0) {
			_selectMode = MODE_MULTI;
		}
		
if(Debug.getEnabled()){
Debug.println("SelectManager",
"SELECT:SelectManager:setInputModifier:" +
" modifier=" + modifier +
" singleMods=" + _singleMods +
" multiMods=" + _multiMods +
" selectMode=" + _selectMode);}

	}
			
	// personal body ============================================

	/** No targets can be selected. */	
	private int MODE_NONE = 0;
	/** Only one target can be selected at a time. */	
	private int MODE_SINGLE = 1;
	/** Multiple targets can be selected at a time. */	
	private int MODE_MULTI = 2;
	
	/** Single-select modifier flags. */
	private int _singleMods = Input.MODIFIER_NONE;
	
	/** Multi-select modifier flags. */
	private int _multiMods = Input.MODIFIER_CTRL;
	
	/** Selection mode (MODE_???). */
	private int _selectMode = MODE_SINGLE;
	
	/** "only manage user" mode. */
	private boolean _onlyManageUser = true;

	// FeedbackManager implementation
	
	protected void manageFeedback(FeedbackMinion minion,
	 int state, boolean isTarget) {
				
if(Debug.getEnabled()){
Debug.println("SelectManager",
"MANAGE:SelectManager:manageFeedback:" +
" isTarget=" + isTarget +
" state=" + Feedback.toSelectString(state) +
" selectMode=" + _selectMode +
" onlyManageUser=" + _onlyManageUser +
" minion=" + minion);}
		
		// manage selection targets
		Iterator iter;
		FeedbackMinion minionI;
		
		if(_onlyManageUser && state==Feedback.SELECT_AUTO) {
			// ignore if auto select		
		
if(Debug.getEnabled()){
Debug.println("SelectManager.verbose",
"VERBOSE:SelectManager:manageFeedback:" +
" ignore auto select");}

		} else
		if(_selectMode == MODE_NONE) {
			// de-select all targets
		
if(Debug.getEnabled()){
Debug.println("SelectManager.verbose",
"VERBOSE:SelectManager:manageFeedback:" +
" global deselect");}

			iter = getMinions().iterator();
			while(iter.hasNext()) {
				minionI = (FeedbackMinion)iter.next();
				minionI.updateTargetState(
				 Feedback.SELECT_NORMAL);
			}
		} else
		if(minion == null) {
			// quit if no target		
		
if(Debug.getEnabled()){
Debug.println("SelectManager.verbose",
"VERBOSE:SelectManager:manageFeedback:" +
" no minion");}

		} else
		if(state == Feedback.SELECT_NORMAL) {
		
if(Debug.getEnabled()){
Debug.println("SelectManager.verbose",
"VERBOSE:SelectManager:manageFeedback:" +
" explicit deselect");}

			// explict target de-select		
			minion.updateTargetState(state);
		} else
		if(_selectMode == MODE_SINGLE) {
			// single-select target
		
if(Debug.getEnabled()){
Debug.println("SelectManager.verbose",
"VERBOSE:SelectManager:manageFeedback:" +
" single select");}

			/// de-select other targets			
			iter = getMinions().iterator();
			while(iter.hasNext()) {
				minionI = (FeedbackMinion)iter.next();
				if(minionI != minion) {
					minionI.updateTargetState(
					 Feedback.SELECT_NORMAL);
				}
			}
			
			/// select target			
			minion.updateTargetState(state);
		} else
		if(_selectMode == MODE_MULTI) {
			// multi-select target
			if(!isTarget && state==Feedback.SELECT_SINGLE &&
			 minion.getTargetState()!=Feedback.SELECT_NORMAL) {
				// target selected, toggle selection
		
if(Debug.getEnabled()){
Debug.println("SelectManager.verbose",
"VERBOSE:SelectManager:manageFeedback:" +
" toggle de-select");}

				minion.updateTargetState(Feedback.SELECT_NORMAL);
			} else {
				// target not selected, select it			
		
if(Debug.getEnabled()){
Debug.println("SelectManager.verbose",
"VERBOSE:SelectManager:manageFeedback:" +
" multi select");}

				minion.updateTargetState(state);
			}

		}
	}		
}