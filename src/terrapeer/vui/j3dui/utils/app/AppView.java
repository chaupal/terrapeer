package terrapeer.vui.j3dui.utils.app;

import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.*;
import terrapeer.vui.j3dui.visualize.change.*;

/**
 This class represents an application's "view space" and the
 internal geometry associated with the view.  One or more AppView
 objects can be added to an AppWorld's scenegraph in order to
 provide a views of its virtual world.
 <P>
 All nodes in the view space are added under the "root"
 BranchGroup.  This provides a single generally accessible
 location for the contents of the view.  It also separates the
 contents of the view space from the internal nodes needed to
 support the view.  All nodes should be added to this view using
 addNode(), which assures that all nodes are under the root.
 <P>
 The world and view spaces are assumed to be in meters.  The
 display space plane (image plate) is assumed to be at a distance
 of 1.0 meters from the eye.  The screen size is obtained from
 the system toolkit and, unlike the Java 3D version, can not be
 altered.
 <P>
 As of Java 3D 1.1.2 there appears to be no reliable way to
 specify the display-view offset (DVO).  Setting the eye position
 in the image plate only works for a window eye policy of
 RELATIVE_TO_SCREEN, but then offset is also a function of the
 window position on the screen.  Also, because of rather complex
 coupling amongst the various eyepoint policy modes, explicit and
 consistent control over view and display scale factors (VSF,
 DSF) is difficult (and maybe impossible).
 */
public class AppView extends BranchGroup
{

  // public interface =========================================

  /**
    Constructs a default AppView with a default AppDisplay.  Use
    getDisplay() to retrieve the default display.
    @see AppView#getDisplay
   */
  public AppView()
  {
    //this(new AppDisplay());
    initAppView(new AppDisplay(initConf()));
  }

  private GraphicsConfiguration initConf()
  {
    GraphicsEnvironment theEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment( );
    GraphicsConfiguration[] configs = theEnvironment.getDefaultScreenDevice().getConfigurations();
    GraphicsConfigTemplate3D configTemplate = new GraphicsConfigTemplate3D();
    //Canvas3D canvas3D = new Canvas3D( configTemplate.getBestConfiguration( configs ) );
    return configTemplate.getBestConfiguration(configs);
  }

  public ViewPlatform getViewPlatform()
  {
    return _platform;
  }

  public void initAppView(AppDisplay display)
  {
    if (display == null)
      throw new
          IllegalArgumentException("<display> is null.");

    _display = display;

    // setup view root
    _root = new BranchGroup();
    addChild(_root);

    // build and add view
    _view = new View();

    _view.addCanvas3D(_display);

    PhysicalBody body = new PhysicalBody();
    _view.setPhysicalBody(body);

    PhysicalEnvironment environ = new PhysicalEnvironment();
    _view.setPhysicalEnvironment(environ);

    _platform = new ViewPlatform();
    _view.attachViewPlatform(_platform);
    addChild(_platform);

    // monitor view internal changes
    _display.addComponentListener(new ComponentAdapter()
    {
      public void componentResized(ComponentEvent evt)
      {
        ChangeSensor.processAllChanges(AppView.this);
      }
    });

    // configure view internally
    _viewFov = _view.getFieldOfView();

    /// set Z clip for display overlay
    setViewClipDist(new Vector2d(.05, 10));
  }

  /**
    Constructs an AppView with the specified AppDisplay to show
    it.  In order to see the display it must be added to an
    AppFrame.
    @param display AppDisplay showing the view.  Never null.
   */
  public AppView(AppDisplay display)
  {
    if (display == null)
      throw new
          IllegalArgumentException("<display> is null.");

    _display = display;

    // setup view root
    _root = new BranchGroup();
    addChild(_root);

    // build and add view
    _view = new View();

    _view.addCanvas3D(_display);

    PhysicalBody body = new PhysicalBody();
    _view.setPhysicalBody(body);

    PhysicalEnvironment environ = new PhysicalEnvironment();
    _view.setPhysicalEnvironment(environ);

    _platform = new ViewPlatform();
    _view.attachViewPlatform(_platform);
    addChild(_platform);

    // monitor view internal changes
    _display.addComponentListener(new ComponentAdapter()
    {
      public void componentResized(ComponentEvent evt)
      {
        ChangeSensor.processAllChanges(AppView.this);
      }
    });

    // configure view internally
    _viewFov = _view.getFieldOfView();

    /// set Z clip for display overlay
    setViewClipDist(new Vector2d(.05, 10));

    if (Debug.getEnabled())
    {
      Vector2d vec = new Vector2d();
      Debug.println("AppView.verbose",
                    "INIT:AppView:" +
                    " screenSize=" + _display.getScreenSize(vec) +
                    " screenRes=" + _display.getScreenResolution(vec) +
                    " displaySize=" + _display.getDisplaySize(vec) +
                    " viewFov=" + getViewFov());
    }
  }

