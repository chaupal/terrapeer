package terrapeer.vui.j3dui.utils.blocks;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.utils.*;
import terrapeer.vui.j3dui.utils.app.*;
import terrapeer.vui.j3dui.utils.objects.*;
import terrapeer.vui.j3dui.control.*;
import terrapeer.vui.j3dui.control.inputs.*;
import terrapeer.vui.j3dui.control.inputs.sensors.*;
import terrapeer.vui.j3dui.control.mappers.*;
import terrapeer.vui.j3dui.control.mappers.intuitive.*;
import terrapeer.vui.j3dui.control.actuators.*;
import terrapeer.vui.j3dui.control.actuators.groups.*;

/**
 These utilities are common building block utilities that were
 first introduced for the book's examples on "intuitive" control.
 <P>
 Because these building blocks are rather specific in purpose
 they are included here as utilities instead of as general
 purpose building block classes in the framework core.
 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */

public class IntuitiveBlocks
{

  // public utilities =========================================

  /**
    Creates a mouse move and drag sensors, an object picker, and
    an over enabler and connects them together for continuous
    target picking and enabling, whether during mouse movement
    or dragging.  All enable targets will be initialized as if
    no target was picked.
    @param view Source display.
    @param pickRoot Scene graph pick root.
    @param pickList List of pick targets.
    @param enableList List of enable targets corresponding to
    the targets in the pick list.
   */
  public static final OverEnableMapper buildOverEnabler(
      AppView view, BranchGroup pickRoot, ArrayList pickList,
      ArrayList enableList)
  {

    // setup over target enabling
    OverEnableMapper enabler = new OverEnableMapper();
    enabler.setEventTargets(enableList);

    enabler.initEventTargets( -1);

    // setup continuous target picking
    ObjectPickMapper picker = new ObjectPickMapper(enabler,
        new PickEngine(pickRoot, pickList));

    /// generate mouse position for any move or drag		
//		MouseDragSensor dragger = new MouseDragSensor(picker,
//		 view.getDisplay(), view.getRoot());
    AwtMouseDragSensor dragger = new AwtMouseDragSensor(
        view.getDisplay(), picker);
    dragger.setButtons(Input.BUTTON_ALL);

//		MouseMoveSensor mouseMove = new MouseMoveSensor(
//		 picker, view.getDisplay(), view.getRoot());
    AwtMouseMoveSensor mouseMove = new AwtMouseMoveSensor(
        view.getDisplay(), picker);

    return enabler;
  }

  /**
    Creates an intuitive drag mapper and corresponding source
    drag mapper, configures them for DRM translation in view X-Y,
    and connects them to the target object.
    @param target Target object.
    @param view Source display.  If null drag source is used.
    @param cumulative True if drag actions are cumulative.
    @return New building block.
   */
  public static final InputDragTarget
      buildDrmTranslationMapper(AffineGroup target,
                                AppView view, boolean cumulative)
  {

    // use target translation node as source space	
    DirectSourceDragPlugin plugin =
        new DirectSourceDragPlugin(
        target.getTranslation().getTargetNode());

    // build source mapper for target X-Y translation		
    SourceDragMapper mapper = new SourceDragMapper(
        target.getTranslation(), plugin);
    mapper.setCumulative(cumulative);

    // build DRM mapper
    DrmDragPlugin drmPlugin = new DrmDragPlugin();
    if (view != null)
      drmPlugin = new DrmDragPlugin(view);

    IntuitiveDragMapper drmMapper = new IntuitiveDragMapper(
        mapper, drmPlugin);

    // scale input drag
    return new InputDragFilter(drmMapper,
                               new ScaleInputDragPlugin(new Vector2d(.025, .025)));
  }

  /**
    Creates an intuitive drag mapper and corresponding source
    drag mapper, configures them for DRM translation in view X-Z,
    and connects them to the target object.
    @param target Target object.
    @param view Source display.  If null drag source is used.
    @param cumulative True if drag actions are cumulative.
    @return New building block.
   */
  public static final InputDragTarget
      buildDrmTranslationZMapper(AffineGroup target,
                                 AppView view, boolean cumulative)
  {

    // use target translation node as source space	
    DirectSourceDragPlugin plugin =
        new DirectSourceDragPlugin(
        target.getTranslation().getTargetNode());

    // build source mapper for target X-Z translation		
    plugin.setSourceMap(Mapper.DIM_X, Mapper.DIM_Z,
                        Mapper.DIM_NONE);
    plugin.setSourceScale(new Vector3d(1, -1, 1));

    SourceDragMapper mapper = new SourceDragMapper(
        target.getTranslation(), plugin);
    mapper.setCumulative(cumulative);

    // build DRM mapper
    DrmDragPlugin drmPlugin = new DrmDragPlugin();
    if (view != null)
      drmPlugin = new DrmDragPlugin(view);

    IntuitiveDragMapper drmMapper = new IntuitiveDragMapper(
        mapper, drmPlugin);

    // scale input drag
    return new InputDragFilter(drmMapper,
                               new ScaleInputDragPlugin(new Vector2d(.025, .025)));
  }

