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

/**
 These utilities are common building block utilities that were
 first introduced for the book's examples on "basic" control.
 <P>
 Because these building blocks are rather specific in purpose
 they are included here as utilities instead of as general
 purpose building block classes in the framework core.
 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */

public class BasicBlocks
{

  // public utilities =========================================

  /**
    Attaches the view to a TransformGroup, positions and
    rotates it, and adds a headlight.
    @param view View to be attached.
    @param pos Position of view.
    @param rot Euler rotation of view.
    @return TransformGroup with view attached.
   */
  public static final TransformGroup buildView(
      AppView view, Vector3d pos, Vector3d rot)
  {

    // create group, add view.
    TransformGroup group = new TransformGroup();
    group.addChild(view);

    // rotate and position the group.
    Transform3D xform = new Transform3D();
    xform.setEuler(rot);
    xform.setTranslation(pos);
    group.setTransform(xform);

    // add a headlight to the view.
    view.addNode(new TestLight());

    return group;
  }

  /**
    Creates a chain of transforms used for manipulating a
    target object.  Typically the first transform is for
    translation and the second one if for some interactive
    operation (translation, rotation, scale, etc.).
    @param target Target object.
    @return Transform chain with the target attached.  The head
    transform is [0], which is initialized with <pos>.  The tail
    transform is [1], which contains the target.
   */
  public static final TransformGroup[] buildTarget(
      Node target)
  {

    return buildTarget(target, new Vector3d(0, 0, 0));
  }

  /**
    Creates a chain of transforms used for manipulating a
    target object.  Typically the first transform is for
    translation and the second one if for some interactive
    operation (translation, rotation, scale, etc.).
    @param target Target object.
    @param pos Initial position of the first transform.
    @return Transform chain with the target attached.  The head
    transform is [0], which is initialized with <pos>.  The tail
    transform is [1], which contains the target.
   */
  public static final TransformGroup[] buildTarget(
      Node target, Vector3d pos)
  {

    TransformGroup chain[] =
        {
        new TransformGroup(),
        new TransformGroup()};

    Transform3D xform = new Transform3D();
    xform.setTranslation(pos);
    chain[0].setTransform(xform);

    chain[0].addChild(chain[1]);
    chain[1].addChild(target);

    return chain;
  }

  /**
    Example building block that connects its input events to a
    TransformGroup target such that the target translates as
    input position changes.
   */
  public static final class Translator implements
      InputMoveTarget, InputDragTarget
  {

    // public interface =====================================

    /**
       Constructs a Translator.
       @param target Manipulation target.
       @param view View containing the sensor source display.
     */
    public Translator(TransformGroup target)
    {
      _target = target;
      _target.setCapability(
          TransformGroup.ALLOW_TRANSFORM_WRITE);
    }

    /**
       Sets the translation scale factor.
       @param scale Translation scale factor.
     */
    public void setScale(double scale)
    {
      _scale = scale;
    }

    // InputMoveTarget implementation

    public void setInputMove(Canvas3D source,
                             Vector2d pos)
    {

      _vec3d.set(_scale * pos.x, _scale * pos.y, 0);

      _xform.setIdentity();
      _xform.setTranslation(_vec3d);
      _target.setTransform(_xform);
    }

    // InputDragTarget implementation

    public void startInputDrag(Canvas3D source,
                               Vector2d pos)
    {
      setInputMove(source, pos);
    }

    public void doInputDrag(Canvas3D source,
                            Vector2d pos)
    {
      setInputMove(source, pos);
    }

    public void stopInputDrag(Canvas3D source,
                              Vector2d pos)
    {
      setInputMove(source, pos);
    }

    // personal body ========================================

    /** Manipulation target. */
    private TransformGroup _target;

    /** Scale factor. */
    private double _scale = .025;

    /** Dummy Vector3d.  (for GC) */
    private final Vector3d _vec3d = new Vector3d();

    /** Dummy Transform3D.  (for GC) */
    private final Transform3D _xform = new Transform3D();

  }

