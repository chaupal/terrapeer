package terrapeer.vui.zone;

import terrapeer.*;
import terrapeer.vui.*;
import terrapeer.vui.zone.*;

import java.io.Serializable;


/**
 * The Settlement class manages a Zone's virtual settlement status.
 * The following lists the property variables.
 *
 * State:
 *  SETTLE_NONE
 *  SETTLE_RESERVED
 *  SETTLE_MOVED
 *  SETTLE_SHARED
 *  SETTLE_SETTLED
 *
 * Dates:
 *  Last time Build Zone
 *  Last time Reserved Zone
 *  Last time Moved Zone
 *  Last time Settled Zone
 *  Last time Shared Zone
 *
 * A Move counter
 *  - to calculate settlement priorities for frequent moved
 *  Zones, i.e. unsuccessful reservations
 *
 *
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P feedback system</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */
public class Settlement implements Serializable
{
  private static TerraPeerLog myLog = TerraPeerLog.getLogger();

  private int settleState;
  private java.util.Calendar lastSettleDate;
  private java.util.Calendar lastBuildDate;
  private java.util.Calendar lastMovedDate;
  private java.util.Calendar lastShareDate;
  private java.util.Calendar lastReservationDate;
  private int moveCount;

  public Settlement()
  {
  }
  public int getSettleState()
  {
    return settleState;
  }
  public void setSettleState(int settleState)
  {
    this.settleState = settleState;
  }
  public java.util.Calendar getLastSettleDate()
  {
    return lastSettleDate;
  }
  public void setLastSettleDate(java.util.Calendar lastSettleDate)
  {
    this.lastSettleDate = lastSettleDate;
  }
  public java.util.Calendar getLastBuildDate()
  {
    return lastBuildDate;
  }
  public void setLastBuildDate(java.util.Calendar lastBuildDate)
  {
    this.lastBuildDate = lastBuildDate;
  }
  public java.util.Calendar getLastMovedDate()
  {
    return lastMovedDate;
  }
  public void setLastMovedDate(java.util.Calendar lastMovedDate)
  {
    this.lastMovedDate = lastMovedDate;
  }
  public java.util.Calendar getLastShareDate()
  {
    return lastShareDate;
  }
  public void setLastShareDate(java.util.Calendar lastShareDate)
  {
    this.lastShareDate = lastShareDate;
  }
  public int getMoveCount()
  {
    return moveCount;
  }
  public void setMoveCount(int moveCount)
  {
    this.moveCount = moveCount;
  }
  public java.util.Calendar getLastReservationDate()
  {
    return lastReservationDate;
  }
  public void setLastReservationDate(java.util.Calendar lastReservationDate)
  {
    this.lastReservationDate = lastReservationDate;
  }

}