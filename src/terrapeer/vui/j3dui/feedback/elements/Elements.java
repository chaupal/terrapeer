package terrapeer.vui.j3dui.feedback.elements;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.image.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import terrapeer.vui.j3dui.utils.Debug;

/**
Contains constants and utilities of general use for feedback
elements.
  
@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class Elements {

 	// public constants =========================================
	
	/** Flag for no sides of an element. */
	public static final int SIDE_NONE = 0x01;
	/** Flag for the top side of an element. */
	public static final int SIDE_TOP = 0x02;
	/** Flag for the top side of an element. */
	public static final int SIDE_BOTTOM = 0x04;
	/** Flag for all sides of an element. */
	public static final int SIDE_ALL =
	 SIDE_TOP | SIDE_BOTTOM;
	 	
}