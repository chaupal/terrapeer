package terrapeer.vui.j3dui.utils;

import javax.media.j3d.*;

import terrapeer.vui.j3dui.utils.*;

/**
 Contains constants and utilities of general use for object
 picking.
 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */

public class PickUtils
{

  // public utilities =========================================

  /**
    Sets whether or not an object's leaf nodes are pickable, but
    not its geometry.  (Use setGeoPickable for leaf and geo
    pickability.)  Although Node.setPickable is supposed to do
    this, an apparent bug (feature?) prevents it from working
    consistently.
    @param target Node representing the target object.  Never
    null.
    @param pickable True to make node pickable, false to make
    it non-pickable.
   */
  public static void setLeafPickable(Node target,
                                     boolean pickable)
  {

    if (target instanceof Group)
    {
      // branch: get kids and recurse
      Group group = (Group) target;

      int nodeC = group.numChildren();
      for (int nodeI = 0; nodeI < nodeC; nodeI++)
      {
        setLeafPickable(group.getChild(nodeI), pickable);
      }
    }
    else
    if (target instanceof Shape3D)
    {
      target.setPickable(pickable);
    }
    else
    if (target instanceof Morph)
    {
      target.setPickable(pickable);
    }
  }

  /**
    Used to setup for using setGeoPickable() in a live scene.
    Sets the target object's capability bits for recursively
    setting its geometry pickability.  Must be used while the
    target is not live.
    @param target Node representing the object to be configured.
    Never null.
   */
  public static void allowSetGeoPickable(Node target)
  {
    if (target instanceof Group)
    {
      // branch: get kids and recurse
      Group group = (Group) target;
      group.setCapability(Group.ALLOW_CHILDREN_READ);

      int nodeC = group.numChildren();
      for (int nodeI = 0; nodeI < nodeC; nodeI++)
      {
        allowSetGeoPickable(group.getChild(nodeI));
      }
    }
    else
    if (target instanceof Shape3D)
    {
      Shape3D leaf = (Shape3D) target;
      leaf.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
    }
    else
    if (target instanceof Morph)
    {
      Morph leaf = (Morph) target;
      leaf.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
    }
  }

  /**
    Sets whether or not the an object's leaf nodes and geometry
    is pickable.  The target must be dead or the appropriate
    capability bits must have been set, such as with
    allowSetGeoPickable().
    @param target Node representing the target object.  Never
    null.
    @param pickable True to make node pickable, false to make
    it non-pickable.
   */
  public static void setGeoPickable(Node target,
                                    boolean pickable)
  {

    if (target instanceof Group)
    {
      // branch: get kids and recurse
      Group group = (Group) target;

      int nodeC = group.numChildren();
      for (int nodeI = 0; nodeI < nodeC; nodeI++)
      {
        setGeoPickable(group.getChild(nodeI), pickable);
      }
    }
    else
    if (target instanceof Shape3D)
    {
      // leaf: set pickability
      /// set node pickability
      target.setPickable(pickable);

      /// set geometry intersectability
      Shape3D leaf = (Shape3D) target;
      Geometry geo = leaf.getGeometry();

      if (geo != null)
      {
        if (pickable)
        {
          geo.setCapability(Geometry.ALLOW_INTERSECT);
        }
        else
        {
          geo.clearCapability(Geometry.ALLOW_INTERSECT);
        }
      }
    }
    else
    if (target instanceof Morph)
    {
      // leaf: set pickability
      /// set node pickability
      target.setPickable(pickable);

      /// set geometry intersectability
      Morph leaf = (Morph) target;

      int arrayC = leaf.getWeights().length;
      for (int arrayI = 0; arrayI < arrayC; arrayI++)
      {
        if (pickable)
        {
          leaf.getGeometryArray(arrayI).
              setCapability(Geometry.ALLOW_INTERSECT);
        }
        else
        {
          leaf.getGeometryArray(arrayI).
              clearCapability(Geometry.ALLOW_INTERSECT);
        }
      }
    }
  }

  // personal body ============================================

}