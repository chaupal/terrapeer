package terrapeer.vui.j3dui.feedback.elements;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.utils.*;

/**
Abstract group respresenting a pickable, colored, shaded,
transparent, and textured object intended for use as a
feedback element.  As such, the object is configured
so that all of these parameters can be set while the object
is live.  Also, its color is set intrinsically as an
attribute (not as the diffuse color), and its pickability
is for geometry (not bounds).  Objects typically consist
of two flat leaf shapes, back to back, with the texture
being applied to both sides (top and bottom) so that they
match (like the pattern sewn into a flag). 
<P>
Note that the texture is modulated by the underlying shape
color, whether intrinsically colored or shaded.  Thus, if
intrinsic coloring is used, the color must be set to something
other than black for the texture to be seen. 
<P>
Note that with transparent textures (has a transparent
background) then the underlying shape must also be
transparent for background transparency to work.  Note that
transparency only needs to be enabled on the shape; the actual
transparency value is ignored by Java 3D (which seems odd).
<P>
Note that an object can either be intrinsically colored
or shaded, but not both -- if shaded, coloring is ignored, and
vice versa.  Also, for shading to work, the object's geometry
must contain surface normals.  Due to the nature of Java 3D,
the type of shading -- flat or gouraud -- must be known
prior to generating the normals.  As such, objects generally do
not provide a choice of shading model, but instead return the
type of shading model used to generate its normals, if any,
through getShadingModel().
<P>
In Java 3D 1.1.2, overlapping transparent shapes, especially
textured ones, can appear quite strange.  To assure correct
overlap the shapes must be non-intersecting, separate children
of an OrderedGroup, and non-coplanar.  This problem has seems to
be fixed in Java 3D 1.2.

@author Jon Barrilleaux,
copyright (c) 1999-2000 Jon Barrilleaux,
All Rights Reserved.
*/

public abstract class TextureShape extends Group {
 
 	// public constants =========================================

	/** No shading model (shading not supported). */
	public static final int SHADING_NONE = 0;

	/** Flat shading. */
	public static final int SHADING_FLAT = 1;

	/** Goraud shading. */
	public static final int SHADING_GOURAUD = 2;
	
	// public interface =========================================

