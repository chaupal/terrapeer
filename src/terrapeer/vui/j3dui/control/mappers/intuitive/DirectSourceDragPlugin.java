package terrapeer.vui.j3dui.control.mappers.intuitive;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.mappers.*;
import terrapeer.vui.j3dui.control.actuators.*;

/**
 A source drag mapper plugin that directly maps source drag
 value dimensions to target actuation value dimensions.  All
 actuation value dimensions are affected.
 <P>
 The output actuation value is defined as follows:
 <UL>
 <LI>X - Generic X actuation value.
 <LI>Y - Generic Y actuation value.
 <LI>Z - Generic Z actuation value.
 <LI>W - Generic W actuation value
 </UL>
 <P>
 By definition, source space to actuation transformation is
 vector not point-based, which means that direction and
 magnitude are mapped but not position.

 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */

public class DirectSourceDragPlugin extends SourceDragMapperPlugin
{

  // public interface =========================================

  /**
    Constructs a DirectSourceDragPlugin with default source
    mapping (i.e. X to X, Y to Y, Z to Z) and with the target
    space defined by the specified target node.  Sets the target
    node's capability bits for live use.
    @param targetSpace Target space reference node.  If null the
    world space will be used as the target space.
   */
  public DirectSourceDragPlugin(Node targetSpace)
  {
    super(targetSpace);
  }

  /**
    Sets the scaling factor applied to source values.
    @param scale Source scaling value.
   */
  public Vector3d setSourceScale(Vector3d scale)
  {
    _scale.set(scale);
    return _scale;
  }

  /**
    Gets the scaling factor applied to source values.
    @return Reference to the source scaling value.
   */
  public Vector3d getSourceScale()
  {
    return _scale;
  }

  /**
    Sets the source-side mapping from 3D source space to 3D
    target space, by source space dimension.
    @param mapX The target dimensions (Mapper.DIM_X/Y/Z)
    controlled by the source X dimension.
    @param mapY The target dimensions (Mapper.DIM_X/Y/Z)
    controlled by the source Y dimension.
    @param mapZ The target dimensions (Mapper.DIM_X/Y/Z)
    controlled by the source Z dimension.
   */
  public void setSourceMap(int mapX, int mapY, int mapZ)
  {
    _srcMapX[0] = ((mapX & Mapper.DIM_X) == 0) ? false : true;
    _srcMapX[1] = ((mapX & Mapper.DIM_Y) == 0) ? false : true;
    _srcMapX[2] = ((mapX & Mapper.DIM_Z) == 0) ? false : true;

    _srcMapY[0] = ((mapY & Mapper.DIM_X) == 0) ? false : true;
    _srcMapY[1] = ((mapY & Mapper.DIM_Y) == 0) ? false : true;
    _srcMapY[2] = ((mapY & Mapper.DIM_Z) == 0) ? false : true;

    _srcMapZ[0] = ((mapZ & Mapper.DIM_X) == 0) ? false : true;
    _srcMapZ[1] = ((mapZ & Mapper.DIM_Y) == 0) ? false : true;
    _srcMapZ[2] = ((mapZ & Mapper.DIM_Z) == 0) ? false : true;

    if (Debug.getEnabled())
    {
      Debug.println("DirectSourceDragPlugin",
                    "MAP:DirectSourceDragPlugin:setSourceMap:" +
                    " srcMapX=" + _srcMapX[0] + " " + _srcMapX[1] + " " + _srcMapX[2] +
                    " srcMapY=" + _srcMapY[0] + " " + _srcMapY[1] + " " + _srcMapY[2] +
                    " srcMapZ=" + _srcMapZ[0] + " " + _srcMapZ[1] + " " + _srcMapZ[2]);
    }

  }

