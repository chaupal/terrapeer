package terrapeer.vui.service;

import java.net.URL;
import java.io.Serializable;
import javax.media.j3d.Node;
import javax.vecmath.*;


/**
 *
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */
public class ServiceBase extends VObject implements Serializable
{
  private static URL url;

  public ServiceBase(String id, Vector3d zonePos)
  {
    super(id, zonePos);
  }

  public URL getURL()
  {
    return url;
  }
  public void setURL(URL url)
  {
    this.url = url;
  }

}
