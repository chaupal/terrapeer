package terrapeer.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P feedback system</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */

public class PanelTemplate extends JPanel
{
  public JPanel jPanelCenter = new JPanel();
  public JPanel jPanelSouth = new JPanel();

  JLabel jLabelTitle = new JLabel();
  JPanel jPanelNorth = new JPanel();
  JPanel jPanelWest = new JPanel();
  JScrollPane jScrollPaneWest = new JScrollPane();
  JTextArea jTextAreaHelp = new JTextArea();
  TitledBorder titledBorder1;
  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  Border borderEtch;
  Border borderBackTitle;
  Border borderLine;
  Border border8space;

  public PanelTemplate()
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
    //this.setPreferredSize(new Dimension(500, 500));
    this.setBackground(new Color(216, 216, 255));
    this.setFont(new java.awt.Font("SansSerif", 0, 10));
    borderEtch = BorderFactory.createEtchedBorder(new Color(235, 235, 255),new Color(105, 105, 255));
    titledBorder1 = new TitledBorder(borderEtch,"etchBorder1");
    borderBackTitle = new TitledBorder(BorderFactory.createEtchedBorder(new Color(235, 235, 255),new Color(135, 135, 255)),"TerraPeer");
    borderLine = BorderFactory.createLineBorder(new Color(200, 200, 255),2);
    border8space = BorderFactory.createEmptyBorder(8,8,8,8);
    this.setLayout(borderLayout1);
    this.setBorder(borderBackTitle);
    jPanelWest.setBackground(new Color(225, 225, 255));
    jPanelWest.setBorder(borderLine);
    jPanelWest.setPreferredSize(new Dimension(150, 10));
    jPanelWest.setLayout(borderLayout2);
    jPanelNorth.setBackground(new Color(216, 216, 255));
    jPanelNorth.setBorder(null);
    jPanelNorth.setPreferredSize(new Dimension(10, 30));
    jLabelTitle.setFont(new java.awt.Font("SansSerif", 0, 12));
    jLabelTitle.setForeground(new Color(40, 40, 84));
    jLabelTitle.setText("Title");
    jPanelSouth.setBackground(new Color(216, 216, 255));
    jPanelSouth.setPreferredSize(new Dimension(10, 30));
    jPanelCenter.setBackground(new Color(225, 225, 255));
    jPanelCenter.setBorder(borderLine);
    jTextAreaHelp.setBackground(new Color(225, 225, 255));
    jTextAreaHelp.setFont(new java.awt.Font("SansSerif", 0, 11));
    jTextAreaHelp.setForeground(new Color(0, 0, 64));
    jTextAreaHelp.setBorder(border8space);
    jTextAreaHelp.setPreferredSize(new Dimension(100, 200));
    jTextAreaHelp.setEditable(false);
    jTextAreaHelp.setText("This is a description");
    jTextAreaHelp.setLineWrap(true);
    jTextAreaHelp.setWrapStyleWord(true);
    jScrollPaneWest.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneWest.setBorder(null);
    this.add(jPanelNorth,  BorderLayout.NORTH);
    this.add(jPanelWest,  BorderLayout.WEST);
    jPanelWest.add(jScrollPaneWest, BorderLayout.CENTER);
    jScrollPaneWest.getViewport().add(jTextAreaHelp, null);
    this.add(jPanelCenter, BorderLayout.CENTER);
    this.add(jPanelSouth,  BorderLayout.SOUTH);
    jPanelNorth.add(jLabelTitle, null);
  }

  public void setTitle(String title)
  {
    jLabelTitle.setText(title);
  }

  public void setDescription(String descr)
  {
    jTextAreaHelp.setText(descr);
  }

}
