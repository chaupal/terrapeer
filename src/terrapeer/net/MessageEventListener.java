package terrapeer.net;

import java.util.EventListener;

/**
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P DVE System</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */

public interface MessageEventListener extends EventListener
{
  public void myEventOccurred(MessageEvent evt);
}