	/**
	Constructs a TextureShape that is empty.  Use setShapes() to
	set the object shapes and then use the other setters to set
	the shape visible attributes.
	*/
	public TextureShape() {
		// set live capabilities
		_app.setCapability(Appearance.
		 ALLOW_TEXTURE_WRITE);
		
		_app.setCapability(Appearance.
		 ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
		_transAtt.setCapability(TransparencyAttributes.
		 ALLOW_VALUE_WRITE);
		_transAtt.setCapability(TransparencyAttributes.
		 ALLOW_MODE_WRITE);
		
		_app.setCapability(Appearance.
		 ALLOW_COLORING_ATTRIBUTES_WRITE);
		_colorAtt.setCapability(ColoringAttributes.
		 ALLOW_COLOR_WRITE);
		
		// build and save basic appearance
		_app.setMaterial(null);
	}

	/**
	Sets the object geometry pickability.
	@param pickable True if pickable by geometry.
	*/
	public void setPickable(boolean pickable) {
		Iterator iter = _shapes.iterator();
		while(iter.hasNext()) {
			Shape3D shape = (Shape3D)iter.next();
			PickUtils.setGeoPickable(shape, pickable);
		}
	}

	/**
	Sets the object intrinsic color, which is not dependent on
	lighting; and disables any shading.
	@param color New color.  If null, the color will be set to
	white, which allows any texture to appear normally, without
	color modulation.
	*/
	public void setColor(Color3f color) {
		if(color==null) color = _white;
		
		// disable shading
		_app.setMaterial(null);
		
		// set color
		_colorAtt.setColor(color);
		_app.setColoringAttributes(_colorAtt);
		
		// update shapes, by shape		
		Iterator iter = _shapes.iterator();
		while(iter.hasNext()) {
			Shape3D shape = (Shape3D)iter.next();
			shape.setAppearance(_app);
		}
	}

	/**
	Sets the object shading appearance, which is dependent
	on lighting.  The object shading will modulate the texture
	coloring.  Ignored if no shading model is supported (i.e.
	getShadingModel() returns TextureShape.SHADING_NONE).
	@param mat The surface material properties.  By value.  If
	null, disables shading and the object is colored
	intrinsically (without lighting).
	*/
	public void setShading(Material mat) {
		if(getShadingModel()==SHADING_NONE) return;
		
		// set material
		_app.setMaterial(mat);

		// set shading model
		switch(getShadingModel()) {
		 case SHADING_FLAT:
			_colorAtt.setShadeModel(
			 ColoringAttributes.SHADE_FLAT);
			break;
		 case SHADING_GOURAUD:
			_colorAtt.setShadeModel(
			 ColoringAttributes.SHADE_GOURAUD);
			break;
		}
				
		_app.setColoringAttributes(_colorAtt);

		// update shapes, by shape		
		Iterator iter = getShapes().iterator();
		while(iter.hasNext()) {
			Shape3D shape = (Shape3D)iter.next();
			shape.setAppearance(_app);
		}
	}

	/**
	Gets the shading model, if any, used by this shape in
	generating its surface normals.  If
	TextureShape.SHADING_NONE, the shape does not support
	shading.  Must be overriden by subclasses.
	@return Shading model (TextureShape.???).
	*/
	public abstract int getShadingModel();

	/**
	Sets the object transparency (0.0 to 1.0).  If out of range
	(e.g. -1) the transparency will be disabled.
	@param trans New transparency.
	*/
	public void setTransparency(double trans) {
		// set transparency mode
		if(trans<0.0 || trans>1.0) {
			// disable transparency
			_transAtt.setTransparencyMode(
			 TransparencyAttributes.NONE);
		} else {
			// set transparency (Java 3D 1.1.2 has a bug with
			// NICEST and transparent textures)
			_transAtt.setTransparencyMode(
			 TransparencyAttributes.FASTEST);
		}

		// set transparency value			 
		_transAtt.setTransparency((float)trans);
		_app.setTransparencyAttributes(_transAtt);
		
		// update shapes, by shape		
		Iterator iter = _shapes.iterator();
		while(iter.hasNext()) {
			Shape3D shape = (Shape3D)iter.next();
			shape.setAppearance(_app);
		}
	}

	/**
	Sets the object texture.
	@param texPath The path of the file containing the texture
	image.  Null if none.
	*/
	public void setTexture(String texPath) {
		// load and configure texture
		Texture2D texture = null;
		
		if(texPath!=null) {
			texture = LoadUtils.loadTexture2D(texPath);
			if(texture==null) {
				System.out.println("TextureShape:" +
				 " Problem loading texture at \"" +
				 texPath + "\"");
			} else {
				texture.setMinFilter(Texture.NICEST);
				texture.setMagFilter(Texture.NICEST);
			}
		}
		
		// set texture
		_app.setTexture(texture);

		// modulate texture with shape color		
		_textureAtt.setTextureMode(TextureAttributes.MODULATE);
		_app.setTextureAttributes(_textureAtt);
		
		// update shapes, by shape		
		Iterator iter = _shapes.iterator();
		while(iter.hasNext()) {
			Shape3D shape = (Shape3D)iter.next();
			shape.setAppearance(_app);
		}
	}

	/**
	Gets the object shapes.
	@return List of shapes.
	*/
	public ArrayList getShapes() {
		return _shapes;
	}

	/**
	Gets the object appearance.
	@return reference to appearance.
	*/
	public Appearance getAppearance() {
		return _app;
	}
			
	// personal body ============================================

	/** List of shapes.  Never null. */
	private ArrayList _shapes = new ArrayList();

	/** Local copy of appearance. */
	private final Appearance _app = new Appearance();

	/** Constant white color. */
	private final Color3f _white = new Color3f(1f, 1f, 1f);

	/** Dummy ColoringAttributes.  (for GC) */
	private final ColoringAttributes _colorAtt =
	 new ColoringAttributes();

	/** Dummy TextureAttributes.  (for GC) */
	private final TextureAttributes _textureAtt =
	 new TextureAttributes();

	/** Dummy TransparencyAttributes.  (for GC) */
	private final TransparencyAttributes _transAtt =
	 new TransparencyAttributes();

	/**
	Adds the specified shape to the object and configures it for
	live access.  This object must be dead.  This method is
	intended for use during construction.  Because of problems
	with Java 3D transparent object overlap, the appearance
	of overlapping shapes may be dependant on the order in which
	they are added. 
	@param shape Shape to be added.  Ignored if null.
	*/
	protected void addShape(Shape3D shape) {
		if(shape==null) return;

		shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);		
		PickUtils.allowSetGeoPickable(shape);
		addChild(shape);
		_shapes.add(shape);
	}
}