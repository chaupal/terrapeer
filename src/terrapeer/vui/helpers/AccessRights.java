package terrapeer.vui.helpers;

/**
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P feedback system</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */

/**
 * Class used for Zone and Objects to provide access rights, groups,
 * and ownership information.
 * Rights are Read, Write, Execute (RWX)
 * <p>Title: TerraPeer</p>
 * <p>Description: P2P feedback system</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Henrik Gehrmann
 * @version 1.0
 */
public class AccessRights
{
  private String ownership;
  private boolean readViewRight;
  private boolean writeChangeRight;
  private boolean executeUseRight;
  private java.util.Vector groups;
  public AccessRights()
  {
  }
  public String getOwnership()
  {
    return ownership;
  }
  public void setOwnership(String ownership)
  {
    this.ownership = ownership;
  }
  public boolean isReadViewRight()
  {
    return readViewRight;
  }
  public void setReadViewRight(boolean readViewRight)
  {
    this.readViewRight = readViewRight;
  }
  public boolean isWriteChangeRight()
  {
    return writeChangeRight;
  }
  public void setWriteChangeRight(boolean writeChangeRight)
  {
    this.writeChangeRight = writeChangeRight;
  }
  public boolean isExecuteUseRight()
  {
    return executeUseRight;
  }
  public void setExecuteUseRight(boolean executeUseRight)
  {
    this.executeUseRight = executeUseRight;
  }
  public java.util.Vector getGroups()
  {
    return groups;
  }
  public void setGroups(java.util.Vector groups)
  {
    this.groups = groups;
  }

}