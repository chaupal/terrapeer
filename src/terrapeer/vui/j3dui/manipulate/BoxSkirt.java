package terrapeer.vui.j3dui.manipulate;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;
import terrapeer.vui.j3dui.feedback.elements.*;

/**
Creates a box skirt shape (see TextureShape) intended for use
as a feeler that projects from the bottom of a host object's
bounding box along the -Y axis.  The "top" side is away from
the Y axis.  The geometry of the box can be set while the object
is live.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class BoxSkirt extends TextureShape {
 
 	// public constants =========================================
	 
	// standard interactive state colors
	public static final Color3f SKIRT_NORMAL =
	 new Color3f(.75f, .75f, 1f);
	public static final Color3f SKIRT_OVER =
	 new Color3f(.5f, 1f, 1f);
	public static final Color3f SKIRT_DRAG =
	 new Color3f(1f, 1f, 1f);
	
	public static final Color3f SKIRT_DEFAULT =
	 new Color3f(1f, 1f, 1f);
		
	// public utilities =========================================

	/**
	Builds the geometry for a box skirt shape.
	@param topSide If true the shape will be the "top" side.
	If false it will be the "bottom" side.
	@param min Minimum extent of the host shape bounding box.
	@param max Maximum extent of the host shape bounding box.
	@param length Length of the skirt.
	@return A new shape geometry.  Never null.
	*/
	public static Geometry buildGeometry(boolean topSide,
	 Tuple3d min, Tuple3d max, double length) {
			
if(Debug.getEnabled()){		
Debug.println("BoxOutline",
"BoxOutline:buildShape:" +
" min=" + min +
" max=" + max +
" length=" + length);}
		
		// build geometry
		IndexedTriangleStripArray array =
		 new IndexedTriangleStripArray(10,
		 GeometryArray.COORDINATES |
		 GeometryArray.TEXTURE_COORDINATE_2, 10,
		 new int[] {10});
		
		array.setCoordinates(0, new double[] {
		 // bottom
		 min.x, min.y-length, min.z,
		 min.x, min.y-length, max.z,
		 max.x, min.y-length, max.z,
		 max.x, min.y-length, min.z,
		 min.x, min.y-length, min.z,
		 // top
		 min.x, min.y, min.z,
		 min.x, min.y, max.z,
		 max.x, min.y, max.z,
		 max.x, min.y, min.z,
		 min.x, min.y, min.z
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
		 1f, 0f,
		 // top
		 0f, 1f,
		 depth/perim, 1f,
		 (depth+width)/perim, 1f,
		 (depth+width+depth)/perim, 1f,
		 1f, 1f
		});
		
		if(topSide) {
			array.setCoordinateIndices(0, new int[] {
			 // left
			 5, 0, 6, 1,
			 // front
			 7, 2,
			 // right
			 8, 3,
			 // back
			 9, 4
			});
			array.setTextureCoordinateIndices(0, new int[] {
			 // left
			 5, 0, 6, 1,
			 // front
			 7, 2,
			 // right
			 8, 3,
			 // back
			 9, 4
			});
		} else {
			array.setCoordinateIndices(0, new int[] {
			 // back
			 9, 4, 8, 3,
			 // right
			 7, 2,
			 // front
			 6, 1,
			 // left
			 5, 0
			});
			array.setTextureCoordinateIndices(0, new int[] {
			 // back
			 9, 4, 8, 3,
			 // right
			 7, 2,
			 // front
			 6, 1,
			 // left
			 5, 0
			});
		}
		
		return array;
	}
	
	// public interface =========================================

	/**
	Constructs a BoxSkirt with the specified attributes.
	By default the object will not be pickable, will be white,
	will not be transparent, and will not be textured.
	@param sideFlags Flags indicating which sides of the object
	to build (Element.SIDE_???).
	@param min Minimum extent of the host shape bounding box.
	@param max Maximum extent of the host shape bounding box.
	@param length Length of the skirt.
	*/
	public BoxSkirt(int sideFlags, Tuple3d min,
	 Tuple3d max, double length) {
	
		this(false, SKIRT_DEFAULT, -1, null,
		 sideFlags, min, max, length); 
	}

	/**
	Constructs a BoxSkirt with the specified color and
	geometry.
	@param pickable If true the skirt is geometry pickable,
	otherwise it is not pickable.
	@param color Object intrinsic color.  Null for white.
	@param trans Object transparency (0 - 1).  If out of range
	transparency is disabled.
	@param texPath The path of the file containing the texture
	image.  Null if none.
	@param sideFlags Flags indicating which sides of the object
	to build (Element.SIDE_???).
	@param min Minimum extent of the host shape bounding box.
	@param max Maximum extent of the host shape bounding box.
	@param length Length of the skirt.
	*/
	public BoxSkirt(boolean pickable, Color3f color,
	 double trans, String texPath, int sideFlags, Tuple3d min,
	 Tuple3d max, double length) {
			
		// build dummy sides with settable geometry
		_sideFlags = sideFlags;
		
		if((sideFlags & Elements.SIDE_BOTTOM) != 0) {
			Shape3D shape = new Shape3D();
			shape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);		
			addShape(shape);
		}
		if((sideFlags & Elements.SIDE_TOP) != 0) {
			Shape3D shape = new Shape3D();
			shape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);		
			addShape(shape);
		}
		
		// set settables (geo first)
		setGeometry(min, max, length);
		
		setPickable(pickable);
		setColor(color);
		setTransparency(trans);
		setTexture(texPath);
	}

	/**
	Sets the skirt geometry, which extends below the specified
	bounding box along -Z.
	@param sideFlags Flags indicating which sides of the object
	to build (Element.SIDE_???).
	@param min Minimum extent of the host shape bounding box.
	@param max Maximum extent of the host shape bounding box.
	@param length Length of the skirt.
	*/
	public void setGeometry(Tuple3d min, Tuple3d max,
	 double length) {

		boolean bottomSet = false;	
		Iterator iter = getShapes().iterator();
		while(iter.hasNext()) {
			Shape3D shape = (Shape3D)iter.next();
			
			if((_sideFlags & Elements.SIDE_BOTTOM) != 0 &&
			 !bottomSet) {
				shape.setGeometry(buildGeometry(false,
				 min, max, length));
				bottomSet = true;
			} else
			if((_sideFlags & Elements.SIDE_TOP) != 0) {
				shape.setGeometry(buildGeometry(true,
				 min, max, length));
			}
		}
	}
	
	public int getShadingModel() {
		return TextureShape.SHADING_NONE;
	}
			
	// personal body ============================================

	/** Which shape sides to build (Elements.SIDE_???). */
	private int _sideFlags;
	
}