package  terrapeer.vui.j3dui.control;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;
import terrapeer.vui.j3dui.control.inputs.*;
import terrapeer.vui.j3dui.control.inputs.sensors.*;

/**
A convenience class "dragger" with mouse and keyboard input and
an InputDragTarget event output whose position is relative to
the drag start position in the source display space.  If more
than one source display is used, the same dragger configuration
applies to all of them.
<P>
The event chain uses a mouse and keyboard drag sensor;
connects the keyboard output to an absolute offset filter
driven by the mouse for keyboard input reference; enables the
two sensor outputs according to button and modifier conditions;
connects each enable output to scale filters; connects those
two filters to a drag enable filter used for drag canceling; and
connects that filter to an input drag splitter as the event out.
<P>
Arrow keys are inherently a relative device.  To use them
as an absolute device the mouse is used to establish an
absolute reference position by means of an input move sensor
connected to an input offset filter that filters the arrow
sensor output.

@author Jon Barrilleaux,
copyright (c) 2000 Jon Barrilleaux,
All Rights Reserved.
*/
public class RelativeDragger {
	
	// public interface =========================================

	/**
	Constructs an RelativeDragger.  For it to work, you must use
	addEventSource() to add a source display.
	*/
	public RelativeDragger() {
		// make drag relative
		InputDragFilter relativeFilter =
		 new InputDragFilter(_eventOut,
		 new RelativeInputDragPlugin());

		// drag canceler (requires separate mod sensor)
		EnableInputDragFilter dragEnabler =
		 new EnableInputDragFilter(relativeFilter, true);
		
		InputCancelTrigger cancelTrigger =
		 new InputCancelTrigger(dragEnabler);
		
		_canceler = new AwtKeyboardModifierSensor(
		 null, cancelTrigger);
		_canceler.setModifiers(Input.MODIFIER_ESC);
		
		// absolute mouse drag
		/// scale drag
		_mouseScale = new ScaleInputDragPlugin(
		 new Vector2d(0.01, 0.01));
		
		InputDragFilter mouseFilter = new InputDragFilter(
		 dragEnabler, _mouseScale);

		/// enable drag from modifiers		
		EnableInputDragFilter mouseEnabler =
		 new EnableInputDragFilter(mouseFilter);

		/// sense mouse drag		
		_mouseDragger = new AwtMouseDragSensor(null,
		 mouseEnabler);
		
		// absolute arrow drag
		/// scale drag
		_arrowScale = new ScaleInputDragPlugin(
		 new Vector2d(0.01, 0.01));
		
		InputDragFilter arrowFilter = new InputDragFilter(
		 dragEnabler, _arrowScale);

		/// enable drag from modifiers		
		EnableInputDragFilter arrowEnabler =
		 new EnableInputDragFilter(arrowFilter);

		/// offset arrow with mouse		
		OffsetInputDragPlugin arrowOffset =
		 new OffsetInputDragPlugin();
		InputDragFilter offsetFilter = new InputDragFilter(
		 arrowEnabler, arrowOffset);
		
		_mouseMover = new AwtMouseMoveSensor(null,
		 arrowOffset);

		/// sense arrow drag		
		_arrowDragger = new AwtKeyboardDragSensor(null,
		 offsetFilter);
		
		// drag modifiers
		_mouseTrigger = new InputModifierTrigger(mouseEnabler);
		_arrowTrigger = new InputModifierTrigger(arrowEnabler);
		
		_modifier = new AwtKeyboardModifierSensor();
		_modifier.getEventOut().addEventTarget(_mouseTrigger);
		_modifier.getEventOut().addEventTarget(_arrowTrigger);
		
		// set defaults and init triggers
		setMouseButtons(Input.BUTTON_ALL);
		setMouseModifiers(Input.MODIFIER_ALL);
		setArrowModifiers(Input.MODIFIER_ALL);
	}

	/**
	Constructs an RelativeDragger with an event source
	and target.
	@param source Event and space source display.  Null if none,
	but one must eventually be added.
	@param target Event target.  Null if none.
	*/
	public RelativeDragger(Canvas3D source,
	 InputDragTarget target) {

	 	this();
		if(source!=null) addEventSource(source);
		if(target!=null) getEventOut().addEventTarget(target);
	}

