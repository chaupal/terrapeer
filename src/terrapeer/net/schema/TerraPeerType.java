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
public class TerraPeerType extends terrapeer.net.xml.Node
{
  public TerraPeerType()
  {
    super();
  }

  public TerraPeerType(TerraPeerType node)
  {
    super(node);
  }

  public TerraPeerType(org.w3c.dom.Node node)
  {
    super(node);
  }

  public TerraPeerType(org.w3c.dom.Document doc)
  {
    super(doc);
  }

  public void adjustPrefix()
  {
    int count;
    count = getDomChildCount(Element, null, "ID");
    for (int i = 0; i < count; i++)
    {
      org.w3c.dom.Node tmpNode = getDomChildAt(Element, null, "ID", i);
      internalAdjustPrefix(tmpNode, true);
    }
    count = getDomChildCount(Element, null, "Version");
    for (int i = 0; i < count; i++)
    {
      org.w3c.dom.Node tmpNode = getDomChildAt(Element, null, "Version", i);
      internalAdjustPrefix(tmpNode, true);
    }
    count = getDomChildCount(Element, null, "LastUpdated");
    for (int i = 0; i < count; i++)
    {
      org.w3c.dom.Node tmpNode = getDomChildAt(Element, null, "LastUpdated", i);
      internalAdjustPrefix(tmpNode, true);
    }
    count = getDomChildCount(Element, null, "Name");
    for (int i = 0; i < count; i++)
    {
      org.w3c.dom.Node tmpNode = getDomChildAt(Element, null, "Name", i);
      internalAdjustPrefix(tmpNode, true);
    }
    count = getDomChildCount(Element, null, "Zone");
    for (int i = 0; i < count; i++)
    {
      org.w3c.dom.Node tmpNode = getDomChildAt(Element, null, "Zone", i);
      internalAdjustPrefix(tmpNode, true);
      new ZoneType(tmpNode).adjustPrefix();
    }
    count = getDomChildCount(Element, null, "ZoneWorld");
    for (int i = 0; i < count; i++)
    {
      org.w3c.dom.Node tmpNode = getDomChildAt(Element, null, "ZoneWorld", i);
      internalAdjustPrefix(tmpNode, true);
      new ZoneWorldType(tmpNode).adjustPrefix();
    }
  }

  public int getIDMinCount()
  {
    return 1;
  }

  public int getIDMaxCount()
  {
    return 1;
  }

  public int getIDCount()
  {
    return getDomChildCount(Element, null, "ID");
  }

  public boolean hasID()
  {
    return hasDomChild(Element, null, "ID");
  }

  public SchemaString getIDAt(int index) throws Exception
  {
    return new SchemaString(getDomNodeValue(getDomChildAt(Element, null, "ID", index)));
  }

  public SchemaString getID() throws Exception
  {
    return getIDAt(0);
  }

  public void removeIDAt(int index)
  {
    removeDomChildAt(Element, null, "ID", index);
  }

  public void removeID()
  {
    while (hasID())
    {
      removeIDAt(0);
    }
  }

  public void addID(SchemaString value)
  {
    setDomChild(Element, null, "ID", value.toString());
  }

  public void addID(String value) throws Exception
  {
    addID(new SchemaString(value));
  }

  public void insertIDAt(SchemaString value, int index)
  {
    insertDomChildAt(Element, null, "ID", index, value.toString());
  }

  public void insertIDAt(String value, int index) throws Exception
  {
    insertIDAt(new SchemaString(value), index);
  }

  public void replaceIDAt(SchemaString value, int index)
  {
    replaceDomChildAt(Element, null, "ID", index, value.toString());
  }

  public void replaceIDAt(String value, int index) throws Exception
  {
    replaceIDAt(new SchemaString(value), index);
  }

  public int getVersionMinCount()
  {
    return 1;
  }

  public int getVersionMaxCount()
  {
    return 1;
  }

  public int getVersionCount()
  {
    return getDomChildCount(Element, null, "Version");
  }

  public boolean hasVersion()
  {
    return hasDomChild(Element, null, "Version");
  }

  public SchemaString getVersionAt(int index) throws Exception
  {
    return new SchemaString(getDomNodeValue(getDomChildAt(Element, null, "Version", index)));
  }

  public SchemaString getVersion() throws Exception
  {
    return getVersionAt(0);
  }

  public void removeVersionAt(int index)
  {
    removeDomChildAt(Element, null, "Version", index);
  }

  public void removeVersion()
  {
    while (hasVersion())
    {
      removeVersionAt(0);
    }
  }

  public void addVersion(SchemaString value)
  {
    setDomChild(Element, null, "Version", value.toString());
  }

  public void addVersion(String value) throws Exception
  {
    addVersion(new SchemaString(value));
  }

  public void insertVersionAt(SchemaString value, int index)
  {
    insertDomChildAt(Element, null, "Version", index, value.toString());
  }

  public void insertVersionAt(String value, int index) throws Exception
  {
    insertVersionAt(new SchemaString(value), index);
  }

