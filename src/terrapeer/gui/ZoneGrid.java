package terrapeer.gui;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import terrapeer.*;
import terrapeer.vui.zone.*;
import java.io.*;
import java.awt.event.*;
import javax.vecmath.*;

/**
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P 3D System</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */

public class ZoneGrid extends JPanel
{
  private static TerraPeerLog myLog = TerraPeerLog.getLogger();

  private Dimension d = null;
  private int gridspace_pixel = vars.ZONEGRID_GRIDSPACE_PIXEL;
  private int xcount = 0;
  private int ycount = 0;
  private int curr_xcoord = vars.ZONEGRID_START_XCOORD;
  private int curr_ycoord = vars.ZONEGRID_START_YCOORD;
  private int coord_step = vars.ZONEGRID_COORD_STEP;
  private int label_step = vars.ZONEGRID_LABEL_STEP;

  private Color cl_bg = Color.white;
  private Color cl_gridlines = Color.lightGray;
  private Color cl_labels = Color.blue;
  private Color cl_label_zone = Color.black;
  private Color cl_zone = Color.gray;
  private Color cl_zone_sel = Color.orange;
  private Color cl_zone_click = Color.red;
  private Color cl_myzone = Color.green;
  private Color cl_myzone_sel = Color.orange;
  private Color cl_myzone_click = Color.white;

  //private int zone_count = 0;
  private Vector zones = null;
  private ZoneWorld myZoneWorld = null;
  private Zone myZone = null;

  private JLabel ZoneGridMouseCoordsLabel = null;
  private int lastMovedOverZone = -1;
  private int thisMovedOverZone = -1;
  private boolean isMouseOverZone = false;

  private boolean isCyberspaceRunning = false;


  public ZoneGrid(JLabel MouseCoords)
  {
    this.ZoneGridMouseCoordsLabel = MouseCoords;
  }

  public void initZoneGrid(ZoneWorld myZoneWorld, Zone myZone, boolean isCyberspaceRunning)
  {
    this.addMouseListener(new ZoneGrid_this_mouseAdapter(this));
    this.addMouseMotionListener(new ZoneGrid_this_mouseMotionAdapter(this));
    updateZoneGrid(myZoneWorld, myZone, isCyberspaceRunning);
  }

  public void updateZoneGrid(ZoneWorld myZoneWorld, Zone myZone, boolean isCyberspaceRunning)
  {
    this.myZoneWorld = myZoneWorld;
    this.zones = myZoneWorld.getZones();
    this.myZone = myZone;
    //this.zone_count = myZoneWorld.getZoneCount();
    this.isCyberspaceRunning = isCyberspaceRunning;

    myLog.addMessage(4, "Zone count in ZoneCoord viewer: "+getZoneCount());
  }

  public int getZoneCount()
  {
    return myZoneWorld.getZoneCount();//zone_count;
  }

  public void paintComponent(Graphics g)
  {
    //super.paintComponent(g);

    d = this.getSize();
    xcount = d.width / gridspace_pixel;
    ycount = d.height / gridspace_pixel;

    //draw bg
    g.setColor(cl_bg);
    g.fillRect(0, 0, d.width, d.height);

    //draw Grid
    drawGrid(g);

    //check zone in range

    if(isCyberspaceRunning)
    {
      //draw Zones
      drawMyZone(g);
      drawZones(g);
    }
  }