	/**
	Constructs an RelativeDragger with an event target and common
	parameters.
	@param source Event and space source display.  Null if none,
	but one must eventually be added.
	@param target Event target.  Null if none.
	@param mouseButtons Button flags (Input.BUTTON_???).
	Defaults to Input.BUTTON_ALL.  If BUTTON_NONE or
	BUTTON_IGNORE, mouse dragging is disabled.
	@param mouseModifiers Modifier flags (Input.MODIFIER_???).
	Defaults to MODIFIER_ALL.  If MODIFIER_IGNORE, mouse
	dragging is disabled.
	@param arrowModifiers Modifier flags (Input.MODIFIER_???).
	Defaults to MODIFIER_ALL.  If MODIFIER_IGNORE, arrow
	dragging is disabled.
	@param mouseScale Mouse drag scale factor by input dimension.
	Defaults to (0.01, 0.01).  Null if none.
	@param arrowScale Arrow drag scale factor by input dimension.
	Defaults to (0.01, 0.01).  Null if none.
	*/
	public RelativeDragger(Canvas3D source,
	 InputDragTarget target, int mouseButtons,
	 int mouseModifiers, int arrowModifiers,
	 Vector2d mouseScale, Vector2d arrowScale) {
	
	 	this(source, target);
		
		setMouseButtons(mouseButtons);
		setMouseModifiers(mouseModifiers);
		setArrowModifiers(arrowModifiers);

		if(mouseScale!=null) setMouseScale(mouseScale);
		if(arrowScale!=null) setArrowScale(arrowScale);
	}
	
	/**
	Gets the event output splitter.  Use it to add and
	remove event targets.
	@return Reference to the event out splitter.  Never null.
	*/
	public InputDragSplitter getEventOut() {
		return _eventOut;
	}

	/**
	Adds a 3D display as an event source for this dragger,
	and sets capability bits of its view platform for use
	in intuitive mapping.
	@param source Event and space source display.  Never null.
	*/
	public void addEventSource(Canvas3D source) {
		if(source==null) throw new
		 IllegalArgumentException("'source' is null.");
		
		source.getView().getViewPlatform().setCapability(
		 Node.ALLOW_LOCAL_TO_VWORLD_READ);
		
		// order is important: canceler first
		_canceler.addEventSource(source);
		_mouseDragger.addEventSource(source);
		_mouseMover.addEventSource(source);
		_arrowDragger.addEventSource(source);
		_modifier.addEventSource(source);
	}
	
	/**
	Sets the mouse buttons that enable mouse dragging.
	@param mouseButtons Button flags (Input.BUTTON_???).
	Defaults to Input.BUTTON_ALL.  If BUTTON_NONE or
	BUTTON_IGNORE, mouse dragging is disabled.
	*/
	public void setMouseButtons(int buttons) {
		_mouseDragger.setButtons(buttons);
	}
	
	/**
	Sets the modifier keys that enable mouse dragging.
	@param modifiers Modifier flags (Input.MODIFIER_???).
	Defaults to MODIFIER_ALL.  If MODIFIER_IGNORE, mouse
	dragging is disabled.
	*/
	public void setMouseModifiers(int modifiers) {
		_mouseTrigger.setModifiers(modifiers);
		_mouseTrigger.initEventTarget(
		 modifiers==Input.MODIFIER_NONE);
	}
	
	/**
	Sets the modifier keys that enable arrow dragging.
	@param modifiers Modifier flags (Input.MODIFIER_???).
	Defaults to MODIFIER_ALL.  If MODIFIER_IGNORE, arrow
	dragging is disabled.
	*/
	public void setArrowModifiers(int modifiers) {
		_arrowTrigger.setModifiers(modifiers);
		_arrowTrigger.initEventTarget(
		 modifiers==Input.MODIFIER_NONE);
	}
	
	/**
	Sets the mouse drag scaling factor.
	@param scale Mouse drag scale factor by input dimension.
	Defaults to (0.01, 0.01).  Null if none.
	*/
	public void setMouseScale(Vector2d scale) {
		_mouseScale.setScale(scale);
	}
	
	/**
	Sets the arrow drag scaling factor.
	@param scale Mouse drag scale factor by input dimension.
	Defaults to (0.01, 0.01).  Null if none.
	*/
	public void setArrowScale(Vector2d scale) {
		_arrowScale.setScale(scale);
	}
			
	// personal body ============================================
	
	/** Event out splitter. Never null. */
	private InputDragSplitter _eventOut =
	 new InputDragSplitter();
	
	/** Canceler sensor. Never null. */
	private AwtKeyboardModifierSensor _canceler;
	
	/** Mouse dragger. Never null. */
	private AwtMouseDragSensor _mouseDragger;
	
	/** Mouse mover. Never null. */
	private AwtMouseMoveSensor _mouseMover;
	
	/** Arrow dragger. Never null. */
	private AwtKeyboardDragSensor _arrowDragger;
	
	/** Modifier sensor. Never null. */
	private AwtKeyboardModifierSensor _modifier;
		
	/** Mouse modifier trigger. Never null. */
	private InputModifierTrigger _mouseTrigger;
	
	/** Arrow modifier trigger. Never null. */
	private InputModifierTrigger _arrowTrigger;
	
	/** Mouse drag scale plugin. Never null. */
	private ScaleInputDragPlugin _mouseScale;
	
	/** Arrow drag scale plugin. Never null. */
	private ScaleInputDragPlugin _arrowScale;
	
}

