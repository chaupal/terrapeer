package terrapeer.vui.space;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.*;
import terrapeer.vui.*;
import terrapeer.vui.zone.*;
import terrapeer.vui.j3dui.utils.Debug;
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
import terrapeer.vui.helpers.*;
import terrapeer.vui.service.*;

/**
 * MySpace
 *   { AppWorld spaceWorld }
 *   { AppView Space Views: Grid, AvatZone, OrbitZone }
 *   { UISceneTree }
 *   { SpaceCore }
 *   { ZoneBuilder }
 *   { ZoneWorld }
 *   { Zone myZone }
 *
 * Main GUI for Space
 *
 * User interface:
 *  - Setup Space
 *  - Start and display VUI
 *
 *
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P feedback system</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */
public class MySpace //extends JPanel
{
  private static TerraPeerLog myLog = TerraPeerLog.getLogger();

  private boolean spaceInitialized = false;
  private boolean currentVPP = true;

  //virtual world
  private static AppWorld spaceWorld = null;

  //window viewers
  private static AppView spaceView_grid = null;
  private static AppView spaceView_avat = null;
  private static AppView spaceView_orbitAvat = null;

  //window tree
  private static UISceneTree zone_tree = null;

  //Space
  public static SpaceCore spaceCore = null;

  //Zone
  public static ZoneBuilder zoneBuilder = null;
  public static ZoneWorld myZoneWorld = null;
  public static Zone myZone = null;

  //GUI
  private javax.swing.JPanel gridViewPanel = null;
  private javax.swing.JPanel avatViewPanel = null;
  private javax.swing.JPanel orbitViewPanel = null;
  private javax.swing.JPanel mapViewPanel = null;


  public MySpace()
  {
  }

  public void initSpace(UISceneTree tree, javax.swing.JPanel gridViewPanel, javax.swing.JPanel avatViewPanel, javax.swing.JPanel orbitViewPanel, javax.swing.JPanel mapViewPanel)
  {
    myLog.addMessage(4, "MySpace: Initializing Space...");
    //init 3D world and views
    spaceWorld = new AppWorld();
    spaceView_grid = new AppView();
    spaceView_avat = new AppView();
    spaceView_orbitAvat = new AppView();
    myLog.addMessage(4, "   3D World and Views set.");

    //init GUI
    zone_tree = tree;
    this.gridViewPanel = gridViewPanel;
    this.avatViewPanel = avatViewPanel;
    this.orbitViewPanel = orbitViewPanel;
    this.mapViewPanel = mapViewPanel;
    myLog.addMessage(4, "   3D GUI Panels set.");

    //init abstract space
    spaceCore = new SpaceCore(spaceWorld, spaceView_grid, spaceView_orbitAvat, spaceView_avat);
    myLog.addMessage(4, "   Abstract Space, Grid and Navigation set.");

    myZoneWorld = new ZoneWorld();
    myLog.addMessage(4, "   ZoneWorld (repository) initialized.");

    zoneBuilder = new ZoneBuilder(spaceCore, spaceWorld, spaceView_avat, myZoneWorld, zone_tree);
    myLog.addMessage(4, "   ZoneBuilder set.");
    spaceInitialized = true;
  }

  public boolean startSpace()
  {
    if(spaceInitialized)
    {
      myLog.addMessage(4, "MySpace: Starting Space...");
      setupMyZone();
      updateMyZoneWorld();

      zoneBuilder.buildAllZones();

      spaceCore.spaceNav.setupNavigation(vars.SPACEVIEW_GRID);
      spaceCore.spaceNav.setupNavigation(vars.SPACEVIEW_AVATAR);
      spaceCore.spaceNav.setupNavigation(vars.SPACEVIEW_ORBIT_1);

      spaceCore.spaceEnvio.buildEnvironment();

      finalizeWorld();

      setupPanel();
      myLog.addMessage(4, "MySpace: Space started.");
      return true;
    }
    else
    {
      myLog.addMessage(2, "MySpace: Cannot start Space. Not Initialized!");
      return false;
    }
  }

