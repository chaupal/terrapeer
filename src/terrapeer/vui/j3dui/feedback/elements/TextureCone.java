package terrapeer.vui.j3dui.feedback.elements;

import java.net.*;
import java.awt.*;
import java.awt.image.*;

import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
Creates a conical texture shape (see TextureShape).
<P>
The axis of the cone is the Y axis.  The base is located
at zero along the Y axis.  The apex is located at a
specified height along the +Y axis.  The side towards
+Y is the "top"
<P>
the texture is projected "down" onto the cone.  The center of
the texture corresponds to the peak of the cone. The edge of
the cone corresponds to a circle tangent to the sides of the
texture image.  The texture +X axis is parallel to the cone
+X axis.  Top and bottom side textures will match. If the
texture can not be loaded it is ignored with a warning.

@see TextureShape

@author Jon Barrilleaux,
copyright (c) 1999-2000 Jon Barrilleaux,
All Rights Reserved.
*/

public class TextureCone extends TextureShape {
		
	// public utilities =========================================

	/**
	Builds one side of a texture cone shape with geometry
	only.
	@param topSide If true the shape will be the "top" side.
	If false it will be the "bottom" side.
	@param radius Radius of the cone base.
	@param height Height of the cone from base to peak.
	@return A new shape geometry.  Never null.
	*/
	public static Geometry buildGeometry(boolean topSide,
	 double radius, double height) {
			
if(Debug.getEnabled()){		
Debug.println("TextureCone",
"TextureCone:buildGeometry:" +
" topSide=" + topSide +
" radius=" + radius +
" height=" + height);}

		int facetCount = 12;		
		int vertexCount = facetCount + 2;		
		double angStart = 0;
		double angIncr = 2*Math.PI / facetCount;
		double texIncr = angIncr;
		
		if(topSide) {
			angIncr *= -1;
			texIncr *= -1;
		}
		
		// build geometry
		TriangleFanArray array = new TriangleFanArray(
		 vertexCount, GeometryArray.COORDINATES |
		 GeometryArray.TEXTURE_COORDINATE_2 |
		 GeometryArray.NORMALS,
		 new int[] {vertexCount});
		
		double ang, tex;
		float posX, posZ, texX, texY, oldPosX, oldPosZ;

		/// build cone apex			
		array.setCoordinate(0, new float[] {
		 0f, (float)height, 0f});
		array.setTextureCoordinate(0, new float[] {
		 .5f, .5f});
		array.setNormal(0, new float[] {
		 0f, 1f, 0f});

		/// build rest of cone, by facet			 
		Vector3f parallel = new Vector3f();
		Vector3f tangent = new Vector3f();
		Vector3f normal = new Vector3f();
		
		ang = -angStart;
		tex = 0;
		posX = posZ = 0;
		
		for(int index=1; index<vertexCount; index++) {
			if(index==1) {
				oldPosX = (float)(radius * Math.cos(ang-angIncr));
				oldPosZ = (float)(radius * Math.sin(ang-angIncr));
			} else {
				oldPosX = posX;
				oldPosZ = posZ;
			}
			
			posX = (float)(radius * Math.cos(ang));
			posZ = (float)(radius * Math.sin(ang));
			texX = (float)(Math.cos(tex)/2.0 + 0.5);
			texY = (float)(-Math.sin(tex)/2.0 + 0.5);
		
if(Debug.getEnabled()){		
Debug.println("TextureCone.build",
"BUILD: TextureCone:buildSide:" +
" index=" + index +
" ang=" + ang +
" tex=" + tex +
" pos=" + posX + " " + posZ +
" tex=" + texX + " " + texY
);}

			array.setCoordinate(index, new float[] {
			 posX, (float)0, posZ});
			array.setTextureCoordinate(index, new float[] {
			 texX, texY});

			/// compute normal from cross product			
			parallel.set(-posX, (float)height, -posZ);
			parallel.normalize();
			tangent.set(oldPosX-posX, 0, oldPosZ-posZ);
			tangent.normalize();
			
			normal.cross(parallel, tangent);
			array.setNormal(index, normal);

			/// update increments				
			ang += angIncr;
			tex += texIncr;
		}
		
		return array;
	}
	
	// public interface =========================================

	/**
	Constructs a TextureCone with the specified attributes.
	By default the object will not be pickable, will be white,
	will not be transparent, and will not be textured.
	@param sideFlags Flags indicating which sides of the object
	to build (Element.SIDE_???).
	@param radius Radius of the cone base.
	@param height Height of the cone from base to peak.
	*/
	public TextureCone(int sideFlags, double radius,
	 double height) {
	 
		this(false, null, -1, null,
		 sideFlags, radius, height); 
	}

	/**
	Constructs a TextureCone with the specified attributes.
	@param pickable If true the object will be pickable.
	@param color Object intrinsic color.  Null for white.
	@param trans Object transparency (0 - 1).  If out of range
	transparency is disabled.
	@param texPath The path of the file containing the texture
	image.  Null if none.
	@param sideFlags Flags indicating which sides of the object
	to build (Element.SIDE_???).
	@param radius Radius of the cone base.
	@param height Height of the cone.
	*/
	public TextureCone(boolean pickable, Color3f color,
	 double trans, String texPath, int sideFlags,
	 double radius, double height) {
	
		// build sides
		if((sideFlags & Elements.SIDE_BOTTOM) != 0) {
			Shape3D shape = new Shape3D();
			shape.setGeometry(buildGeometry(false,
			 radius, height));
			addShape(shape);
		}
		if((sideFlags & Elements.SIDE_TOP) != 0) {
			Shape3D shape = new Shape3D();
			shape.setGeometry(buildGeometry(true,
			 radius, height));
			addShape(shape);
		}
		
		// set settables
		setPickable(pickable);
		setColor(color);
		setTransparency(trans);
		setTexture(texPath);
	}
	
	public int getShadingModel() {
		return TextureShape.SHADING_GOURAUD;
	}
			
	// personal body ============================================
		
}