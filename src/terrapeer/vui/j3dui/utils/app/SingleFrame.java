package terrapeer.vui.j3dui.utils.app;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.utils.*;
import terrapeer.vui.j3dui.utils.objects.*;

/**
 Standard test frame containing an AppWorld and one AppView and
 its display.
 <P>
 To use this frame...
 <UL>
 <LI> Get the world with getWorld() and add some visible objects
 to its scene root.
 <LI> Get the view with getView() and it to the world's view root.
 <LI> Use showFrame() when everything is ready to be shown.
 </UL>
 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved. Edited 2003 by Henrik Gehrmann.
 */
public class SingleFrame extends JFrame
{

  // public interface =========================================

  /**
    Constructs a SingleFrame with an empty virtual world.
   */
  public SingleFrame()
  {
    // kill the window on close
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent winEvent)
      {
        System.exit(0);
      }
    });

    // build window components
    buildContentPanel();
  }

  /**
    Makes the world live and sizes and shows the frame.
   */
  public void showFrame()
  {
    _world.setLive(true);

    // size contents THEN show frame
    pack();
    show();

    if (Debug.getEnabled())
    {
      Vector2d vec = new Vector2d();
      Debug.println("SingleFrame.verbose",
                    "VIEW:SingleFrame.showFrame:" +
                    " screenSize=" + _view.getDisplay().getScreenSize(vec) +
                    " screenRes=" + _view.getDisplay().getScreenResolution(vec) +
                    " displaySize=" + _view.getDisplay().getDisplaySize(vec));
    }

  }

  /**
    Gets the frame's 3D world.
    @return The 3D world.
   */
  public AppWorld getWorld()
  {
    return _world;
  }

  /**
    Gets the frame's view.
    @return The view.
   */
  public AppView getView()
  {
    return _view;
  }

  // personal body ============================================

  /** The virtual world. */
  private AppWorld _world = new AppWorld();

  /** The window view. */
  private AppView _view = new AppView();

  // component builders

  /**
    Builds the window contents panel and adds it to the
    window.
   */
  protected void buildContentPanel()
  {

    // build view panel
    JPanel panel = new JPanel();

    panel.setPreferredSize(new Dimension(300, 300));
    panel.setLayout(new GridLayout(1, 1, 2, 2));

    panel.add(_view.getDisplay());

    // add it to frame
    getContentPane().add(panel, BorderLayout.CENTER);
  }

}