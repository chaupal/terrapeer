package terrapeer.vui.j3dui.utils.blocks;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.*;
import terrapeer.vui.helpers.*;
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
 first introduced for the book's examples on "second person
 manipulation".
 <P>
 Because these building blocks are rather specific in purpose
 they are included here as utilities instead of as general
 purpose building block classes in the framework core.
 @author Jon Barrilleaux,
 copyright (c) 1999-2000 Jon Barrilleaux,
 All Rights Reserved.
 */

public class SecondBlocks
{

  // public utilities =========================================

  /**
    Creates a set of MultiShape feedback decorations for a
    target object that is intended to be slid, rotated, and
    lifted over an X-Z floor plane.  The decorations include
    tooltips, handles, sound, outline, and skirt.  Visualization
    techniques are used for the decorations as is appropriate.
    Also computes the target bounds and centers the target with
    its origin at the bottom.  For the visualization to work use
    ChangePoster to post node changes (POST_NODE_EXTERNAL) with
    the returned object as the trigger.
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
    @param min Return copy of the centered target's bounding
    box minimum extent.  Never null.
    @param max Return copy of the centered target's bounding
    box maximum extent.  Never null.
    @param viewSpltr Source for view state changes.  Never null.
    @param world Host environment for computing the target
    bounds.  Never null.
    @param view Host environment for the sound audio device.
    If null then no sound.
    @return New MultiShape proxy target.
   */
  public static final MultiShape buildPerfectFloorTarget(
      Node target, AffineGroup slide, AffineGroup rotate,
      AffineGroup lift, Point3d min, Point3d max,
      ViewChangeSplitter viewSpltr, AppWorld world,
      AppView view)
  {

    PerfectOverlayGroup perfect;
    AffineGroup affine;
    MultiSelect select;
    double overlay;
    double size = .02;

    String dir = Blocks.buildResourcePath("images/");
    double handleH = .5;
    double slideR = 1;
    double rotateRT = .4;
    double rotateRB = .8;
    double liftW = 1;
    double liftH = .66;
    double calloutW = 3;
    double calloutH = .75;
    double margin = .05;

    // make target pickable and center it
    PickUtils.setGeoPickable(target, true);
    MultiShape centerTarget = ManipulateBlocks.
        centerFloorTarget(target, min, max, world);

    // build transform chain, connect feedback
    rotate.addNode(centerTarget);
    lift.addNode(rotate);
    slide.addNode(lift);

    MultiShape multiTarget = new MultiShape();
    multiTarget.addProxyTarget(slide, centerTarget);

    // add multi sounds
    if (view != null)
    {
      MultiShape sound = FeedbackBlocks.
          buildMultiSound(view);
      multiTarget.addProxyTarget(sound, sound);
    }

    // add normal outline
    MultiShape outline = ManipulateBlocks.
        buildMultiOutline(true, min, max, margin);
    rotate.addNode(outline);
    multiTarget.addEventTarget(outline);

    // add normal skirt
    MultiShape skirt = ManipulateBlocks.
        buildMultiSkirt(false, min, max, 100.0);
    rotate.addNode(skirt);
    multiTarget.addEventTarget(skirt);

    // add near perfect overlay slide and rotation handles
    overlay = 0.2;

    /// build handles by selection
    select = new MultiSelect();

    select.setSelectNode(Feedback.SELECT_SINGLE,
                         FeedbackBlocks.buildMultiSlide(true, slideR, handleH));
    select.setSelectNode(Feedback.SELECT_DOUBLE,
                         FeedbackBlocks.buildMultiRotate(true, rotateRB,
        rotateRT, handleH));

    multiTarget.addEventTarget(select);

    /// position above target
    affine = new AffineGroup(select);
    affine.getTranslation().initActuation(new Vector4d(
        0, margin, 0, 0));

    /// overlay handles
    perfect = new PerfectOverlayGroup(affine,
                                      Visualize.DO_OVER_SIZE, overlay, size);

    viewSpltr.addEventTarget(perfect);

    /// position at top of target
    affine = new AffineGroup(perfect);
    affine.getTranslation().initActuation(new Vector4d(
        0, max.y, 0, 0));

    /// connect to chain
    lift.addNode(affine);

    // add perfect overlay lift handle
    overlay = 0.2;

    /// build handles by selection
    select = new MultiSelect();

    select.setSelectNode(Feedback.SELECT_TRIPLE,
                         FeedbackBlocks.buildMultiLift(true, liftW, liftH));

    multiTarget.addEventTarget(select);

    /// position above target
    affine = new AffineGroup(select);
    affine.getTranslation().initActuation(new Vector4d(
        0, margin, 0, 0));

    /// overlay handle
    perfect = new PerfectOverlayGroup(affine,
                                      Visualize.DO_ALL, overlay, size);

    viewSpltr.addEventTarget(perfect);

    /// position at top of target
    affine = new AffineGroup(perfect);
    affine.getTranslation().initActuation(new Vector4d(
        0, max.y, 0, 0));

    /// connect to chain
    lift.addNode(affine);

    // add perfect overlay tooltips
    overlay = 0.15; // frontmost

    /// build tooltips by selection
    select = new MultiSelect();

    select.setSelectNode(Feedback.SELECT_SINGLE,
                         FeedbackBlocks.buildMultiTooltip(false, calloutW,
        calloutH, dir + "CalloutSlide.gif"));
    select.setSelectNode(Feedback.SELECT_DOUBLE,
                         FeedbackBlocks.buildMultiTooltip(false, calloutW,
        calloutH, dir + "CalloutRotate.gif"));
    select.setSelectNode(Feedback.SELECT_TRIPLE,
                         FeedbackBlocks.buildMultiTooltip(false, calloutW,
        calloutH, dir + "CalloutLift.gif"));
    multiTarget.addEventTarget(select);

    /// position above handles
    affine = new AffineGroup(select);
    affine.getTranslation().initActuation(new Vector4d(
        0, handleH + calloutH / 2.0 + margin, 0, 0));

    /// overlay tooltips
    perfect = new PerfectOverlayGroup(affine,
                                      Visualize.DO_ALL, overlay, size);

    viewSpltr.addEventTarget(perfect);

    /// position at top of target
    affine = new AffineGroup(perfect);
    affine.getTranslation().initActuation(new Vector4d(
        0, max.y, 0, 0));

    /// connect to chain
    lift.addNode(affine);

    return multiTarget;
  }

