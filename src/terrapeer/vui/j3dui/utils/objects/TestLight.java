package terrapeer.vui.j3dui.utils.objects;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.objects.*;

/**
 Creates a standard test light with infinite influencing bounds.
 If the light has a direction the default orientation is
 pointing along the -Z axis.
 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */

public class TestLight extends Group
{

  // public interface =========================================

  /**
    Constructs a TestLight with a PointLight target light.
    (A DirectionalLight would make a more efficient
    test light but Java 3D 1.1.2 has a bug that appears when
    such a light is used in multiple views.)
   */
  public TestLight()
  {
    this(new PointLight());
    _light.setColor(new Color3f(0.6f, 0.6f, 0.6f));
  }

  /**
    Constructs a TestLight from the specified target light.
    Typically the light will be pointing along the -Z axis.
    @param light The target light.  Never null.
   */
  public TestLight(Light light)
  {
    if (light == null)
      throw new
          IllegalArgumentException("'light' is null.");

    _light = light;

    BoundingSphere lightBounds = new BoundingSphere(
        new Point3d(0.0, 0.0, 0.0), Double.POSITIVE_INFINITY);
    _light.setInfluencingBounds(lightBounds);
    _light.setEnable(true);

    addChild(_light);
  }

  /**
    Gets the target light.
    @return Reference to the target light.
   */
  public Light getLight()
  {
    return _light;
  }

  // personal body ============================================

  /** Target light. */
  Light _light;

}