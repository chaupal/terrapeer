package terrapeer.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import javax.swing.border.*;

import terrapeer.*;
import com.borland.internetbeans.*;
import javax.swing.event.*;

/**
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P DVE System</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */

public class BrowserFrame extends JFrame
{
  private static TerraPeerLog myLog = TerraPeerLog.getLogger();

  URL urlHome = null;
  URL[] urlHistory = new URL[40];
  int urlHistoryIndex = 0;
  URL[] urlBookmarks = new URL[40];
  int urlBindex = 0;

  BorderLayout borderLayout1 = new BorderLayout();
  JButton jButton1 = new JButton();
  JPanel jPanelSouth = new JPanel();
  JPanel jPanelNorth = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JLabel jLabel1 = new JLabel();
  JPanel panel1 = new JPanel();
  JPanel jPanelCenter = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  JPanel jPanelBN = new JPanel();
  JPanel jPanelBC = new JPanel();
  JScrollPane jScrollPane1 = new JScrollPane();
  BorderLayout borderLayout4 = new BorderLayout();
  JButton jButtonHome = new JButton();
  JButton jButtonBack = new JButton();
  JTextField jTextFieldURL = new JTextField();
  JButton jButtonGo = new JButton();
  JEditorPane jEditorPaneWeb = new JEditorPane();
  JComboBox jComboBoxBookmarks = new JComboBox();
  JPanel jPanel1 = new JPanel();
  JLabel jLabelTitle = new JLabel();
  JLabel jLabel7 = new JLabel();
  JLabel jLabelStatus = new JLabel();
  GridLayout gridLayout1 = new GridLayout();
  Border border1;
  JLabel jLabel8 = new JLabel();
  JButton jButtonBookmark = new JButton();
  JScrollPane jScrollPane2 = new JScrollPane();

  public BrowserFrame()
  {
    try
    {
      urlHome = new URL("http://www.gbar.dtu.dk/~s948179/master");
    }
    catch (MalformedURLException ex1)
    {
    }

    try
    {
      jbInit();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }

    showHidePanels();
  }

  void jbInit() throws Exception
  {
    this.setTitle("TerraPeer Browser");
    border1 = BorderFactory.createEmptyBorder(2,10,2,10);
    jPanelSouth.setBackground(new Color(220, 220, 200));
    jPanelSouth.setBorder(border1);
    jPanelSouth.setLayout(gridLayout1);
    jButton1.setText("Close");
    jButton1.addActionListener(new BrowserFrame_jButton1_actionAdapter(this));
    this.getContentPane().setLayout(borderLayout1);
    jPanelNorth.setBackground(new Color(220, 220, 200));
    jPanelNorth.setBorder(BorderFactory.createEtchedBorder());
    jPanelNorth.addMouseListener(new BrowserFrame_jPanelNorth_mouseAdapter(this));
    jLabel1.setFont(new java.awt.Font("Dialog", 1, 11));
    jLabel1.setForeground(new Color(0, 0, 84));
    jLabel1.setText("TerrraPeer Browser");
    panel1.setLayout(borderLayout2);
    jPanelCenter.setLayout(borderLayout3);
    jPanelBC.setLayout(borderLayout4);
    jPanelBC.setBorder(BorderFactory.createEtchedBorder());
    jButtonHome.setText("Home");
    jButtonHome.addActionListener(new BrowserFrame_jButtonHome_actionAdapter(this));
    jButtonBack.setText("Back");
    jButtonBack.addActionListener(new BrowserFrame_jButtonBack_actionAdapter(this));
    jTextFieldURL.setPreferredSize(new Dimension(180, 20));
    jTextFieldURL.setToolTipText("");
    jTextFieldURL.setText("");
    jButtonGo.setText("Go");
    jButtonGo.addActionListener(new BrowserFrame_jButtonGo_actionAdapter(this));
    jLabelTitle.setHorizontalAlignment(SwingConstants.LEADING);
    jLabelTitle.setText("Title");
    jLabelTitle.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
    jEditorPaneWeb.setEditable(false);
    jEditorPaneWeb.setText("");
    jEditorPaneWeb.addHyperlinkListener(new BrowserFrame_jEditorPaneWeb_hyperlinkAdapter(this));
    jLabel7.setFont(new java.awt.Font("Tahoma", 0, 20));
    jLabel7.setForeground(Color.lightGray);
    jLabel7.setText(" | ");
    jLabelStatus.setFont(new java.awt.Font("Tahoma", 0, 9));
    jLabelStatus.setForeground(Color.gray);
    jLabelStatus.setText("Status");
    jLabel8.setText(" | ");
    jLabel8.setForeground(Color.lightGray);
    jLabel8.setFont(new java.awt.Font("Tahoma", 0, 20));
    jButtonBookmark.setText("Bookmark");
    jButtonBookmark.addActionListener(new BrowserFrame_jButtonBookmark_actionAdapter(this));
    jComboBoxBookmarks.addItemListener(new BrowserFrame_jComboBoxBookmarks_itemAdapter(this));
    panel1.add(jPanelNorth, BorderLayout.NORTH);
    jPanelNorth.add(jLabel1, null);
    panel1.add(jPanelCenter, BorderLayout.CENTER);
    jPanelCenter.add(jPanelBN,  BorderLayout.NORTH);
    jPanelCenter.add(jPanelBC, BorderLayout.CENTER);
    jPanelCenter.add(jPanelBN, BorderLayout.NORTH);
    panel1.add(jPanelSouth, BorderLayout.SOUTH);
    jPanelSouth.add(jLabelStatus, null);
    this.getContentPane().add(panel1,  BorderLayout.CENTER);
    jPanelBC.add(jPanel1, BorderLayout.NORTH);
    jPanel1.add(jLabelTitle, null);
    jPanelBC.add(jScrollPane1, BorderLayout.CENTER);
    jPanelBC.add(jScrollPane2,  BorderLayout.SOUTH);
    jScrollPane1.getViewport().add(jEditorPaneWeb, null);
    jPanelBN.add(jButtonHome, null);
    jPanelBN.add(jComboBoxBookmarks, null);
    jPanelBN.add(jButtonBookmark, null);
    jPanelBN.add(jLabel8, null);
    jPanelBN.add(jButtonBack, null);
    jPanelBN.add(jTextFieldURL, null);
    jPanelBN.add(jButtonGo, null);
    jPanelBN.add(jLabel7, null);
    jPanelBN.add(jButton1, null);
  }

