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
import terrapeer.vui.j3dui.control.mappers.*;
import terrapeer.vui.j3dui.control.actuators.groups.*;
import terrapeer.vui.j3dui.feedback.elements.*;
import terrapeer.vui.j3dui.navigate.*;

/**
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */

public class SpaceEnvio
{
  private static TerraPeerLog myLog = TerraPeerLog.getLogger();
  private static AppWorld spaceWorld = null;

  public SpaceEnvio(AppWorld spaceWorld)
  {
    this.spaceWorld = spaceWorld;
  }

  public void buildEnvironment()
  {
    myLog.addMessage(4, "SpaceCore: Building Environment...");
    // build world ground
    /// build small textured earth rect
    //TextureRect earth = new TextureRect(true, new Color3f(1, 1, 1), 0, vars.IMG_FULL_PATH+File.separatorChar+"earth.jpg", Elements.SIDE_TOP, vars.SPACENAV_LIMIT_XY, vars.SPACENAV_LIMIT_XY);

    /// build large untextured water rect
    TextureRect waterRect = new TextureRect(true, new Color3f(.77f, .77f, .77f), 0, null, Elements.SIDE_TOP, vars.SPACENAV_LIMIT_XY, vars.SPACENAV_LIMIT_XY);
    AffineGroup water = new AffineGroup(waterRect);

    /// make water non-coplanar with earth
    water.getTranslation().initActuation(new Vector4d(0, 0, -1, 0));

    /// compose ground and flip it up
    AffineGroup ground = new AffineGroup();
    //ground.addNode(earth);
    ground.addNode(water);
    ground.getAxisAngle().initActuation(new Vector4d(1, 0, 0, -Math.PI / 2.0));

    spaceWorld.addSceneNode(ground);

    // build world thing at origin
    //spaceWorld.addSceneNode(new TestThing(new Color3f(1f, 1f, 0f), new Color3f(1f, 0f, 0f)));

    // build world light
    OrbitGroup sun = new OrbitGroup(new TestLight(new DirectionalLight()));
    sun.initActuation(new Vector4d(-Math.PI / 6, -Math.PI / 8, 0, 0));

    spaceWorld.addSceneNode(sun);

    // build world sky
    /// build large textured cone
    TextureCone sky = new TextureCone(Elements.SIDE_BOTTOM, vars.SPACENAV_LIMIT_XY, 100+vars.SPACENAV_LIMIT_UP);
    sky.setTexture(vars.IMG_FULL_PATH+File.separatorChar+vars.IMG_FILE[60]);

    spaceWorld.addSceneNode(sky);
  }

}
