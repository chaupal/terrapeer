package terrapeer.vui.zone;

import terrapeer.*;
import terrapeer.vui.*;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.*;
import terrapeer.vui.j3dui.utils.app.*;
import terrapeer.vui.j3dui.utils.objects.*;
import terrapeer.vui.j3dui.utils.blocks.*;

import terrapeer.net.xml.*;
import terrapeer.net.xml.types.*;
import terrapeer.net.schema.*;

import terrapeer.vui.service.*;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import net.jxta.document.XMLDocument;

/**
 * All Zone I/O to local storage
 *
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */
public class ZoneRepository
{
  /** Log */
  private static TerraPeerLog myLog = TerraPeerLog.getLogger();
  /** Dialog for selecting model file to be loaded. */
  private static FileDialog selectModelDialog;
  /** Repository filename */
  private static String repositoryFilename;

  private static String myZoneID = "CCCP_1";


  public ZoneRepository()
  {
    this(vars.ZONE_REPOSITORY_FILE);
  }

  public ZoneRepository(String repositoryFilename)
  {
    this.repositoryFilename = repositoryFilename;
    createXMLRepository();
  }

  /**
   * Create XML Repository (current repositoryFilename), if it does not exist
   */
  public static void createXMLRepository()
  {
    myLog.addMessage(4, "ZoneRepository: Checking if Repository exists...");
    File f = new File(repositoryFilename);
    if(!f.exists())
    {
      myLog.addMessage(4, "   Creating New Repository...");
      terrapeer_v1Doc doc = new terrapeer_v1Doc();
      TerraPeerType root = new TerraPeerType();
      doc.setRootElementName("", "TerraPeer");
      doc.setSchemaLocation("terrapeer_v1.xsd"); // optional
      doc.save(repositoryFilename, root);
    }
    myLog.addMessage(4, "   Repository: "+f.getName()+" is OK");
  }

  /**
   * Save a Zone z to the TerraPeer XML repositry
   * @param z Zone
   * @return boolean True if success
   */
  public static boolean saveZone(Zone objZone)
  {
    myLog.addMessage(4, "ZoneRepository: Saving Zone");
    ZoneType xmlZone = null;
    xmlZone = convertZoneObj2XML(xmlZone, objZone);
    return addZoneToRepository(xmlZone);
  }

  /**
   * Adds a Zone XML data directly to the Repository (current repositoryFilename)
   * @param zoneXML String
   * @return boolean
   */
  public static boolean addZoneToRepository(String zoneXML)
  {
    myLog.addMessage(4, "ZoneRepository.addZoneToRepository...");
    //remove leading and trailing <TerraPeer> tag
    if(zoneXML.startsWith("<?xml"))// && zoneXML.startsWith("<TerraPeer>", 40))
      zoneXML = zoneXML.substring(51, zoneXML.length());
    if(zoneXML.trim().endsWith("</TerraPeer>"))
      zoneXML = zoneXML.substring(0, zoneXML.length()-13);
    //System.out.println(zoneXML);

    //rm leading spaces?
    //if(!zoneXML.startsWith("<Zone>") || !zoneXML.trim().endsWith("</Zone>"))
    //  return false;

    String rep = loadRepositoryFileAsRaw();
    //add zone to repository XML; handle leading and trailing tags
    if(rep.trim().endsWith("</ZoneWorld>\n</TerraPeer>"))
      rep = rep.substring(0, rep.length()-26) + zoneXML + "</ZoneWorld>\n</TerraPeer>";
    else
      return false;

    if(!saveRepositoryFileAsRaw(rep))
      return false;

    return true;
  }

  /**
   * Adds a Zone to the Repository (current repositoryFilename)
   * @param z ZoneType
   * @return boolean
   */
  public static boolean addZoneToRepository(ZoneType z)
  {
    TerraPeerType root = loadTerraPeerXML(repositoryFilename);
    try
    {
      root.getZoneWorld().addZone(z);
    }
    catch (Exception ex)
    {
      myLog.addMessage(2, "ZoneRepository: Error Adding Zone!");
      return false;
    }
    terrapeer_v1Doc d = new terrapeer_v1Doc();
    d.save(repositoryFilename, root);
    return true;
  }

