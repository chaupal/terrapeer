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
import terrapeer.vui.j3dui.feedback.*;
import terrapeer.vui.j3dui.feedback.elements.*;

/**
 These utilities are common building block utilities that were
 first introduced for the book's examples on "feedback".
 <P>
 Because these building blocks are rather specific in purpose
 they are included here as utilities instead of as general
 purpose building block classes in the framework core.
 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved. Edited 2003 by Henrik Gehrmann.
 */

public class FeedbackBlocks
{

  // public utilities =========================================

  /**
    Creates a MultiShape version of a TestThing whose box
    and ball color are determined by the overall interaction
    state.
    @return New MultiShape object.
   */
  public static final MultiShape buildMultiThing()
  {

    MultiStatus status = new MultiStatus();

    status.setStatusNode(Feedback.STATUS_NORMAL,
                         buildMultiSelectThing(Feedback.STATUS_NORMAL));
    status.setStatusNode(Feedback.STATUS_DISABLE,
                         buildMultiSelectThing(Feedback.STATUS_DISABLE));

    return status;
  }

  /**
    Creates a MultiSelect version of a TestThing whose box
    color is determined by the select state and whose ball color
    is determined by the action state.  The box color intensity
    will appear according to the status state.
    @param status Status state (STATUS_NORMAL or STATUS_DISABLE).
    @return New MultiSelect object.
   */
  public static final MultiSelect buildMultiSelectThing(
      int status)
  {

    MultiSelect select = new MultiSelect();

    if (status == Feedback.STATUS_NORMAL)
    {
      select.setSelectNode(Feedback.SELECT_NORMAL,
                           buildMultiActionThing(TestThing.BOX_NORMAL_0));
      select.setSelectNode(Feedback.SELECT_SINGLE,
                           buildMultiActionThing(TestThing.BOX_NORMAL_1));
      select.setSelectNode(Feedback.SELECT_DOUBLE,
                           buildMultiActionThing(TestThing.BOX_NORMAL_2));
      select.setSelectNode(Feedback.SELECT_TRIPLE,
                           buildMultiActionThing(TestThing.BOX_NORMAL_3));
    }
    else
    {
      select.setSelectNode(Feedback.SELECT_NORMAL,
                           buildMultiActionThing(TestThing.BOX_DISABLE_0));
      select.setSelectNode(Feedback.SELECT_SINGLE,
                           buildMultiActionThing(TestThing.BOX_DISABLE_1));
      select.setSelectNode(Feedback.SELECT_DOUBLE,
                           buildMultiActionThing(TestThing.BOX_DISABLE_2));
      select.setSelectNode(Feedback.SELECT_TRIPLE,
                           buildMultiActionThing(TestThing.BOX_DISABLE_3));
    }

    return select;
  }

  /**
    Creates a MultiAction version of a TestThing with the
    specified box color.  The ball color will be changed
    according to the interaction state.
    @param boxColor Box color for all shapes.
    @return New MultiAction object.
   */
  public static final MultiAction buildMultiActionThing(
      Color3f boxColor)
  {

    MultiAction action = new MultiAction();

    action.setActionNode(Feedback.ACTION_NORMAL,
                         new TestThing(boxColor, TestThing.BALL_NORMAL));
    action.setActionNode(Feedback.ACTION_OVER,
                         new TestThing(boxColor, TestThing.BALL_OVER));
    action.setActionNode(Feedback.ACTION_PAUSE,
                         new TestThing(boxColor, TestThing.BALL_PAUSE));
    action.setActionNode(Feedback.ACTION_DOWN,
                         new TestThing(boxColor, TestThing.BALL_DOWN));
    action.setActionNode(Feedback.ACTION_DRAG,
                         new TestThing(boxColor, TestThing.BALL_DRAG));
    action.setActionNode(Feedback.ACTION_DROP,
                         new TestThing(boxColor, TestThing.BALL_DROP));
    action.setActionNode(Feedback.ACTION_CANCEL,
                         new TestThing(boxColor, TestThing.BALL_CANCEL));

    return action;
  }

