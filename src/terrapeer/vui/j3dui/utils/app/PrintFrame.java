package terrapeer.vui.j3dui.utils.app;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import terrapeer.vui.j3dui.utils.app.*;

/**
 This is a frame showing a static image as a print preview.
 A print button allows the image to be printed.
 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */
public class PrintFrame extends Frame
{

  // public interface =========================================

  /**
    Constructs a PrintFrame from the specified image canvas.
    @param image Source image.
   */
  public PrintFrame(ImageCanvas canvas)
  {
    super("Print Preview");
    buildFrame(canvas);
  }

  /**
    Initiates a print job that prints just the frame's image.
   */
  public void doPrint()
  {
    new ImagePrinter(_canvas.getImage());
  }

  // personal body ============================================

  /** Image canvas. */
  private ImageCanvas _canvas;

  /**
    Builds and shows the window frame containing a canvas image.
    @param canvas Canvas containing the display image.
   */
  protected void buildFrame(ImageCanvas canvas)
  {
    // dispose of frame
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e)
      {
        dispose();
      }
    });

    _canvas = canvas;
    buildContentPanel();

    pack();
    show();
  }

  /**
    Builds the window contents panel and adds it to the window.
   */
  protected void buildContentPanel()
  {

    JPanel panel = new JPanel(new BorderLayout());
    panel.setPreferredSize(new Dimension(300, 350));

    // image
    panel.add(_canvas, BorderLayout.CENTER);

    // controls Box
    JPanel controlPanel = new JPanel(new FlowLayout());
    panel.add(controlPanel, BorderLayout.SOUTH);

    JButton printButton = new JButton("Print");
    controlPanel.add(printButton);

    printButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent ev)
      {
        doPrint();
      }
    });

    // add it to frame		
    add(panel);

  }

}