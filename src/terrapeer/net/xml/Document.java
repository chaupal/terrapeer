package terrapeer.net.xml;

import java.util.*;

/**
 *
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */
public abstract class Document implements java.io.Serializable
{
  protected static javax.xml.parsers.DocumentBuilderFactory factory = null;
  protected static javax.xml.parsers.DocumentBuilder builder = null;
  protected static org.w3c.dom.Document tmpDocument = null;
  protected static org.w3c.dom.DocumentFragment tmpFragment = null;
  protected static int tmpNameCounter = 0;

  protected static synchronized javax.xml.parsers.DocumentBuilder getDomBuilder()
  {
    try
    {
      if (builder == null)
      {
        if (factory == null)
        {
          factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
          factory.setIgnoringElementContentWhitespace(true);
          factory.setNamespaceAware(true);
          //factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
          //factory.setValidating(true);
        }
        builder = factory.newDocumentBuilder();

        builder.setErrorHandler(new org.xml.sax.ErrorHandler()
        {
          public void warning(org.xml.sax.SAXParseException e)
          {
          }

          public void error(org.xml.sax.SAXParseException e) throws XmlException
          {
            throw new XmlException(e);
          }

          public void fatalError(org.xml.sax.SAXParseException e) throws XmlException
          {
            throw new XmlException(e);
          }
        });
      }
      return builder;
    }
    catch (javax.xml.parsers.ParserConfigurationException e)
    {
      throw new XmlException(e);
    }
  }

  protected static synchronized org.w3c.dom.Document getTemporaryDocument()
  {
    if (tmpDocument == null)
    {
      tmpDocument = getDomBuilder().newDocument();
    }
    return tmpDocument;
  }

  protected static synchronized org.w3c.dom.Node createTemporaryDomNode()
  {
    String tmpName = "_" + tmpNameCounter++;
    if (tmpFragment == null)
    {
      tmpFragment = getTemporaryDocument().createDocumentFragment();
      tmpDocument.appendChild(tmpFragment);
    }
    org.w3c.dom.Node node = getTemporaryDocument().createElement(tmpName);
    tmpFragment.appendChild(node);
    return node;
  }

  protected String rootElementName = null;
  protected String namespaceURI = null;
  protected String schemaLocation = null;

  public Document()
  {
  }

  public void setRootElementName(String namespaceURI, String rootElementName)
  {
    this.namespaceURI = namespaceURI;
    this.rootElementName = rootElementName;
  }

  public void setSchemaLocation(String schemaLocation)
  {
    this.schemaLocation = schemaLocation;
  }

  public org.w3c.dom.Node load(String filename)
  {
    try
    {
      return getDomBuilder().parse(new java.io.File(filename)).getDocumentElement();
    }
    catch (org.xml.sax.SAXException e)
    {
      throw new XmlException(e);
    }
    catch (java.io.IOException e)
    {
      throw new XmlException(e);
    }
  }

  public org.w3c.dom.Node load(java.io.InputStream istream)
  {
    try
    {
      return getDomBuilder().parse(istream).getDocumentElement();
    }
    catch (org.xml.sax.SAXException e)
    {
      throw new XmlException(e);
    }
    catch (java.io.IOException e)
    {
      throw new XmlException(e);
    }
  }

  public void save(String filename, Node node)
  {
    finalizeRootElement(node);

    Node.internalAdjustPrefix(node.domNode, true);
    node.adjustPrefix();

    internalSave(
        new javax.xml.transform.stream.StreamResult(
        new java.io.File(filename)
        ),
        node.domNode.getOwnerDocument()
        );
  }

  public void save(java.io.OutputStream ostream, Node node)
  {
    finalizeRootElement(node);

    Node.internalAdjustPrefix(node.domNode, true);
    node.adjustPrefix();

    internalSave(
        new javax.xml.transform.stream.StreamResult(ostream),
        node.domNode.getOwnerDocument()
        );
  }

  protected static void internalSave(javax.xml.transform.Result result, org.w3c.dom.Document doc)
  {
    try
    {
      javax.xml.transform.Source source
          = new javax.xml.transform.dom.DOMSource(doc);
      javax.xml.transform.Transformer transformer
          = javax.xml.transform.TransformerFactory.newInstance().newTransformer();
      transformer.transform(source, result);
    }
    catch (javax.xml.transform.TransformerConfigurationException e)
    {
      throw new XmlException(e);
    }
    catch (javax.xml.transform.TransformerException e)
    {
      throw new XmlException(e);
    }
  }

  public org.w3c.dom.Node transform(Node node, String xslFilename)
  {
    try
    {
      javax.xml.transform.TransformerFactory factory = javax.xml.transform.TransformerFactory.newInstance();
      javax.xml.transform.Transformer transformer = factory.newTransformer(
          new javax.xml.transform.stream.StreamSource(xslFilename)
          );

      javax.xml.transform.dom.DOMResult result = new javax.xml.transform.dom.DOMResult();
      transformer.transform(
          new javax.xml.transform.dom.DOMSource(node.domNode),
          result
          );

      return result.getNode();
    }
    catch (javax.xml.transform.TransformerException e)
    {
      throw new XmlException(e);
    }
  }

  protected void finalizeRootElement(Node root)
  {
    if (root.domNode.getParentNode().getNodeType() != org.w3c.dom.Node.DOCUMENT_FRAGMENT_NODE)
    {
      return;
    }

    if (rootElementName == null || rootElementName.equals(""))
    {
      throw new XmlException("Call setRootElementName first");
    }

    org.w3c.dom.Document doc = getDomBuilder().newDocument();
    org.w3c.dom.Element newRootElement = doc.createElementNS(namespaceURI, rootElementName);
    root.cloneInto(newRootElement);
    doc.appendChild(newRootElement);

    newRootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
    if (namespaceURI == null || namespaceURI.equals(""))
    {
      if (schemaLocation != null && schemaLocation != "")
      {
        newRootElement.setAttribute("xsi:noNamespaceSchemaLocation", schemaLocation);
      }
    }
    else
    {
      if (schemaLocation != null && schemaLocation != "")
      {
        newRootElement.setAttribute("xsi:schemaLocation", namespaceURI + " " + schemaLocation);
      }
    }

    root.domNode = newRootElement;
    declareNamespaces(root);
  }

  public abstract void declareNamespaces(Node node);

  protected void declareNamespace(Node node, String prefix, String URI)
  {
    node.declareNamespace(prefix, URI);
  }
}