  /**
    Creates input sensors for the status trigger target.
    Includes various input sensors and a select canceler.
    @param target Event target.
   */
  public static final void
      buildStatusTriggerSupport(StatusTrigger target)
  {

    // limit states to available shapes
    target.setStatusFlags(Feedback.STATUS_NORMAL |
                          Feedback.STATUS_DISABLE);
  }

  /**
    Creates input sensors for the select trigger target.
    Includes various input sensors but no select canceler.
    @param target Event target.
    @param display Source display.  Never null.
    @param host Sensor host group.
   */
  public static final void
      buildSelectTriggerSupport(SelectTrigger target,
                                AppDisplay display, Group host)
  {

    if (display == null)
      throw new
          IllegalArgumentException("'display' is null.");

    // limit states to available shapes
    target.setSelectFlags(Feedback.SELECT_NORMAL |
                          Feedback.SELECT_SINGLE | Feedback.SELECT_DOUBLE |
                          Feedback.SELECT_TRIPLE);

    // build input sensors
    AwtMouseButtonSensor buttoner =
        new AwtMouseButtonSensor(display, target);

    /// select canceler
    InputCancelTrigger canceler =
        new InputCancelTrigger(target);

    AwtKeyboardModifierSensor modifier =
        new AwtKeyboardModifierSensor(display, canceler);
    modifier.setModifiers(Input.MODIFIER_ESC);
  }

  /**
    Creates input sensors for the action trigger target.  The
    trigger's pick targets are used for the pick engine pick
    list.  Includes various input sensors, an over picker, and
    a drag canceler.
    @param target Event target.
    @param dragSpltr An input drag splitter that supplies the
    same button and modifier triggering as that used for the
    target manipulation.
    @param display Source display.  Never null.
    @param pickRoot Scene graph pick root and sensor host group.
   */
  public static final void
      buildActionTriggerSupport(ActionTrigger target,
                                InputDragSplitter dragSpltr, AppDisplay display,
                                BranchGroup pickRoot)
  {

    if (display == null)
      throw new
          IllegalArgumentException("'display' is null.");

    // build input sensors
    dragSpltr.addEventTarget(target);

    InputMoveSplitter moveSpltr = new InputMoveSplitter();
    AwtMouseMoveSensor mover = new AwtMouseMoveSensor(
        display, moveSpltr);
    moveSpltr.addEventTarget(target);

    AwtMouseButtonSensor buttoner = new AwtMouseButtonSensor(
        display, target);

    // build move pauser
    InputPauseTrigger pauser =
        new InputPauseTrigger(target);
    moveSpltr.addEventTarget(pauser);
    target.getAllowPauseSource().addEventTarget(pauser);

    // build drag canceler
    InputCancelTrigger canceler =
        new InputCancelTrigger(target);

    AwtKeyboardModifierSensor modifier =
        new AwtKeyboardModifierSensor(display, canceler);
    modifier.setModifiers(Input.MODIFIER_ESC);

    // build over picker, use trigger's pick targets
    ObjectPickMapper picker = new ObjectPickMapper(target,
        new PickEngine(pickRoot, target.getPickTargets()));
    moveSpltr.addEventTarget(picker);
  }

  /**
    Creates a set of feedback managers and adds them to an empty
    group  manager.  Includes a modifier sensor for multi-
    selection.  Use buildTargetSupport() to add targets to the
    group manager for feedback management.
    @param display Source display.  Never null.
    @param host Host group for sensors.
    @return The feedback group manager.
   */
  public static final FeedbackGroupManager
      buildGroupManager(AppDisplay display, Group host)
  {

    if (display == null)
      throw new
          IllegalArgumentException("'display' is null.");

    // build feedback managers
    StatusManager statusManager = new StatusManager();
    SelectManager selectManager = new SelectManager();
    ActionManager actionManager = new ActionManager();

    /// monitor display for modifiers
    new AwtKeyboardModifierSensor(display, selectManager);

    // build group manager
    FeedbackGroupManager groupManager =
        new FeedbackGroupManager(statusManager, selectManager,
                                 actionManager);

    return groupManager;
  }

