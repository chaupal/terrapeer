package terrapeer.vui.zone;

import java.util.*;
import terrapeer.*;

/**
 * ZoneWorld
 *   { Repository }
 *
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */
public class ZoneWorld
{
  private static TerraPeerLog myLog = TerraPeerLog.getLogger();

  private static Vector zones = null;
  private static int zoneCount = 0;

  public static Zone currSelectedZone = null;
  public static boolean isCurrSelectedZone = false;

  //public static ZoneRepository myRepository;

  public ZoneWorld()
  {
    myLog.addMessage(4, "Constructing ZoneWorld (Repository and Zone Vector)...");
    //myRepository = new ZoneRepository();
    zones = new Vector(10);

  }

  public int addZone(Zone z)
  {
    myLog.addMessage(4, "Adding Zone (ID: "+z.getZone_ID()+") to ZoneWorld (as #"+zoneCount+")");
    zones.add(zoneCount, z);
    zoneCount++;
    return (zoneCount-1);
  }

  public Zone getZone(String zoneID)
  {
    int zc = findZone(zoneID);
    //myLog.addMessage(4, "Zone "+zoneID+" has ZoneWorld #"+zc);
    if(zc>=0)
      return (Zone)zones.get(zc);
    return null;
  }

  public void removeZone(String zoneID)
  {
    try
    {
      zones.remove(findZone(zoneID));
      zoneCount--;
    }
    catch (Exception ex)
    {
    }
  }

  public void clearZoneWorld()
  {
    zones = new Vector(10);
    zoneCount = 0;
  }

  private int findZone(String zoneID)
  {
    Zone z = null;
    for(int i=0; i<zones.size(); i++)
    {
      //myLog.addMessage(4, "Zone["+i+"] has ID: "+((Zone)zones.elementAt(i)).getZone_ID());
      if(((Zone)zones.get(i)).getZone_ID().equals(zoneID))
        return i;
    }
    return -1;
  }

  public Vector getZones()
  {
    return zones;
  }

  public int getZoneCount()
  {
    //System.out.println("zoneworld zones: zones.size()="+zones.size()+" zoneCount="+zoneCount);
    if( zones.size() != zoneCount)
      myLog.addMessage(1, "Error: ZoneWorld counter unaligned!");
    return zoneCount;
  }

}