  /**
   * Save My Zone (Obj->XML); replace existing data
   * @param objZone Zone
   * @return boolean
   */
  public static boolean saveMyZone(Zone objZone)
  {
    myLog.addMessage(4, "ZoneRepository: Saving MyZone");
    TerraPeerType root = loadTerraPeerXML(repositoryFilename);
    ZoneType xmlZone = null;
    xmlZone = convertZoneObj2XML(xmlZone, objZone);
    try
    {
      root.replaceZoneAt(xmlZone, 0);
    }
    catch (Exception ex)
    {
      myLog.addMessage(2, "   Error Saving MyZone!");
      return false;
    }
    return true;
  }

  /**
   * Load My Zone ID to load actual Zone data
   * @return String
   */
  public static String loadMyZoneID()
  {
    myLog.addMessage(4, "ZoneBuilder: Loading Zone ID...");
    if(myZoneID.equals("CCCP_1"))
    {
      try
      {
        myZoneID = loadTerraPeerXML(repositoryFilename).getZone().getID().asString();
      }
      catch (Exception ex)
      {
      }
    }
    return myZoneID;
  }

  /**
   * Load My Zone from the TerraPeer XML repositry
   * @return Zone The Zone Object
   */
  public static Zone loadMyZoneFromFile()
  {
    myLog.addMessage(4, "ZoneRepository: Loading MyZone as Object");
    TerraPeerType root = loadTerraPeerXML(repositoryFilename);
    Zone objZone = null;
    try
    {
      root.getZone().getID().asString();
      objZone = convertZoneXML2Obj(loadMyZoneID(), root.getZone(), objZone);
    }
    catch (Exception e)
    {
      myLog.addMessage(2, e.getMessage());
    }
    return objZone;
  }

  /**
   * Load My Zone from the TerraPeer XML repositry
   * @return ZoneType The Zone XML
   */
  public static ZoneType loadMyZoneFromFileAsXML()
  {
    myLog.addMessage(4, "ZoneRepository: Loading MyZone as XML Type");
    TerraPeerType root = loadTerraPeerXML(repositoryFilename);
    ZoneType z = null;
    try
    {
      z = root.getZone();
    }
    catch (Exception ex)
    {
    }
    return z;
  }

  /**
   * Load My Zone from the TerraPeer XML repositry
   * @return String The Zone XML
   */
  public String loadMyZoneFromFileAsRaw()
  {
    myLog.addMessage(4, "ZoneRepository: Loading MyZone as raw XML");
    TerraPeerType root = loadTerraPeerXML(repositoryFilename);
    ZoneType z = null;
    try
    {
      z = root.getZone();
    }
    catch (Exception ex)
    {
      myLog.addMessage(2, "ZoneRepository.loadMyZoneFromFileAsRaw: Exception");
    }

    String s = "";
    root = loadTerraPeerXML("ara.xml");
    try
    {
      root.addZone(z);
    }
    catch (Exception ex)
    {
    }

    //terrapeer_v1Doc d = new terrapeer_v1Doc();
    //d.save("ara.xml", root);

    //writeXmlFile(root.getDomNode().getOwnerDocument(),"ara.xml");

    OutputStream ostream = new ByteArrayOutputStream();
    try
    {
      Source source = new DOMSource(root.getDomNode().getOwnerDocument());
      Result result = new StreamResult(ostream);
      Transformer xformer = TransformerFactory.newInstance().newTransformer();
      xformer.transform(source, result);
    }
    catch (TransformerConfigurationException e)
    {
    }
    catch (TransformerException e)
    {
    }
    s = ostream.toString();

    //s = loadFileAsRaw("ara.xml");
    return s;
  }


  // This method writes a DOM document to a file
  public static void writeXmlFile(org.w3c.dom.Document doc, String filename)
  {
    try
    {
      // Prepare the DOM document for writing
      Source source = new DOMSource(doc);

      // Prepare the output file
      File file = new File(filename);
      Result result = new StreamResult(file);

      // Write the DOM document to the file
      Transformer xformer = TransformerFactory.newInstance().newTransformer();
      xformer.transform(source, result);
    }
    catch (TransformerConfigurationException e)
    {
    }
    catch (TransformerException e)
    {
    }
  }