  /**
   * Updates status bar
   * @param type int
   * @param msg String
   * @todo impl type
   */
  void setStatus(int type, String msg)
  {
    if(type!=-1)
      jLabelStatus.setText(msg);
  }

  void goHome()
  {
    myLog.addMessage(4, "Browser: Going home");
    goToURL(urlHome);
    urlHistory[urlHistoryIndex] = urlHome;
    urlHistoryIndex++;
  }

  void goToURL(URL u)
  {
    jTextFieldURL.setBackground(Color.gray);
    setStatus(0, "Trying to open URL "+u.getProtocol()+":"+u.getPort()+"//"+u.getHost()+u.getPath()+" ...");
    myLog.addMessage(4, "Browser: Trying to open URL "+u.getProtocol()+":"+u.getPort()+"// "+u.getHost()+" "+u.getPath()+" ...");
    if(u!=null)
    {
      urlHistory[urlHistoryIndex] = u;
      urlHistoryIndex++;

      try
      {
        jEditorPaneWeb.setPage(u);
        jLabelTitle.setText(u.getHost()+u.getPath());
      }
      catch (IOException ex1)
      {
        setStatus(0, "I/O Exception ("+u.getPath()+")");
      }
    }
    setStatus(0, "URL Loaded: "+u.getProtocol()+":"+u.getPort()+"//"+u.getHost()+u.getPath()+" ...");
    jTextFieldURL.setBackground(Color.white);
  }

  void bookmarkURL(URL u)
  {
    myLog.addMessage(4, "Browser: Bookmarking URL "+u.getPath());
    urlBookmarks[urlBindex] = u;
    jComboBoxBookmarks.addItem(
      urlBookmarks[urlBindex].getProtocol()+"://"+
      urlBookmarks[urlBindex].getHost()+
      urlBookmarks[urlBindex].getPath());
    urlBindex++;
  }

  /**
   * Go to specified URL
   */
  void goToURL()
  {
    URL u = null;
    try
    {
      u = new URL(jTextFieldURL.getText());
    }
    catch (MalformedURLException ex)
    {
      jTextFieldURL.setBackground(Color.red);
      setStatus(0, "Seems like the URL is malformed...");
      jTextFieldURL.setBackground(Color.white);
      return;
    }

    goToURL(u);
  }

