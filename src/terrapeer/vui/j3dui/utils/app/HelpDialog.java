package terrapeer.vui.j3dui.utils.app;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import terrapeer.vui.j3dui.utils.app.*;

/**
 Standard scrollable dialog for providing initial user help.
 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */
public class HelpDialog extends Dialog
{

  // public interface =========================================

  /**
    Constructs a HelpDialog.
    @param owner Frame owning this dialog.
    @param title Title of this dialog.
    @param text Text in this dialog.
   */
  public HelpDialog(Frame owner, String title, String text)
  {
    super(owner, title);

    // kill the dialog on close
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent winEvent)
      {
        dispose();
      }
    });

    // build dialog contents
    /// build text area with line wrap
    JTextArea area = new JTextArea(text);
    area.setLineWrap(true);
    area.setWrapStyleWord(true);
    area.setEditable(false);

    /// make text scrollable and add to dialog
    JScrollPane pane = new JScrollPane(area);
    add(pane, BorderLayout.CENTER);

    // show dialog
    pack();
    setSize(475, 375);
    setLocation(225, 100);
    show();
  }

}