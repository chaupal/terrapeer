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
 * SpaceCore
 *   { Space Envio }
 *   { Space Grid }
 *   { Space Nav }
 *
 * other zone models
 * zone reservation
 * space filtering
 *
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P feedback system</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */
public class SpaceCore
{
  private static TerraPeerLog myLog = TerraPeerLog.getLogger();
  private static AppWorld spaceWorld = null;

  public static SpaceGrid spaceGrid = null;
  public static SpaceEnvio spaceEnvio = null;
  public static SpaceNav spaceNav = null;

  public static boolean isWorldFinalized = false;

  public SpaceCore(AppWorld spaceWorld, AppView spaceView_grid, AppView spaceView_orbit, AppView spaceView_avat)
  {
    this.spaceWorld = spaceWorld;
    spaceGrid = new SpaceGrid();
    spaceEnvio = new SpaceEnvio(spaceWorld);
    spaceNav = new SpaceNav(this, spaceWorld, spaceView_grid, spaceView_orbit, spaceView_avat);
  }

}
