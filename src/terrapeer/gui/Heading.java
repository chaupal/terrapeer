package terrapeer.gui;

import java.awt.*;
import java.awt.image.*;
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

public class Heading extends JPanel
{
  Graphics2D g2 = null;
  double currDegree = 0.0;
  ImageIcon img_base = null;
  ImageIcon img_pointer_dn = null;
  ImageIcon img_pointer_up = null;
  boolean firsttime = true;

  public Heading()
  {
    this.setPreferredSize(new Dimension(132, 44));
  }

  public void initHeading()
  {
    //g2 = (Graphics2D) this.getGraphics();
    img_base =  new ImageIcon(terrapeer.gui.Heading.class.getResource(vars.IMG_PATH+File.separatorChar+vars.IMG_FILE[53]));
    img_pointer_dn = new ImageIcon(terrapeer.gui.Heading.class.getResource(vars.IMG_PATH+File.separatorChar+vars.IMG_FILE[51]));
    img_pointer_up = new ImageIcon(terrapeer.gui.Heading.class.getResource(vars.IMG_PATH+File.separatorChar+vars.IMG_FILE[56]));
  }

  private void doFirstTime()
  {
    drawImages();
  }

  public void moveHeading(double degree)
  {
    if(degree>=-180 && degree<=180)
    {
      currDegree = degree;
      //paintComponent(getGraphics());
    }
    else
    {
        //ERR
    }
  }

  private void transformPointer()
  {
    //dx pixel 0...128, goes between degrees -180...0...180
    //64 pixel = 180 degree  =>  64/180 px/d
    //a degree  =>  64/180 * a  px/d * d = px
    double ratio = 0.355556; //64/180;
    double dx = 64 + ratio * currDegree;
    int dy = 0;

    //System.out.println("H currDegree= "+currDegree+"  ratio="+ratio+" dx= "+dx);

    AffineTransform at_pointer1 = new AffineTransform();
    at_pointer1.translate( dx, dy);
    g2.transform(at_pointer1);
  }

  private void drawImages()
  {
    //AlphaComposite a = AlphaComposite.getInstance(AlphaComposite.CLEAR);
    //g.setColor(Color.BLACK);

    //draw heading bg
    //draw base
    BufferedImage bi1 = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
    g2.drawImage(img_base.getImage(), 0, 5, 128, 32, img_base.getImageObserver());

    //draw pointer
    //BufferedImage bi2 = new BufferedImage(14, 11, BufferedImage.TYPE_INT_RGB);
    //a.createContext(bi1.getColorModel(), bi2.getColorModel());
    //AffineTransform at_pointer = new AffineTransform();
    //at_pointer.translate((this.getWidth() - 14) / 2, (this.getHeight() - 11) / 2 - 16);
    //g2.transform(at_pointer);
    transformPointer();
    g2.drawImage(img_pointer_dn.getImage(), 0, 5, 7, 5, img_pointer_up.getImageObserver());
    g2.drawImage(img_pointer_up.getImage(), 0, 32, 7, 5, img_pointer_up.getImageObserver());
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
