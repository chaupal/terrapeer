package  terrapeer.vui.j3dui.navigate;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.utils.objects.*;
import terrapeer.vui.j3dui.control.*;
import terrapeer.vui.j3dui.control.inputs.*;
import terrapeer.vui.j3dui.control.mappers.*;
import terrapeer.vui.j3dui.control.actuators.*;
import terrapeer.vui.j3dui.control.actuators.groups.*;

/**
Creates a controller (draggers and mappers) for an OrbitCamera,
which presumably has a view and possibly a headlight attached.
<P>
By default, camera LFO-Z is directly controlled with mouse
and arrow Y, and the CTRL modifier; camera orbit-heading
and elevation are directly controlled with mouse and arrow
X and Y, and no modifiers; and, camera LAP-X and Z are
DRM controlled with mouse and arrow X and Y, and the
SHIFT-CTRL modifiers.

@author Jon Barrilleaux,
copyright (c) 2000 Jon Barrilleaux,
All Rights Reserved.
*/
public class OrbitCameraControl {
	
	// public interface =========================================

	/**
	Constructs an OrbitCameraControl for a source display
	and target camera.
	@param display Display serving as the event source
	for drags.  Typically this is the display of the view that
	is attached to the camera.  Null if none, but have to
	eventually add one to all draggers and cancelers.
	@param camera Target orbit camera.  Never null.
	*/
	public OrbitCameraControl(Canvas3D display,
	 OrbitCamera camera) {
		if(camera==null) throw new
		 IllegalArgumentException("'camera' is null.");
		_camera = camera;
				
		// build LFO controls
		_lfoMapper = new DirectMapper(
		 _camera.getLfoActuator(), true, new Vector2d(0, -1),
		 Mapper.DIM_NONE, Mapper.DIM_Z, null);
		 
		_lfoDragger = new RelativeDragger(
		 display, _lfoMapper,
		 Input.BUTTON_FIRST, Input.MODIFIER_CTRL,
		 Input.MODIFIER_CTRL, new Vector2d(.05, .05),
		 new Vector2d(.05, .05));
		
		// build orbit controls
		_orbitMapper = new DirectMapper(
		 _camera.getOrbitActuator(), true, new Vector2d(1, -1),
		 Mapper.DIM_Y, Mapper.DIM_X, null);
		 
		_orbitDragger = new RelativeDragger(
		 display, _orbitMapper,
		 Input.BUTTON_FIRST, Input.MODIFIER_NONE,
		 Input.MODIFIER_NONE, new Vector2d(.005, .005),
		 new Vector2d(.01, .01));
		
		// build LAP controls
		_lapMapper = new DrmTranslationMapper(
		 _camera.getLapActuator(), true,
		 DrmTranslationMapper.TRANSLATE_XY_XZ, false);
		
		_lapDragger = new RelativeDragger(
		 display, _lapMapper,
		 Input.BUTTON_FIRST, Input.MODIFIER_SHIFT_CTRL,
		 Input.MODIFIER_SHIFT_CTRL, new Vector2d(.05, .05),
		 new Vector2d(.05, .05));

		/// limit LAP to X-Z plane		 
		_camera.getLapActuator().getPlugin().setTargetClamp(
		 Mapper.DIM_Y, new Vector2d(0,0));
	}

	/**											
	Gets the LFO mapper.
	@return Reference to the LFO mapper.
	*/
	public DirectMapper getLfoMapper() {
		return _lfoMapper;
	}

	/**											
	Gets the LFO dragger.
	@return Reference to the LFO dragger.
	*/
	public RelativeDragger getLfoDragger() {
		return _lfoDragger;
	}

	/**											
	Gets the orbit mapper.
	@return Reference to the orbit mapper.
	*/
	public DirectMapper getOrbitMapper() {
		return _orbitMapper;
	}

	/**											
	Gets the orbit dragger.
	@return Reference to the orbit dragger.
	*/
	public RelativeDragger getOrbitDragger() {
		return _orbitDragger;
	}

	/**											
	Gets the LAP mapper.
	@return Reference to the LAP mapper.
	*/
	public DrmTranslationMapper getLapMapper() {
		return _lapMapper;
	}

	/**											
	Gets the LAP dragger.
	@return Reference to the LAP dragger.
	*/
	public RelativeDragger getLapDragger() {
		return _lapDragger;
	}
			
	// personal body ============================================
	
	/** Orbit camera. Never null. */
	private OrbitCamera _camera;
	
	/** LFO direct mapper. Never null. */
	private DirectMapper _lfoMapper;
	 
	/** LFO relative dragger. Never null. */
	private RelativeDragger _lfoDragger;
	
	/** Orbit direct mapper. Never null. */
	private DirectMapper _orbitMapper;
	 
	/** Orbit relative dragger. Never null. */
	private RelativeDragger _orbitDragger;
	
	/** LAP DRM mapper. Never null. */
	private DrmTranslationMapper _lapMapper;
	 
	/** LAP relative dragger. Never null. */
	private RelativeDragger _lapDragger;
	
}