  // Parses an XML file and returns a DOM document.
  // If validating is true, the contents is validated against the DTD
  // specified in the file.
  public static org.w3c.dom.Document parseXmlFile(String filename, boolean validating)
  {
    try
    {
      // Create a builder factory
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setValidating(validating);

      // Create the builder and parse the file
      org.w3c.dom.Document doc = factory.newDocumentBuilder().parse(new File(filename));
      return doc;
    }
    catch (SAXException e)
    {
      // A parsing error occurred; the xml input is not valid
    }
    catch (ParserConfigurationException e)
    {
    }
    catch (IOException e)
    {
    }
    return null;
  }

  /**
   * Load Repository from File As Raw Text
   * @return String
   */
  public static String loadRepositoryFileAsRaw()
  {
    return loadFileAsRaw(repositoryFilename);
  }

  /**
   * loadFileAsRaw
   * @param fileName String
   * @return String
   */
  public static String loadFileAsRaw(String fileName)
  {
    myLog.addMessage(4, "ZoneRepository: Loading Repository as Raw XML");
    File f = new File(fileName);
    String rStr = "";
    String str;
    try
    {
      FileReader fr = new FileReader(f);
      BufferedReader in = new BufferedReader(fr);
      while ((str = in.readLine()) != null)
        rStr += str + "\n";
      in.close();
    }
    catch (IOException e)
    {
      myLog.addMessage(2, "ZoneRepository.loadRepositoryFileAsRaw: IO Exception");
    }
    //System.out.println(rStr);
    return rStr;
  }

  /**
   * Save Repository to File As Raw Text
   * @return String
   */
  public static boolean saveRepositoryFileAsRaw(String data)
  {
    return saveFileAsRaw(repositoryFilename, data);
  }

  /**
   * Save Raw Text to File
   * @param fileName String
   * @param data String
   * @return boolean
   */
  public static boolean saveFileAsRaw(String fileName, String data)
  {
    myLog.addMessage(4, "ZoneRepository: Saving Repository as Raw XML");
    File f = new File(fileName);
    try
    {
      FileWriter fw = new FileWriter(f);
      BufferedWriter out = new BufferedWriter(fw);
      out.write(data);
      out.close();
    }
    catch (IOException e)
    {
      myLog.addMessage(2, "ZoneRepository.saveRepositoryFileAsRaw: IO Exception");
      return false;
    }
    return true;
  }

  /**
   * Load all Zones from the TerraPeer XML Repository
   * @return ZoneType
   */
  public static Zone[] loadAllZonesFromRep()
  {
    TerraPeerType root = loadTerraPeerXML(repositoryFilename);
    Zone[] z = null;
    ZoneType zt = null;
    try
    {
      z = new Zone[root.getZoneWorld().getZoneCount()];
      //run through all zones in ZoneWorld
      for (int i = 0; i < root.getZoneWorld().getZoneCount(); i++)
      {
        zt = new ZoneType();
        //z[i] = new Zone(root.getZoneWorld().getZoneAt(i).getID().asString());
        z[i] = convertZoneXML2Obj(root.getZoneWorld().getZoneAt(i).getID().asString(),
                                  root.getZoneWorld().getZoneAt(i), z[i]);
      }
    }
    catch(Exception e)
    {
      myLog.addMessage(2, e.getMessage());
    }
    return z;
  }

  /**
   * Load a Zone from the TerraPeer XML Repository by its ID
   * @param zoneID String The ID of the requested Zone
   * @return Zone The Zone
   */
  public static Zone loadZoneFromFile(String zoneID)
  {
    myLog.addMessage(4, "ZoneRepository: Loading Zone");
    ZoneType xmlZone = searchZoneByID(zoneID);
    Zone objZone = null;
    try
    {
      objZone = convertZoneXML2Obj(zoneID, xmlZone, objZone);
    }
    catch (Exception e)
    {
      myLog.addMessage(2, e.getMessage());
    }
    return objZone;
  }

  /**
   * Load a Zone from the TerraPeer XML repositry by its ID
   * @param zoneID String The ID of the requested Zone
   * @return ZoneType The Zone Raw XML
   */
  public static String loadZoneFromFileAsRaw(String zoneID)
  {
    myLog.addMessage(4, "ZoneRepository: Loading Zone as Raw XML");
    ZoneType xmlZone = searchZoneByID(zoneID);

    String z = "";




//todo

    return z;
  }

