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
import terrapeer.vui.j3dui.visualize.*;
import terrapeer.vui.j3dui.visualize.change.*;
import terrapeer.vui.j3dui.manipulate.*;

/**
 These utilities are common building block utilities that were
 first introduced for the book's examples on "third person
 manipulation".
 <P>
 Because these building blocks are rather specific in purpose
 they are included here as utilities instead of as general
 purpose building block classes in the framework core.
 @author Jon Barrilleaux,
 copyright (c) 1999-2000 Jon Barrilleaux,
 All Rights Reserved.
 */

public class ThirdBlocks
{

  // public utilities =========================================

  /**
    Creates a MultiShape control "mark" for a third person
    control.
    @param pickable If true the mark will be pickable.
    @param rectW Width of the mark rectangle.
    @param rectH Height of the mark rectangle.
    @return New MultiShape object containing the mark.
   */
  public static final MultiShape buildMultiMark(
      boolean pickable, double rectW, double rectH)
  {

    // build states
    MultiStatus status = new MultiStatus();
    MultiAction action;
    String dir = Blocks.buildResourcePath("images/");

    /// disabled
    action = new MultiAction();
    status.setStatusNode(Feedback.STATUS_DISABLE, action);

    action.setActionNode(Feedback.ACTION_ALL,
                         new TextureRect(pickable, null, 0,
                                         dir + "DisableMark.gif",
                                         Elements.SIDE_TOP,
                                         rectW, rectH));

    /// enabled
    action = new MultiAction();
    status.setStatusNode(Feedback.STATUS_NORMAL, action);

    action.setActionNode(Feedback.ACTION_NORMAL,
                         new TextureRect(pickable, null, 0,
                                         dir + "NormalMark.gif",
                                         Elements.SIDE_TOP,
                                         rectW, rectH));
    action.setActionNode(Feedback.ACTION_IS_OVER &
                         ~Feedback.ACTION_DRAG,
                         new TextureRect(pickable, null, 0,
                                         dir + "OverMark.gif",
                                         Elements.SIDE_TOP,
                                         rectW, rectH));
    action.setActionNode(Feedback.ACTION_DRAG,
                         new TextureRect(pickable, null, 0,
                                         dir + "DragMark.gif",
                                         Elements.SIDE_TOP,
                                         rectW, rectH));

    // make it visable by default
    status.setFeedbackStatus(Feedback.STATUS_NORMAL);
    status.setFeedbackAction(Feedback.ACTION_NORMAL);

    return status;
  }

  /**
    Creates a MultiShape "back" for a third person rotate
    control.
    @param pickable If true the back will be pickable.
    @param rectW Width of the back rectangle.
    @param rectH Height of the back rectangle.
    @return New MultiShape object containing the back.
   */
  public static final MultiShape buildMultiRotateBack(
      boolean pickable, double rectW, double rectH)
  {

    // build states
    MultiStatus status = new MultiStatus();
    MultiAction action;
    String dir = Blocks.buildResourcePath("images/");

    /// disabled
    action = new MultiAction();
    status.setStatusNode(Feedback.STATUS_DISABLE, action);

    action.setActionNode(Feedback.ACTION_ALL,
                         new TextureRect(pickable, null, 0,
                                         dir + "DisableRotateBack.gif",
                                         Elements.SIDE_TOP,
                                         rectW, rectH));

    /// enabled
    action = new MultiAction();
    status.setStatusNode(Feedback.STATUS_NORMAL, action);

    action.setActionNode(Feedback.ACTION_NORMAL,
                         new TextureRect(pickable, null, 0,
                                         dir + "NormalRotateBack.gif",
                                         Elements.SIDE_TOP,
                                         rectW, rectH));
    action.setActionNode(Feedback.ACTION_IS_OVER,
                         new TextureRect(pickable, null, 0,
                                         dir + "OverRotateBack.gif",
                                         Elements.SIDE_TOP,
                                         rectW, rectH));

    // make it visable by default
    status.setFeedbackStatus(Feedback.STATUS_NORMAL);
    status.setFeedbackAction(Feedback.ACTION_NORMAL);

    return status;
  }