  /**
    Creates triggers and sensors for managing a target's
    feedback through a common feedback group manager.  The
    target's status trigger is returned so that it may be
    connected to an enable source for status feedback.
    @param target Pick and feedback target.
    @param groupManager Feedback group manager.  Never null.
    @param dragSpltr An input drag splitter that supplies the
    same button and modifier triggering as that used for the
    target manipulation.
    @param display Source display.  Never null.
    @param pickRoot Scene graph pick root and sensor host group.
    @return The target's status trigger.
   */
  public static final StatusTrigger
      buildTargetFeedbackSupport(MultiShape target,
                                 FeedbackGroupManager groupManager,
                                 InputDragSplitter dragSpltr,
                                 AppDisplay display,
                                 BranchGroup pickRoot)
  {

    if (display == null)
      throw new
          IllegalArgumentException("'display' is null.");

    EnableSplitter overSpltr = new EnableSplitter();

    // build select trigger
    SelectManager selectManager =
        groupManager.getSelectManager();

    SelectTrigger select = (SelectTrigger)
        selectManager.getTargetTrigger(target);

    if (select == null)
    {
      select = (SelectTrigger)
          selectManager.addTarget(target);
      overSpltr.addEventTarget(select);
    }

    FeedbackBlocks.buildSelectTriggerSupport(select,
                                             display, pickRoot);

    // build status trigger
    StatusManager statusManager =
        groupManager.getStatusManager();

    StatusTrigger status = (StatusTrigger)
        statusManager.getTargetTrigger(target);

    if (status == null)
    {
      status = (StatusTrigger)
          statusManager.addTarget(target);

      FeedbackBlocks.buildStatusTriggerSupport(status);
    }

    // build action trigger
    ActionManager actionManager =
        groupManager.getActionManager();

    ActionTrigger action = (ActionTrigger)
        actionManager.getTargetTrigger(target);

    if (action == null)
    {
      action = (ActionTrigger)
          actionManager.addTarget(target);
      action.getOverTargetSource().addEventTarget(overSpltr);
    }

    FeedbackBlocks.buildActionTriggerSupport(action,
                                             dragSpltr, display, pickRoot);

    return status;
  }

  /**
    Creates feedback enable triggers that enable target control
    elements (e.g. actuators for translation, rotation, scale)
    according to the selection state (single, double, triple) of
    the target's multi-shape feedback source.  The target's
    feedback status will be updated accordingly through its
    status trigger.
    <P>
    Note that all triggers will be initialized "true" to permit
    initialization of enable targets.  The final act of
    initialization must be to initialize the multishape target
    state, which in turn initializes the triggers and their
    enable targets.
    @param targetMulti Target's feedback source.  Never null.
    @param targetTrigger Target's status trigger.  Null if none.
    @param targetSingle Single-select enable target.  Null if
    none.
    @param targetDouble Double-select enable target.  Null if
    none.
    @param targetTriple Triple-select enable target.  Null if
    none.
   */
  public static final void buildTargetSelectEnablers(
      MultiShape targetMulti, EnableTarget targetTrigger,
      EnableTarget targetSingle, EnableTarget targetDouble,
      EnableTarget targetTriple)
  {

    FeedbackEnableTrigger enabler;
    int selectFlags = 0;

    // build actuator enablers by select
    if (targetSingle != null)
    {
      selectFlags |= Feedback.SELECT_SINGLE;
      enabler = new FeedbackEnableTrigger(targetSingle);
      enabler.setSelectFlags(Feedback.SELECT_SINGLE);
      enabler.setActionFlags(Feedback.ACTION_IS_OVER);
      targetMulti.addEventTarget(enabler);
      enabler.initEventTarget(true);
    }

    if (targetDouble != null)
    {
      selectFlags |= Feedback.SELECT_DOUBLE;
      enabler = new FeedbackEnableTrigger(targetDouble);
      enabler.setSelectFlags(Feedback.SELECT_DOUBLE);
      enabler.setActionFlags(Feedback.ACTION_IS_OVER);
      targetMulti.addEventTarget(enabler);
      enabler.initEventTarget(true);
    }

    if (targetTriple != null)
    {
      selectFlags |= Feedback.SELECT_TRIPLE;
      enabler = new FeedbackEnableTrigger(targetTriple);
      enabler.setSelectFlags(Feedback.SELECT_TRIPLE);
      enabler.setActionFlags(Feedback.ACTION_IS_OVER);
      targetMulti.addEventTarget(enabler);
      enabler.initEventTarget(true);
    }

    // build status feedback enabler
    if (targetTrigger != null)
    {
      enabler = new FeedbackEnableTrigger(targetTrigger);
      enabler.setSelectFlags(selectFlags);
      targetMulti.addEventTarget(enabler);
      enabler.initEventTarget(true);
    }
  }