  /**
    Constructs a room with lighting, an X-Z floor plane, and a
    defined size for limiting the movement of its contents.  The
    room elements are pickable so that they can be used to
    deselect selected room objects.  The top surface of the
    floor is positioned such that ground level is at Y=0.  The
    floor is centered about the Y axis.
   */
  public static final class Room extends MultiShape
  {

    // public interface =====================================

    /**
       Constructs a Room.
       @param size Size of the room.  Never null.
     */
    public Room(Tuple3d size)
    {
      AffineGroup affine;
      _size.set(size);

      // build multi proxy floor
      _floor = new MultiShape();
      _floor.addNode(new BasePlane(vars.PLANE_DEFAULT, _size.x, _size.z));

      /// level floor
      affine = new AffineGroup(_floor);
      affine.getTranslation().initActuation(new Vector4d(0, (-vars.PLANE_THICKNESS/2.0)-0.02, 0, 0));
      affine.getAxisAngle().initActuation(new Vector4d(1, 0, 0, -Math.PI / 2.0));

      addNode(affine);

      // add light
      affine = new AffineGroup(new TestLight());
      affine.getTranslation().initActuation(new Vector4d(-_size.x / 2.0, _size.y, _size.z / 2.0, 0));

      addNode(affine);
    }

    /**
       Gets the selectable floor.
       @param Reference to the floor.
     */
    public MultiShape getFloor()
    {
      return _floor;
    }

    /**
       Gets the room size.
       @param Reference to the room size.
     */
    public Tuple3d getSize()
    {
      return _size;
    }

    // personal body ========================================

    /** Room size.  Never null. */
    private Vector3d _size = new Vector3d();

    /** Selectable floor.  Never null. */
    private MultiShape _floor;
  }

  /**
    Wraps a target object for sliding, rotating, and lifting it
    inside a room with an X-Z floor plane.  The wrapper
    includes feedback decorations implemented with
    visualization techniques, actuators that limit target
    manipulation to inside a room, and mappers for WRM sliding
    and direct rotation and lifting.  If the target is a
    multishape it will be connected for feedback.  The target
    will be centered for rotation and offset such that it sits
    on the floor.
   */
  public static final class FloorTarget extends MultiShape
  {

    // public interface =====================================

