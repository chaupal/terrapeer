package terrapeer.gui;

import java.awt.*;
import javax.swing.*;
import terrapeer.*;
import java.io.*;
import java.awt.geom.AffineTransform;
import java.awt.Graphics2D;
import java.awt.event.*;

import terrapeer.vui.space.*;

/**
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */

public class NavControlDir extends JPanel
{
  Graphics2D g2 = null;
  double currDegree = 0.0;
  ImageIcon img_wheel = null;
  ImageIcon img_wheel2 = null;
  boolean firsttime = true;
  private int degree = 0;
  private double d = Math.PI / 2;
  private SpaceNav spaceNav = null;

  private int component_x_size = 60;
  private int component_y_size = 60;

  private int component_x_offset = 15;
  private int component_y_offset = 0;

  private boolean mouseClick = false;
  private int mouseX = 0;
  private int mouseY = 0;

  public NavControlDir()
  {
    this.setPreferredSize(new Dimension(component_x_size, component_y_size));
  }

  public void initControl()
  {
    img_wheel = new ImageIcon(terrapeer.gui.NavControlDir.class.getResource(vars.IMG_PATH + File.separatorChar
        + vars.IMG_FILE[57]));
    img_wheel2 = new ImageIcon(terrapeer.gui.NavControlDir.class.getResource(vars.IMG_PATH + File.separatorChar
        + vars.IMG_FILE[58]));
  }

  public void initControl2(SpaceNav spaceNav)
  {
    this.spaceNav = spaceNav;
    //??below just added
    this.addMouseMotionListener(new NavControlDir_this_mouseMotionAdapter(this));
    this.addMouseListener(new NavControlDir_this_mouseAdapter(this));
  }

  private void doFirstTime()
  {
    drawImage1();
  }

  private void drawImage1()
  {
    //draw wheel
    g2.drawImage(img_wheel.getImage(), component_x_offset, component_y_offset, component_x_size, component_y_size, img_wheel.getImageObserver());
  }

  private void drawImage2()
  {
    //draw wheel
    g2.drawImage(img_wheel2.getImage(), component_x_offset, component_y_offset, component_x_size, component_y_size, img_wheel.getImageObserver());
  }

  private void drawDirLine()
  {
    g2.setColor(Color.black);
    g2.setFont(new Font("Arial", 0, 8));
    //g2.drawString(degree+"", (component_x_size-component_x_offset)/2-20, component_y_offset );
    g2.drawLine( component_x_offset+component_x_size/2,
                 component_y_offset+component_y_size/2,
                 component_x_offset+component_x_size/2+(int)(15*Math.cos(d)),
                 component_y_offset+component_y_size/2+(int)(-15*Math.sin(d)) );
  }

  private void drawDirLineMouse()
  {
    g2.setColor(Color.white);
    g2.drawLine( component_x_offset+component_x_size/2,
                 component_y_offset+component_y_size/2,
                 mouseX,
                 mouseY );
  }

  private void moveView()
  {
    try
    {
      spaceNav.dirAvatarDegree(degree);
    }
    catch(Exception e)
    {
      //ERR
    }
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
    else
    {
      if(mouseClick)
      {
        drawImage2();
        drawDirLineMouse();
      }
      else
      {
        drawImage1();
        drawDirLine();
      }
      moveView();
    }
  }

  private void jbInit() throws Exception
  {
    this.setBackground(Color.white);
    this.addMouseMotionListener(new NavControlDir_this_mouseMotionAdapter(this));
    this.addMouseListener(new NavControlDir_this_mouseAdapter(this));
  }

  void this_mouseClicked(MouseEvent e)
  {
  }

  void this_mousePressed(MouseEvent e)
  {
    mouseX = e.getX();
    mouseY = e.getY();

    //check whether mouse hits inside image
    if( (mouseX>= component_x_offset && mouseX <= component_x_offset+component_x_size) &&
        (mouseY>= component_y_offset && mouseY <= component_y_offset+component_y_size) )
    {
      setHeading();

      mouseClick = true;
      this.repaint();
    }
  }

  private void setHeading()
  {
    //the radian we're looking for
    d = 0.0;
    //convert mouse-coords to internal coords with 0,0 in the center
    int imgX = mouseX - component_x_size / 2 - component_x_offset;
    int imgY = component_y_size / 2 - mouseY - component_y_offset;

    //if we hit the y-axis, just set d to pi/2
    //if (imgX != 0)
    //{
      //convert coords to scale between 0 and +/- 1
      double adjX = imgX * 100 / (component_x_size / 2);
      double adjY = imgY * 100 / (component_y_size / 2);
      //calculate d
      d = Math.atan2(adjY, adjX);
    //}
    //else
    //  d = Math.PI / 2;

    //see if mouse hits left of y-axis
    //if (imgX < 0)
    //  d += Math.PI;

    //convert radian to degree
    degree = (int)Math.toDegrees(d);

    //adjust degrees to clockwise dial
    if(degree<0)
      degree = 360 + degree;

    if(degree>90)
      degree -= 90;
    else
      degree = 270 + degree;

    //debug info
    //System.out.println("mx=" + mouseX + " my=" + mouseX +
    //                   " rx=" + imgX + " ry=" + imgY +
    //System.out.println("deg = " + degree + " (" + d + ")" );
  }

  void this_mouseReleased(MouseEvent e)
  {
    mouseClick = false;
    this.repaint();
  }

  void this_mouseDragged(MouseEvent e)
  {
    mouseX = e.getX();
    mouseY = e.getY();

    //check whether mouse hits inside image
    if( (mouseX>= component_x_offset && mouseX <= component_x_offset+component_x_size) &&
        (mouseY>= component_y_offset && mouseY <= component_y_offset+component_y_size) )
    {
      setHeading();

      this.repaint();
    }
  }

}

class NavControlDir_this_mouseAdapter extends java.awt.event.MouseAdapter
{
  NavControlDir adaptee;

  NavControlDir_this_mouseAdapter(NavControlDir adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.this_mouseClicked(e);
  }
  public void mousePressed(MouseEvent e)
  {
    adaptee.this_mousePressed(e);
  }
  public void mouseReleased(MouseEvent e)
  {
    adaptee.this_mouseReleased(e);
  }
}

class NavControlDir_this_mouseMotionAdapter extends java.awt.event.MouseMotionAdapter
{
  NavControlDir adaptee;

  NavControlDir_this_mouseMotionAdapter(NavControlDir adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseDragged(MouseEvent e)
  {
    adaptee.this_mouseDragged(e);
  }
}
