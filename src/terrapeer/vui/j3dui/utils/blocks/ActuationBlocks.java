package terrapeer.vui.j3dui.utils.blocks;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.utils.*;
import terrapeer.vui.j3dui.utils.app.*;
import terrapeer.vui.j3dui.utils.objects.*;
import terrapeer.vui.j3dui.control.*;
import terrapeer.vui.j3dui.control.inputs.*;
import terrapeer.vui.j3dui.control.inputs.sensors.*;
import terrapeer.vui.j3dui.control.mappers.*;
import terrapeer.vui.j3dui.control.mappers.intuitive.*;
import terrapeer.vui.j3dui.control.actuators.*;
import terrapeer.vui.j3dui.control.actuators.groups.*;
import terrapeer.vui.j3dui.visualize.*;

/**
 These utilities are common building block utilities that were
 first introduced for the book's examples on "actuation" control.
 <P>
 Because these building blocks are rather specific in purpose
 they are included here as utilities instead of as general
 purpose building block classes in the framework core.
 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */

public class ActuationBlocks
{

  // public utilities =========================================

  /**
    Creates mouse and arrow drag sensors, enables the two sensor
    outputs according to button and modifier conditions,
    connects the combined drag inputs to a drag canceler, and
    connects it to the input drag target.
    <P>
    Arrow keys are inherently a relative device.  To use them
    as an absolute device the mouse is used to establish an
    absolute reference position by means of an input move sensor
    connected to an input offset filter that filters the arrow
    sensor output.
    @param target Event target.
    @param view Source display.  Never null.
    @param mouseButtons Mouse buttons enabling drag
    (Input.BUTTON_???).
    @param mouseModifiers Modifier keys enabling mouse drag
    (Input.MODIFIER_???).
    @param arrowModifiers Modifier keys enabling arrow drag
    (Input.MODIFIER_???).
   */
  public static final void buildAbsoluteDragger(
      InputDragTarget target, AppView view, int mouseButtons,
      int mouseModifiers, int arrowModifiers)
  {

    new AbsoluteDragger(view.getDisplay(), target,
                        mouseButtons, mouseModifiers, arrowModifiers);

    /*
      EnableInputDragFilter targetEnable =
       new EnableInputDragFilter(target, true);
      // drag cancel
      InputCancelTrigger cancelTrigger =
       new InputCancelTrigger(targetEnable);
      KeyboardModifierSensor cancelModifier =
       new KeyboardModifierSensor(cancelTrigger,
       null, view.getRoot());
      cancelModifier.setModifiers(Input.MODIFIER_ESC);
      // absolute mouse drag
      EnableInputDragFilter mouseEnable =
       new EnableInputDragFilter(targetEnable);
      MouseDragSensor mouseDrag = new MouseDragSensor(
       mouseEnable, view.getDisplay(), view.getRoot());
      mouseDrag.setButtons(mouseButtons);
      // absolute arrow drag
      EnableInputDragFilter arrowEnable =
       new EnableInputDragFilter(targetEnable);
      /// offset arrow with mouse		
      OffsetInputDragPlugin arrowOffset =
       new OffsetInputDragPlugin();
      InputDragFilter arrowFilter = new InputDragFilter(
       arrowEnable, arrowOffset);
      MouseMoveSensor mouseMove = new MouseMoveSensor(
       arrowOffset, view.getDisplay(), view.getRoot());
      AwtKeyboardArrowSensor arrowDrag =
       new AwtKeyboardArrowSensor(arrowFilter,
       view.getDisplay());
      // modifiers
      InputModifierTrigger mouseTrigger =
       new InputModifierTrigger(mouseEnable, mouseModifiers,
       true);
      mouseTrigger.initEventTarget(
       mouseModifiers==Input.MODIFIER_NONE);
      InputModifierTrigger arrowTrigger =
       new InputModifierTrigger(arrowEnable, arrowModifiers,
       true);
      arrowTrigger.initEventTarget(
       arrowModifiers==Input.MODIFIER_NONE);
      InputModifierSplitter modifierSplitter =
       new InputModifierSplitter();
      modifierSplitter.addEventTarget(mouseTrigger);
      modifierSplitter.addEventTarget(arrowTrigger);
      KeyboardModifierSensor mod = new KeyboardModifierSensor(
       modifierSplitter, null, view.getRoot());
     */
  }

