package terrapeer.net;

/**
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P feedback system</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */

import java.io.OutputStream;
import java.io.InputStream;
import java.io.*;
import java.util.EventListener;
import java.util.Enumeration;

import net.jxta.document.*;
import net.jxta.document.Advertisement;
import net.jxta.document.MimeMediaType;

import net.jxta.impl.document.LiteXMLDocument;
import net.jxta.impl.document.LiteXMLElement;
import net.jxta.endpoint.MessageElement;
import net.jxta.endpoint.TextDocumentMessageElement;
import net.jxta.endpoint.StringMessageElement;
import net.jxta.impl.document.PlainTextDocument;
import net.jxta.impl.document.PlainTextElement;
import net.jxta.impl.document.DOMXMLDocument;
import net.jxta.impl.document.DOMXMLElement;

import terrapeer.*;
import terrapeer.vui.zone.*;
import terrapeer.net.schema.ZoneType;
import net.jxta.endpoint.TextMessageElement;
import terrapeer.net.schema.terrapeer_v1Doc;

/**
 * Manages all TerraPeer messages between peers
 *
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */
public class MessageManager
{
  private static TerraPeerLog myLog = TerraPeerLog.getLogger();

  private static ZoneRepository zoneRep = new ZoneRepository();
  private static PeerService peerService = null;

  private static long msgID = 0;
  private static int msgType = 0;
  private static int msgLength = 0;
  private static String msgContent = null;
  private static String msgX3Dscene = null;
  private static Zone msgWorldID = null;

  private static String lastMsgFrom = vars.JXTA_PEER_NAME;

  private static MimeMediaType mimeMediaTypeXML = MimeMediaType.XMLUTF8;

  /**
   * Constructor
   * @param peerService PeerService
   */
  public MessageManager(PeerService peerService)
  {
    this.peerService = peerService;
  }

  /**
   *
   * @param id long
   * @param type int
   * @return int
   */
  public static int prepMessage(long id, int type)
  {
    msgID = id;
    msgType = type;
    msgLength = msgContent.length();
    return msgLength;
  }

  public void sendZoneRequest(String[] zoneIDs)
  {

  }

  /**
   * Send MyZone
   */
  public void sendMyZone()
  {
    String z = zoneRep.loadMyZoneFromFileAsRaw();
    myLog.addMessage(4,"MessageManager sending MyZone:\nSTART>>>"+ z +"<<<EOF");
    z = z.trim();
    peerService.createMessage(vars.MESSAGE_NAMESPACE, createXMLMessage(z));
    peerService.sendMessage();

/*
    ZoneType z = zoneRep.loadMyZoneFromFileAsXML();
    peerService.createMessage(vars.MESSAGE_NAMESPACE, createZoneMessage(z));
    peerService.sendMessage();
*/
  }

  /**
   * Send Zones by ID
   * @param zoneID String
   */
  public void sendZones(String toZoneID, String[] zoneIDs)
  {
    String z = "";
    z += zoneRep.loadZoneFromFileAsRaw(zoneIDs[1]);
    myLog.addMessage(4,"MessageManager sending Zones:\nSTART>>>"+ z +"<<<EOF");
    z = z.trim();
    createXMLMessage(z);
    peerService.sendMessage();
  }

  public void sendZoneDataRequest()
  {

  }

  public void sendZoneData(String toZoneID)
  {

  }

  public void sendServiceRequest(String toZoneID, String[] serviceIDs)
  {

  }

  public void sendService(String toZoneID, String serviceID)
  {

  }

  public void sendSubscriptionRequest(String toZoneID, String[] subIDs)
  {

  }

  public void sendSubscription(String toZoneID, String[] subIDs)
  {

  }

  public void sendListenerRequest(String toZoneID)
  {

  }

  public void sendNotification(String toZoneID)
  {

  }

  public void sendTextMessage(String toZoneID, String text)
  {

  }

  public void sendStatus(String toZoneID, String statusMsg)
  {

  }

  private void createMessage(int msgType, int type, String id, String txt, Object obj)
  {
    String nameSpace = vars.MESSAGE_NAMESPACE;
    MessageElement[] mes = new MessageElement[5];


    peerService.createMessage(nameSpace, mes);
  }

