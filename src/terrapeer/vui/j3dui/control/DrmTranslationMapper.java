package  terrapeer.vui.j3dui.control;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;
import terrapeer.vui.j3dui.control.inputs.*;
import terrapeer.vui.j3dui.control.actuators.*;
import terrapeer.vui.j3dui.control.mappers.*;
import terrapeer.vui.j3dui.control.mappers.intuitive.*;

/**
A convenience class "mapper" with an InputDragTarget input
and a translation Actuator target.  The target position will
be relative to the position of the drag on the source
display.
<P>
Mappings appropriate for DRM translation are predefined
(TRANSLATE_???).  The mappings are defined for canonical target
object movement (i.e. drag right to move the target to the
right).  Provisions are included to reverse the sense of the
mapping (i.e. drag right to move the target left), which can be
useful if the target is the view itself.
<P>
The event chain uses an IntuitiveDragMapper with a DRM plugin;
connects that mapper to a source mapper with a direct plugin
configured for translation; and connects that mapper to the
target actuator.
<P>
Since this class deals with intuitive mapping, the target must
be a single Actuator instead of an actuation target exposed as
an event out splitter.

@author Jon Barrilleaux,
copyright (c) 2000 Jon Barrilleaux,
All Rights Reserved.
*/
public class DrmTranslationMapper implements InputDragTarget {
 
 	// public constants =========================================

	/** Constant for mapping display drag X to target
	translation X.  Display drag Y is ignored. */
	public static final int TRANSLATE_X_X = 1;

	/** Constant for mapping display drag Y to target
	translation Y.  Display drag X is ignored. */
	public static final int TRANSLATE_Y_Y = 2;

	/** Constant for mapping display drag Y to target
	translation -Z.  Display drag X is ignored. */
	public static final int TRANSLATE_Y_Z = 3;

	/** Constant for mapping display drag X,Y to target
	translation X,Y.  The target object appears to move
	parallel to the view plane. */
	public static final int TRANSLATE_XY_XY = 4;

	/** Constant for mapping display drag X,Y to target
	translation X,-Z.  The target object appears to move
	perpendicular to the view plane. */
	public static final int TRANSLATE_XY_XZ = 5;
	
	// public interface =========================================

	/**
	Constructs a DrmTranslationMapper with an actuator target
	and DRM plugin.
	@param actuator Target actuator.  Must be a translation
	actuator.  The actuator's target node serves as the mapper's
	target space node.  Never null.
	*/
	public DrmTranslationMapper(Actuator actuator) {
	 
		if(actuator==null) throw new
		 IllegalArgumentException("'actuator' is null.");
		_actuator = actuator;

		// check actuator and build direct source mapper
		if(!(_actuator.getPlugin() instanceof TransformGroupPlugin &&
		 ((TransformGroupPlugin)_actuator.getPlugin()).getPlugin()
		 instanceof TranslationPlugin)) {
			throw new IllegalArgumentException(
			 "'actuator' does not have a TranslationPlugin.");
		}
		
		DirectSourceDragPlugin sourcePlugin =
		 new DirectSourceDragPlugin(_actuator.getTargetNode());
		
		_sourceMapper = new SourceDragMapper(
		 _actuator, sourcePlugin);

		// build DRM intuitive drag mapper		
		DrmDragPlugin drmPlugin = new DrmDragPlugin();
		 		
		_inputMapper = new IntuitiveDragMapper(
		 _sourceMapper, drmPlugin);
		
		// set default mapping
		setMapping(TRANSLATE_XY_XY, false);
	}

	/**
	Constructs a DrmTranslationMapper with an actuator target
	and common parameters.
	@param actuator Target actuator.  Must be a translation
	actuator.  The actuator's target node serves as the mapper's
	target space node.  Never null.
	@param cumulative If true, the actuation is cumulative
	between drags.  Defaults to true. 
	@param mapping Translation mapping choice (TRANSLATE_???).
	Defaults to TRANSLATE_XY_XY. 
	*/
	public DrmTranslationMapper(Actuator actuator,
	 boolean cumulative, int mapping, boolean reverse) {
	
	 	this(actuator);
		setCumulative(cumulative);
		setMapping(mapping, reverse);
	}
	
	/**
	Gets the target actuator.  Use it to configure actuation
	parameters (offset, scale, clamp).
	@return Reference to the target actuator.  Never null.
	*/
	public Actuator getActuator() {
		return _actuator;
	}
	
	/**
	Sets whether or not the drag action is cumulative.
	@param enable If true, the action is cumulative
	between drags.  Defaults to true. 
	*/
	public void setCumulative(boolean enable) {
		_sourceMapper.setCumulative(enable);
	}
	
	/**
	Sets the display drag to actuator translation mapping and
	direction sense.  Only mappings that make sense for DRM
	translation are allowed.  If the target is the view, with
	a normal sense, the drag appears to move the view; but, with
	a reverse sense, the drag appears to move the world. 
	@param mapping Translation mapping choice (TRANSLATE_???).
	Defaults to TRANSLATE_XY_XY. 
	@param reverse If false, the sense is normal.  If true, the
	sense is reversed.  Defaults to false. 
	*/
	public void setMapping(int mapping, boolean reverse) {
		DirectSourceDragPlugin plugin =
		 (DirectSourceDragPlugin)_sourceMapper.getPlugin();
		
		Vector3d sense = new Vector3d();

		// set mapping
		switch(mapping){
		 case TRANSLATE_X_X:
			plugin.setSourceMap(Mapper.DIM_X, Mapper.DIM_NONE,
			 Mapper.DIM_NONE);
			sense.set(1, 0, 0);
		 	break;
		 case TRANSLATE_Y_Y:
			plugin.setSourceMap(Mapper.DIM_NONE, Mapper.DIM_Y,
			 Mapper.DIM_NONE);
			sense.set(0, 1, 0);
		 	break;
		 case TRANSLATE_Y_Z:
			plugin.setSourceMap(Mapper.DIM_NONE, Mapper.DIM_Z,
			 Mapper.DIM_NONE);
			sense.set(0, -1, 0);
		 	break;
		 case TRANSLATE_XY_XY:
			plugin.setSourceMap(Mapper.DIM_X, Mapper.DIM_Y,
			 Mapper.DIM_NONE);
			sense.set(1, 1, 0);
		 	break;
		 case TRANSLATE_XY_XZ:
			plugin.setSourceMap(Mapper.DIM_X, Mapper.DIM_Z,
			 Mapper.DIM_NONE);
			sense.set(1, -1, 0);
		 	break;
		}
		
		// set sense
		if(reverse) sense.negate();
		plugin.setSourceScale(sense);
	}

	// InputDragTarget implementation
	
	public void startInputDrag(Canvas3D source, Vector2d pos) {
		_inputMapper.startInputDrag(source, pos);
	}
	
	public void doInputDrag(Canvas3D source, Vector2d pos) {
		_inputMapper.doInputDrag(source, pos);
	}
	
	public void stopInputDrag(Canvas3D source, Vector2d pos) {
		_inputMapper.stopInputDrag(source, pos);
	}
			
	// personal body ============================================
	
	/** Target actuator. Never null. */
	private Actuator _actuator;
	
	/** Source mapper. Never null. */
	private SourceDragMapper _sourceMapper;
	
	/** Input mapper. Never null. */
	private IntuitiveDragMapper _inputMapper;	
}
