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
public class ServiceObjectType extends terrapeer.net.xml.Node
{
  public ServiceObjectType()
  {
    super();
  }

  public ServiceObjectType(ServiceObjectType node)
  {
    super(node);
  }

  public ServiceObjectType(org.w3c.dom.Node node)
  {
    super(node);
  }

  public ServiceObjectType(org.w3c.dom.Document doc)
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
    count = getDomChildCount(Element, null, "BBTYPE");
    for (int i = 0; i < count; i++)
    {
      org.w3c.dom.Node tmpNode = getDomChildAt(Element, null, "BBTYPE", i);
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
    count = getDomChildCount(Element, null, "Position");
    for (int i = 0; i < count; i++)
    {
      org.w3c.dom.Node tmpNode = getDomChildAt(Element, null, "Position", i);
      internalAdjustPrefix(tmpNode, true);
      new Vector3dType(tmpNode).adjustPrefix();
    }
    count = getDomChildCount(Element, null, "LocalFileName");
    for (int i = 0; i < count; i++)
    {
      org.w3c.dom.Node tmpNode = getDomChildAt(Element, null, "LocalFileName", i);
      internalAdjustPrefix(tmpNode, true);
    }
    count = getDomChildCount(Element, null, "Web_URL");
    for (int i = 0; i < count; i++)
    {
      org.w3c.dom.Node tmpNode = getDomChildAt(Element, null, "Web_URL", i);
      internalAdjustPrefix(tmpNode, true);
    }
    count = getDomChildCount(Element, null, "FTP_URL");
    for (int i = 0; i < count; i++)
    {
      org.w3c.dom.Node tmpNode = getDomChildAt(Element, null, "FTP_URL", i);
      internalAdjustPrefix(tmpNode, true);
    }
    count = getDomChildCount(Element, null, "BinaryContent");
    for (int i = 0; i < count; i++)
    {
      org.w3c.dom.Node tmpNode = getDomChildAt(Element, null, "BinaryContent", i);
      internalAdjustPrefix(tmpNode, true);
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

  public int getBBTYPEMinCount()
  {
    return 1;
  }

  public int getBBTYPEMaxCount()
  {
    return 1;
  }

  public int getBBTYPECount()
  {
    return getDomChildCount(Element, null, "BBTYPE");
  }

  public boolean hasBBTYPE()
  {
    return hasDomChild(Element, null, "BBTYPE");
  }

  public SchemaInt getBBTYPEAt(int index) throws Exception
  {
    return new SchemaInt(getDomNodeValue(getDomChildAt(Element, null, "BBTYPE", index)));
  }

  public SchemaInt getBBTYPE() throws Exception
  {
    return getBBTYPEAt(0);
  }

  public void removeBBTYPEAt(int index)
  {
    removeDomChildAt(Element, null, "BBTYPE", index);
  }

  public void removeBBTYPE()
  {
    while (hasBBTYPE())
    {
      removeBBTYPEAt(0);
    }
  }

  public void addBBTYPE(SchemaInt value)
  {
    setDomChild(Element, null, "BBTYPE", value.toString());
  }

  public void addBBTYPE(String value) throws Exception
  {
    addBBTYPE(new SchemaInt(value));
  }

  public void insertBBTYPEAt(SchemaInt value, int index)
  {
    insertDomChildAt(Element, null, "BBTYPE", index, value.toString());
  }

  public void insertBBTYPEAt(String value, int index) throws Exception
  {
    insertBBTYPEAt(new SchemaInt(value), index);
  }

  public void replaceBBTYPEAt(SchemaInt value, int index)
  {
    replaceDomChildAt(Element, null, "BBTYPE", index, value.toString());
  }

  public void replaceBBTYPEAt(String value, int index) throws Exception
  {
    replaceBBTYPEAt(new SchemaInt(value), index);
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

  public int getPositionMinCount()
  {
    return 0;
  }

  public int getPositionMaxCount()
  {
    return 1;
  }

  public int getPositionCount()
  {
    return getDomChildCount(Element, null, "Position");
  }

  public boolean hasPosition()
  {
    return hasDomChild(Element, null, "Position");
  }

  public Vector3dType getPositionAt(int index) throws Exception
  {
    return new Vector3dType(getDomChildAt(Element, null, "Position", index));
  }

  public Vector3dType getPosition() throws Exception
  {
    return getPositionAt(0);
  }

  public void removePositionAt(int index)
  {
    removeDomChildAt(Element, null, "Position", index);
  }

  public void removePosition()
  {
    while (hasPosition())
    {
      removePositionAt(0);
    }
  }

  public void addPosition(Vector3dType value)
  {
    appendDomElement(null, "Position", value);
  }

  public void insertPositionAt(Vector3dType value, int index)
  {
    insertDomElementAt(null, "Position", index, value);
  }

  public void replacePositionAt(Vector3dType value, int index)
  {
    replaceDomElementAt(null, "Position", index, value);
  }

  public int getLocalFileNameMinCount()
  {
    return 0;
  }

  public int getLocalFileNameMaxCount()
  {
    return 1;
  }

  public int getLocalFileNameCount()
  {
    return getDomChildCount(Element, null, "LocalFileName");
  }

  public boolean hasLocalFileName()
  {
    return hasDomChild(Element, null, "LocalFileName");
  }

  public SchemaString getLocalFileNameAt(int index) throws Exception
  {
    return new SchemaString(getDomNodeValue(getDomChildAt(Element, null, "LocalFileName", index)));
  }

  public SchemaString getLocalFileName() throws Exception
  {
    return getLocalFileNameAt(0);
  }

  public void removeLocalFileNameAt(int index)
  {
    removeDomChildAt(Element, null, "LocalFileName", index);
  }

  public void removeLocalFileName()
  {
    while (hasLocalFileName())
    {
      removeLocalFileNameAt(0);
    }
  }

  public void addLocalFileName(SchemaString value)
  {
    setDomChild(Element, null, "LocalFileName", value.toString());
  }

  public void addLocalFileName(String value) throws Exception
  {
    addLocalFileName(new SchemaString(value));
  }

  public void insertLocalFileNameAt(SchemaString value, int index)
  {
    insertDomChildAt(Element, null, "LocalFileName", index, value.toString());
  }

  public void insertLocalFileNameAt(String value, int index) throws Exception
  {
    insertLocalFileNameAt(new SchemaString(value), index);
  }

  public void replaceLocalFileNameAt(SchemaString value, int index)
  {
    replaceDomChildAt(Element, null, "LocalFileName", index, value.toString());
  }

  public void replaceLocalFileNameAt(String value, int index) throws Exception
  {
    replaceLocalFileNameAt(new SchemaString(value), index);
  }

  public int getWeb_URLMinCount()
  {
    return 0;
  }

  public int getWeb_URLMaxCount()
  {
    return 1;
  }

  public int getWeb_URLCount()
  {
    return getDomChildCount(Element, null, "Web_URL");
  }

  public boolean hasWeb_URL()
  {
    return hasDomChild(Element, null, "Web_URL");
  }

  public SchemaString getWeb_URLAt(int index) throws Exception
  {
    return new SchemaString(getDomNodeValue(getDomChildAt(Element, null, "Web_URL", index)));
  }

  public SchemaString getWeb_URL() throws Exception
  {
    return getWeb_URLAt(0);
  }

  public void removeWeb_URLAt(int index)
  {
    removeDomChildAt(Element, null, "Web_URL", index);
  }

  public void removeWeb_URL()
  {
    while (hasWeb_URL())
    {
      removeWeb_URLAt(0);
    }
  }

  public void addWeb_URL(SchemaString value)
  {
    setDomChild(Element, null, "Web_URL", value.toString());
  }

  public void addWeb_URL(String value) throws Exception
  {
    addWeb_URL(new SchemaString(value));
  }

  public void insertWeb_URLAt(SchemaString value, int index)
  {
    insertDomChildAt(Element, null, "Web_URL", index, value.toString());
  }

  public void insertWeb_URLAt(String value, int index) throws Exception
  {
    insertWeb_URLAt(new SchemaString(value), index);
  }

  public void replaceWeb_URLAt(SchemaString value, int index)
  {
    replaceDomChildAt(Element, null, "Web_URL", index, value.toString());
  }

  public void replaceWeb_URLAt(String value, int index) throws Exception
  {
    replaceWeb_URLAt(new SchemaString(value), index);
  }

  public int getFTP_URLMinCount()
  {
    return 0;
  }

  public int getFTP_URLMaxCount()
  {
    return 1;
  }

  public int getFTP_URLCount()
  {
    return getDomChildCount(Element, null, "FTP_URL");
  }

  public boolean hasFTP_URL()
  {
    return hasDomChild(Element, null, "FTP_URL");
  }

  public SchemaString getFTP_URLAt(int index) throws Exception
  {
    return new SchemaString(getDomNodeValue(getDomChildAt(Element, null, "FTP_URL", index)));
  }

  public SchemaString getFTP_URL() throws Exception
  {
    return getFTP_URLAt(0);
  }

  public void removeFTP_URLAt(int index)
  {
    removeDomChildAt(Element, null, "FTP_URL", index);
  }

  public void removeFTP_URL()
  {
    while (hasFTP_URL())
    {
      removeFTP_URLAt(0);
    }
  }

  public void addFTP_URL(SchemaString value)
  {
    setDomChild(Element, null, "FTP_URL", value.toString());
  }

  public void addFTP_URL(String value) throws Exception
  {
    addFTP_URL(new SchemaString(value));
  }

  public void insertFTP_URLAt(SchemaString value, int index)
  {
    insertDomChildAt(Element, null, "FTP_URL", index, value.toString());
  }

  public void insertFTP_URLAt(String value, int index) throws Exception
  {
    insertFTP_URLAt(new SchemaString(value), index);
  }

  public void replaceFTP_URLAt(SchemaString value, int index)
  {
    replaceDomChildAt(Element, null, "FTP_URL", index, value.toString());
  }

  public void replaceFTP_URLAt(String value, int index) throws Exception
  {
    replaceFTP_URLAt(new SchemaString(value), index);
  }

  public int getBinaryContentMinCount()
  {
    return 0;
  }

  public int getBinaryContentMaxCount()
  {
    return 1;
  }

  public int getBinaryContentCount()
  {
    return getDomChildCount(Element, null, "BinaryContent");
  }

  public boolean hasBinaryContent()
  {
    return hasDomChild(Element, null, "BinaryContent");
  }

  public SchemaString getBinaryContentAt(int index) throws Exception
  {
    return new SchemaString(getDomNodeValue(getDomChildAt(Element, null, "BinaryContent", index)));
  }

  public SchemaString getBinaryContent() throws Exception
  {
    return getBinaryContentAt(0);
  }

  public void removeBinaryContentAt(int index)
  {
    removeDomChildAt(Element, null, "BinaryContent", index);
  }

  public void removeBinaryContent()
  {
    while (hasBinaryContent())
    {
      removeBinaryContentAt(0);
    }
  }

  public void addBinaryContent(SchemaString value)
  {
    setDomChild(Element, null, "BinaryContent", value.toString());
  }

  public void addBinaryContent(String value) throws Exception
  {
    addBinaryContent(new SchemaString(value));
  }

  public void insertBinaryContentAt(SchemaString value, int index)
  {
    insertDomChildAt(Element, null, "BinaryContent", index, value.toString());
  }

  public void insertBinaryContentAt(String value, int index) throws Exception
  {
    insertBinaryContentAt(new SchemaString(value), index);
  }

  public void replaceBinaryContentAt(SchemaString value, int index)
  {
    replaceDomChildAt(Element, null, "BinaryContent", index, value.toString());
  }

  public void replaceBinaryContentAt(String value, int index) throws Exception
  {
    replaceBinaryContentAt(new SchemaString(value), index);
  }
}
