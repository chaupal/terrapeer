package terrapeer.vui.j3dui.utils;

import java.util.*;

import terrapeer.*;

/**
 Static methods that provide support for "debugging" messages.
 The default is for debugging to be disabled.  Use setEnabled()
 or loadPropertyEnabled() with command line "-D" property
 "debug.enabled=true" to enable it.  All output is printed to
 System.err.
 <P>
 Debugging messages can be associated with none or more "tags"
 and optionally an object (typically "this").  For a message
 to be printed its tag and object, if any, must be in
 the "global" tag and object tables.  A null tag or object is
 considered a wild card and will be considered present in its
 respective table.  By default, all objects are printed.
 <P>
 Message tags can be added to the table with addTags() or the
 command line property "debug.tags".  Message objects can be
 added to the table with addObj().  There is no command line
 option to add objects since they are considered runtime
 entities.
 <P>
 In all cases tags are specified as a list of tag tokens
 delimited by whitespace, commas, or semicolons.  Whitespace
 is trimmed.  Ex: "TAG0 TAG1", "TAG0,TAG1", " TAG0, TAG1 "
 would parse into tags "TAG0" and "TAG1".
 <P>
 Although no messages are printed if debug is disabled,
 processing for the message arguments is still performed which
 can adversely affect performance.  As such, all debug print
 statements should be enclosed in an "if(Debug.getEnabled())"
 block.
 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */
public class Debug
{

  // public interface =========================================

  /**
    Sets whether or not debug message printing is enabled.  If
    not enabled then no output is generated.  Accessor methods
    are unaffected.
    @param enabled True to enable printing.
   */
  public static void setEnabled(boolean enabled)
  {
    _enabled = enabled;
    if (_enabled)
      System.out.println("Debug: Enabled...");
    else
      System.out.println("Debug: Disabled...");
  }

  /**
    Gets the debug message printing enable state.
    @return True if debug printing enabled.
   */
  public static boolean getEnabled()
  {
    return _enabled;
  }

  /**
    Sets whether or not all tags are printed.  If not enabled
    then only messages whose tags are in the tag table are
    printed.
    @param enabled True to enable printing for all tags.
    The default is false.
   */
  public static void setAllTags(boolean enabled)
  {
    _allTags = enabled;

    if (_enabled)
      System.err.println("Debug: AllTags Enabled...");
    else
      System.err.println("Debug: AllTags Disabled...");
  }

  /**
    Sets whether or not all objects are printed.  If not enabled
    then only messages whose objects are in the object table are
    printed.
    @param enabled True to enable printing for all objects.  The
    default is true.
   */
  public static void setAllObjs(boolean enabled)
  {
    _allObjs = enabled;

    if (_enabled)
      System.err.println("Debug: AllObjs Enabled...");
    else
      System.err.println("Debug: AllObjs Disabled...");
  }

  /**
    Enables/disables debugging according to the
    "debug.enabled" system property value.
    <UL>
    <LI>true - Debugging is enabled.
    <LI>false - Debugging is disabled.
    </UL>
    @return True if the property was found with a valid value.
   */
  public static boolean loadPropertyEnabled()
  {
    boolean status = false;
    String name = "debug.enabled";
    String value = System.getProperty(name);
    if (value != null)
    {
      if (value.equals("true"))
      {
        setEnabled(true);
        status = true;
      }
      else
      if (value.equals("false"))
      {
        setEnabled(false);
        status = true;
      }
      else
      {
        System.err.println(
            "Debug: Unrecognized value for '" +
            name + "' [" + value + "]");
        status = false;
      }
    }

    return status;
  }

  /**
    Enables/disables printing all tags according to the
    "debug.alltags" system property value.
    <UL>
    <LI>true - All tags printing is enabled.
    <LI>false - All tags printing is disabled.
    </UL>
    @return True if the property was found with a valid value.
   */
  public static boolean loadPropertyAllTags()
  {
    boolean status = false;
    String name = "debug.alltags";
    String value = System.getProperty(name);
    if (value != null)
    {
      if (value.equals("true"))
      {
        setAllTags(true);
        status = true;
      }
      else
      if (value.equals("false"))
      {
        setAllTags(false);
        status = true;
      }
      else
      {
        System.err.println(
            "Debug: Unrecognized value for '" +
            name + "' [" + value + "]");
        status = false;
      }
    }

    return status;
  }

  /**
    Enables/disables printing all objects according to the
    "debug.allobjs" system property value.
    <UL>
    <LI>true - All objects printing is enabled.
    <LI>false - All objects printing is disabled.
    </UL>
    @return True if the property was found with a valid value.
   */
  public static boolean loadPropertyAllObjs()
  {
    boolean status = false;
    String name = "debug.allobjs";
    String value = System.getProperty(name);
    if (value != null)
    {
      if (value.equals("true"))
      {
        setAllObjs(true);
        status = true;
      }
      else
      if (value.equals("false"))
      {
        setAllObjs(false);
        status = true;
      }
      else
      {
        System.err.println(
            "Debug: Unrecognized value for '" +
            name + "' [" + value + "]");
        status = false;
      }
    }

    return status;
  }

  /**
    Adds the message tags specified in the "debug.enabled"
    system property to the tag table.
    @return True if the property was found with a valid value.
   */
  public static boolean loadPropertyTags()
  {
    String name = "debug.tags";

    String value = System.getProperty(name);
    if (value != null)
    {
      System.err.println("Debug: Adding tags [" +
                         value + "]...");
      addTags(value);
      return true;
    }
    return false;
  }

