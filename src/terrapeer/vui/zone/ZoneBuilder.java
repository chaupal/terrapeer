package terrapeer.vui.zone;

import terrapeer.*;
import terrapeer.vui.*;
import terrapeer.vui.space.*;
import terrapeer.vui.helpers.*;
import terrapeer.vui.j3dui.utils.*;
import terrapeer.vui.j3dui.utils.app.*;
import terrapeer.vui.j3dui.utils.objects.*;
import terrapeer.vui.j3dui.utils.blocks.*;
import terrapeer.vui.j3dui.control.*;
import terrapeer.vui.j3dui.control.inputs.*;
import terrapeer.vui.j3dui.control.inputs.sensors.*;
import terrapeer.vui.j3dui.control.mappers.*;
import terrapeer.vui.j3dui.control.mappers.intuitive.*;
import terrapeer.vui.j3dui.control.actuators.*;
import terrapeer.vui.j3dui.control.actuators.groups.*;
import terrapeer.vui.j3dui.feedback.*;
import terrapeer.vui.j3dui.feedback.elements.*;
import terrapeer.vui.j3dui.visualize.*;
import terrapeer.vui.j3dui.visualize.change.*;
import terrapeer.vui.j3dui.utils.blocks.SecondBlocks.*;

import java.util.*;
import java.net.URL;
import javax.vecmath.*;
import javax.media.j3d.*;
import terrapeer.vui.helpers.*;
import java.awt.Font;


/**
 * Responsible for keeping and building my Zone.
 *
 * Load or Create existing zone
 * Add/change/model objects, services
 *
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */
public class ZoneBuilder
{
  private static TerraPeerLog myLog = TerraPeerLog.getLogger();

  private static boolean zoneExists = false;
  private static String myZoneID = null;
  private static ZoneRepository builderRepository = null;

  private Zone myZone = null;
  private AppWorld spaceWorld = null;
  private AppView spaceView_avat = null;
  private AppView spaceView_x = null;
  private SpaceCore spaceCore = null;
  private ZoneWorld myZoneWorld = null;
  private UISceneTree zone_tree = null;

  StatusTrigger trigger = null;

  //user interface
  private FeedbackGroupManager groupMgr = null;
  private ViewChangeSplitter viewSpltr = null;
  private BasePlane myZoneRoom = null;
  private InputDragSplitter absDrag = null;
  private InputDragSplitter relDrag = null;

  /**
   * The ZoneBuilder constructor sets SpaceCore, SpaceWorld, and ZoneWorld
   * @param spaceCore SpaceCore
   * @param spaceWorld AppWorld
   * @param myZoneWorld ZoneWorld
   */
  public ZoneBuilder(SpaceCore spaceCore, AppWorld spaceWorld, AppView spaceView_avat, ZoneWorld myZoneWorld, UISceneTree zone_tree)
  {
    myLog.addMessage(4, "Initializing ZoneBuilder...");
    this.spaceCore = spaceCore;
    this.spaceWorld = spaceWorld;
    this.spaceView_avat = spaceView_avat;
    this.myZoneWorld = myZoneWorld;
    this.zone_tree = zone_tree;

    spaceView_x = new AppView();

    builderRepository = new ZoneRepository();
    myZoneID = builderRepository.loadMyZoneID();

    setupWorld();
  }

  /**
   * Setup world related stuff (view, IO)
   */
  private void setupWorld()
  {
    myLog.addMessage(4, "   Setting up World...");
    buildView();
    //buildZone();
    buildIO();
    //buildTarget();
  }

  public boolean zoneExists()
  {
    return zoneExists;
  }

  private String generateZoneID()
  {
    myLog.addMessage(4, "ZoneBuilder: Generating New Zone ID...");
    Calendar c = Calendar.getInstance();
    String zid = vars.ZONE_ID_START
                 + ":T-" + c.get(Calendar.YEAR) + "." + c.get(Calendar.MONTH) + "." + c.get(Calendar.DAY_OF_MONTH)
                 + "." + c.get(Calendar.HOUR_OF_DAY) + "." + c.get(Calendar.MINUTE) + "." + c.get(Calendar.SECOND)
                 + ":PID-" + vars.JXTA_PEER_ID + "." + vars.JXTA_PEER_NAME;
    return zid;
  }