  /**
    Creates a MultiShape "back" for a third person lift
    control.
    @param pickable If true the back will be pickable.
    @param rectW Width of the back rectangle.
    @param rectH Height of the back rectangle.
    @return New MultiShape object containing the back.
   */
  public static final MultiShape buildMultiLiftBack(
      boolean pickable, double rectW, double rectH)
  {

    // build states
    MultiStatus status = new MultiStatus();
    MultiAction action;
    String dir = Blocks.buildResourcePath("images/");

    /// disabled
    action = new MultiAction();
    status.setStatusNode(Feedback.STATUS_DISABLE, action);

    action.setActionNode(Feedback.ACTION_ALL,
                         new TextureRect(pickable, null, 0,
                                         dir + "DisableLiftBack.gif",
                                         Elements.SIDE_TOP,
                                         rectW, rectH));

    /// enabled
    action = new MultiAction();
    status.setStatusNode(Feedback.STATUS_NORMAL, action);

    action.setActionNode(Feedback.ACTION_NORMAL,
                         new TextureRect(pickable, null, 0,
                                         dir + "NormalLiftBack.gif",
                                         Elements.SIDE_TOP,
                                         rectW, rectH));
    action.setActionNode(Feedback.ACTION_IS_OVER,
                         new TextureRect(pickable, null, 0,
                                         dir + "OverLiftBack.gif",
                                         Elements.SIDE_TOP,
                                         rectW, rectH));

    // make it visable by default
    status.setFeedbackStatus(Feedback.STATUS_NORMAL);
    status.setFeedbackAction(Feedback.ACTION_NORMAL);

    return status;
  }

  /**
    Creates a set of sound effects for a third person control.
    @param view Host environment for the sound audio device.
    @return New MultiShape object containing the decoration.
   */
  public static final MultiShape buildControlSound(
      AppView view)
  {

    String dir = Blocks.buildResourcePath("sound/");
    FeedbackEnableTrigger enabler;
    SoundEffect sound;
    MultiShape multi = new MultiShape();

    // select over sound
    sound = new SoundEffect(dir + "SelectOverSound.wav",
                            0.3, view.getView().getPhysicalEnvironment());
    enabler = new FeedbackEnableTrigger(sound);
    enabler.setStatusFlags(Feedback.STATUS_NORMAL);
    enabler.setSelectFlags(Feedback.SELECT_ALL);
    enabler.setActionFlags(Feedback.ACTION_IS_OVER);
    multi.addProxyTarget(sound, enabler);

    // select drag sound
    sound = new SoundEffect(dir + "SelectDragSound.wav",
                            0.3, view.getView().getPhysicalEnvironment());
    enabler = new FeedbackEnableTrigger(sound);
    enabler.setStatusFlags(Feedback.STATUS_NORMAL);
    enabler.setSelectFlags(Feedback.SELECT_ALL);
    enabler.setActionFlags(Feedback.ACTION_DRAG);
    multi.addProxyTarget(sound, enabler);

    return multi;
  }

