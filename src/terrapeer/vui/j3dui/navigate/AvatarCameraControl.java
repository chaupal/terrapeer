package terrapeer.vui.j3dui.navigate;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.*;
import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.utils.objects.*;
import terrapeer.vui.j3dui.control.*;
import terrapeer.vui.j3dui.control.inputs.*;
import terrapeer.vui.j3dui.control.inputs.sensors.*;
import terrapeer.vui.j3dui.control.mappers.*;
import terrapeer.vui.j3dui.control.actuators.*;
import terrapeer.vui.j3dui.control.actuators.groups.*;

/**
 * Creates a controller (draggers, filters, and mappers) for a Camera,
 * which presumably has a view and possibly a headlight attached.
 * <P>
 * By default, camera thrust is directly controlled with arrow Y,
 * no modifiers, through an acceleration filter; camera
 * attitude-roll and pitch are controlled with mouse X and Y,
 * no modifiers, through a rate filter; and, camera attitude-yaw
 * is controlled through arrow X, no modifiers, through a rate
 * filter.
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */
public class AvatarCameraControl
{
  /** Avatar camera. Never null. */
  private AvatarCamera _camera;

  /** Thrust DRM mapper. Never null. */
  private DrmTranslationMapper _thrustMapper;
  /** Thrust acceleration filter. Never null. */
  private AccelerateInputDragFilter _thrustFilter;
  /** Thrust canceler sensor. Never null. */
  private AwtKeyboardModifierSensor _thrustCanceler;
  /** Thrust relative dragger. Never null. */
  private RelativeDragger _thrustDragger;

  /** Roll DRM mapper. Never null. */
  private DrmRotationMapper _rollMapper;
  /** Roll rate filter. Never null. */
  private RateInputDragFilter _rollFilter;
  /** Roll relative dragger. Never null. */
  private RelativeDragger _rollDragger;

  /** Pitch DRM mapper. Never null. */
  private DrmRotationMapper _pitchMapper;
  /** Pitch rate filter. Never null. */
  private RateInputDragFilter _pitchFilter;
  /** Pitch relative dragger. Never null. */
  private RelativeDragger _pitchDragger;

  /** Yaw DRM mapper. Never null. */
  private DrmRotationMapper _yawMapper;
  /** Yaw rate filter. Never null. */
  private RateInputDragFilter _yawFilter;
  /** Yaw relative dragger. Never null. */
  private RelativeDragger _yawDragger;

  /**
   * Constructs a AvatarCameraControl for a source display and target camera.
   * @param display Display serving as the event source for drags.
   *   Typically this is the display of the view that is attached to the camera.
   *   Null if none, but have to eventually add one to all draggers and cancelers.
   * @param camera Target fly camera.  Never null.
   */
  public AvatarCameraControl(Canvas3D display, AvatarCamera camera)
  {
    if (camera == null)
    {
      throw new IllegalArgumentException("'camera' is null.");
    }
    _camera = camera;

    // build thrust controls
    _thrustMapper = new DrmTranslationMapper(_camera.getThrustActuator(), true, DrmTranslationMapper.TRANSLATE_Y_Z, false);
    _thrustFilter = new AccelerateInputDragFilter(_thrustMapper);
    _thrustDragger = new RelativeDragger(
        display, _thrustFilter,
        Input.BUTTON_THIRD, Input.MODIFIER_NONE,
        Input.MODIFIER_NONE, new Vector2d(.005, .005),
        new Vector2d(.005, .005));

    /// limit thrust
    _thrustFilter.setRateClampY(new Vector2d( -vars.SPACENAV_LIMIT_SPEED_A, vars.SPACENAV_LIMIT_SPEED_B));

    /// thrust canceler
    _thrustCanceler = new AwtKeyboardModifierSensor(display, new InputCancelTrigger(_thrustFilter));
    _thrustCanceler.setModifiers(Input.MODIFIER_ALL_ESC);

    // build roll controls
    _rollMapper = new DrmRotationMapper(_camera.getRollActuator(), true, DrmRotationMapper.ROTATE_X_Z, false);
    _rollFilter = new RateInputDragFilter(_rollMapper);
    _rollDragger = new RelativeDragger(
        display, _rollFilter,
        Input.BUTTON_FIRST, Input.MODIFIER_SHIFT,
        Input.MODIFIER_IGNORE, new Vector2d(.005, .005),
        new Vector2d(.005, .005));

    // build pitch controls
    _pitchMapper = new DrmRotationMapper(_camera.getPitchActuator(), true, DrmRotationMapper.ROTATE_Y_X, true);
    _pitchFilter = new RateInputDragFilter(_pitchMapper);
    _pitchDragger = new RelativeDragger(
        display, _pitchFilter,
        Input.BUTTON_FIRST, Input.MODIFIER_CTRL,
        Input.MODIFIER_IGNORE, new Vector2d(.005, .005),
        new Vector2d(.005, .005));

    // build yaw controls
    _yawMapper = new DrmRotationMapper(_camera.getYawActuator(), true, DrmRotationMapper.ROTATE_X_Y, true);
    _yawDragger = new RelativeDragger(
        display, _yawMapper,
        Input.BUTTON_FIRST, Input.MODIFIER_CTRL,
        Input.MODIFIER_IGNORE, new Vector2d(.01, .01),
        new Vector2d(.01, .01));

    //?? limit view movements
    //_camera.getRollActuator().getPlugin().setTargetClamp(Mapper.DIM_ANGLE, new Vector2d(vars.SPACENAV_LIMIT_PITCH_DN, vars.SPACENAV_LIMIT_PITCH_UP));
    //_camera.getPitchActuator().getPlugin().setTargetClamp(Mapper.DIM_Y, new Vector2d(vars.SPACENAV_LIMIT_PITCH_DN, vars.SPACENAV_LIMIT_PITCH_UP));
    //_camera.getYawActuator().getPlugin().setTargetClamp(Mapper.DIM_ANGLE, new Vector2d(vars.SPACENAV_LIMIT_PITCH_UP, vars.SPACENAV_LIMIT_PITCH_DN));

  }


