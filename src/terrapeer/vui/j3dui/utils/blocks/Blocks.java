package terrapeer.vui.j3dui.utils.blocks;

import terrapeer.vui.j3dui.utils.blocks.*;

/**
 Contains constants and utilities of general use for the example
 building blocks.
 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved. Edited 2003 by Henrik Gehrmann.
 */

public class Blocks
{

  // public constants =========================================

  /** Resource catalog dir relative to example dirs. */
  public static final String RESOURCE_DIR =
      "classes/terrapeer/objects/";

  // public utilities =========================================

  /**
    Returns the specified file path pre-appended with the path
    to the examples resource directory and with the proper file
    path separator.  The return path is only assured to work from
    an examples directory.
    @param filePath Relative path to a resource file from the
    resource directory (e.g. "catalog/desk.wrl").
    @return Resource file path.
   */
  public static final String buildResourcePath(
      String filePath)
  {

    String path = RESOURCE_DIR + filePath;
    path = path.replace('\\', java.io.File.separatorChar);
    path = path.replace('/', java.io.File.separatorChar);

    return path;
  }

  // personal body ============================================

}