  /**
    Interconnects the actuation of a rotate control with that
    of a floor target.  The control is connected such that the
    target is only affected when it is double-selected for
    rotation and the mouse is over the control.
    @param control Rotate control.  Never null.
    @param target Floor target.  Never null.
    @return Actuation splitter connected to the target and used
    as an enabler.
   */
  public static final ActuationSplitter connectRotateControl(
      ThirdBlocks.RotateControl control,
      SecondBlocks.FloorTarget target)
  {

    // connect target actuation to control input
    target.getRotate().getActuationSource().
        addEventTarget(control.getActuationSplitter());

    // build actuation enabler and connect to control
    ActuationSplitter enabler = new ActuationSplitter();
    enabler.setEnable(false);
    enabler.addEventTarget(target.getRotate());
    control.getActuationSplitter().addEventTarget(enabler);

    // build trigger, enable on double-select and over
    FeedbackEnableTrigger trigger =
        new FeedbackEnableTrigger(enabler);
    trigger.setSelectFlags(Feedback.SELECT_DOUBLE);
    trigger.setActionFlags(Feedback.ACTION_IS_OVER);

    // connect target select and control action to trigger
    FeedbackTrigger filter;

    filter = new FeedbackTrigger(trigger);
    filter.setStatusFlags(Feedback.STATUS_IGNORE);
    filter.setActionFlags(Feedback.ACTION_IGNORE);
    target.addEventTarget(filter);

    filter = new FeedbackTrigger(trigger);
    filter.setStatusFlags(Feedback.STATUS_IGNORE);
    filter.setSelectFlags(Feedback.SELECT_IGNORE);
    control.getFeedbackSource().addEventTarget(filter);

    return enabler;
  }

  /**
    Interconnects the actuation of a lift control with that
    of a floor target.  The control is connected such that the
    target is only affected when it is triple-selected for
    lift and the mouse is over the control.
    @param control Lift control.  Never null.
    @param target Floor target.  Never null.
    @return Actuation splitter connected to the target and used
    as an enabler.
   */
  public static final ActuationSplitter connectLiftControl(
      ThirdBlocks.LiftControl control,
      SecondBlocks.FloorTarget target)
  {

    // connect target actuation to control input
    target.getLift().getActuationSource().
        addEventTarget(control.getActuationSplitter());

    // build actuation enabler and connect to control
    ActuationSplitter enabler = new ActuationSplitter();
    enabler.setEnable(false);
    enabler.addEventTarget(target.getLift());
    control.getActuationSplitter().addEventTarget(enabler);

    // build trigger, enable on double-select and over
    FeedbackEnableTrigger trigger =
        new FeedbackEnableTrigger(enabler);
    trigger.setSelectFlags(Feedback.SELECT_TRIPLE);
    trigger.setActionFlags(Feedback.ACTION_IS_OVER);

    // connect target select and control action to trigger
    FeedbackTrigger filter;

    filter = new FeedbackTrigger(trigger);
    filter.setStatusFlags(Feedback.STATUS_IGNORE);
    filter.setActionFlags(Feedback.ACTION_IGNORE);
    target.addEventTarget(filter);

    filter = new FeedbackTrigger(trigger);
    filter.setStatusFlags(Feedback.STATUS_IGNORE);
    filter.setSelectFlags(Feedback.SELECT_IGNORE);
    control.getFeedbackSource().addEventTarget(filter);

    return enabler;
  }

  /**
    Constructs a base control with feedback and actuation input
    and output allowing it to be used as both a drag control
    source and as a control indicator target.
    <P>
    The control is in display overlay and consists of a
    stationary "back" and "frame", and a movable "mark".  The
    mark moves relative to the mark and back center points in
    response to internal or external actuation angle. The mark
    is frontmost and the frame is backmost.
    <P>
    Making a control element pickable, such as with
    PickUtils.setGeoPickable(), allows the element to serve as
    a drag source for internal control actuation.  The internal
    drag sensor uses the first button with no modifiers, and
    ignores arrow key input.
    <P>
    The input enable event interface controls the status
    feedback of the control as well as its internal and external
    actuation inputs.
   */
  public static abstract class BaseControl implements EnableTarget
  {

    // public interface =====================================

