package  terrapeer.vui.j3dui.control;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;
import terrapeer.vui.j3dui.control.inputs.*;
import terrapeer.vui.j3dui.control.actuators.*;
import terrapeer.vui.j3dui.control.mappers.intuitive.*;

/**
A convenience class "mapper" with an AbsoluteDragger input
and a translation Actuator target.  The target position will
be relative to the position of the drag in the world on a
WRM source object.
<P>
As with any form of WRM, an absolute dragger with no offset
or scaling must be used as the event drag input.  An
AbsoluteDragger satisfies this condition, and provides support
for mouse and keyboard input, and multiple source displays. 
<P>
The event chain uses an IntuitiveDragMapper with a WRM plugin;
connects that mapper to a source mapper with a direct plugin
configured for translation; and connects that mapper to the
target actuator.
<P>
Since this class deals with intuitive mapping, the target must
be a single Actuator, instead of an actuation target exposed as
an event out splitter.

@author Jon Barrilleaux,
copyright (c) 2000 Jon Barrilleaux,
All Rights Reserved.
*/
public class WrmTranslationMapper {
	
	// public interface =========================================

	/**
	Constructs a WrmTranslationMapper with a WRM plugin and a
	target actuator and reference space node.
	@param dragger Absolute display dragger.  Never null.
	@param plugin WRM drag mapper plugin.  Never null.
	@param actuator Target actuator.  Must be a translation
	actuator.  The actuator's target node serves as the mapper's
	target space node.  Never null.
	*/
	public WrmTranslationMapper(AbsoluteDragger dragger,
	 WrmDragPlugin wrmPlugin, Actuator actuator) {
	 
		if(dragger==null) throw new
		 IllegalArgumentException("'dragger' is null.");
		if(wrmPlugin==null) throw new
		 IllegalArgumentException("'wrmPlugin' is null.");
		if(actuator==null) throw new
		 IllegalArgumentException("'actuator' is null.");
		
		_dragger = dragger;
		_wrmPlugin = wrmPlugin;
		_actuator = actuator;
		
		// check actuator type and build direct source mapper
		if(!(_actuator.getPlugin() instanceof TransformGroupPlugin &&
		 ((TransformGroupPlugin)_actuator.getPlugin()).getPlugin()
		 instanceof TranslationPlugin)) {
			throw new IllegalArgumentException(
			 "'actuator' does not have a TranslationPlugin.");
		}
		
		DirectSourceDragPlugin sourcePlugin =
		 new DirectSourceDragPlugin(_actuator.getTargetNode());

		SourceDragMapper sourceMapper = new SourceDragMapper(
		 _actuator, sourcePlugin);
		sourceMapper.setCumulative(false);

		// build WRM intuitive drag mapper		
		IntuitiveDragMapper inputMapper =
		 new IntuitiveDragMapper(sourceMapper, _wrmPlugin);
		
		// connect dragger to input mapper
		_dragger.getEventOut().addEventTarget(inputMapper);
	}
	
	/**
	Gets the absolute dragger.  Use it to configure dragger
	parameters (buttons and modifiers).
	@return Reference to the target actuator.  Never null.
	*/
	public AbsoluteDragger getAbsoluteDragger() {
		return _dragger;
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
	Gets the WRM mapper's plugin.  Use it to configure the WRM
	mapping (source picking, etc.).
	@return Reference to the WRM plugin.  Never null.
	*/
	public WrmDragPlugin getWrmPlugin() {
		return _wrmPlugin;
	}
			
	// personal body ============================================
	
	/** Absolute dragger. Never null. */
	private AbsoluteDragger _dragger;
	
	/** WRM plugin. Never null. */
	private WrmDragPlugin _wrmPlugin;
	
	/** Target actuator. Never null. */
	private Actuator _actuator;
	
}
