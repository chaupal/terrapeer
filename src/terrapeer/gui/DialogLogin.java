package terrapeer.gui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

/**
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P feedback system</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */

public class DialogLogin extends JDialog
{
  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanelNorth = new JPanel();
  JLabel jLabel1 = new JLabel();
  JPanel jPanelCenter = new JPanel();
  JPanel jPanelSouth = new JPanel();
  JButton jButtonOK = new JButton();
  JButton jButtonCancel = new JButton();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel jLabel2 = new JLabel();
  JTextField jTextField1 = new JTextField();
  JLabel jLabel3 = new JLabel();
  JPasswordField jPasswordField1 = new JPasswordField();
  JLabel jLabel4 = new JLabel();
  JComboBox jComboBox1 = new JComboBox();
  Border border1;
  JTextArea jTextArea1 = new JTextArea();
  Border border2;

  public DialogLogin(Frame frame, String title, boolean modal)
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

  public DialogLogin()
  {
    this(null, "", false);
  }

  private void jbInit() throws Exception
  {
    border1 = BorderFactory.createEmptyBorder(20,20,20,20);
    border2 = BorderFactory.createEmptyBorder(20,20,20,20);
    panel1.setLayout(borderLayout1);
    jLabel1.setFont(new java.awt.Font("Dialog", 1, 11));
    jLabel1.setForeground(new Color(0, 0, 84));
    jLabel1.setText("Login & Connection Dialog");
    jPanelNorth.setBackground(new Color(220, 220, 200));
    jPanelNorth.setBorder(BorderFactory.createEtchedBorder());
    jPanelSouth.setBackground(new Color(220, 220, 200));
    jPanelSouth.setBorder(BorderFactory.createEtchedBorder());
    jButtonOK.setText("Connect");
    jButtonCancel.setText("Cancel");
    jButtonCancel.addActionListener(new DialogLogin_jButtonCancel_actionAdapter(this));
    jPanelCenter.setLayout(gridBagLayout1);
    jLabel2.setText("Username");
    jLabel3.setText("Password");
    jLabel4.setText("Network");
    jPasswordField1.setText("");
    jTextField1.setText("");
    jTextField1.addActionListener(new DialogLogin_jTextField1_actionAdapter(this));
    jPanelCenter.setBorder(border2);
    jTextArea1.setOpaque(false);
    jTextArea1.setText("Please enter your Username and Password to login and connect to the " +
    "peer network.");
    jTextArea1.setLineWrap(true);
    jTextArea1.setWrapStyleWord(true);
    getContentPane().add(panel1);
    panel1.add(jPanelNorth, BorderLayout.NORTH);
    jPanelNorth.add(jLabel1, null);
    jPanelCenter.add(jLabel2,    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5));
    jPanelCenter.add(jTextField1,      new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanelCenter.add(jLabel3,    new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5));
    jPanelCenter.add(jPasswordField1,       new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanelCenter.add(jLabel4,    new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5));
    jPanelCenter.add(jComboBox1,      new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanelCenter.add(jTextArea1,    new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 20, 0), 0, 0));
    panel1.add(jPanelSouth,  BorderLayout.SOUTH);
    jPanelSouth.add(jButtonCancel, null);
    jPanelSouth.add(jButtonOK, null);
    panel1.add(jPanelCenter, BorderLayout.CENTER);
  }

  void jTextField1_actionPerformed(ActionEvent e)
  {

  }

  void jButtonCancel_actionPerformed(ActionEvent e)
  {
    this.hide();
  }
}

class DialogLogin_jTextField1_actionAdapter implements java.awt.event.ActionListener
{
  DialogLogin adaptee;

  DialogLogin_jTextField1_actionAdapter(DialogLogin adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jTextField1_actionPerformed(e);
  }
}

class DialogLogin_jButtonCancel_actionAdapter implements java.awt.event.ActionListener
{
  DialogLogin adaptee;

  DialogLogin_jButtonCancel_actionAdapter(DialogLogin adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonCancel_actionPerformed(e);
  }
}