  public void replaceVersionAt(SchemaString value, int index)
  {
    replaceDomChildAt(Element, null, "Version", index, value.toString());
  }

  public void replaceVersionAt(String value, int index) throws Exception
  {
    replaceVersionAt(new SchemaString(value), index);
  }

  public int getLastUpdatedMinCount()
  {
    return 1;
  }

  public int getLastUpdatedMaxCount()
  {
    return 1;
  }

  public int getLastUpdatedCount()
  {
    return getDomChildCount(Element, null, "LastUpdated");
  }

  public boolean hasLastUpdated()
  {
    return hasDomChild(Element, null, "LastUpdated");
  }

  public SchemaDateTime getLastUpdatedAt(int index) throws Exception
  {
    return new SchemaDateTime(getDomNodeValue(getDomChildAt(Element, null, "LastUpdated", index)));
  }

  public SchemaDateTime getLastUpdated() throws Exception
  {
    return getLastUpdatedAt(0);
  }

  public void removeLastUpdatedAt(int index)
  {
    removeDomChildAt(Element, null, "LastUpdated", index);
  }

  public void removeLastUpdated()
  {
    while (hasLastUpdated())
    {
      removeLastUpdatedAt(0);
    }
  }

  public void addLastUpdated(SchemaDateTime value)
  {
    setDomChild(Element, null, "LastUpdated", value.toString());
  }

  public void addLastUpdated(String value) throws Exception
  {
    addLastUpdated(new SchemaDateTime(value));
  }

  public void insertLastUpdatedAt(SchemaDateTime value, int index)
  {
    insertDomChildAt(Element, null, "LastUpdated", index, value.toString());
  }

  public void insertLastUpdatedAt(String value, int index) throws Exception
  {
    insertLastUpdatedAt(new SchemaDateTime(value), index);
  }

  public void replaceLastUpdatedAt(SchemaDateTime value, int index)
  {
    replaceDomChildAt(Element, null, "LastUpdated", index, value.toString());
  }

  public void replaceLastUpdatedAt(String value, int index) throws Exception
  {
    replaceLastUpdatedAt(new SchemaDateTime(value), index);
  }

  public int getNameMinCount()
  {
    return 0;
  }

  public int getNameMaxCount()
  {
    return 1;
  }

  public int getNameCount()
  {
    return getDomChildCount(Element, null, "Name");
  }

  public boolean hasName()
  {
    return hasDomChild(Element, null, "Name");
  }

  public SchemaString getNameAt(int index) throws Exception
  {
    return new SchemaString(getDomNodeValue(getDomChildAt(Element, null, "Name", index)));
  }

  public SchemaString getName() throws Exception
  {
    return getNameAt(0);
  }

  public void removeNameAt(int index)
  {
    removeDomChildAt(Element, null, "Name", index);
  }

  public void removeName()
  {
    while (hasName())
    {
      removeNameAt(0);
    }
  }

  public void addName(SchemaString value)
  {
    setDomChild(Element, null, "Name", value.toString());
  }

  public void addName(String value) throws Exception
  {
    addName(new SchemaString(value));
  }

  public void insertNameAt(SchemaString value, int index)
  {
    insertDomChildAt(Element, null, "Name", index, value.toString());
  }

  public void insertNameAt(String value, int index) throws Exception
  {
    insertNameAt(new SchemaString(value), index);
  }

  public void replaceNameAt(SchemaString value, int index)
  {
    replaceDomChildAt(Element, null, "Name", index, value.toString());
  }

  public void replaceNameAt(String value, int index) throws Exception
  {
    replaceNameAt(new SchemaString(value), index);
  }

  public int getZoneMinCount()
  {
    return 1;
  }

  public int getZoneMaxCount()
  {
    return 1;
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

  public int getZoneWorldMinCount()
  {
    return 0;
  }

  public int getZoneWorldMaxCount()
  {
    return 1;
  }

  public int getZoneWorldCount()
  {
    return getDomChildCount(Element, null, "ZoneWorld");
  }

  public boolean hasZoneWorld()
  {
    return hasDomChild(Element, null, "ZoneWorld");
  }

  public ZoneWorldType getZoneWorldAt(int index) throws Exception
  {
    return new ZoneWorldType(getDomChildAt(Element, null, "ZoneWorld", index));
  }

  public ZoneWorldType getZoneWorld() throws Exception
  {
    return getZoneWorldAt(0);
  }

  public void removeZoneWorldAt(int index)
  {
    removeDomChildAt(Element, null, "ZoneWorld", index);
  }

  public void removeZoneWorld()
  {
    while (hasZoneWorld())
    {
      removeZoneWorldAt(0);
    }
  }

  public void addZoneWorld(ZoneWorldType value)
  {
    appendDomElement(null, "ZoneWorld", value);
  }

  public void insertZoneWorldAt(ZoneWorldType value, int index)
  {
    insertDomElementAt(null, "ZoneWorld", index, value);
  }

  public void replaceZoneWorldAt(ZoneWorldType value, int index)
  {
    replaceDomElementAt(null, "ZoneWorld", index, value);
  }
}
