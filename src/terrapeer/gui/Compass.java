package terrapeer.gui;

import java.awt.*;
import javax.swing.*;
import terrapeer.*;
import java.io.*;
import java.awt.geom.AffineTransform;
import java.awt.Graphics2D;

/**
* The Compass class represents a GUI component that is able to show a
* basic compass-rose with a pointer on top. The pointer's direction can be
* adjusted through the <method>movePointer</method> method, where it's
* parameter sets the degree-value (0-360) of the direction, starting at the
* top and going clock-wise.
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */

public class Compass extends JPanel
{
  Graphics2D g2 = null;
  double currDegree = 0.0;
  ImageIcon img_wheel = null;
  ImageIcon img_pointer = null;
  boolean firsttime = true;

  private int component_x_offset = 10;
  private int component_y_offset = 2;

  public Compass()
  {
    this.setPreferredSize(new Dimension(120, 120));
  }

  public void initCompass()
  {
    //g2 = (Graphics2D) this.getGraphics();
    img_wheel = new ImageIcon(terrapeer.gui.Compass.class.getResource(vars.IMG_PATH+File.separatorChar+vars.IMG_FILE[55]));
    img_pointer = new ImageIcon(terrapeer.gui.Compass.class.getResource(vars.IMG_PATH+File.separatorChar+vars.IMG_FILE[50]));
  }

  private void doFirstTime()
  {
    drawImages();

    //compassThread t = new compassThread(this);
    //t.start();
  }

  class compassThread extends Thread
  {
    private Compass c = null;

    public compassThread(Compass c)
    {
      this.c = c;
    }

    public void run()
    {
      while(true)
      {
        currDegree += 5;
        if(currDegree>=360) currDegree = 0;
        c.paintComponent(c.getGraphics());
        try
        {
          currentThread().sleep(10);
        }
        catch (InterruptedException ex)
        {
        }
      }
    }
  }

  /**
   * Takes degree between -180.0 and 180.0
   * Transforms to 0 to 360.
   * @param degree int
   */
  public void moveHeading(double degree)
  {
    if(degree>=-180 && degree<=180)
    {
      currDegree = degree;
      if(degree>=-180 && degree<0)
        currDegree = 360 + degree;

      //paintComponent(getGraphics());
    }
    else
    {
        //ERR
    }
  }

  /**
   * This function transforms the compass pointer to the correct current
   * direction by rotating and moving the image to the appropriate position
   * @param degree int
   */
  private void transformPointer()
  {
    int dx = (int)(25.0 * Math.cos(Math.toRadians(currDegree-90)));
    int dy = (int)(25.0 * Math.sin(Math.toRadians(currDegree-90)));

    //System.out.println("dx = "+dx+"   dy = "+dy);
    /*
    if(degree%90 != 0)
    {
      dx *= 1;
      dy *= 1;
    }*/

    AffineTransform at_pointer1 = new AffineTransform();
    at_pointer1.translate(50-8+dx+component_x_offset, 50-16+dy+component_y_offset);
    g2.transform(at_pointer1);
    AffineTransform at_pointer2 = new AffineTransform();
    //at_pointer2.preConcatenate(at_pointer1);
    at_pointer2.setToRotation(Math.toRadians(currDegree), 8, 16); //(this.getWidth()-16)/2, (this.getHeight()-32)/2 );
    g2.transform(at_pointer2);
  }

  private void drawImages()
  {
    //draw wheel
    //AffineTransform at_wheel = new AffineTransform();
    //at_wheel.translate( 14,14 );//(this.getWidth()-100)/2, (this.getHeight()-100)/2 );
    //g2.transform(at_wheel);
    g2.drawImage(img_wheel.getImage(), component_x_offset, component_y_offset, 100, 100, img_wheel.getImageObserver());

    //transparancy
    //g2.setComposite(makeComposite(0.5F));
    //img = new ImageIcon(terrapeer.gui.TerraPeerGUI.class.getResource(vars.IMG_PATH+File.separatorChar+vars.IMG_FILE[54]));
    //g2.drawImage(img.getImage(), 0, 0, 120, 120, img.getImageObserver());

    //draw pointer
    transformPointer();
    g2.drawImage(img_pointer.getImage(), 0, 0, 16, 32, img_pointer.getImageObserver());
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

  /**
   * This method can adjust an images alpha-values (transparancy)
   * @param alpha float
   * @return AlphaComposite
   */
  private AlphaComposite makeComposite(float alpha)
  {
    int type = AlphaComposite.SRC_OVER;
    return (AlphaComposite.getInstance(type, alpha));
  }

}
