package terrapeer.vui.j3dui.feedback.elements;

import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.audioengines.javasound.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.utils.*;
import terrapeer.vui.j3dui.utils.app.*;
import terrapeer.vui.j3dui.control.*;

/**
Creates a background sound effect with infinite scheduling
bounds that plays once, such as for mouse-over.  The gain is
set to a low (gentle) value relative to the current volume
setting.
<P>
This class delegates to the sound instead of extending it
so that setEnable() can be defined for debugging.  This is
necessary because Sound declares the method as final.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class SoundEffect extends Group
 implements EnableTarget {
	
	// public interface =========================================

	/**
	Constructs a SoundEffect with the specified sound file.  If
	there is a problem with loading the sound file it is reported
	and the sound is left in an undefined state.
	@param soundPath Path to the sound file.
	@param gain Relative intensity of the sound [0-1].
	@param host Host environment for the sound audio
	device.
	*/
	public SoundEffect(String soundPath, double gain,
	 PhysicalEnvironment host) {
	
		// build sound node and add as child to this
		_sound = new BackgroundSound();
		_sound.setInitialGain((float)gain);
		_sound.setReleaseEnable(true);  // let it finish playing
		addChild(_sound);

		// load sound from file and cache it for speed		
		MediaContainer media = LoadUtils.loadSound(soundPath);
		if(media==null) return;
		media.setCacheEnable(true);
		_sound.setSoundData(media);

		// build audio device based on physical environ		
		JavaSoundMixer mixer = new JavaSoundMixer(host);
		mixer.initialize();

		// set infinite bounds		
		BoundingSphere bounds = new BoundingSphere(
		 new Point3d(0.0,0.0,0.0), Double.POSITIVE_INFINITY); 
		_sound.setSchedulingBounds(bounds);

		// set live capabilities
		_sound.setCapability(Sound.ALLOW_ENABLE_WRITE);
		_sound.setCapability(Sound.ALLOW_IS_PLAYING_READ);
		
if(Debug.getEnabled()){
_sound.setCapability(Sound.ALLOW_IS_READY_READ);
_sound.setCapability(Sound.ALLOW_DURATION_READ);
}
		
	}
	
	// EnableTarget implementation

	/**
	The sound is started when this event is received with a
	true value.
	*/
	public void setEnable(boolean enable) {
					
if(Debug.getEnabled()){		
Debug.println("SoundEffect",
"SOUND: SoundEffect:" +
" enable=" + enable +
" isReady=" + _sound.isReady() +
" isPlaying=" + _sound.isPlaying() +
" duration=" + _sound.getDuration());}

		// start a new sound if the previous one is finished
// J3D 1.1.2 bug does not correctly report isPlaying()
//		if(!_sound.isPlaying()) _sound.setEnable(enable);
		_sound.setEnable(enable);
			
if(Debug.getEnabled()){		
Debug.println("SoundEffect",
"SOUND: SoundEffect:" +
" isPlaying=" + _sound.isPlaying());}

	}
			
	// personal body ============================================
	
	/** The sound. */
	BackgroundSound _sound;

}