  private MessageElement[] createZoneMessage(ZoneType z)
  {
    myLog.addMessage(4,"MessageManager.createZoneMessage...");
    MessageElement[] mes = new MessageElement[2];

    StringMessageElement sme = new StringMessageElement(vars.MSG_NAME_From, vars.JXTA_PEER_NAME, null);
    mes[0] = sme;

    //org.w3c.dom.Document ddoc = (org.w3c.dom.Document)z.getDomNode();
    //javax.xml.transform.Source source = new javax.xml.transform.dom.DOMSource(ddoc);
    //liteDoc = (LiteXMLDocument)LiteXMLDocument.INSTANTIATOR.newInstance(mimeMediaTypeXML, );
    XMLDocument xmlDoc = (XMLDocument) z.getDomNode().getOwnerDocument();
    myLog.addMessage(4, "   XMLDocument created: \n" + xmlDoc.toString());

    TextDocumentMessageElement tdme2 = new TextDocumentMessageElement(vars.MSG_NAME_Zones, xmlDoc, null);
    mes[1] = tdme2;

    return mes;
  }

  /**
   * Create a Message, Type GFX
   * @param xmlStr String
   */
  private MessageElement[] createXMLMessage(String xmlStr)
  {
    myLog.addMessage(4,"MessageManager.createXMLMessage...");
    MessageElement[] mes = new MessageElement[2];

    StringMessageElement sme = new StringMessageElement(vars.MSG_NAME_From, vars.JXTA_PEER_NAME, null);
    mes[0] = sme;

    StringReader reader = new StringReader(xmlStr);
    LiteXMLDocument liteDoc = null;
    try
    {
      liteDoc = (LiteXMLDocument)LiteXMLDocument.INSTANTIATOR.newInstance(mimeMediaTypeXML, reader);
      myLog.addMessage(4, "   LiteXMLDocument created: \n" + spew("", liteDoc));
    }
    catch (Exception e)
    {
      myLog.addMessage(2, "   LiteXMLDocument exception \n"+e.getMessage());
    }
    finally
    {
      reader.close();
    }
    TextDocumentMessageElement tdme2 = new TextDocumentMessageElement(vars.MSG_NAME_Zones, liteDoc, null);
    mes[1] = tdme2;

    return mes;
  }

  /**
   * Create a Message, Type TXT
   * @param toName String
   * @param txtStr String
   * @param typeStr String
   */
  private MessageElement[] createTXTMessage(String toName, String txtStr, String fromName)
  {
    myLog.addMessage(4,"PeerCore.createTXTMessage...");
    MessageElement[] mes = new MessageElement[3];

    StringMessageElement sme = new StringMessageElement(vars.MSG_NAME_HELO_TextMessage, toName, null);
    mes[0] = sme;
    sme = new StringMessageElement(vars.MSG_NAME_TXT, txtStr, null);
    mes[1] = sme;
    sme = new StringMessageElement(vars.MSG_NAME_From, fromName, null);
    mes[2] = sme;

    return mes;
  }

  /**
   * Create a TEST Message
   */
  private void createTestMessage()
  {
    myLog.addMessage(4,"MessageManager.createTestMessage...");
    MessageElement[] mes = new MessageElement[3];
    MimeMediaType mimeMediaTypeXML = MimeMediaType.XMLUTF8;
    MimeMediaType mimeMediaTypeTest = new MimeMediaType("Text/Xml");
    final String useDocTypeTest = "Test";
    final String useDocTypeTP_GFX = "TerraPeer:GFX";

    StringMessageElement sme = new StringMessageElement(vars.MSG_NAME_TXT, "Hello, world", null);
    mes[0] = sme;

    StructuredDocumentFactory.Instantiator instantiator = LiteXMLDocument.INSTANTIATOR;
    StructuredTextDocument doc = (StructuredTextDocument)instantiator.newInstance(mimeMediaTypeTest, useDocTypeTest);
    TextDocumentMessageElement tdme = new TextDocumentMessageElement(vars.MSG_NAME_XML, doc, null);
    mes[1] = tdme;

    String xml = "<?xml version='1.0'?><aha><g>party</g><cool>jxta</cool></aha>";

    StringReader reader = new StringReader(xml);
    LiteXMLDocument liteDoc = null;
    try
    {
      liteDoc = (LiteXMLDocument)LiteXMLDocument.INSTANTIATOR.newInstance(mimeMediaTypeXML, reader);
      myLog.addMessage(4, "   LiteXMLDocument created: \n" + spew("", liteDoc));
    }
    catch (IOException caught)
    {
      myLog.addMessage(2, "   LiteXMLDocument exception");
    }
    finally
    {
      reader.close();
    }
    TextDocumentMessageElement tdme2 = new TextDocumentMessageElement(vars.MSG_NAME_XML, liteDoc, null);
    mes[2] = tdme2;

    peerService.createMessage(vars.MESSAGE_NAMESPACE, mes);
  }