  /**
    Sets the target-side mapping from 3D target space to 4D
    actuation space, by target space dimension.
    @param mapX The actuation dimensions (Mapper.DIM_X/Y/Z/W)
    controlled by the target space X dimension.
    @param mapY The actuation dimensions (Mapper.DIM_X/Y/Z/W)
    controlled by the target space Y dimension.
    @param mapZ The actuation dimensions (Mapper.DIM_X/Y/Z/W)
    controlled by the target space Z dimension.
   */
  public void setTargetMap(int mapX, int mapY, int mapZ)
  {
    _trgMapX[0] = ((mapX & Mapper.DIM_X) == 0) ? false : true;
    _trgMapX[1] = ((mapX & Mapper.DIM_Y) == 0) ? false : true;
    _trgMapX[2] = ((mapX & Mapper.DIM_Z) == 0) ? false : true;
    _trgMapX[3] = ((mapX & Mapper.DIM_W) == 0) ? false : true;

    _trgMapY[0] = ((mapY & Mapper.DIM_X) == 0) ? false : true;
    _trgMapY[1] = ((mapY & Mapper.DIM_Y) == 0) ? false : true;
    _trgMapY[2] = ((mapY & Mapper.DIM_Z) == 0) ? false : true;
    _trgMapY[3] = ((mapY & Mapper.DIM_W) == 0) ? false : true;

    _trgMapZ[0] = ((mapZ & Mapper.DIM_X) == 0) ? false : true;
    _trgMapZ[1] = ((mapZ & Mapper.DIM_Y) == 0) ? false : true;
    _trgMapZ[2] = ((mapZ & Mapper.DIM_Z) == 0) ? false : true;
    _trgMapZ[3] = ((mapZ & Mapper.DIM_W) == 0) ? false : true;

    if (Debug.getEnabled())
    {
      Debug.println("DirectSourceDragPlugin",
                    "MAP:DirectSourceDragPlugin:setTargetMap:" +
                    " trgMapX=" +
                    _trgMapX[0] + " " + _trgMapX[1] + " " + _trgMapX[2] + " " + _trgMapX[3] +
                    " trgMapY=" +
                    _trgMapY[0] + " " + _trgMapY[1] + " " + _trgMapY[2] + " " + _trgMapY[3] +
                    " trgMapZ=" +
                    _trgMapZ[0] + " " + _trgMapZ[1] + " " + _trgMapZ[2] + " " + _trgMapZ[3]);
    }

  }

  /**
    Gets the source-side mapping for the source space X
    dimension.
    @return Reference to an array of flags by target space
    dimension.
   */
  public boolean[] getSourceMapX()
  {
    return _srcMapX;
  }

  /**
    Gets the source-side mapping for the source space Y
    dimension.
    @return Reference to an array of flags by target space
    dimension.
   */
  public boolean[] getSourceMapY()
  {
    return _srcMapY;
  }

  /**
    Gets the source-side mapping for the source space Z
    dimension.
    @return Reference to an array of flags by target space
    dimension.
   */
  public boolean[] getSourceMapZ()
  {
    return _srcMapZ;
  }

  /**
    Gets the target-side mapping for the target space X
    dimension.
    @return Reference to an array of flags by actuation space
    dimension.
   */
  public boolean[] getTargetMapX()
  {
    return _trgMapX;
  }

  /**
    Gets the target-side mapping for the target space Y
    dimension.
    @return Reference to an array of flags by actuation space
    dimension.
   */
  public boolean[] getTargetMapY()
  {
    return _trgMapY;
  }

  /**
    Gets the target-side mapping for the target space Z
    dimension.
    @return Reference to an array of flags by actuation space
    dimension.
   */
  public boolean[] getTargetMapZ()
  {
    return _trgMapZ;
  }

  /**
    Sets the offset constant applied to target values.
    @param offset Target offset value.
    @return Target offset value.
   */
  public Vector4d setTargetOffset(Vector4d offset)
  {
    _offset.set(offset);
    return _offset;
  }

  /**
    Gets the offset constant applied to target values.
    @return Reference to the target offset value.
   */
  public Vector4d getTargetOffset()
  {
    return _offset;
  }

  // SourceDragMapperPlugin implementation

  public String toString()
  {
    return "DirectSourceDragPlugin";
  }

  // personal body ============================================

  /** The scale factor applied to source values. */
  private Vector3d _scale = new Vector3d(1, 1, 1);

  /** If true, the target space dim gets source X value. */
  private boolean _srcMapX[] =
                               {true, false, false};

  /** If true, the target space dim gets source Y value. */
  private boolean _srcMapY[] =
                               {false, true, false};

  /** If true, the target space dim gets source Y value. */
  private boolean _srcMapZ[] =
                               {false, false, true};

  /** If true, the actuation dim gets target space X value. */
  private boolean _trgMapX[] =
                               {true, false, false, false};

  /** If true, the actuation dim gets target space Y value. */
  private boolean _trgMapY[] =
                               {false, true, false, false};

  /** If true, the actuation dim gets target space Y value. */
  private boolean _trgMapZ[] =
                               {false, false, true, false};

