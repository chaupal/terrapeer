package terrapeer.net;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.EventObject;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredTextDocument;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;
import net.jxta.endpoint.StringMessageElement;
import net.jxta.exception.PeerGroupException;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupFactory;
import net.jxta.pipe.InputPipe;
import net.jxta.pipe.OutputPipe;
import net.jxta.pipe.PipeService;
import net.jxta.platform.ModuleClassID;
import net.jxta.protocol.ModuleSpecAdvertisement;
import net.jxta.protocol.ModuleClassAdvertisement;
import net.jxta.protocol.PipeAdvertisement;

import terrapeer.*;
import java.net.URL;
import java.net.*;
import java.net.*;
import net.jxta.platform.ModuleSpecID;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.pipe.PipeMsgEvent;


/**
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P DVE System</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */
public class PeerService
{
  private static TerraPeerLog myLog = TerraPeerLog.getLogger();

  //Common properties
  private static PeerGroup peerGroup = null;

  //Server-side properties
  private static boolean serverRunning = false;
  private static DiscoveryService discoSvcServer;
  private static PipeService pipeSvcServer;
  private static InputPipe serverPipe; // input pipe for the service

  private static ModuleClassAdvertisement modclass_adv = null;
  private static ModuleSpecAdvertisement modspec_adv = null;
  private static PipeAdvertisement pipe_adv = null;

  //Client-side properties
  private static DiscoveryService discoSvcClient;
  private static PipeService pipeSvcClient;
  private static OutputPipe clientPipe; // Output pipe to connect the service
  private static Message clientMessage = null;
  private static PipeAdvertisement pipeAdv = null;


  /**
   * Constructor
   * @param peerGroup PeerGroup
   */
  public PeerService(PeerGroup peerGroup)
  {
    this.peerGroup = peerGroup;
  }

  /**
   * Start and Stop Server (JXTA Message Receive Service)
   * @param isOn boolean
   */
  public static void startStopServer(boolean isOn)
  {
    serverRunning = isOn;
  }

