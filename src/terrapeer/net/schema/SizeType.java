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
public class SizeType extends terrapeer.net.xml.Node
{
  public SizeType()
  {
    super();
  }

  public SizeType(SizeType node)
  {
    super(node);
  }

  public SizeType(org.w3c.dom.Node node)
  {
    super(node);
  }

  public SizeType(org.w3c.dom.Document doc)
  {
    super(doc);
  }

  public void adjustPrefix()
  {
    int count;
    count = getDomChildCount(Attribute, null, "width");
    for (int i = 0; i < count; i++)
    {
      org.w3c.dom.Node tmpNode = getDomChildAt(Attribute, null, "width", i);
      internalAdjustPrefix(tmpNode, false);
    }
    count = getDomChildCount(Attribute, null, "height");
    for (int i = 0; i < count; i++)
    {
      org.w3c.dom.Node tmpNode = getDomChildAt(Attribute, null, "height", i);
      internalAdjustPrefix(tmpNode, false);
    }
  }

  public int getwidthMinCount()
  {
    return 0;
  }

  public int getwidthMaxCount()
  {
    return 1;
  }

  public int getwidthCount()
  {
    return getDomChildCount(Attribute, null, "width");
  }

  public boolean haswidth()
  {
    return hasDomChild(Attribute, null, "width");
  }

  public SchemaDouble getwidthAt(int index) throws Exception
  {
    return new SchemaDouble(getDomNodeValue(getDomChildAt(Attribute, null, "width", index)));
  }

  public SchemaDouble getwidth() throws Exception
  {
    return getwidthAt(0);
  }

  public void removewidthAt(int index)
  {
    removeDomChildAt(Attribute, null, "width", index);
  }

  public void removewidth()
  {
    while (haswidth())
    {
      removewidthAt(0);
    }
  }

  public void addwidth(SchemaDouble value)
  {
    setDomChild(Attribute, null, "width", value.toString());
  }

  public void addwidth(String value) throws Exception
  {
    addwidth(new SchemaDouble(value));
  }

  public void insertwidthAt(SchemaDouble value, int index)
  {
    insertDomChildAt(Attribute, null, "width", index, value.toString());
  }

  public void insertwidthAt(String value, int index) throws Exception
  {
    insertwidthAt(new SchemaDouble(value), index);
  }

  public void replacewidthAt(SchemaDouble value, int index)
  {
    replaceDomChildAt(Attribute, null, "width", index, value.toString());
  }

  public void replacewidthAt(String value, int index) throws Exception
  {
    replacewidthAt(new SchemaDouble(value), index);
  }

  public int getheightMinCount()
  {
    return 0;
  }

  public int getheightMaxCount()
  {
    return 1;
  }

  public int getheightCount()
  {
    return getDomChildCount(Attribute, null, "height");
  }

  public boolean hasheight()
  {
    return hasDomChild(Attribute, null, "height");
  }

  public SchemaDouble getheightAt(int index) throws Exception
  {
    return new SchemaDouble(getDomNodeValue(getDomChildAt(Attribute, null, "height", index)));
  }

  public SchemaDouble getheight() throws Exception
  {
    return getheightAt(0);
  }

  public void removeheightAt(int index)
  {
    removeDomChildAt(Attribute, null, "height", index);
  }

  public void removeheight()
  {
    while (hasheight())
    {
      removeheightAt(0);
    }
  }

  public void addheight(SchemaDouble value)
  {
    setDomChild(Attribute, null, "height", value.toString());
  }

  public void addheight(String value) throws Exception
  {
    addheight(new SchemaDouble(value));
  }

  public void insertheightAt(SchemaDouble value, int index)
  {
    insertDomChildAt(Attribute, null, "height", index, value.toString());
  }

  public void insertheightAt(String value, int index) throws Exception
  {
    insertheightAt(new SchemaDouble(value), index);
  }

  public void replaceheightAt(SchemaDouble value, int index)
  {
    replaceDomChildAt(Attribute, null, "height", index, value.toString());
  }

  public void replaceheightAt(String value, int index) throws Exception
  {
    replaceheightAt(new SchemaDouble(value), index);
  }
}
