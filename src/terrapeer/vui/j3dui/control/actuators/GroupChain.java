package terrapeer.vui.j3dui.control.actuators;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
An abstract class that encapsulates one or more Groups arranged
parent-to-child as a Group to provide more complex and
constrained geometric manipulation (e.g. view orbiting) than that
allowed by a single Group. 
<P>
Generally, only the head (root) and tail (leaf) nodes are
exposed in the chain.  The head node is the first group
in the chain.  If the chain is empty then this group serves
as the dummy head.  In any case, this group and its chain
can be added to the scene graph the same as any other group.
In general child nodes are added to the tail group.  For
special situations children can also be added
to any group in the chain (e.g. look-at and look-from
objects in a view orbit chain).

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public abstract class GroupChain extends Group {
	
	// public interface =========================================

	/**
	Constructs a GroupChain with the specified chain array.  The
	groups in the chain are assumed to exist and to be connected
	in a parent-child chain.
	@param chain Chain array.  Null if none.
	@param length Number of groups actually used in the chain.
	If 0 this group will be the head and tail.
	@param child First child node of this group.  Null if none.
	*/
	public GroupChain(Group[] chain, int length, Node child) {
		setChain(chain, length);
		addNode(child);
	}

	/**
	Constructs a GroupChain with a default chain of Group
	elements.  Override buildChain() to build a more specific
	type of chain.
	@param length Number of groups in chain.  If 0 this group
	will be the head and tail.
	@param child First child node of this group.  Null if none.
	*/
	public GroupChain(int length, Node child) {
		setChain(buildChain(length), length);
		addNode(child);
	}
	
	/**
	Adds the specified node to this chain's tail group.
	@param child Node added to the chain tail.  Ignored if null.
	@return Reference to child node.
	*/
	public Node addNode(Node child) {
		if(child!=null) getTail().addChild(child);
		return child;
	}
	
	/**
	Gets the tail group in the chain, which is where child
	nodes of the chain are added.  Handles any recursion
	(i.e. tail of tail)
	@return Tail group.  Never null.
	*/
	public Group getTail() {
		if(_length==0) {
			return this;
		} else {
			Group tail = _chain[_length-1];
			if(tail instanceof GroupChain)
				return ((GroupChain)tail).getTail();
			else
				return tail;
		}
	}
	
	/**
	Gets the number of groups in the chain.
	@return Group count in chain.
	*/
	public int getLength() {
		return _length;
	}

	/**
	Gets the group at index <index>.
	@param index Group index (0 = head, length-1 = tail).
	@return The specified group.  Null if <index> is
	out of range.
	*/
	public Group getGroup(int index) {
		if(index<0 || index>=_length) {
			if(index==0 && _length==0)
				return this;
			else
				return null;
		} else
			return _chain[index];
	}

	/**
	Called during construction to build a default group chain
	of the specified length.  Override to build a more specific
	default group chain.  The returned chain must be complete
	with groups connected in a parent-child chain.  If the
	length is zero a null chain should be returned.
	@param length Number of groups in the chain.  If 0 then
	return a null chain.
	@param chain Return group chain.  Null if length is 0.
	*/
	public Group[] buildChain(int length) {
		if(length==0) return null;
		
		Group[] _chain = new Group[length];
		
		for(int groupI=0; groupI<length; groupI++) {
			Group group = new Group();
			_chain[groupI] = group;

			if(groupI>0) {
				_chain[groupI-1].addChild(group);
			}
		}
		
		return _chain;
	}
			
	// personal body ============================================
	
	/** Group [_length].  Null if length=0. */
	private Group _chain[];
	
	/** Chain length. >=0 */
	private int _length;

	/**
	Sets the chain array.  Should only be called once during
	construction because any old chain will not be removed from
	this group.
	@param chain Fully constructed chain array.  Null if none.
	@param length Number of groups actually used in the chain.
	If 0 this group will be the head and tail.  If the chain
	is null or too short the length will be 0.
	*/
	public void setChain(Group[] chain, int length) {
		if(chain==null || length==0 || chain.length<length) {
			_chain = null;
			_length = 0;
		} else {
			_chain = chain;
			_length = length;
			this.addChild(_chain[0]);
		}
	}
	
}