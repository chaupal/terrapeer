package terrapeer.vui.j3dui.utils.objects;

import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;

/**
 Creates a standard test object consisting of a ball embedded
 in a thin box with tics representing the positive extent
 along the major axes.  The box size is 2 x 2 x 0.2.  The ball
 radius is 0.66.  When in a live scene, the box and ball color
 can be set, and the whole object is pickable.
 <P>
 Because rendering speed may become an issue for large numbers
 of objects there are two forms: fast and slow.  This is
 a class, not an instance, attribute.  It only has an effect
 when the object is constructed, not after the fact.
 <P><B>Notes:</B><BR>
 Although the capability to change appearance is set on the box
 and ball shapes the ability to set the material and any of its
 properties is not set (and can not be set).  This together with
 the fact that setMaterial() and setAppearance() are
 set-by-reference, not set-by-value, means that dummy objects
 can not be used in a straight-forward manner to set material
 and appearance.  Therefore, to avoid GC stutter a "double
 buffer" approach is usd for the dummy objects.
 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */

public class TestThing extends BranchGroup
{

  // public constants =========================================

  /** box size X (width). */
  public static final double BOX_SIZE_X = 2.0;

  /** box size Y (height). */
  public static final double BOX_SIZE_Y = 2.0;

  /** box size Z (depth). */
  public static final double BOX_SIZE_Z = 0.2;

  /** ball size (radius). */
  public static final double BALL_SIZE = 0.666;

  // standard box interaction state colors
  public static final Color3f BOX_NORMAL_0 =
      new Color3f(.8f, .8f, .8f);
  public static final Color3f BOX_NORMAL_1 =
      new Color3f(1f, .3f, .3f);
  public static final Color3f BOX_NORMAL_2 =
      new Color3f(.3f, 1f, .3f);
  public static final Color3f BOX_NORMAL_3 =
      new Color3f(.3f, .3f, 1f);
  public static final Color3f BOX_DISABLE_0 =
      new Color3f(.4f, .4f, .4f);
  public static final Color3f BOX_DISABLE_1 =
      new Color3f(.4f, .3f, .3f);
  public static final Color3f BOX_DISABLE_2 =
      new Color3f(.3f, .4f, .3f);
  public static final Color3f BOX_DISABLE_3 =
      new Color3f(.3f, .3f, .4f);

  // standard ball interaction state colors
  public static final Color3f BALL_NORMAL =
      new Color3f(0f, 0f, 1f);
  public static final Color3f BALL_OVER =
      new Color3f(0f, 1f, 1f);
  public static final Color3f BALL_PAUSE =
      new Color3f(1f, 0f, 1f);
  public static final Color3f BALL_DOWN =
      new Color3f(1f, 1f, 0f);
  public static final Color3f BALL_DRAG =
      new Color3f(0f, 1f, 0f);
  public static final Color3f BALL_DROP =
      new Color3f(1f, 0f, 0f);
  public static final Color3f BALL_CANCEL =
      new Color3f(.6f, .6f, .6f);

  // standard box example colors
  public static final Color3f BOX_DEFAULT =
      new Color3f(1f, 0f, .6f);
  public static final Color3f BOX_TRANSLATION =
      new Color3f(1f, 0f, 0f);
  public static final Color3f BOX_ROTATION =
      new Color3f(0f, 1f, 0f);
  public static final Color3f BOX_SCALE =
      new Color3f(0f, 0f, 1f);

  // standard ball example colors
  public static final Color3f BALL_DEFAULT =
      new Color3f(0f, .6f, 1f);
  public static final Color3f BALL_AFFINE =
      new Color3f(1f, .66f, .33f);
  public static final Color3f BALL_SPHERE =
      new Color3f(.25f, .75f, .5f);

  // public utilities =========================================

  /**
    Sets whether or not TestThing balls will be constructed
    using a fast or slow form.
    @param enabled True for the fast form.
   */
  public static void setFast(boolean enabled)
  {
    _fast = enabled;
  }

  // public interface =========================================

  /**
    Constructs a TestThing with default colors.
   */
  public TestThing()
  {
    this(BOX_DEFAULT, BALL_DEFAULT);
  }

