package terrapeer.vui.j3dui.utils.objects;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.objects.*;

/**
 Creates a standard test line object that is a line segment
 whose ends are specified at construction time.  The
 color can be set at run time, and is an attribute.
 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */

public class TestLine extends Shape3D
{

  // public constants =========================================

  // standard example colors
  public static final Color3f LINE_DEFAULT =
      new Color3f(1f, 1f, 1f);

  // public interface =========================================

  /**
    Constructs a TestLine that is pickable.  The line extends
    from the origin to Y=1.
   */
  public TestLine()
  {
    this(true, LINE_DEFAULT, new Vector3d(0, 0, 0),
         new Vector3d(0, 1, 0));
  }

  /**
    Constructs a TestLine that is pickable and has the specified
    geometry.
    @param min Minimum extent of the shape.
    @param max Maximum extent of the shape.
   */
  public TestLine(Tuple3d min, Tuple3d max)
  {
    this(true, LINE_DEFAULT, min, max);
  }

  /**
    Constructs a TestLine with the specified color and
    geometry.
    @param pickable If true the shape is geometry pickable,
    otherwise is is not pickable.
    @param color Object color.
    @param min Minimum extent of the shape.
    @param max Maximum extent of the shape.
   */
  public TestLine(boolean pickable, Color3f color,
                  Tuple3d min, Tuple3d max)
  {

    // set live coloring capability
    setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
    _app.setCapability(Appearance.
                       ALLOW_COLORING_ATTRIBUTES_WRITE);
    _colorAtt.setCapability(ColoringAttributes.
                            ALLOW_COLOR_WRITE);

    // build geometry
    LineArray array =
        new LineArray(2, GeometryArray.COORDINATES);

    array.setCoordinates(0, new double[]
                         {
                         min.x, min.y, min.z,
                         max.x, max.y, min.z});

    setGeometry(array);

    // set picking
    if (pickable)
    {
      setPickable(true);
      array.setCapability(Geometry.ALLOW_INTERSECT);
    }
    else
    {
      setPickable(false);
    }

    // set color
    setColor(color);
  }

  /**
    Sets the color of the outline.  The appearance is purely
    emissive.
    @param color New color.
   */
  public void setColor(Color3f color)
  {
    _colorAtt.setColor(color);
    _app.setColoringAttributes(_colorAtt);
    setAppearance(_app);
  }

  // personal body ============================================

  /** Dummy Appearance.  (for GC) */
  private final Appearance _app = new Appearance();

  /** Dummy ColoringAttributes.  (for GC) */
  private final ColoringAttributes _colorAtt =
      new ColoringAttributes();

}