  /**
    Creates a MultiAction tooltip that appears during a pause.
    The object faces out along the Z axis with the origin at
    its center.
    @param pickable If true the object will be pickable.
    @param calloutW Width of the callout rectangle.
    @param calloutH Height of the callout rectangle.
    @param calloutPath Path of the callout texture.
    @return New MultiAction object containing the decoration.
   */
  public static final MultiAction buildMultiTooltip(
      boolean pickable, double calloutW, double calloutH,
      String calloutPath)
  {

    MultiAction action = new MultiAction();

    // pause shape
    TextureRect pause = new TextureRect(pickable, null, 0,
                                        calloutPath, Elements.SIDE_ALL,
                                        calloutW, calloutH);

    // show actions
    action.setActionNode(Feedback.ACTION_PAUSE, pause);

    // make it visable by default
    action.setFeedbackAction(Feedback.ACTION_PAUSE);

    return action;
  }

  /**
    Creates a MultiAction manipulation handle for sliding a
    target object.  The object is a cone facing up along the
    Y axis with the origin at its bottom center.
    @param pickable If true the handle will be pickable.
    @param handleR Radius of the handle.
    @param handleH Height of the handle.
    @return New MultiAction object containing the decoration.
   */
  public static final MultiAction buildMultiSlide(
      boolean pickable, double handleR, double handleH)
  {

    String dir = Blocks.buildResourcePath("images/");
    MultiAction action = new MultiAction();

    // normal shape
    Node normal = new TextureCone(pickable, null, 0,
                                  dir + "NormalSlide.gif", Elements.SIDE_ALL,
                                  handleR, handleH);

    // over shape
    Node over = new TextureCone(pickable, null, 0,
                                dir + "OverSlide.gif", Elements.SIDE_ALL,
                                handleR, handleH);

    // show actions
    action.setActionNode(Feedback.ACTION_NORMAL |
                         Feedback.ACTION_CANCEL, normal);
    action.setActionNode(Feedback.ACTION_OVER |
                         Feedback.ACTION_PAUSE | Feedback.ACTION_DROP, over);

    // make it visable by default
    action.setFeedbackAction(Feedback.ACTION_OVER);

    return action;
  }

  /**
    Creates a MultiAction handle for rotating a target object.
    The object is a cyclinder facing up along the Y axis with
    the origin at its bottom center.
    @param pickable If true the object will be pickable.
    @param handleRB Bottom radius of the handle.
    @param handleRT Top radius of the handle.
    @param handleH Height of the handle.
    @return New MultiAction object containing the decoration.
   */
  public static final MultiAction buildMultiRotate(
      boolean pickable, double handleRB, double handleRT,
      double handleH)
  {

    String dir = Blocks.buildResourcePath("images/");
    MultiAction action = new MultiAction();

    // normal shape
    Node normal = new TextureCylinder(pickable, null, 0,
                                      dir + "NormalRotate.gif",
                                      Elements.SIDE_ALL,
                                      handleRB, handleRT, handleH);

    // over shape
    Node over = new TextureCylinder(pickable, null, 0,
                                    dir + "OverRotate.gif", Elements.SIDE_ALL,
                                    handleRB, handleRT, handleH);

    // show actions
    action.setActionNode(Feedback.ACTION_NORMAL |
                         Feedback.ACTION_CANCEL, normal);
    action.setActionNode(Feedback.ACTION_OVER |
                         Feedback.ACTION_PAUSE | Feedback.ACTION_DROP, over);

    // make it visable by default
    action.setFeedbackAction(Feedback.ACTION_OVER);

    return action;
  }