    /**
       Constructs a BaseControl.
       @param mark Control mark element.  Null if none.
       @param back Control back element.  Null if none.
       @param frame Control frame element.  Null if none.
       @param dispOrigin Position of the control origin
       relative to the display window expressed as a fraction
       of the window size.
       @param bodyOrigin Position of the control elements
       (body) relative to the control origin in pixels.
       @param markCenter Position of mark center relative to
       mark shape in pixels.
       @param backCenter Position of back center relative to
       back shape and control in pixels.
       @param markLimits Min/max limits on the actuation range
       of the mark as defined by the specific control type.
       Ignored if max limit less than min limit.
       @param actScale Scaling factor applied to the actuation
       output, and the inverse value applied to actuation input.
       @param viewSpltr Source for view change events.
       @param view Host view for the control.
     */
    public BaseControl(Node mark, Node back,
                       Node frame, Vector2d dispOrigin, Vector2d bodyOffset,
                       Vector2d markCenter, Vector2d backCenter,
                       Vector2d markLimits, double actScale,
                       ViewChangeSplitter viewSpltr, AppView view)
    {

      _backCenter.set(backCenter);

      // build display overlay
      DisplayOverlayRoot root =
          new DisplayOverlayRoot(view);
      viewSpltr.addEventTarget(root);

      _disp = new DisplayOverlayGroup(root);
      viewSpltr.addEventTarget(_disp);

      _disp.setRelativeOrigin(dispOrigin);
      _disp.setAbsoluteOffset(bodyOffset);

      // build mark group
      Node markRoot = buildMarkGroup(mark, markCenter,
                                     backCenter);

      // build internal actuation enabler
      ActuationSplitter enabler =
          new ActuationSplitter();
      enabler.addEventTarget(_actSpltr);
      enabler.setEnable(false);

      // build the mark dragger
      InputDragSplitter drag = buildMarkDragger(
          view, enabler, markLimits, actScale, _centerPlug);

      // add elements to display by plane
      _disp.addNode(markRoot, -3);
      _disp.addNode(back, -4);
      _disp.addNode(frame, -5);

      // connect elements for feedback
      FeedbackSplitter fbInternal = new FeedbackSplitter();

      /// add as feedback targets
      if (mark instanceof MultiShape)
      {
        fbInternal.addEventTarget((MultiShape)mark);
        _fbTarget.addEventTarget((MultiShape)mark);
        ((MultiShape)mark).addEventTarget(_fbSource);
      }
      if (back instanceof MultiShape)
      {
        fbInternal.addEventTarget((MultiShape)back);
        _fbTarget.addEventTarget((MultiShape)back);
        ((MultiShape)back).addEventTarget(_fbSource);
      }
      if (frame instanceof MultiShape)
      {
        fbInternal.addEventTarget((MultiShape)frame);
        _fbTarget.addEventTarget((MultiShape)frame);
        ((MultiShape)frame).addEventTarget(_fbSource);
      }

      /// build action feedback sensors and trigger
      ActionTrigger trigger = new ActionTrigger(
          fbInternal, _disp);
      FeedbackBlocks.buildActionTriggerSupport(
          trigger, drag, view.getDisplay(), root);

      trigger.getOverTargetSource().addEventTarget(
          enabler);

      /// add sound, connect as node and for feedback
      MultiShape sound = ThirdBlocks.buildControlSound(view);

      //root.addNode(sound);
      _fbSource.addEventTarget(sound);

      // drag center update (after disp overlay update)
      viewSpltr.addEventTarget(new ViewChangeTarget()
      {
        public void setViewExternal(View source,
                                    Point3d pos, Transform3D xform)
        {}

        public void setViewInternal(View source,
                                    double fov, double vsf, double dsf,
                                    Vector2d
                                    dvo, Vector2d ds, Vector2d ss, Vector2d sr)
        {
          // compute absolute drag center
          Vector2d center = new Vector2d();

          /// compensate for mouse to display offset
          center.x = ds.x / 2.0;
          center.y = -ds.y / 2.0;

          /// add control origin and offset
          Vector2d origin = new Vector2d();
          _disp.getAbsoluteOrigin(origin);
          center.add(origin);

          /// add control element offset
          Vector2d offset = new Vector2d();
          _disp.getAbsoluteOffset(offset);
          center.add(offset);

          /// add back center
          center.add(_backCenter);

          _centerPlug.setOrigin(center);
        }
      });

      // initialize feedback (last)
      setEnable(false);
    }

