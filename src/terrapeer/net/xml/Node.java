package terrapeer.net.xml;

/**
 *
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */
public abstract class Node implements java.io.Serializable
{
  final protected static short Attribute = 0;
  final protected static short Element = 1;

  protected static String getDomNodeValue(org.w3c.dom.Node node)
  {
    if (node == null)
    {
      return null;
    }
    String value = node.getNodeValue();
    if (value != null)
    {
      return value;
    }
    org.w3c.dom.Node child = node.getFirstChild();
    if (child != null && child.getNodeType() == org.w3c.dom.Node.TEXT_NODE)
    {
      return child.getNodeValue();
    }
    else
    {
      return null;
    }
  }

  protected static void setDomNodeValue(org.w3c.dom.Node node, String value)
  {
    if (node == null)
    {
      return;
    }
    if (node.getNodeValue() != null)
    {
      node.setNodeValue(value);
      return;
    }
    org.w3c.dom.Node child = node.getFirstChild();
    if (child == null || child.getNodeType() != org.w3c.dom.Node.TEXT_NODE)
    {
      node.appendChild(node.getOwnerDocument().createTextNode(value));
      return;
    }
  }

  protected org.w3c.dom.Node domNode = null;

  public Node()
  {
    domNode = Document.createTemporaryDomNode();
  }

  public Node(Node node)
  {
    domNode = node.domNode;
  }

  public Node(org.w3c.dom.Node domNode)
  {
    this.domNode = domNode;
  }

  public Node(org.w3c.dom.Document domDocument)
  {
    this.domNode = domDocument.getDocumentElement();
  }

  public org.w3c.dom.Node getDomNode()
  {
    return domNode;
  }

  public void mapPrefix(String prefix, String URI)
  {
    if (URI == null || URI.equals(""))
    {
      return;
    }

    org.w3c.dom.Element element = (org.w3c.dom.Element)domNode;
    if (prefix == null || prefix.equals(""))
    {
      element.setAttribute("xmlns", URI);
    }
    else
    {
      element.setAttribute("xmlns:" + prefix, URI);
    }
  }

  protected void declareNamespace(String prefix, String URI)
  {
    if (URI == null || URI.equals(""))
    {
      return;
    }
    org.w3c.dom.Element root = domNode.getOwnerDocument().getDocumentElement();

    org.w3c.dom.NamedNodeMap attrs = root.getAttributes();
    if (attrs != null)
    {
      for (int i = 0; i < attrs.getLength(); i++)
      {
        org.w3c.dom.Attr attr = (org.w3c.dom.Attr)attrs.item(i);
        if (attr.getValue().equals(URI)) // namespace URI already mapped?
        {
          return; // do not overwrite
        }
      }
    }

    if (prefix == null || prefix.equals(""))
    {
      root.setAttribute("xmlns", URI);
    }
    else
    {
      root.setAttribute("xmlns:" + prefix, URI);
    }
  }

  protected org.w3c.dom.Node appendDomChild(int type, String namespaceURI, String name, String value)
  {
    switch (type)
    {
      case Attribute:
        org.w3c.dom.Attr attribute = domNode.getOwnerDocument().createAttributeNS(namespaceURI, name);
        attribute.setNodeValue(value);
        domNode.getAttributes().setNamedItemNS(attribute);
        return attribute;

      case Element:
        org.w3c.dom.Element element = domNode.getOwnerDocument().createElementNS(namespaceURI, name);
        if (value != null && !value.equals(""))
        {
          element.appendChild(domNode.getOwnerDocument().createTextNode(value));
        }
        domNode.appendChild(element);
        return element;

      default:
        throw new XmlException("Unknown type");
    }
  }

  protected boolean domNodeNameEquals(org.w3c.dom.Node node, String namespaceURI, String localName)
  {
    if (node == null)
    {
      return false;
    }
    String nodeURI = node.getNamespaceURI() == null ? "" : node.getNamespaceURI();
    String nodeLocalName = node.getLocalName() == null ? "" : node.getLocalName();
    if (namespaceURI == null)
    {
      namespaceURI = "";
    }
    if (localName == null)
    {
      localName = "";
    }
    return nodeURI.equals(namespaceURI) && nodeLocalName.equals(localName);
  }

  protected int getDomChildCount(int type, String namespaceURI, String name)
  {
    switch (type)
    {
      case Attribute:
        return ((org.w3c.dom.Element)domNode).hasAttributeNS(namespaceURI, name) ? 1 : 0;

      case Element:
        org.w3c.dom.NodeList elements = domNode.getChildNodes();
        int length = elements.getLength();
        int count = 0;

        for (int i = 0; i < length; i++)
        {
          org.w3c.dom.Node child = elements.item(i);
          if (domNodeNameEquals(child, namespaceURI, name))
          {
            count++;
          }
        }
        return count;

      default:
        throw new XmlException("Unknown type");
    }
  }

