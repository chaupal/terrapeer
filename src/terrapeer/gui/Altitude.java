package terrapeer.gui;

import java.awt.*;
import javax.swing.*;
import terrapeer.*;
import java.io.*;
import java.awt.geom.AffineTransform;
import java.awt.Graphics2D;

/**
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */

public class Altitude extends JPanel
{
  Graphics2D g2 = null;
  int currAlt = 0;
  ImageIcon img_base = null;
  ImageIcon img_pointer = null;
  boolean firsttime = true;

  private int component_x_offset = 10;
  private int component_y_offset = 5;

  public Altitude()
  {
    this.setPreferredSize(new Dimension(44, 132));
  }

  public void initAltitude()
  {
    //g2 = (Graphics2D) this.getGraphics();
    img_base =  new ImageIcon(terrapeer.gui.Altitude.class.getResource(vars.IMG_PATH+File.separatorChar+vars.IMG_FILE[54]));
    img_pointer = new ImageIcon(terrapeer.gui.Altitude.class.getResource(vars.IMG_PATH+File.separatorChar+vars.IMG_FILE[52]));
  }

  private void doFirstTime()
  {
    drawImages();
  }

  public void moveAltitude(int alt)
  {
    if(alt>=vars.SPACENAV_LIMIT_DN && alt<=vars.SPACENAV_LIMIT_UP)
    {
      currAlt = alt;
      //paintComponent();
      //this.repaint();
    }
    else
    {
        //ERR
    }
  }

  /**
   * Takes altitude between SPACENAV_LIMIT_DOWN and SPACENAV_LIMIT_UP
   * in meters; converts to range
   *
   * @param alt int
   * @todo only works for certain LIMIT
   */
  private void transformPointer()
  {
    //dy between SPACENAV_LIMIT, max pixel range 0...128
    //128 px = alt_range m  =>  128/alt_range px/m
    double ratio = 0.128; //(vars.SPACENAV_LIMIT_UP - vars.SPACENAV_LIMIT_DN);
    int dx = 0;
    double dy = 128 - 14 - (currAlt * ratio);

    //System.out.println("A ratio= "+ratio);

    AffineTransform at_pointer1 = new AffineTransform();
    at_pointer1.translate( dx, dy);
    g2.transform(at_pointer1);
  }

  private void drawImages()
  {
    //draw base
    g2.drawImage(img_base.getImage(), component_x_offset + 11, component_y_offset + 0, 32, 128, img_base.getImageObserver());

    //draw pointer
    transformPointer();
    g2.drawImage(img_pointer.getImage(), component_x_offset + 6, component_y_offset + 0, 11, 14, img_pointer.getImageObserver());
  }

  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    g2 = (Graphics2D) g;

    //first time
    if(firsttime)
    {
      doFirstTime();
      firsttime = false;
    }

    drawImages();
  }

}