  /**
   * Retrieve MyZone data based on ZoneID, if either exists
   * Else create new Zone
   * @return Zone MyZone
   */
  public Zone getMyZone()
  {
    myLog.addMessage(4, "ZoneBuilder: Retrieving My Zone...");
    if(!zoneExists)
      if(!loadMyZone())
        if(!createNewZone())
          return null;
    myLog.addMessage(4, "      MyZone ID: "+myZone.getZone_ID());
    myLog.addMessage(4, "      MyZone Name: "+myZone.getZone_Name());
    return this.myZone;
  }


  /**
   * Build all Zones (first My Zone, then ZoneWorld)
   * Calls initZoneObjects() and buildZone(), which creates a 'room' or the
   * ground-level plane.
   * Then adds targets (onjetcs) to that plane.
   */
  public void buildAllZones()
  {
    Vector3d v3d = null;

    //build my zone
    myLog.addMessage(4, "ZoneBuilder: Building My Zone...");
    initZoneObjects(this.myZone);
    v3d = this.myZone.myGeometry.getPosition();
    this.myZoneRoom = buildZone(v3d,
                                this.myZone.myGeometry.getTwoPoint_W(),
                                this.myZone.myGeometry.getTwoPoint_H(),
                                vars.ZONE_DEFAULT_MYCOLOR);

    //???what the...?  v3d.y += 3;
    addTarget(this.myZoneRoom,
              build3DText(this.myZone.getZone_Name()+" ["+this.myZone.getZone_ID()+"]"),
              v3d);

    int nodeCount = this.myZone.myObjects.getVObjectCount();
    for(int n=0; n < nodeCount; n++)
    {
      //Node thing = builderRepository.loadObject("desk.wrl");
      //PickUtils.allowSetGeoPickable(this.myZone.myObjects.getVObject(n).getNode());
      //PickUtils.setGeoPickable(this.myZone.myObjects.getVObject(n).getNode(), true);

      addMovableTarget(this.myZone.myGeometry,
                       this.myZoneRoom,
                       this.myZone.myObjects.getVObject(n).getNode(),
                       this.myZone.myObjects.getVObject(n).getPosition());
    }

    buildZoneWorld();
  }

  /**
   * Build all ZoneWorld Zones
   */
  public void buildZoneWorld()
  {
    Vector3d v3d = null;

    //build other zones
    myLog.addMessage(4, "ZoneBuilder: Building ZoneWorld...");
    Vector zones = myZoneWorld.getZones();
    int zoneCount = zones.size();
    Zone z = null;
    BasePlane zoneRoom = null;
    for (int i = 0; i < zoneCount; i++)
    {
      z = (Zone)zones.get(i);
      initZoneObjects(z);
      v3d = z.myGeometry.getPosition();
      zoneRoom = buildZone(v3d,
                           z.myGeometry.getTwoPoint_W(),
                           z.myGeometry.getTwoPoint_H(),
                           vars.ZONE_DEFAULT_COLOR);

      v3d.y += 3;
      addTarget(zoneRoom,
                build3DText(z.getZone_Name()+" ["+z.getZone_ID()+"]"),
                v3d);

      int nodeCount = z.myObjects.getVObjectCount();
      for(int n=0; n < nodeCount; n++)
      {
        //PickUtils.allowSetGeoPickable(z.myObjects.getVObject(n).getNode());
        //PickUtils.setGeoPickable(z.myObjects.getVObject(n).getNode(), true);
        //PickUtils.setLeafPickable(z.myObjects.getVObject(n).getNode(), true);
        //z.myObjects.getVObject(n).getNode().setCapability(Node.ALLOW_AUTO_COMPUTE_BOUNDS_READ);
        //z.myObjects.getVObject(n).getNode().setCapability(Node.ALLOW_BOUNDS_READ);
        //z.myObjects.getVObject(n).getNode().setCapability(Node.ALLOW_COLLIDABLE_READ);
        //z.myObjects.getVObject(n).getNode().setCapability(Node.ALLOW_LOCAL_TO_VWORLD_READ);
        //z.myObjects.getVObject(n).getNode().setCapability(Node.ALLOW_PICKABLE_READ);

        addTarget(zoneRoom,
                  z.myObjects.getVObject(n).getNode(),
                  z.myObjects.getVObject(n).getPosition());
      }
    }
  }

  private BranchGroup build3DText(String txt)
  {

    Font3D f3d = new Font3D(vars.ZONE_TEXT_FONT, new FontExtrusion());
    Text3D t3d = new Text3D(f3d);
    t3d.setString(txt);

    // Define a generic shaded appearance
    Appearance app = new Appearance();
    Material mat = new Material();
    mat.setLightingEnable(true);
    app.setMaterial(mat);

    Shape3D s3d = new Shape3D(t3d, app);
    s3d.setCapability( Shape3D.ALLOW_GEOMETRY_WRITE );

    // Build a branch group to hold the text shape
    // (this allows us to remove the text shape later,
    // change it, then put it back)
    BranchGroup textGroup = new BranchGroup();
    textGroup.setCapability(BranchGroup.ALLOW_DETACH);
    textGroup.addChild( s3d );

    return textGroup;
  }

