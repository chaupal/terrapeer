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
public class ZoneType extends terrapeer.net.xml.Node
{
  public ZoneType()
  {
    super();
  }

  public ZoneType(ZoneType node)
  {
    super(node);
  }

  public ZoneType(org.w3c.dom.Node node)
  {
    super(node);
  }

  public ZoneType(org.w3c.dom.Document doc)
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
    count = getDomChildCount(Element, null, "Description");
    for (int i = 0; i < count; i++)
    {
      org.w3c.dom.Node tmpNode = getDomChildAt(Element, null, "Description", i);
      internalAdjustPrefix(tmpNode, true);
    }
    count = getDomChildCount(Element, null, "Geometry");
    for (int i = 0; i < count; i++)
    {
      org.w3c.dom.Node tmpNode = getDomChildAt(Element, null, "Geometry", i);
      internalAdjustPrefix(tmpNode, true);
      new GeometryType(tmpNode).adjustPrefix();
    }
    count = getDomChildCount(Element, null, "BaseObject");
    for (int i = 0; i < count; i++)
    {
      org.w3c.dom.Node tmpNode = getDomChildAt(Element, null, "BaseObject", i);
      internalAdjustPrefix(tmpNode, true);
      new BaseObjectType(tmpNode).adjustPrefix();
    }
    count = getDomChildCount(Element, null, "ServiceObject");
    for (int i = 0; i < count; i++)
    {
      org.w3c.dom.Node tmpNode = getDomChildAt(Element, null, "ServiceObject", i);
      internalAdjustPrefix(tmpNode, true);
      new ServiceObjectType(tmpNode).adjustPrefix();
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
    return 1;
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

  public int getDescriptionMinCount()
  {
    return 0;
  }

  public int getDescriptionMaxCount()
  {
    return 1;
  }

  public int getDescriptionCount()
  {
    return getDomChildCount(Element, null, "Description");
  }

  public boolean hasDescription()
  {
    return hasDomChild(Element, null, "Description");
  }

  public SchemaString getDescriptionAt(int index) throws Exception
  {
    return new SchemaString(getDomNodeValue(getDomChildAt(Element, null, "Description", index)));
  }

  public SchemaString getDescription() throws Exception
  {
    return getDescriptionAt(0);
  }

  public void removeDescriptionAt(int index)
  {
    removeDomChildAt(Element, null, "Description", index);
  }

  public void removeDescription()
  {
    while (hasDescription())
    {
      removeDescriptionAt(0);
    }
  }

  public void addDescription(SchemaString value)
  {
    setDomChild(Element, null, "Description", value.toString());
  }

  public void addDescription(String value) throws Exception
  {
    addDescription(new SchemaString(value));
  }

  public void insertDescriptionAt(SchemaString value, int index)
  {
    insertDomChildAt(Element, null, "Description", index, value.toString());
  }

  public void insertDescriptionAt(String value, int index) throws Exception
  {
    insertDescriptionAt(new SchemaString(value), index);
  }

  public void replaceDescriptionAt(SchemaString value, int index)
  {
    replaceDomChildAt(Element, null, "Description", index, value.toString());
  }

  public void replaceDescriptionAt(String value, int index) throws Exception
  {
    replaceDescriptionAt(new SchemaString(value), index);
  }

  public int getGeometryMinCount()
  {
    return 1;
  }

  public int getGeometryMaxCount()
  {
    return 1;
  }

  public int getGeometryCount()
  {
    return getDomChildCount(Element, null, "Geometry");
  }

  public boolean hasGeometry()
  {
    return hasDomChild(Element, null, "Geometry");
  }

  public GeometryType getGeometryAt(int index) throws Exception
  {
    return new GeometryType(getDomChildAt(Element, null, "Geometry", index));
  }

  public GeometryType getGeometry() throws Exception
  {
    return getGeometryAt(0);
  }

  public void removeGeometryAt(int index)
  {
    removeDomChildAt(Element, null, "Geometry", index);
  }

  public void removeGeometry()
  {
    while (hasGeometry())
    {
      removeGeometryAt(0);
    }
  }

  public void addGeometry(GeometryType value)
  {
    appendDomElement(null, "Geometry", value);
  }

  public void insertGeometryAt(GeometryType value, int index)
  {
    insertDomElementAt(null, "Geometry", index, value);
  }

  public void replaceGeometryAt(GeometryType value, int index)
  {
    replaceDomElementAt(null, "Geometry", index, value);
  }

  public int getBaseObjectMinCount()
  {
    return 0;
  }

  public int getBaseObjectMaxCount()
  {
    return Integer.MAX_VALUE;
  }

  public int getBaseObjectCount()
  {
    return getDomChildCount(Element, null, "BaseObject");
  }

  public boolean hasBaseObject()
  {
    return hasDomChild(Element, null, "BaseObject");
  }

  public BaseObjectType getBaseObjectAt(int index) throws Exception
  {
    return new BaseObjectType(getDomChildAt(Element, null, "BaseObject", index));
  }

  public BaseObjectType getBaseObject() throws Exception
  {
    return getBaseObjectAt(0);
  }

  public void removeBaseObjectAt(int index)
  {
    removeDomChildAt(Element, null, "BaseObject", index);
  }

  public void removeBaseObject()
  {
    while (hasBaseObject())
    {
      removeBaseObjectAt(0);
    }
  }

  public void addBaseObject(BaseObjectType value)
  {
    appendDomElement(null, "BaseObject", value);
  }

  public void insertBaseObjectAt(BaseObjectType value, int index)
  {
    insertDomElementAt(null, "BaseObject", index, value);
  }

  public void replaceBaseObjectAt(BaseObjectType value, int index)
  {
    replaceDomElementAt(null, "BaseObject", index, value);
  }

  public int getServiceObjectMinCount()
  {
    return 0;
  }

  public int getServiceObjectMaxCount()
  {
    return Integer.MAX_VALUE;
  }

  public int getServiceObjectCount()
  {
    return getDomChildCount(Element, null, "ServiceObject");
  }

  public boolean hasServiceObject()
  {
    return hasDomChild(Element, null, "ServiceObject");
  }

  public ServiceObjectType getServiceObjectAt(int index) throws Exception
  {
    return new ServiceObjectType(getDomChildAt(Element, null, "ServiceObject", index));
  }

  public ServiceObjectType getServiceObject() throws Exception
  {
    return getServiceObjectAt(0);
  }

  public void removeServiceObjectAt(int index)
  {
    removeDomChildAt(Element, null, "ServiceObject", index);
  }

  public void removeServiceObject()
  {
    while (hasServiceObject())
    {
      removeServiceObjectAt(0);
    }
  }

  public void addServiceObject(ServiceObjectType value)
  {
    appendDomElement(null, "ServiceObject", value);
  }

  public void insertServiceObjectAt(ServiceObjectType value, int index)
  {
    insertDomElementAt(null, "ServiceObject", index, value);
  }

  public void replaceServiceObjectAt(ServiceObjectType value, int index)
  {
    replaceDomElementAt(null, "ServiceObject", index, value);
  }
}
