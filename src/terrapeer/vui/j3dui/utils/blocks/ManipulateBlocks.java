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
import terrapeer.vui.j3dui.manipulate.*;

/**
 These utilities are common building block utilities that were
 first introduced for the book's examples on "manipulation".
 <P>
 Because these building blocks are rather specific in purpose
 they are included here as utilities instead of as general
 purpose building block classes in the framework core.
 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */

public class ManipulateBlocks
{

  // public utilities =========================================

  /**
    Computes the target bounds, and centers the target
    horizontally and offsets it vertically so its origin is
    at the bottom.
    @param target Target object to be centered and offset.  If
    the target is a FeedbackTarget it will also be connected as
    a feedback target.
    @param min Return copy of the centered target's bounding
    box minimum extent.  Never null.
    @param max Return copy of the centered target's bounding
    box maximum extent.  Never null.
    @param world Host environment for computing the target
    bounds.  Never null.
    @return New MultiShape proxy target.
   */
  public static final MultiShape centerFloorTarget(
      Node target, Point3d min, Point3d max, AppWorld world)
  {

    // build centered target (before connecting it)
    MultiShape multiTarget = new MultiShape();

    /// get target bounds (before connecting target)
    BoundingBox bbox = BoundsUtils.getBoundingBox(
        target, world, new BoundingBox());

    bbox.getLower(min);
    bbox.getUpper(max);

    /// center and offset target
    Vector3d offset = new Vector3d(
        - ( (max.x - min.x) / 2.0 + min.x),
        -min.y,
        - ( (max.z - min.z) / 2.0 + min.z));

    AffineGroup center = new AffineGroup(target);
    center.getTranslation().initActuation(new Vector4d(
        offset.x, offset.y, offset.z, 0));

    /// center and offset bounds
    min.add(offset);
    max.add(offset);

    // connect target		
    multiTarget.addNode(center);
    if (target instanceof FeedbackTarget)
      multiTarget.addEventTarget( (FeedbackTarget) target);

    return multiTarget;
  }

  /**
    Creates a MultiShape outline for a target.
    @param pickable If true the outline will be pickable.
    @param min  The minimum (bottom) extent of the target.
    @param max  The maximum (top) extent of the target.
    @param margin The distance between the target extent and the
    outline itself.
    @return New MultiShape object containing the decorations.
   */
  public static final MultiShape buildMultiOutline(
      boolean pickable, Tuple3d min, Tuple3d max, double margin)
  {

    // build states
    MultiSelect select = new MultiSelect();
    MultiAction action;

    /// not selected
    action = new MultiAction();
    select.setSelectNode(Feedback.SELECT_NORMAL, action);

    action.setActionNode(Feedback.ACTION_IS_OVER &
                         ~Feedback.ACTION_DRAG,
                         new BoxOutline(pickable, BoxOutline.OUTLINE_DRAG,
                                        -1, null, min, max, margin));

    /// selected
    action = new MultiAction();
    select.setSelectNode(Feedback.SELECT_IS_SELECT, action);

    action.setActionNode(Feedback.ACTION_NORMAL,
                         new BoxOutline(pickable, BoxOutline.OUTLINE_NORMAL,
                                        -1, null, min, max, margin));
    action.setActionNode(Feedback.ACTION_IS_OVER &
                         ~Feedback.ACTION_DRAG,
                         new BoxOutline(pickable, BoxOutline.OUTLINE_OVER,
                                        -1, null, min, max, margin));

    // make it visable by default				
    select.setFeedbackSelect(Feedback.SELECT_NORMAL);
    select.setFeedbackAction(Feedback.ACTION_OVER);

    return select;
  }

  /**
    Creates a MultiShape skirt for a target.
    @param pickable If true the skirt will be pickable.
    @param min  The minimum (bottom) extent of the target.
    @param max  The maximum (top) extent of the target.
    @param length The distance between the target extent and the
    far end of the skirt.
    @return New MultiShape object containing the decorations.
   */
  public static final MultiShape buildMultiSkirt(
      boolean pickable, Tuple3d min, Tuple3d max, double length)
  {

    // build states
    MultiSelect select = new MultiSelect();
    MultiAction action;

    /// not selected
    action = new MultiAction();
    select.setSelectNode(Feedback.SELECT_NORMAL, action);

    action.setActionNode(Feedback.ACTION_IS_OVER &
                         ~Feedback.ACTION_DRAG,
                         new BoxSkirt(pickable, BoxSkirt.SKIRT_DRAG,
                                      .5, null, Elements.SIDE_TOP, min, max,
                                      length));

    /// selected
    action = new MultiAction();
    select.setSelectNode(Feedback.SELECT_IS_SELECT, action);

    action.setActionNode(Feedback.ACTION_IS_OVER &
                         ~Feedback.ACTION_DRAG,
                         new BoxSkirt(pickable, BoxSkirt.SKIRT_OVER,
                                      .5, null, Elements.SIDE_TOP, min, max,
                                      length));
    action.setActionNode(Feedback.ACTION_DRAG,
                         new BoxSkirt(pickable, BoxSkirt.SKIRT_DRAG,
                                      .5, null, Elements.SIDE_TOP, min, max,
                                      length));

    // make it visable by default				
    select.setFeedbackSelect(Feedback.SELECT_NORMAL);
    select.setFeedbackAction(Feedback.ACTION_OVER);

    return select;
  }

  /**
    Creates a set of passive feeler feedback decorations for a
    target object that is intended to be slid, rotated, and
    lifted over an X-Z floor plane.  The decorations include
    an outline and a skirt.  Automatically centers the object
    and offsets it vertically so its origin is at the bottom.
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
    @param world Host environment for computing the target
    bounds.  Never null.
    @param view Host environment for the sound audio device.
    If null then no sound.
    @return New MultiShape proxy target.
   */
  public static final MultiShape buildFeelerFloorTarget(
      Node target, AffineGroup slide, AffineGroup rotate,
      AffineGroup lift, AppWorld world,
      AppView view)
  {

    MultiShape multiTarget = new MultiShape();
    AffineGroup affine;
    double margin = .05;

    // center target (before connecting it)
    Point3d min = new Point3d();
    Point3d max = new Point3d();

    MultiShape center = centerFloorTarget(target,
                                          min, max, world);

    // build basic feedback decorations
    MultiShape decor = FeedbackBlocks.
        buildMultiFloorTarget(center, slide,
                              rotate, lift, view, max.y);
    multiTarget.addProxyTarget(decor, decor);

    // add outline
    MultiShape outline = ManipulateBlocks.
        buildMultiOutline(true, min, max, margin);
    multiTarget.addEventTarget(outline);

    if (rotate == null)
      multiTarget.addNode(outline);
    else
      rotate.addNode(outline);

      // add skirt
    MultiShape skirt = ManipulateBlocks.
        buildMultiSkirt(false, min, max, 100.0);
    multiTarget.addEventTarget(skirt);

    if (rotate == null)
      multiTarget.addNode(skirt);
    else
      rotate.addNode(skirt);

    return multiTarget;
  }

}