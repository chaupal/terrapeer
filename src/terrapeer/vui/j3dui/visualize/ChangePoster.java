package terrapeer.vui.j3dui.visualize;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.control.inputs.*;

/**
This class monitors for input events and translates them into
behavior posts to interested observers.  The input events
indicate that one or more source objects may have changed.
Change sensors monitoring those objects are notified of the
potential change via change posts.  To narrow the scope of the
post a trigger object serves as a key for changes that occur
on a given source object or set of source objects.
<P>
This class is a kludge.  As of Java 3D 1.1.2 there is no direct
functionality to wakeup on view or node movement or parametric
changes, and behaviors that detect movement such as
WakeupOnCollisionMovement and Billboard exhibit
a bug where they generate events continuously.  And, even if
the event were properly reported Java 3D lacks the ability to
synchronously act on that change (i.e. the response is always
one frame behind the stimulus).
<P>
As a workaround for the event generation this class uses
behavior posting to relay candidate node and view change events
to change sensors.  There is no workaround for the
synchronization problem.
<P>
This class is a super kludge.  It was discovered after building
the initial kludge that a bug in Behavior only provides an
interested behavior with the first post instead of all of them.
Out of desparation this class bypasses the whole Behavior mess
by sending posts directly to interested listeners.  A real
Behavior post is only used to synchronize the pseudo-posting
with the Java 3D traverser.  Although not the most efficient
implementation of a global observer pattern it is effective.

@deprecated Replaced by ChangeSensor and the rest of the change
package.

@author Jon Barrilleaux,
copyright (c) 1999-2000 Jon Barrilleaux,
All Rights Reserved.
*/

public class ChangePoster implements InputDragTarget {
 
 	// public constants =========================================

	/** Pseudo post ID for node external change (pos/rot). */ 
	public static final int POST_NODE_EXTERNAL = 0x01;
	/** Pseudo post ID for node internal change (scale/etc). */ 
	public static final int POST_NODE_INTERNAL = 0x02;
	/** Pseudo post ID for view external change (pos/rot). */ 
	public static final int POST_VIEW_EXTERNAL = 0x04;
	/** Pseudo post ID for view internal change (FOV/etc). */ 
	public static final int POST_VIEW_INTERNAL = 0x08;
		
	// public utilities =========================================

	/**
	Class representing a behavior post.
	*/
	public static interface Observer {
		/**
		Called to notify the observer that a change post (i.e. a
		potential change) has occured.  The observer must search
		through the posts to find those that it may be interested
		in and act accordingly. 
		@param posts Iterator for a list of Post objects.
		*/
		public void checkPosts(Iterator posts);
	}

	/**
	Class representing a change post.  The trigger object
	serves as a key for who may be changing (i.e. the source of
	the potential change) and the post ID specifies what might
	be changing about it.
	*/
	public static class Post {
		/**
		Constructs a Post.
		@param trigger Trigger object serving as a key for the
		monitored source object(s).  If null then the trigger is
		unknown or all sources may have changed.
		@param postId Type of potential change (POST_???).
		*/
		public Post(Object trigger, int postId) {
			_trigger = trigger;
			_postId = postId;
		}
		
		public String toString() {
			return "Post[trigger=" + _trigger +
			 " postId=" + _postId + "]";
		}
		
		public Object getTrigger() {return _trigger;}
		public int getPostId() {return _postId;}
	
		private Object _trigger;
		private int _postId;
	}
	
	/**
	Initializes the poster.  Must be called before creating
	ChangePoster objects.
	@param host Group that this behavior will be added to.
	Never null.
	*/		
	public static final void init(Group host) {
		_behavior = new SyncBehavior();
		host.addChild(_behavior);
	}
	
	/**
	Checks if the poster is initialized.
	@return True if initialized.
	*/		
	public static final boolean isInit() {
		if(_behavior==null)
			return false;
		else
			return true;
	}
	
	/**
	Adds the specified observer to the observer list.  When
	any post occurs the observer is notified and is responsible
	for checking the post list.
	@param observer Observer to be added.  Never null.
	*/		
	public static final void addObserver(Observer observer) {
		if(observer==null) throw new
		 IllegalArgumentException("<observer> is null.");

		_observers.add(observer);
	}

