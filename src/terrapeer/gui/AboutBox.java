package terrapeer.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import com.borland.jbcl.layout.*;

import terrapeer.*;

/**
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P feedback system</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */

public class AboutBox extends JDialog implements ActionListener
{
  JButton button1 = new JButton();
  ImageIcon image1 = new ImageIcon();
  ImageIcon image2 = new ImageIcon();
  String product = "TerraPeer";
  String version = "Version 1.0";
  String copyright = "Copyright (c) 2003";
  String comments = "P2P VR System";
  JLabel jLabel1 = new JLabel();
  JPanel jPanelSouth = new JPanel();
  JPanel panel3 = new JPanel();
  JPanel jPanelCenter = new JPanel();
  JPanel jPanelNorth = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  JPanel insetsPanel3 = new JPanel();
  JLabel label3 = new JLabel();
  JLabel imageLabel = new JLabel();
  GridLayout gridLayout1 = new GridLayout();
  JPanel panel2 = new JPanel();
  JLabel label4 = new JLabel();
  FlowLayout flowLayout1 = new FlowLayout();
  JLabel label2 = new JLabel();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel insetsPanel2 = new JPanel();
  JLabel label1 = new JLabel();
  JPanel jPanel1 = new JPanel();
  JLabel jLabelLogo = new JLabel();
  VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
  JLabel label5 = new JLabel();
  JPanel jPanel2 = new JPanel();
  JScrollPane jScrollPane1 = new JScrollPane();
  JEditorPane jEditorPaneAbout = new JEditorPane();
  BorderLayout borderLayout1 = new BorderLayout();

  public AboutBox(Frame parent)
  {
    super(parent);
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try
    {
      jbInit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

//Component initialization
  private void jbInit() throws Exception
  {
    this.setTitle("About");

    image1 = new ImageIcon(terrapeer.gui.TerraPeerGUI.class.getResource("../objects/images/about.png"));
    imageLabel.setIcon(image1);
    image2 = new ImageIcon(terrapeer.gui.TerraPeerGUI.class.getResource("../objects/images/tp_6.gif"));
    jLabelLogo.setIcon(image2);

    button1.setText("Ok");
    button1.addActionListener(new AboutBox_button1_actionAdapter(this));
    button1.addActionListener(this);
    jLabel1.setText("Dialog");
    jLabel1.setForeground(new Color(0, 0, 84));
    jLabel1.setFont(new java.awt.Font("Dialog", 1, 11));
    jPanelSouth.setBorder(BorderFactory.createEtchedBorder());
    jPanelSouth.setBackground(new Color(220, 220, 200));
    panel3.setLayout(borderLayout3);
    jPanelNorth.setBackground(new Color(220, 220, 200));
    jPanelNorth.setBorder(BorderFactory.createEtchedBorder());
    insetsPanel3.setLayout(gridLayout1);
    insetsPanel3.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 10));
    label3.setText(copyright);
    gridLayout1.setRows(4);
    gridLayout1.setColumns(1);
    panel2.setLayout(borderLayout2);
    label4.setText(comments);
    label2.setText(version);
    insetsPanel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    label1.setText(product);
    jPanelCenter.setLayout(verticalFlowLayout1);
    label5.setText("P2P VR System");
    jEditorPaneAbout.setBackground(Color.lightGray);
    jEditorPaneAbout.setFont(new java.awt.Font("Arial Narrow", 0, 14));
    jEditorPaneAbout.setEditable(false);
    jEditorPaneAbout.setPage(vars.HELP_PAGES[vars.HELP_ABOUT]);
    jEditorPaneAbout.setContentType("text/html");
    jPanel2.setLayout(borderLayout1);
    this.getContentPane().add(panel3, BorderLayout.NORTH);
    panel3.add(jPanelNorth, BorderLayout.NORTH);
    jPanelNorth.add(jLabel1, null);
    panel3.add(jPanelCenter, BorderLayout.CENTER);
    panel2.add(insetsPanel2, BorderLayout.WEST);
    insetsPanel2.add(imageLabel, null);
    panel2.add(insetsPanel3, BorderLayout.CENTER);
    insetsPanel3.add(label1, null);
    insetsPanel3.add(label2, null);
    insetsPanel3.add(label3, null);
    insetsPanel3.add(label4, null);
    panel2.add(jPanel2,  BorderLayout.SOUTH);
    jPanel2.add(jScrollPane1, BorderLayout.CENTER);
    jScrollPane1.getViewport().add(jEditorPaneAbout, null);
    jPanel1.add(jLabelLogo, null);
    panel3.add(jPanelSouth, BorderLayout.SOUTH);
    jPanelSouth.add(button1, null);

    jPanelCenter.add(jPanel1, null);
    jPanelCenter.add(panel2, null);

    setResizable(true);
  }

  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e)
  {
    if (e.getID() == WindowEvent.WINDOW_CLOSING)
    {
      cancel();
    }
    super.processWindowEvent(e);
  }

  //Close the dialog
  void cancel()
  {
    dispose();
  }

  //Close the dialog on a button event
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == button1)
    {
      cancel();
    }
  }

  void button1_actionPerformed(ActionEvent e)
  {

  }
}

class AboutBox_button1_actionAdapter implements java.awt.event.ActionListener
{
  AboutBox adaptee;

  AboutBox_button1_actionAdapter(AboutBox adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.button1_actionPerformed(e);
  }
}