    /**
       Constructs a FloorTarget.
       @param target Target object to be decorated and
       manipulated.  Never null.
       @param room Room that the target is in.
       @param viewSpltr Source for view state changes.
       Never null.
       @param absDrag Source for absolute input drags.  Used
       for target WRM sliding.  Never null.
       @param relDrag Source for relative input drags.  Used
       for target direct rotating and lifting.  Never null.
       @param groupManager Feedback group manager managing the
       target object.  Never null.
       @param world The world that the target belongs to.
       Never null.
       @param view Host environment for the sound audio device.
       If null then no sound.
     */
    public FloorTarget(Node target, Room room,
                       ViewChangeSplitter viewSpltr, InputDragSplitter
                       absDrag, InputDragSplitter relDrag,
                       FeedbackGroupManager groupMgr, AppWorld world,
                       AppView view)
    {

      // build floor target chain and decorations
      // (proxyTarget also serves as the poster trigger)
      Point3d min = new Point3d();
      Point3d max = new Point3d();

      MultiShape proxyTarget = SecondBlocks.
          buildPerfectFloorTarget(target, _slide, _rotate,
                                  _lift, min, max, viewSpltr, world, view);

      addProxyTarget(proxyTarget, proxyTarget);

      // setup manipulation actuators
      /// limit slide relative to room and bounds
      _slide.getTranslation().getPlugin().setTargetClamp(
          Mapper.DIM_X, new Vector2d(
          -room.getSize().x / 2.0 - min.x,
          room.getSize().x / 2.0 - max.x));
      _slide.getTranslation().getPlugin().setTargetClamp(
          Mapper.DIM_Z, new Vector2d(
          -room.getSize().z / 2.0 - min.z,
          room.getSize().z / 2.0 - max.z));

      _slide.getTranslation().getPlugin().setTargetClamp(
          Mapper.DIM_Y, new Vector2d(0, 0));

      /// limit lift relative to room and bounds
      _lift.getTranslation().getPlugin().setTargetClamp(
          Mapper.DIM_Y, new Vector2d(
          0, room.getSize().y - max.y));

      _lift.getTranslation().getPlugin().setTargetClamp(
          Mapper.DIM_X | Mapper.DIM_X, new Vector2d(0, 0));

      /// set rotation axis
      _rotate.getAxisAngle().getPlugin().setSourceOffset(
          new Vector4d(0, 1, 0, 0));

      // build manipulation mappers, use enablers for
      // internal control
      InputDragTarget mapper;
      Vector2d rotateScale = new Vector2d(.025, .025);
      Vector2d liftScale = new Vector2d(.025, .025);

      /// WRM slide
      mapper = IntuitiveBlocks.
          buildStickyWrmTranslationMapper(room.getFloor(),
                                          _slide, world.getSceneRoot(), true, true);

      EnableInputDragFilter slideEnabler =
          new EnableInputDragFilter(mapper);
      absDrag.addEventTarget(slideEnabler);

      /// direct rotate
      mapper = ActuationBlocks.buildDirectMapper(
          _rotate.getAxisAngle(), rotateScale,
          Mapper.DIM_W, Mapper.DIM_NONE, true);

      EnableInputDragFilter rotateEnabler =
          new EnableInputDragFilter(mapper);
      relDrag.addEventTarget(rotateEnabler);

      /// direct lift
      mapper = ActuationBlocks.buildDirectMapper(
          _lift.getTranslation(), liftScale,
          Mapper.DIM_NONE, Mapper.DIM_Y, true);

      EnableInputDragFilter liftEnabler =
          new EnableInputDragFilter(mapper);
      relDrag.addEventTarget(liftEnabler);

      // setup target feedback and enable
      StatusTrigger trigger = FeedbackBlocks.
          buildTargetFeedbackSupport(this,
                                     groupMgr, relDrag, view.getDisplay(),
                                     world.getSceneRoot());

      FeedbackBlocks.buildTargetSelectEnablers(this,
                                               trigger, slideEnabler,
                                               rotateEnabler, liftEnabler);
    }

    /**
       Gets the target manipulation slide actuator.
       @return Reference to the slider.
     */
    public Actuator getSlide()
    {
      return _slide.getTranslation();
    }

    /**
       Gets the target manipulation rotate actuator.
       @return Reference to the slider.
     */
    public Actuator getRotate()
    {
      return _rotate.getAxisAngle();
    }

    /**
       Gets the target manipulation lift actuator.
       @return Reference to the slider.
     */
    public Actuator getLift()
    {
      return _lift.getTranslation();
    }

    // personal body ========================================

    /** Target slider.  Never null. */
    private AffineGroup _slide = new AffineGroup();

    /** Target rotator.  Never null. */
    private AffineGroup _rotate = new AffineGroup();

    /** Target lifter.  Never null. */
    private AffineGroup _lift = new AffineGroup();
  }

}