  public void updateSpace()
  {
    myLog.addMessage(4, "MySpace: Updating Space...");
    spaceCore.isWorldFinalized = false;
    //spaceWorld.setLive(false);
    //spaceWorld.getSceneRoot().removeAllChildren();
    //spaceWorld.getSceneRoot().detach();

    setupMyZone();
    updateMyZoneWorld();
    spaceWorld.clearScene();

    zoneBuilder.buildAllZones(); //buildZoneWorld();
    //spaceCore.spaceNav.updateNavigation();
    spaceCore.spaceEnvio.buildEnvironment();
    finalizeWorld();
    myLog.addMessage(4, "MySpace: Space updated.");
  }

  public void changeVP()
  {
    /**@todo Check this*/
    myLog.addMessage(4, "MySpace: Changing Viewpoint...");
    if(currentVPP)
    {
      //avatViewPanel.remove(spaceView_orbitAvat.getDisplay());
      avatViewPanel.removeAll();
      avatViewPanel.add(spaceView_avat.getDisplay());
      currentVPP = true;
    }
    else
    {
      avatViewPanel.remove(spaceView_avat.getDisplay());
      //this.removeAll();
      avatViewPanel.add(spaceView_orbitAvat.getDisplay());
      currentVPP = false;
    }
  }

  public void changeVPGrid()
  {
    /**@todo do this*/
  }

  private void setupPanel()
  {
    myLog.addMessage(4, "   Setting up Panel...");
    avatViewPanel.setPreferredSize(new Dimension(400, 400));
    avatViewPanel.setLayout(new GridLayout(1, 1, 2, 2));
    //this.add(spaceView_orbitZone.getDisplay());
    avatViewPanel.add(spaceView_avat.getDisplay());

    gridViewPanel.setPreferredSize(new Dimension(100, 100));
    gridViewPanel.setLayout(new GridLayout(1, 1, 2, 2));
    gridViewPanel.add(spaceView_grid.getDisplay());

    orbitViewPanel.setPreferredSize(new Dimension(140, 140));
    orbitViewPanel.setLayout(new GridLayout(1, 1, 2, 2));
    orbitViewPanel.add(spaceView_orbitAvat.getDisplay());

  }

  private void setupMyZone()
  {
    myLog.addMessage(4, "   Setting up MyZone...");
    this.myZone = zoneBuilder.getMyZone();

    if(zoneBuilder.validateZone(this.myZone))
      myLog.addMessage(2, "Error: MyZone could not be validated!");
    else
      myLog.addMessage(4, "MyZone validation OK!");
  }


  private void updateMyZoneWorld()
  {
    Zone[] zones = zoneBuilder.getBuilderRepository().loadAllZonesFromRep();
    if(zones.length>0)
    {
      myZoneWorld.clearZoneWorld();
      for (int i = 0; i < zones.length; i++)
        myZoneWorld.addZone(zones[i]);
    }
  }