  /**
    Constructs a TestThing with the specified component colors.
    @param boxColor Box Color.
    @param ballColor Ball Color.
   */
  public TestThing(Color3f boxColor, Color3f ballColor)
  {
    // build box
    _box = new Box( (float) (BOX_SIZE_X / 2.0),
                   (float) (BOX_SIZE_Y / 2.0), (float) (BOX_SIZE_Z / 2.0),
                   Primitive.GENERATE_NORMALS |
                   Primitive.ENABLE_APPEARANCE_MODIFY |
                   Primitive.ENABLE_GEOMETRY_PICKING,
                   _boxApp[_boxI]);
    addChild(_box);

    // build ball
    if (_fast)
    {
      _fastBall = new Box( (float) BALL_SIZE,
                          (float) BALL_SIZE, (float) BALL_SIZE,
                          Primitive.GENERATE_NORMALS |
                          Primitive.ENABLE_APPEARANCE_MODIFY |
                          Primitive.ENABLE_GEOMETRY_PICKING,
                          _ballApp[_ballI]);
      addChild(_fastBall);
    }
    else
    {
      _slowBall = new Sphere( (float) BALL_SIZE,
                             Primitive.GENERATE_NORMALS |
                             Primitive.ENABLE_APPEARANCE_MODIFY |
                             Primitive.ENABLE_GEOMETRY_PICKING,
                             _ballApp[_ballI]);
      addChild(_slowBall);
    }

    // build tics
    addChild(new TestTic(TestTic.TIC_GRAY,
                         new Vector3d(BOX_SIZE_X / 2.0, 0, 0)));
    addChild(new TestTic(TestTic.TIC_GRAY,
                         new Vector3d(0, BOX_SIZE_Y / 2.0, 0)));
    addChild(new TestTic(TestTic.TIC_GRAY,
                         new Vector3d(0, 0, BALL_SIZE)));

    // set picking
    setCapability(Node.ENABLE_PICK_REPORTING);

    // set colors (i.e. fill in appearance nodes)
    setBoxColor(boxColor);
    setBallColor(ballColor);
  }

  /**
    Sets the color of the Thing box.
    @param color New color.
   */
  public void setBoxColor(Color3f color)
  {
    _boxI = ++_boxI % 2;

    _boxMat[_boxI].setLightingEnable(true);
    _boxMat[_boxI].setAmbientColor(color);
    _boxMat[_boxI].setDiffuseColor(color);
    _boxApp[_boxI].setMaterial(_boxMat[_boxI]);
    _box.setAppearance(_boxApp[_boxI]);
  }

  /**
    Sets the color of the Thing ball.
    @param color New color.
   */
  public void setBallColor(Color3f color)
  {
    _ballI = ++_ballI % 2;

    _ballMat[_ballI].setLightingEnable(true);
    _ballMat[_ballI].setAmbientColor(color);
    _ballMat[_ballI].setDiffuseColor(color);
    _ballApp[_ballI].setMaterial(_ballMat[_ballI]);

    if (_fast)
      _fastBall.setAppearance(_ballApp[_ballI]);
    else
      _slowBall.setAppearance(_ballApp[_ballI]);
  }

  // personal body ============================================

  /** Box component. */
  private Box _box;

  /** Slow ball component.  Null if none. */
  private Sphere _slowBall;

  /** Fast ball component.  Null is none. */
  private Box _fastBall;

  /** If true, the fast form is used. */
  private static boolean _fast = false;

  /** Box dummy buffer index (0 or 1). */
  private int _boxI = 0;

  /** Dummy box Appearance.  (for GC) */
  private final Appearance _boxApp[] =
      {
      new Appearance(), new Appearance()};

  /** Dummy box Material.  (for GC) */
  private final Material _boxMat[] =
      {
      new Material(), new Material()};

  /** Ball dummy buffer index (0 or 1). */
  private int _ballI = 0;

  /** Dummy ball Appearance.  (for GC) */
  private final Appearance _ballApp[] =
      {
      new Appearance(), new Appearance()};

  /** Dummy ball Material.  (for GC) */
  private final Material _ballMat[] =
      {
      new Material(), new Material()};
}