  public Vector3d getPosition()
  {
    //not used

    //System.out.println("> "+_thrustMapper.getActuator().getPlugin().getSourceOffset().toString());

    Transform3D t = new Transform3D();
    Vector3d v = null;
    /*
    try
    {
      _thrustMapper.getActuator().getPlugin().getSourceOffset().getLocalToVworld(t);
      t.get(v);
    }
    catch(Exception e)
    {
      System.err.println("Ehhy");
    }
*/
    return v;
  }

  public Tuple4d getHeading()
  {
    //not used
    return (Tuple4d)_camera.getYawActuator().getPlugin().getSourceOffset();
  }

  public Vector2d getSpeed()
  {
    return _thrustFilter.getRate(new Vector2d());
  }

  /**
    Gets the thrust mapper.
    @return Reference to the thrust mapper.
   */
  public DrmTranslationMapper getThrustMapper()
  {
    return _thrustMapper;
  }

  /**
    Gets the thrust dragger.
    @return Reference to the thrust dragger.
   */
  public RelativeDragger getThrustDragger()
  {
    return _thrustDragger;
  }

  /**
    Gets the thrust canceler.
    @return Reference to the thrust canceler sensor.
   */
  public AwtKeyboardModifierSensor getThrustCanceler()
  {
    return _thrustCanceler;
  }

  /**
    Gets the roll mapper.
    @return Reference to the roll mapper.
   */
  public DrmRotationMapper getRollMapper()
  {
    return _rollMapper;
  }

  /**
    Gets the roll dragger.
    @return Reference to the roll dragger.
   */
  public RelativeDragger getRollDragger()
  {
    return _rollDragger;
  }

  /**
    Gets the pitch mapper.
    @return Reference to the pitch mapper.
   */
  public DrmRotationMapper getPitchMapper()
  {
    return _pitchMapper;
  }

  /**
    Gets the pitch dragger.
    @return Reference to the pitch dragger.
   */
  public RelativeDragger getPitchDragger()
  {
    return _pitchDragger;
  }

  /**
    Gets the yaw mapper.
    @return Reference to the yaw mapper.
   */
  public DrmRotationMapper getYawMapper()
  {
    return _yawMapper;
  }

  /**
    Gets the yaw dragger.
    @return Reference to the yaw dragger.
   */
  public RelativeDragger getYawDragger()
  {
    return _yawDragger;
  }
  public AccelerateInputDragFilter get_thrustFilter()
  {
    return _thrustFilter;
  }

}
