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

public class NavControlSpeed extends JPanel
{
  Graphics2D g2 = null;
  double currDegree = 0.0;
  ImageIcon img_wheel = null;
  boolean firsttime = true;

  private int component_x_offset = 0;
  private int component_y_offset = 0;

  public NavControlSpeed()
  {
    this.setPreferredSize(new Dimension(90, 30));
  }

  public void initControl()
  {
    img_wheel = new ImageIcon(terrapeer.gui.Compass.class.getResource(vars.IMG_PATH+File.separatorChar+vars.IMG_FILE[55]));
  }

  private void doFirstTime()
  {
    drawImages();
  }

  private void drawImages()
  {
    //draw wheel
    g2.drawImage(img_wheel.getImage(), 0, 0, 100, 100, img_wheel.getImageObserver());
  }

  /**
   * The object paint method is called whenever the GUI is repainted.
   * A compass component is painted on top on the canvas.
   * @param g Graphics
   */
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
