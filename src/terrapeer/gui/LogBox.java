package terrapeer.gui;

/**
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P feedback system</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

import terrapeer.*;
import terrapeer.gui.*;
import terrapeer.vui.*;

public class LogBox extends JDialog
{
  PanelTemplate panelTemplate1 = new PanelTemplate();
  JScrollPane jScrollPane1 = new JScrollPane();
  JTextArea jTextAreaLog = new JTextArea();
  BorderLayout borderLayout1 = new BorderLayout();
  JButton jButtonCancel = new JButton();
  JButton jButtonOK = new JButton();

  public LogBox(Frame frame, String title, boolean modal)
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

  public LogBox()
  {
    this(null, "", false);
  }

  private void jbInit() throws Exception
  {
    this.setTitle("TerraPeer Log");
    clearAll();
    jButtonOK.addMouseListener(new DialogLog_jButtonOK_mouseAdapter(this));
    jButtonCancel.addMouseListener(new DialogLog_jButtonCancel_mouseAdapter(this));
    jScrollPane1.setFont(new java.awt.Font("SansSerif", 0, 10));
    jScrollPane1.getViewport().add(jTextAreaLog, null);
    panelTemplate1.jPanelCenter.setLayout(borderLayout1);
    panelTemplate1.jPanelCenter.setPreferredSize(new Dimension(1000, 400));
    panelTemplate1.jPanelCenter.add(jScrollPane1,  BorderLayout.CENTER);
    panelTemplate1.setTitle("Log");
    panelTemplate1.setDescription(vars.DESCR_LOG_DIALOG);
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
    jTextAreaLog.setFont(new java.awt.Font("Monospaced", 0, 10));
    jTextAreaLog.setVerifyInputWhenFocusTarget(true);
    jTextAreaLog.setEditable(false);
    jTextAreaLog.setText("Welcome to the Log Viewer!\n\n");
    jTextAreaLog.setLineWrap(true);
    jTextAreaLog.setWrapStyleWord(true);
    jTextAreaLog.addMouseListener(new DialogLog_jTextAreaLog_mouseAdapter(this));
  }

  public void setMessages(TerraPeerLog.logItem[] logitem)
  {
    clearAll();
    for(int i=0; i<logitem.length; i++)
      addMessage(logitem[i]);
  }

  public void addMessage(TerraPeerLog.logItem logitem)
  {
    if(logitem!=null && logitem.logID!=-1)
      jTextAreaLog.append("\n"+logitem.logID+": ["+
                           logitem.date.getTime().toString()+
                           /*logitem.date.get(Calendar.HOUR)+":"+
                           logitem.date.get(Calendar.MINUTE)+":"+
                           logitem.date.get(Calendar.SECOND)+*/
                           "] <"+logitem.level+"> "+logitem.message);
    jTextAreaLog.setCaretPosition(jTextAreaLog.getText().length());
  }

  void jButtonOK_mouseClicked(MouseEvent e)
  {
    hide();
  }

  void jTextAreaLog_mouseClicked(MouseEvent e)
  {

  }

  void jButtonCancel_mouseClicked(MouseEvent e)
  {
    hide();
  }



}

class DialogLog_jButtonOK_mouseAdapter extends java.awt.event.MouseAdapter
{
  LogBox adaptee;

  DialogLog_jButtonOK_mouseAdapter(LogBox adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jButtonOK_mouseClicked(e);
  }
}

class DialogLog_jTextAreaLog_mouseAdapter extends java.awt.event.MouseAdapter
{
  LogBox adaptee;

  DialogLog_jTextAreaLog_mouseAdapter(LogBox adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jTextAreaLog_mouseClicked(e);
  }
}

class DialogLog_jButtonCancel_mouseAdapter extends java.awt.event.MouseAdapter
{
  LogBox adaptee;

  DialogLog_jButtonCancel_mouseAdapter(LogBox adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.jButtonCancel_mouseClicked(e);
  }
}