  /**
   * Converts a Zone Object to XML format (ZoneType)
   * @param xmlZone ZoneType
   * @param objZone Zone
   * @return ZoneType
   */
  public static ZoneType convertZoneObj2XML(ZoneType xmlZone, Zone objZone)
  {
    if(objZone!=null)
    {
      try
      {
        xmlZone = new ZoneType();
        GeometryType g = new GeometryType();
        Vector3dType v3d = new Vector3dType();
        SizeType st = new SizeType();
        xmlZone.addID(objZone.getZone_ID());
        xmlZone.addName(objZone.getZone_Name());
        xmlZone.addDescription(objZone.getZone_Description());
        v3d.addX(String.valueOf(objZone.myGeometry.getPosition().x));
        v3d.addY(String.valueOf(objZone.myGeometry.getPosition().y));
        v3d.addZ(String.valueOf(objZone.myGeometry.getPosition().z));
        st.addheight(String.valueOf(objZone.myGeometry.getTwoPoint_H()));
        st.addwidth(String.valueOf(objZone.myGeometry.getTwoPoint_W()));
        g.addPosition(v3d);
        g.addSpatial(st);
        xmlZone.addGeometry(g);
        xmlZone.addLastUpdated(String.valueOf(Calendar.getInstance().getTimeInMillis()));
        xmlZone.addVersion(vars.ZONE_REPOSITORY_VER);

        for(int o=0; o<objZone.myObjects.getVObjectCount(); o++)
        {
          BaseObjectType bot = new BaseObjectType();
          v3d = new Vector3dType();
          bot.addObjectID(String.valueOf(objZone.myObjects.getVObject(o).getId()));
          bot.addName(String.valueOf(objZone.myObjects.getVObject(o).getName()));
          v3d.addX(String.valueOf(objZone.myObjects.getVObject(o).getPosition().x));
          v3d.addY(String.valueOf(objZone.myObjects.getVObject(o).getPosition().y));
          v3d.addZ(String.valueOf(objZone.myObjects.getVObject(o).getPosition().z));
          bot.addPosition(v3d);
          bot.addBBTYPE(String.valueOf(objZone.myObjects.getVObject(o).getBbType()));
          //? bot.addDescription(String.valueOf(objZone.myObjects.getVObject(o).getId()));
          bot.addLocalFileName(String.valueOf(objZone.myObjects.getVObject(o).getFileName()));
          xmlZone.addBaseObject(bot);
        }
        for(int s=0; s<objZone.myServices.getServiceCount(); s++)
        {
          ServiceObjectType sot = new ServiceObjectType();
          v3d = new Vector3dType();
          sot.addID(String.valueOf(objZone.myServices.getURLService(s).getId()));
          sot.addName(String.valueOf(objZone.myServices.getURLService(s).getName()));
          v3d.addX(String.valueOf(objZone.myServices.getURLService(s).getPosition().x));
          v3d.addY(String.valueOf(objZone.myServices.getURLService(s).getPosition().y));
          v3d.addZ(String.valueOf(objZone.myServices.getURLService(s).getPosition().z));
          sot.addPosition(v3d);
          sot.addBBTYPE(String.valueOf(objZone.myServices.getURLService(s).getBbType()));
          sot.addLocalFileName(String.valueOf(objZone.myServices.getURLService(s).getFileName()));
          //sot.addDescription();
          sot.addWeb_URL(String.valueOf(objZone.myServices.getURLService(s).getFileName()));
          //sot.addFTP_URL();
          //sot.addBinaryContent();
          xmlZone.addServiceObject(sot);
        }
      }
      catch (Exception e)
      {
        myLog.addMessage(2, e.getMessage());
      }
    }
    return xmlZone;
  }