  /**
    Creates a MultiAction handle for lifting a target object.
    The object is a flat arrow pointing up along the Y axis with
    the origin at its bottom center.
    <P>
    This object was intended to be two flat arrows at right
    angles but problems with Java 3D 1.1.2 transparent object
    overlap preclude this.  As such, for practical use, this
    object must be made display facing.
    @param pickable If true the object will be pickable.
    @param handleW Width of the handle.
    @param handleH Height of the handle.
    @return New MultiShape object containing the decorations.
   */
  public static final MultiShape buildMultiLift(
      boolean pickable, double handleW, double handleH)
  {

    String dir = Blocks.buildResourcePath("images/");
    AffineGroup affine;

    // normal shape
    Group normal = new Group();

    /// front-facing arrow
    normal.addChild(new TextureRect(pickable, null, 0,
                                    dir + "NormalArrow.gif", Elements.SIDE_ALL,
                                    handleW, handleH));

    /* J3D transparency problems make this ugly
      /// side-facing arrow
      affine = new AffineGroup(new TextureRect(pickable,
       dir + "NormalArrow.gif", Elements.SIDE_ALL,
       handleW, handleH));
      affine.getAxisAngle().initActuation(
       new Vector4d(0, 1, 0, Math.PI/2.0));
      normal.addChild(affine);
     */

    // over shape
    Group over = new Group();

    /// front-facing arrow
    over.addChild(new TextureRect(pickable, null, 0,
                                  dir + "OverArrow.gif", Elements.SIDE_ALL,
                                  handleW, handleH));

    /* J3D transparency problems make this ugly
      /// side-facing arrow
      affine = new AffineGroup(new TextureRect(pickable,
       dir + "OverArrow.gif", Elements.SIDE_ALL,
       handleW, handleH));
      affine.getAxisAngle().initActuation(
       new Vector4d(0, 1, 0, Math.PI/2.0));
      over.addChild(affine);
     */

    // show actions
    MultiAction action = new MultiAction();

    action.setActionNode(Feedback.ACTION_NORMAL |
                         Feedback.ACTION_CANCEL, normal);
    action.setActionNode(Feedback.ACTION_OVER |
                         Feedback.ACTION_PAUSE | Feedback.ACTION_DROP, over);

    // shift origin
    affine = new AffineGroup(action);
    affine.getTranslation().initActuation(
        new Vector4d(0, handleH / 2.0, 0, 0));

    MultiShape handle = new MultiShape();
    handle.addProxyTarget(affine, action);

    // make it visable by default
    handle.setFeedbackAction(Feedback.ACTION_OVER);

    return handle;
  }

  /**
    Creates a MultiShape set of sound effects for a target.
    @param view Host environment for the sound audio device.
    @return New MultiShape object containing the decoration.
   */
  public static final MultiShape buildMultiSound(
      AppView view)
  {

    String dir = Blocks.buildResourcePath("sound/");
    FeedbackEnableTrigger enabler;
    SoundEffect sound;
    MultiShape multi = new MultiShape();

    // normal over sound
    sound = new SoundEffect(dir + "NormalOverSound.wav",
                            0.5, view.getView().getPhysicalEnvironment());
    enabler = new FeedbackEnableTrigger(sound);
    enabler.setStatusFlags(Feedback.STATUS_ALL);
    enabler.setSelectFlags(Feedback.SELECT_NORMAL);
    enabler.setActionFlags(Feedback.ACTION_IS_OVER);
    multi.addProxyTarget(sound, enabler);

    // select over sound
    sound = new SoundEffect(dir + "SelectOverSound.wav",
                            0.3, view.getView().getPhysicalEnvironment());
    enabler = new FeedbackEnableTrigger(sound);
    enabler.setStatusFlags(Feedback.STATUS_ALL);
    enabler.setSelectFlags(Feedback.SELECT_IS_SELECT);
    enabler.setActionFlags(Feedback.ACTION_IS_OVER);
    multi.addProxyTarget(sound, enabler);

    // select drag sound
    sound = new SoundEffect(dir + "SelectDragSound.wav",
                            0.3, view.getView().getPhysicalEnvironment());
    enabler = new FeedbackEnableTrigger(sound);
    enabler.setStatusFlags(Feedback.STATUS_ALL);
    enabler.setSelectFlags(Feedback.SELECT_IS_SELECT);
    enabler.setActionFlags(Feedback.ACTION_DRAG);
    multi.addProxyTarget(sound, enabler);

    return multi;
  }

