package terrapeer.vui.j3dui.utils;

import java.io.*;
import java.net.*;
import javax.media.j3d.*;

import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.vrml97.*;

import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.Scene;

/**
 Collection of utilities for loading 3D models from URL or file
 path.  Models can be in the form of a VRML file or a Java 3D
 class or source files.
 @author Jon Barrilleaux, 1999. Edited 2003 by Henrik Gehrmann.
 */

public class ModelLoader
{
  private static BranchGroup objRoot = null;
  private static URL filename = null;
  private static boolean noTriangulate = false;
  private static boolean noStripify = false;
  private static double creaseAngle = 60.0;

  // public utilities =========================================

  /**
    Loads a 3D model.  Tries to load the model in various ways
    until one worKs or they all fail.
    @param name URL/file path of model to be loaded.
    @return Node containing the model root.  Null if none.
   */
  public static Node loadModelFromName(String name)
  {
    if (Debug.getEnabled())
      Debug.println("ModelLoader", "Object: "+name);

    // load scene
    if(filename == null && name.endsWith("obj"))
    {
      if (Debug.getEnabled())
        Debug.println("ModelLoader", "     ...loading type OBJ");

      System.out.println("Loading Type OBJ");
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

      try
      {
        filename = new URL("file:"+name);
      }
      catch (MalformedURLException e)
      {
        System.err.println(e);
        System.exit(1);
      }

      int flags = ObjectFile.RESIZE;
      if (!noTriangulate)
        flags |= ObjectFile.TRIANGULATE;
      if (!noStripify)
        flags |= ObjectFile.STRIPIFY;

      ObjectFile f = new ObjectFile(flags,
                                    (float) (creaseAngle * Math.PI / 180.0));
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

      return objRoot;
    }
    else
    {

      if (Debug.getEnabled())
      {
        Debug.println("ModelLoader",
                      "LOAD:ModelLoader:loadModelFromName:" +
                      " name=" + name);
      }

      Node model = null;

      model = loadVrmlFromName(name);
      if (model != null)
        return model;

      if (Debug.getEnabled())
      {
        Debug.println("ModelLoader",
                      "LOAD:ModelLoader:loadModelFromName:" +
                      " Not a vrml model.");
      }

      model = (Node) loadObjectFromName(name, Node.class);
      if (model != null)
        return model;

      if (Debug.getEnabled())
      {
        Debug.println("ModelLoader",
                      "LOAD:ModelLoader:loadModelFromName:" +
                      " Not a class model.");
      }

      System.out.println(
          " Can't load model at '" + name + "'.");
    }
    return null;
  }

  /**
    Loads a VRML model.
    @param name URL/file path of model to be loaded.
    @return Node containing the model root.
   */
  public static BranchGroup loadVrmlFromName(String name)
  {

    if (Debug.getEnabled())
    {
      Debug.println("ModelLoader",
                    "LOAD:ModelLoader:loadVrmlFromName:" +
                    " name=" + name);
    }

    Loader loader = new VrmlLoader();
    Scene scene = null;
    BranchGroup group = null;

    try
    {
      // first try as URL
      URL url = new URL(name);
      try
      {
        scene = loader.load(url);
      }
      catch (Exception ex)
      {

        if (Debug.getEnabled())
        {
          Debug.println("ModelLoader",
                        "LOAD:ModelLoader:loadModelFromName:" +
                        " Bad URL. " + ex);
        }

      }
      catch (Error er)
      {

        if (Debug.getEnabled())
        {
          Debug.println("ModelLoader",
                        "LOAD:ModelLoader:loadModelFromName:" +
                        " Bad URL. " + er);
        }

      }
    }
    catch (MalformedURLException badUrl)
    {
      // next try as path
      try
      {
        scene = loader.load(name);
      }
      catch (Exception ex)
      {

        if (Debug.getEnabled())
        {
          Debug.println("ModelLoader",
                        "LOAD:ModelLoader:loadModelFromName:" +
                        " Bad path. " + ex);
        }

      }
      catch (Error er)
      {

        if (Debug.getEnabled())
        {
          Debug.println("ModelLoader",
                        "LOAD:ModelLoader:loadModelFromName:" +
                        " Bad path. " + er);
        }

      }
    }

    if (scene != null)
    {
      // get the model root
      group = scene.getSceneGroup();
    }

    return group;

  }

