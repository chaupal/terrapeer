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
public class XmlException extends RuntimeException
{
  protected java.lang.Exception innerException;
  protected String message;

  public XmlException(String text)
  {
    innerException = null;
    message = text;
  }

  public XmlException(java.lang.Exception other)
  {
    innerException = other;
    message = other.getMessage();
  }

  public String getMessage()
  {
    return message;
  }

  public java.lang.Exception getInnerException()
  {
    return innerException;
  }
}
