package terrapeer.vui.space;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.io.*;

import terrapeer.*;
import terrapeer.vui.*;
import terrapeer.vui.zone.*;
import terrapeer.vui.j3dui.utils.*;
import terrapeer.vui.j3dui.utils.app.*;
import terrapeer.vui.j3dui.utils.blocks.*;
import terrapeer.vui.j3dui.utils.objects.*;
import terrapeer.vui.j3dui.control.*;
import terrapeer.vui.j3dui.control.inputs.*;
import terrapeer.vui.j3dui.control.mappers.*;
import terrapeer.vui.j3dui.control.actuators.groups.*;
import terrapeer.vui.j3dui.control.actuators.*;
import terrapeer.vui.j3dui.feedback.*;
import terrapeer.vui.j3dui.feedback.elements.*;
import terrapeer.vui.j3dui.navigate.*;
import terrapeer.vui.j3dui.visualize.change.*;
import terrapeer.vui.j3dui.visualize.DisplayOverlayGroup;
import terrapeer.vui.j3dui.visualize.DisplayOverlayRoot;

//import com.Ostermiller.util.*;

/**
 *
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */
public class SpaceNav
{
  private static TerraPeerLog myLog = TerraPeerLog.getLogger();

  private static SpaceCore spaceCore = null;
  private static AppWorld spaceWorld = null;
  private static AppView spaceView_grid = null;
  private static AppView spaceView_orbit = null;
  private static AppView spaceView_avat = null;

  private static OrbitCamera gridCamera = null;
  private static AvatarCamera avatCamera = null;
  private static OrbitCamera orbitCamera = null;
  private static AvatarCameraControl avatControl = null;
  private static OrbitCameraControl gridControl = null;
  private static OrbitCameraControl orbitControl = null;

  private static Vector3d Avatar_Position = new Vector3d(0,0,0);
  private static double Avatar_HeadingYaw = 0.0;
  private static double Avatar_HeadingPitch = 0.0;
  private static double Avatar_HeadingRoll = 0.0;
  private static double Avatar_Speed = 0.0;
  private static int Avatar_Level = 0;

  private Node myPointer;

  public SpaceNav(SpaceCore spaceCore, AppWorld spaceWorld, AppView spaceView_grid, AppView spaceView_orbit, AppView spaceView_avat)
  {
    this.spaceCore = spaceCore;
    this.spaceWorld = spaceWorld;
    this.spaceView_grid = spaceView_grid;
    this.spaceView_orbit = spaceView_orbit;
    this.spaceView_avat = spaceView_avat;
  }

  public void setupNavigation(int spaceView)
  {
    switch(spaceView)
    {
      case vars.SPACEVIEW_GRID:
        setupGridNav();
        break;
      case vars.SPACEVIEW_AVATAR:
        setupAvatarNav();
        break;
      case vars.SPACEVIEW_ORBIT_1:
        setupOrbitNav();
        break;
      case vars.SPACEVIEW_ORBIT_2:
        setupOrbitNav();
        break;
    }
  }

  public void updateNavigation()
  {
    spaceWorld.getViewRoot().removeAllChildren();
    spaceWorld.addViewNode(gridCamera);
    spaceWorld.addViewNode(avatCamera);

  }

  private void setupGridNav()
  {
    // configure views for large area
    spaceView_grid.setViewClipDist(new Vector2d(.1, 200));
    gridCamera = new OrbitCamera(spaceView_grid);
    gridControl = new OrbitCameraControl(spaceView_grid.getDisplay(), gridCamera);

    /// start looking from the top
    gridCamera.getOrbitActuator().initActuation(new Vector4d( -Math.PI / 2, -Math.PI, 0, 0));

    //limit movement

    gridCamera.getLapActuator().getPlugin().setTargetClamp(Mapper.DIM_ANGLE, new Vector2d(-Math.PI / 2, -Math.PI / 2));
    gridCamera.getLapActuator().getPlugin().setTargetClamp(Mapper.DIM_W, new Vector2d(0,0));

    /// attach camera to world
    spaceWorld.addViewNode(gridCamera);
  }

