package terrapeer.vui.service;

import terrapeer.vui.*;
import terrapeer.vui.helpers.*;

import java.io.Serializable;
import javax.media.j3d.Node;
import javax.vecmath.*;

/**
 * An object that encapsulates a virtual object's information:
 * - ID
 * - Name
 * - File Name
 * - 3D node
 * Extends VGeometry
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */

public class VObject extends VGeometry implements Serializable
{
  private String id;
  private String name;
  private String fileName;
  private int bbType;
  private Node node;

  public VObject(String id, Vector3d zonePos)
  {
    this(id, zonePos, "NoName", "", 1, new Vector3d(0,0,0));
  }

  public VObject(String id, Vector3d zonePos, String name, String fileName, int bbType, Vector3d objPos)
  {
    super();
    this.id = id;
    this.name = name;
    this.fileName = fileName;
    this.bbType = bbType;
    objPos.add(zonePos);
    this.setPosition(objPos);
    //this.node = n;
  }

  public javax.media.j3d.Node getNode()
  {
    return node;
  }
  public void setNode(javax.media.j3d.Node node)
  {
    this.node = node;
  }
  public String getId()
  {
    return id;
  }
  public void setId(String id)
  {
    this.id = id;
  }
  public String getName()
  {
    return name;
  }
  public void setName(String name)
  {
    this.name = name;
  }
  public String getFileName()
  {
    return fileName;
  }
  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }
  public int getBbType()
  {
    return bbType;
  }
  public void setBbType(int bbType)
  {
    this.bbType = bbType;
  }

}