  /**
   * Initializes all objects within a zone.
   * Is called before buildZone() to load all objects and place them
   * into the zone.
   * @param z Zone The Zone where objects should be loaded
   */
  private void initZoneObjects(Zone z)
  {
    myLog.addMessage(4, "ZoneBuilder: Initializing all Objects within Zone: "+z.getZone_Name());
    int objCount = z.myObjects.getVObjectCount();
    myLog.addMessage(4, "      Object count: "+objCount);
    Node n = null;
    int bb = 1;
    for(int i=0; i < objCount; i++)
    {
      bb = z.myObjects.getVObject(i).getBbType();
      myLog.addMessage(4, "      Loading Object Type "+bb);
      n = builderRepository.loadObject(vars.OBJECT_FILES[bb]);
      z.myObjects.getVObject(i).setNode(n);
    }
  }

  private boolean loadMyZone()
  {
    myLog.addMessage(4, "ZoneBuilder: Loading My Zone...");
    if((!zoneExists) && (myZoneID!=null))
    {
      myZone = builderRepository.loadMyZoneFromFile();
      if(myZone!=null)
      {
        myLog.addMessage(4, "      Zone Loaded.");
        zoneExists = true;
        return true;
      }
    }
    myLog.addMessage(4, "      No Zone Found.");
    return false;
  }

  /**
   * Create a new Home Zone (MyZone)
   * @return boolean
   */
  private boolean createNewZone()
  {
    myLog.addMessage(4, "   Creating New MyZone");
    myZone = new Zone(generateZoneID());
    if(myZone!=null)
    {
      zoneExists = true;
      return true;
    }
    return false;
  }

  public boolean validateZone(Zone z)
  {
    myLog.addMessage(4, "ZoneBuilder: Validating Zone...");
    if(z == null)
      return false;
    if(z.getGlobalState() == vars.STATE_ERR)
      return false;
    else
      return true;
  }

  public void setZoneGeometrySimple(int NWPointX, int NWPointY, int SEPoint_X, int SEPointY)
  {
    myZone.myGeometry.set2Points(new Vector3d(NWPointX, NWPointY, 0), new Vector3d(SEPoint_X, SEPointY, 0));
  }

  /**
   * Add a new building block (VObject) to my zone.
   * Placed at myZone.myGeometry.getPositionCoords()
   * @param bbType int
   */
  public void addBuildingBlock(int bbType)
  {
    Node thing = builderRepository.loadObject(vars.OBJECT_FILES[bbType]);
    addMovableTarget(this.myZone.myGeometry,
                     myZoneRoom,
                     thing,
                     myZone.myGeometry.getPosition());

    //"TERRA-Z000"+objZone.getZone_ID()+"-B-"+xmlZone.getBaseObjectAt(o).getBBTYPE().getValue()+"-"+o

    ///!!
  }

  public String addURLServiceToZone(String name, Vector3d servicePos, Vector3d zonePos, String urlStr)
  {
    String serviceID = myZone.myServices.addURLService(name, servicePos, zonePos, urlStr);
    return serviceID;
  }

  private void buildView()
  {
    // build view change sensor
    viewSpltr = new ViewChangeSplitter();
    new ViewExternalChangeSensor(viewSpltr, spaceView_avat);
    new ViewInternalChangeSensor(viewSpltr, spaceView_avat);

  }

  private void buildIO()
  {
    // build target draggers
    // absolute draggers (for WRM slide)
    absDrag = new InputDragSplitter();
    ActuationBlocks.buildAbsoluteDragger(absDrag, spaceView_avat,
                                         Input.BUTTON_FIRST,
                                         Input.MODIFIER_NONE,
                                         Input.MODIFIER_NONE);

    // relative draggers (for direct rotate & lift)
    relDrag = new InputDragSplitter();
    ActuationBlocks.buildRelativeDragger(relDrag, spaceView_avat,
                                         Input.BUTTON_FIRST,
                                         Input.MODIFIER_NONE,
                                         Input.MODIFIER_NONE);
  }

