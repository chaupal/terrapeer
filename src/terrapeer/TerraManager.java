package terrapeer;

/**
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P feedback system</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */

import javax.swing.*;
import javax.swing.UIManager;
import java.awt.*;
import com.l2fprod.gui.plaf.skin.*;

import terrapeer.gui.*;
import terrapeer.vui.*;
import terrapeer.vui.j3dui.utils.Debug;

import net.jxta.peergroup.*;
import net.jxta.exception.*;
public class TerraManager
{
  boolean packFrame = false;

  //static vars variables = new vars();
  //private static final TerraPeerLog logManager = new TerraPeerLog();
  private static TerraPeerLog myLog = new TerraPeerLog();

  //Construct the application
  public TerraManager()
  {

    Debug.setEnabled(true);
    Debug.loadAllProperties();

    if(myLog.makeNewLog())
    {
      myLog = myLog.getLogger();
      myLog.addMessage(4, "Log created. Starting TerraPeer Manager...");
    }
    else
      System.err.println("Could not start logger!");
    terrapeer.gui.TerraPeerGUI mainWindow = new terrapeer.gui.TerraPeerGUI();

    //Validate frames that have preset sizes
    //Pack frames that have useful preferred size info, e.g. from their layout
    if (packFrame)
    {
      mainWindow.pack();
    }
    else
    {
      mainWindow.validate();
    }
    //Center the window
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = mainWindow.getSize();
    if (frameSize.height > screenSize.height)
    {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width)
    {
      frameSize.width = screenSize.width;
    }
    mainWindow.setLocation( (screenSize.width - frameSize.width) / 2,
                      (screenSize.height - frameSize.height) / 2);
    mainWindow.setVisible(true);
  }

  //Main method
  public static void main(String[] args)
  {
    boolean startFailure = false;

    if(args.length!=2)
      startFailure = true;
    try
    {
      vars.APP_HOME_PATH = args[0];
      vars.JXTA_PEER_NAME = args[1];
      vars.IMG_FULL_PATH = vars.APP_HOME_PATH + vars.IMG_FULL_PATH;
    }
    catch (Exception ex)
    {
    }

    if(vars.JXTA_PEER_NAME.equals("GForce_PEER_A"))
    {
      vars.JXTA_HTTP_PORT = 9707;
      vars.JXTA_TCP_PORT = 9708;
    }
    else if(vars.JXTA_PEER_NAME.equals("GForce_PEER_B"))
    {
      vars.JXTA_HTTP_PORT = 9713;
      vars.JXTA_TCP_PORT = 9714;
    }
    else if(vars.JXTA_PEER_NAME.equals("GForce_PEER_C"))
    {
      vars.JXTA_HTTP_PORT = 9715;
      vars.JXTA_TCP_PORT = 9716;
    }
    else
    {
      startFailure = true;
    }

    //System.out.println("Home: "+vars.APP_HOMOE_PATH);

    try
    {
      //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      //UIManager.setLookAndFeel("swing.addon.plaf.threeD.ThreeDLookAndFeel");
      //terrapeer.gui.SkinLNF mySkin = new terrapeer.gui.SkinLNF();
      String theme = "xp";
      if (theme.equals("xp"))
      {
        SkinLookAndFeel slaf = new SkinLookAndFeel();
        slaf.setSkin(SkinLookAndFeel.loadThemePack(vars.APP_HOME_PATH+vars.THEME_PATH));
        //UIManager.setLookAndFeel("com.l2fprod.gui.plaf.skin.SkinLookAndFeel");
        UIManager.setLookAndFeel(slaf);
      }
      //SwingUtilities.updateComponentTreeUI();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    if(startFailure)
      System.out.println("Sorry, wrong arguments!\nPlease use format: TerraManager application-path peer-name\n\n");
    else
      new TerraManager();
  }
}
