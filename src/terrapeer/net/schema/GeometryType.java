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
public class GeometryType extends terrapeer.net.xml.Node
{
  public GeometryType()
  {
    super();
  }

  public GeometryType(GeometryType node)
  {
    super(node);
  }

  public GeometryType(org.w3c.dom.Node node)
  {
    super(node);
  }

  public GeometryType(org.w3c.dom.Document doc)
  {
    super(doc);
  }

  public void adjustPrefix()
  {
    int count;
    count = getDomChildCount(Element, null, "Position");
    for (int i = 0; i < count; i++)
    {
      org.w3c.dom.Node tmpNode = getDomChildAt(Element, null, "Position", i);
      internalAdjustPrefix(tmpNode, true);
      new Vector3dType(tmpNode).adjustPrefix();
    }
    count = getDomChildCount(Element, null, "Spatial");
    for (int i = 0; i < count; i++)
    {
      org.w3c.dom.Node tmpNode = getDomChildAt(Element, null, "Spatial", i);
      internalAdjustPrefix(tmpNode, true);
      new SizeType(tmpNode).adjustPrefix();
    }
  }

  public int getPositionMinCount()
  {
    return 1;
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

  public int getSpatialMinCount()
  {
    return 1;
  }

  public int getSpatialMaxCount()
  {
    return 1;
  }

  public int getSpatialCount()
  {
    return getDomChildCount(Element, null, "Spatial");
  }

  public boolean hasSpatial()
  {
    return hasDomChild(Element, null, "Spatial");
  }

  public SizeType getSpatialAt(int index) throws Exception
  {
    return new SizeType(getDomChildAt(Element, null, "Spatial", index));
  }

  public SizeType getSpatial() throws Exception
  {
    return getSpatialAt(0);
  }

  public void removeSpatialAt(int index)
  {
    removeDomChildAt(Element, null, "Spatial", index);
  }

  public void removeSpatial()
  {
    while (hasSpatial())
    {
      removeSpatialAt(0);
    }
  }

  public void addSpatial(SizeType value)
  {
    appendDomElement(null, "Spatial", value);
  }

  public void insertSpatialAt(SizeType value, int index)
  {
    insertDomElementAt(null, "Spatial", index, value);
  }

  public void replaceSpatialAt(SizeType value, int index)
  {
    replaceDomElementAt(null, "Spatial", index, value);
  }
}