  private void drawGrid(Graphics g)
  {
    g.setFont(new Font("Arial",0,8));
    int step = label_step;

    for(int x=0; x<xcount; x++) //vertical
    {
      //draw origo-line
      if(x+curr_xcoord == 0) g.setColor(cl_labels); else g.setColor(cl_gridlines);

      g.drawLine(x * gridspace_pixel, 0, x * gridspace_pixel, d.height);
      if (step == 0)
      {
        g.setColor(cl_labels);
        g.drawString("" + (x+curr_xcoord), x * gridspace_pixel, 10);
        step = label_step;
        g.setColor(cl_gridlines);
      }
      step--;
    }
    step = label_step;
    for (int y = 0; y < ycount; y++) //horizontal
    {
      //draw origo-line
      if(y+curr_ycoord == 0) g.setColor(cl_labels); else g.setColor(cl_gridlines);

      g.drawLine(0, y * gridspace_pixel, d.width, y * gridspace_pixel);
      if (step == 0)
      {
        g.setColor(cl_labels);
        g.drawString("" + (y+curr_ycoord), 5, y * gridspace_pixel);
        step = label_step;
        g.setColor(cl_gridlines);
      }
      step--;
    }
  }

  private void drawZoneString(Graphics g, int i)
  {
    g.setColor(cl_label_zone);
    g.setFont(new Font("Arial", 0, 10));
    g.drawString("Zone ID: " + ((Zone)zones.get(i)).getZone_ID() + " (#" + i + ")",
                 (((Zone)zones.get(i)).myGeometry.getTwoPoint_X1() - curr_xcoord) * gridspace_pixel + 5,
                 (((Zone)zones.get(i)).myGeometry.getTwoPoint_Y1() - curr_ycoord) * gridspace_pixel + 15);

    g.drawString("Name: " + ((Zone)zones.get(i)).getZone_Name(),
                 (((Zone)zones.get(i)).myGeometry.getTwoPoint_X1() - curr_xcoord) * gridspace_pixel + 5,
                 (((Zone)zones.get(i)).myGeometry.getTwoPoint_Y1() - curr_ycoord) * gridspace_pixel + 25);

    g.drawString("Coord: [" + ((Zone)zones.get(i)).myGeometry.getTwoPoint_X1() + ","
                 + ((Zone)zones.get(i)).myGeometry.getTwoPoint_Y1() + " - "
                 + ((Zone)zones.get(i)).myGeometry.getTwoPoint_X2() + ","
                 + ((Zone)zones.get(i)).myGeometry.getTwoPoint_Y2() + "]",
                 (((Zone)zones.get(i)).myGeometry.getTwoPoint_X1() - curr_xcoord) * gridspace_pixel + 5,
                 (((Zone)zones.get(i)).myGeometry.getTwoPoint_Y1() - curr_ycoord) * gridspace_pixel + 35);
  }

