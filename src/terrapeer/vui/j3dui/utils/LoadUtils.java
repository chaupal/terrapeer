package terrapeer.vui.j3dui.utils;

import java.io.*;
import java.net.*;
import javax.media.j3d.*;

import java.awt.*;
import java.awt.image.*;

import terrapeer.vui.j3dui.utils.*;

/**
 Contains constants and utilities of general use for loading
 data from file.
 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */

public class LoadUtils
{

  // public utilities =========================================

  /**
    Loads an image from file.  If there is a problem
    it is reported and null is returned.
    @param path The path of the file containing the
    image.  Never null.
    @param size Return size of the image.  Null if none.
    @return A new image.  Null if there was a problem.
   */
  public static Image loadImage(String path, Dimension size)
  {
    if (path == null)
      throw new
          IllegalArgumentException("'path' is null.");

    // load image from file
    Image image = Toolkit.getDefaultToolkit().getImage(path);

    if (Debug.getEnabled())
    {
      Debug.println("LoadUtils.image",
                    "IMAGE: LoadUtils.loadImage:" +
                    " path=" + path +
                    " image=" + image);
    }

    /// make sure its loaded, loop until done
    Component observer = new Component()
    {};
    observer.prepareImage(image, null);

    int status;
    while (true)
    {
      status = observer.checkImage(image, null);

      if ( (status & ImageObserver.ERROR) != 0)
      {
        System.out.println("LoadUtils:loadImage:" +
                           " Error while loading image.");

        if (Debug.getEnabled())
        {
          Debug.println("LoadUtils.image",
                        "TEXTURE: LoadUtils:loadImage:" +
                        " status=" + status +
                        " ALLBITS=" + ImageObserver.ALLBITS +
                        " PROPERTIES=" + ImageObserver.PROPERTIES +
                        " ERROR=" + ImageObserver.ERROR +
                        " ABORT=" + ImageObserver.ABORT);
        }

        return null;
      }
      else if ( (status & ImageObserver.ALLBITS) != 0)
      {
        // image done, quit checking
        break;
      }

      try
      {
        Thread.sleep(100);
      }
      catch (InterruptedException ex)
      {}
    }

    // return results
    if (size != null)
    {
      size.width = image.getWidth(observer);
      size.height = image.getHeight(observer);

      if (Debug.getEnabled())
      {
        Debug.println("LoadUtils.image",
                      "IMAGE: LoadUtils.loadImage:" +
                      " size=" + size);
      }

    }

    return image;
  }

  /**
    Loads a texture image from file.  If there is a problem
    it is reported and null is returned.  Handles textures
    with transparent backgrounds (i.e. preserves alpha).
    @param path The path of the file containing the
    texture image.  Never null.
    @return A new texture.  Null if there was a problem.
   */
  public static Texture2D loadTexture2D(String path)
  {
    if (path == null)
      throw new
          IllegalArgumentException("<path> is null.");

    // load image from file
    Dimension size = new Dimension();
    Image image = loadImage(path, size);

    if (Debug.getEnabled())
    {
      Debug.println("LoadUtils.texture",
                    "TEXTURE: LoadUtils:loadTexture2D:" +
                    " path=" + path +
                    " image=" + image +
                    " size=" + size);
    }

    if (image == null)
      return null;

    // convert image to buffered image        		
    BufferedImage bufImage = new BufferedImage(
        size.width, size.height, BufferedImage.TYPE_INT_ARGB);

    Graphics g = bufImage.getGraphics();
    g.drawImage(image, 0, 0, null);
    g.dispose();

    // convert buffered image to texture 		
    ImageComponent2D image2D = new ImageComponent2D(
        ImageComponent.FORMAT_RGBA, bufImage);

    Texture2D texture = new Texture2D(Texture.BASE_LEVEL,
                                      Texture.RGBA, size.width, size.height);
    texture.setImage(0, image2D);

    return texture;
  }

  /**
    Loads a sound from file.  If there is a problem
    it is reported and null is returned.
    @param soundPath The path of the file containing the sound.
    Never null.
    @return A new sound.  Null if there was a problem.
   */
  public static MediaContainer loadSound(String soundPath)
  {
    if (soundPath == null)
      throw new
          IllegalArgumentException("<soundPath> is null.");

    if (Debug.getEnabled())
    {
      Debug.println("LoadUtils.sound",
                    "SOUND: LoadUtils:loadSound:" +
                    " soundPath=" + soundPath);
    }

    MediaContainer sound = null;

    try
    {
      // load sound from file
      File file = new File(soundPath);
      URL url = file.toURL();

      if (Debug.getEnabled())
      {
        Debug.println("LoadUtils.sound",
                      "SOUND: LoadUtils:loadSound:" +
                      " file=" + file +
                      " url=" + url);
      }

      sound = new MediaContainer(url);
    }
    catch (Exception ex)
    {
      System.out.println("LoadUtils:loadSound:" +
                         " Error while loading sound file.");
      return null;
    }

    if (Debug.getEnabled())
    {
      Debug.println("LoadUtils.sound",
                    "SOUND: LoadUtils:loadSound:" +
                    " sound=" + sound);
    }

    return sound;
  }

}