  /**
   * Creates a visual base plane 'room' that represents the virtual Zone.
   * @param position Vector4d
   * @param width int
   * @param height int
   * @param color Color3f
   * @return BasePterrapeer.vui.helpers.BasePlane*/
  private BasePlane buildZone(Vector3d position, int width, int height, Color3f color)
  {
    myLog.addMessage(4, "   Building Zone BasePlane: "+width+"x"+height);
    myLog.addMessage(4, "   Zone Position: "+position.x+","+position.y+","+position.z);
    // build zone with floor
    //zoneRoom = new SecondBlocks.Room(new Vector3d(100, 10, 100));
    //spaceWorld.addSceneNode(zoneRoom);

    // build floor plane
    BasePlane zoneRoom = new BasePlane(color, width, height);
    AffineGroup affine = new AffineGroup(zoneRoom);
    //check actuation start value...?
    Vector4d v4pos = new Vector4d(position.x, position.y, position.z, 0);
    affine.getTranslation().initActuation(v4pos);
    affine.getAxisAngle().initActuation(new Vector4d(1, 0, 0, -Math.PI / 2.0));
    spaceWorld.addSceneNode(affine);
    return zoneRoom;
  }

  private void buildMapping(BasePlane zoneRoom, AffineGroup slide, AffineGroup rotate, AffineGroup lift)
  {
    myLog.addMessage(4, "      Building Mapping");

    //spaceWorld.setLive(false);

    /// manipulation mappers
    InputDragTarget mapper;
    Vector2d rotateScale = new Vector2d(.025, .025);
    Vector2d liftScale = new Vector2d(.025, .025);

    //// WRM slide
    mapper = IntuitiveBlocks.buildStickyWrmTranslationMapper(zoneRoom, slide, spaceWorld.getSceneRoot(), true, true);
    absDrag.addEventTarget(mapper);

    //// direct rotate
    mapper = ActuationBlocks.buildDirectMapper(rotate.getAxisAngle(), rotateScale, Mapper.DIM_W, Mapper.DIM_NONE, true);
    rotate.getAxisAngle().getPlugin().setSourceOffset( new Vector4d(0, 1, 0, 0));
    relDrag.addEventTarget(mapper);

    //// direct lift
    lift.getTranslation().initActuation(new Vector4d(0, 2, 0, 0));
    mapper = ActuationBlocks.buildDirectMapper(lift.getTranslation(), liftScale, Mapper.DIM_NONE, Mapper.DIM_Y, true);
    relDrag.addEventTarget(mapper);


    // build feedback manager
    groupMgr = FeedbackBlocks.buildGroupManager(spaceView_avat.getDisplay(), spaceWorld.getViewRoot());

    /// use room for deselect
    //trigger = FeedbackBlocks.buildTargetFeedbackSupport(zoneRoom, groupMgr, relDrag, spaceView_avat.getDisplay(), spaceWorld.getSceneRoot());

    //spaceWorld.setLive(true);
  }

