package terrapeer.vui.j3dui.utils.app;

import java.awt.*;

import terrapeer.vui.j3dui.utils.*;
import terrapeer.vui.j3dui.utils.app.*;

/**
 General purpose canvas for displaying a static image.  The
 image is drawn such that its aspect ratio is preserved as the
 canvas is resized.
 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */
public class ImageCanvas extends Canvas
{

  // public interface =========================================

  /**
    Constructs an ImageCanvas from the specified image.
    @param image Source image.
   */
  public ImageCanvas(Image image)
  {
    _image = image;
    repaint();
  }

  /**
    Constructs an ImageCanvas from the specified image file.
    @param file Source image file.
   */
  public ImageCanvas(String file)
  {
    this(LoadUtils.loadImage(file, null));
  }

  /**
    Constructs an ImageCanvas from the specified display by
    capturing a snapshot of its image.
    @param display Source image display.
   */
  public ImageCanvas(AppDisplay display)
  {
    // must use a thread to get the display image
    // otherwise display is left frozen
    _display = display;
    Thread thread = new Thread(new Runnable()
    {
      public void run()
      {
        _image = _display.getImage();
      }
    });

    thread.start();

    // wait for completion and then update
    try
    {
      thread.join();
      repaint();
    }
    catch (InterruptedException ex)
    {}
  }

  /**
    Gets this object's image.
    @return Reference to this object's image.
   */
  public Image getImage()
  {
    return _image;
  }

  // Canvas overrides

  public Dimension getMinimumSize()
  {
    return new Dimension(100, 100);
  }

  public void paint(Graphics g)
  {
    update(g);
  }

  public void update(Graphics g)
  {
    // maintain constant aspect ration
    int size = Math.min(getSize().width, getSize().height);
    int posX = (getSize().width - size) / 2;
    int posY = 0;

    // draw it
    if (_image != null)
    {
      g.drawImage(_image, posX, posY, size, size, this);
    }
  }

  // personal body ============================================

  /** The image. */
  private Image _image = null;

  /** Dummy for thread argument. */
  private AppDisplay _display;

}