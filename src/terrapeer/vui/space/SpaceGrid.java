package terrapeer.vui.space;

/**
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P feedback system</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */

import javax.media.j3d.BranchGroup;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.geometry.*;

import terrapeer.*;
import terrapeer.vui.j3dui.utils.objects.*;

/**
 * The default Space Grid
 *
 * Always visible
 * Dynamic Grid detail
 *
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P feedback system</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */
public class SpaceGrid extends BranchGroup
{
  private int gridCount = 0;
  private double gridStep = 0.0;
  private float axisLength = 0;
  private float gridLength = 0;

  public SpaceGrid()
  {
    this(vars.GRID_DEFAULT_GRIDCOUNT, vars.GRID_DEFAULT_AXISLEN, vars.GRID_DEFAULT_GRIDLEN);
  }

  public SpaceGrid(int gridCount, float axisLength, float gridLength)
  {
    this.gridCount = gridCount;
    this.axisLength = axisLength;
    this.gridLength = gridLength;

    updateGrid(vars.GRID_DEFAULT_GRIDSTEP);
  }

  public void updateGrid(double gridstep)
  {
    this.gridStep = gridstep;

    //Point3d p = new Point3d(0,0,0);

    // construct object to represent the axis
    LineArray axisXLines = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
    LineArray axisYLines = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
    LineArray axisZLines = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);

    axisXLines.setCoordinate(0, new Point3f(-axisLength, 0.0f, 0.0f));
    axisXLines.setCoordinate(1, new Point3f( axisLength, 0.0f, 0.0f));
    axisYLines.setCoordinate(0, new Point3f( 0.0f,-axisLength, 0.0f));
    axisYLines.setCoordinate(1, new Point3f( 0.0f, axisLength, 0.0f));
    axisZLines.setCoordinate(0, new Point3f( 0.0f, 0.0f, -axisLength));
    axisZLines.setCoordinate(1, new Point3f( 0.0f, 0.0f, axisLength));

    axisXLines.setColor(0, vars.GRID_X_COLOR);
    axisXLines.setColor(1, vars.GRID_Y_COLOR);
    axisYLines.setColor(0, vars.GRID_X_COLOR);
    axisYLines.setColor(1, vars.GRID_Y_COLOR);
    axisZLines.setColor(0, vars.GRID_X_COLOR);
    axisZLines.setColor(1, vars.GRID_Y_COLOR);

    //BranchGroup axisBG = new BranchGroup();
    addChild(new Shape3D(axisXLines));
    addChild(new Shape3D(axisYLines));
    addChild(new Shape3D(axisZLines));

    float xShift = 0.0f;
    float zShift = 0.0f;

    LineArray[] gridXLines = new LineArray[gridCount];
    LineArray[] gridZLines = new LineArray[gridCount];

    for(int i=-(gridCount/2); i<(gridCount/2); i+=gridStep)
    {
      //p.set(x, y, z);
      xShift = i;
      zShift = i;

      gridXLines[i+(gridCount/2)] = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
      gridXLines[i+(gridCount/2)].setCoordinate(0, new Point3f(-gridLength, 0.0f, zShift));
      gridXLines[i+(gridCount/2)].setCoordinate(1, new Point3f( gridLength, 0.0f, zShift));
      gridXLines[i+(gridCount/2)].setColor(0, vars.COLOR3F_GRAY);
      gridXLines[i+(gridCount/2)].setColor(1, vars.COLOR3F_RED);
      addChild(new Shape3D(gridXLines[i+(gridCount/2)]));

      gridZLines[i+(gridCount/2)] = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
      gridZLines[i+(gridCount/2)].setCoordinate(0, new Point3f( xShift, 0.0f,-gridLength));
      gridZLines[i+(gridCount/2)].setCoordinate(1, new Point3f( xShift, 0.0f, gridLength));
      gridZLines[i+(gridCount/2)].setColor(0, vars.GRID_Z_COLOR);
      gridZLines[i+(gridCount/2)].setColor(1, vars.GRID_Z_COLOR);
      addChild(new Shape3D(gridZLines[i+(gridCount/2)]));
    }

    setCapability(Node.ENABLE_PICK_REPORTING);
  }

}