  /**
    Loads and instantiates an object of the specified class
    type from an arbitrary name.
    @param name Name referencing the class to be loaded.
    @param type Class type of the new object.  If null then
    the type will not be verified.
    @return New object.  Null if class not found, not loaded,
    or wrong type.
   */
  public static Object loadObjectFromName(String name,
                                          Class type)
  {

    if (Debug.getEnabled())
    {
      Debug.println("ModelLoader",
                    "LOAD:ModelLoader:loadObjectFromName:" +
                    " name=" + name);
    }

    Class cls = loadClassFromName(name);
    if (cls == null)
      return null;

    Object object = null;
    try
    {
      object = cls.newInstance();
    }
    catch (Exception ex)
    {

      if (Debug.getEnabled())
      {
        Debug.println("ModelLoader",
                      "LOAD:ModelLoader:loadObjectFromName: " + ex);
      }

    }
    catch (Error er)
    {

      if (Debug.getEnabled())
      {
        Debug.println("ModelLoader",
                      "LOAD:ModelLoader:loadObjectFromName: " + er);
      }

    }

    // verify object type
    if (object != null && type != null)
    {
      if (type.isInstance(object))
        return object;

      if (Debug.getEnabled())
      {
        Debug.println("ModelLoader",
                      "LOAD:ModelLoader:loadObjectFromName:" +
                      " Wrong type!");
      }

      return null;
    }
    return object;
  }

  /**
    Loads an arbitrary class from an arbitrary name.  The
    name can be a class name (e.g. foo.class), a file name
    (e.g. C:/foo/bar.java), or an URL (e.g.
    http://host:80/foo/bar.class#myref).  The extension
    can be ".class", ".java", or none.
    <P>
    The class is resolved from the name by successively
    removing the leading path element until a class is loaded
    or the name is empty.
    @param name Name referencing the class to be loaded.
    @return Loaded class ready for instantiation.  Null if
    class not found of loaded.
   */
  public static Class loadClassFromName(String name)
  {

    if (Debug.getEnabled())
    {
      Debug.println("ModelLoader",
                    "LOAD:ModelLoader:loadClassFromName:" +
                    " name=" + name);
    }

    if (name == null)
      return null;

    // convert name to URL for normalization
    URL url = null;

    try
    {
      url = new URL(new URL("file:"), name);
    }
    catch (Exception ex)
    {
      System.err.println("ModelLoader:loadClassFromName: " + ex);
      return null;
    }

    if (Debug.getEnabled())
    {
      Debug.println("ModelLoader",
                    "LOAD:ModelLoader:loadClassFromName:" +
                    " url=" + url);
    }

    // convert URL to class name
    String className = new String(url.getFile());
    int index;

    /// delete reference
    String urlRef = url.getRef();
    if (urlRef != null && className.endsWith(urlRef))
    {
      className = className.substring(0,
                                      className.length() -
                                      ("#" + urlRef).length());
    }

    /// delete class/java extension
    if (className.endsWith(".class"))
    {
      className = className.substring(0,
                                      className.length() - ".class".length());
    }
    else
    if (className.endsWith(".java"))
    {
      className = className.substring(0,
                                      className.length() - ".java".length());
    }

    /// delete windows/protocol/port prefix
    index = className.lastIndexOf(':');
    if (index >= 0)
    {
      index = className.indexOf('/', index);
      if (index >= 0)
        className = className.substring(index + 1);
      else
        className = "";
    }

    /// delete root
    if (className.startsWith("/"))
    {
      className = className.substring("/".length());
    }

    /// replace file separator with class separator
    className = className.replace('/', '.');

    // load class by trial and error
    Class cls = null;

    while (true)
    {

      if (Debug.getEnabled())
      {
        Debug.println("ModelLoader",
                      "LOAD:ModelLoader:loadClassFromName:" +
                      " trying class=" + className);
      }

      try
      {
        // try to load and instantiate class
        cls = Class.forName(className);
        break; // object loaded
      }
      catch (Exception ex)
      {
        // bad try: shorten class name
        index = className.indexOf('.');
        if (index < 0)
          break; // class not found
        className = className.substring(index + 1);
      }
      catch (Error e)
      {
        break; // class found but bad
      }
    }

    if (Debug.getEnabled())
    {
      Debug.println("ModelLoader",
                    "LOAD:ModelLoader:loadClassFromName:" +
                    " class=" + cls);
    }

    return cls;
  }

  // personal body ============================================

}