package terrapeer.vui.j3dui.utils;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.utils.app.*;

/**
 Contains constants and utilities of general use for object
 bounds.
 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */

public class BoundsUtils
{

  // public utilities =========================================

  /**
    Used to setup for using getBoundingBox(), which must be done
    in a live scene.  Sets the target object's capability bits
    for recursively determining its bounding box.  Must be used
    while the target is not live.  Any manually set bounds may be
    lost.
    @param target Node representing the object to be configured.
    Never null.
   */
  public static void allowGetBoundingBox(Node target)
  {
    target.setBoundsAutoCompute(true);
    target.setCapability(Node.ALLOW_BOUNDS_READ);
    target.setCapability(Node.ALLOW_LOCAL_TO_VWORLD_READ);

    if (target instanceof Group)
    {
      // branch: get kids and recurse
      Group group = (Group) target;
      target.setCapability(Group.ALLOW_CHILDREN_READ);

      int nodeC = group.numChildren();
      for (int nodeI = 0; nodeI < nodeC; nodeI++)
      {
        allowGetBoundingBox(group.getChild(nodeI));
      }
    }
  }

  /**
    Gets an axis-aligned bounding box in world coordinates for
    the target object.  The target must be live and the
    appropriate capability bits must have been set, such as
    with allowGetBoundingBox().
    @param target Node representing the target object.  Never
    null.
    @param copy Container for the returned object, which must
    initially be empty.
    @return Reference to copy, which contains the target object
    bounding box.
   */
  public static BoundingBox getBoundingBox(Node target,
                                           BoundingBox copy)
  {

    // set bounds to empty
    copy.setLower(1, 1, 1);
    copy.setUpper( -1, -1, -1);

    // get bounds recursively
    getBoundingBoxP(target, copy);

    if (Debug.getEnabled())
    {
      Debug.println("BoundsUtils",
                    "BOUNDS:BoundsUtils.getBoundingBox:" +
                    " bounds=" + copy);
    }

    return copy;
  }

  /**
    Gets an axis-aligned bounding box in world coordinates for
    the target object.  Makes no assumptions as to the state of
    the world or the capability bits of the target.  If the
    target has no parent then it will be temporarily connected
    to the world to compute its bounding box, otherwise the
    target is assumed to be connected to the world.
    <P>
    The target object may be left with different and additional
    capability bits and any manually set bounds may be lost.
    @param target Node representing the target object.  Never
    null.
    @param world World that the target belongs to.
    @param copy Container for the returned object, which must
    initially be empty.
    @return Reference to copy, which contains the target object
    bounding box.
   */
  public static BoundingBox getBoundingBox(Node target,
                                           AppWorld world, BoundingBox copy)
  {

    // setup the world and target
    boolean isLive = world.setLive(false);
    Group root = world.getSceneRoot();

    boolean isChild = true;
    if (target.getParent() == null)
    {
      isChild = false;
      root.addChild(target);
    }

    allowGetBoundingBox(target);

    // get bounds recursively
    world.setLive(true);
    getBoundingBox(target, copy);
    world.setLive(false);

    // restore the world and target
    if (!isChild)
      root.removeChild(root.numChildren() - 1);
    world.setLive(isLive);

    return copy;
  }

  // personal body ============================================

  /**
    Private internal version.  Gets an axis-aligned bounding box
    for the target object.
   */
  private static final void getBoundingBoxP(Node target,
                                            BoundingBox copy)
  {

    if (Debug.getEnabled())
    {
      Debug.println("BoundsUtils.verbose",
                    "BOUNDS:BoundsUtils.getBoundingBoxP:" +
                    " target=" + target +
                    " bounds=" + copy);
    }

    if (target instanceof Group)
    {
      // branch: get kids and recurse
      Group group = (Group) target;

      int nodeC = group.numChildren();
      for (int nodeI = 0; nodeI < nodeC; nodeI++)
      {
        getBoundingBoxP(group.getChild(nodeI),
                        copy);
      }
    }
    else
    if (target instanceof Shape3D)
    {
      // leaf: get bounding box
      Shape3D leaf = (Shape3D) target;

      /// get and xform bounds, add it
      Bounds bounds = leaf.getBounds();
      Transform3D xform = new Transform3D();
      leaf.getLocalToVworld(xform);
      bounds.transform(xform);
      copy.combine(bounds);

      if (Debug.getEnabled())
      {
        Debug.println("BoundsUtils.verbose",
                      "BOUNDS:BoundsUtils.getBoundingBoxP:" +
                      " leafBounds=" + leaf.getBounds() +
                      " comboBounds=" + copy);
      }

    }
    else
    if (target instanceof Morph)
    {
      // leaf: get bounding box
      Morph leaf = (Morph) target;

      /// get and xform bounds, add it
      Bounds bounds = leaf.getBounds();
      Transform3D xform = new Transform3D();
      leaf.getLocalToVworld(xform);
      bounds.transform(xform);
      copy.combine(bounds);

      if (Debug.getEnabled())
      {
        Debug.println("BoundsUtils.verbose",
                      "BOUNDS:BoundsUtils.getBoundingBoxP:" +
                      " leafBounds=" + leaf.getBounds() +
                      " comboBounds=" + copy);
      }

    }
  }

}