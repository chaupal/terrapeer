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
public class Vector3dType extends terrapeer.net.xml.Node
{
  public Vector3dType()
  {
    super();
  }

  public Vector3dType(Vector3dType node)
  {
    super(node);
  }

  public Vector3dType(org.w3c.dom.Node node)
  {
    super(node);
  }

  public Vector3dType(org.w3c.dom.Document doc)
  {
    super(doc);
  }

  public void adjustPrefix()
  {
    int count;
    count = getDomChildCount(Attribute, null, "X");
    for (int i = 0; i < count; i++)
    {
      org.w3c.dom.Node tmpNode = getDomChildAt(Attribute, null, "X", i);
      internalAdjustPrefix(tmpNode, false);
    }
    count = getDomChildCount(Attribute, null, "Y");
    for (int i = 0; i < count; i++)
    {
      org.w3c.dom.Node tmpNode = getDomChildAt(Attribute, null, "Y", i);
      internalAdjustPrefix(tmpNode, false);
    }
    count = getDomChildCount(Attribute, null, "Z");
    for (int i = 0; i < count; i++)
    {
      org.w3c.dom.Node tmpNode = getDomChildAt(Attribute, null, "Z", i);
      internalAdjustPrefix(tmpNode, false);
    }
  }

  public int getXMinCount()
  {
    return 0;
  }

  public int getXMaxCount()
  {
    return 1;
  }

  public int getXCount()
  {
    return getDomChildCount(Attribute, null, "X");
  }

  public boolean hasX()
  {
    return hasDomChild(Attribute, null, "X");
  }

  public SchemaDouble getXAt(int index) throws Exception
  {
    return new SchemaDouble(getDomNodeValue(getDomChildAt(Attribute, null, "X", index)));
  }

  public SchemaDouble getX() throws Exception
  {
    return getXAt(0);
  }

  public void removeXAt(int index)
  {
    removeDomChildAt(Attribute, null, "X", index);
  }

  public void removeX()
  {
    while (hasX())
    {
      removeXAt(0);
    }
  }

  public void addX(SchemaDouble value)
  {
    setDomChild(Attribute, null, "X", value.toString());
  }

  public void addX(String value) throws Exception
  {
    addX(new SchemaDouble(value));
  }

  public void insertXAt(SchemaDouble value, int index)
  {
    insertDomChildAt(Attribute, null, "X", index, value.toString());
  }

  public void insertXAt(String value, int index) throws Exception
  {
    insertXAt(new SchemaDouble(value), index);
  }

  public void replaceXAt(SchemaDouble value, int index)
  {
    replaceDomChildAt(Attribute, null, "X", index, value.toString());
  }

  public void replaceXAt(String value, int index) throws Exception
  {
    replaceXAt(new SchemaDouble(value), index);
  }

  public int getYMinCount()
  {
    return 0;
  }

  public int getYMaxCount()
  {
    return 1;
  }

  public int getYCount()
  {
    return getDomChildCount(Attribute, null, "Y");
  }

  public boolean hasY()
  {
    return hasDomChild(Attribute, null, "Y");
  }

  public SchemaDouble getYAt(int index) throws Exception
  {
    return new SchemaDouble(getDomNodeValue(getDomChildAt(Attribute, null, "Y", index)));
  }

  public SchemaDouble getY() throws Exception
  {
    return getYAt(0);
  }

  public void removeYAt(int index)
  {
    removeDomChildAt(Attribute, null, "Y", index);
  }

  public void removeY()
  {
    while (hasY())
    {
      removeYAt(0);
    }
  }

  public void addY(SchemaDouble value)
  {
    setDomChild(Attribute, null, "Y", value.toString());
  }

  public void addY(String value) throws Exception
  {
    addY(new SchemaDouble(value));
  }

  public void insertYAt(SchemaDouble value, int index)
  {
    insertDomChildAt(Attribute, null, "Y", index, value.toString());
  }

  public void insertYAt(String value, int index) throws Exception
  {
    insertYAt(new SchemaDouble(value), index);
  }

  public void replaceYAt(SchemaDouble value, int index)
  {
    replaceDomChildAt(Attribute, null, "Y", index, value.toString());
  }

  public void replaceYAt(String value, int index) throws Exception
  {
    replaceYAt(new SchemaDouble(value), index);
  }

  public int getZMinCount()
  {
    return 0;
  }

  public int getZMaxCount()
  {
    return 1;
  }

  public int getZCount()
  {
    return getDomChildCount(Attribute, null, "Z");
  }

  public boolean hasZ()
  {
    return hasDomChild(Attribute, null, "Z");
  }

  public SchemaDouble getZAt(int index) throws Exception
  {
    return new SchemaDouble(getDomNodeValue(getDomChildAt(Attribute, null, "Z", index)));
  }

  public SchemaDouble getZ() throws Exception
  {
    return getZAt(0);
  }

  public void removeZAt(int index)
  {
    removeDomChildAt(Attribute, null, "Z", index);
  }

  public void removeZ()
  {
    while (hasZ())
    {
      removeZAt(0);
    }
  }

  public void addZ(SchemaDouble value)
  {
    setDomChild(Attribute, null, "Z", value.toString());
  }

  public void addZ(String value) throws Exception
  {
    addZ(new SchemaDouble(value));
  }

  public void insertZAt(SchemaDouble value, int index)
  {
    insertDomChildAt(Attribute, null, "Z", index, value.toString());
  }

  public void insertZAt(String value, int index) throws Exception
  {
    insertZAt(new SchemaDouble(value), index);
  }

  public void replaceZAt(SchemaDouble value, int index)
  {
    replaceDomChildAt(Attribute, null, "Z", index, value.toString());
  }

  public void replaceZAt(String value, int index) throws Exception
  {
    replaceZAt(new SchemaDouble(value), index);
  }
}
