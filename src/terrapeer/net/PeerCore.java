package terrapeer.net;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

import net.jxta.rendezvous.*;
import net.jxta.discovery.*;
import net.jxta.document.*;
import net.jxta.document.Advertisement;
import net.jxta.document.MimeMediaType;
import net.jxta.endpoint.*;
import net.jxta.exception.*;
import net.jxta.id.*;
import net.jxta.peergroup.*;
import net.jxta.pipe.*;
import net.jxta.protocol.*;
import net.jxta.util.config.Configurator;
import net.jxta.impl.config.Config;
import net.jxta.impl.util.BidirectionalPipeService;
import net.jxta.impl.document.LiteXMLDocument;
import net.jxta.impl.util.BidirectionalPipeService.*;
import net.jxta.platform.*;

//import org.apache.log4j.*;

import terrapeer.*;
import terrapeer.net.helpers.*;

/**
 *
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P DVE System</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */
public class PeerCore implements Runnable, DiscoveryListener
{
  private static TerraPeerLog myLog = TerraPeerLog.getLogger();
  //private static final Logger LOG = Logger.getLogger(PeerCore.class.getName());

  //PeerCore instantiates MessageManager and PeerService
  public static MessageManager messageMngr = null;
  public static PeerService peerService = null;

  private static PeerGroup peerGroup = null;
  private static PeerGroupAdvertisement groupAd = null;
  private static DiscoveryService discoveryService = null;
  private static PipeService pipeService = null;
  private static RendezVousService rendezvousService = null;
  private static InputPipe inputPipe = null; // input pipe for the service
  private static OutputPipe outputPipe = null; // output pipe for the service
  private static Message msg = null;
  private static ID gid = null; // group id
  private static BidirectionalPipeService bps = null;

  // Currently known rendezvous peer (by stringified peer advertisement)
  private Set rendezvousPeers = new HashSet();

  // Currently known peers and groups
  public static Vector currKnownPeers = new Vector(40);
  public static Vector currKnownPeerGroups = new Vector(40);

  private static String myPrincipal = vars.JXTA_PEER_NAME;
  private static String myPassword = vars.JXTA_PEER_PW;
  private static String configStr = "";

  public boolean JXTASTATUS_IS_ON = true;

  /**
   * Constructor
   */
  public PeerCore()
  {
    this(null);
  }

  /**
   * Constructor
   * @param pg PeerGroup
   */
  public PeerCore(PeerGroup pg)
  {
    this.peerGroup = pg;
  }

  /**
   * Start JXTA Network
   */
  public void startJxta()
  {
    // this is how to obtain the group advertisement
    groupAd = peerGroup.getPeerGroupAdvertisement();

    // get the discovery service
    myLog.addMessage(4, "Retrieving Discovery Service...");
    discoveryService = peerGroup.getDiscoveryService();

    //get pipe service
    myLog.addMessage(4, "Retrieving Pipe Service...");
    pipeService = peerGroup.getPipeService();

    //get rendezvous service
    myLog.addMessage(4, "Retrieving Rendezvous Service...");
    rendezvousService = peerGroup.getRendezVousService();

    //Make peer a Rendezvous
    //registrar for other rendezvous peers + rendezvous peer
    //rendezvousService.startRendezVous();
    //rendezvousPeers.add(advToStr(peerGroup.getPeerAdvertisement()));

    String peerInfoStr = "\nJXTA Peer Settings";
    peerInfoStr += "\n  PeerGroupName: " + peerGroup.getPeerGroupName();
    peerInfoStr += "\n  PeerGroupID: " + peerGroup.getPeerGroupID().toString();
    peerInfoStr += "\n  PeerName: " + peerGroup.getPeerName();
    peerInfoStr += "\n  PeerID: " + peerGroup.getPeerID().toString();
    peerInfoStr += "\n  GroupAdv Name: " + groupAd.getName();
    peerInfoStr += "\n  GroupAdv Type: " + groupAd.getAdvertisementType();
    peerInfoStr += "\n  GroupAdv Description: " + groupAd.getDescription();
    myLog.addMessage(4,"Peer Information:"+peerInfoStr);

    //showJXTAStatus();
  }