  /**
   * Catches event from Server:receiveMessage()
   * @param evt MessageEvent
   */
  public static void readMessage(MessageEvent evt)
  {
    parseMessage(evt.messageType, evt.messageElement);
  }

  /**
   * Parse Messages according to type
   * @param msg_type int
   * @param msg_element MessageElement
   */
  private static void parseMessage(int msg_type, MessageElement msg_element)
  {
    myLog.addMessage(4,"MessageManager.parseMessage...");

    //check SENDER
    if(msg_type == vars.MSG_TYPE_From)
      lastMsgFrom = msg_element.toString().trim();

    //if sender is self, don't parse
    if(vars.JXTA_PEER_NAME.equals(lastMsgFrom))
    {
      myLog.addMessage(2, "MessageManager.parseMessage: Last Message from self");
    }
    else
    {
      switch (msg_type)
      {
        case vars.MSG_TYPE_X:
          break;
        case vars.MSG_TYPE_TXT:
          break;
        case vars.MSG_TYPE_XML:
          break;
        case vars.MSG_TYPE_X3D:
          break;
        case vars.MSG_TYPE_BIN:
          break;
        case vars.MSG_TYPE_GEO:
          break;
        case vars.MSG_TYPE_Zones:
  /*
          //TextDocumentMessageElement tdme = (TextDocumentMessageElement)msg_element;
          TextMessageElement tdme = (TextMessageElement)msg_element;
          LiteXMLDocument ldoc = null;
          try
          {
            ldoc = (LiteXMLDocument)LiteXMLDocument.INSTANTIATOR.newInstance(mimeMediaTypeXML, tdme.getReader());
          }
          catch (IOException ex)
          {
            myLog.addMessage(2, "MessageManager.parseMessage: LiteXMLDocument IOException");
          }
          storeReceivedZoneData(ldoc.toString());
  */
          try
          {
            XMLDocument doc = (XMLDocument)StructuredDocumentFactory.newStructuredDocument(msg_element.getMimeType(),msg_element.getStream());
            //org.w3c.dom.Document ddoc = (org.w3c.dom.Document)doc;
            //ZoneType z = new ZoneType(ddoc);
            storeReceivedZoneData(doc.toString());
          }
          catch (IOException ex)
          {
          }
          //storeReceivedZoneData(msg_element.toString());
          break;
        case vars.MSG_TYPE_ZoneData:
          break;
        case vars.MSG_TYPE_Blocks:
          break;
        case vars.MSG_TYPE_Services:
          break;
        case vars.MSG_TYPE_REQ_All:
          break;
        case vars.MSG_TYPE_REQ_HaveZones:
          break;
        case vars.MSG_TYPE_REQ_RequestZones:
          break;
        case vars.MSG_TYPE_REQ_MaxZones:
          break;
        case vars.MSG_TYPE_REQ_LevelOfDetail:
          break;
        case vars.MSG_TYPE_REQ_Services:
          break;
        case vars.MSG_TYPE_REQ_SubscribeService:
          break;
        case vars.MSG_TYPE_REQ_Listener:
          break;
        case vars.MSG_TYPE_RPLY_SubscriptionOK:
          break;
        case vars.MSG_TYPE_RPLY_NotificationOK:
          break;
        case vars.MSG_TYPE_HELO_Notification:
          break;
        case vars.MSG_TYPE_HELO_TextMessage:
          break;
        case vars.MSG_TYPE_HELO_Status:
          break;
        default:
          break;
      }
    }
  }

  /**
   * Store Received Zone Data
   * @param type int
   * @param data String
   */
  private static void storeReceivedZoneData(String data)
  {
    myLog.addMessage(4, "MessageManager.storeReceivedZoneData...");
    if (!zoneRep.addZoneToRepository(data))
      myLog.addMessage(2, "MessageManager: Could not add ZoneData to Repository");
  }

  private static void storeReceivedZoneData(ZoneType z)
  {
    myLog.addMessage(4, "MessageManager.storeReceivedZoneData...");
    if (!zoneRep.addZoneToRepository(z))
      myLog.addMessage(2, "MessageManager: Could not add ZoneData to Repository");
  }

  /**
   * Utility method to recoursively print a XML doc
   * @param content String
   * @param element LiteXMLElement
   * @return String
   */
  public String spew(String content, LiteXMLElement element)
  {
    content += ((element.getName()!=null) ? "<"+element.getName()+">" : "")
        + ((element.getTextValue()!=null) ? element.getTextValue() :"");

    Enumeration children = element.getChildren();
    while (children.hasMoreElements())
      content += spew("", (LiteXMLElement)children.nextElement());

    content += "</"+element.getName()+">";
    return content;
  }

}
