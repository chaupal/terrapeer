package terrapeer;

/**
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P feedback system</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */

import java.util.*;
import java.awt.event.*;
import javax.swing.*;

import terrapeer.*;
import terrapeer.gui.*;
import terrapeer.vui.*;

/**
 * Logger Class
 *
 * Log Levels
 *    0 = EXCEPTION (total crash)
 *    1 = SERIOUS ERROR (need to shutdown)
 *    2 = ERROR (not good, but can continue)
 *    4 = INFORMATIVE (just FYI)
 *    8 = DEBUG
 *
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P feedback system</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */
public class TerraPeerLog
{
  private static TerraPeerLog theLog = null;
  private static logItem[] logList = new logItem[vars.LOGLIST_SIZE];
  private static int lastCount = 0;
  public static LogBox logWindow = new LogBox();
  public static JProgressBar jProgressBar1 = new JProgressBar();

  public TerraPeerLog()
  {
  }

  public void setLogWindow(LogBox logWindow)
  {
    this.logWindow = logWindow;
  }

  public boolean makeNewLog()
  {
    if(this.theLog==null)
    {
      this.theLog = new TerraPeerLog();
      for(int i=0; i<vars.LOGLIST_SIZE; i++)
        logList[i] = new logItem();
    }
    else
      return false;
    return true;
  }

  public static TerraPeerLog getLogger()
  {
    return theLog;
  }

  public class logItem
  {
    public int logID = -1;
    public int level;
    public String message;
    public Calendar date;
  }

  public class progressStatus
  {
    public int status;
    public int counter;
    public int counter_max;
    public Calendar startTime;
    private javax.swing.Timer t;
    int delay = 1000; //milliseconds
    ActionListener taskPerformer = new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        updateProgress();
      }
    };

    public void startProgress()
    {
      status = 0;
      counter = 0;
      counter_max = 100;
      startTime = Calendar.getInstance();
      t = new javax.swing.Timer(delay, taskPerformer);
      t.start();
    }

    public void stopProgress()
    {
      t.stop();
    }

    public void updateProgress()
    {
      if(counter>=counter_max) counter = 0;
      jProgressBar1.setValue(counter++);
    }

    public Date timeUsage()
    {
      int h = Calendar.getInstance().get(Calendar.HOUR) - startTime.get(Calendar.HOUR);
      int m = Calendar.getInstance().get(Calendar.MINUTE) - startTime.get(Calendar.MINUTE);
      int s = Calendar.getInstance().get(Calendar.SECOND) - startTime.get(Calendar.SECOND);
      return (new Date(0, 0, 0, h, m, s));
    }
  }

  public void clearLog()
  {
    lastCount = 0;
    for(int i=0; i<vars.LOGLIST_SIZE; i++)
      logList[i] = new logItem();
    updateWindow(true);
  }

  public void addMessage(int level, String message)
  {
    logList[lastCount].logID = lastCount;
    logList[lastCount].level = level;
    logList[lastCount].message = message;
    logList[lastCount].date = Calendar.getInstance();
    lastCount++;
    updateWindow(false);
  }

  public void updateWindow(boolean all)
  {
    //throw out on console as well
    if(vars.LOG_TO_CONSOLE)
      System.err.println("LOG: ["+logList[lastCount-1].date.getTime().toString()+"] <"+logList[lastCount-1].level+"> "+logList[lastCount-1].message);

    if(vars.LOGLIST_SIZE-23 == lastCount)
      addMessage(2, "LOG-WARNING: Only 20 log-lines left before cleanup!");
    if(vars.LOGLIST_SIZE-3 == lastCount)
      addMessage(2, "LOG-WARNING: Cleaning up Log Now!");

    if(all)
      logWindow.setMessages(logList);
    else
      logWindow.addMessage(logList[lastCount-1]);

    if(vars.LOGLIST_SIZE-2 == lastCount)
      clearLog();
  }

}