  /**
    Creates a set of MultiShape feedback decorations for a
    target object that is intended to be slid, rotated, and
    lifted over an X-Z floor plane.  The decorations include
    tooltips, handles, and sound.
    @param target Target object to be centered, offset, and
    decorated.  If the target is a FeedbackTarget it will also
    be connected as a feedback target.
    @param slide Affine group used to slide the target.  The
    group will be connected into an internal group chain and
    therefore should be supplied as new.  Null if none.
    @param rotate Affine group used to rotate the target.  The
    group will be connected into an internal group chain and
    therefore should be supplied as new.  Null if none.
    @param lift Affine group used to slide the target.  The
    group will be connected into an internal target chain and
    therefore should be supplied as new.  Null if none.
    @param view Host environment for the sound audio device.
    If null then no sound.
    @param height Height (+Y) of the target above its origin.
    @return New MultiShape proxy target.
   */
  public static final MultiShape buildMultiFloorTarget(
      Node target, AffineGroup slide, AffineGroup rotate,
      AffineGroup lift, AppView view, double height)
  {

    AffineGroup affine;
    MultiSelect select;

    String dir = Blocks.buildResourcePath("images/");
    double handleH = .5; // height
    double slideR = 1; // radius
    double rotateRT = .4; // radius top
    double rotateRB = .8; // radius bottom
    double liftW = 1; // width
    double liftH = .66; // height
    double calloutW = 3; // width
    double calloutH = .75; // height
    double margin = .4;

    // build transform chain
    MultiShape multiTarget = new MultiShape();
    Node node = target;

    if (rotate != null)
    {
      rotate.addNode(node);
      node = rotate;
    }
    if (lift != null)
    {
      lift.addNode(node);
      node = lift;
    }
    if (slide != null)
    {
      slide.addNode(node);
      node = slide;
    }
    multiTarget.addNode(node);

    // add pickable multi target
    PickUtils.setGeoPickable(target, true);

    if (target instanceof FeedbackTarget)
      multiTarget.addEventTarget( (FeedbackTarget) target);

/*
      // add multi sounds
    if (view != null)
    {
      MultiShape sound = FeedbackBlocks.
          buildMultiSound(view);
      multiTarget.addProxyTarget(sound, sound);
    }
*/
    // add multi handles, by selection
    select = new MultiSelect();

    select.setSelectNode(Feedback.SELECT_SINGLE,
                         buildMultiSlide(true, slideR, handleH));
    select.setSelectNode(Feedback.SELECT_DOUBLE,
                         buildMultiRotate(true, rotateRB, rotateRT, handleH));
    select.setSelectNode(Feedback.SELECT_TRIPLE,
                         buildMultiLift(true, liftW, liftH));
    multiTarget.addEventTarget(select);

    affine = new AffineGroup(select);
    affine.getTranslation().initActuation(new Vector4d(
        0, height + margin, 0, 0));

    if (lift == null)
      multiTarget.addNode(affine);
    else
      lift.addNode(affine);

      // add multi tooltips, by selection
    select = new MultiSelect();

    select.setSelectNode(Feedback.SELECT_SINGLE,
                         buildMultiTooltip(false, calloutW, calloutH,
                                           dir + "CalloutSlide.gif"));
    select.setSelectNode(Feedback.SELECT_DOUBLE,
                         buildMultiTooltip(false, calloutW, calloutH,
                                           dir + "CalloutRotate.gif"));
    select.setSelectNode(Feedback.SELECT_TRIPLE,
                         buildMultiTooltip(false, calloutW, calloutH,
                                           dir + "CalloutLift.gif"));
    multiTarget.addEventTarget(select);

    affine = new AffineGroup(select);
    affine.getTranslation().initActuation(new Vector4d(
        0, height + handleH + calloutH / 2.0 + margin, 0, 0));

    if (lift == null)
      multiTarget.addNode(affine);
    else
      lift.addNode(affine);

    return multiTarget;
  }

}
