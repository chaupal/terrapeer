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
public class URLService extends ServiceBase implements Serializable
{

  public URLService(String id, Vector3d zonePos)
  {
    super(id, zonePos);
  }

}
