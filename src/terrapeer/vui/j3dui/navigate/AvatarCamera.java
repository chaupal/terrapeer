package terrapeer.vui.j3dui.navigate;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.utils.objects.*;
import terrapeer.vui.j3dui.control.mappers.*;
import terrapeer.vui.j3dui.control.actuators.*;
import terrapeer.vui.j3dui.control.actuators.groups.*;

/**
 *  Creates an actuator group that serves as a camera simulation.
 * The camera contains an actuator group chain with
 * thrust translation and attitude (roll, pitch, yaw) spherical
 * angle groups, in that order from group head to tail.
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */
public class AvatarCamera extends ActuatorGroup
{
  /** Thrust group. Never null. */
  private AffineGroup _thrust;
  /** Attitude group. Never null. */
  private AxisAngleSphereGroup _attitude;

  public AvatarCamera()
  {
    this(null);
  }

  /**
    Constructs a AvatarCamera with a child node.
    @param node First child node of this camera.  Typically the
    view.  Null if none.
   */
  public AvatarCamera(Node node)
  {
    // empty chain
    super(0, null);

    // build group chain
    Group[] chain = new Group[2];

    chain[1] = _attitude = new AxisAngleSphereGroup(node);
    chain[0] = _thrust = new AffineGroup(_attitude);

    setChain(chain, 2);
  }

  /**
    Gets the thrust actuator (see TranslationPlugin
    for actuation value definition), which is used to position
    the camera view.
    @return Reference to the actuator.
   */
  public Actuator getThrustActuator()
  {
    return _thrust.getTranslation();
  }

  /**
    Gets the attitude roll actuator (see AxisAnglePlugin
    for actuation value definition), which is used to roll
    the camera view about its Z axis.
    @return Reference to the actuator.
   */
  public Actuator getRollActuator()
  {
    return _attitude.getAxisAngleZ();
  }

  /**
    Gets the attitude pitch actuator (see AxisAnglePlugin
    for actuation value definition), which is used to pitch
    the camera view about its X axis.
    @return Reference to the actuator.
   */
  public Actuator getPitchActuator()
  {
    return _attitude.getAxisAngleX();
  }

  /**
    Gets the attitude yaw actuator (see AxisAnglePlugin
    for actuation value definition), which is used to pitch
    the camera view about its Y axis.
    @return Reference to the actuator.
   */
  public Actuator getYawActuator()
  {
    return _attitude.getAxisAngleY();
  }

  /**
    Gets the thrust tail group, which can be used for adding
    children.
    @return Reference to the LFP group.
   */
  public Group getThrustGroup()
  {
    return _thrust.getTail();
  }

  /**
    Gets the attitude tail group, which can be used for adding
    children.  Since this is the tail of this group, adding a
    child to it has the same effect as using addNode().
    @return Reference to the attitude group.
   */
  public Group getAttitudeGroup()
  {
    return _attitude.getTail();
  }


  // ActuatorGroup implementation
  protected void setEnableGroup(boolean enable)
  {
    _thrust.setEnable(enable);
    _attitude.setEnable(enable);
  }

}