  /**
    Gets the view's display.
    @return The view's display.
   */
  public AppDisplay getDisplay()
  {
    return _display;
  }

  /**
    Gets the view's Java 3D view.
    @return The view's Java 3D view.
   */
  public View getView()
  {
    return _view;
  }

  /**
    Gets the view space scenegraph root.
    @return The view's root.
   */
  public BranchGroup getRoot()
  {
    return _root;
  }

  /**
    Adds a node to the view root.
    @param node Node to be added.
   */
  public void addNode(Node node)
  {
    _root.addChild(node);
  }

  /**
    Removes all nodes under the view root.  The view display,
    world
    geometry, and internal components are unaffected.
   */
  public void clearView()
  {
    // children shift when removed so start at end
    for (int childI = _root.numChildren() - 1;
         childI >= 0; childI--)
    {
      _root.removeChild(childI);
    }
  }

  /**
    Gets the view scaling factor (i.e. view to display space).
    The display screen is assumed to be at a distance of 1.0
    from the eye position.
    @return View scaling factor.
   */
  public double getViewScale()
  {
    // not cleanly supported in Java 3D 1.1.2,
    // assume scale tracks display size (view model default)

    double viewSizeM;
    double fov = getViewFov();

    if (fov == 0.0)
    {
      // parallel projection
      /// assume unit extent view at display in meters
      viewSizeM = 2.0;
    }
    else
    {
      // perspective projection
      /// compute view horizontal size at display in meters
      viewSizeM = 2.0 * Math.tan(fov / 2.0);
    }

    // convert view size into display pixels
    _display.getScreenResolution(_vector);
    double viewSizeP = _vector.x * viewSizeM;

    // get display size in pixels
    _display.getDisplaySize(_vector);
    double dispSizeP = _vector.x;

    // compute view to display scale
    return dispSizeP / viewSizeP;
  }

  /**
    Gets the display scaling factor (i.e. display to window
    space).
    @return Display scaling factor.
   */
  public double getDisplayScale()
  {
    // not cleanly supported in Java 3D 1.1.2,
    // assume always one
    return 1.0;
  }

  /**
    Gets the display space to view space origin offset in pixels.
    @param offset Return display-view offset.
    @return Reference to the screen size.
   */
  public Tuple2d getDisplayViewOffset(Tuple2d offset)
  {
    // not cleanly supported in Java 3D 1.1.2,
    // assume always zero
    offset.set(0, 0);
    return offset;
  }

  /**
    Sets the view horizontal field of view in radians.  If zero
    the projection is parallel.
    @param fov The view FOV.
    @return The view FOV.
   */
  public double setViewFov(double fov)
  {
    _viewFov = fov;

    if (_viewFov == 0.0)
    {
      _view.setProjectionPolicy(
          View.PARALLEL_PROJECTION);
    }
    else
    {
      _view.setProjectionPolicy(
          View.PERSPECTIVE_PROJECTION);
      _view.setFieldOfView(fov);
    }

    ChangeSensor.processAllChanges(this);
    return _viewFov;
  }

  /**
    Gets the view horizontal field of view in radians.  If zero
    the projection is parallel.
    @return The view FOV.
   */
  public double getViewFov()
  {
    return _viewFov;
  }

  /**
    Sets the view clip plane distances.
    @param dist Near and far clip plane distance (near, far).
    @return The view FOV.
   */
  public Tuple2d setViewClipDist(Tuple2d dist)
  {
    _view.setFrontClipDistance(dist.x);
    _view.setBackClipDistance(dist.y);
    return dist;
  }

  /**
    Gets the view near and far clip plane distances.
    @return The clip plane distances (near. far).
   */
  public Tuple2d getViewClipDist(Tuple2d dist)
  {
    dist.set(_view.getFrontClipDistance(),
             _view.getBackClipDistance());
    return dist;
  }

  // personal body ============================================

  /** Root node for the view space contents. */
  private BranchGroup _root;

  /** View display. */
  private AppDisplay _display;

  /** Internal geometric platform for the view. */
  private ViewPlatform _platform;

  /** Internal container for the view display canvas. */
  private View _view;

  /** View horizontal FOV in radians.  Parallel if zero */
  private double _viewFov;

  /** Dummy vector. (for GC) */
  private Vector2d _vector = new Vector2d();

}
