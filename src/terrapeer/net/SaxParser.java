package terrapeer.net;

import java.io.IOException;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import javax.xml.parsers.*;

public class SaxParser extends DefaultHandler
{

  public static void main(String[] argv)
  {
    if (argv.length != 1)
    {
      System.out.println("Usage: java terrapeer.SaxParser [URI]");
      System.exit(0);
    }
    System.setProperty("javax.xml.parsers.SAXParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");
    String uri = argv[0];
    try
    {
      SAXParserFactory parserFactory = SAXParserFactory.newInstance();
      parserFactory.setValidating(true);
      parserFactory.setNamespaceAware(true);
      parserFactory.setFeature("http://xml.org/sax/features/namespace-prefixes",true);
      SaxParser SaxParserInstance = new SaxParser();
      SAXParser parser = parserFactory.newSAXParser();
      parser.parse(uri, SaxParserInstance);
    }
    catch(IOException ex)
    {
      ex.printStackTrace();
    }
    catch(SAXException ex)
    {
      ex.printStackTrace();
    }
    catch(ParserConfigurationException ex)
    {
      ex.printStackTrace();
    }
    catch(FactoryConfigurationError ex)
    {
      ex.printStackTrace();
    }

  }

  public void characters(char[] ch, int start, int length) throws SAXException
  {
    /**@todo Implement this characters method*/
    throw new java.lang.UnsupportedOperationException("Method characters() not yet implemented.");
  }

  public void endDocument() throws SAXException
  {
    /**@todo Implement this endDocument method*/
    throw new java.lang.UnsupportedOperationException("Method endDocument() not yet implemented.");
  }

  public void endElement(String uri, String localName, String qName) throws SAXException
  {
    /**@todo Implement this endElement method*/
    throw new java.lang.UnsupportedOperationException("Method endElement() not yet implemented.");
  }

  public void processingInstruction(String target, String data) throws SAXException
  {
    /**@todo Implement this processingInstruction method*/
    throw new java.lang.UnsupportedOperationException("Method processingInstruction() not yet implemented.");
  }

  public void setDocumentLocator(String target)
  {
    /**@todo Implement this setDocumentLocator method*/
    throw new java.lang.UnsupportedOperationException("Method setDocumentLocator() not yet implemented.");
  }

  public void startDocument() throws SAXException
  {
    /**@todo Implement this startDocument method*/
    throw new java.lang.UnsupportedOperationException("Method startDocument() not yet implemented.");
  }

  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
  {
    /**@todo Implement this startElement method*/
    throw new java.lang.UnsupportedOperationException("Method startElement() not yet implemented.");
  }

  public void error(SAXParseException e) throws SAXException
  {
    /**@todo Implement this error method*/
    throw new java.lang.UnsupportedOperationException("Method error() not yet implemented.");
  }

  public void fatalError(SAXParseException e) throws SAXException
  {
    /**@todo Implement this fatalError method*/
    throw new java.lang.UnsupportedOperationException("Method fatalError() not yet implemented.");
  }

  public void warning(SAXParseException e) throws SAXException
  {
    /**@todo Implement this warning method*/
    throw new java.lang.UnsupportedOperationException("Method warning() not yet implemented.");
  }
}