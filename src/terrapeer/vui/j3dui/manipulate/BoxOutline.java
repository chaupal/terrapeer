package terrapeer.vui.j3dui.manipulate;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.feedback.elements.*;

/**
Creates a box outline (wireframe bounding box) texture shape
(see TextureShape).  A "margin" can be specified so that the
outline is offset from around the object that it is bounding.
<P>
The texture is wrapped around the outline with the left vertical
edge of the texture image corresponding to the (-X, -Y) box
edge, continuing around to the (-X, +Z) edge, and ending with
the right image edge back at the (-X, -Z) box edge.

@see TextureShape

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class BoxOutline extends TextureShape {
 
 	// public constants =========================================
	 
	// standard interactive state colors
	public static final Color3f OUTLINE_NORMAL =
	 new Color3f(.75f, .75f, 1f);
	public static final Color3f OUTLINE_OVER =
	 new Color3f(.5f, 1f, 1f);
	public static final Color3f OUTLINE_DRAG =
	 new Color3f(1f, 1f, 1f);
	
	public static final Color3f OUTLINE_DEFAULT =
	 new Color3f(1f, 1f, 1f);
		
	// public utilities =========================================

	/**
	Builds the geometry for a box outline shape.
	@param min Minimum extent of the shape.
	@param max Maximum extent of the shape.
	@param margin The amount added to all extents.
	@return A new shape geometry.  Never null.
	*/
	public static Geometry buildGeometry(Tuple3d min,
	 Tuple3d max, double margin) {
			
if(Debug.getEnabled()){		
Debug.println("BoxOutline",
"BoxOutline:buildGeometry:" +
" min=" + min +
" max=" + max +
" margin=" + margin);}
			 
		// build geometry
		IndexedLineStripArray array =
		 new IndexedLineStripArray(8,
		 GeometryArray.COORDINATES |
		 GeometryArray.TEXTURE_COORDINATE_2, 18,
		 new int[] {5, 5, 2, 2, 2, 2});
		
		array.setCoordinates(0, new double[] {
		 // bottom
		 min.x-margin, min.y-margin, min.z-margin,
		 min.x-margin, min.y-margin, max.z+margin,
		 max.x+margin, min.y-margin, max.z+margin,
		 max.x+margin, min.y-margin, min.z-margin,
		 // top
		 min.x-margin, max.y+margin, min.z-margin,
		 min.x-margin, max.y+margin, max.z+margin,
		 max.x+margin, max.y+margin, max.z+margin,
		 max.x+margin, max.y+margin, min.z-margin
		});

		float width = (float)(max.x - min.x);
		float depth = (float)(max.z - min.z);
		float perim = 2f * (width + depth);
				
		array.setTextureCoordinates(0, new float[] {
		 // bottom
		 0f, 0f,
		 depth/perim, 0f,
		 (depth+width)/perim, 0f,
		 (depth+width+depth)/perim, 0f,
		 // top
		 0f, 1f,
		 depth/perim, 1f,
		 (depth+width)/perim, 1f,
		 (depth+width+depth)/perim, 1f
		});
		
		array.setCoordinateIndices(0, new int[] {
		 // bottom
		 0, 1, 2, 3, 0,
		 // top
		 4, 5, 6, 7, 4,
		 // corners
		 0, 4,
		 1, 5,
		 2, 6,
		 3, 7
		});
		array.setTextureCoordinateIndices(0, new int[] {
		 // bottom
		 0, 1, 2, 3, 0,
		 // top
		 4, 5, 6, 7, 4,
		 // corners
		 0, 4,
		 1, 5,
		 2, 6,
		 3, 7
		});
		
		return array;
	}
	
	// public interface =========================================

	/**
	Constructs a BoxOutline with the specified attributes.
	By default the object will not be pickable, will be default
	colored, will not be transparent, and will not be textured.
	*/
	public BoxOutline(Tuple3d min, Tuple3d max, double margin) {
		this(false, OUTLINE_DEFAULT, -1, null, min, max, margin);
	}

	/**
	Constructs a BoxOutline with the specified attributes.
	@param pickable If true the object will be pickable.
	@param color Object intrinsic color.  Null for white.
	@param trans Object transparency (0 - 1).  If out of range
	transparency is disabled.
	@param texPath The path of the file containing the texture
	image.  Null if none.
	@param min Minimum extent of the shape.
	@param max Maximum extent of the shape.
	@param margin The amount added to all extents.
	*/
	public BoxOutline(boolean pickable, Color3f color,
	 double trans, String texPath, Tuple3d min, Tuple3d max,
	 double margin) {
	
		// build shape
		Shape3D shape = new Shape3D();
		shape.setGeometry(buildGeometry(min, max, margin));
		addShape(shape);
		
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