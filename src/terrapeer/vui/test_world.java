package terrapeer.vui;

/**
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P feedback system</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */

import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.Scene;
import java.awt.*;
import java.awt.event.*;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.io.*;
import com.sun.j3d.utils.behaviors.vp.*;
import com.sun.j3d.utils.behaviors.sensor.Mouse6DPointerBehavior;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Map;

public class test_world
{
  // root of the branch graph
  private BranchGroup objRoot = null;
  private BoundingSphere bounds = null;
  private boolean spin = false;
  private boolean noTriangulate = false;
  private boolean noStripify = false;
  private double creaseAngle = 60.0;
  private URL filename = null;
  private ConfiguredUniverse confUniv;

  public static void main(String[] args)
  {
    test_world w = new test_world();
  }

  public test_world()
  {
    init();
  }

  public void init()
  {

    // load scene
    if(filename == null)
    {
      try
      {
        filename = new URL("file:classes/terrapeer/objects/obj/beethoven.obj");
      }
      catch (MalformedURLException e)
      {
        System.err.println(e);
        System.exit(1);
      }
    }

    // Get the config file URL from the j3d.configURL property
    URL configURL = ConfiguredUniverse.getConfigURL("file:classes/terrapeer/vui/helpers/j3d1x1-window");

    // Create a simple scene and attach it to the virtual universe
    BranchGroup scene = createSceneGraph();
    confUniv = new ConfiguredUniverse(configURL);

    // Get the ViewingPlatform.
    ViewingPlatform viewingPlatform = confUniv.getViewingPlatform();

    // RELATIVE_TO_COEXISTENCE (default window eyepoint policy), in config file
    //   move ViewPlatform back a bit so the objects in the scene can be viewed
    // NOMINAL_SCREEN (default view attach policy)
    //   sets the view platform origin in the physical world
    //   = center of coexistence
    //   = eye positions expressed relative to coexistence
    //     to see the appropriate FOV (field of view) automatically
    viewingPlatform.setNominalViewingTransform();

    // Add a ViewPlatformBehavior if not specified in the config file.
    if (!spin && viewingPlatform.getViewPlatformBehavior() == null)
    {
      OrbitBehavior orbit = new OrbitBehavior(confUniv.getCanvas(), OrbitBehavior.REVERSE_ALL);
      BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
      orbit.setSchedulingBounds(bounds);
      viewingPlatform.setViewPlatformBehavior(orbit);
    }

    // See if there's a 6 DOF (degree of freedom) mouse in the environment
    Map sensorMap = null;
    sensorMap = confUniv.getNamedSensors();
    if (sensorMap != null)
    {
      Sensor mouse6d = (Sensor) sensorMap.get("mouse6d");
      if (mouse6d != null)
      {
        Mouse6DPointerBehavior behavior = new Mouse6DPointerBehavior(mouse6d, 1.0, true);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        behavior.setSchedulingBounds(bounds);
        scene.addChild(behavior);
        scene.addChild(behavior.getEcho());
      }
    }

    // Listen for a typed "q", "Q", or "Escape" key on each canvas to
    // allow a convenient exit from full screen configurations.
    Canvas3D[] canvases = confUniv.getViewer().getCanvas3Ds();

    class QuitListener extends KeyAdapter
    {
      public void keyTyped(KeyEvent e)
      {
        char c = e.getKeyChar();
        if (c == 'q' || c == 'Q' || c == 27)
          System.exit(0);
      }
    }

    QuitListener quitListener = new QuitListener();
    for (int i = 0; i < canvases.length; i++)
    {
      canvases[i].addKeyListener(quitListener);
      // Make the scenegraph live.
    }
    confUniv.addBranchGraph(scene);
  }

  public BranchGroup createSceneGraph()
  {
    objRoot = new BranchGroup();

    // Create a Transformgroup to scale all objects so they appear in the scene
    TransformGroup objScale = new TransformGroup();
    Transform3D t3d = new Transform3D();
    t3d.setScale(0.7);
    objScale.setTransform(t3d);
    objRoot.addChild(objScale);

    // Create the transform group node and initialize it to the identity.
    // Enable TRANSFORM_WRITE: behavior code can modify it at runtime
    // Add to the root of the subgraph
    TransformGroup objTrans = new TransformGroup();
    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    objScale.addChild(objTrans);

    int flags = ObjectFile.RESIZE;
    if (!noTriangulate)
      flags |= ObjectFile.TRIANGULATE;
    if (!noStripify)
      flags |= ObjectFile.STRIPIFY;

    ObjectFile f = new ObjectFile(flags, (float)(creaseAngle * Math.PI / 180.0));
    Scene s = null;
    try
    {
      s = f.load(filename);
    }
    catch (FileNotFoundException e)
    {
      System.err.println(e);
      System.exit(1);
    }
    catch (ParsingErrorException e)
    {
      System.err.println(e);
      System.exit(1);
    }
    catch (IncorrectFormatException e)
    {
      System.err.println(e);
      System.exit(1);
    }

    objTrans.addChild(s.getSceneGroup());

    bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

    if (spin)
    {
      Transform3D yAxis = new Transform3D();
      Alpha rotationAlpha = new Alpha( -1, Alpha.INCREASING_ENABLE,
                                      0, 0,
                                      4000, 0, 0,
                                      0, 0, 0);

      RotationInterpolator rotator =
          new RotationInterpolator(rotationAlpha, objTrans, yAxis,
                                   0.0f, (float) Math.PI * 2.0f);
      rotator.setSchedulingBounds(bounds);
      objTrans.addChild(rotator);
    }

    // Set up the background
    Color3f bgColor = new Color3f(0.05f, 0.05f, 0.5f);
    Background bgNode = new Background(bgColor);
    bgNode.setApplicationBounds(bounds);
    objRoot.addChild(bgNode);

    // Set up the ambient light
    Color3f ambientColor = new Color3f(0.1f, 0.1f, 0.1f);
    AmbientLight ambientLightNode = new AmbientLight(ambientColor);
    ambientLightNode.setInfluencingBounds(bounds);
    objRoot.addChild(ambientLightNode);

    // Set up the directional lights
    Color3f light1Color = new Color3f(1.0f, 1.0f, 0.9f);
    Vector3f light1Direction = new Vector3f(1.0f, 1.0f, 1.0f);
    Color3f light2Color = new Color3f(1.0f, 1.0f, 1.0f);
    Vector3f light2Direction = new Vector3f( -1.0f, -1.0f, -1.0f);

    DirectionalLight light1
        = new DirectionalLight(light1Color, light1Direction);
    light1.setInfluencingBounds(bounds);
    objRoot.addChild(light1);

    DirectionalLight light2
        = new DirectionalLight(light2Color, light2Direction);
    light2.setInfluencingBounds(bounds);
    objRoot.addChild(light2);


    return objRoot;
  }

}