  /**
   * Converts a XML Zone Type to a Zone Object Type, which is created new with ID
   * All virtual objects (base as well as services) within a Zone are included.
   * ! Conversation loss in types !
   * Virtual objects IDs are generated on-the-fly
   * @param zoneID String
   * @param xmlZone ZoneType
   * @param objZone Zone
   * @return Zone
   */
  public static Zone convertZoneXML2Obj(String zoneID, ZoneType xmlZone, Zone objZone)
  {
    if(xmlZone!=null)
    {
      try
      {
        objZone = new Zone(zoneID.trim());
        objZone.setZone_Name((String)xmlZone.getName().getValue().trim());
        objZone.setZone_Description((String)xmlZone.getDescription().getValue().trim());
        //(String)xmlZone.getVersion().getValue()
        //(String)xmlZone.getLastUpdated().getValue()
        objZone.myGeometry.setTwoPoint_H((int)xmlZone.getGeometry().getSpatial().getheight().getValue());
        objZone.myGeometry.setTwoPoint_W((int)xmlZone.getGeometry().getSpatial().getwidth().getValue());
        objZone.myGeometry.setPosition(new Vector3d(xmlZone.getGeometry().getPosition().getX().getValue(),
                                                    xmlZone.getGeometry().getPosition().getY().getValue(),
                                                    xmlZone.getGeometry().getPosition().getZ().getValue()));
        for(int o=0; o<xmlZone.getBaseObjectCount(); o++)
        {
          //not used: xmlZone.getBaseObjectAt(o).getObjectID().getValue()
          //xmlZone.getBaseObjectAt(o).getDescription()
          objZone.myObjects.addVObject( new VObject(""+xmlZone.getBaseObjectAt(o).getObjectID().getValue().trim(),
              objZone.myGeometry.getPosition(),
              xmlZone.getBaseObjectAt(o).getName().getValue().trim(),
              xmlZone.getBaseObjectAt(o).getLocalFileName().getValue().trim(),
              xmlZone.getBaseObjectAt(o).getBBTYPE().getValue(),
              new Vector3d(xmlZone.getBaseObjectAt(o).getPosition().getX().getValue(),
                           xmlZone.getBaseObjectAt(o).getPosition().getY().getValue(),
                           xmlZone.getBaseObjectAt(o).getPosition().getZ().getValue())));
        }
        for(int s=0; s<xmlZone.getServiceObjectCount(); s++)
        {
          //not used: xmlZone.getBaseObjectAt(s).getObjectID()
          //xmlZone.getServiceObjectAt(s).getDescription()
          //xmlZone.getServiceObjectAt(s).getBinaryContent()
          //xmlZone.getServiceObjectAt(s).getFTP_URL()
          //xmlZone.getServiceObjectAt(s).getWeb_URL()
          /*
          objZone.myObjects.addVObject(new VObject("TERRA-Z000"+objZone.getZone_ID()+"-B-"+xmlZone.getBaseObjectAt(o).getBBTYPE().getValue()+"-"+s,
              objZone.myGeometry.getPosition(),
              xmlZone.getServiceObjectAt(s).getName().getValue(),
              xmlZone.getServiceObjectAt(s).getLocalFileName().getValue(),
              xmlZone.getServiceObjectAt(s).getBBTYPE().getValue(),
              new Vector3d(xmlZone.getServiceObjectAt(s).getPosition().getX().getValue(),
                           xmlZone.getServiceObjectAt(s).getPosition().getY().getValue(),
                           xmlZone.getServiceObjectAt(s).getPosition().getZ().getValue())));
           */
          objZone.myServices.addURLService(xmlZone.getServiceObjectAt(s).getName().getValue(),
                                           new Vector3d(xmlZone.getServiceObjectAt(s).getPosition().getX().getValue(),
              xmlZone.getServiceObjectAt(s).getPosition().getY().getValue(),
              xmlZone.getServiceObjectAt(s).getPosition().getZ().getValue()),
                                           objZone.myGeometry.getPosition(),
                                           xmlZone.getServiceObjectAt(s).getWeb_URL().getValue());
        }
      }
      catch (Exception e)
      {
        myLog.addMessage(2, e.getMessage());
      }
    }
    return objZone;
  }