  private void setupAvatarNav()
  {
    spaceView_avat.setCapability(Group.ALLOW_CHILDREN_EXTEND);
    spaceView_avat.setCapability(Group.ALLOW_CHILDREN_READ);
    spaceView_avat.setCapability(Group.ALLOW_CHILDREN_WRITE);

    // configure views for large area
    spaceView_avat.setViewClipDist(new Vector2d(.1, 200));

    // build fly camera and controls
    avatCamera = new AvatarCamera(spaceView_avat);
    avatControl = new AvatarCameraControl(spaceView_avat.getDisplay(), avatCamera);

    /// attach camera to world
    spaceWorld.addViewNode(avatCamera);

    /// build avatar at LFP
    //flyView.addNode(spaceCore.myRepository.loadObject("plane.wrl"));//ModelLoader.loadModelFromName("plane.wrl"));

    /// limit movement to volume over the earth
    // GUI-convention Y is really internally Z
    avatCamera.getThrustActuator().getPlugin().setTargetClamp(Mapper.DIM_X | Mapper.DIM_Z, new Vector2d(-vars.SPACENAV_LIMIT_XY, vars.SPACENAV_LIMIT_XY));
    avatCamera.getThrustActuator().getPlugin().setTargetClamp(Mapper.DIM_Y, new Vector2d(vars.SPACENAV_LIMIT_DN, vars.SPACENAV_LIMIT_UP));

    /// start at the south looking north
    avatCamera.getThrustActuator().initActuation(vars.SPACENAV_POS_START);
    //avatCamera.getRollActuator().initActuation(vars.SPACENAV_STARTPOS);

    //build3DFeedbackPanel();
    build3DControlPanel();
  }

  private void setupOrbitNav()
  {
    // configure views for large area
    spaceView_orbit.setViewClipDist(new Vector2d(.1, 200));

    /// make cancel work from both views
    //flyControl.getThrustCanceler().addEventSource(orbitView.getDisplay());

    // build orbit camera and controls
    orbitCamera = new OrbitCamera(spaceView_orbit);
    orbitControl = new OrbitCameraControl(spaceView_orbit.getDisplay(), orbitCamera);

    /// build LAP thing
    AffineGroup lapThing = new AffineGroup(new TestThing());
    lapThing.getScale().initActuation(new Vector4d(.25, .25, .25, 0));
    orbitCamera.getLapGroup().addChild(lapThing);

    /// start looking from the south west
    orbitCamera.getOrbitActuator().initActuation(new Vector4d( -Math.PI / 6, -Math.PI / 8, 0, 0));

    /// attach camera to avat camera's unoriented LFP
    if(avatCamera!=null)
      avatCamera.getThrustGroup().addChild(orbitCamera);
  }

  private void build3DFeedbackPanel()
  {
    //not used

    // build view change sensor
    ViewChangeSplitter viewSpltr = new ViewChangeSplitter();

    new ViewExternalChangeSensor(viewSpltr, spaceView_avat);
    new ViewInternalChangeSensor(viewSpltr, spaceView_avat);

    int zPlane;
    Vector2d origin = new Vector2d();
    Vector2d offset = new Vector2d();

    DisplayOverlayRoot dispRoot = new DisplayOverlayRoot(spaceView_avat);
    viewSpltr.addEventTarget(dispRoot);
    DisplayOverlayGroup disp1 = new DisplayOverlayGroup(dispRoot);
    viewSpltr.addEventTarget(disp1);

    //// frontmost
    origin.set(.5, .5);
    offset.set(-60, -20);
    disp1.setRelativeOrigin(origin);
    disp1.setAbsoluteOffset(offset);
    zPlane = -4;
    Node thing = new TextureRect(true, new Color3f(1f, 1f, 1f), 0, Blocks.buildResourcePath("images/")+"pointer.gif", Elements.SIDE_TOP, 16, 32);
    //disp1.addNode(thing, zPlane);

    AffineGroup slide = new AffineGroup();
    AffineGroup rotate = new AffineGroup();
    AffineGroup lift = new AffineGroup();
    MultiShape target0 = FeedbackBlocks.buildMultiFloorTarget(thing, slide, rotate, lift, spaceView_avat, 1);
    disp1.addNode(target0, zPlane);
    rotate.getTranslation().initActuation(new Vector4d(20, 20, 10, 0));

    //// backmost
    origin.set(.5, .5);
    offset.set(-60, -60);
    disp1.setRelativeOrigin(origin);
    disp1.setAbsoluteOffset(offset);
    zPlane = -5;
    thing = new TextureRect(true, new Color3f(1f, 1f, 1f), 0, Blocks.buildResourcePath("images/")+vars.IMG_FILE[55], Elements.SIDE_TOP, 100, 100);
    disp1.addNode(thing, zPlane);
  }