  /**
    Example building block that connects its input events to a
    TransformGroup target such that the target rotates as input
    position changes.
   */
  public static final class Rotator implements
      InputMoveTarget, InputDragTarget
  {

    // public interface =====================================

    /**
       Constructs a Rotator.
       @param target Manipulation target.
       @param view View containing the sensor source display.
       @param dim Target dimension affected (0=X, 1=Y, 2=Z).
     */
    public Rotator(TransformGroup target, int dim)
    {
      _target = target;
      _target.setCapability(
          TransformGroup.ALLOW_TRANSFORM_WRITE);
      _dim = dim;
    }

    /**
       Sets the rotation scale factor.
       @param scale Translation scale factor.
     */
    public void setScale(double scale)
    {
      _scale = scale;
    }

    // InputMoveTarget implementation

    public void setInputMove(Canvas3D source,
                             Vector2d pos)
    {

      switch (_dim)
      {
        default:
        case 0:
          _vec3d.set( -_scale * pos.y, 0, 0);
          break;
        case 1:
          _vec3d.set(0, _scale * pos.x, 0);
          break;
        case 2:
          _vec3d.set(0, 0, -_scale * pos.x);
          break;
      }

      _xform.setIdentity();
      _xform.setEuler(_vec3d);
      _target.setTransform(_xform);
    }

    // InputDragTarget implementation

    public void startInputDrag(Canvas3D source,
                               Vector2d pos)
    {
      setInputMove(source, pos);
    }

    public void doInputDrag(Canvas3D source,
                            Vector2d pos)
    {
      setInputMove(source, pos);
    }

    public void stopInputDrag(Canvas3D source,
                              Vector2d pos)
    {
      setInputMove(source, pos);
    }

    // personal body ========================================

    /** Manipulation target. */
    private TransformGroup _target;

    /** Rotation dimension. */
    private int _dim;

    /** Scale factor. */
    private double _scale = .025;

    /** Dummy Vector3d.  (for GC) */
    private final Vector3d _vec3d = new Vector3d();

    /** Dummy Transform3D.  (for GC) */
    private final Transform3D _xform = new Transform3D();

  }

  /**
    Example building block that connects the input events to a
    TestThing target such that the target changes color as the
    input modifier state changes.
   */
  public static final class Colorer implements
      InputModifierTarget
  {

    // public interface =====================================

    /**
       @param target Manipulation target.
     */
    public Colorer(TestThing target)
    {
      _target = target;
    }

    // InputModifierTarget implementation

    public void setInputModifier(int modifier)
    {
      switch (modifier)
      {
        case Input.MODIFIER_NONE:
          _target.setBoxColor(TestThing.BOX_DEFAULT);
          _target.setBallColor(TestThing.BALL_DEFAULT);
          break;
        case Input.MODIFIER_ESC:
          _target.setBoxColor(TestThing.BOX_DEFAULT);
          _target.setBallColor(TestThing.BALL_DOWN);
          break;
        case Input.MODIFIER_SHIFT:
          _target.setBoxColor(TestThing.BOX_NORMAL_1);
          _target.setBallColor(TestThing.BALL_DOWN);
          break;
        case Input.MODIFIER_CTRL:
          _target.setBoxColor(TestThing.BOX_NORMAL_2);
          _target.setBallColor(TestThing.BALL_DOWN);
          break;
        case Input.MODIFIER_ALT:
          _target.setBoxColor(TestThing.BOX_NORMAL_3);
          _target.setBallColor(TestThing.BALL_DOWN);
          break;
        case Input.MODIFIER_SHIFT_CTRL:
          _target.setBoxColor(TestThing.BOX_NORMAL_1);
          _target.setBallColor(TestThing.BOX_NORMAL_2);
          break;
        case Input.MODIFIER_SHIFT_ALT:
          _target.setBoxColor(TestThing.BOX_NORMAL_1);
          _target.setBallColor(TestThing.BOX_NORMAL_3);
          break;
      }
    }

    // personal body ========================================

    /** Manipulation target. */
    private TestThing _target;

  }

}