  /**
   * Loads the TerraPeer XML repositry,
   * and prints out its content (all data fields) in a string
   * @param zoneID String
   * @return String Zone data content
   */
  public static String printTerraPeerXMLContent()
  {
    myLog.addMessage(4, "ZoneRepository: Printing TerraPeer XML Repository");
    TerraPeerType root = loadTerraPeerXML(repositoryFilename);
    String zoneXMLContent = "";

    try
    {
      zoneXMLContent += '\n' + "================================================";
      zoneXMLContent += '\n' + "TerraPeer XML File content overview";
      zoneXMLContent += '\n' + "================================================";
      zoneXMLContent += '\n' + "ID = " + root.getID();
      zoneXMLContent += '\n' + "LastUpdated = " + root.getLastUpdated();
      zoneXMLContent += '\n' + "Name = " + root.getName();
      zoneXMLContent += '\n' + "Version = " + root.getVersion();
      zoneXMLContent += '\n' + "MyZone Present = " + root.getZoneCount();
      zoneXMLContent += '\n' + "ZoneWorld ZoneCount = " + root.getZoneWorld().getZoneCount();
      zoneXMLContent += '\n' + "________________________________________________";

      ZoneType z = root.getZone();
      zoneXMLContent += '\n' + "____";
      zoneXMLContent += '\n' + "MyZone";
      zoneXMLContent += '\n' + "   ID = " + z.getID();
      zoneXMLContent += '\n' + "   Description = " + z.getDescription();
      zoneXMLContent += '\n' + "   LastUpdated = " + z.getLastUpdated();
      zoneXMLContent += '\n' + "   Name = " + z.getName();
      zoneXMLContent += '\n' + "   Version = " + z.getVersion();
      zoneXMLContent += '\n' + "   BaseObject.getBBTYPE = " +
          z.getBaseObject().getBBTYPE();
      zoneXMLContent += '\n' + "   BaseObject.getLocalFileName = " +
          z.getBaseObject().getLocalFileName();
      zoneXMLContent += '\n' + "   Geometry Position = " +
          z.getGeometry().getPosition().getX() + "," +
          z.getGeometry().getPosition().getY();
      zoneXMLContent += '\n' + "   NameCount = " + z.getNameCount();
      zoneXMLContent += '\n' + "________________________________________________";
      zoneXMLContent += '\n' + "ZoneWorld";

      for (int i = 0; i < root.getZoneWorld().getZoneCount(); i++)
      {
        z = root.getZoneWorld().getZoneAt(i);
        zoneXMLContent += '\n' + "____";
        zoneXMLContent += '\n' + "ZONE";
        zoneXMLContent += '\n' + "   ID = " + z.getID();
        zoneXMLContent += '\n' + "   LastUpdated = " + z.getLastUpdated();
        zoneXMLContent += '\n' + "   Name = " + z.getName();
        zoneXMLContent += '\n' + "   Description = " + z.getDescription();
        zoneXMLContent += '\n' + "   Version = " + z.getVersion();
        zoneXMLContent += '\n' + "   BaseObject BBTYPE = " +
            z.getBaseObject().getBBTYPE();
        zoneXMLContent += '\n' + "   BaseObject LocalFileName = " +
            z.getBaseObject().getLocalFileName();
        zoneXMLContent += '\n' + "   Geometry Position = " +
            z.getGeometry().getPosition().getX() + "," +
            z.getGeometry().getPosition().getY();
        zoneXMLContent += '\n' + "   NameCount = " + z.getNameCount();
      }
      zoneXMLContent += '\n' + "________________________________________________";
      zoneXMLContent += '\n' + "EOF" + '\n';
    }
    catch(Exception e)
    {
      myLog.addMessage(2, e.getMessage());
      return null;
    }

    return zoneXMLContent;
  }