  /**
   * Configure JXTA Network
   * @return boolean
   */
  public boolean configureJxta()
  {
    boolean configOK = false;

    try
    {
      System.setProperty("JXTA_HOME", vars.APP_HOME_PATH+vars.JXTA_HOME_PATH);
      System.setProperty("net.jxta.tls.principal", myPrincipal);
      System.setProperty("net.jxta.tls.password", myPassword);
      configureJXTAPlatform();
      configOK = true;
    }
    catch (Exception e)
    {
      myLog.addMessage(2, "PeerCore.configureJxta: Can't set password, continuing...");
    }

    try
    {
      String PeerAdvPath = vars.APP_HOME_PATH+vars.JXTA_HOME_PATH+File.separatorChar+vars.PLATFORM_CFG_ADV_FILE;
      //PeerAdvertisement peerAd = createPeerAd(PeerAdvPath, configStr); //vars.TEST_ADV_FILE

      // create, and Start the default jxta NetPeerGroup
      peerGroup = PeerGroupFactory.newNetPeerGroup();
      //peerGroup = createPeerGroup(myPrincipal, myPassword, peerAd);

      //new peer service
      peerService = new PeerService(peerGroup);
      //new message manager
      messageMngr = new MessageManager(peerService);

      peerService.addMessageEventListener(new MessageEventListener()
                                          {
                                            public void myEventOccurred(MessageEvent evt)
                                            {
                                              // MyEvent was fired
                                              messageMngr.readMessage(evt);
                                            }
                                          }
                                          );

      configOK = true;
    }
    catch (PeerGroupException pge)
    {
      // could not instanciate the group, print the stack and exit
      myLog.addMessage(0, "PeerCore.configureJxta: Fatal error: Group Creation Failure");
      pge.printStackTrace();
      System.exit(1);
    }
    catch (Exception e)
    {
      myLog.addMessage(0, "PeerCore.configureJxta: I/O error: Exception");
      e.printStackTrace();
      System.exit(1);
    }

    if(configOK)
      myLog.addMessage(4, "PeerCore.configureJxta: Peer is now running...");

    return configOK;
  }

  /**
   * Peer C/S Thread
   * runDiscovery() - createPipeAdv() -
   */
  public void run()
  {
    myLog.addMessage(4, "PeerCore.run: Starting PeerCore run() thread...");

    flushDiscovery();
    try
    {
      runDiscovery();
    }
    catch (InterruptedException ex1)
    {
    }

/*
    // create a new BidirectionalPipeService
    bps = new BidirectionalPipeService(peerGroup);

    if(vars.JXTA_PEER_NAME.equals("GForce_PEER_A"))
      runBidirectionalSubmitPipe(readBidirectionalPipeAdv());

    if(vars.JXTA_PEER_NAME.equals("GForce_PEER_B"))
      //runBidirectionalAcceptPipe(initBidirectionalAcceptPipe(null));
      runBidirectionalAcceptPipe(initBidirectionalAcceptPipe(readBidirectionalPipeAdv()));
*/

    createPipeAdv();

/*
    //acts as SERVER
    if (vars.JXTA_PEER_NAME.equals("GForce_PEER_A"))
    {
      if(peerService.startServer())
        peerService.receiveMessages();
      else
        myLog.addMessage(2, "PeerCore.run: Failed to start Server");
    }

    //acts as CLIENT
    if (vars.JXTA_PEER_NAME.equals("GForce_PEER_B"))
    {
      peerService.startClient();
    }
*/

    //acts as SERVER
    myLog.addMessage(4, "   Starting Peer Server...");
    PeerServerSideThread psst = new PeerServerSideThread();
    PeerClientSideThread pcst = new PeerClientSideThread();

    if (!peerService.startServer())
  //    psst.start();
  //  else
      myLog.addMessage(2, "PeerCore.run: Failed to start Server");

    //acts as CLIENT
    myLog.addMessage(4, "   Starting Peer Client...");
    //pcst.start();
    peerService.startClient();

    myLog.addMessage(4, "Peer Server and Client successfully running.");
  }

