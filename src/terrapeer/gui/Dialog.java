package terrapeer.gui;

import java.awt.*;
import javax.swing.*;

/**
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P feedback system</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */

public class Dialog extends JDialog
{
  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanelNorth = new JPanel();
  JLabel jLabel1 = new JLabel();
  JPanel jPanelCenter = new JPanel();
  JPanel jPanelSouth = new JPanel();
  JButton jButton1 = new JButton();
  JButton jButton2 = new JButton();

  public Dialog(Frame frame, String title, boolean modal)
  {
    super(frame, title, modal);
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

  public Dialog()
  {
    this(null, "", false);
  }
  private void jbInit() throws Exception
  {
    panel1.setLayout(borderLayout1);
    jLabel1.setFont(new java.awt.Font("Dialog", 1, 11));
    jLabel1.setForeground(new Color(0, 0, 84));
    jLabel1.setText("Dialog");
    jPanelNorth.setBackground(new Color(220, 220, 200));
    jPanelNorth.setBorder(BorderFactory.createEtchedBorder());
    jPanelSouth.setBackground(new Color(220, 220, 200));
    jPanelSouth.setBorder(BorderFactory.createEtchedBorder());
    jButton1.setText("OK");
    jButton2.setText("Cancel");
    getContentPane().add(panel1);
    panel1.add(jPanelNorth, BorderLayout.NORTH);
    jPanelNorth.add(jLabel1, null);
    panel1.add(jPanelCenter,  BorderLayout.CENTER);
    panel1.add(jPanelSouth,  BorderLayout.SOUTH);
    jPanelSouth.add(jButton2, null);
    jPanelSouth.add(jButton1, null);
  }
}