  private void drawMyZone(Graphics g)
  {
    if (myZone != null)
    if (myZone.isMouseOver() && !myZone.isSelected())
    {
      //zone is mouse-over
      g.setColor(cl_myzone_sel);
      drawZone(g, myZone.myGeometry.getTwoPoint_X1(),
               myZone.myGeometry.getTwoPoint_Y1(),
               myZone.myGeometry.getTwoPoint_X2(),
               myZone.myGeometry.getTwoPoint_Y2());
      g.setColor(cl_label_zone);
      g.setFont(new Font("Arial", 0, 10));
      g.drawString("Zone ID: " + myZone.getZone_ID(),
                   (myZone.myGeometry.getTwoPoint_X1() - curr_xcoord) * gridspace_pixel + 5,
                   (myZone.myGeometry.getTwoPoint_Y1() - curr_ycoord) * gridspace_pixel + 15);

      g.drawString("Name: " + myZone.getZone_Name(),
                   (myZone.myGeometry.getTwoPoint_X1() - curr_xcoord) * gridspace_pixel + 5,
                   (myZone.myGeometry.getTwoPoint_Y1() - curr_ycoord) * gridspace_pixel + 25);

      g.drawString("Coord: [" + myZone.myGeometry.getTwoPoint_X1() + ","
                   + myZone.myGeometry.getTwoPoint_Y1() + " - "
                   + myZone.myGeometry.getTwoPoint_X2() + ","
                   + myZone.myGeometry.getTwoPoint_Y2() + "]",
                   (myZone.myGeometry.getTwoPoint_X1() - curr_xcoord) * gridspace_pixel + 5,
                   (myZone.myGeometry.getTwoPoint_Y1() - curr_ycoord) * gridspace_pixel + 35);
    }
    else if (myZone.isSelected())
    {
      //zone is selected
      g.setColor(cl_myzone_click);
      drawZone(g, myZone.myGeometry.getTwoPoint_X1(),
               myZone.myGeometry.getTwoPoint_Y1(),
               myZone.myGeometry.getTwoPoint_X2(),
               myZone.myGeometry.getTwoPoint_Y2());
      g.setColor(cl_label_zone);
      g.setFont(new Font("Arial", 0, 10));
      g.drawString("Zone ID: " + myZone.getZone_ID(),
                   (myZone.myGeometry.getTwoPoint_X1() - curr_xcoord) * gridspace_pixel + 5,
                   (myZone.myGeometry.getTwoPoint_Y1() - curr_ycoord) * gridspace_pixel + 15);

      g.drawString("Name: " + myZone.getZone_Name(),
                   (myZone.myGeometry.getTwoPoint_X1() - curr_xcoord) * gridspace_pixel + 5,
                   (myZone.myGeometry.getTwoPoint_Y1() - curr_ycoord) * gridspace_pixel + 25);

      g.drawString("Coord: [" + myZone.myGeometry.getTwoPoint_X1() + ","
                   + myZone.myGeometry.getTwoPoint_Y1() + " - "
                   + myZone.myGeometry.getTwoPoint_X2() + ","
                   + myZone.myGeometry.getTwoPoint_Y2() + "]",
                   (myZone.myGeometry.getTwoPoint_X1() - curr_xcoord) * gridspace_pixel + 5,
                   (myZone.myGeometry.getTwoPoint_Y1() - curr_ycoord) * gridspace_pixel + 35);
    }
    else
    {
      g.setColor(cl_myzone);
      drawZone(g, myZone.myGeometry.getTwoPoint_X1(),
               myZone.myGeometry.getTwoPoint_Y1(),
               myZone.myGeometry.getTwoPoint_X2(),
               myZone.myGeometry.getTwoPoint_Y2());
    }
  }

  private void drawZones(Graphics g)
  {
    for(int i=0; i<getZoneCount(); i++)
    {
      if( !((Zone)zones.get(i)).isFiltered1() && !((Zone)zones.get(i)).isFiltered2() )
      {
        if (((Zone)zones.get(i)).isMouseOver() && !((Zone)zones.get(i)).isSelected())
        {
          //zone is mouse-over
          g.setColor(cl_zone_sel);
          drawZone(g, ((Zone)zones.get(i)).myGeometry.getTwoPoint_X1(),
                   ((Zone)zones.get(i)).myGeometry.getTwoPoint_Y1(),
                   ((Zone)zones.get(i)).myGeometry.getTwoPoint_X2(),
                   ((Zone)zones.get(i)).myGeometry.getTwoPoint_Y2());
          drawZoneString(g, i);
        }
        else if (((Zone)zones.get(i)).isSelected())
        {
          //zone is selected
          g.setColor(cl_zone_click);
          drawZone(g, ((Zone)zones.get(i)).myGeometry.getTwoPoint_X1(),
                   ((Zone)zones.get(i)).myGeometry.getTwoPoint_Y1(),
                   ((Zone)zones.get(i)).myGeometry.getTwoPoint_X2(),
                   ((Zone)zones.get(i)).myGeometry.getTwoPoint_Y2());
          drawZoneString(g, i);
        }
        else
        {
          g.setColor(cl_zone);
          drawZone(g, ((Zone)zones.get(i)).myGeometry.getTwoPoint_X1(),
                   ((Zone)zones.get(i)).myGeometry.getTwoPoint_Y1(),
                   ((Zone)zones.get(i)).myGeometry.getTwoPoint_X2(),
                   ((Zone)zones.get(i)).myGeometry.getTwoPoint_Y2());
        }
      }

    }
  }

