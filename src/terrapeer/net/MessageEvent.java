package terrapeer.net;

import java.util.EventObject;
import net.jxta.endpoint.MessageElement;

/**
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P DVE System</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */

public class MessageEvent extends EventObject
{
  int messageType;
  MessageElement messageElement;

  public MessageEvent(Object source, int i, MessageElement msg_element)
  {
    super(source);
    messageType = i;
    messageElement = msg_element;
  }

}
