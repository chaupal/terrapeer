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
 Standard test frame containing an AppWorld and two AppViews and
 their displays.
 <P>
 To use this frame...
 <UL>
 <LI> Get the world with getWorld() and add some visible objects
 to its scene root.
 <LI> Get the views with getViewLeft() and getViewRight() and
 add them to the world's view root.
 <LI> Use showFrame() when everything is ready to be shown.
 </UL>
 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */
public class DoubleFrame extends JFrame
{

  // public interface =========================================

  /**
    Constructs a DoubleFrame with an empty virtual world.
   */
  public DoubleFrame()
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
      Debug.println("DoubleFrame.verbose",
                    "VIEW:DoubleFrame.showFrame:" +
                    "\nviewL:" +
                    " screenSize=" + _viewL.getDisplay().getScreenSize(vec) +
                    " screenRes=" + _viewL.getDisplay().getScreenResolution(vec) +
                    " displaySize=" + _viewL.getDisplay().getDisplaySize(vec) +
                    "\nviewR:" +
                    " screenSize=" + _viewR.getDisplay().getScreenSize(vec) +
                    " screenRes=" + _viewR.getDisplay().getScreenResolution(vec) +
                    " displaySize=" + _viewR.getDisplay().getDisplaySize(vec));
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
    Gets the frame's left view.
    @return The left view.
   */
  public AppView getViewLeft()
  {
    return _viewL;
  }

  /**
    Gets the frame's right view.
    @return The right view.
   */
  public AppView getViewRight()
  {
    return _viewR;
  }

  // personal body ============================================

  /** The virtual world. */
  private AppWorld _world = new AppWorld();

  /** The window left view. */
  private AppView _viewL = new AppView();

  /** The window right view. */
  private AppView _viewR = new AppView();

  // component builders

  /**
    Builds the window contents panel and adds it to the
    window.
   */
  protected void buildContentPanel()
  {

    // build view panel
    JPanel panel = new JPanel();

    panel.setPreferredSize(new Dimension(400, 200));
    panel.setLayout(new GridLayout(1, 2, 2, 2));

    panel.add(_viewL.getDisplay());
    panel.add(_viewR.getDisplay());

    // add it to frame		
    getContentPane().add(panel, BorderLayout.CENTER);
  }

}