  /**
   * Start Server
   * @return boolean
   */
  public boolean startServer()
  {
    myLog.addMessage(4, "PeerService.Server: Starting the Server daemon...");
    boolean startServerOK = false;

    // get the discovery and pipe services
    discoSvcServer = peerGroup.getDiscoveryService();
    pipeSvcServer = peerGroup.getPipeService();

    try
    {
      if(createServiceAdv())
      {
        // publish Module class advertisement in local cache and to peergroup (NetPeerGroup)
        discoSvcServer.publish(modclass_adv, DiscoveryService.ADV);
        discoSvcServer.remotePublish(modclass_adv, DiscoveryService.ADV);
        myLog.addMessage(4,"Published MODULE CLASS ADV: "+modclass_adv.getID());

        // add the pipe advertisement to the ModuleSpecAdvertisement
        modspec_adv.setPipeAdvertisement(pipe_adv);

        // display the advertisement as a plain text document.
        myLog.addMessage(4, "PeerService.Server: Created service advertisement:");
        StructuredTextDocument doc = (StructuredTextDocument)
                                     modspec_adv.getDocument(new MimeMediaType("text/plain"));

        StringWriter out = new StringWriter();
        doc.sendToWriter(out);
        myLog.addMessage(4, "PeerService.Server: Advertisement Information\n"+ out.toString());
        out.close();

        // Module advertisement was created; publish in local cache and into NetPeerGroup
        discoSvcServer.publish(modspec_adv, DiscoveryService.ADV);
        discoSvcServer.remotePublish(modspec_adv, DiscoveryService.ADV);

//ADDED
        PipeMsgListener myService1Listener = new PipeMsgListener()
        {
          public void pipeMsgEvent(PipeMsgEvent event)
          {
            Message myMessage = null;
            try
            {
              myLog.addMessage(4, "PeerService.Server: Listener Pipe Message Event!");
              myMessage = event.getMessage();
              checkMessage(myMessage);
              return;
            }
            catch (Exception ee)
            {
              ee.printStackTrace();
              return;
            }
          }
        };


        // create input pipe endpoint clients will use to connect to the service
        //serverPipe = pipeSvcServer.createInputPipe(pipe_adv);
        serverPipe = pipeSvcServer.createInputPipe(pipe_adv, myService1Listener);

        startServerOK = true;
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      myLog.addMessage(2, "PeerService.Server: Error publishing the module");
    }

    serverRunning = true;
    return startServerOK;
  }

  /**
   * Create Service Advertisements: Module class, Module Spec, Pipe
   * @return boolean
   */
  private static boolean createServiceAdv()
  {
    boolean readPipeAdvOK = false;
    // First create the Module class advertisement associated with the service
    // The Module class advertisement is to be used to simply advertise the existence of the service.
    // We build the module class advertisement using the Advertisement Factory class by passing it
    // the type of the advertisement we want to construct. The Module class advertisement is a
    // a very small advertisement that only advertises the existence of service.
    // In order to access the service, a peer will have to discover the associated module spec advertisement.
    modclass_adv = (ModuleClassAdvertisement)
                                     AdvertisementFactory.newAdvertisement(ModuleClassAdvertisement.getAdvertisementType());

    modclass_adv.setName(vars.SERVICE_MODCLASS_ADV);
    modclass_adv.setDescription("TerraPeer test adv; uses JXTA module advertisement Framework");

    ModuleClassID mcID = IDFactory.newModuleClassID();
    modclass_adv.setModuleClassID(mcID);

    // Create the Module Spec advertisement associated with the service
    // We build the module Spec Advertisement using the advertisement Factory class by passing
    // in the type of the advertisement we want to construct. The Module Spec advertisement will contain
    // all the information necessary for a client to contact the service, for instance
    // a pipe advertisement to be used to contact the service
    modspec_adv = (ModuleSpecAdvertisement)
                                    AdvertisementFactory.newAdvertisement(ModuleSpecAdvertisement.getAdvertisementType());

    // Setup some of the information field about the servive.
    // The module creates an input pipes to listen on this pipe endpoint.
    modspec_adv.setName(vars.SERVICE_MODSPEC_ADV);
    modspec_adv.setVersion("Version 1.0");
    modspec_adv.setCreator("cetai.net");
    //USE only ID once created to avoid caching of similar adv
    //--modspec_adv.setModuleSpecID(IDFactory.newModuleSpecID(mcID));
    //++(ModuleSpecID) IDFactory.fromURL(new URL("urn", "","jxta:uuid- <...ID created...>")
    try
    {
      modspec_adv.setModuleSpecID((ModuleSpecID)IDFactory.fromURL(new URL("urn", "",
          "jxta:uuid-E8E423CD80CF4513987EA6A1B3613DB490700CCA50E947F49013B4C8FAEAEE1106")));
    }
    catch (UnknownServiceException ex)
    {
    }
    catch (MalformedURLException ex)
    {
    }
    modspec_adv.setSpecURI("http://www.cetai.net");

    // Create a pipe advertisement for the Service. The client MUST use
    // the same pipe advertisement to talk to the server. When the client
    // discovers the module advertisement it will extract the pipe
    // advertisement to create its pipe. So, we are reading the pipe
    // advertisement from a default config file to ensure that the
    // service will always advertise the same pipefs
    myLog.addMessage(4, "PeerService.Server: Reading in file " + vars.APP_HOME_PATH + vars.SERVICE_ADV_FILENAME);
    try
    {
      FileInputStream is = new FileInputStream(vars.APP_HOME_PATH + vars.SERVICE_ADV_FILENAME);
      pipe_adv = (PipeAdvertisement)AdvertisementFactory.newAdvertisement(new MimeMediaType("text/xml"), is);
      is.close();
      readPipeAdvOK = true;
    }
    catch (java.io.FileNotFoundException fnfe)
    {
      myLog.addMessage(2, "PeerService.Server: File Not Found Exception");
    }
    catch (Exception e)
    {
      myLog.addMessage(0, "PeerService.Server: Failed to read/parse pipe advertisement");
      e.printStackTrace();
      System.exit( -1);
    }

    return readPipeAdvOK;
  }

  /**
   * Server: Wait - Read Messages - send to Parse
   * Types: Normal, DOC, GFX, TXT_TO, TXT_DATA, TXT_TYPE
   */
  public void receiveMessages()
  {
    Message msg = null;

    // Loop over every input received from client (daemon)
    while (serverRunning)
    {
      myLog.addMessage(4, "PeerService.receiveMessages: Waiting for client messages to arrive");

      try
      {
        // Listen on the pipe for a client message
        msg = serverPipe.waitForMessage();
      }
      catch (Exception e)
      {
        serverPipe.close();
        myLog.addMessage(2, "PeerService.receiveMessages: Server Error when listening for message");
        return;
      }

      checkMessage(msg);
    }
  }

  /**
   * Checks the received message elements, and fires event accordingly
   * called from receiveMessages() or PipeMsgListener
   * @param msg Message
   */
  public void checkMessage(Message msg)
  {
    if(msg!=null)
    {
      MessageElement msg_element = null;

      //check SENDER element
      try
      {
        msg_element = msg.getMessageElement(vars.MESSAGE_NAMESPACE, vars.MSG_NAME_From);
        if (msg_element == null)
          myLog.addMessage(2, "PeerService.checkMessage: No sender (from) element!");
        else
          fireMessageEvent(new MessageEvent(this, vars.MSG_TYPE_From, msg_element));
      }
      catch (NullPointerException npe)
      {
        myLog.addMessage(2, "PeerService.checkMessage: getMessageElement NullPointerException - "
                         + vars.MSG_NAME_From);
      }

      //check all other message elements
      for(int i=0; i<vars.MSG_TYPE_COUNT; i++)
      {
        try
        {
          msg_element = msg.getMessageElement(vars.MESSAGE_NAMESPACE, vars.MESSAGE_TYPE[i]);
          if (msg_element == null)
          {
            myLog.addMessage(2, "PeerService.checkMessage: No element " +
                             vars.MESSAGE_NAMESPACE + ":" + vars.MESSAGE_TYPE[i]);
          }
          else
          {
            myLog.addMessage(4, "PeerService.checkMessage: Server Received Message: \n"
                             + "Message Type: " + vars.MESSAGE_TYPE[i]
                             + "\n" + msg_element.toString());
            fireMessageEvent(new MessageEvent(this, i, msg_element));
          }
        }
        catch (NullPointerException npe)
        {
          myLog.addMessage(2, "PeerService.checkMessage: getMessageElement NullPointerException - "
                           + vars.MESSAGE_TYPE[i]);
        }
      }
    }
    else
    {
      myLog.addMessage(2, "PeerService.checkMessage: Null message received.");
    }
  }

  // Create the listener list
  protected static javax.swing.event.EventListenerList listenerList =
      new javax.swing.event.EventListenerList();

  // This methods allows classes to register for MyEvents
  public void addMessageEventListener(MessageEventListener listener)
  {
    listenerList.add(MessageEventListener.class, listener);
  }

  // This methods allows classes to unregister for MyEvents
  public void removeMessageEventListener(MessageEventListener listener)
  {
    listenerList.remove(MessageEventListener.class, listener);
  }

  // This private class is used to fire MyEvents
  private static void fireMessageEvent(MessageEvent evt)
  {
    Object[] listeners = listenerList.getListenerList();
    // Each listener occupies two elements - the first is the listener class
    // and the second is the listener instance
    for (int i = 0; i < listeners.length; i += 2)
    {
      if (listeners[i] == MessageEventListener.class)
      {
        ((MessageEventListener)listeners[i + 1]).myEventOccurred(evt);
      }
    }
  }

  /**
   * Start the client
   */
  public static void startClient()
  {
    myLog.addMessage(4, "PeerService.Client: Starting the Client...");

    // Find service advertisement
    ModuleSpecAdvertisement mdsadv = findServiceAdv();

    try
    {
      // let’s print the advertisement as a plain text document
      StructuredTextDocument doc = (StructuredTextDocument)
                                   mdsadv.getDocument(new MimeMediaType("text/plain"));

      StringWriter out = new StringWriter();
      doc.sendToWriter(out);
      myLog.addMessage(4, "PeerService.Client: Advertisement Information\n"+ out.toString());
      out.close();

      // Get the pipe advertisement -- need it to talk to the service
      pipeAdv = mdsadv.getPipeAdvertisement();
      myLog.addMessage(4, "PeerService.Client: Advertisement Type: " + pipeAdv.getType());

      if (pipeAdv == null)
      {
        myLog.addMessage(2, "PeerService.Client: Error -- Null pipe advertisement!");
        //System.exit(1);
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      myLog.addMessage(2, "PeerService.Client: Error sending message to the service");
    }

    myLog.addMessage(4, "PeerService.Client: Ready to Send Messages");
  }

  /**
   * Send Message using getMessage() to Client Pipe; 3 tries
   */
  public static void sendMessage()
  {
    if(pipeAdv != null)
    {
      // create the output pipe endpoint to connect
      // to the server, try 3 times to bind the pipe endpoint to
      // the listening endpoint pipe of the service
      clientPipe = null;
      for (int i = 0; i < 3; i++)
      {
        myLog.addMessage(4, "PeerService.Client: Trying to bind to pipe... (" + i + ")");
        try
        {
          clientPipe = pipeSvcClient.createOutputPipe(pipeAdv, 10000);
          break;
        }
        catch (java.io.IOException e)
        {
          // go try again
          //e.printStackTrace();
        }
      }
      if (clientPipe == null)
      {
        myLog.addMessage(2, "PeerService.Client: Error resolving pipe endpoint");
        //System.exit(1);
      }

      // send the message to the service pipe
      /*
           String msgStr = "";
           while(getMessage().getMessageElements().hasNext())
        msgStr += ((MessageElement)getMessage().getMessageElements().next()).getElementName()
            + " (" + ((MessageElement)getMessage().getMessageElements().next()).getMimeType().getMimeMediaType()
            + ")\n";
           String msgNStr = getMessage().getMessageNumber() +" elements";
       */
      try
      {
        clientPipe.send(clientMessage);
      }
      catch (IOException ex)
      {
        myLog.addMessage(2, "PeerService.Client: Error sending message");
      }
      //myLog.addMessage(4, "PeerService.Client: Message ["+ msgNStr +"] sent to Server \n" + msgStr);
      myLog.addMessage(4, "PeerService.Client: Message sent to Server");
    }
    else
    {
        myLog.addMessage(2, "PeerService.Client: Pipe Advertisement is null. Start Client first.");
    }
  }

  /**
   * Create Message
   * @param mes MessageElement[]
   */
  public static void createMessage(String nameSpace, MessageElement[] mes)
  {
    // create the pipe message
    clientMessage = new Message();

    // create the data string to send to the server
    //String data = "Hello my friend!";
    //StringMessageElement sme = new StringMessageElement(TAG, data, null);
    //clientMessage.addMessageElement(NAMESPACE, sme);
    for(int i=0; i<mes.length; i++)
      clientMessage.addMessageElement(nameSpace, mes[i]);
  }

  /**
   * Find Service Advertisement
   * @return ModuleSpecAdvertisement
   */
  private static ModuleSpecAdvertisement findServiceAdv()
  {
    // get the discovery and pipe services
    discoSvcClient = peerGroup.getDiscoveryService();
    pipeSvcClient = peerGroup.getPipeService();

    // Let’s try to locate the service advertisement SERVICE
    // we will loop until we find it!
    myLog.addMessage(4, "PeerService.Client: searching for the " + vars.SERVICE_MODSPEC_ADV +
                       " Service advertisement");
    Enumeration enum = null;
    while (true)
    {
      try
      {
        // look first in local cache; try to discover an advertisement
        // which has the (Name, JXTA_SPEC:Terra-S) tag value
        enum = discoSvcClient.getLocalAdvertisements(DiscoveryService.ADV, "Name", vars.SERVICE_MODSPEC_ADV);
        // Found it! Stop searching and go send a message.
        if ((enum != null) && enum.hasMoreElements())
          break;

        // send a remote discovery request searching for the service adv
        discoSvcClient.getRemoteAdvertisements(null, DiscoveryService.ADV, "Name", vars.SERVICE_MODSPEC_ADV, 1, null);

        // The discovery is asynchronous and we do not know
        // how long is going to take (could implement asynchronous listener pipe)
        try
        {
          Thread.sleep(2000);
        }
        catch (Exception e)
        {}
      }
      catch (IOException e)
      {
        // found nothing -- move on
      }
    }

    myLog.addMessage(4, "PeerService.Client: Found the service advertisement");
    return (ModuleSpecAdvertisement)enum.nextElement();
  }

}
