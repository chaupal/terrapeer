package terrapeer.vui.helpers;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.geometry.*;

import terrapeer.*;
import terrapeer.vui.j3dui.utils.objects.*;

/**
*  Creates a standard plane consisting of a thin box that is
PLANE_THICKNESS thick.  The plane lies in the X-Y plane and
whose origin is centered in the object.  When in a live scene,
the plane is pickable and its color can be set.
(P>
Note that setMaterial() and setAppearance() are set-by-reference,
not set-by-value, so separate ones must be used for each
component.
<P>
The capabilty bit for reading the Box and Ball Material is not
set but the shapes are internally compiled, so new Material
objects must be created each time the colors are set.

 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */
public class BasePlane extends BranchGroup
{
  /** Plane object. */
  private Box _plane;

  /** Dummy Appearance.  (for GC) */
  private final Appearance _app = new Appearance();

  /** Dummy Material.  (for GC) */
  private final Material _mat = new Material();

  /**
    Constructs a TestPlane with default colors and size (10x10).
   */
  public BasePlane()
  {
    this(vars.PLANE_DEFAULT, 10, 10);
  }

  /**
    Constructs a TestPlane with the specified color and size.
    @param sizeX Size of plane along X.
    @param sizeY Size of plane along Y.
    @param color Object color.
   */
  public BasePlane(Color3f color, double sizeX, double sizeY)
  {
    // build primitive with capabilities
    _plane = new Box((float)(sizeX / 2), (float)(sizeY / 2),
                     (float)vars.PLANE_THICKNESS,
                     Primitive.GENERATE_NORMALS |
                     Primitive.ENABLE_APPEARANCE_MODIFY |
                     Primitive.ENABLE_GEOMETRY_PICKING,
                     _app);

    addChild(_plane);

    // set picking
    setCapability(Node.ENABLE_PICK_REPORTING);

    // set colors (i.e. fill in appearance node)
    setColor(color);
  }

  /**
    Sets the color of the object.
    @param color New color.
   */
  public void setColor(Color3f color)
  {
    _mat.setLightingEnable(true);
    _mat.setAmbientColor(color);
    _mat.setDiffuseColor(color);

    _app.setMaterial(_mat);
    _plane.setAppearance(_app);
  }

}
