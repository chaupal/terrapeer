package terrapeer.vui.zone;

import terrapeer.*;
import terrapeer.vui.service.*;

import java.net.URL;
import java.util.Vector;
import java.io.Serializable;
import javax.vecmath.*;
import java.net.*;

/**
 * Responsible for all Zone services
 *
 * Services available:
 *  - URL (links only)
 *  - HTTP (host a website)
 *  - FTP (host a file server)
 *  - Biz (tba)
 *
 * Transformations to/from XML
 *
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */
public class ZoneServices implements Serializable
{
  private static TerraPeerLog myLog = TerraPeerLog.getLogger();

  private String zoneID = null;
  private int serviceCount;
  private java.util.Vector myHTTPServices;
  private java.util.Vector myFTPServices;
  private java.util.Vector myBizServices;
  private java.util.Vector myURLServices;

  public ZoneServices(String zoneID)
  {
    this.zoneID = zoneID;
    this.serviceCount = 0;
    myHTTPServices = null;
    myFTPServices = null;
    myBizServices = null;
    myURLServices = new Vector(5);
  }

  public String getServiceZoneID()
  {
    return this.zoneID;
  }

  public String getServicesAsXML()
  {
    //todo

    return null;
  }

  private String generateServiceID(int type, int count)
  {
    String typeStr = "X";
    switch(type)
    {
      case vars.SERVICE_URL:
        typeStr = "URL";
        break;
      case vars.SERVICE_HTTP:
        typeStr = "HTT";
        break;
      case vars.SERVICE_FTP:
        typeStr = "FTP";
        break;
      case vars.SERVICE_WEBSERVICES:
        typeStr = "WSV";
        break;
    }

    String id = "TERRA-Z000"+zoneID+"-S-"+typeStr+"-"+count;
    return id;
  }

  public int getServiceCount()
  {
    return serviceCount;
  }

  private void incrServiceCount()
  {
    this.serviceCount++;
  }

  private void decrServiceCount()
  {
    this.serviceCount--;
  }

  public String addURLService(String name, Vector3d servicePos, Vector3d zonePos, String urlStr)
  {
    String id = generateServiceID(vars.SERVICE_URL, myURLServices.size()+1);
    URLService s = new URLService(id, zonePos);
    s.setName(name);
    s.setPosition(servicePos);
    URL url = null;
    try
    {
      url = new URL(urlStr);
      s.setURL(url);
    }
    catch (MalformedURLException ex)
    {
    }
    s.setBbType(vars.SERVICE_URL);
    myURLServices.add(s);

    incrServiceCount();
    return id;
  }

  public String addHTTPService(Vector3d servicePos, Vector3d zonePos, String urlStr)
  {
    String id = generateServiceID(vars.SERVICE_HTTP, myURLServices.size()+1);
    URLService s = new URLService(id, zonePos);
    s.setPosition(servicePos);
    URL url = null;
    try
    {
      url = new URL(urlStr);
      s.setURL(url);
    }
    catch (MalformedURLException ex)
    {
    }
    s.setBbType(vars.SERVICE_URL);
    myURLServices.add(s);

    incrServiceCount();
    return id;
  }

  public URLService getURLService(int i)
  {
    return (URLService)myURLServices.get(i);
  }

  public URL getService_URL(String service_id)
  {
    URL u = null;
    URLService s = null;
    while(myURLServices.elements().hasMoreElements())
    {
      try
      {
        s = (URLService)myURLServices.elements().nextElement();
        if(service_id.equals(s.getId()))
           u = s.getURL();
      }
      catch (Exception ex)
      {
      }
    }
    return u;
  }

}
