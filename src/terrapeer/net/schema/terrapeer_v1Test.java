package terrapeer.net.schema;

import terrapeer.net.xml.types.*;

public class terrapeer_v1Test
{
  protected static void example() throws Exception
  {
    terrapeer_v1Doc doc = new terrapeer_v1Doc();
    TerraPeerType root = new TerraPeerType(doc.load("test.xml"));

    System.out.println("________________________________________________");
    System.out.println("TerraPeer XML File content overview");
    System.out.println("================================================");
    System.out.println("getZoneCount = " + root.getZoneCount());
    System.out.println("getZoneWorldCount = " + root.getZoneWorldCount());
    System.out.println("getZoneWorld().getZoneCount() = " + root.getZoneWorld().getZoneCount());
    System.out.println("________________________________________________");

    ZoneType z = root.getZone();
    System.out.println("getID = " + z.getID());
    System.out.println("getDescription = " + z.getDescription());
    System.out.println("getLastUpdated = " + z.getLastUpdated());
    System.out.println("getName = " + z.getName());
    System.out.println("getVersion = " + z.getVersion());
    System.out.println("getBaseObject.getBBTYPE = " +
                       z.getBaseObject().getBBTYPE());
    System.out.println("getBaseObject.getLocalFileName = " +
                       z.getBaseObject().getLocalFileName());
    System.out.println("getGeometry().getPosition().getX() = " +
                       z.getGeometry().getPosition().getX());
    System.out.println("getNameCount = " + z.getNameCount());

    System.out.println("________________________________________________");
    for (int i = 0; i < root.getZoneWorld().getZoneCount(); i++)
    {
      z = root.getZoneWorld().getZoneAt(i);
      System.out.println("getID = " + z.getID());
      System.out.println("getDescription = " + z.getDescription());
      System.out.println("getLastUpdated = " + z.getLastUpdated());
      System.out.println("getName = " + z.getName());
      System.out.println("getVersion = " + z.getVersion());
      System.out.println("getBaseObject.getBBTYPE = " +
                         z.getBaseObject().getBBTYPE());
      System.out.println("getBaseObject.getLocalFileName = " +
                         z.getBaseObject().getLocalFileName());
      System.out.println("getGeometry().getPosition().getX() = " +
                         z.getGeometry().getPosition().getX());
      System.out.println("getNameCount = " + z.getNameCount());
      System.out.println("___");
    }

  }

  public static void main(String args[])
  {
    try
    {
      System.out.println("terrapeer_v1 Test Application");
      example();
      System.out.println("OK");
      System.exit(0);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.exit(1);
    }
  }
}