  void goBack()
  {
    myLog.addMessage(4, "Browser: Going back");
    urlHistoryIndex -= 2;
    goToURL(urlHistory[urlHistoryIndex]);
  }

  void showHidePanels()
  {
    if(jPanelBN.isVisible())
    {
      jPanelBN.setVisible(false);
      jPanelSouth.setVisible(false);
    }
    else
    {
      jPanelBN.setVisible(true);
      jPanelSouth.setVisible(true);
    }
  }


  void jButton1_actionPerformed(ActionEvent e)
  {
    hide();
  }

  void jButtonGo_actionPerformed(ActionEvent e)
  {
    goToURL();
  }

  void jButtonBack_actionPerformed(ActionEvent e)
  {
    goBack();
  }

  void jButtonHome_actionPerformed(ActionEvent e)
  {
    goHome();
  }

  void jButtonBookmark_actionPerformed(ActionEvent e)
  {
    bookmarkURL(urlHistory[urlHistoryIndex-1]);
  }

  void jPanelNorth_mouseClicked(MouseEvent e)
  {
    showHidePanels();
  }

  void jComboBoxBookmarks_itemStateChanged(ItemEvent e)
  {
    try
    {
      goToURL(new URL((String)jComboBoxBookmarks.getSelectedItem()));
    }
    catch (MalformedURLException ex)
    {
    }
  }

  void jEditorPaneWeb_hyperlinkUpdate(HyperlinkEvent e)
  {
    if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
    {
      goToURL(e.getURL());
    }
  }


}

class BrowserFrame_jButtonGo_actionAdapter implements java.awt.event.ActionListener
{
  BrowserFrame adaptee;

  BrowserFrame_jButtonGo_actionAdapter(BrowserFrame adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonGo_actionPerformed(e);
  }
}

class BrowserFrame_jButtonBack_actionAdapter implements java.awt.event.ActionListener
{
  BrowserFrame adaptee;

  BrowserFrame_jButtonBack_actionAdapter(BrowserFrame adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonBack_actionPerformed(e);
  }
}

class BrowserFrame_jButtonHome_actionAdapter implements java.awt.event.ActionListener
{
  BrowserFrame adaptee;

  BrowserFrame_jButtonHome_actionAdapter(BrowserFrame adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonHome_actionPerformed(e);
  }
}

class BrowserFrame_jButtonBookmark_actionAdapter implements java.awt.event.ActionListener
{
  BrowserFrame adaptee;

  BrowserFrame_jButtonBookmark_actionAdapter(BrowserFrame adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButtonBookmark_actionPerformed(e);
  }
}

class BrowserFrame_jPanelNorth_mouseAdapter extends java.awt.event.MouseAdapter
{
  BrowserFrame adaptee;

  BrowserFrame_jPanelNorth_mouseAdapter(BrowserFrame adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jPanelNorth_mouseClicked(e);
  }
}

class BrowserFrame_jComboBoxBookmarks_itemAdapter implements java.awt.event.ItemListener
{
  BrowserFrame adaptee;

  BrowserFrame_jComboBoxBookmarks_itemAdapter(BrowserFrame adaptee)
  {
    this.adaptee = adaptee;
  }
  public void itemStateChanged(ItemEvent e)
  {
    adaptee.jComboBoxBookmarks_itemStateChanged(e);
  }
}

class BrowserFrame_jButton1_actionAdapter implements java.awt.event.ActionListener
{
  BrowserFrame adaptee;

  BrowserFrame_jButton1_actionAdapter(BrowserFrame adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.jButton1_actionPerformed(e);
  }
}

class BrowserFrame_jEditorPaneWeb_hyperlinkAdapter implements javax.swing.event.HyperlinkListener
{
  BrowserFrame adaptee;

  BrowserFrame_jEditorPaneWeb_hyperlinkAdapter(BrowserFrame adaptee)
  {
    this.adaptee = adaptee;
  }
  public void hyperlinkUpdate(HyperlinkEvent e)
  {
    adaptee.jEditorPaneWeb_hyperlinkUpdate(e);
  }
}
