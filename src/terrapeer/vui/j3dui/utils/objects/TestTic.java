package terrapeer.vui.j3dui.utils.objects;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.geometry.*;
import terrapeer.vui.j3dui.utils.objects.*;

/**
 Creates a standard test tic object that is a small box with
 a fixed translation.  The tic size is 0.1 x 0.1 x 0.1.  The
 color can be set at run time, and is purely emissive.
 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */

public class TestTic extends TransformGroup
{

  // public constants =========================================

  /** tic size. */
  public static final double TIC_SIZE = 0.1;

  // standard standard usage colors
  public static final Color3f TIC_WHITE =
      new Color3f(1f, 1f, 1f);
  public static final Color3f TIC_GRAY =
      new Color3f(.66f, .66f, .66f);
  public static final Color3f TIC_BLACK =
      new Color3f(0f, 0f, 0f);
  public static final Color3f TIC_X =
      new Color3f(.66f, 0f, 0f);
  public static final Color3f TIC_Y =
      new Color3f(0f, .66f, 0f);
  public static final Color3f TIC_Z =
      new Color3f(0f, 0f, .66f);

  // public interface =========================================

  /**
    Constructs a TestTic with the specified position and color.
    @param color Tic color.
    @param pos Tic position relative to the origin.
   */
  public TestTic(Color3f color, Vector3d pos)
  {
    // build primitive with capabilities
    _tic = new Box( (float) (TIC_SIZE / 2.0),
                   (float) (TIC_SIZE / 2.0), (float) (TIC_SIZE / 2.0),
                   Primitive.GENERATE_NORMALS |
                   Primitive.ENABLE_APPEARANCE_MODIFY |
                   Primitive.ENABLE_GEOMETRY_PICKING,
                   _app);

    addChild(_tic);

    // set picking
    setCapability(Node.ENABLE_PICK_REPORTING);

    // set color
    setColor(color);

    // set position
    Transform3D xform = new Transform3D();
    xform.setTranslation(pos);
    setTransform(xform);
  }

  /**
    Sets the color of the object, which is strictly emissive.
    @param color New color.
   */
  public void setColor(Color3f color)
  {
    _mat.setLightingEnable(true);
    _mat.setAmbientColor(0f, 0f, 0f);
    _mat.setDiffuseColor(0f, 0f, 0f);
    _mat.setEmissiveColor(color);
    _mat.setSpecularColor(0f, 0f, 0f);

    _app.setMaterial(_mat);
    _tic.setAppearance(_app);
  }

  // personal body ============================================

  /** Plane object. */
  private Box _tic;

  /** Dummy Appearance.  (for GC) */
  private final Appearance _app = new Appearance();

  /** Dummy Material.  (for GC) */
  private final Material _mat = new Material();
}