  /**
    Builds an absolute dragger, connects it to a relative
    drag filter, and connects the filter to the input drag
    target.
    @param target Event target.  Never null.
    @param view Source display.  Never null.
    @param mouseButtons Mouse buttons enabling drag
    (Input.BUTTON_???).
    @param mouseModifiers Modifier keys enabling mouse drag
    (Input.MODIFIER_???).
    @param arrowModifiers Modifier keys enabling arrow drag
    (Input.MODIFIER_???).
   */
  public static final void buildRelativeDragger(
      InputDragTarget target, AppView view, int mouseButtons,
      int mouseModifiers, int arrowModifiers)
  {

    new RelativeDragger(view.getDisplay(), target,
                        mouseButtons, mouseModifiers, arrowModifiers,
                        new Vector2d(1, 1), new Vector2d(1, 1));

    /*	
      // relative mouse drag
      InputDragFilter filter = new InputDragFilter(
       target, new RelativeInputDragPlugin());
      // absolute dragger
      buildAbsoluteDragger(filter, view, mouseButtons,
       mouseModifiers, arrowModifiers);
     */
  }

  /**
    Creates an input drag mapper for direct mapping, sets the
    source scale and dimension map, and connects the mapped
    drag input to the actuation target.
    @param target Event target.
    @param dragScale Display space scale factor.
    @param dragMapX Input X to actuation coordinate map
    (Mapper.DIM_???).
    @param dragMapY Input Y to actuation coordinate map
    (Mapper.DIM_???).
    @param cumulative True if drag actions are cumulative.
    @return New input drag mapper.
   */
  public static final InputDragMapper buildDirectMapper(
      ActuationTarget target, Vector2d dragScale,
      int dragMapX, int dragMapY, boolean cumulative)
  {

    DirectInputDragPlugin plugin =
        new DirectInputDragPlugin();
    plugin.setSourceScale(dragScale);
    plugin.setSourceMap(dragMapX, dragMapY);

    InputDragMapper mapper = new InputDragMapper(
        target, plugin);
    mapper.setCumulative(cumulative);

    return mapper;
  }

  /**
    Creates an OrbitGroup, attaches the specified view to it
    with a headlight, and connects it to a relative dragger.
    @param view View to be attached and the source of drag input.
    @param orbit Initial orbit angles.
    @param dist Initial distance from view to LAP.
    @param mouseButtons Mouse buttons enabling drag
    (Input.BUTTON_???).
    @param mouseModifiers Modifier keys enabling mouse drag
    (Input.MODIFIER_???).
    @param arrowModifiers Modifier keys enabling arrow drag
    @return New orbit group.
   */
  public static final OrbitGroup buildViewOrbiter(
      AppView view, Vector3d orbit, double dist, int mouseButtons,
      int mouseModifiers, int arrowModifiers)
  {

    // add headlight to view
    view.addNode(new TestLight());

    // move view back
    AffineGroup viewGroup = new AffineGroup(view);
    viewGroup.getTranslation().initActuation(
        TranslationPlugin.toActuation(
        new Vector3d(0, 0, dist))
        );

    // build orbiter
    OrbitGroup orbitGroup = new OrbitGroup(viewGroup);
    orbitGroup.initActuation(OrbitGroup.toActuation(orbit));

    /// limit range		
    orbitGroup.getElevation().getPlugin().setTargetClamp(
        Mapper.DIM_W, new Vector2d( -Math.PI / 2, Math.PI / 2));

    // build control
    InputDragTarget mapper = buildDirectMapper(orbitGroup,
                                               new Vector2d(.01, -.01),
                                               Mapper.DIM_Y, Mapper.DIM_X,
                                               true);

    InputDragSplitter spltr = new InputDragSplitter();
    buildRelativeDragger(spltr, view, mouseButtons,
                         mouseModifiers, arrowModifiers);
    spltr.addEventTarget(mapper);

    /// change posters (kludge for Java 3D 1.1.2 view change)
//		if(ChangePoster.isInit()) {
//			spltr.addEventTarget(new ChangePoster(
//			 view, ChangePoster.POST_VIEW_EXTERNAL));
//		}

    // done		
    return orbitGroup;
  }

}