  private void build3DControlPanel()
  {
    // build view change sensor
    ViewChangeSplitter viewSpltr = new ViewChangeSplitter();

    new ViewExternalChangeSensor(viewSpltr, spaceView_avat);
    new ViewInternalChangeSensor(viewSpltr, spaceView_avat);

    // build control panel
    Vector2d size = new Vector2d();
    Vector2d limits = new Vector2d();
    Vector2d origin = new Vector2d();
    Vector2d offset = new Vector2d();
    double scale;
    Node mark, back, frame;

    /// rotate control
    size.set(66, 66);
    limits.set(0, -1); // ignore limits
    scale = 1.0;
    origin.set( -.45, -.45);
    offset.set(size.x / 2.0, size.y / 2.0);

    mark = ThirdBlocks.buildMultiMark(true, 10, 10);
    back = ThirdBlocks.buildMultiRotateBack(true, size.x, size.y);
    frame = new TextureRect(false, new Color3f(.5f, .5f, .5f), .5, null, Elements.SIDE_TOP, size.x, size.y);

    ThirdBlocks.RotateControl rotate = new ThirdBlocks.RotateControl(mark, back, frame, origin, offset,
        new Vector2d( -21, 0), new Vector2d(0, 6), limits,
        scale, viewSpltr, spaceView_avat);

    /// lift control
    size.set(66, 66);
    limits.set(0, 38);
    scale = 20 / (limits.y - limits.x);
    origin.set(-.25, -.45);
    offset.set( -size.x / 2.0, size.y / 2.0);

    mark = ThirdBlocks.buildMultiMark(true, 10, 10);
    back = ThirdBlocks.buildMultiLiftBack(true, size.x, size.y);
    frame = new TextureRect(false, new Color3f(.5f, .5f, .5f), .5, null, Elements.SIDE_TOP, size.x, size.y);

    ThirdBlocks.LiftControl lift = new ThirdBlocks.LiftControl(mark, back, frame, origin, offset,
        new Vector2d(0, 0), new Vector2d(0, -12), limits,
        scale, viewSpltr, spaceView_avat);

    // connect controls and targets
    ActuationSplitter enabler;
    FeedbackTrigger filter;
    FeedbackEnableTrigger trigger;
  }

  public void moveAvatarForward(int speed)
  {
    //Transform3D t = new Transform3D();
    //spaceView_avat.getViewPlatform().getLocalToVworld(t);
    //t.transform(new Vector3d(0,0,-vars.SPACENAV_SPEED_STEP));
    //avatCamera.getThrustActuator().updateActuation(new Vector4d( 0, 0, vars.SPACENAV_SPEED_STEP, 0));
    Vector2d r = new Vector2d();
    avatControl.get_thrustFilter().getRate(r);
    r.set(vars.SPACENAV_SPEED_STEP*speed+vars.SPACENAV_SPEED_STEP, vars.SPACENAV_SPEED_STEP*speed+vars.SPACENAV_SPEED_STEP);
    avatControl.get_thrustFilter().setRate(r);
    myLog.addMessage(4, "SpaceNav: Set Avatar Rate to "+r.x+","+r.y);
  }

