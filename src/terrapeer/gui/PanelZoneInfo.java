package terrapeer.gui;

import java.awt.*;
import javax.swing.*;

import terrapeer.vui.zone.*;

/**
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */

public class PanelZoneInfo extends JPanel
{
  private static ZoneRepository zoneRep = null;

  BorderLayout borderLayout1 = new BorderLayout();
  JLabel jLabel8 = new JLabel();
  JPanel jPanelZoneMyHome = new JPanel();
  JLabel jLabel12 = new JLabel();
  BorderLayout borderLayout15 = new BorderLayout();
  JSplitPane jSplitPaneZBSet = new JSplitPane();
  JLabel jLabel1 = new JLabel();
  BorderLayout borderLayout17 = new BorderLayout();
  JLabel jLabel13 = new JLabel();
  JTextField jTextFieldHome_ZoneName = new JTextField();
  JPanel jPanelZoneAttr = new JPanel();
  JTextField jTextFieldHome_ZoneKeyw = new JTextField();
  GridLayout gridLayout1 = new GridLayout();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel14 = new JLabel();
  JTextField jTextFieldHome_PeerID = new JTextField();
  JLabel jLabel9 = new JLabel();
  JTextField jTextFieldHome_PeerName = new JTextField();
  JLabel jLabel11 = new JLabel();
  JTextField jTextFieldHome_ZoneDescr = new JTextField();
  JLabel jLabel7 = new JLabel();
  //JPanel jPanelZoneBuildSet = new JPanel();
  JTextField jTextFieldHome_ZoneAccess = new JTextField();
  JLabel jLabel6 = new JLabel();
  JTextField jTextFieldHome_ZoneServices = new JTextField();
  JPanel jPanelZoneMyHome2 = new JPanel();
  //BorderLayout borderLayout6 = new BorderLayout();
  JTextField jTextFieldHome_ZoneVisibility = new JTextField();
  JScrollPane jScrollPane1 = new JScrollPane();
  JEditorPane jEditorPaneZoneObjects = new JEditorPane();
  JTextField jTextFieldHome_ZoneSpatial = new JTextField();
  JLabel jLabel3 = new JLabel();
  JTextField jTextFieldHome_ZonePos = new JTextField();
  JLabel jLabel4 = new JLabel();
  JTextField jTextFieldHome_ZoneObjects = new JTextField();
  JLabel jLabel5 = new JLabel();
  JTextField jTextFieldHome_ZoneID = new JTextField();
  JLabel jLabel10 = new JLabel();
  JTabbedPane jTabbedPane1 = new JTabbedPane();
  JScrollPane jScrollPane2 = new JScrollPane();
  JEditorPane jEditorPaneZoneServices = new JEditorPane();
  JScrollPane jScrollPane3 = new JScrollPane();
  JEditorPane jEditorPaneZoneXML = new JEditorPane();

