package terrapeer.vui.j3dui.utils.app;

import javax.media.j3d.*;
import javax.vecmath.*;

import java.awt.*;

import terrapeer.vui.j3dui.utils.*;
import terrapeer.vui.j3dui.utils.app.*;

/**
 This class represents a view's "display space" and the internal
 geometry associated with the display and its screen.  It must be
 added to the application frame in order to see its associated
 view of the virtual world.  It also provides image capture support
 through getImage(), such as for printing.
 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */
public class AppDisplay extends Canvas3D
{

  /**
    Constructs a new AppDisplay().  Must be associated with an
    AppView in order to use it.
   */
/*  public AppDisplay()
  {
    // use default 3D graphics config (null)
    super(null);
  }
*/
  public AppDisplay(GraphicsConfiguration gc)
  {
    // use default 3D graphics config (null)
    //super(null);
    super(gc);
  }

  /**
    Gets the display window size in pixels.  The display canvas
    must be live for this to work.
    @param size Size Return display size.
    @return Reference to the display size.
   */
  public Tuple2d getDisplaySize(Tuple2d size)
  {
    getSize(_dimension);
    size.set(_dimension.width, _dimension.height);
    return size;
  }

  /**
    Gets the screen size in pixels.
    @param size Return screen size.
    @return Reference to the screen size.
   */
  public Tuple2d getScreenSize(Tuple2d size)
  {
    // use toolkit for actual system values
    Toolkit toolkit = Toolkit.getDefaultToolkit();

    size.set(
        toolkit.getScreenSize().width,
        toolkit.getScreenSize().height);

    return size;
  }

  /**
    Gets the screen resolution in pixels per meter.
    @param res Return screen resolution.
    @return Reference to the screen resolution.
   */
  public Tuple2d getScreenResolution(Tuple2d res)
  {
    // use toolkit for actual system values
    Toolkit toolkit = Toolkit.getDefaultToolkit();

    /// convert dots per inch to per meter
    res.set(
        (double) toolkit.getScreenResolution() * 39.3701,
        (double) toolkit.getScreenResolution() * 39.3701
        );

    return res;
  }

  /**
    Gets a snapshot image of the current display.  MUST be called
    from a separate thread otherwise the canvas will become
    frozen.  Also, ONLY works for the first display added to the
    frame (bug in Canvas3D?).
    @return New image captured from the display.
   */
  public Image getImage()
  {
    // build raster with canvas size
    ImageComponent2D image2d = new ImageComponent2D(
        ImageComponent.FORMAT_RGB, getWidth(), getHeight());

    Raster raster = new Raster(new Point3f(),
                               Raster.RASTER_COLOR, 0, 0, getWidth(),
                               getHeight(), image2d, null);

    // wait for raster capture
// doesn't seem to be needed
//		_captureRequest = true;
//		repaint();

    getGraphicsContext3D().readRaster(raster);
    return raster.getImage().getImage();
  }

  // Canvas overrides

  public Dimension getMinimumSize()
  {
    return new Dimension(100, 100);
  }

  // Canvas3D overrides

  /**
    Called by the renderer.  Do not use this method directly,
    instead use getImage() to coordinate everything and to get
    the image.
   */
  public void postSwap()
  {
    super.postSwap();

    if (Debug.getEnabled())
    {
      Debug.println("AppDisplay.postSwap",
                    "RENDER:AppDisplay:postSwap: swap done");
    }

    if (_captureRequest)
    {
      // capture dispay image in raster
      _captureRequest = false;

      if (Debug.getEnabled())
      {
        Debug.println("AppDisplay.postSwap",
                      "PRINT:AppDisplay:postSwap: capture done");
      }

    }
  }

  /**
    Called by the renderer.  Do not use this method directly.
    Implements change poster notification after all scenegraph
    changes have been presumably completed.
   */
  public void preRender()
  {

    if (Debug.getEnabled())
    {
      Debug.println("AppDisplay.preRender",
                    "RENDER:AppDisplay:preRender: begin");
    }

//		terrapeer.vui.j3dui.visualize.ChangeSensor.processAllChanges();

//if(Debug.getEnabled()){Debug.println("AppDisplay.preRender",
// "RENDER:AppDisplay:preRender: end changes");}

    super.preRender();
  }

  // personal body ============================================

  /** True if a canvas capture has been requested. */
  private boolean _captureRequest;

  /** Dummy dimension.  (for GC) */
  private Dimension _dimension = new Dimension();

}