  private class PeerServerSideThread extends Thread
  {
    public void PeerServerSideThread()
    {
    }

    public void run()
    {
      /*
      try
      {
        this.sleep(300);
      }
      catch (InterruptedException ex)
      {
      }*/
      peerService.receiveMessages();
    }
  }

  private class PeerClientSideThread extends Thread
  {
    public void PeerClientSideThread()
    {
    }

    public void run()
    {
    }
  }

  /**
   * Create PipeAdv - create new pipe advertisement and save to file if not exists
   */
  private void createPipeAdv()
  {
    File advFile = new File(vars.APP_HOME_PATH + vars.SERVICE_ADV_FILENAME);
    if(!advFile.exists())
    {
      myLog.addMessage(4, "PeerCore.run: Creating New Advertisement File");
      try
      {
        PipeAdvertisement pAdv = (PipeAdvertisement)
                                 AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
        pAdv.setPipeID( IDFactory.newPipeID(peerGroup.getPeerGroupID()) );
        pAdv.setName("JXTA_PIPEADV:GFORCE-X");

        String pipeAdvStr = "\nPipe Advertisement:";
        pipeAdvStr += "\n  getID "+pAdv.getID();
        pipeAdvStr += "\n  getName "+pAdv.getName();
        pipeAdvStr += "\n  getPipeID "+pAdv.getPipeID();
        pipeAdvStr += "\n  getAdvType "+pAdv.getAdvType();
        myLog.addMessage(4, "PeerCore.run: Creating PipeAdvertisement..."+pipeAdvStr);

        // Save the advertisement to a file
        myLog.addMessage(4, "   Saving advertisement to file: " + vars.APP_HOME_PATH + vars.SERVICE_ADV_FILENAME + "...");
        saveAdvToFile(pAdv, advFile);
        myLog.addMessage(4, "     ...done.");
      }
      catch (IOException e)
      {
        myLog.addMessage(2, "PeerCore.run: PipeService Init Error: Failed to bind/save pipe advertisement.");
        e.printStackTrace();
      }
    }
  }

  /**
   * Show JXTA Status
   */
  private void showJXTAStatus()
  {
    if(JXTASTATUS_IS_ON)
    {
      String statusTxt = "\nStored LocalAdvertisements: ";
      Enumeration e1 = null;
      Enumeration e2 = null;
      Enumeration e3 = null;
      Enumeration e8 = null;
      try
      {
        e1 = discoveryService.getLocalAdvertisements(discoveryService.ADV, null, null);
        e2 = discoveryService.getLocalAdvertisements(discoveryService.PEER, null, null);
        e3 = discoveryService.getLocalAdvertisements(discoveryService.GROUP, null, null);
        while (e1.hasMoreElements())
        {
          Advertisement ad1 = (Advertisement)e1.nextElement();
          statusTxt += "\n      * ADV [" + ad1.getAdvType() + "] " + ad1.getID() + " ";
        }
        while (e2.hasMoreElements())
        {
          PeerAdvertisement ad2 = (PeerAdvertisement)e2.nextElement();
          currKnownPeers.add(ad2.getName());
          statusTxt += "\n      * PEER-ADV [" + ad2.getAdvType() + "] " + ad2.getName() + " ";
        }
        while (e3.hasMoreElements())
        {
          PeerGroupAdvertisement ad3 = (PeerGroupAdvertisement)e3.nextElement();
          currKnownPeerGroups.add(ad3.getName());
          statusTxt += "\n      * PG-ADV [" + ad3.getAdvType() + "] " + ad3.getName() + " ";
        }
      }
      catch (IOException ex)
      {
        statusTxt = "Exception!";
        ex.printStackTrace();
      }

      statusTxt += "\nRendesvous Information: ";

      if (rendezvousService.isRendezVous())
        statusTxt += "\n      * I am a Rendesvous";
      else
        statusTxt += "\n      * I am not a Rendesvous";

      if (rendezvousService.isConnectedToRendezVous())
      {
        String rdvs = "";
        e8 = rendezvousService.getConnectedRendezVous();
        try
        {
          while (e8.hasMoreElements())
            rdvs += "\n* " + ((PeerGroupAdvertisement)e8.nextElement()).getName();
        }
        catch (Exception ex1)
        {
        }
        statusTxt += "\n      * Connected to Rendesvous: " + rdvs;
      }
      else
        statusTxt += "\n      * No Rendesvous connection";

      myLog.addMessage(4, "PeerCore.showJXTAStatus()..."+statusTxt);
    }
  }