  public void moveAvatarBackward(int speed)
  {
    //vars.SPACENAV_SPEED_STEP;
    Vector2d r = new Vector2d();
    avatControl.get_thrustFilter().getRate(r);
    r.set(-vars.SPACENAV_SPEED_STEP*speed-vars.SPACENAV_SPEED_STEP, -vars.SPACENAV_SPEED_STEP*speed-vars.SPACENAV_SPEED_STEP);
    avatControl.get_thrustFilter().setRate(r);
    myLog.addMessage(4, "SpaceNav: Set Avatar Rate to "+r.x+","+r.y);
  }

  public void stopAvatar()
  {
    //spaceWorld.setLive(false);
    //orbitCamera.getOrbitActuator().getHeading().updateActuation(new Vector4d( 0, 0, 0, 0));
    //spaceWorld.setLive(true);
    //avatCamera.getThrustActuator().updateActuation(new Vector4d( 0, 0, 0, 0));
    //Tuple4d t = avatCamera.getThrustActuator().getPlugin().getSourceOffset();
    //t.negate();
    //avatCamera.getThrustActuator().updateActuation(t);

    avatControl.get_thrustFilter().setRate(new Vector2d(0, 0));
    myLog.addMessage(4, "SpaceNav: Stopped Avatar");
  }

  public void alignAvatar()
  {
    //avatCamera.getPitchActuator().initActuation(new Vector4d( 0, 0, 0, 0));
    //avatCamera.getYawActuator().initActuation(new Vector4d( 0, 0, 0, 1));
    avatCamera.getRollActuator().initActuation(new Vector4d( 0, 0, 0, 0));
    myLog.addMessage(4, "SpaceNav: Aligned Avatar");
  }

  public void northAvatar()
  {
    avatCamera.getYawActuator().initActuation(new Vector4d( 0, 0, 0, 0));
    myLog.addMessage(4, "SpaceNav: Avatar heading North");
  }

  /**
   * set camera view position to home zone
   */
  public void moveAvatarToHome(ZoneGeometry zoneGeo)
  {
    //spaceWorld.setLive(false);
    stopAvatar();
    double x = (zoneGeo.getTwoPoint_X2() + zoneGeo.getTwoPoint_X1() )/2.0;
    double y = zoneGeo.getTwoPoint_Y1() - 10.0;
    avatCamera.getThrustActuator().initActuation( new Vector4d(x, y, 10.0, 0.0) );
    avatCamera.getYawActuator().initActuation(vars.SPACENAV_YAW_HOME);
    //spaceWorld.setLive(true);
    myLog.addMessage(4, "SpaceNav: Moving Avatar Home");
  }

  /**
   * set camera view position to default view south-west towards origo
   */
  public void moveAvatarToDefault()
  {
    //spaceWorld.setLive(false);
    stopAvatar();
    avatCamera.getThrustActuator().initActuation(vars.SPACENAV_POS_START);
    avatCamera.getYawActuator().initActuation(vars.SPACENAV_YAW_START);
    //spaceWorld.setLive(true);
    myLog.addMessage(4, "SpaceNav: Moving Avatar to Default");
  }

  /**
   * set camera view position to origo
   */
  public void moveAvatarToOrigo()
  {
    //spaceWorld.setLive(false);
    stopAvatar();
    avatCamera.getThrustActuator().initActuation(vars.SPACENAV_POS_ORIGO_TOP);
    myLog.addMessage(4, "SpaceNav: Moving Avatar to Origo");
  }

  /**
   * set camera view position to north view, slightly south of origo
   */
  public void moveAvatarToOrigoNorth()
  {
    stopAvatar();
    avatCamera.getThrustActuator().initActuation(vars.SPACENAV_POS_ORIGO_NORTH);
    myLog.addMessage(4, "SpaceNav: Moving Avatar to Origo North view");
  }

  /**
   * set camera view position to origo top view
   */
  public void moveAvatarToOrigoTop()
  {
    stopAvatar();
    avatCamera.getThrustActuator().initActuation(vars.SPACENAV_POS_ORIGO_TOP);
    avatCamera.getRollActuator().initActuation(vars.SPACENAV_ROLL_ORIGO_TOP);
    myLog.addMessage(4, "SpaceNav: Moving Avatar to Origo Top view");
  }

