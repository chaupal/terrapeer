package terrapeer.net.schema;

import terrapeer.net.xml.types.*;

/**
 *
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */
public class ZoneWorldType extends terrapeer.net.xml.Node
{
  public ZoneWorldType()
  {
    super();
  }

  public ZoneWorldType(ZoneWorldType node)
  {
    super(node);
  }

  public ZoneWorldType(org.w3c.dom.Node node)
  {
    super(node);
  }

  public ZoneWorldType(org.w3c.dom.Document doc)
  {
    super(doc);
  }

  public void adjustPrefix()
  {
    int count;
    count = getDomChildCount(Element, null, "Zone");
    for (int i = 0; i < count; i++)
    {
      org.w3c.dom.Node tmpNode = getDomChildAt(Element, null, "Zone", i);
      internalAdjustPrefix(tmpNode, true);
      new ZoneType(tmpNode).adjustPrefix();
    }
  }

  public int getZoneMinCount()
  {
    return 1;
  }

  public int getZoneMaxCount()
  {
    return Integer.MAX_VALUE;
  }

  public int getZoneCount()
  {
    return getDomChildCount(Element, null, "Zone");
  }

  public boolean hasZone()
  {
    return hasDomChild(Element, null, "Zone");
  }

  public ZoneType getZoneAt(int index) throws Exception
  {
    return new ZoneType(getDomChildAt(Element, null, "Zone", index));
  }

  public ZoneType getZone() throws Exception
  {
    return getZoneAt(0);
  }

  public void removeZoneAt(int index)
  {
    removeDomChildAt(Element, null, "Zone", index);
  }

  public void removeZone()
  {
    while (hasZone())
    {
      removeZoneAt(0);
    }
  }

  public void addZone(ZoneType value)
  {
    appendDomElement(null, "Zone", value);
  }

  public void insertZoneAt(ZoneType value, int index)
  {
    insertDomElementAt(null, "Zone", index, value);
  }

  public void replaceZoneAt(ZoneType value, int index)
  {
    replaceDomElementAt(null, "Zone", index, value);
  }
}
