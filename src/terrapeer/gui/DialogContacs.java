package terrapeer.gui;

import java.awt.*;
import javax.swing.*;
import com.borland.jbcl.layout.*;
import java.awt.event.*;

/**
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */

public class DialogContacs extends JDialog
{
  VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
  JButton jButtonCancel = new JButton();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JPanel jPanelSouth = new JPanel();
  VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
  JPanel jPanelUI = new JPanel();
  JPanel panel1 = new JPanel();
  JPanel jPanelCenter = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanelNorth = new JPanel();
  JButton jButtonOK = new JButton();
  JScrollPane jScrollPane1 = new JScrollPane();
  JTable jTable1 = new JTable();

  public DialogContacs(Frame frame, String title, boolean modal)
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

  public DialogContacs()
  {
    this(null, "", false);
  }

  private void jbInit() throws Exception
  {
    jPanelSouth.setBorder(BorderFactory.createEtchedBorder());
    jPanelSouth.setBackground(new Color(220, 220, 200));
    jLabel2.setText("Contact Listing");
    jLabel2.setOpaque(true);
    jLabel2.setBackground(Color.lightGray);
    jLabel1.setText("Contacts Dialog");
    jLabel1.setForeground(new Color(0, 0, 84));
    jLabel1.setFont(new java.awt.Font("Dialog", 1, 11));
    jButtonCancel.addActionListener(new DialogContacs_jButtonCancel_actionAdapter(this));
    jButtonCancel.setText("Cancel");
    jButtonCancel.addActionListener(new DialogContacs_jButtonCancel_actionAdapter(this));
    jPanelUI.setBorder(BorderFactory.createEtchedBorder());
    jPanelUI.setLayout(verticalFlowLayout2);
    panel1.setLayout(borderLayout1);
    panel1.setPreferredSize(new Dimension(400, 500));
    jPanelCenter.setLayout(verticalFlowLayout1);
    jPanelCenter.setForeground(Color.black);
    jPanelNorth.setBorder(BorderFactory.createEtchedBorder());
    jPanelNorth.setBackground(new Color(220, 220, 200));
    jButtonOK.setText("OK");
    jButtonOK.addActionListener(new DialogContacs_jButtonOK_actionAdapter(this));
    jButtonOK.addActionListener(new DialogContacs_jButtonOK_actionAdapter(this));
    panel1.add(jPanelNorth, BorderLayout.NORTH);
    jPanelNorth.add(jLabel1, null);
    panel1.add(jPanelSouth, BorderLayout.SOUTH);
    jPanelSouth.add(jButtonCancel, null);
    jPanelSouth.add(jButtonOK, null);
    panel1.add(jPanelCenter, BorderLayout.CENTER);
    jPanelCenter.add(jPanelUI, null);
    jPanelUI.add(jLabel2, null);
    jPanelUI.add(jScrollPane1, null);
    jScrollPane1.getViewport().add(jTable1, null);
    this.getContentPane().add(panel1,  BorderLayout.CENTER);
  }

  void jButtonOK_actionPerformed(ActionEvent e)
  {
    this.hide();
  }

  void jButtonCancel_actionPerformed(ActionEvent e)
  {
    this.hide();
  }
}

class DialogContacs_jButtonOK_actionAdapter implements java.awt.event.ActionListener
{
  DialogContacs adaptee;

  DialogContacs_jButtonOK_actionAdapter(DialogContacs adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonOK_actionPerformed(e);
  }
}

class DialogContacs_jButtonCancel_actionAdapter implements java.awt.event.ActionListener
{
  DialogContacs adaptee;

  DialogContacs_jButtonCancel_actionAdapter(DialogContacs adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonCancel_actionPerformed(e);
  }
}