  /**
   * turn view direction
   * @param deg int between 0-360 degrees
   */
  public void dirAvatarDegree(int deg)
  {
    double d = 1.0;
    d = Math.toRadians(deg); //deg * (2 * Math.PI) / 360;
    avatCamera.getYawActuator().initActuation(new Vector4d(0, 1, 0, d));
  }

  public void dirAvatarLeft()
  {
    avatCamera.getYawActuator().updateActuation(new Vector4d(0, 1, 0, vars.SPACENAV_DIR_STEP));
  }

  public void dirAvatarRight()
  {
    avatCamera.getYawActuator().updateActuation(new Vector4d(0, 1, 0, vars.SPACENAV_DIR_STEP));
  }

  /**
   * Retrieves current coordinates of Avatar. (0,0,0) to (XL,YL,ZL) where
   * L is the limit of space given by the default setting (vars).
   * @return Vector3d double values of X, Y, and Z position from Origo
   */
  public Vector3d getAvatarPostition()
  {
    //not used...
    //if(avatControl.getPosition()!=null)
    //  this.Avatar_Position = avatControl.getPosition();

    Transform3D t = new Transform3D();
    Vector3d v = new Vector3d();
    //Matrix4d m
    try
    {
      spaceView_avat.getViewPlatform().getLocalToVworld(t);
      t.get(v);
      this.Avatar_Position = v;
      //v.set(new Tuple3d(t.[3][0],t[3][1],t[3][2]));
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    return this.Avatar_Position;
  }

/*
  Latest try.. seems ok

    Transform3D t = new Transform3D();
    Vector3d v = new Vector3d();
    Matrix3d m = new Matrix3d();
    try
    {
      spaceView_avat.getViewPlatform().getLocalToVworld(t);
      t.get(m, v);
      this.Avatar_Position = v;
      //v.set(new Tuple3d(t.[3][0],t[3][1],t[3][2]));
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

        //System.err.print("Vx = "+SignificantFigures.format(v.x,2));
        //System.err.print("   Vy = "+SignificantFigures.format(v.y,2));
        //System.err.println("   Vz = "+SignificantFigures.format(v.z,2));
        //System.err.println("M0:  "+m.m00+"  "+m.m01+" "+m.m02);
        //System.err.println("M1:  "+m.m10+"  "+m.m11+" "+m.m12);
        //System.err.println("M2:  "+m.m20+"  "+m.m21+" "+m.m22);

        //heading?  _roll
        double h_i = m.m00 / m.m10;
        double h_a = Math.atan(h_i);
        double h_a2 = Math.atan2(m.m10, m.m00);
        double h_d = Math.toDegrees(h_a);
        double h_d2 = Math.toDegrees(h_a2);
        //roll?  _pitch
        double r_i = m.m21 / m.m22;
        double r_a = Math.atan(r_i);
        double r_d = Math.toDegrees(r_a);
        //pitch?  _heading
        double p_i = - m.m20;
        double p_a = Math.asin(p_i);
        double p_d = Math.toDegrees(p_a);
        //System.err.println("_h = "+h_a+"  h2 = "+h_a2);
        //System.err.println("_r = "+r_a);
        //System.err.println("_p = "+p_a);

        System.err.print("roll = "+SignificantFigures.format(h_d,2));
        System.err.print("   pitch = "+SignificantFigures.format(r_d,2));
        System.err.println("   heading = "+SignificantFigures.format(p_d,2));
        System.err.println("");

----
this didn't work

      System.err.println("Pitch: "+_camera.getPitchActuator().getPlugin().getSourceOffset().x
                         );
      System.err.println("Roll: "+_camera.getRollActuator().getPlugin().getSourceOffset().toString());
      System.err.println("Yaw: "+_camera.getYawActuator().getPlugin().getSourceOffset().toString());
      System.err.println("Thrust: X="+_camera.getThrustActuator().getPlugin().getSourceOffset().x
                         +" Y="+_camera.getThrustActuator().getPlugin().getSourceOffset().y );
*/

  /**
   * Retrieves the current yaw heading (space Z-axis angle) of the avatar.
   * @return double Value between -180.0 and 180.0
   */
  public double getAvatarHeadingYaw()
  {
    //Tuple4d h = avatControl.getHeading();
    //AxisAngle4d a = AxisAnglePlugin.fromActuation(h);
    //this.Avatar_HeadingHoriz = a.angle;
    // h.toString()=0  h.w=0  a.angle=0

    //Tuple4d h = avatControl.getYawMapper().getTheVec();
    //System.err.println("_V4: "+h.toString());
    //AxisAngle4d a = AxisAnglePlugin.fromActuation(h);
    //System.err.println("_AxisAnglePlugin: "+a.toString());
    //System.err.println("_ax = "+a.angle*a.x+"   ay = "+a.angle*a.y);

    Transform3D t = new Transform3D();
    Matrix3d m = new Matrix3d();
    try
    {
      spaceView_avat.getViewPlatform().getLocalToVworld(t);
      t.get(m);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    double p_i = - m.m20;
    double p_a = Math.asin(p_i);
    double p_d = Math.toDegrees(p_a);

    //p_d neg is yaw pos (-90...0...90 degrees from East to West both ways)
    this.Avatar_HeadingYaw = - p_d;

    //if counterclockwise, yaw neg => pn neg, E is - and W is +
    int pn = 1;
    if(Avatar_HeadingYaw<0)
      pn = -1;

    //for yaw (-180...0...180) requires add 90 when South, +/- depending on W/E
    //m00 +/- => N/S of X-axis
    if(m.m00<0)
      this.Avatar_HeadingYaw = (pn * 180) - Avatar_HeadingYaw;

    return this.Avatar_HeadingYaw;
  }

  /**
   * Retrieves the current pitch heading (space X-axis angle) of the avatar.
   * @return double Value between -180.0 and 180.0
   */
  public double getAvatarHeadingPitch()
  {
    //Tuple4d h = avatControl.getHeading();
    //this.Avatar_HeadingVert = h.w;

    Transform3D t = new Transform3D();
    Matrix3d m = new Matrix3d();
    try
    {
      spaceView_avat.getViewPlatform().getLocalToVworld(t);
      t.get(m);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    //double r_i = m.m21 / m.m22;
    //double r_a = Math.atan(r_i);
    //double r_d = Math.toDegrees(r_a);

    this.Avatar_HeadingPitch = Math.toDegrees(Math.atan2(m.m22, m.m21));
    return this.Avatar_HeadingPitch;
  }

  /**
   * Retrieves the current roll heading (space Y-axis angle) of the avatar.
   * @return double Value between -180.0 and 180.0
   */
  public double getAvatarHeadingRoll()
  {
    Transform3D t = new Transform3D();
    Matrix3d m = new Matrix3d();
    try
    {
      spaceView_avat.getViewPlatform().getLocalToVworld(t);
      t.get(m);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

    //angle = arctan( m00/m10 )
    this.Avatar_HeadingRoll = Math.toDegrees(Math.atan2(m.m10, m.m00));
    return this.Avatar_HeadingRoll;
  }

  public double getAvatarSpeed()
  {
    //?
    Vector2d s = avatControl.getSpeed();
    this.Avatar_Speed = (s.x + s.y ) /2 ;
    return this.Avatar_Speed;
  }

  public int getAvatarLevel()
  {
    return this.Avatar_Level;
  }

  public void changeZoom(int spaceView, int zoom)
  {
    switch(spaceView)
    {
      case vars.SPACEVIEW_GRID:
        //ScaleInputDragPlugin si = new ScaleInputDragPlugin(new Vector2d(0,100));
        //gridCamera.getLapActuator().updateActuation(new Vector4d(0, zoom*10, 0, 0));
        //gridCamera.getOrbitActuator()
        break;
      case vars.SPACEVIEW_AVATAR:
        break;
      case vars.SPACEVIEW_ORBIT_1:
        //gridCamera.getLfoActuator().updateActuation(new Vector4d(0, zoom*10, 0, 0));
        gridCamera.getOrbitActuator().updateActuation(new Vector4d(0, zoom*10, 0, 0));
        break;
      case vars.SPACEVIEW_ORBIT_2:
        break;
    }
  }

}