  /**
    Creates an intuitive drag mapper and corresponding source
    drag mapper, configures them for DRM Y-axis rotation,
    and connects them to the target object.
    @param target Target object.
    @param view Source display.  If null drag source is used.
    @param cumulative True if drag actions are cumulative.
    @return New building block.
   */
  public static final InputDragTarget
      buildDrmRotationYMapper(AffineGroup target,
                              AppView view, boolean cumulative)
  {

    // use target axis-angle node as source space	
    AxisAngleSourceDragPlugin plugin =
        new AxisAngleSourceDragPlugin(
        target.getAxisAngle().getTargetNode());

    // build source mapper for target Y rotation		
    plugin.setTargetMap(Mapper.DIM_W, Mapper.DIM_NONE,
                        Mapper.DIM_NONE);
    plugin.setAxis(new Vector3d(0, 1, 0));

    SourceDragMapper mapper = new SourceDragMapper(
        target.getAxisAngle(), plugin);
    mapper.setCumulative(cumulative);

    // build DRM mapper
    DrmDragPlugin drmPlugin = new DrmDragPlugin();
    if (view != null)
      drmPlugin = new DrmDragPlugin(view);

    IntuitiveDragMapper drmMapper = new IntuitiveDragMapper(
        mapper, drmPlugin);

    // scale input drag
    return new InputDragFilter(drmMapper,
                               new ScaleInputDragPlugin(new Vector2d(.025, .025)));
  }

  /**
    Creates an intuitive drag mapper and corresponding source
    drag mapper, configures them for DRM scaling, and
    connects them to the target object.
    @param target Target object.
    @param view Source display.  If null drag source is used.
    @param cumulative True if drag actions are cumulative.
    @return New building block.
   */
  public static final InputDragTarget
      buildDrmScaleMapper(AffineGroup target,
                          AppView view, boolean cumulative)
  {

    // use target scale node as source space	
    DirectSourceDragPlugin plugin =
        new DirectSourceDragPlugin(
        target.getScale().getTargetNode());

    // build source mapper for target scale		
    plugin.setTargetOffset(new Vector4d(1, 1, 1, 0));

    SourceDragMapper mapper = new SourceDragMapper(
        target.getScale(), plugin);
    mapper.setCumulative(cumulative);

    // build DRM mapper
    DrmDragPlugin drmPlugin = new DrmDragPlugin();
    if (view != null)
      drmPlugin = new DrmDragPlugin(view);

    IntuitiveDragMapper drmMapper = new IntuitiveDragMapper(
        mapper, drmPlugin);

    // scale input drag
    return new InputDragFilter(drmMapper,
                               new ScaleInputDragPlugin(new Vector2d(.025, .025)));
  }

  /**
    Creates an intuitive drag mapper and corresponding source
    drag mappers, configures them for DRM spherical rotation,
    and connects them to the target object.
    @param target Target actuator.
    @param view Source display.  If null drag source is used.
    @param cumulative True if drag actions are cumulative.
    @return New input drag mapper.
   */
  public static final InputDragTarget
      buildDrmSphereMapper(AxisAngleSphereGroup target,
                           AppView view, boolean cumulative)
  {

    AxisAngleSourceDragPlugin plugin;
    SourceDragMapper mapper;
    SourceDragSplitter splitter = new SourceDragSplitter();

    // X axis		
    plugin = new AxisAngleSourceDragPlugin(
        target.getAxisAngleX().getTargetNode());
    plugin.setTargetMap(Mapper.DIM_NONE, Mapper.DIM_W,
                        Mapper.DIM_NONE);
    plugin.setAxis(new Vector3d( -1, 0, 0));

    mapper = new SourceDragMapper(
        target.getAxisAngleX(), plugin);
    mapper.setCumulative(cumulative);

    splitter.addEventTarget(mapper);

    // Y axis		
    plugin = new AxisAngleSourceDragPlugin(
        target.getAxisAngleY().getTargetNode());
    plugin.setTargetMap(Mapper.DIM_W, Mapper.DIM_NONE,
                        Mapper.DIM_NONE);
    plugin.setAxis(new Vector3d(0, 1, 0));

    mapper = new SourceDragMapper(
        target.getAxisAngleY(), plugin);
    mapper.setCumulative(cumulative);

    splitter.addEventTarget(mapper);

    // input drag mapper		
    DrmDragPlugin drmPlugin = new DrmDragPlugin();
    if (view != null)
      drmPlugin = new DrmDragPlugin(view);

    IntuitiveDragMapper drmMapper = new IntuitiveDragMapper(
        splitter, drmPlugin);

    return new InputDragFilter(drmMapper,
                               new ScaleInputDragPlugin(new Vector2d(.025, .025)));
  }

