package terrapeer.gui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import com.borland.jbcl.layout.*;
import javax.swing.border.*;
import javax.xml.parsers.*;

import terrapeer.*;
import terrapeer.vui.zone.*;

/**
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */

public class RepositoryFrame extends JFrame
{
  private static TerraPeerLog myLog = TerraPeerLog.getLogger();
  private static String repositoryFileName = vars.ZONE_REPOSITORY_FILE;
  private Frame frame = null;
  private static ZoneRepository zoneRep = null;

  // This is the XTree object which displays the XML in a JTree
  private XTree xTree;

  JButton jButtonCancel = new JButton();
  JButton jButtonOK = new JButton();
  JPanel jPanelSouth = new JPanel();
  JPanel jPanelNorth = new JPanel();
  JLabel jLabel1 = new JLabel();
  JPanel panel1 = new JPanel();
  JPanel jPanelCenter = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel jPanelTopMenu = new JPanel();
  JPanel jPanelWestMenu = new JPanel();
  JPanel jPanel3 = new JPanel();
  JSplitPane jSplitPane1 = new JSplitPane();
  BorderLayout borderLayout3 = new BorderLayout();
  JButton jButtonLoadDefault = new JButton();
  JPanel jPanel4 = new JPanel();
  JButton jButtonBrowse = new JButton();
  JTextField jTextFieldFileName = new JTextField();
  JButton jButtonLoad = new JButton();
  JLabel jLabel2 = new JLabel();
  JSplitPane jSplitPane2 = new JSplitPane();
  JLabel jLabel3 = new JLabel();
  JPanel jPanelPtree = new JPanel();
  JLabel jLabel4 = new JLabel();
  BorderLayout borderLayout4 = new BorderLayout();
  JPanel jPanelRfile = new JPanel();
  JLabel jLabel5 = new JLabel();
  BorderLayout borderLayout5 = new BorderLayout();
  JScrollPane jScrollPane1 = new JScrollPane();
  //JTree jTree1 = new JTree();
  JPanel jPanelPtext = new JPanel();
  BorderLayout borderLayout6 = new BorderLayout();
  JLabel jLabel6 = new JLabel();
  JScrollPane jScrollPane2 = new JScrollPane();
  JScrollPane jScrollPane3 = new JScrollPane();
  JEditorPane jEditorPaneRaw = new JEditorPane();
  JEditorPane jEditorPaneParse = new JEditorPane();
  JButton jButtonParse = new JButton();
  VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
  Border border1;
  JLabel jLabel7 = new JLabel();
  JButton jButtonMyZone = new JButton();
  JPanel jPanelSouthZone = new JPanel();
  BorderLayout borderLayout7 = new BorderLayout();
  PanelZoneInfo jPanelZoneInfo = new PanelZoneInfo();
  JPanel jPanelSouthWest = new JPanel();
  JToggleButton jToggleButton1 = new JToggleButton();
  JLabel jLabel8 = new JLabel();
  JTextField jTextFieldLookupZoneID = new JTextField();
  JLabel jLabel9 = new JLabel();
  VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
  JLabel jLabel10 = new JLabel();


