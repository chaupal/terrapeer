package terrapeer.gui;

/**
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

import terrapeer.*;
import terrapeer.gui.*;
import terrapeer.vui.*;
import javax.swing.text.html.HTMLDocument;
import java.net.URL;
import java.net.*;

public class HelpBox extends JDialog
{
  PanelTemplate panelTemplate1 = new PanelTemplate();
  JScrollPane jScrollPane1 = new JScrollPane();
  JEditorPane jEditorPaneHelp = new JEditorPane();
  BorderLayout borderLayout1 = new BorderLayout();
  JButton jButtonCancel = new JButton();
  JButton jButtonOK = new JButton();
  JComboBox jComboBoxTopic = new JComboBox();

  public HelpBox(Frame frame, String title, boolean modal)
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

  public HelpBox()
  {
    this(null, "", false);
  }

  private void jbInit() throws Exception
  {
    this.setTitle("TerraPeer Help");
    clearAll();
    jButtonOK.addMouseListener(new HelpBox_jButtonOK_mouseAdapter(this));
    jButtonCancel.addMouseListener(new HelpBox_jButtonCancel_mouseAdapter(this));
    jScrollPane1.setFont(new java.awt.Font("SansSerif", 0, 10));
    jScrollPane1.getViewport().add(jEditorPaneHelp, null);
    panelTemplate1.jPanelCenter.setLayout(borderLayout1);
    panelTemplate1.jPanelCenter.setPreferredSize(new Dimension(1000, 400));
    panelTemplate1.jPanelCenter.add(jComboBoxTopic,  BorderLayout.NORTH);
    panelTemplate1.jPanelCenter.add(jScrollPane1,  BorderLayout.CENTER);
    panelTemplate1.setTitle("Help");
    panelTemplate1.setDescription(vars.DESCR_HELP_DIALOG);
    panelTemplate1.jPanelSouth.add(jButtonCancel, null);
    panelTemplate1.jPanelSouth.add(jButtonOK, null);
    jButtonCancel.setPreferredSize(new Dimension(80, 23));
    jButtonCancel.setText("Cancel");
    jButtonOK.setPreferredSize(new Dimension(80, 23));
    jButtonOK.setText("OK");
    panelTemplate1.jPanelSouth.add(jButtonCancel, null);
    panelTemplate1.jPanelSouth.add(jButtonOK, null);
    this.getContentPane().add(panelTemplate1, BorderLayout.CENTER);
  }

  public void clearAll()
  {
    jComboBoxTopic.addActionListener(new HelpBox_jComboBoxTopic_actionAdapter(this));
    jComboBoxTopic.addItem("Introduction");
    jComboBoxTopic.addItem("GUI Overview");
    jComboBoxTopic.addItem("Virtual Space");
    jComboBoxTopic.addItem("P2P Network");
    jComboBoxTopic.addItem("Personal Tools");
    jComboBoxTopic.addItem("Troubleshooting");

    //jEditorPaneHelp.setContentType();
    jEditorPaneHelp.setFont(new java.awt.Font("Monospaced", 0, 10));
    jEditorPaneHelp.setVerifyInputWhenFocusTarget(true);
    jEditorPaneHelp.setEditable(false);
    jEditorPaneHelp.setText("Welcome to the Help Viewer!\n\n");
  }

  void jButtonOK_mouseClicked(MouseEvent e)
  {
    hide();
  }

  void jButtonCancel_mouseClicked(MouseEvent e)
  {
    hide();
  }

  void jComboBoxTopic_actionPerformed(ActionEvent e)
  {
    loadHTMLpage(jComboBoxTopic.getSelectedIndex());
  }

  public void loadHTMLpage(int htmlPageIndex)
  {
    File f = new File( vars.APP_HELP_PATH +File.separatorChar+ vars.HELP_PAGES[htmlPageIndex] );
    //FileReader fr = new FileReader(f);
    //FileInputStream fis = new FileInputStream(f);
    URL url = null;
    try
    {
      url = new URL("file", "", vars.APP_HELP_PATH + File.separatorChar + vars.HELP_PAGES[htmlPageIndex]);
    }
    catch (MalformedURLException ex1)
    {
    }
    try
    {
      jEditorPaneHelp.setPage(url);
    }
    catch (IOException ex)
    {
    }
  }

}

class HelpBox_jButtonOK_mouseAdapter extends java.awt.event.MouseAdapter
{
  HelpBox adaptee;

  HelpBox_jButtonOK_mouseAdapter(HelpBox adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jButtonOK_mouseClicked(e);
  }
}

class HelpBox_jButtonCancel_mouseAdapter extends java.awt.event.MouseAdapter
{
  HelpBox adaptee;

  HelpBox_jButtonCancel_mouseAdapter(HelpBox adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jButtonCancel_mouseClicked(e);
  }
}

class HelpBox_jComboBoxTopic_actionAdapter implements java.awt.event.ActionListener
{
  HelpBox adaptee;

  HelpBox_jComboBoxTopic_actionAdapter(HelpBox adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jComboBoxTopic_actionPerformed(e);
  }
}