  public PanelZoneInfo()
  {
    try
    {
      jbInit();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception
  {
    this.setLayout(borderLayout1);
    jPanelZoneMyHome.setLayout(borderLayout15);
    jPanelZoneMyHome.setPreferredSize(new Dimension(47, 60));
    jPanelZoneMyHome.setDebugGraphicsOptions(0);
    jPanelZoneMyHome.setBorder(BorderFactory.createLoweredBevelBorder());
    jLabel8.setText("Description:");
    jLabel8.setFont(new java.awt.Font("Tahoma", 0, 9));
    jLabel8.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel12.setFont(new java.awt.Font("Tahoma", 0, 9));
    jLabel12.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel12.setText("Visibility:");
    jSplitPaneZBSet.setLastDividerLocation(400);
    jSplitPaneZBSet.setResizeWeight(0.7);
    jLabel1.setVerifyInputWhenFocusTarget(true);
    jLabel1.setText("Zone Details");
    jLabel1.setVerticalAlignment(SwingConstants.CENTER);
    jLabel1.setVerticalTextPosition(SwingConstants.CENTER);
    jLabel13.setFont(new java.awt.Font("Tahoma", 0, 9));
    jLabel13.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel13.setText("Services:");
    jTextFieldHome_ZoneName.setFont(new java.awt.Font("Tahoma", 0, 9));
    jTextFieldHome_ZoneName.setPreferredSize(new Dimension(40, 19));
    jTextFieldHome_ZoneName.setText("");
    jPanelZoneAttr.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanelZoneAttr.setDebugGraphicsOptions(0);
    jPanelZoneAttr.setLayout(borderLayout17);
    jTextFieldHome_ZoneKeyw.setText("");
    jTextFieldHome_ZoneKeyw.setFont(new java.awt.Font("Tahoma", 0, 9));
    jTextFieldHome_ZoneKeyw.setPreferredSize(new Dimension(40, 19));
    gridLayout1.setColumns(2);
    gridLayout1.setRows(12);
    jLabel2.setFont(new java.awt.Font("Tahoma", 0, 9));
    jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel2.setText("Zone ID:");
    jLabel14.setFont(new java.awt.Font("Tahoma", 0, 9));
    jLabel14.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel14.setText("Access Level:");
    jTextFieldHome_PeerID.setFont(new java.awt.Font("Tahoma", 0, 9));
    jTextFieldHome_PeerID.setPreferredSize(new Dimension(40, 19));
    jTextFieldHome_PeerID.setText("");
    jLabel9.setFont(new java.awt.Font("Tahoma", 0, 9));
    jLabel9.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel9.setText("Peer ID:");
    jTextFieldHome_PeerName.setFont(new java.awt.Font("Tahoma", 0, 9));
    jTextFieldHome_PeerName.setPreferredSize(new Dimension(40, 19));
    jTextFieldHome_PeerName.setText("");
    jLabel11.setFont(new java.awt.Font("Tahoma", 0, 9));
    jLabel11.setRequestFocusEnabled(true);
    jLabel11.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel11.setText("Peer Name:");
    jTextFieldHome_ZoneDescr.setText("");
    jTextFieldHome_ZoneDescr.setFont(new java.awt.Font("Tahoma", 0, 9));
    jTextFieldHome_ZoneDescr.setPreferredSize(new Dimension(40, 19));
    jLabel7.setFont(new java.awt.Font("Tahoma", 0, 9));
    jLabel7.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel7.setText("Keywords:");
    jTextFieldHome_ZoneAccess.setFont(new java.awt.Font("Tahoma", 0, 9));
    jTextFieldHome_ZoneAccess.setPreferredSize(new Dimension(40, 19));
    jTextFieldHome_ZoneAccess.setText("");
    jLabel6.setText("Zone Attributes");
    jTextFieldHome_ZoneServices.setFont(new java.awt.Font("Tahoma", 0, 9));
    jTextFieldHome_ZoneServices.setPreferredSize(new Dimension(40, 19));
    jTextFieldHome_ZoneServices.setText("");
    jPanelZoneMyHome2.setLayout(gridLayout1);
    jTextFieldHome_ZoneVisibility.setFont(new java.awt.Font("Tahoma", 0, 9));
    jTextFieldHome_ZoneVisibility.setPreferredSize(new Dimension(40, 19));
    jTextFieldHome_ZoneVisibility.setText("");
    jSplitPaneZBSet.setDividerLocation(200);
    jEditorPaneZoneObjects.setText("");
    jTextFieldHome_ZoneSpatial.setText("");
    jTextFieldHome_ZoneSpatial.setFont(new java.awt.Font("Tahoma", 0, 9));
    jTextFieldHome_ZoneSpatial.setPreferredSize(new Dimension(40, 19));
    jLabel3.setText("Zone Name:");
    jLabel3.setFont(new java.awt.Font("Tahoma", 0, 9));
    jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
    jTextFieldHome_ZonePos.setText("");
    jTextFieldHome_ZonePos.setFont(new java.awt.Font("Tahoma", 0, 9));
    jTextFieldHome_ZonePos.setPreferredSize(new Dimension(40, 19));
    jLabel4.setText("Zone Position:");
    jLabel4.setFont(new java.awt.Font("Tahoma", 0, 9));
    jLabel4.setHorizontalAlignment(SwingConstants.RIGHT);
    jTextFieldHome_ZoneObjects.setText("");
    jTextFieldHome_ZoneObjects.setFont(new java.awt.Font("Tahoma", 0, 9));
    jTextFieldHome_ZoneObjects.setPreferredSize(new Dimension(40, 19));
    jLabel5.setText("Zone Spatial:");
    jLabel5.setFont(new java.awt.Font("Tahoma", 0, 9));
    jLabel5.setHorizontalAlignment(SwingConstants.RIGHT);
    jTextFieldHome_ZoneID.setText("");
    jTextFieldHome_ZoneID.setFont(new java.awt.Font("Tahoma", 0, 9));
    jTextFieldHome_ZoneID.setPreferredSize(new Dimension(40, 19));
    jLabel10.setText("Objects:");
    jLabel10.setFont(new java.awt.Font("Tahoma", 0, 9));
    jLabel10.setHorizontalAlignment(SwingConstants.RIGHT);
    jEditorPaneZoneServices.setText("Services");
    jEditorPaneZoneXML.setText("");
    jPanelZoneMyHome.add(jLabel6,  BorderLayout.NORTH);
    jPanelZoneMyHome.add(jPanelZoneMyHome2,  BorderLayout.CENTER);
    jSplitPaneZBSet.add(jPanelZoneAttr, JSplitPane.RIGHT);
    jSplitPaneZBSet.add(jPanelZoneMyHome, JSplitPane.LEFT);
    jPanelZoneAttr.add(jLabel1,  BorderLayout.NORTH);
    jPanelZoneMyHome2.add(jLabel2, null);
    jPanelZoneMyHome2.add(jTextFieldHome_ZoneID, null);
    jPanelZoneMyHome2.add(jLabel3, null);
    jPanelZoneMyHome2.add(jTextFieldHome_ZoneName, null);
    jPanelZoneMyHome2.add(jLabel8, null);
    jPanelZoneMyHome2.add(jTextFieldHome_ZoneDescr, null);
    jPanelZoneMyHome2.add(jLabel7, null);
    jPanelZoneMyHome2.add(jTextFieldHome_ZoneKeyw, null);
    jPanelZoneMyHome2.add(jLabel4, null);
    jPanelZoneMyHome2.add(jTextFieldHome_ZonePos, null);
    jPanelZoneMyHome2.add(jLabel5, null);
    jPanelZoneMyHome2.add(jTextFieldHome_ZoneSpatial, null);
    jPanelZoneMyHome2.add(jLabel10, null);
    jPanelZoneMyHome2.add(jTextFieldHome_ZoneObjects, null);
    jPanelZoneMyHome2.add(jLabel13, null);
    jPanelZoneMyHome2.add(jTextFieldHome_ZoneServices, null);
    jPanelZoneMyHome2.add(jLabel14, null);
    jPanelZoneMyHome2.add(jTextFieldHome_ZoneAccess, null);
    jPanelZoneMyHome2.add(jLabel12, null);
    jPanelZoneMyHome2.add(jTextFieldHome_ZoneVisibility, null);
    jPanelZoneMyHome2.add(jLabel9, null);
    jPanelZoneMyHome2.add(jTextFieldHome_PeerID, null);
    jPanelZoneMyHome2.add(jLabel11, null);
    jPanelZoneMyHome2.add(jTextFieldHome_PeerName, null);

    this.add(jSplitPaneZBSet, BorderLayout.CENTER);
    jPanelZoneAttr.add(jTabbedPane1,  BorderLayout.CENTER);
    jTabbedPane1.add(jScrollPane1,  "Objects");
    jScrollPane1.getViewport().add(jEditorPaneZoneObjects, null);
    jTabbedPane1.add(jScrollPane2,  "Services");
    jScrollPane2.getViewport().add(jEditorPaneZoneServices, null);
    jTabbedPane1.add(jScrollPane3,  "Zone XML");
    jScrollPane3.getViewport().add(jEditorPaneZoneXML, null);
  }

  public void setZoneRepository(ZoneRepository zoneRep)
  {
    this.zoneRep = zoneRep;
  }


  public void fillZoneInfo(Zone objZone)
  {
    updateFields(objZone.getZone_ID(), objZone.getZone_Name(), "N/A", "N/A",
                 objZone.myGeometry.getPosition().x+","+
                 objZone.myGeometry.getPosition().y+","+
                 objZone.myGeometry.getPosition().z,
                 objZone.myGeometry.getTwoPoint_H()+","+
                 objZone.myGeometry.getTwoPoint_W(),
                 objZone.getZone_Description(),
                 "#"+objZone.myObjects.getVObjectCount(),
                 "#"+objZone.myServices.getServiceCount(),
                 "N/A", "N/A", "N/A", "N/A", "N/A");

    String oStr = "";
    for(int o=0; o<objZone.myObjects.getVObjectCount(); o++)
    {
      oStr += "BASE OBJECT:\n";
      oStr += "  ID: "+objZone.myObjects.getVObject(o).getId()+"\n";
      oStr += "  Name: "+objZone.myObjects.getVObject(o).getName()+"\n";
      oStr += "  BBType: "+objZone.myObjects.getVObject(o).getBbType()+"\n";
      oStr += "  File: "+objZone.myObjects.getVObject(o).getFileName()+"\n";
      oStr += "  Position: "+objZone.myObjects.getVObject(o).getPosition().x+","+
          objZone.myObjects.getVObject(o).getPosition().y+","+
          objZone.myObjects.getVObject(o).getPosition().z+"\n";
    }
    jEditorPaneZoneObjects.setText(oStr);
    String sStr = "";
    for(int s=0; s<objZone.myServices.getServiceCount(); s++)
    {
      sStr += "SERVICE OBJECT:\n";
      sStr += "  ID: "+objZone.myServices.getURLService(s).getId()+"\n";
      sStr += "  Name: "+objZone.myServices.getURLService(s).getName()+"\n";
      sStr += "  BBType: "+objZone.myServices.getURLService(s).getBbType()+"\n";
      sStr += "  File: "+objZone.myServices.getURLService(s).getFileName()+"\n";
      sStr += "  URL: "+objZone.myServices.getURLService(s).getURL()+"\n";
      sStr += "  Position: "+objZone.myServices.getURLService(s).getPosition().x+","+
          objZone.myServices.getURLService(s).getPosition().y+","+
          objZone.myServices.getURLService(s).getPosition().z+"\n";
    }
    jEditorPaneZoneServices.setText(sStr);
  }

  public void fillZoneInfo(String xmlZone)
  {
    jEditorPaneZoneXML.setText(xmlZone);
  }

  private void updateFields(String zone_id, String zone_name, String last_updated,
                            String version, String geo_pos, String geo_spat,
                            String description, String objects, String services,
                            String visibility, String peer_name, String peer_id,
                            String access, String keywords)
  {
    jTextFieldHome_ZoneAccess.setText(access);
    jTextFieldHome_ZoneDescr.setText(description);
    jTextFieldHome_ZoneID.setText(zone_id);
    jTextFieldHome_ZoneName.setText(zone_name);
    jTextFieldHome_ZoneObjects.setText(objects);
    jTextFieldHome_ZoneServices.setText(services);
    jTextFieldHome_ZonePos.setText(geo_pos);
    jTextFieldHome_ZoneSpatial.setText(geo_spat);
    jTextFieldHome_ZoneVisibility.setText(visibility);
    jTextFieldHome_ZoneKeyw.setText(keywords);
    jTextFieldHome_PeerName.setText(peer_name);
    jTextFieldHome_PeerID.setText(peer_id);
  }

}