  /**
   * Constructor
   * @param frame Frame
   * @param title String
   * @param modal boolean
   */
  public RepositoryFrame(Frame frame, String title, boolean modal)
  {
    //super(frame, title, modal);
    this.frame = frame;
    this.zoneRep = new ZoneRepository();

    try
    {
      jbInit();
      pack();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public RepositoryFrame()
  {
    this(null, "", false);
  }

  private void jbInit() throws Exception
  {
    this.setSize(new Dimension(900,600));
    this.setTitle("TerraPeer Repository");
    border1 = BorderFactory.createEmptyBorder(2,4,2,2);
    jPanelSouth.setBorder(BorderFactory.createEtchedBorder());
    jPanelSouth.setBackground(new Color(220, 220, 200));
    jButtonOK.setText("OK");
    jButtonOK.addActionListener(new RepositoryFrame_jButtonOK_actionAdapter(this));
    jButtonCancel.setText("Cancel");
    jButtonCancel.addActionListener(new RepositoryFrame_jButtonCancel_actionAdapter(this));
    jPanelNorth.setBackground(new Color(220, 220, 200));
    jPanelNorth.setBorder(BorderFactory.createEtchedBorder());
    jLabel1.setFont(new java.awt.Font("Dialog", 1, 11));
    jLabel1.setForeground(new Color(0, 0, 84));
    jLabel1.setText("Repository");
    panel1.setLayout(borderLayout1);
    jPanelCenter.setLayout(borderLayout2);
    jPanel3.setLayout(borderLayout3);
    jSplitPane1.setResizeWeight(0.5);
    jPanelWestMenu.setPreferredSize(new Dimension(100, 10));
    jPanelWestMenu.setLayout(verticalFlowLayout1);
    jButtonLoadDefault.setText("Load Default");
    jButtonLoadDefault.addActionListener(new RepositoryFrame_jButtonLoadDefault_actionAdapter(this));
    jButtonBrowse.setText("Browse...");
    jButtonBrowse.addActionListener(new RepositoryFrame_jButtonBrowse_actionAdapter(this));
    jTextFieldFileName.setPreferredSize(new Dimension(180, 20));
    jTextFieldFileName.setToolTipText("");
    jTextFieldFileName.setText("");
    jButtonLoad.setText("Load");
    jButtonLoad.addActionListener(new RepositoryFrame_jButtonLoad_actionAdapter(this));
    jPanel4.setBackground(Color.lightGray);
    jPanel4.setBorder(null);
    jPanel4.setOpaque(false);
    jLabel2.setText("Repository File:");
    jLabel3.setBackground(Color.orange);
    jLabel3.setOpaque(true);
    jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel3.setHorizontalTextPosition(SwingConstants.TRAILING);
    jLabel3.setText("Repository");
    jLabel4.setBackground(new Color(192, 192, 255));
    jLabel4.setFont(new java.awt.Font("Tahoma", 0, 9));
    jLabel4.setForeground(Color.darkGray);
    jLabel4.setBorder(border1);
    jLabel4.setOpaque(true);
    jLabel4.setText("Parsed Tree View");
    jPanelPtree.setLayout(borderLayout4);
    jLabel5.setText("Raw File View");
    jLabel5.setOpaque(true);
    jLabel5.setForeground(Color.darkGray);
    jLabel5.setBorder(border1);
    jLabel5.setBackground(new Color(192, 192, 255));
    jLabel5.setFont(new java.awt.Font("Tahoma", 0, 9));
    jPanelRfile.setLayout(borderLayout5);
    jPanelPtext.setLayout(borderLayout6);
    jLabel6.setText("Parsed Text View");
    jLabel6.setOpaque(true);
    jLabel6.setForeground(Color.darkGray);
    jLabel6.setBorder(border1);
    jLabel6.setBackground(new Color(192, 192, 255));
    jLabel6.setFont(new java.awt.Font("Tahoma", 0, 9));
    jEditorPaneParse.setText("");
    jEditorPaneRaw.setText("");
    jPanelRfile.setPreferredSize(new Dimension(300, 39));
    jPanelPtext.setPreferredSize(new Dimension(200, 39));
    jPanelPtree.setPreferredSize(new Dimension(100, 377));
    jButtonParse.setText("Parse");
    jButtonParse.addActionListener(new RepositoryFrame_jButtonParse_actionAdapter(this));
    jLabel7.setFont(new java.awt.Font("Tahoma", 0, 20));
    jLabel7.setForeground(Color.lightGray);
    jLabel7.setText(" | ");
    jButtonMyZone.setText("Show Zone");
    jButtonMyZone.addActionListener(new RepositoryFrame_jButtonMyZone_actionAdapter(this));
    jPanelSouthZone.setLayout(borderLayout7);
    jPanelSouthWest.setBackground(Color.lightGray);
    jPanelSouthWest.setPreferredSize(new Dimension(100, 10));
    jPanelSouthWest.setLayout(verticalFlowLayout2);
    jPanelZoneInfo.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanelSouthZone.setPreferredSize(new Dimension(236, 200));
    jToggleButton1.setSelected(true);
    jToggleButton1.setText("Zone Info");
    jToggleButton1.addActionListener(new RepositoryFrame_jToggleButton1_actionAdapter(this));
    jLabel8.setFont(new java.awt.Font("Tahoma", 0, 20));
    jLabel8.setForeground(Color.lightGray);
    jLabel8.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel8.setText("___");
    jTextFieldLookupZoneID.setPreferredSize(new Dimension(80, 20));
    jTextFieldLookupZoneID.setText("MZ");
    jLabel9.setFont(new java.awt.Font("Tahoma", 0, 9));
    jLabel9.setText("Lookup ID");
    jLabel10.setBackground(new Color(212, 255, 212));
    jLabel10.setOpaque(true);
    jLabel10.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel10.setText("Zone");
    jPanelSouthWest.add(jLabel10, null);
    panel1.add(jPanelNorth, BorderLayout.NORTH);
    jPanelNorth.add(jLabel1, null);
    panel1.add(jPanelCenter, BorderLayout.CENTER);
    jPanelCenter.add(jPanelTopMenu, BorderLayout.NORTH);
    jPanelTopMenu.add(jLabel2, null);
    jPanelTopMenu.add(jButtonLoadDefault, null);
    jPanelTopMenu.add(jLabel7, null);
    jPanelTopMenu.add(jPanel4, null);
    jPanel4.add(jButtonBrowse, null);
    jPanel4.add(jTextFieldFileName, null);
    jPanel4.add(jButtonLoad, null);
    jPanelCenter.add(jPanelWestMenu,  BorderLayout.WEST);
    jPanelWestMenu.add(jLabel3, null);
    jPanelWestMenu.add(jButtonParse, null);
    jPanelWestMenu.add(jLabel8, null);
    jPanelCenter.add(jPanel3, BorderLayout.CENTER);
    jPanelPtree.add(jLabel4, BorderLayout.NORTH);
    jPanelPtree.add(jScrollPane1, BorderLayout.CENTER);
    jPanelRfile.add(jLabel5, BorderLayout.NORTH);
    jPanelRfile.add(jScrollPane2,  BorderLayout.CENTER);
    jScrollPane2.getViewport().add(jEditorPaneRaw, null);
    jPanelPtext.add(jLabel6, BorderLayout.NORTH);
    jPanelPtext.add(jScrollPane3,  BorderLayout.CENTER);
    jPanelCenter.add(jPanelSouthZone,  BorderLayout.SOUTH);
    jPanelSouthZone.add(jPanelSouthWest, BorderLayout.WEST);
    jPanelSouthZone.add(jPanelZoneInfo,  BorderLayout.CENTER);
    jScrollPane3.getViewport().add(jEditorPaneParse, null);
    //jScrollPane1.getViewport().add(jTree1, null);
    jPanel3.add(jSplitPane2,  BorderLayout.CENTER);
    panel1.add(jPanelSouth, BorderLayout.SOUTH);
    jPanelSouth.add(jButtonCancel, null);
    jPanelSouth.add(jButtonOK, null);
    this.getContentPane().add(panel1,  BorderLayout.CENTER);
    jSplitPane1.add(jPanelPtree, JSplitPane.LEFT);
    jSplitPane1.add(jPanelPtext, JSplitPane.RIGHT);
    jSplitPane2.add(jPanelRfile, JSplitPane.LEFT);
    jSplitPane2.add(jSplitPane1, JSplitPane.RIGHT);
    jPanelWestMenu.add(jToggleButton1, null);
    jPanelSouthWest.add(jLabel9, null);
    jPanelSouthWest.add(jTextFieldLookupZoneID, null);
    jPanelSouthWest.add(jButtonMyZone, null);
    jSplitPane1.setDividerLocation(100);
    jSplitPane2.setDividerLocation(300);
  }

  /**
   * Loads a local text File (current repositoryFileName)
   * @return String Content
   */
  private String loadFile()
  {
    myLog.addMessage(4, "Repository Dialog: Loading File");
    File f = new File(this.repositoryFileName);
    String fStr = "";
    try
    {
      FileReader fr = new FileReader(f);
      int c = 0;
      while (c != -1)
      {
        c = fr.read();
        fStr += (char)c;
      }
    }
    catch(FileNotFoundException fnfe)
    {
    }
    catch(IOException ioe)
    {
    }
    return fStr;
  }

  private void showZoneInfo(String zoneID)
  {
    jPanelZoneInfo.fillZoneInfo(zoneRep.printZoneXMLContent(zoneID));
    jPanelZoneInfo.fillZoneInfo(zoneRep.loadMyZoneFromFile());
  }

  /**
   * Construct a Tree (xTree) based on XML File (current repositoryFileName)
   */
  private void parseXMLtoTree()
  {
    myLog.addMessage(4, "   Constructing tree...");
    BufferedReader reader;
    String line;
    StringBuffer xmlText;

    // Build a Document object based on the specified XML file
    try
    {
      if (this.repositoryFileName.substring(this.repositoryFileName.indexOf('.')).equals(".xml"))
      {
        reader = new BufferedReader(new FileReader(this.repositoryFileName));
        xmlText = new StringBuffer();

        while ((line = reader.readLine()) != null)
        {
          xmlText.append(line);
        }
        reader.close();

        //xTreeTester = new XTreeTester("XTree Tester", xmlText.toString());
        xTree = new XTree(xmlText.toString());
        jScrollPane1.getViewport().add(xTree, null);
      }
      else
      {
        myLog.addMessage(3, "   Error - the repository is not a XML file!");
      }
    }
    catch (FileNotFoundException fnfEx)
    {
      myLog.addMessage(3, "   Error - " + this.repositoryFileName + " was not found.");
    }
    catch (Exception ex)
    {
      myLog.addMessage(3, "   Error - exception while parsing.");
      //ex.printStackTrace();
    }
  }

  void jButtonOK_actionPerformed(ActionEvent e)
  {
    hide();
  }

  void jButtonCancel_actionPerformed(ActionEvent e)
  {
    hide();
  }

  void jButtonBrowse_actionPerformed(ActionEvent e)
  {
    FileDialog fd = new FileDialog(null, "TerraPeer Repository", FileDialog.LOAD);
    fd.show();
    jTextFieldFileName.setText(fd.getFile());
  }

  void jButtonLoadDefault_actionPerformed(ActionEvent e)
  {
    jEditorPaneRaw.setText(loadFile());
  }

  void jButtonParse_actionPerformed(ActionEvent e)
  {
    myLog.addMessage(4, "Repository Dialog: Parsing XML Content");
    jEditorPaneParse.setText(zoneRep.printTerraPeerXMLContent());
    parseXMLtoTree();
  }

  void jButtonMyZone_actionPerformed(ActionEvent e)
  {
    showZoneInfo(jTextFieldLookupZoneID.getText());
  }

  void jToggleButton1_actionPerformed(ActionEvent e)
  {
    if(jPanelSouthZone.isVisible())
      jPanelSouthZone.setVisible(false);
    else
      jPanelSouthZone.setVisible(true);
  }

  void jButtonLoad_actionPerformed(ActionEvent e)
  {
    String fn = jTextFieldFileName.getText();
    if(!fn.equals(""))
    {
      this.repositoryFileName = fn;
      loadFile();
    }
  }


}


class RepositoryFrame_jButtonOK_actionAdapter implements java.awt.event.ActionListener
{
  RepositoryFrame adaptee;

  RepositoryFrame_jButtonOK_actionAdapter(RepositoryFrame adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonOK_actionPerformed(e);
  }
}

class RepositoryFrame_jButtonCancel_actionAdapter implements java.awt.event.ActionListener
{
  RepositoryFrame adaptee;

  RepositoryFrame_jButtonCancel_actionAdapter(RepositoryFrame adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonCancel_actionPerformed(e);
  }
}

class RepositoryFrame_jButtonBrowse_actionAdapter implements java.awt.event.ActionListener
{
  RepositoryFrame adaptee;

  RepositoryFrame_jButtonBrowse_actionAdapter(RepositoryFrame adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonBrowse_actionPerformed(e);
  }
}

class RepositoryFrame_jButtonLoadDefault_actionAdapter implements java.awt.event.ActionListener
{
  RepositoryFrame adaptee;

  RepositoryFrame_jButtonLoadDefault_actionAdapter(RepositoryFrame adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonLoadDefault_actionPerformed(e);
  }
}

class RepositoryFrame_jButtonParse_actionAdapter implements java.awt.event.ActionListener
{
  RepositoryFrame adaptee;

  RepositoryFrame_jButtonParse_actionAdapter(RepositoryFrame adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonParse_actionPerformed(e);
  }
}

class RepositoryFrame_jButtonMyZone_actionAdapter implements java.awt.event.ActionListener
{
  RepositoryFrame adaptee;

  RepositoryFrame_jButtonMyZone_actionAdapter(RepositoryFrame adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonMyZone_actionPerformed(e);
  }
}

class RepositoryFrame_jToggleButton1_actionAdapter implements java.awt.event.ActionListener
{
  RepositoryFrame adaptee;

  RepositoryFrame_jToggleButton1_actionAdapter(RepositoryFrame adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jToggleButton1_actionPerformed(e);
  }
}

class RepositoryFrame_jButtonLoad_actionAdapter implements java.awt.event.ActionListener
{
  RepositoryFrame adaptee;

  RepositoryFrame_jButtonLoad_actionAdapter(RepositoryFrame adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonLoad_actionPerformed(e);
  }
}
