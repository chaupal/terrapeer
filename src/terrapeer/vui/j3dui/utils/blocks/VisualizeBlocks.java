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
 first introduced for the book's examples on "visualization".
 <P>
 Because these building blocks are rather specific in purpose
 they are included here as utilities instead of as general
 purpose building block classes in the framework core.
 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */

public class VisualizeBlocks
{

  // public utilities =========================================

  /**
    Builds a FOV actuator, attaches the specified view to it,
    and connects it to a relative dragger.  Increasing dragger
    Y decreases FOV (zoom in).  Note that in AppView a FOV of
    zero results in a parallel projection.
    @param view View to be attached and the drag input source.
    @param clamp Limits on the view FOV range.
    @param mouseButtons Mouse buttons enabling drag
    (Input.BUTTON_???).
    @param mouseModifiers Modifier keys enabling mouse drag
    (Input.MODIFIER_???).
    @param arrowModifiers Modifier keys enabling arrow drag
    @return New orbit group.
   */
  public static final Actuator buildViewFover(
      AppView view, Vector2d clamp, int mouseButtons,
      int mouseModifiers, int arrowModifiers)
  {

    class FovPlugin extends ActuatorPlugin
    {
      public FovPlugin(AppView view)
      {
        _view = view;
      }

      public Node getTargetNode()
      {
        return _view;
      }

      public String toString()
      {
        return "";
      }

      protected void initActuation(Tuple4d value)
      {
        toActuationSource(value, _value);
        _state.set(_value);
        _reference.set(_state);
        toActuationTarget(_state, _state);
        _view.setViewFov(_state.y);
      }

      protected void updateActuation(Tuple4d value)
      {
        toActuationSource(value, _value);
        _state.y = _reference.y + _value.y;
        toActuationTarget(_state, _state);
        _view.setViewFov(_state.y);
      }

      protected void syncActuation()
      {
        _reference.set(_state);
      }

      private AppView _view;
      private Vector4d _reference = new Vector4d();
      private Vector4d _state = new Vector4d();
      private final Vector4d _value = new Vector4d();
    }

    // build fover
    Actuator fover = new Actuator(new FovPlugin(view));
    fover.getPlugin().setTargetClamp(Mapper.DIM_ALL, clamp);
    fover.initActuation(
        new Vector4d(0, view.getViewFov(), 0, 0));

    // build control
    InputDragTarget mapper = ActuationBlocks.
        buildDirectMapper(fover,
                          new Vector2d(0, -.01), Mapper.DIM_NONE,
                          Mapper.DIM_Y, true);

    InputDragSplitter spltr = new InputDragSplitter();
    ActuationBlocks.buildRelativeDragger(spltr, view,
                                         mouseButtons, mouseModifiers,
                                         arrowModifiers);
    spltr.addEventTarget(mapper);

    // done		
    return fover;
  }

}