  private void drawZone(Graphics g, int x1, int y1, int x2, int y2)
  {
    //System.out.println("drawZone: x1="+x1+"  x2="+x2+"  y1="+y1+"  y2="+y2);

    if(x2<curr_xcoord || y2<curr_ycoord ||
       x1*gridspace_pixel>d.width || y1*gridspace_pixel>d.height)
    {
      //zone outside display
    }
    else
    {
      int rx1 = (x1 - curr_xcoord) * gridspace_pixel;
      int ry1 = (y1 - curr_ycoord) * gridspace_pixel;
      int rw = (x2 - x1) * gridspace_pixel;
      int rh = (y2 - y1) * gridspace_pixel;

      if (rx1 < 10) //left boundary
      {
        rx1 = 10;
        rw = (x2 - curr_xcoord) * gridspace_pixel;
      }
      if (ry1 < 10) //upper boundary
      {
        ry1 = 10;
        rh = (y2 - curr_ycoord) * gridspace_pixel;
      }
      if (rx1 + rw > d.width) //right boundary
        rw = d.width - rx1;
      if (ry1 + rh > d.height) //lower boundary
        rh = d.height - ry1;

      g.drawRect(rx1 + 1, ry1 + 1, rw - 2, rh - 2);
      g.fillRect(rx1 + 4, ry1 + 4, rw - 8, rh - 8);
      //System.out.println("z: rx1="+rx1+"  ry1="+ry1+"  rw="+rw+"  rh="+rh);
    }
  }

  void this_mouseMoved(MouseEvent e)
  {
    int mouseXCoord = e.getX()/gridspace_pixel + curr_xcoord;
    int mouseYCoord = e.getY()/gridspace_pixel + curr_ycoord;

    //check if mouse over other Zones
    for(int i=0; i<getZoneCount(); i++)
    {
      ((Zone)zones.get(i)).setMouseOver(false);

      if (mouseXCoord > ((Zone)zones.get(i)).myGeometry.getTwoPoint_X1() &&
          mouseXCoord < ((Zone)zones.get(i)).myGeometry.getTwoPoint_X2() &&
          mouseYCoord > ((Zone)zones.get(i)).myGeometry.getTwoPoint_Y1() &&
          mouseYCoord < ((Zone)zones.get(i)).myGeometry.getTwoPoint_Y2())
      {
        ((Zone)zones.get(i)).setMouseOver(true);
        thisMovedOverZone = i;
        break;
      }
    }

    //check if mouse over my Zone
    myZone.setMouseOver(false);
    if (mouseXCoord > myZone.myGeometry.getTwoPoint_X1() &&
        mouseXCoord < myZone.myGeometry.getTwoPoint_X2() &&
        mouseYCoord > myZone.myGeometry.getTwoPoint_Y1() &&
        mouseYCoord < myZone.myGeometry.getTwoPoint_Y2())
    {
      myZone.setMouseOver(true);
      thisMovedOverZone = -2;
    }

    //System.out.println("x:"+e.getX()+"  y:"+e.getY());
    //repaint();
    //System.err.print(" Last:"+lastMovedOverZone+" ");

    isMouseOverZone = false;
    for (int i = 0; i < getZoneCount(); i++)
      if(((Zone)zones.get(i)).isMouseOver())
        isMouseOverZone = true;

    if(myZone.isMouseOver())
      isMouseOverZone = true;

    if (thisMovedOverZone != lastMovedOverZone)
      this.paintComponent(this.getGraphics());

    if (!isMouseOverZone)
      lastMovedOverZone = -1;

    lastMovedOverZone = thisMovedOverZone;

    ZoneGridMouseCoordsLabel.setText(mouseXCoord+","+mouseYCoord);
  }