    /**
       Gets the control actuation splitter.  Use it as an event
       target for external input to the control, and as an
       event source for external output from the control.
       @param Reference to the actuation splitter.  The
       actuation value components are defined by the subclass.
     */
    public ActuationSplitter getActuationSplitter()
    {
      return _actSpltr;
    }

    /**
       Gets the control feedback event source.  Use it as an
       event source for external output from the control.
       @param Reference to the feedback splitter.
     */
    public FeedbackSplitter getFeedbackSource()
    {
      return _fbSource;
    }

    /**
       Gets the control feedback event target.  Use it as an
       event target for external input to the control.
       @param Reference to the feedback target.
     */
    public FeedbackTarget getFeedbackTarget()
    {
      return _fbTarget;
    }

    // EnableTarget implementation

    public void setEnable(boolean enable)
    {

      _actSpltr.setEnable(enable);

      if (enable)
      {
        _fbTarget.setFeedbackStatus(
            Feedback.STATUS_NORMAL);
      }
      else
      {
        _fbTarget.setFeedbackStatus(
            Feedback.STATUS_DISABLE);
      }
    }

    // personal body ========================================

    /** Center position of back element.  Never null. */
    private Vector2d _backCenter = new Vector2d();

    /** Overlay group for control elements.  Never null. */
    private DisplayOverlayGroup _disp;

    /** Plugin for control drag center.  Never null. */
    private AbsoluteInputDragPlugin _centerPlug =
        new AbsoluteInputDragPlugin();

    /** Splitter for control actuation.  Never null. */
    private ActuationSplitter _actSpltr =
        new ActuationSplitter();

    /** Elements as a feedback target.  Never null. */
    FeedbackSplitter _fbTarget = new FeedbackSplitter();

    /** Elements as a feedback source.  Never null. */
    FeedbackSplitter _fbSource = new FeedbackSplitter();

    /**
       Builds the mark group, which centers the mark,
       actuates it, and connects its actuator to the actuation
       splitter.  Returns the root node of the mark group.
     */
    protected abstract Node buildMarkGroup(Node mark,
                                           Vector2d markCenter,
                                           Vector2d backCenter);

    /**
       Builds the mark dragger, which includes a drag sensor
       for the specified view, a gesture drag filter using the
       specified plugin for its origin, and a drag mapper.
       Connects the mapper to the specified actuation enabler,
       configures the mark actuator with the specified limits,
       and returns a splitter for the dragger.
     */
    protected abstract InputDragSplitter buildMarkDragger(
        AppView view, ActuationTarget enabler,
        Vector2d markLimits, double actScale,
        AbsoluteInputDragPlugin plugin);
  }