  /**
   * Add visual object (target) to ZoneRoom. Used for MyZone or Zones current user has access to.
   * @param zoneRoom BasePlane
   * @param thing Node
   */
  private void addMovableTarget(ZoneGeometry zoneGeo, BasePlane zoneRoom, Node thing, Vector3d position)
  {
    myLog.addMessage(4, "   Adding Movable Target");
    myLog.addMessage(4, "      Object Pos: "+position.x+","+position.y+","+position.z+"");

    spaceWorld.setLive(false);

    AffineGroup slide = new AffineGroup();
    AffineGroup rotate = new AffineGroup();
    AffineGroup lift = new AffineGroup();

    //SecondBlocks.FloorTarget target = new SecondBlocks.FloorTarget(thing, zoneRoom, viewSpltr, absDrag, relDrag, groupMgr, spaceWorld, spaceView_x);
    MultiShape target0 = FeedbackBlocks.buildMultiFloorTarget(thing, slide, rotate, lift, spaceView_avat, 1);
    spaceWorld.addSceneNode(target0);

    Vector4d v4pos = new Vector4d(position.x, position.y, position.z, 0);
    slide.getTranslation().initActuation(v4pos);

    // build floor target chain and decorations
    // (proxyTarget also serves as the poster trigger)
    Point3d min = new Point3d();
    Point3d max = new Point3d();

    /// get target bounds (before connecting target)
    //BoundingBox bbox = BoundsUtils.getBoundingBox(zoneRoom, new BoundingBox());
    //bbox.getLower(min);
    //bbox.getUpper(max);

    // setup manipulation actuators
    /// limit slide relative to room and bounds
    slide.getTranslation().getPlugin().setTargetClamp(
        Mapper.DIM_X, new Vector2d( zoneGeo.getTwoPoint_X1(), zoneGeo.getTwoPoint_X2() ) );
      //-zoneRoom.getSize().x / 2.0 - min.x, zoneRoom.getSize().x / 2.0 - max.x));
    slide.getTranslation().getPlugin().setTargetClamp(
        Mapper.DIM_Z, new Vector2d( zoneGeo.getTwoPoint_Y1(), zoneGeo.getTwoPoint_Y2() ) ); //-zoneRoom.getSize().z / 2.0 - min.z, zoneRoom.getSize().z / 2.0 - max.z));

    slide.getTranslation().getPlugin().setTargetClamp(
        Mapper.DIM_Y, new Vector2d(0, 0));

    /// limit lift relative to room and bounds
    lift.getTranslation().getPlugin().setTargetClamp(
        Mapper.DIM_Y, new Vector2d(0, 20));//room.getSize().y - max.y));

    lift.getTranslation().getPlugin().setTargetClamp(
        Mapper.DIM_X | Mapper.DIM_X, new Vector2d(0, 0));

    //slide.getTranslation().initActuation(new Vector4d(0, 0.5, -2, 0));

    buildMapping(zoneRoom, slide, rotate, lift);
    //spaceWorld.addSceneNode(spaceView_x);

    groupMgr = FeedbackBlocks.buildGroupManager(spaceView_avat.getDisplay(), spaceWorld.getViewRoot());
    trigger = FeedbackBlocks.buildTargetFeedbackSupport(target0, groupMgr, relDrag, spaceView_avat.getDisplay(), spaceWorld.getSceneRoot());
    FeedbackBlocks.buildTargetSelectEnablers(target0, trigger, slide.getTranslation(), rotate.getAxisAngle(), lift.getTranslation());
    groupMgr.initTargets(null, Feedback.STATUS_DISABLE, Feedback.SELECT_NORMAL, Feedback.ACTION_NORMAL);

    //target.getLift().initActuation(new Vector4d(0, -0.02, 0, 0));
    //zone_tree.updateTree(spaceWorld.getSceneRoot());

    if(spaceCore.isWorldFinalized)
      spaceWorld.setLive(true);
  }


  /**
   * Add visual object to ZoneRoom
   * @param zoneRoom BasePlane
   * @param thing Node
   * @param position Vector4d
   */
  private void addTarget(BasePlane zoneRoom, Node thing, Vector3d position)
  {
    myLog.addMessage(4, "   Adding Target");
    myLog.addMessage(4, "      Object Pos: "+position.x+","+position.y+","+position.z+" ");

    spaceWorld.setLive(false);

    /*
    SecondBlocks.FloorTarget target = new SecondBlocks.FloorTarget(thing, zoneRoom1, viewSpltr, absDrag, relDrag, groupMgr, spaceWorld, spaceView_avat);
    target.getSlide().initActuation(new Vector4d(1.2, 0, .05, 0));
    target.getLift().initActuation(new Vector4d(0, .55-0.02, 0, 0));
*/
    AffineGroup slide = new AffineGroup();
    AffineGroup rotate = new AffineGroup();
    AffineGroup lift = new AffineGroup();

    MultiShape target = FeedbackBlocks.buildMultiFloorTarget(thing, slide, rotate, lift, spaceView_avat, 1);
    target.setPickable(false);//true);

    spaceWorld.addSceneNode(target);

    Vector4d v4pos = new Vector4d(position.x, position.y, position.z, 0);
    slide.getTranslation().initActuation(v4pos);

    buildMapping(zoneRoom, slide, rotate, lift);
    //spaceWorld.addSceneNode(spaceView_x);

    groupMgr = FeedbackBlocks.buildGroupManager(spaceView_avat.getDisplay(), spaceWorld.getViewRoot());
    trigger = FeedbackBlocks.buildTargetFeedbackSupport(target, groupMgr, relDrag, spaceView_avat.getDisplay(), spaceWorld.getSceneRoot());
    FeedbackBlocks.buildTargetSelectEnablers(target, trigger, slide.getTranslation(), rotate.getAxisAngle(), lift.getTranslation());
    groupMgr.initTargets(null, Feedback.STATUS_DISABLE, Feedback.SELECT_NORMAL, Feedback.ACTION_NORMAL);

    //spaceWorld.addSceneNode(target);
    //zone_tree.updateTree(spaceWorld.getSceneRoot());
    if(spaceCore.isWorldFinalized)
      spaceWorld.setLive(true);
  }
  public ZoneRepository getBuilderRepository()
  {
    return builderRepository;
  }

}