  /** The offset value applied to the target values. */
  private Vector4d _offset = new Vector4d(0, 0, 0, 0);

  /** Dummy source transform.  (for GC) */
  private final Transform3D _sourceXform =
      new Transform3D();

  /** Dummy target transform.  (for GC) */
  private final Transform3D _targetXform =
      new Transform3D();

  /** Dummy vector.  (for GC) */
  private final Vector3d _vector = new Vector3d();

  /** Dummy value.  (for GC) */
  private final Vector3d _value = new Vector3d();

  /**
    Performs the source-side vector processing, which includes
    scale, and then directly maps the 3D XYZ source space input
    value into a 3D XYZ target space value.  All output
    dimensions are affected.  Output values are accumulated by
    dimension.
    @param value Source value.
    @param copy Container for the copied return value.
    @return Reference to copy.
   */
  protected Vector3d toSourceValue(Vector3d value,
                                   Vector3d copy)
  {

    // can't use _vector, already in use
    _value.set(_scale.x * value.x, _scale.y * value.y,
               _scale.z * value.z);

    copy.set(0, 0, 0);

    if (_srcMapX[0])
    {
      copy.x += _value.x;
    }
    if (_srcMapX[1])
    {
      copy.y += _value.x;
    }
    if (_srcMapX[2])
    {
      copy.z += _value.x;

    }
    if (_srcMapY[0])
    {
      copy.x += _value.y;
    }
    if (_srcMapY[1])
    {
      copy.y += _value.y;
    }
    if (_srcMapY[2])
    {
      copy.z += _value.y;

    }
    if (_srcMapZ[0])
    {
      copy.x += _value.z;
    }
    if (_srcMapZ[1])
    {
      copy.y += _value.z;
    }
    if (_srcMapZ[2])
    {
      copy.z += _value.z;

    }
    if (Debug.getEnabled())
    {
      Debug.println("DirectSourceDragPlugin",
                    "DRAG:DirectSourceDragPlugin:toSourceValue:" +
                    " inVal=" + value +
                    " srcScale=" + _scale +
                    " outVal=" + copy);
    }

    return copy;
  }

  /**
    Performs the target-side vector processing, which includes
    offset, and then directly maps the 3D XYZ target space input
    value into a 4D XYZW actuation space value.  All output
    dimensions are affected.  Output values are accumulated by
    dimension.
    @param value Source value.
    @param copy Container for the copied return value.
    @return Reference to copy.
   */
  public Vector4d toTargetValue(Vector3d value,
                                Vector4d copy)
  {

    copy.set(_offset);

    if (_trgMapX[0])
    {
      copy.x += value.x;
    }
    if (_trgMapX[1])
    {
      copy.y += value.x;
    }
    if (_trgMapX[2])
    {
      copy.z += value.x;
    }
    if (_trgMapX[3])
    {
      copy.w += value.x;

    }
    if (_trgMapY[0])
    {
      copy.x += value.y;
    }
    if (_trgMapY[1])
    {
      copy.y += value.y;
    }
    if (_trgMapY[2])
    {
      copy.z += value.y;
    }
    if (_trgMapY[3])
    {
      copy.w += value.y;

    }
    if (_trgMapZ[0])
    {
      copy.x += value.z;
    }
    if (_trgMapZ[1])
    {
      copy.y += value.z;
    }
    if (_trgMapZ[2])
    {
      copy.z += value.z;
    }
    if (_trgMapZ[3])
    {
      copy.w += value.z;

    }
    if (Debug.getEnabled())
    {
      Debug.println("DirectSourceDragPlugin",
                    "DRAG:DirectSourceDragPlugin:toTargetValue:" +
                    " inVal=" + value +
                    " trgOffset=" + _offset +
                    " outVal=" + copy);
    }

    return copy;
  }

  // SourceDragMapperPlugin implementation

  protected Vector4d startSourceDrag(Node source, Vector3d pos,
                                     Vector4d copy)
  {
    // do nothing
    return copy;
  }

  protected Vector4d doSourceDrag(Node source, Vector3d pos,
                                  Vector4d copy)
  {

    // map vector value from source to actuation
    toSourceValue(pos, _vector);
    Mapper.toTargetSpace(_vector, source, getTargetSpace(),
                         _vector);
    toTargetValue(_vector, copy);

    return copy;
  }

  protected Vector4d stopSourceDrag(Node source, Vector3d pos,
                                    Vector4d copy)
  {
    // do nothing
    return copy;
  }

}
