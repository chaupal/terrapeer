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

public class DialogPrefs extends JDialog
{
  int showPrefPage = 0;

  JLabel jLabel4 = new JLabel();
  JPanel jPanelNorth = new JPanel();
  JButton jButtonCancel = new JButton();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JTextArea jTextArea1 = new JTextArea();
  JButton jButtonOK = new JButton();
  JLabel jLabel2 = new JLabel();
  JPanel jPanelSouth = new JPanel();
  JPanel panelMain = new JPanel();
  JPanel jPanelCenter = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
  JPanel jPanelUI = new JPanel();
  JPanel jPanelNet = new JPanel();
  JPanel jPanelSpace = new JPanel();
  JCheckBox jCheckBox1 = new JCheckBox();
  JCheckBox jCheckBox2 = new JCheckBox();
  JCheckBox jCheckBox3 = new JCheckBox();
  JCheckBox jCheckBox4 = new JCheckBox();
  JCheckBox jCheckBox5 = new JCheckBox();
  JCheckBox jCheckBox6 = new JCheckBox();
  VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
  VerticalFlowLayout verticalFlowLayout3 = new VerticalFlowLayout();
  VerticalFlowLayout verticalFlowLayout4 = new VerticalFlowLayout();
  JLabel jLabel5 = new JLabel();
  JLabel jLabel6 = new JLabel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JPasswordField jPasswordField1 = new JPasswordField();
  JComboBox jComboBox1 = new JComboBox();
  JLabel jLabel7 = new JLabel();
  JTextField jTextField1 = new JTextField();
  JPanel jPanelNetUser = new JPanel();
  VerticalFlowLayout verticalFlowLayout5 = new VerticalFlowLayout();
  JLabel jLabel8 = new JLabel();
  JCheckBox jCheckBox7 = new JCheckBox();
  JPanel jPanelSec = new JPanel();

  public DialogPrefs(Frame frame, String title, boolean modal)
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

  public DialogPrefs(int showPrefPage)
  {
    this(null, "", false);
    this.showPrefPage = showPrefPage;

    switch (showPrefPage)
    {
      case 0: //default
        break;
      case 1: //network
        break;
      case 2: //security
        break;
    }
  }