  private void setupMyZoneWorldManual()
  {
    Zone z1 = new Zone("Zone1");
    z1.setZone_Name("Other Zone #1");
    z1.myGeometry.setTwoPoint_H(10);
    z1.myGeometry.setTwoPoint_W(10);
    z1.myGeometry.setPosition(new Vector3d(-15, 0, 15));
    z1.myObjects.addVObject(new VObject("TERRA-Z000"+z1.getZone_ID()+"-B-"+vars.BBTYPE_BOX+"-0", z1.myGeometry.getPosition(), "Hello World", "", vars.BBTYPE_BOX, new Vector3d(0,0,0)));
    myLog.addMessage(4, "   Adding Zone to myZoneWorld as #"+myZoneWorld.addZone(z1));

    Zone z2 = new Zone("Zone2");
    z2.setZone_Name("Other Zone #2");
    z2.myGeometry.setTwoPoint_H(4);
    z2.myGeometry.setTwoPoint_W(20);
    z2.myGeometry.setPosition(new Vector3d(-20, 0, 0));
    z2.myObjects.addVObject(new VObject("TERRA-Z000"+z2.getZone_ID()+"-B-"+vars.BBTYPE_SPHERE+"-0", z2.myGeometry.getPosition(), "Party Object 1", "", vars.BBTYPE_SPHERE, new Vector3d(0,0,0)));
    z2.myObjects.addVObject(new VObject("TERRA-Z000"+z2.getZone_ID()+"-B-"+vars.BBTYPE_CYLINDER+"-1", z2.myGeometry.getPosition(), "Party Object 2", "", vars.BBTYPE_CYLINDER, new Vector3d(2,0,0)));
    z2.myObjects.addVObject(new VObject("TERRA-Z000"+z2.getZone_ID()+"-B-"+vars.BBTYPE_PYRAMID+"-2", z2.myGeometry.getPosition(), "Party Object 3", "", vars.BBTYPE_PYRAMID, new Vector3d(4,0,0)));
    z2.myObjects.addVObject(new VObject("TERRA-Z000"+z2.getZone_ID()+"-B-"+vars.BBTYPE_BOX+"-3", z2.myGeometry.getPosition(), "Party Object 4", "", vars.BBTYPE_BOX, new Vector3d(6,0,0)));
    myLog.addMessage(4, "   Adding Zone to myZoneWorld as #"+myZoneWorld.addZone(z2));

    Zone z3 = new Zone("Zone3");
    z3.setZone_Name("Other Zone #3");
    z3.myGeometry.setTwoPoint_H(12);
    z3.myGeometry.setTwoPoint_W(10);
    z3.myGeometry.setPosition(new Vector3d(10, 0, -20));
    myLog.addMessage(4, "   Adding Zone to myZoneWorld as #"+myZoneWorld.addZone(z3));

    Zone z4 = new Zone("Zone4");
    z4.setZone_Name("Other Zone #4");
    z4.myGeometry.setTwoPoint_H(2);
    z4.myGeometry.setTwoPoint_W(2);
    z4.myGeometry.setPosition(new Vector3d(-20, 0, -20));
    myLog.addMessage(4, "   Adding Zone to myZoneWorld as #"+myZoneWorld.addZone(z4));

    Zone z5 = new Zone("Zone5");
    z5.setZone_Name("Other Zone #5");
    z5.myGeometry.setTwoPoint_H(20);
    z5.myGeometry.setTwoPoint_W(20);
    z5.myGeometry.setPosition(new Vector3d(20, 0, 20));
    myLog.addMessage(4, "   Adding Zone to myZoneWorld as #"+myZoneWorld.addZone(z5));

    //System.out.println("Zone3 X1 = "+myZoneWorld.getZone("Other Zone #3").myGeometry.getTwoPoint_X1());
    //zoneBuilder.setMyZoneWorld(myZoneWorld.getZones());
  }

  private void finalizeWorld()
  {
    myLog.addMessage(4, "MySpace: Finalizing World");
    //setup Grid
    spaceWorld.addSceneNode(spaceCore.spaceGrid);

    //javax.media.j3d.RestrictedAccessException: Cannot modify capability bits on a live or compiled object
    //if (spaceWorld.getSceneRoot() instanceof Group)
    //  ((Group)spaceWorld.getSceneRoot()).setCapability(Group.ALLOW_CHILDREN_READ);

    //zone_tree.updateTree(spaceWorld.getSceneRoot());

    //initialize feedback (last)
    //groupMgr.initTargets(null, Feedback.STATUS_DISABLE, Feedback.SELECT_NORMAL, Feedback.ACTION_NORMAL);

    spaceWorld.setLive(true);
    spaceCore.isWorldFinalized = true;
  }

/*
  public void updateModel(Node node)
  {
    // kill the scene before adding node or updating tree
    boolean isLive = spaceWorld.setLive(false);
    spaceWorld.addSceneNode(node);
    zone_tree.updateTree(spaceWorld.getSceneRoot());
    spaceWorld.setLive(true);
  }
*/

  public boolean isSpaceInitialized()
  {
    return spaceInitialized;
  }

}
