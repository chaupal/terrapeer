package terrapeer.vui.j3dui.feedback.elements;

import java.net.*;
import java.awt.*;
import java.awt.image.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.utils.*;

/**
Creates a rectangular texture shape (see TextureShape).
<P>
The rectangle lies in the X-Y plane centered about the Z axis.
The side towards +Z is the "top".
<P>
The edges of the texture correponds to the edges of the
rectangle.  The texture +X and +Y axes are parallel to
the rectangle +X and +Y axes.  Top and bottom side textures
will match.  If the texture can not be loaded it is ignored
with a warning.

@see TextureShape

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class TextureRect extends TextureShape {
		
	// public utilities =========================================

	/**
	Builds the geometry for one side of a texture rectangle
	shape.
	@param topSide If true the shape will be the "top" side.
	If false it will be the "bottom" side.
	@param width Width of the rectangle.
	@param height Height of the rectangle.
	@return A new shape geometry.  Never null.
	*/
	public static Geometry buildGeometry(boolean topSide, double width,
	 double height) {
			
if(Debug.getEnabled()){		
Debug.println("TextureRect",
"TextureRect:buildGeometry:" +
" topSide=" + topSide +
" width=" + width +
" height=" + height);}
		
		// build geometry
		QuadArray array = new QuadArray(4,
		 GeometryArray.COORDINATES |
		 GeometryArray.TEXTURE_COORDINATE_2 |
		 GeometryArray.NORMALS);
		
		if(topSide) {
			array.setCoordinates(0, new double[] {
			 -width/2.0, height/2.0, 0,
			 -width/2.0, -height/2.0, 0,
			 width/2.0, -height/2.0, 0,
			 width/2.0, height/2.0, 0});
			array.setTextureCoordinates(0, new float[] {
			 0f, 1f,
			 0f, 0f,
			 1f, 0f,
			 1f, 1f});
			array.setNormals(0, new float[] {
			 0f, 0f, 1f,
			 0f, 0f, 1f,
			 0f, 0f, 1f,
			 0f, 0f, 1f});
		} else {
			array.setCoordinates(0, new double[] {
			 -width/2.0, height/2.0, 0,
			 width/2.0, height/2.0, 0,
			 width/2.0, -height/2.0, 0,
			 -width/2.0, -height/2.0, 0});
			array.setTextureCoordinates(0, new float[] {
			 0f, 1f,
			 1f, 1f,
			 1f, 0f,
			 0f, 0f});
			array.setNormals(0, new float[] {
			 0f, 0f, -1f,
			 0f, 0f, -1f,
			 0f, 0f, -1f,
			 0f, 0f, -1f});
		}
		
		return array;
	}
	
	// public interface =========================================

	/**
	Constructs a TextureRect with the specified attributes.
	By default the object will not be pickable, will be white,
	will not be transparent, and will not be textured.
	@param sideFlags Flags indicating which sides of the object
	to build (Element.SIDE_???).
	@param width Width of the rectangle.
	@param height Height of the rectangle.
	*/
	public TextureRect(int sideFlags, double width,
	 double height) {
	
		this(false, null, -1, null,
		 sideFlags, width, height); 
	}

	/**
	Constructs a colored TextureRect with the specified
	attributes.
	@param pickable If true the object will be pickable.
	@param color Object intrinsic color.  Null for white.
	@param trans Object transparency (0 - 1).  If out of range
	transparency is disabled.
	@param texPath The path of the file containing the texture
	image.  Null if none.
	@param sideFlags Flags indicating which sides of the object
	to build (Element.SIDE_???).
	@param width Width of the rectangle.
	@param height Height of the rectangle.
	*/
	public TextureRect(boolean pickable, Color3f color,
	 double trans, String texPath, int sideFlags,
	 double width, double height) {
	
		// build sides
		if((sideFlags & Elements.SIDE_BOTTOM) != 0) {
			Shape3D shape = new Shape3D();
			shape.setGeometry(buildGeometry(false,
			 width, height));
			addShape(shape);
		}
		if((sideFlags & Elements.SIDE_TOP) != 0) {
			Shape3D shape = new Shape3D();
			shape.setGeometry(buildGeometry(true,
			 width, height));
			addShape(shape);
		}
		
		// set settables
		setPickable(pickable);
		setColor(color);
		setTransparency(trans);
		setTexture(texPath);
	}
	
	public int getShadingModel() {
		return TextureShape.SHADING_FLAT;
	}
			
	// personal body ============================================
}