  protected boolean hasDomChild(int type, String namespaceURI, String name)
  {
    switch (type)
    {
      case Attribute:
        return ((org.w3c.dom.Element)domNode).hasAttributeNS(namespaceURI, name) ? true : false;

      case Element:
        org.w3c.dom.NodeList elements = domNode.getChildNodes();
        int length = elements.getLength();
        for (int i = 0; i < length; i++)
        {
          if (domNodeNameEquals(elements.item(i), namespaceURI, name))
          {
            return true;
          }
        }
        return false;

      default:
        throw new XmlException("Unknown type");
    }
  }

  protected org.w3c.dom.Node getDomChildAt(int type, String namespaceURI, String name, int index)
  {
    int length, count = 0;

    switch (type)
    {
      case Attribute:
        return domNode.getAttributes().getNamedItemNS(namespaceURI, name);

      case Element:
        org.w3c.dom.NodeList elements = domNode.getChildNodes();
        length = elements.getLength();
        for (int i = 0; i < length; i++)
        {
          org.w3c.dom.Node child = elements.item(i);
          if (domNodeNameEquals(child, namespaceURI, name) && count++ == index)
          {
            return child;
          }
        }
        throw new XmlException("Index out of range");

      default:
        throw new XmlException("Unknown type");
    }
  }

  protected org.w3c.dom.Node getDomChild(int type, String namespaceURI, String name)
  {
    return getDomChildAt(type, namespaceURI, name, 0);
  }

  protected org.w3c.dom.Node insertDomChildAt(int type, String namespaceURI, String name, int index, String value)
  {
    if (type == Attribute)
    {
      return appendDomChild(type, namespaceURI, name, value);
    }
    else
    {
      org.w3c.dom.Element element = domNode.getOwnerDocument().createElementNS(namespaceURI, name);
      element.appendChild(domNode.getOwnerDocument().createTextNode(value));
      return domNode.insertBefore(element, getDomChildAt(Element, namespaceURI, name, index));
    }
  }

  protected org.w3c.dom.Node insertDomElementAt(String namespaceURI, String name, int index,
                                                terrapeer.net.xml.Node srcNode)
  {
    srcNode.domNode = domNode.insertBefore(
        cloneDomElementAs(namespaceURI, name, srcNode),
        getDomChildAt(Element, namespaceURI, name, index)
        );
    return srcNode.domNode;
  }

  protected org.w3c.dom.Node replaceDomChildAt(int type, String namespaceURI, String name, int index, String value)
  {
    if (type == Attribute)
    {
      return appendDomChild(type, namespaceURI, name, value);
    }
    else
    {
      org.w3c.dom.Element element = domNode.getOwnerDocument().createElementNS(namespaceURI, name);
      element.appendChild(domNode.getOwnerDocument().createTextNode(value));
      return domNode.replaceChild(element, getDomChildAt(Element, namespaceURI, name, index));
    }
  }

  protected org.w3c.dom.Node replaceDomElementAt(String namespaceURI, String name, int index,
                                                 terrapeer.net.xml.Node srcNode)
  {
    srcNode.domNode = domNode.replaceChild(
        cloneDomElementAs(namespaceURI, name, srcNode),
        getDomChildAt(Element, namespaceURI, name, index)
        );
    return srcNode.domNode;
  }

  protected org.w3c.dom.Node setDomChildAt(int type, String namespaceURI, String name, String value, int index)
  {
    int length, count = 0;

    switch (type)
    {
      case Attribute:
        org.w3c.dom.Attr oldAttr = ((org.w3c.dom.Element)domNode).getAttributeNodeNS(namespaceURI, name);
        ((org.w3c.dom.Element)domNode).setAttributeNS(namespaceURI, name, value);
        return oldAttr;

      case Element:
        org.w3c.dom.NodeList elements = domNode.getChildNodes();
        length = elements.getLength();
        for (int i = 0; i < length; i++)
        {
          org.w3c.dom.Node child = elements.item(i);
          if (domNodeNameEquals(child, namespaceURI, name) && count++ == index)
          {
            return child.replaceChild(child.getOwnerDocument().createTextNode(value), child.getFirstChild());
          }
        }
        throw new XmlException("Index out of range");

      default:
        throw new XmlException("Unknown type");
    }
  }