  /**
   * discovering peers
   * every minute, and displaying the results.
   * @throws java.lang.InterruptedException
   */
  public void runDiscovery() throws InterruptedException
  {
    try
    {
      // Add ourselves as a DiscoveryListener for DiscoveryResponse events
      discoveryService.addDiscoveryListener(this);
      myLog.addMessage(4, "PeerCore.runDiscovery: Sending a Discovery Message...");
      // look for any adv
      discoveryService.getRemoteAdvertisements(null, DiscoveryService.ADV, null, null, 10, this);
      // look for any peer
      discoveryService.getRemoteAdvertisements(null, DiscoveryService.PEER, null, null, 10, this);
      // look for any peer group
      discoveryService.getRemoteAdvertisements(null, DiscoveryService.GROUP, null, null, 10, this);

      // wait a bit before sending next discovery message
      try
      {
        Thread.sleep(vars.DISCOVERY_TIME_INTERVAL);
      }
      catch (Exception e)
      {}

      showJXTAStatus();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   * by implementing DiscoveryListener we must define this method
   * to deal to discovery responses
   */
  public void discoveryEvent(DiscoveryEvent ev)
  {
    DiscoveryResponseMsg res = ev.getResponse();
    String name = "unknown";

    // Get the responding peer's advertisement
    PeerAdvertisement peerAdv = res.getPeerAdvertisement();
    // some peers may not respond with their peerAdv
    if (peerAdv != null)
      name = peerAdv.getName();

    myLog.addMessage(4,"Got a Discovery Response [" +
                       res.getResponseCount() + " elements] from peer: " +
                       name);
    //printout each discovered peer
    PeerAdvertisement adv = null;
    Enumeration enum = res.getAdvertisements();
    String advName = "";
    if (enum != null)
    {
      while (enum.hasMoreElements())
      {
        try
        {
          adv = (PeerAdvertisement)enum.nextElement();
          advName = adv.getName();
        }
        catch (Exception ex)
        {
          advName = "N/A";
        }
        myLog.addMessage(4," Peer name/group = " + advName);
      }
    }
  }

  public void flushDiscovery()
  {
    try
    {
      discoveryService.flushAdvertisements(null, discoveryService.ADV);
      discoveryService.flushAdvertisements(null, discoveryService.PEER);
      discoveryService.flushAdvertisements(null, discoveryService.GROUP);
      currKnownPeers = new Vector(40);
      currKnownPeerGroups = new Vector(40);
    }
    catch (IOException ex)
    {
    }
  }

  private BidirectionalPipeService.AcceptPipe initBidirectionalAcceptPipe(PipeAdvertisement pipeAdv)
  {
    // create a new BidirectionalPipeService
    myLog.addMessage(4,"Creating BidirectionalPipeService...");
    AcceptPipe acceptPipe = null;

    if(pipeAdv!=null)
    {
      myLog.addMessage(4, "   Reading existing pipe advertisement");
      try
      {
        acceptPipe = bps.bind(pipeAdv);
      }
      catch (IOException e)
      {
        myLog.addMessage(2, "PipeService Init Error: Failed to bind existing pipe advertisement.");
        e.printStackTrace();
      }
    }
    else
    {
      myLog.addMessage(4, "   Creating new pipe advertisement");
      try
      {
        acceptPipe = bps.bind(vars.BIPIPE_NAME);
        PipeAdvertisement adv = acceptPipe.getAdvertisement();
        // Save the advertisement to a file
        myLog.addMessage(4, "   Saving advertisement to file: " + vars.APP_HOME_PATH + vars.BIPIPE_FILENAME + "...");
        saveAdvToFile(adv, new File(vars.APP_HOME_PATH + vars.BIPIPE_FILENAME));
        myLog.addMessage(4, "     ...done.");
      }
      catch (IOException e)
      {
        myLog.addMessage(2, "PipeService Init Error: Failed to bind/save pipe advertisement.");
        e.printStackTrace();
      }
    }
    return acceptPipe;
  }

  private PipeAdvertisement readBidirectionalPipeAdv()
  {
    PipeAdvertisement pipeAdv = null;
    boolean success = false;

    // Read in the pipe advertisement from the file
    myLog.addMessage(4,"Reading in pipe advertisement from file: " + vars.APP_HOME_PATH+vars.BIPIPE_FILENAME);
    try
    {
      FileInputStream is = new FileInputStream(vars.APP_HOME_PATH+vars.BIPIPE_FILENAME);
      pipeAdv = (PipeAdvertisement)
                AdvertisementFactory.newAdvertisement(new MimeMediaType("text/xml"), is);
      is.close();
      success = true;
    }
    catch (Exception e)
    {
      //e.printStackTrace();
      success = false;
    }

    if(success)
      myLog.addMessage(4,"Successfully read pipe advertisement.");
    else
      myLog.addMessage(2, "PipeService Init File Error: Failed to read/parse pipe advertisement.");

    return pipeAdv;
  }

  private void runBidirectionalSubmitPipe(PipeAdvertisement pipeAdv)
  {
    myLog.addMessage(4,"Now Running Bidirectional Submit Pipe");
    try
    {
      myLog.addMessage(4,"Connecting to Bidirectional Pipe Service...");
      BidirectionalPipeService.Pipe pipe = bps.connect(pipeAdv, 40 * 1000);

      if (pipe != null)
      {
        myLog.addMessage(4,"   ...connected.");


        String xml = "<aha><g>party</g><cool>jxta</cool></aha>";
        MimeMediaType mimeMediaType = MimeMediaType.XMLUTF8;

        StringReader reader = new StringReader(xml);
        LiteXMLDocument liteDoc = null;
        try
        {
         liteDoc = (LiteXMLDocument)LiteXMLDocument.INSTANTIATOR.newInstance(mimeMediaType, reader);

          //System.out.println(document.getValue());   party
          String s = messageMngr.spew("", liteDoc);
        }
        catch (IOException caught)
        {
          myLog.addMessage(2,"LiteXMLDocument exception");
        }
        finally
        {
          reader.close();
        }

        MimeMediaType refMime = new MimeMediaType("Text/Xml");
        StructuredDocumentFactory.Instantiator instantiator = LiteXMLDocument.INSTANTIATOR;
        final String useDocType = "Test";
        StructuredTextDocument doc = null;
        doc = (StructuredTextDocument) instantiator.newInstance( refMime, useDocType );

        // Create a new message and add one element
        Message msg = new Message();
        TextDocumentMessageElement tdme = new TextDocumentMessageElement(vars.BIPIPE_TAG, doc, null);
        StringMessageElement sme = new StringMessageElement(vars.BIPIPE_TAG, "Hello, world", null);
        msg.addMessageElement(null, sme);

        msg.addMessageElement(null, tdme);

        // Send the message
        pipe.getOutputPipe().send(msg);
        myLog.addMessage(4,"Message sent.");
      }
      else
      {
        myLog.addMessage(2,"Failed to establish a bps-connection.");
      }
    }
    catch (Exception e)
    {
      myLog.addMessage(2,"Could not connect to Pipe, probably because of missing Advertisement file.");
      e.printStackTrace();
    }

  }

  private void runBidirectionalAcceptPipe(BidirectionalPipeService.AcceptPipe acceptPipe)
  {
    myLog.addMessage(4,"Now Running Bidirectional Accept Pipe");
    try
    {
      myLog.addMessage(4,"Waiting for messages:");
      while (true)
      {
        try
        {
          BidirectionalPipeService.Pipe pipe = acceptPipe.accept(30 * 1000);
          myLog.addMessage(4,"   BPS: Accepted pipe " + pipe);
          // extract the message from our input pipe
          Message msg = pipe.getInputPipe().poll(30 * 1000);

          MessageElement el = msg.getMessageElement(null, vars.BIPIPE_TAG);
          if (el == null)
          {
            myLog.addMessage(2,"Message Error: Could not find element " + vars.BIPIPE_TAG);
          }
          else
          {
            myLog.addMessage(4,"   Received message: " + el.toString());
          }
        }
        catch (InterruptedException e)
        {
          //timed out; continue
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.exit( -1);
    }
  }

  /**
   * Create the peer group to which we'll belong.
   * @return a <code>PeerGroup</code> value.
   * @throws PeerGroupException if an error occurs.
   * @throws IOException if an error occurs.
   */
  private static PeerGroup createPeerGroup(String principal, String password, PeerAdvertisement peerAd) throws
      PeerGroupException, IOException
  {
    myLog.addMessage(4,"Creating Peer Group");
    JXTAPlatform platform = new JXTAPlatform(peerAd);
    platform.init( /*parent pg*/null, /*id*/ null, /*impl ad*/ null);
    PeerGroup netPeerGroup = PeerGroupFactory.newNetPeerGroup(platform);
    // A specific application group could then use the netPeerGroup to create
    // a new group based on its implementation advertisement at this point by doing
    //     return netPeerGroup.newGroup(myGroupImplAd);
    // For this demo, we just return the netPeerGroup itself.

    return netPeerGroup;
  }

  /**
   * Utility method to convert a peer advertisement in an XML string into a {@link PeerAdvertisement}.
   * @param string a <code>String</code> value.
   * @return a <code>PeerAdvertisement</code> value.
   */
  private static PeerAdvertisement createPeerAd(String adFilename, String adString)
  {
    myLog.addMessage(4,"Reading Peer Advertisement: "+adFilename);
    try
    {
      StringReader reader = new StringReader(adString);

      java.io.FileInputStream fis = new java.io.FileInputStream(adFilename);
      Advertisement ad = AdvertisementFactory.newAdvertisement(MimeMediaType.XML_DEFAULTENCODING, fis);

      //Advertisement ad = AdvertisementFactory.newAdvertisement(MimeMediaType.XML_DEFAULTENCODING, PeerCore.class.getResourceAsStream(adFilename));

      reader.close();
      return (PeerAdvertisement)ad;
    }
    catch (IOException ex)
    {
      throw new IllegalStateException("Unexpected I/O exception: " + ex.getMessage());
    }
  }

  /**
   * Utility function to yield an Advertisement as a String.
   * @param ad an <code>Advertisement</code> value.
   * @return String representation of <var>ad</var>.
   */
  private static String advToStr(PeerAdvertisement ad)
  {
    if (ad == null)
      return "[null]";
    try
    {
      Document doc = ad.getDocument(MimeMediaType.XMLUTF8);
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      doc.sendToStream(out);
      out.close();
      return out.toString();
    }
    catch (IOException ex)
    {
      throw new IllegalStateException("Unexpected I/O exception: " + ex.getMessage());
    }
  }

  /**
   * Configure JXTA Platform
   */
  private static void configureJXTAPlatform()
  {
    myLog.addMessage(4,"Configuring JXTA platform...");
    myLog.addMessage(4,"   Creating Config File: "+Config.JXTA_HOME+vars.PLATFORM_CFG_ADV_FILE);
    File configFile = new File(Config.JXTA_HOME + vars.PLATFORM_CFG_ADV_FILE);
    if (configFile.exists())
    {
      // noting to do
      return;
    }

    myLog.addMessage(4,"   Asking user for peername and proxy.");
    String peername = vars.JXTA_PEER_NAME;//null;
    String httpproxy = null;
/*
    try
    {
      String[] options = new String[]
                         {"OK",
                         "OK & Firewall Config",
                         "Cancel & Expert Config"};
      JOptionPane pane = new JOptionPane("Enter peername (nickname)",
                                         JOptionPane.PLAIN_MESSAGE,
                                         JOptionPane.DEFAULT_OPTION,
                                         null, options, options[0]);
      pane.setWantsInput(true);
      JDialog dialog = pane.createDialog(null, "TerraPeer Configuration");
      dialog.show();
      Object inputValue = pane.getInputValue();
      Object selectedValue = pane.getValue();
      if (inputValue != null && inputValue instanceof String)
      {
        peername = (String)inputValue;
      }

      if (options[0].equals(selectedValue))
      { // OK
        // do nothing
      }
      else if (options[1].equals(selectedValue))
      { // OK & Firewall Config
        httpproxy = JOptionPane.showInputDialog(null,
                                                "Enter HTTP proxy address\n(ex: 192.168.123.45:6789)",
                                                "TerraPeer Configuration", JOptionPane.PLAIN_MESSAGE);
      }
      else
      { // Cancel or CLOSE
        myLog.addMessage(4,"   Canceled, falling back to normal config...");
        return;
      }
    }
    catch (Exception e)
    {
      myLog.addMessage(2,"   Can't continue, falling back to normal config...");
      return;
    }
*/

    if (peername == null || peername.length() == 0)
    {
      myLog.addMessage(2,"   Can't continue, falling back to normal config...");
      return;
    }

    String principal = System.getProperty("net.jxta.tls.principal");
    String password = System.getProperty("net.jxta.tls.password");

    myLog.addMessage(4,"   Creating new configurator...");
    Configurator configurator = new Configurator(peername,
                                                 "TerraPeer Configuration",
                                                 principal,
                                                 password);

    myLog.addMessage(4,"   Setting HTTP and TCP Ports: "+vars.JXTA_HTTP_PORT+","+vars.JXTA_TCP_PORT);
    configurator.setHttpPort(vars.JXTA_HTTP_PORT);
    configurator.setTcpPort(vars.JXTA_TCP_PORT);
    configurator.setEndpointQueueSize(80);

    configurator.setTcpEnabled(true);
    configurator.setRendezVous(false);
    configurator.setRelay(false);
    //configurator.setSecurity(principal, credential);

    myLog.addMessage(4,"   Setting proxy information...");
    if (httpproxy != null && httpproxy.length() > 0)
      configurator.setHttpProxy(httpproxy);

    WellKnownPeerLoader wkpl = null;

    try
    {
      if (httpproxy != null && httpproxy.length() > 0)
      {
        final int separatorIndex = httpproxy.indexOf(":");
        if (separatorIndex < 0)
        {
          throw new IllegalArgumentException(
              "proxy format error. separator(:) not found.");
        }

        final String proxyServer = httpproxy.substring(0, separatorIndex);
        final String proxyPortStr = httpproxy.substring(separatorIndex + 1);
        myLog.addMessage(4,"   Trying to load the Well-Known-Peer information with a proxy..." +
                  "\n     Host:" + proxyServer + "\n     Port:" + proxyPortStr);

        //if proxyPortStr is not number-format, then throw NumberFormatException
        final int proxyPort = Integer.parseInt(proxyPortStr);
        wkpl = new WellKnownPeerLoader(proxyServer, proxyPort);
      }
      else
      {
        myLog.addMessage(4,"   Trying to load the Well-Known-Peer information without a proxy...");
        wkpl = new WellKnownPeerLoader();
      }
    }
    catch (NumberFormatException ex)
    {
      myLog.addMessage(2,"   Sorry, NumberFormatException!");
      ex.printStackTrace();
      wkpl = null;
    }
    catch (IllegalArgumentException ex)
    {
      myLog.addMessage(2,"   Sorry, IllegalArgumentException!");
      ex.printStackTrace();
      wkpl = null;
    }
    catch (IOException ex)
    {
      myLog.addMessage(2,"   Sorry, IOException!");
      ex.printStackTrace();
      wkpl = null;
    }

    // workaround
    myLog.addMessage(4,"   Starting workaround...");
    final String[] tcpRelays;
    if (wkpl != null)
    {
      tcpRelays = wkpl.getTCPRelays();
    }
    else
    {
      tcpRelays = new String[]
                  {
                  "209.25.154.232:9701",
                  "209.25.154.236:9701",
                  "67.114.156.42:9701"};
    }
    myLog.addMessage(4,"     Setting TCP Relays");
    configurator.setTcpRelays(Arrays.asList(tcpRelays));

    final String[] httpRelays;
    if (wkpl != null)
    {
      httpRelays = wkpl.getHTTPRelays();
    }
    else
    {
      httpRelays = new String[]
                   {
                   "209.25.154.232:9700",
                   "209.25.154.236:9700",
                   "67.114.156.42:9700"};
    }
    myLog.addMessage(4,"     Setting HTTP Relays");
    configurator.setHttpRelays(Arrays.asList(httpRelays));

    final String[] rdvs;
    if (wkpl != null)
    {
      rdvs = wkpl.getAllRendezvous();
    }
    else
    {
      rdvs = new String[]
             {
             "http://209.25.154.232:9700",
             "http://209.25.154.236:9700",
             "http://67.114.156.42:9700",
             "http://172.16.1.15:9700",
             "tcp://172.16.1.15:9701",
             "tcp://209.25.154.232:9701",
             "tcp://209.25.154.236:9701",
             "tcp://67.114.156.42:9701"};
    }
    myLog.addMessage(4,"     Setting RendezVous");
    configurator.setRendezVous(Arrays.asList(rdvs));

    try
    {
      configurator.save(configFile.getPath());
      myLog.addMessage(4,"   Configuration saved.");
    }
    catch (IOException e)
    {
      //LOG.warn("couldn't save configuration:" + e);
      myLog.addMessage(2,"   Sorry, could not save configuration!");
    }
  }

  // utilily method; Saves an advertisement in an XML file
  static void saveAdvToFile(Advertisement adv, File f) throws IOException
  {
    try
    {
      InputStream in = adv.getDocument(new MimeMediaType("text/xml")).getStream();
      copy(in, f);
    }
    catch (IOException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new IOException("Can’t get document from adv: " +
                            e.getMessage());
    }
  }

  // utility method; copies an InputStream to a file
  static void copy(InputStream in, File f) throws IOException
  {
    FileOutputStream out = null;
    try
    {
      out = new FileOutputStream(f);
      copy(in, out);
    }
    finally
    {
      if (out != null)
      {
        try
        {
          out.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }
  }

  // Utility method; copies an InputStream to an OutputStream
  static void copy(InputStream in, OutputStream out) throws IOException
  {
    byte[] buf = new byte[8192];
    int r;
    while ((r = in.read(buf)) > 0)
    {
      out.write(buf, 0, r);
    }
  }


}