  /**
    Tries to load all Debug system properties.
    @return True if any properties were found with a valid
    value.
   */
  public static boolean loadAllProperties()
  {
    boolean status = false;

    status = loadPropertyEnabled() ? true : status;
    status = loadPropertyAllTags() ? true : status;
    status = loadPropertyAllObjs() ? true : status;
    status = loadPropertyTags() ? true : status;

    return status;
  }

  /**
    Determines if any of the tags in the specified tags string
    are in the tag table.
    @param tags List of message tags.
    @return True if any of the tags are in the tag table.
   */
  public static boolean hasTag(String tags)
  {
    StringTokenizer toker = new StringTokenizer(tags,
                                                _delimiters);
    while (toker.hasMoreTokens())
    {
      if (_tagTable.containsKey(toker.nextToken().trim()))
        return true;
    }
    return false;
  }

  /**
    Adds the specified tags to the tag table.
    @param tags List of message tags to be added.
   */
  public static void addTags(String tags)
  {
    StringTokenizer toker = new StringTokenizer(tags,
                                                _delimiters);
    while (toker.hasMoreTokens())
    {
      _tagTable.put(toker.nextToken().trim(), null);
    }
  }

  /**
    Removes the specified tags from the tag table.
    @param tags List of message tags to be removed.  If a tag
    is not in the table it is ignored.
   */
  public static void removeTags(String tags)
  {
    StringTokenizer toker = new StringTokenizer(tags,
                                                _delimiters);
    while (toker.hasMoreTokens())
    {
      _tagTable.remove(toker.nextToken().trim());
    }
  }

  /**
    Clears the tag table.
   */
  public static void clearTags()
  {
    _tagTable.clear();
  }

  /**
    Determines if the object is in the object table.
    @param obj Message object.
    @return True if object is in the object table.
   */
  public static boolean hasObj(Object obj)
  {
    return _objTable.contains(obj);
  }

  /**
    Adds the specified object to the object table.
    @param obj Object to be added.
   */
  public static void addObj(Object obj)
  {
    if (!hasObj(obj))
      _objTable.add(obj);
  }

  /**
    Removes the specified object from the object table.
    @param tags Message object to be removed.  If an object
    is not in the table it is ignored.
   */
  public static void removeObj(Object obj)
  {
    int index = _objTable.indexOf(obj);
    if (index >= 0)
      _objTable.remove(index);
  }

  /**
    Clears the object table.
   */
  public static void clearObjs()
  {
    _objTable.clear();
  }

  /**
    Prints the specified message, without newline, regardless
    of the tag table contents.
    @param message Debug message.
    @return True if the message was printed.
   */
  public static boolean print(String message)
  {
    if (!_enabled)
      return false;
    return print(null, message);
  }

  /**
    Prints the specified message, with newline, regardless of
    the tag table contents.
    @param message Debug message.
    @return True if the message was printed.
   */
  public static boolean println(String message)
  {
    if (!_enabled)
      return false;
    return println(null, message);
  }

  /**
    Prints the specified message, without newline, if any of
    the specified tags are in the tag table.
    @param tags List of message tags.  Null for all tags.
    @param message Debug message.
    @return True if the message was printed.
   */
  public static boolean print(String tags, String message)
  {
    if (!_enabled)
      return false;

    if (_allTags || tags == null || hasTag(tags))
    {
      System.err.print(message);
      return true;
    }
    else
      return false;
  }

  /**
    Prints the specified message, with newline, if any of
    the specified tags are in the tag table.
    @param tags List of message tags.  Null for all tags.
    @param message Debug message.
    @return True if the message was printed.
   */
  public static boolean println(String tags, String message)
  {
    if (!_enabled)
      return false;

    if (_allTags || tags == null || hasTag(tags))
    {
      System.err.println(message);
      return true;
    }
    else
      return false;
  }

  /**
    Prints the specified message, without newline, if any of
    the specified tags are in the tag table and if the
    object is in the object table.  Also prints the object at
    the end of the message as formatted by Object.toString().
    @param obj Message target object.  Typically "this".
    Null for all targets.
    @param tags List of message tags.  Null for all tags.
    @param message Debug message.
    @return True if the message was printed.
   */
  public static boolean print(Object obj, String tags,
                              String message)
  {
    if (!_enabled)
      return false;

    if ( (_allTags || tags == null || hasTag(tags)) &&
        (_allObjs || obj == null || hasObj(obj)))
    {
      System.err.print(message + " [" + obj + "]");
      return true;
    }
    else
      return false;
  }

  /**
    Prints the specified message, with newline, if any of
    the specified tags are in the tag table.  Also prints
    the object at the end of the message as formatted by
    Object.toString().
    @param obj Message target object.  Typically "this".
    Null for all targets.
    @param tags List of message tags.  Null for all tags.
    @param message Debug message.
    @return True if the message was printed.
   */
  public static boolean println(Object obj, String tags,
                                String message)
  {
    if (!_enabled)
      return false;

    if ( (_allTags || tags == null || hasTag(tags)) &&
        (_allObjs || obj == null || hasObj(obj)))
    {
      System.err.println(message + " [" + obj + "]");
      return true;
    }
    else
      return false;
  }

  // personal body ============================================

  /** Recognized delimiters. */
  private static final String _delimiters =
      new String(" ,;\t\n\r\f");

  /** True if debugging is enabled. */
  private static boolean _enabled = false;

  /** True to print messages for all tags. */
  private static boolean _allTags = false;

  /** True to print messages for all objects. */
  private static boolean _allObjs = true;

  /** Table of message tags.  Messages with tags that are in
    the table will be printed. */
  private static HashMap _tagTable = new HashMap();

  /** Table of message objects.  Messages with objects that
    are in the table will be printed. */
  private static ArrayList _objTable = new ArrayList();

}