  /**
   * Prints the XML Content of a Zone with specified ID
   * @param zoneID String
   * @return String
   */
  public static String printZoneXMLContent(String zoneID)
  {
    myLog.addMessage(4, "ZoneRepository: Printing Zone XML (ID "+zoneID+")");
    TerraPeerType root = loadTerraPeerXML(repositoryFilename);
    String zoneXMLContent = "";
    ZoneType z = searchZoneByID(zoneID);

    try
    {
      zoneXMLContent += '\n' + "================================================";
      zoneXMLContent += '\n' + "TerraPeer XML File content overview";
      zoneXMLContent += '\n' + "================================================";
      zoneXMLContent += '\n' + "ID = " + root.getID();
      zoneXMLContent += '\n' + "LastUpdated = " + root.getLastUpdated();
      zoneXMLContent += '\n' + "Name = " + root.getName();
      zoneXMLContent += '\n' + "Version = " + root.getVersion();
      zoneXMLContent += '\n' + "MyZone Present = " + root.getZoneCount();
      zoneXMLContent += '\n' + "ZoneWorld ZoneCount = " + root.getZoneWorld().getZoneCount();
      zoneXMLContent += '\n' + "________________________________________________";
      zoneXMLContent += '\n' + "ZONE";

      zoneXMLContent += '\n' + "   ID = " + z.getID();
      zoneXMLContent += '\n' + "   Description = " + z.getDescription();
      zoneXMLContent += '\n' + "   LastUpdated = " + z.getLastUpdated();
      zoneXMLContent += '\n' + "   Name = " + z.getName();
      zoneXMLContent += '\n' + "   Version = " + z.getVersion();
      zoneXMLContent += '\n' + "   BaseObject.getBBTYPE = " +
          z.getBaseObject().getBBTYPE();
      zoneXMLContent += '\n' + "   BaseObject.getLocalFileName = " +
          z.getBaseObject().getLocalFileName();
      zoneXMLContent += '\n' + "   Geometry Position = " +
          z.getGeometry().getPosition().getX() + "," +
          z.getGeometry().getPosition().getY();
      zoneXMLContent += '\n' + "   NameCount = " + z.getNameCount();
      zoneXMLContent += '\n' + "________________________________________________";
      zoneXMLContent += '\n' + "EOZ" + '\n';
    }
    catch(Exception e)
    {
      myLog.addMessage(2, e.getMessage());
      return null;
    }

    return zoneXMLContent;
  }

  /**
   * Searches for Zone in repository by its ID, and returns the
   * XML ZoneType object
   * @param zoneID String
   * @return ZoneType
   */
  private static ZoneType searchZoneByID(String zoneID)
  {
    ZoneType z = null;
    TerraPeerType root = loadTerraPeerXML(repositoryFilename);
    try
    {
      //check if MyZone
      if(root.getZone().getID().getValue().equals(zoneID))
        z = root.getZone();
      //run through all zones in ZoneWorld, and check their ID
      for (int i = 0; i < root.getZoneWorld().getZoneCount(); i++)
        if (root.getZoneWorld().getZoneAt(i).getID().getValue().equals(zoneID))
          z = root.getZoneWorld().getZoneAt(i);
    }
    catch(Exception e)
    {
      myLog.addMessage(2, e.getMessage());
    }
    return z;
  }

  /**
   * Loads the TerraPeer XML repository file, and returns its root
   * @param fileName String
   * @return TerraPeerType
   */
  private static TerraPeerType loadTerraPeerXML(String fileName)
  {
    terrapeer_v1Doc doc = new terrapeer_v1Doc();
    TerraPeerType root = new TerraPeerType(doc.load(fileName));
    return root;
  }

  /**
   * Loads Model (Dialog) - calls loadModelFromFile()
   */
  public static void loadModelDialog()
  {
    myLog.addMessage(4, "ZoneRepository: Loading Model (Dialog)");

    selectModelDialog.show();
    if(selectModelDialog.getFile() == null) return;

    String fileName = new String(
     selectModelDialog.getDirectory() +
     selectModelDialog.getFile());

    loadModelFromFile(fileName);
  }

  /**
   * Loads a 3D model file and updates the frame
   * @param fileName
   * @return
   */
  public static javax.media.j3d.Node loadModelFromFile(String fileName)
  {
    myLog.addMessage(4, "ZoneRepository: Loading Model (File Name: "+fileName+")");
    javax.media.j3d.Node node = ModelLoader.loadModelFromName(fileName);
    if (node == null)
      return null;
    return node;
  }

  /**
   * Loads a 3D node object by its name
   * @param objectName String
   * @return Node
   */
  public static javax.media.j3d.Node loadObject(String objectName)
  {
    myLog.addMessage(4, "ZoneRepository: Loading Object (Object Name: "+objectName+")");
    return ModelLoader.loadModelFromName(Blocks.buildResourcePath(vars.VRML_REPOSITORY+objectName));
  }

}