  private void jbInit() throws Exception
  {
    jPanelNorth.setBorder(BorderFactory.createEtchedBorder());
    jPanelNorth.setBackground(new Color(220, 220, 200));
    jLabel4.setBackground(Color.lightGray);
    jLabel4.setOpaque(true);
    jLabel4.setText("Network");
    jButtonCancel.setText("Cancel");
    jButtonCancel.addActionListener(new DialogPrefs_jButtonCancel_actionAdapter(this));
    jLabel1.setFont(new java.awt.Font("Dialog", 1, 11));
    jLabel1.setForeground(new Color(0, 0, 84));
    jLabel1.setText("Preferences Dialog");
    jLabel3.setBackground(Color.lightGray);
    jLabel3.setOpaque(true);
    jLabel3.setText("Virtual Space");
    jTextArea1.setOpaque(false);
    jTextArea1.setText("Edit the TerraPeer Preferences:");
    jTextArea1.setLineWrap(true);
    jTextArea1.setWrapStyleWord(true);
    jButtonOK.setText("OK");
    jButtonOK.addActionListener(new DialogPrefs_jButtonOK_actionAdapter(this));
    jLabel2.setBackground(Color.lightGray);
    jLabel2.setOpaque(true);
    jLabel2.setText("User Interface");
    jPanelSouth.setBackground(new Color(220, 220, 200));
    jPanelSouth.setBorder(BorderFactory.createEtchedBorder());
    panelMain.setLayout(borderLayout1);
    jPanelCenter.setLayout(verticalFlowLayout1);
    panelMain.setPreferredSize(new Dimension(400, 500));
    jPanelCenter.setForeground(Color.black);
    jPanelUI.setBorder(BorderFactory.createEtchedBorder());
    jPanelUI.setLayout(verticalFlowLayout2);
    jPanelNet.setBorder(BorderFactory.createEtchedBorder());
    jPanelNet.setLayout(verticalFlowLayout3);
    jPanelSpace.setBorder(BorderFactory.createEtchedBorder());
    jPanelSpace.setLayout(verticalFlowLayout4);
    jCheckBox1.setSelected(true);
    jCheckBox1.setText("Show Toolbar Menu (top) at startup");
    jCheckBox2.setSelected(true);
    jCheckBox2.setText("Show Action Panel (left) at startup");
    jCheckBox3.setSelected(true);
    jCheckBox3.setText("Show Info Panel (right) at startup");
    jCheckBox4.setSelected(true);
    jCheckBox4.setText("Show South Panel at startup");
    jCheckBox5.setSelected(true);
    jCheckBox5.setText("Show Nav Panel (bottom) at startup");
    jCheckBox6.setSelected(true);
    jCheckBox6.setText("Auto-Start Cyberspace at startup");
    jLabel5.setText("Network");
    jLabel6.setText("Username");
    jPasswordField1.setText("");
    jLabel7.setText("Password");
    jTextField1.setText("");
    jPanelNetUser.setLayout(gridBagLayout1);
    jLabel8.setText("Security");
    jLabel8.setOpaque(true);
    jLabel8.setBackground(Color.lightGray);
    jCheckBox7.setText("Enable JXTA encryption");
    jCheckBox7.setSelected(true);
    jPanelSec.setLayout(verticalFlowLayout5);
    jPanelSec.setBorder(BorderFactory.createEtchedBorder());
    jPanelUI.add(jLabel2, null);
    jPanelUI.add(jCheckBox1, null);
    jPanelUI.add(jCheckBox2, null);
    jPanelUI.add(jCheckBox3, null);
    jPanelUI.add(jCheckBox4, null);
    jPanelUI.add(jCheckBox5, null);
    panelMain.add(jPanelNorth, BorderLayout.NORTH);
    jPanelNorth.add(jLabel1, null);
    panelMain.add(jPanelSouth, BorderLayout.SOUTH);
    jPanelSouth.add(jButtonCancel, null);
    jPanelSouth.add(jButtonOK, null);
    panelMain.add(jPanelCenter, BorderLayout.CENTER);
    jPanelSec.add(jLabel8, null);
    jPanelSec.add(jCheckBox7, null);
    this.getContentPane().add(panelMain,  BorderLayout.CENTER);
    jPanelNetUser.add(jLabel6,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5));
    jPanelNetUser.add(jTextField1,   new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanelNetUser.add(jLabel7,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5));
    jPanelNetUser.add(jPasswordField1,   new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanelNetUser.add(jLabel5,  new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5));
    jPanelNetUser.add(jComboBox1,   new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanelNet.add(jLabel4, null);
    jPanelNet.add(jPanelNetUser, null);
    jPanelSpace.add(jLabel3, null);
    jPanelSpace.add(jCheckBox6, null);

    jPanelCenter.add(jTextArea1, null);
    jPanelCenter.add(jPanelUI, null);
    jPanelCenter.add(jPanelSpace, null);
    jPanelCenter.add(jPanelNet, null);
    jPanelCenter.add(jPanelSec, null);
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

class DialogPrefs_jButtonOK_actionAdapter implements java.awt.event.ActionListener
{
  DialogPrefs adaptee;

  DialogPrefs_jButtonOK_actionAdapter(DialogPrefs adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonOK_actionPerformed(e);
  }
}

class DialogPrefs_jButtonCancel_actionAdapter implements java.awt.event.ActionListener
{
  DialogPrefs adaptee;

  DialogPrefs_jButtonCancel_actionAdapter(DialogPrefs adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonCancel_actionPerformed(e);
  }
}

