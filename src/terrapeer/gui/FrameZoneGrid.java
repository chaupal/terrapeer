package terrapeer.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */

public class FrameZoneGrid extends JFrame
{
  BorderLayout borderLayout1 = new BorderLayout();
  JLabel jLabel1 = new JLabel();
  JButton jButton1 = new JButton();
  JPanel jPanelSouth = new JPanel();
  JPanel panel1 = new JPanel();
  JLabel jLabel2 = new JLabel();
  ZoneGrid jPanelCenter = new ZoneGrid(jLabel2);
  JPanel jPanelNorth = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();

  public FrameZoneGrid()
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
    this.setTitle("ZoneGrid");
    this.setSize(new Dimension(400, 400));
    this.getContentPane().setLayout(borderLayout1);
    jPanelSouth.setBackground(new Color(220, 220, 200));
    jPanelSouth.setBorder(BorderFactory.createEtchedBorder());
    jButton1.setText("Close");
    jButton1.addActionListener(new FrameZoneGrid_jButton1_actionAdapter(this));
    jLabel1.setFont(new java.awt.Font("Dialog", 1, 11));
    jLabel1.setForeground(new Color(0, 0, 84));
    jLabel1.setText("TerraPeer ZoneGrid");
    panel1.setLayout(borderLayout2);
    jPanelNorth.setBackground(new Color(220, 220, 200));
    jPanelNorth.setBorder(BorderFactory.createEtchedBorder());
    panel1.add(jPanelNorth, BorderLayout.NORTH);
    jPanelNorth.add(jLabel1, null);
    panel1.add(jPanelCenter, BorderLayout.CENTER);
    panel1.add(jPanelSouth, BorderLayout.SOUTH);
    jPanelSouth.add(jButton1, null);
    this.getContentPane().add(panel1,  BorderLayout.CENTER);
  }

  void jButton1_actionPerformed(ActionEvent e)
  {
    dispose();
  }
}

class FrameZoneGrid_jButton1_actionAdapter implements java.awt.event.ActionListener
{
  FrameZoneGrid adaptee;

  FrameZoneGrid_jButton1_actionAdapter(FrameZoneGrid adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButton1_actionPerformed(e);
  }
}
