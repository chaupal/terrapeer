package terrapeer.gui;

/**
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P feedback system</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */

import java.awt.*;
import javax.swing.*;
import com.l2fprod.gui.plaf.skin.*;
import java.io.*;

public class SkinLNF
{
  public SkinLNF(java.awt.Container parent, String selectedItem)
  {
    try
    {
      if (selectedItem.endsWith(".xml"))
      {
        SkinLookAndFeel.setSkin(SkinLookAndFeel.loadThemePackDefinition(
            SkinUtils.toURL(new File(selectedItem))));
      }
      else
      {
        SkinLookAndFeel.setSkin(SkinLookAndFeel.loadThemePack(selectedItem));
      }
      SkinLookAndFeel.enable();

      /*
           Component c = SwingUtilities.getAncestorOfClass(java.awt.Window.class, main);
                               if (c == null) {
              c = SwingUtilities.getAncestorOfClass(main.class, main);
                               }
                               if (c != null) {
              SwingUtilities.updateComponentTreeUI(c);
                               }
       */
      SwingUtilities.updateComponentTreeUI(parent);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    // end of try-catch
  }
}