  /**
    Constructs a third person "rotate" control.  The mark moves
    in a circle relative to the mark and back center points in
    response to internal or external actuation angle.
    <P>
    The input/output actuation value is defined as follows:
    <UL>
    <LI>X - (ignored)
    <LI>Y - (ignored)
    <LI>Z - (ignored)
    <LI>W - Rotation angle value in radians.
    </UL>
   */
  public static final class RotateControl extends
      ThirdBlocks.BaseControl
  {

    // public interface =====================================

    /**
       Constructs a RotateControl.
       @param mark Control mark element.  Null if none.
       @param back Control back element.  Null if none.
       @param frame Control frame element.  Null if none.
       @param dispOrigin Position of the control origin
       relative to the display window expressed as a fraction
       of the window size.
       @param bodyOrigin Position of the control elements
       (body) relative to the control origin in pixels.
       @param markCenter Position of mark center relative to
       mark shape in pixels.
       @param backCenter Position of back center relative to
       back shape and control in pixels.
       @param markLimits Limits on the actuation range of
       the mark in radians.  Ignored if max limit less than
       min limit.
       @param actScale Scaling factor applied to the actuation
       output, and the inverse value applied to actuation input.
       @param viewSpltr Source for view change events.
       @param view Host view for the control.
     */
    public RotateControl(Node mark, Node back, Node frame,
                         Vector2d dispOrigin, Vector2d bodyOffset,
                         Vector2d markCenter, Vector2d backCenter,
                         Vector2d markLimits, double actScale,
                         ViewChangeSplitter viewSpltr, AppView view)
    {

      super(mark, back, frame, dispOrigin, bodyOffset,
            markCenter, backCenter, markLimits, actScale,
            viewSpltr, view);
    }

    // BaseControl implementation

    protected Node buildMarkGroup(Node mark,
                                  Vector2d markCenter, Vector2d backCenter)
    {

      /// center mark at mark center
      _markRotate = new CenterAffineGroup(mark);
      _markRotate.getTranslation().initActuation(
          new Vector4d( -markCenter.x, -markCenter.y, 0, 0));
      _markRotate.getCenter().initActuation(
          new Vector4d(markCenter.x, markCenter.y, 0, 0));

      /// connect external actuation splitter
      getActuationSplitter().addEventTarget(
          _markRotate.getAxisAngle());

      /// center mark at back center
      AffineGroup markRoot = new AffineGroup(_markRotate);
      markRoot.getTranslation().initActuation(
          new Vector4d(backCenter.x, backCenter.y, 0, 0));

      return markRoot;
    }

    protected InputDragSplitter buildMarkDragger(
        AppView view, ActuationTarget enabler,
        Vector2d markLimits, double actScale,
        AbsoluteInputDragPlugin plugin)
    {

      InputDragSplitter drag = new InputDragSplitter();

      // build drag mapper
      InputDragTarget mapper = ActuationBlocks.
                               buildDirectMapper(enabler, new Vector2d(1, 1),
                                                 Mapper.DIM_W, Mapper.DIM_NONE, true);

      // configure mark actuator
      _markRotate.getAxisAngle().getPlugin().
          setSourceOffset(new Vector4d(0, 0, 1, 0));

      /// set mark limits
      if (markLimits.x <= markLimits.y)
      {
        _markRotate.getAxisAngle().getPlugin().
            setTargetClamp(Mapper.DIM_W, markLimits);

        /// set inverse actuation scale
      }
      _markRotate.getAxisAngle().getPlugin().
          setSourceScale(new Vector4d(
          0, 0, 0, 1.0 / actScale));

      // build drag filters
      InputDragTarget filter;

      /// relative drag
      filter = new InputDragFilter(mapper,
                                   new RelativeInputDragPlugin());

      /// set scale
      filter = new InputDragFilter(filter,
                                   new ScaleInputDragPlugin(new Vector2d(
          actScale, 1)));

      /// absolute circle drag
      filter = new InputDragFilter(filter,
                                   new CircleInputDragPlugin());
      filter = new InputDragFilter(filter, plugin);

      drag.addEventTarget(filter);

      // absolute drag, first button, no keyboard
      ActuationBlocks.buildAbsoluteDragger(drag,
                                           view, Input.BUTTON_FIRST,
                                           Input.MODIFIER_NONE,
                                           Input.MODIFIER_IGNORE);

      return drag;
    }

    // personal body ========================================

    /** Mark rotator.  Never null. */
    private CenterAffineGroup _markRotate;
  }