  protected org.w3c.dom.Node setDomChild(int type, String namespaceURI, String name, String value)
  {
    if (type == Attribute || getDomChildCount(type, namespaceURI, name) > 0)
    {
      return setDomChildAt(type, namespaceURI, name, value, 0);
    }
    else
    {
      appendDomChild(type, namespaceURI, name, value);
      return null;
    }
  }

  protected org.w3c.dom.Node removeDomChildAt(int type, String namespaceURI, String name, int index)
  {
    int length, count = 0;

    switch (type)
    {
      case Attribute:
        return domNode.getAttributes().removeNamedItemNS(namespaceURI, name);

      case Element:
        org.w3c.dom.NodeList elements = domNode.getChildNodes();
        length = elements.getLength();
        for (int i = 0; i < length; i++)
        {
          org.w3c.dom.Node child = elements.item(i);
          if (domNodeNameEquals(child, namespaceURI, name) && count++ == index)
          {
            return domNode.removeChild(child);
          }
        }
        throw new XmlException("Index out of range");

      default:
        throw new XmlException("Unknown type");
    }
  }

  protected org.w3c.dom.Node appendDomElement(String namespaceURI, String name, terrapeer.net.xml.Node srcNode)
  {
    srcNode.domNode = domNode.appendChild(cloneDomElementAs(namespaceURI, name, srcNode));
    return srcNode.domNode;
  }

  protected org.w3c.dom.Element cloneDomElementAs(String namespaceURI, String name, terrapeer.net.xml.Node srcNode)
  {
    org.w3c.dom.Element newDomNode = domNode.getOwnerDocument().createElementNS(namespaceURI, name);
    org.w3c.dom.Element srcDomNode = (org.w3c.dom.Element)srcNode.domNode;
    org.w3c.dom.Document doc = newDomNode.getOwnerDocument();

    org.w3c.dom.NodeList list = srcDomNode.getChildNodes();
    int length = list.getLength();
    for (int i = 0; i < length; i++)
    {
      newDomNode.appendChild(doc.importNode(list.item(i), true));

    }
    org.w3c.dom.NamedNodeMap srcAttributes = srcDomNode.getAttributes();
    org.w3c.dom.NamedNodeMap newAttributes = newDomNode.getAttributes();
    length = srcAttributes.getLength();
    for (int i = 0; i < length; i++)
    {
      newAttributes.setNamedItemNS((org.w3c.dom.Attr)doc.importNode(srcAttributes.item(i), false));

    }
    return newDomNode;
  }

  protected void cloneInto(org.w3c.dom.Element newDomNode)
  {
    while (domNode.getFirstChild() != null)
    {
      org.w3c.dom.Node n = newDomNode.getOwnerDocument().importNode(domNode.getFirstChild(), true);
      newDomNode.appendChild(n);
      domNode.removeChild(domNode.getFirstChild());
    }

    org.w3c.dom.NamedNodeMap srcAttributes = ((org.w3c.dom.Element)domNode).getAttributes();
    org.w3c.dom.NamedNodeMap newAttributes = newDomNode.getAttributes();
    while (srcAttributes.getLength() > 0)
    {
      org.w3c.dom.Node n = srcAttributes.item(0);
      newAttributes.setNamedItem(newDomNode.getOwnerDocument().importNode(n, true));
      srcAttributes.removeNamedItem(n.getNodeName());
    }

    domNode = newDomNode;
  }

  protected static String lookupPrefix(org.w3c.dom.Node node, String URI)
  {
    if (node == null || URI == null || URI.equals(""))
    {
      return null;
    }

    if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE)
    {
      org.w3c.dom.NamedNodeMap attrs = node.getAttributes();
      if (attrs != null)
      {
        int len = attrs.getLength();
        for (int i = 0; i < len; i++)
        {
          org.w3c.dom.Attr attr = (org.w3c.dom.Attr)attrs.item(i);
          String name = attr.getName();
          String value = attr.getValue();
          if (value != null && value.equals(URI))
          {
            if (name.startsWith("xmlns:"))
            {
              return name.substring(6);
            }
            else
            {
              return null;
            }
          }
        }
      }
      return lookupPrefix(node.getParentNode(), URI);
    }
    else if (node.getNodeType() == org.w3c.dom.Node.ATTRIBUTE_NODE)
    {
      return lookupPrefix(((org.w3c.dom.Attr)node).getOwnerElement(), URI);
    }
    else
    {
      return null;
    }
  }

  protected static void internalAdjustPrefix(org.w3c.dom.Node node, boolean qualified)
  {
    if (node != null && qualified)
    {
      String prefix = lookupPrefix(node, node.getNamespaceURI());
      if (prefix != null)
      {
        node.setPrefix(prefix);
      }
    }
  }

  public abstract void adjustPrefix();
}
