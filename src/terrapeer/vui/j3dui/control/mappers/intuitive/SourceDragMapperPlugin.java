package terrapeer.vui.j3dui.control.mappers.intuitive;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.mappers.*;

/**
 An abstract base class for source drag mapper personality
 plugins.
 <P>
 By definition, source space to actuation transformation is
 vector not point-based, which means that direction and
 magnitude are mapped but not position.

 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */

public abstract class SourceDragMapperPlugin
{

  // public interface =========================================

  /**
    Constructs a SourceDragMapperPlugin with the target space
    defined by the specified target node.  Sets the target
    node's capability bits for live use.
    @param targetSpace Target space reference node.  If null the
    world space will be used as the target space.
   */
  public SourceDragMapperPlugin(Node targetSpace)
  {
    _targetSpace = targetSpace;

    if (_targetSpace != null)
    {
      _targetSpace.setCapability(
          Node.ALLOW_LOCAL_TO_VWORLD_READ);
    }
  }

  /**
    Returns a node defining the reference target space.
    @return Reference to the target space node.  If null the
    target space is the world space.
   */
  public Node getTargetSpace()
  {
    return _targetSpace;
  }

  /**
    Returns a string naming this plugin.
    @return Plugin type string.
   */
  public String toString()
  {
    return "SourceDragMapperPlugin";
  }

  // personal body ============================================

  /** Defines the source space.  If null world space used. */
  private Node _targetSpace;

  /**
    Transforms the starting source drag position <pos>
    defined relative to source node <source> into the target
    actuation value <copy> relative to the target space.
    @param source. Source node.  If null then the source
    value is undefined.
    @param pos. Source position.  Never null.
    @param copy Container for the copied return value.
    @return Reference to <copy>.  Null if the source node is
    null.
   */
  protected abstract Vector4d startSourceDrag(Node source,
                                              Vector3d pos, Vector4d copy);

  /**
    Transforms the continuing source drag position <pos>
    defined relative to source node <source> into the target
    actuation value <copy> relative to the target space.
    @param source. Source node.  If null then the source
    value is undefined.
    @param pos. Source position.  Never null.
    @param copy Container for the copied return value.
    @return Reference to <copy>.  Null if the source node is
    null.
   */
  protected abstract Vector4d doSourceDrag(Node source,
                                           Vector3d pos, Vector4d copy);

  /**
    Transforms the stopping source drag position <pos>
    defined relative to source node <source> into the target
    actuation value <copy> relative to the target space.
    @param source. Source node.  If null then the source
    value is undefined.
    @param pos. Source position.  Never null.
    @param copy Container for the copied return value.
    @return Reference to <copy>.  Null if the source node is
    null.
   */
  protected abstract Vector4d stopSourceDrag(Node source,
                                             Vector3d pos, Vector4d copy);

}
