package terrapeer.vui.j3dui.feedback.elements;

import java.net.*;
import java.awt.*;
import java.awt.image.*;

import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
Creates a cylindrical texture shape (see TextureShape).
<P>
The axis of the cyclinder is the Y axis.  The top and
bottom can have different radii.  The bottom is located at zero
along the Y axis.  The top is located at a specified height
along the +Y axis.  The side away from the Y axis is the "top".
<P>
The texture is "wrapped" around the cylinder counter-clockwise
starting and ending in the direction of the +X axis.  The top
and bottom edges of the texture corespond to the top and bottom
of the cylinder.  Top and bottom side textures will match.  If
the texture can not be loaded it is ignored with a warning.

@see TextureShape

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class TextureCylinder extends TextureShape {
		
	// public utilities =========================================

	/**
	Builds the geometry for one side of a texture cylinder shape.
	@param topSide If true the shape will be the "top" side.
	If false it will be the "bottom" side.
	@param btmRadius Radius of the cyclinder bottom.
	@param topRadius Radius of the cyclinder top.
	@param height Height of the cyclinder from bottom to top.
	@return A new shape geometry.  Never null.
	*/
	public static Geometry buildGeometry(boolean topSide,
	 double btmRadius, double topRadius, double height) {
			
if(Debug.getEnabled()){		
Debug.println("TextureCylinder",
"TextureCylinder:buildGeometry:" +
" topSide=" + topSide +
" btmRadius=" + btmRadius +
" topRadius=" + topRadius +
" height=" + height);}

		int facetCount = 12;		
		int vertexCount = 2 * (facetCount + 1);		
		double angStart = 0;
		double angIncr = 2*Math.PI / facetCount;
		double texIncr = 1.0 / facetCount;
		
		// build geometry
		TriangleStripArray array = new TriangleStripArray(
		 vertexCount, GeometryArray.COORDINATES|
		 GeometryArray.TEXTURE_COORDINATE_2,
		 new int[] {vertexCount});
		
		double rawX, rawZ, ang, tex;
		float posTX, posTZ, posBX, posBZ, texX;
		
		ang = -angStart;
		tex = 0;
		for(int index=0; index<vertexCount; index++) {
			rawX = Math.cos(ang);
			rawZ = Math.sin(ang);
			
			posTX = (float)(topRadius * rawX);
			posTZ = (float)(topRadius * rawZ);
			posBX = (float)(btmRadius * rawX);
			posBZ = (float)(btmRadius * rawZ);
			texX = (float)tex;
		
if(Debug.getEnabled()){		
Debug.println("TextureCylinder.build",
"BUILD: TextureCylinder:buildSide:" +
" index=" + index +
" ang=" + ang +
" raw=" + rawX + " " + rawZ +
" posT=" + posTX + " " + posTZ +
" posB=" + posBX + " " + posBZ +
" texX=" + texX
);}

			array.setCoordinate(index, new float[] {
			 posTX, (float)height, posTZ});
			array.setTextureCoordinate(index, new float[] {
			 texX, 1f});
			
			index++;

			array.setCoordinate(index, new float[] {
			 posBX, 0f, posBZ});
			array.setTextureCoordinate(index, new float[] {
			 texX, 0f});
				
			if(topSide) {
				ang -= angIncr;
				tex -= texIncr;
			} else {
				ang += angIncr;
				tex += texIncr;
			}
		}
		
		return array;
	}
	
	// public interface =========================================

	/**
	Constructs a TextureCylinder with the specified attributes.
	By default the object will not be pickable, will be white,
	will not be transparent, and will not be textured.
	@param sideFlags Flags indicating which sides of the object
	to build (Element.SIDE_???).
	@param btmRadius Radius of the cyclinder bottom.
	@param topRadius Radius of the cyclinder top.
	@param height Height of the cyclinder from bottom to top.
	*/
	public TextureCylinder(int sideFlags, double btmRadius,
	 double topRadius, double height) {
	
		this(false, null, -1, null,
		 sideFlags, btmRadius, topRadius, height); 
	}

	/**
	Constructs a TextureCylinder with the specified attributes.
	@param pickable If true the object will be pickable.
	@param color Object intrinsic color.  Null for white.
	@param trans Object transparency (0 - 1).  If out of range
	transparency is disabled.
	@param texPath The path of the file containing the texture
	image.  Null if none.
	@param sideFlags Flags indicating which sides of the object
	to build (Element.SIDE_???).
	@param btmRadius Radius of the cyclinder bottom.
	@param topRadius Radius of the cyclinder top.
	@param height Height of the cyclinder from bottom to top.
	*/
	public TextureCylinder(boolean pickable, Color3f color,
	 double trans, String texPath, int sideFlags,
	 double btmRadius, double topRadius, double height) {
	
		// build sides
		if((sideFlags & Elements.SIDE_BOTTOM) != 0) {
			Shape3D shape = new Shape3D();
			shape.setGeometry(buildGeometry(false,
			 btmRadius, topRadius, height));
			addShape(shape);
		}
		if((sideFlags & Elements.SIDE_TOP) != 0) {
			Shape3D shape = new Shape3D();
			shape.setGeometry(buildGeometry(true,
			 btmRadius, topRadius, height));
			addShape(shape);
		}
		
		// set settables
		setPickable(pickable);
		setColor(color);
		setTransparency(trans);
		setTexture(texPath);
	}
	
	public int getShadingModel() {
		return TextureShape.SHADING_NONE;
	}
			
	// personal body ============================================
}