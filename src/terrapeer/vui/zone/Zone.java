package terrapeer.vui.zone;

import terrapeer.*;
import terrapeer.vui.*;
import terrapeer.vui.helpers.*;

import java.util.Calendar;
import java.io.Serializable;

/**
 * <p>
 * The Zone class contains all relevant information, state, and functionality
 * for a virtual zone object.
 * </p><p>
 * A Zone is a virtual area placed on the Base Level (0), and can have
 * different geometrical 2D shapes.
 * </p><p>
 * Zone Geometry:
 *  - Defined by 2 Corners (square)
 *  - Defined by 4 Corners
 *  - Defined by a Vector of Points
 * </p><p>
 * Identification settings:
 *  - Zone ID
 *  - Zone Name
 *  - Zone Description
 * </p><p>
 * Other Geometrical settings:
 *  - Zone's center position
 *  - Zone height and depth
 *  - The border may have certain special properties
 * </p><p>
 * Virtual Zone properties:
 *  - Global States (current status)
 *     STATE_OK
 *     STATE_ERR
 *  - Build States (current status)
 *     ZONE_NEW
 *     ZONE_UNDER_CONSTRUCTION
 *     ZONE_BASE
 *     ZONE_SERVICE
 *     ZONE_XT
 *  - Settlement (current state of virtual settlement for the zone)
 *  - Access Rights (who is allowed to view/change/use the zone)
 * </p>
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P feedback system</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */
public class Zone implements Serializable
{
  private static TerraPeerLog myLog = TerraPeerLog.getLogger();

  private String Zone_ID = null;
  private String Zone_Name = "ZoneName";
  private String Zone_Description = "This is a zone.";
  private int GlobalState = vars.STATE_ERR;
  private int BuildState = vars.BUILD_STATE_ZONE_NEW;

  private boolean Filtered1 = false;
  private boolean Filtered2 = false;
  private boolean Selected = false;
  private boolean mouseOver = false;

  public terrapeer.vui.helpers.AccessRights myAccess;
  public terrapeer.vui.zone.ZoneGeometry myGeometry;
  public terrapeer.vui.zone.Settlement mySettlement;
  public terrapeer.vui.zone.ZoneServices myServices;
  public terrapeer.vui.zone.ZoneObjects myObjects;

  /**
   * Constructor requires valid ID (>0)
   * @param Zone_ID
   */
  public Zone(String Zone_ID)
  {
    myLog.addMessage(4, "Constructing New Zone...");

    this.Zone_ID = Zone_ID;
    myLog.addMessage(4, "   Zone ID: "+this.Zone_ID);
    if((this.Zone_ID != null) && (!this.Zone_ID.equals("")))
    {
      this.Zone_ID = Zone_ID;
      this.myAccess = new AccessRights();
      this.myGeometry = new ZoneGeometry();
      this.mySettlement = new Settlement();
      this.myServices = new ZoneServices(Zone_ID);
      this.myObjects = new ZoneObjects(Zone_ID);

      this.GlobalState = vars.STATE_OK;
    }
    else
    {
      this.GlobalState = vars.STATE_ERR;
      myLog.addMessage(1, "   Zone ID Error.");
    }
  }

  public String getXMLZoneData()
  {
    //todo

    return null;
  }

  public boolean setXMLZoneData(String xml_data)
  {
    //todo

    return false;
  }

  public String getZone_ID()
  {
    return Zone_ID;
  }
  public String getZone_Name()
  {
    return Zone_Name;
  }
  public void setZone_Name(String Zone_Name)
  {
    this.Zone_Name = Zone_Name;
  }
  public String getZone_Description()
  {
    return Zone_Description;
  }
  public void setZone_Description(String Zone_Description)
  {
    this.Zone_Description = Zone_Description;
  }
  public terrapeer.vui.helpers.AccessRights getAccess()
  {
    return myAccess;
  }
  public void setAccess(terrapeer.vui.helpers.AccessRights Access)
  {
    this.myAccess = Access;
  }
  public int getGlobalState()
  {
    return GlobalState;
  }
  public void setGlobalState(int GlobalState)
  {
    this.GlobalState = GlobalState;
  }
  public int getBuildState()
  {
    return BuildState;
  }
  public void setBuildState(int BuildState)
  {
    this.BuildState = BuildState;
  }
  public boolean isFiltered1()
  {
    return Filtered1;
  }
  public boolean isFiltered2()
  {
    return Filtered2;
  }
  public void setFiltered1(boolean Filtered1)
  {
    this.Filtered1 = Filtered1;
  }
  public void setFiltered2(boolean Filtered2)
  {
    this.Filtered2 = Filtered2;
  }
  public boolean isSelected()
  {
    return Selected;
  }
  public void setSelected(boolean Selected)
  {
    this.Selected = Selected;
  }
  public boolean isMouseOver()
  {
    return mouseOver;
  }
  public void setMouseOver(boolean mouseOver)
  {
    this.mouseOver = mouseOver;
  }

}