  /**
    Constructs a third person "lift" control.  The mark moves
    in a vertical line relative to the mark and back center
    points in response to internal or external actuation Y
    position.
    <P>
    The input/output actuation value is defined as follows:
    <UL>
    <LI>X - (ignored)
    <LI>Y - Y axis translation value.
    <LI>Z - (ignored)
    <LI>W - (ignored)
    </UL>
   */
  public static final class LiftControl extends
      ThirdBlocks.BaseControl
  {

    // public interface =====================================

    /**
       Constructs a LiftControl.
       @param mark Control mark element.  Null if none.
       @param back Control back element.  Null if none.
       @param frame Control frame element.  Null if none.
       @param dispOrigin Position of the control origin
       relative to the display window expressed as a fraction
       of the window size.
       @param bodyOrigin Position of the control elements
       (body) relative to the control origin in pixels.
       @param markCenter Position of mark center relative to
       mark shape in pixels.
       @param backCenter Position of back center relative to
       back shape and control in pixels.
       @param markLimits Limits on the actuation range of
       the mark in pixels.  Ignored if max limit less than
       min limit.
       @param actScale Scaling factor applied to the actuation
       output, and the inverse value applied to actuation input.
       @param viewSpltr Source for view change events.
       @param view Host view for the control.
     */
    public LiftControl(Node mark, Node back, Node frame,
                       Vector2d dispOrigin, Vector2d bodyOffset,
                       Vector2d markCenter, Vector2d backCenter,
                       Vector2d markLimits, double actScale,
                       ViewChangeSplitter viewSpltr, AppView view)
    {

      super(mark, back, frame, dispOrigin, bodyOffset,
            markCenter, backCenter, markLimits, actScale,
            viewSpltr, view);
    }

    // BaseControl implementation

    protected Node buildMarkGroup(Node mark,
                                  Vector2d markCenter, Vector2d backCenter)
    {

      // center mark at mark center
      AffineGroup affine = new AffineGroup(mark);
      affine.getTranslation().initActuation(
          new Vector4d( -markCenter.x, -markCenter.y, 0, 0));

      // connect external actuation splitter
      _markLift = new AffineGroup(affine);

      getActuationSplitter().addEventTarget(
          _markLift.getTranslation());

      /// center mark at back center
      AffineGroup markRoot = new AffineGroup(_markLift);
      markRoot.getTranslation().initActuation(
          new Vector4d(backCenter.x, backCenter.y, 0, 0));

      return markRoot;
    }

    protected InputDragSplitter buildMarkDragger(
        AppView view, ActuationTarget enabler,
        Vector2d markLimits, double actScale,
        AbsoluteInputDragPlugin plugin)
    {

      InputDragSplitter drag = new InputDragSplitter();

      // build drag mapper
      InputDragTarget mapper = ActuationBlocks.
                               buildDirectMapper(enabler, new Vector2d(1, 1),
                                                 Mapper.DIM_NONE, Mapper.DIM_Y, true);

      // configure mark actuator
      /// set mark limits
      _markLift.getTranslation().getPlugin().
          setTargetClamp(Mapper.DIM_X, new Vector2d(0, 0));

      if (markLimits.x <= markLimits.y)
      {
        _markLift.getTranslation().getPlugin().
            setTargetClamp(Mapper.DIM_Y, markLimits);

        /// set inverse actuation scale
      }
      _markLift.getTranslation().getPlugin().
          setSourceScale(new Vector4d(
          0, 1.0 / actScale, 0, 0));

      // build drag filters
      InputDragTarget filter;

      /// relative drag
      filter = new InputDragFilter(mapper,
                                   new RelativeInputDragPlugin());

      /// set scale
      filter = new InputDragFilter(filter,
                                   new ScaleInputDragPlugin(new Vector2d(
          0, actScale)));

      /// absolute linear drag
      filter = new InputDragFilter(filter, plugin);

      drag.addEventTarget(filter);

      // relative drag, first button, no keyboard
      ActuationBlocks.buildAbsoluteDragger(drag,
                                           view, Input.BUTTON_FIRST,
                                           Input.MODIFIER_NONE,
                                           Input.MODIFIER_IGNORE);

      return drag;
    }

    // personal body ========================================

    /** Mark lifter.  Never null. */
    private AffineGroup _markLift;
  }

}
