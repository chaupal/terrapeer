package  terrapeer.vui.j3dui.control;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.inputs.*;
import terrapeer.vui.j3dui.control.mappers.*;
import terrapeer.vui.j3dui.control.actuators.*;

/**
A convenience class "mapper" with an InputDragTarget event input
and an ActuationTarget event output whose value is directly
mapped from the input display position.  Use getDirectPlugin()
to configure the direct mapping parameters.
<P>
The event chain uses an InputDragMapper with a
DirectInputDragPlugin; and connects that mapper to an actuation
splitter as the event out.
<P>
Since this class only deals with direct mapping, the target can
be an actuation event target, exposed as an event out splitter,
instead of an actuator.

@author Jon Barrilleaux,
copyright (c) 2000 Jon Barrilleaux,
All Rights Reserved.
*/
public class DirectMapper implements InputDragTarget {
	
	// public interface =========================================

	/**
	Constructs a DirectMapper.
	*/
	public DirectMapper() {
		_inputMapper = new InputDragMapper(_eventOut,
		 new DirectInputDragPlugin());
	}

	/**
	Constructs a DirectMapper with an event target.
	@param target Event target.  Null if none.
	*/
	public DirectMapper(ActuationTarget target) {
		this();
		if(target!=null) getEventOut().addEventTarget(target);
	}

	/**
	Constructs a DirectMapper with an event target and common
	parameters.
	@param target Event target.  Null if none.
	@param cumulative If true, the actuation is cumulative
	between drags.  Defaults to true. 
	@param sourceScale Source scale factor.  Defaults to (1, 1).
	Null if none.
	@param mapX The target dimensions (Mapper.DIM_???)
	controlled by the source X dimension.  Defaults to
	Mapper.DIM_X.
	@param mapY The target dimensions (Mapper.DIM_???)
	controlled by the source Y dimension.  Defaults to
	Mapper.DIM_Y.
	@param targetOffset Target offset constant.  Defaults to
	(0, 0, 0, 0).  Null if none.
	*/
	public DirectMapper(ActuationTarget target,
	 boolean cumulative, Vector2d sourceScale,
	 int sourceMapX, int sourceMapY, Vector4d targetOffset) {
	
	 	this(target);
		setCumulative(cumulative);
		if(sourceScale!=null) getDirectPlugin().
		 setSourceScale(sourceScale);
		getDirectPlugin().setSourceMap(sourceMapX, sourceMapY);
		if(targetOffset!=null) getDirectPlugin().
		 setTargetOffset(targetOffset);
	}
	
	/**
	Gets the event output splitter.  Use it to add and
	remove event targets.
	@return Reference to the event out splitter.  Never null.
	*/
	public ActuationSplitter getEventOut() {
		return _eventOut;
	}
	
	/**
	Sets whether or not the drag action is cumulative.
	@param enable If true, the action is cumulative
	between drags.  Defaults to true. 
	*/
	public void setCumulative(boolean enable) {
		_inputMapper.setCumulative(enable);
	}
	
	/**
	Gets the direct mapper's plugin.  Use it to configure the
	direct mapping (scale, mapping, offset).
	@return Reference to the mapper plugin.  Never null.
	*/
	public DirectInputDragPlugin getDirectPlugin() {
		return (DirectInputDragPlugin)(
		 _inputMapper.getPlugin());
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
	
	/** Event out splitter. Never null. */
	private ActuationSplitter _eventOut =
	 new ActuationSplitter();
	
	/** Direct input mapper. Never null. */
	private InputDragMapper _inputMapper;
	
}
