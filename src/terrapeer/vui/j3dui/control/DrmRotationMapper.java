package terrapeer.vui.j3dui.control;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;
import terrapeer.vui.j3dui.control.inputs.*;
import terrapeer.vui.j3dui.control.actuators.*;
import terrapeer.vui.j3dui.control.mappers.*;
import terrapeer.vui.j3dui.control.mappers.intuitive.*;

/**
 A convenience class "mapper" with an InputDragTarget input
 and an axis-angle Actuator target.  The target position will
 be relative to the position of the drag on the source
 display.
 <P>
 Display to target object mappings appropriate for DRM rotation
 are predefined (ROTATE_???).  The mappings are defined for
 canonical target object movement (i.e. drag right to rotate
 the target to the right).  Provisions are included to reverse the
 sense of the mapping (i.e. drag right to rotate the target
 left), which can be useful if the target is the view itself.
 <P>
 The event chain uses an IntuitiveDragMapper with a DRM plugin;
 connects that mapper to a source mapper with a direct plugin
 configured for rotation; and connects that mapper to the
 target actuator.
 <P>
 Since this class deals with intuitive mapping, the target must
 be a single Actuator instead of an actuation target exposed as
 an event out splitter.  For fixed axis rotation, use the
 DirectMapper class.

 */
public class DrmRotationMapper implements InputDragTarget
{

  // public constants =========================================

  /** Constant for mapping display drag X to target
    rotation about Y.  Display drag Y is ignored. */
  public static final int ROTATE_X_Y = 1;

  /** Constant for mapping display drag X to target
    rotation about -Z.  Display drag Y is ignored. */
  public static final int ROTATE_X_Z = 2;

  /** Constant for mapping display drag Y to target
    rotation about -X.  Display drag X is ignored. */
  public static final int ROTATE_Y_X = 3;

  // public interface =========================================

  /**
    Constructs a DrmRotationMapper with an actuator target
    and DRM plugin.
    @param actuator Target actuator.  Must be an axis angle
    actuator.  The actuator's target node serves as the mapper's
    target space node.  Never null.
   */
  public DrmRotationMapper(Actuator actuator)
  {

    if (actuator == null)
    {
      throw new
          IllegalArgumentException("'actuator' is null.");
    }
    _actuator = actuator;

    // check actuator and build axis-angle source mapper
    if (!(_actuator.getPlugin() instanceof TransformGroupPlugin &&
          ((TransformGroupPlugin)_actuator.getPlugin()).getPlugin()
          instanceof AxisAnglePlugin))
    {
      throw new IllegalArgumentException(
          "'actuator' does not have a AxisAnglePlugin.");
    }

    AxisAngleSourceDragPlugin sourcePlugin =
        new AxisAngleSourceDragPlugin(_actuator.getTargetNode());

    _sourceMapper = new SourceDragMapper(
        _actuator, sourcePlugin);

    // build DRM intuitive drag mapper
    DrmDragPlugin drmPlugin = new DrmDragPlugin();

    _inputMapper = new IntuitiveDragMapper(
        _sourceMapper, drmPlugin);

    // set default mapping
    setMapping(ROTATE_X_Y, false);
  }

  /**
    Constructs a DrmRotationMapper with an actuator target
    and common parameters.
    @param actuator Target actuator.  Must be an axis angle
    actuator.  The actuator's target node serves as the mapper's
    target space node.  Never null.
    @param cumulative If true, the actuation is cumulative
    between drags.  Defaults to true.
    @param mapping Rotation mapping choice (ROTATE_???).
    Defaults to ROTATE_X_Y.
   */
  public DrmRotationMapper(Actuator actuator,
                           boolean cumulative, int mapping, boolean reverse)
  {

    this(actuator);
    setCumulative(cumulative);
    setMapping(mapping, reverse);
  }

  /**
    Gets the target actuator.  Use it to configure actuation
    parameters (offset, scale, clamp).
    @return Reference to the target actuator.  Never null.
   */
  public Actuator getActuator()
  {
    return _actuator;
  }

  /**
    Sets whether or not the drag action is cumulative.
    @param enable If true, the action is cumulative
    between drags.  Defaults to true.
   */
  public void setCumulative(boolean enable)
  {
    _sourceMapper.setCumulative(enable);
  }

  /**
    Sets the display drag to actuator translation mapping and
    direction sense.  Only mappings that make sense for DRM
    rotation are allowed.  If the target is the view, with
    a normal sense, the drag appears to move the view; but, with
    a reverse sense, the drag appears to move the world.
    @param mapping Rotation mapping choice (ROTATE_???).
    Defaults to ROTATE_X_Y.
    @param reverse If false, the sense is normal.  If true, the
    sense is reversed.  Defaults to false.
   */
  public void setMapping(int mapping, boolean reverse)
  {
    AxisAngleSourceDragPlugin plugin =
        (AxisAngleSourceDragPlugin)_sourceMapper.getPlugin();

    Vector3d axis = new Vector3d();

    // set mapping
    switch (mapping)
    {
      case ROTATE_X_Y:
        plugin.setTargetMap(Mapper.DIM_W, Mapper.DIM_NONE,
                            Mapper.DIM_NONE);
        axis.set(0, 1, 0);
        break;
      case ROTATE_X_Z:
        plugin.setTargetMap(Mapper.DIM_W, Mapper.DIM_NONE,
                            Mapper.DIM_NONE);
        axis.set(0, 0, -1);
        break;
      case ROTATE_Y_X:
        plugin.setTargetMap(Mapper.DIM_NONE, Mapper.DIM_W,
                            Mapper.DIM_NONE);
        axis.set( -1, 0, 0);
        break;
    }

    // set sense
    if (reverse)
    {
      axis.negate();
    }
    plugin.setAxis(axis);
  }

  // InputDragTarget implementation

  public void startInputDrag(Canvas3D source, Vector2d pos)
  {
    _inputMapper.startInputDrag(source, pos);
  }

  public void doInputDrag(Canvas3D source, Vector2d pos)
  {
    _inputMapper.doInputDrag(source, pos);
  }

  public void stopInputDrag(Canvas3D source, Vector2d pos)
  {
    _inputMapper.stopInputDrag(source, pos);
  }

  public Vector4d getTheVec()
  {
    AxisAngleSourceDragPlugin plugin = (AxisAngleSourceDragPlugin)_sourceMapper.getPlugin();
    return plugin._v4;
  }

  // personal body ============================================

  /** Target actuator. Never null. */
  private Actuator _actuator;

  /** Source mapper. Never null. */
  private SourceDragMapper _sourceMapper;

  /** Input mapper. Never null. */
  private IntuitiveDragMapper _inputMapper;
}