  void this_mouseClicked(MouseEvent e)
  {
    int mouseXCoord = e.getX()/gridspace_pixel + curr_xcoord;
    int mouseYCoord = e.getY()/gridspace_pixel + curr_ycoord;

    myZone.setSelected(false);
    myZoneWorld.isCurrSelectedZone = false;
    myZoneWorld.currSelectedZone = null;

    //check if mouse clicked on other Zone
    for(int i=0; i<getZoneCount(); i++)
    {
      ((Zone)zones.get(i)).setSelected(false);

      if (mouseXCoord > ((Zone)zones.get(i)).myGeometry.getTwoPoint_X1() &&
          mouseXCoord < ((Zone)zones.get(i)).myGeometry.getTwoPoint_X2() &&
          mouseYCoord > ((Zone)zones.get(i)).myGeometry.getTwoPoint_Y1() &&
          mouseYCoord < ((Zone)zones.get(i)).myGeometry.getTwoPoint_Y2())
      {
        ((Zone)zones.get(i)).setSelected(true);
        myZoneWorld.isCurrSelectedZone = true;
        myZoneWorld.currSelectedZone = (Zone)zones.get(i);
        break;
      }
    }

    //check if mouse clicked on my Zone
    if (mouseXCoord > myZone.myGeometry.getTwoPoint_X1() &&
        mouseXCoord < myZone.myGeometry.getTwoPoint_X2() &&
        mouseYCoord > myZone.myGeometry.getTwoPoint_Y1() &&
        mouseYCoord < myZone.myGeometry.getTwoPoint_Y2())
    {
      myZone.setSelected(true);
      myZoneWorld.isCurrSelectedZone = true;
      myZoneWorld.currSelectedZone = myZone;
    }

    //move grid
    if(!myZoneWorld.isCurrSelectedZone)
      //moveGrid(mouseXCoord - (xcount / 2), mouseYCoord - (xcount / 2));
      moveGrid(mouseXCoord, mouseYCoord);

    this.paintComponent(this.getGraphics());
    /**
    for(int i=0; i<zone_count; i++)
      if(isZoneClick[i])
      {
        this.paintComponent(this.getGraphics());
        break;
      } **/
  }

  public void moveGrid(int x, int y)
  {
    curr_xcoord = x - (xcount / 2);
    curr_ycoord = y - (ycount / 2);
  }

  public void moveGridHome()
  {
    moveGrid(0, 0);
  }

  public void moveGridOrigo()
  {
    moveGrid(0, 0);
  }

  public void moveGrid(int controlType)
  {
    switch(controlType)
    {
      case vars.NAV_UP:
        curr_ycoord -= coord_step;
        break;
      case vars.NAV_DOWN:
        curr_ycoord += coord_step;
        break;
      case vars.NAV_RIGHT:
        curr_xcoord += coord_step;
        break;
      case vars.NAV_LEFT:
        curr_xcoord -= coord_step;
        break;
      default:
        break;
    }
    //this.repaint();
    this.paintComponent(this.getGraphics());
  }

  public int getCurrZoneCoordsX()
  {
    return curr_xcoord;
  }

  public int getCurrZoneCoordsY()
  {
    return curr_ycoord;
  }
  public void setCoord_step(int coord_step)
  {
    this.coord_step = coord_step;
  }
  public void setGridspace_pixel(int gridspace_pixel)
  {
    this.gridspace_pixel = gridspace_pixel;
    //d.width / gridspace_pixel;
    //curr_xcoord = curr_xcoord - (xcount / 2);
    //curr_ycoord = curr_xcoord - (ycount / 2);
    //moveGrid(curr_xcoord, curr_ycoord);
    moveGridOrigo();
  }


}

class ZoneGrid_this_mouseMotionAdapter extends java.awt.event.MouseMotionAdapter
{
  ZoneGrid adaptee;

  ZoneGrid_this_mouseMotionAdapter(ZoneGrid adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseMoved(MouseEvent e)
  {
    adaptee.this_mouseMoved(e);
  }
}

class ZoneGrid_this_mouseAdapter extends java.awt.event.MouseAdapter
{
  ZoneGrid adaptee;

  ZoneGrid_this_mouseAdapter(ZoneGrid adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e)
  {
    adaptee.this_mouseClicked(e);
  }
}
