package terrapeer.vui.j3dui.utils.app;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.utils.objects.*;
import terrapeer.vui.j3dui.visualize.change.*;

/**
 This class represents an application's "world space".
 It contains a single scene graph and allows control over whether
 it is live or dead, which is useful for modifying the scene
 graph without restriction.  It also includes a dim ambient
 light so objects can be seen even if the user forgets to
 add lights.
 <P>
 The scene graph is divided into two top-level branches
 represented by a view root and a scene root.  By convention
 objects (subgraphs) associated with views should be added to
 the view root and all other objects should be added to the
 scene root.  This distinction is helpful for clearing the
 world without affecting the views, such as before loading a new
 model.
 <P>
 To see the contents of the world one or more AppViews must be
 added to this AppWorld and their AppDisplays must be added
 to the application AppFrame.
 <P>
 A node's capability bits can not be set if the node is
 in a live scene; and, a node can not be changed (added to
 or removed from the scene, or manipulated in the scene)
 if it is in a live scene unless appropriate capability
 bits are set.  Therefore, before setting the capabilities
 of a node in the scene use AppWorld.setLive(false) to
 kill the scene.
 <P>
 Certain operations such as removing a node which is not a
 BranchGroup can not be performed while the node is in a live
 scene graph regardless of its capability bits.  To be
 on the safe side, all operations here that affect the
 scene graph (e.g. addSceneNode()) will kill the scene graph
 automatically if it is live and then restore its original
 health status when done.
 <P>
 This class coorperates with the ChangeSensor system.  It
 invokes ChangeSensor.init() to initialize the system just
 before making the scenegraph live, and
 ChangeSensor.processAllChanges() to initialize all sensor
 target event chains just after making the scenegraph live.
 */
public class AppWorld extends VirtualUniverse
{

  // public interface =========================================

  /**
    Constructs a dead and empty world.  When ready to show it
    use setLive(true) to make the scene graph live.
    @see AppWorld#setLive
   */
  public AppWorld()
  {
    _locale = new Locale(this);
    _root = new BranchGroup();
    _viewRoot = new BranchGroup();
    _sceneRoot = new BranchGroup();
    _isLive = false;

    _root.addChild(_viewRoot);
    _root.addChild(_sceneRoot);

    // add dim ambient light to view so not cleared		
    addViewNode(new TestLight(new AmbientLight(
        new Color3f(.4f, .4f, .4f))));

    // for scene killing.
    _root.setCapability(BranchGroup.ALLOW_DETACH);
  }

  /**
    Gets the geometric locale that the world's scene graph
    is in.  The VirtualUniverse can be obtained from the
    Locale.
    @return The world's locale.
   */
  public Locale getLocale()
  {
    return _locale;
  }

  /**
    Gets the scene graph root for view objects.  Add
    view-related child nodes to this group or use addViewNode().
    @return The view root.
   */
  public BranchGroup getViewRoot()
  {
    return _viewRoot;
  }

  /**
    Gets the scene graph root for scene objects.  Add non
    view-related child nodes to this group or use addSceneNode().
    @return The scene root.
   */
  public BranchGroup getSceneRoot()
  {
    return _sceneRoot;
  }

  /**
    Adds a node to the view root.  Nodes added to the view root
    by convention should be associated with views.  First kills
    the scene to assure that the node can be added.
    @param node Node to be added.
   */
  public void addViewNode(Node node)
  {
    boolean isLive = setLive(false);
    _viewRoot.addChild(node);
    setLive(isLive);
  }

  /**
    Adds a node to the scene root.  Nodes added to the scene
    root by convention should not be associated with views.
    First kills the scene to assure that the node can be added.
    @param node Node to be added.
   */
  public void addSceneNode(Node node)
  {
    boolean isLive = setLive(false);
    _sceneRoot.addChild(node);
    setLive(isLive);
  }

  /**
    Removes all nodes from the scene so they will no longer be
    seen in the world.  Nodes under the view root are unaffected.
    The memory associated with the removed nodes may not be
    freed, however, if references to them are still held
    elsewhere in the application.
   */
  public void clearScene()
  {
    boolean isLive = setLive(false);

    if (Debug.getEnabled())
    {
      Debug.println("appWorld",
                    "WORLD:appWorld:clearWorld:" +
                    " countBefore=" + _sceneRoot.numChildren());
    }

    // children shift when removed so start at end
    for (int childI = _sceneRoot.numChildren() - 1;
         childI >= 0; childI--)
    {
      _sceneRoot.removeChild(childI);
    }

    if (Debug.getEnabled())
    {
      Debug.println("appWorld",
                    "WORLD:appWorld:clearWorld:" +
                    " countAfter=" + _sceneRoot.numChildren());
    }

    setLive(isLive);
  }

  /**
    Sets whether or not the world scene graph is "live".  While
    live the scene graph can be seen in a view and only those
    nodes whose capability bits are set can be modified.  While
    dead all nodes in the scene graph can be added and removed,
    and their capability bits can be set.
    @param isLive If true the scene graph will become live.
    If false it will become dead.
    @return Previous state of health for the scene graph.
   */
  public boolean setLive(boolean isLive)
  {
    boolean isLiveOld = _isLive;

    if (Debug.getEnabled())
    {
      Debug.println("appWorld",
                    "WORLD:appWorld:setLive:" +
                    " newLive=" + isLive +
                    " oldLive=" + isLiveOld);
    }

    if (isLive != _isLive)
    {
      if (isLive)
      {
        // init the change sensor actuator map
        ChangeSensor.initActuators();

        // make world live
        _locale.addBranchGraph(_root);

        // init all change sensor targets
        ChangeSensor.initSensors();
      }
      else
      {
        // make world dead
        _locale.removeBranchGraph(_root);
      }
    }

    _isLive = isLive;
    return isLiveOld;
  }

  /**
    Gets the state of health for the scenegraph.
    @return True if scene graph is live.
   */
  public boolean getLive()
  {
    return _isLive;
  }

  // personal body ============================================

  /** True if the scene graph is live. */
  private boolean _isLive;

  /** Locale for the world's scenegraph. */
  private Locale _locale;

  /** Root node for the world space contents. */
  private BranchGroup _root;

  /** Root node that view objects should be attached to. */
  private BranchGroup _viewRoot;

  /** Root node that scene objects should be attached to. */
  private BranchGroup _sceneRoot;

}