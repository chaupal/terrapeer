package terrapeer.vui.j3dui.control.mappers.intuitive;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;
import terrapeer.vui.j3dui.control.mappers.*;

/**
An intuitive drag mapper plugin that performs the first half of
Display Relative Mapping (DRM) by mapping from 2D input drag
space to the Z=0 pane in the 3D source drag space.
<P>
The second half of DRM is performed by a SourceDragMapperPlugin.

@see SourceDragMapperPlugin 

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class DrmDragPlugin
 extends IntuitiveDragMapperPlugin {
	
	// public interface =========================================

	/**
	Constructs a DrmDragPlugin whose source space is defined by
	the view associated with the drag source.  The
	Node.ALLOW_LOCAL_TO_VWORLD_READ capability bits must be set
	on the ViewPlatform for all possible input drag sources.
	*/
	public DrmDragPlugin() {
		_useDisplaySource = true;
	}

	/**
	Constructs a DrmDragPlugin with the source view space
	defined by the specified node.  Sets the source node's
	capability bits for live use.
	@param sourceSpace Node defining the source space.  If null
	then the source space is the world space.
	*/
	public DrmDragPlugin(Node sourceSpace) {
		_useDisplaySource = false;
		
		_sourceSpace = sourceSpace;
		if(_sourceSpace!=null) {
			_sourceSpace.setCapability(
			 Node.ALLOW_LOCAL_TO_VWORLD_READ);
		}
	}

	// IntuitiveDragMapperPlugin implementation
	
	public String toString() {
		return "DrmDragPlugin";
	}

	public Node getSourceSpace() {
		return _sourceSpace;
	}
			
	// personal body ============================================
	
	/** Current source space.  World space if null. */
	private Node _sourceSpace = null;
	
	/** If true display source is used for source space. */
	private boolean _useDisplaySource;

	/**
	Performs the mapping from input to source space.
	@param source Source display canvas.  Never null.
	@param pos Input position relative to source display.
	@param copy Container for the copied source space position
	return value.
	@return True if the return position is valid.
	*/	
	protected boolean toTargetValue(Canvas3D source,
	 Vector2d pos, Vector3d copy) {

		copy.set(pos.x, pos.y, 0);
		
		if(_useDisplaySource)
			_sourceSpace = source.getView().getViewPlatform();
 
if(Debug.getEnabled()){
Debug.println("DrmDragPlugin",
"DRM:DrmDragPlugin:toTargetValue:" +
" sourceDsp=" + source +
" inputPos=" + pos +
" sourcePos=" + copy +
" sourceSpc=" + _sourceSpace);}
		
		return true;
	}

	// IntuitiveDragMapperPlugin implementation
	
	protected boolean startInputDrag(Canvas3D source, Vector2d pos,
	 Vector3d copy) {

if(Debug.getEnabled()){
Debug.println("DrmDragPlugin",
"DRM:DrmDragPlugin:startInputDrag:...");}
		
		return toTargetValue(source, pos, copy);
	}
	
	protected boolean doInputDrag(Canvas3D source, Vector2d pos,
	 Vector3d copy) {
 
if(Debug.getEnabled()){
Debug.println("DrmDragPlugin",
"DRM:DrmDragPlugin:doInputDrag:...");}
		
		return toTargetValue(source, pos, copy);
	}
	
	protected boolean stopInputDrag(Canvas3D source, Vector2d pos,
	 Vector3d copy) {
 
if(Debug.getEnabled()){
Debug.println("DrmDragPlugin",
"DRM:DrmDragPlugin:stopInputDrag:...");}
		
		return toTargetValue(source, pos, copy);
	}
	
}