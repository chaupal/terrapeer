package terrapeer.vui.zone;

import terrapeer.vui.service.*;

import java.util.*;
import javax.media.j3d.*;

/**
 * A management class to handle all VObjects within a Zone.
 * All virtual objects are loaded, and retrieved through here.
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */
public class ZoneObjects
{
  private String zoneID = null;
  private Vector vObjects = null;
  private int objCount;

  public ZoneObjects(String zoneID)
  {
    this.zoneID = zoneID;
    this.objCount = 0;
    vObjects = new Vector(10);
  }

  public String getServiceZoneID()
  {
    return this.zoneID;
  }

  public int addVObject(VObject o)
  {
    vObjects.add(objCount, o);
    objCount++;
    return (objCount-1);
  }

  public VObject getVObject(int i)
  {
    return (VObject)vObjects.get(i);
  }

  public VObject getVObjectByID(String id)
  {
    VObject vo = null;
    for(int i=0; i < getVObjectCount(); i++)
    {
      vo = (VObject)vObjects.get(i);
      if(vo.getId().equals(id))
        return vo;
    }
    return null;
  }

  public VObject getVObjectByName(String name)
  {
    VObject vo = null;
    for(int i=0; i < getVObjectCount(); i++)
    {
      vo = (VObject)vObjects.get(i);
      if(vo.getName().equals(name))
        return vo;
    }
    return null;
  }

  public int getVObjectCount()
  {
    return objCount;
  }

}
