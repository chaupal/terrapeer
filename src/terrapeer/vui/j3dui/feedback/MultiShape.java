package terrapeer.vui.j3dui.feedback;

import java.util.*;
import javax.media.j3d.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
Base class for switch-like groups implementing the
"multishape" technique, where an object is represented as a set
of discrete shapes associated with corresponding interactive
feedback states.
<P>
None or more feedback event targets can be added as event
targets with setEventTarget().  This conveniently allows a
multishape to output its composite feedback state for feedback
management and for chaining interaction control.
<P>
A multishape can serve as a proxy for both scenegraph and
feedback grouping by adding a feedback target as an event target
and its corresponding scenegraph target as a child node of this
group.
<P>
Typically subclasses are capable of handling only one type of
feedback: status, select, or action.  Subclasses can be nested
in any order by feedback type and to any depth but they must
be controlled from the top-most multishape in a particular
"cluster" since control propagates down the hierarchy, not up.
A typical hierarchy, from top to bottom, is: MultiStatus,
MultiSelect, MultiAction.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/
public class MultiShape extends Group
 implements FeedbackTarget {
	
	// public interface =========================================

	/**
	Constructs a MultiShape as an empty proxy for a target
	with both scenegraph and feedback qualities.  Use
	addProxyTarget() to add targets.
	*/
	public MultiShape() {
		this(0);
	}

	/**
	Constructs a MultiShape as a proxy target.
	@param nodeTarget Target added as a scene graph child
	node.  Null if none.
	@param eventTarget Target added as a feedback event
	target.  Null if none.
	*/
	public MultiShape(Node nodeTarget,
	 FeedbackTarget eventTarget) {
	
		this(0);
		addProxyTarget(nodeTarget, eventTarget);
	}

	/**
	Adds a target to this multishape, which is serving as a
	proxy with both scenegraph and feedback qualities.
	@param nodeTarget Target added as a scene graph child
	node.  Null if none.
	@param eventTarget Target added as a feedback event
	target.  Null if none.
	*/
	public void addProxyTarget(Node nodeTarget,
	 FeedbackTarget eventTarget) {
		
		addNode(nodeTarget);
		if(eventTarget!=null) addEventTarget(eventTarget);
	}

	/**
	Gets the feedback status state.
	@return Feedback state.
	*/
	public int getFeedbackStatus() {
		return _status;
	}

	/**
	Gets the feedback select state.
	@return Feedback state.
	*/
	public int getFeedbackSelect() {
		return _select;
	}

	/**
	Gets the feedback action state.
	@return Feedback state.
	*/
	public int getFeedbackAction() {
		return _action;
	}

	/**
	Adds a node as a child of this group.
	@param node Child node.  Ignored if null.
	*/
	public void addNode(Node node) {
		if(node != null) addChild(node);
	}

	/**
	Adds an event target to the list of event targets.
	@param eventTarget Event target.  Never null.
	@return Returns true if the list actually changed.
	*/
	public boolean addEventTarget(FeedbackTarget eventTarget) {
		if(eventTarget==null) throw new
		 IllegalArgumentException("<eventTarget> is null.");
		return _eventTargets.add(eventTarget);
	}

	/**
	Gets an iterator for the event target list.
	@return Event target list iterator.
	*/
	public Iterator getEventTargets() {
		return _eventTargets.iterator();
	}
	
	// FeedbackTarget implementation

	/**
	Sets the appearance of the object based on the specified
	status state and notifies any event targets.  If no node
	is defined for the corresponding choice then the object
	will appear hidden.
	@param status Status state (Feedback.STATUS_???).
	*/
	public void setFeedbackStatus(int status) {

		_status = status;
			
if(Debug.getEnabled()){		
Debug.println("MultiShape",
"MULTI:MultiShape:setFeedbackStatus:" +
" status=" + Feedback.toStatusString(_status));}

		// tell the targets
		FeedbackTarget target;
		Iterator targetI = getEventTargets();
		while(targetI.hasNext()) {
			target = (FeedbackTarget)targetI.next();
			target.setFeedbackStatus(_status);
		}

		// if right type, set switch
		if(this instanceof MultiStatus && _count!=0) {
			_switch.setWhichChild(
			 _map[Feedback.toStatusIndex(_status)]);
		}
		
		// recursively set children
		Node node;
		for(int whichI=0; whichI<_count; whichI++) {
			node = _switch.getChild(whichI);
			if(node instanceof MultiShape)
				((MultiShape)node).setFeedbackStatus(_status);
		}
	}

	/**
	Sets the appearance of the object based on the specified
	select state and notifies any event targets.  If no node
	is defined for the corresponding choice then the object
	will appear hidden.
	@param select Selection state (Feedback.SELECT_???).
	*/
	public void setFeedbackSelect(int select) {

		_select = select;
			
if(Debug.getEnabled()){		
Debug.println("MultiShape",
"MULTI:MultiShape:setFeedbackSelect:" +
" select=" + Feedback.toSelectString(_select));}

		// tell the targets
		FeedbackTarget target;
		Iterator targetI = getEventTargets();
		while(targetI.hasNext()) {
			target = (FeedbackTarget)targetI.next();
			target.setFeedbackSelect(_select);
		}

		// if right kind, set switch
		if(this instanceof MultiSelect && _count!=0) {
			_switch.setWhichChild(
			 _map[Feedback.toSelectIndex(_select)]);
		}
		
		// recursively set children
		Node node;
		for(int whichI=0; whichI<_count; whichI++) {
			node = _switch.getChild(whichI);
			if(node instanceof MultiShape)
				((MultiShape)node).setFeedbackSelect(_select);
		}
	}

	/**
	Sets the appearance of the object based on the specified
	action state and notifies any event targets.  If no node
	is defined for the corresponding choice then the object
	will appear hidden.
	@param action Action state (Feedback.ACTION_???).
	*/
	public void setFeedbackAction(int action) {

		_action = action;
			
if(Debug.getEnabled()){		
Debug.println("MultiShape",
"MULTI:MultiShape:setFeedbackAction:" +
" action=" + Feedback.toActionString(_action));}

		// tell the targets
		FeedbackTarget target;
		Iterator targetI = getEventTargets();
		while(targetI.hasNext()) {
			target = (FeedbackTarget)targetI.next();
			target.setFeedbackAction(_action);
		}

		// if right kind, set switch
		if(this instanceof MultiAction && _count!=0) {
			_switch.setWhichChild(
			 _map[Feedback.toActionIndex(_action)]);
		}
		
		// recursively set multishape children
		Node node;
		for(int whichI=0; whichI<_count; whichI++) {
			node = _switch.getChild(whichI);
			if(node instanceof MultiShape)
				((MultiShape)node).setFeedbackAction(_action);
		}
	}
			
	// personal body ============================================
	
	/** Target switch containing slots. */
	private Switch _switch = new Switch();
	
	/** Max number of states/slots.  Zero if only a proxy. */
	private int _count;
	
	/** Maps state index to switch slot. */
	private int _map[];
	
	/** Current status state (Feedback.STATUS_???). */
	private int _status = Feedback.STATUS_NORMAL;
	
	/** Current select state (Feedback.SELECT_???). */
	private int _select = Feedback.SELECT_NORMAL;
	
	/** Current action state (Feedback.ACTION_???). */
	private int _action = Feedback.ACTION_NORMAL;
	
	/** List of event targets. */
	private ArrayList _eventTargets = new ArrayList();	

	/**
	Constructs a MultiShape with the specified number of states
	and configured for live access.
	@param stateCount Maximum number of feedback states that can
	be supported. 
	*/
	protected MultiShape(int stateCount) {
		_count = stateCount;
		addChild(_switch);
		
		// build empty state map and switch slots
		_map = new int[_count];
		
		for(int stateI=0; stateI<_count; stateI++) {
			_map[stateI] = Switch.CHILD_NONE;
			_switch.addChild(new Group());
		}
		
		// enable switch access
		_switch.setCapability(Group.ALLOW_CHILDREN_READ);
		_switch.setCapability(Switch.ALLOW_SWITCH_WRITE);

		// init switch state
		setFeedbackStatus(Feedback.STATUS_NORMAL);
		setFeedbackSelect(Feedback.SELECT_NORMAL);
		setFeedbackAction(Feedback.ACTION_NORMAL);		
	}
	
	/**
	Tests the state flags for the specified state.  If it is
	found the state map is updated with the switch slot and
	the switch slot is updated with the node.  The return slot
	index should be chained to the input slot index.
	@param index State index.
	@param state State flag corresponding to index that is to
	be tested for in flags.
	@param flags State flags to be tested.
	@param slot Switch slot index.  If <0 none has been assigned
	yet.
	@param node Node to be set when the switch slot is assigned.
	@return The switch slot assigned to the node.  -1 if no
	slot was assigned.
	*/
	protected int setNode(int index, int state, int flags,
	 int slot, Node node) {
		
if(Debug.getEnabled()){Debug.println("MultiShape.verbose",
"MULTI:setNode:" +
" index=" + index +
" state=" + state +
" flags=" + flags +
" slot=" + slot +
" count=" + _count +
" node=" + node);}

		if((state & flags) != 0) {
			// state found
			if(slot < 0) {
				// assign and set slot
				slot = index;
				if(slot>=0 && slot<_count) {
					if(node == null) node = new Group();
					_switch.setChild(node, slot);
				}
			}

			// update map with slot			
			if(index>=0 && index<_count) {
				if(slot<0 || slot>=_count)
					slot = Switch.CHILD_NONE;
				_map[index] = slot;
			}
		}
		
		return slot;
	}
	
}