	/**
	Explicitely posts a post to the post list.  Useful during
	initialization.
	*/
	public static final void doPost(Post post) {
		post(post);
	}
		
	// personal utilities =======================================

	/** Real post ID for SyncBehavior wakeup. */ 
	private static final int POST_SYNC = 0x8000;

	/** Global posting lockout. */	
	private static boolean _postEnable = true;

	/** Global post list of Post. */	
	private static final ArrayList _posts = new ArrayList();

	/** Global post list for when post list is busy. */	
	private static final ArrayList _rePosts =
	 new ArrayList();

	/** Global observer list of Observer. */	
	private static final ArrayList _observers = new ArrayList();

	/** Global behavior for posting sync wakeup. */	
	private static Behavior _behavior = null;

	/**
	Behavior that listens for POST_SYNC and then notifies all
	the observers to check the post list.
	*/
	private static class SyncBehavior extends Behavior {
		public SyncBehavior() {
			_wakeup = new WakeupOnBehaviorPost(null,
			 ChangePoster.POST_SYNC);
			
			// behavior always active		
			setSchedulingBounds(new BoundingSphere(
			 new Point3d(0.0,0.0,0.0),
			 Double.POSITIVE_INFINITY));
		}
		
		public void initialize() {wakeupOn(_wakeup);}
		
		public void processStimulus(Enumeration criteria) {
			
if(Debug.getEnabled()){
Debug.println("ChangePoster",
"PROCESS:ChangePoster.PosterBehavior.processStimulus:" +
" event=POST_SYNC");}

			// process posts
			Iterator iter;
			
			/// lockout posters, notify observers
			_postEnable = false;
			
			iter = _observers.iterator();
			while(iter.hasNext()) {
				Observer observer = (Observer)iter.next();
				observer.checkPosts(_posts.iterator());
			}
			_posts.clear();
			_postEnable = true;
			
			/// post the re-posts
			iter = _rePosts.iterator();
			while(iter.hasNext()) {
				post((Post)iter.next());
			}
			_rePosts.clear();

			// rearm posting sync			
			wakeupOn(_wakeup);
		}

		/** Trigger events. */
		private WakeupCondition _wakeup;
	}

	/**
	Posts the post to the post list.  All posting goes through
	this method.
	*/
	public static final void post(Post post) {
		// if posting enable use main list, else backup list
		if(_postEnable) {
			
if(Debug.getEnabled()){
Debug.println("ChangePoster.verbose",
"POST:ChangePoster.post:" +
" post=" + post);}

			// reject duplicates (fast but not perfect)
			if(!_posts.contains(post))
				_posts.add(post);
			
			// make behavior wakeup on the next render cycle
			_behavior.postId(POST_SYNC);
		} else {
			
if(Debug.getEnabled()){
Debug.println("ChangePoster.verbose",
"POST:ChangePoster.post:" +
" rePost=" + post);}

			_rePosts.add(post);
		}
	}

	// public interface =========================================

	/**
	Constructs a ChangePoster that posts the specified
	change ID when an input event occurs.
	@param trigger Trigger object serving as a key for the
	monitored source object(s).  If null then the trigger is
	unknown or all sources may have changed.
	@param postId Type of potential change (POST_???).
	*/
	public ChangePoster(Object trigger, int postId) {
		if(_behavior==null) throw new
		 IllegalArgumentException("Must call init() first.");

		_post = new Post(trigger, postId);		
	}

	/**
	Explicitely posts this post to the post list.  Useful for
	chaining changes.
	*/
	public void doPost() {
		post(_post);
	}
	
	// InputDragTarget implementation
	
	public void startInputDrag(Canvas3D source, Vector2d pos) {
		// do nothing, wait for doInputDrag
	}
	
	public void doInputDrag(Canvas3D source, Vector2d pos) {
 
if(Debug.getEnabled()){
Debug.println("ChangePoster",
"DRAG:ChangePoster:doInputDrag:" +
" post=" + _post);}

		post(_post);
	}
	
	public void stopInputDrag(Canvas3D source, Vector2d pos) {
 
if(Debug.getEnabled()){
Debug.println("ChangePoster",
"DRAG:ChangePoster:stopInputDrag:" +
" post=" + _post);}

		post(_post);
	}

	// personal body ============================================

	/** Post posted by this poster */
	private Post _post;
}