  /**
    Creates an intuitive drag mapper and corresponding source
    drag mapper, configures them for WRM translation, and
    connects them to the target object.
    @param target Target object.
    @param wrmPlugin WRM plugin with pick engine and source
    object list.
    @param relative True if source drag actions are relative.
    Typically, use false for real WRM and true otherwise.  If
    false should initialize target geometry using
    updateActuation() instead of initActuation().
    @param cumulative True if drag actions are cumulative.
    Typically, use true if relative is true.
    @return New building block.  Should be connected to an
    absolute input drag source.
   */
  public static final InputDragTarget
      buildWrmTranslationMapper(AffineGroup target,
                                WrmDragPlugin wrmPlugin, boolean relative,
                                boolean cumulative)
  {

    // build source drag mapper	
    DirectSourceDragPlugin plugin =
        new DirectSourceDragPlugin(target.getTail());

    SourceDragMapper mapper = new SourceDragMapper(
        target.getTranslation(), plugin);
    mapper.setCumulative(cumulative);

    // build relative source filter
    SourceDragTarget filter;

    if (relative)
    {
      filter = new SourceDragFilter(
          mapper, new RelativeSourceDragPlugin());
    }
    else
    {
      filter = mapper;
    }

    // build input drag mapper		
    return new IntuitiveDragMapper(filter, wrmPlugin);
  }

  /**
    Creates an intuitive drag mapper and corresponding source
    drag mapper, configures them for WRM spherical rotation, and
    connects them to the target object.
    @param target Target object.
    @param wrmPlugin WRM plugin with pick engine and source
    object list.
    @param relative True if source drag actions are relative.
    Typically, use false for real WRM and true otherwise.  If
    false should initialize target geometry using
    updateActuation() instead of initActuation().
    @param cumulative True if drag actions are cumulative.
    Typically, use true for rotations in general.
    @return New building block.  Should be connected to an
    absolute input drag source.
   */
  public static final InputDragTarget
      buildWrmSphereMapper(AxisAngleSphereGroup target,
                           WrmDragPlugin wrmPlugin, boolean relative,
                           boolean cumulative)
  {

    AxisAngleSourceDragPlugin plugin;
    SourceDragMapper mapper;
    SourceDragSplitter splitter = new SourceDragSplitter();

    // X axis		
    plugin = new AxisAngleSourceDragPlugin(target.getTail());
    plugin.setTargetMap(Mapper.DIM_NONE, Mapper.DIM_W,
                        Mapper.DIM_NONE);
    plugin.setAxis(new Vector3d( -1, 0, 0));

    mapper = new SourceDragMapper(
        target.getAxisAngleX(), plugin);
    mapper.setCumulative(cumulative);

    splitter.addEventTarget(mapper);

    // Y axis		
    plugin = new AxisAngleSourceDragPlugin(target.getTail());
    plugin.setTargetMap(Mapper.DIM_W, Mapper.DIM_NONE,
                        Mapper.DIM_NONE);
    plugin.setAxis(new Vector3d(0, 1, 0));

    mapper = new SourceDragMapper(
        target.getAxisAngleY(), plugin);
    mapper.setCumulative(cumulative);

    splitter.addEventTarget(mapper);

    // build relative source filter
    SourceDragTarget filter;

    if (relative)
    {
      filter = new SourceDragFilter(
          splitter, new RelativeSourceDragPlugin());
    }
    else
    {
      filter = splitter;
    }

    // build input drag mapper		
    return new IntuitiveDragMapper(filter, wrmPlugin);
  }

  /**
    Creates an intuitive drag mapper and corresponding source
    drag mapper, configures them for sticky pseudo-WRM
    translation with a target picker, and connects them to the
    target object.
    @param source Reference source object defining the picking
    plane orientation.
    @param target Target object.
    @param root Scene graph pick root containing the target.
    @param relative True if source drag actions are relative.
    Typically, use false for real WRM and true otherwise.  If
    false should initialize target geometry using
    updateActuation() instead of initActuation().
    @param cumulative True if drag actions are cumulative.
    Typically, use true if relative is true.
    @return New building block.  Should be connected to an
    absolute input drag source.
   */
  public static final InputDragTarget
      buildStickyWrmTranslationMapper(Node source,
                                      AffineGroup target, BranchGroup root,
                                      boolean relative,
                                      boolean cumulative)
  {

    // build target picker
    ArrayList targetList = new ArrayList();
    targetList.add(target);

    PickEngine targetPicker = new PickEngine(
        root, targetList);

    // build mapper
    InputDragTarget mapper = buildWrmTranslationMapper(
        target, new PseudoWrmDragPlugin(source, targetPicker),
        relative, cumulative);

    return mapper;
  }

}