package terrapeer.vui.j3dui.utils;

/**
 Static methods that provide support for "assertions".  The default
 is for assertions to be enabled.  Use setEnabled() or
 useProperty???() with command line properties to explicitly enable
 and disable it.  All output is printed to System.err.
 @author Jon Barrilleaux,
 copyright (c) 1999 Jon Barrilleaux,
 All Rights Reserved.
 */
public class Assert
{

  // public utilities ========================================================

  /**
    Sets whether or not asserttions are enabled.  If not enabled
    then no exceptions or output is generated.
   */
  public static void setEnabled(boolean enabled)
  {
    _enabled = enabled;
  }

  /**
    If the "assert.enabled" system property is found (its value is
    ignored), subsequent message printing will be enabled.
   */
  public static void usePropertyEnabled()
  {
    String name = "assert.enabled";

    String value = System.getProperty(name);
    if (value != null)
    {
      setEnabled(true);
      System.err.println("Assert: Enabled...");
    }
  }

  /**
    If the "assert.disabled" system property is found (its value is ignored),
    subsequent message printing will be disabled.
   */
  public static void usePropertyDisabled()
  {
    String name = "assert.disabled";

    String value = System.getProperty(name);
    if (value != null)
    {
      setEnabled(false);
      System.err.println("Assert: Disabled...");
    }
  }

  /**
    Prints a stack trace as if assertion failed but does not abort.
   */
  public static void println()
  {
    if (!_enabled)
      return;

    println(false);
  }

  /**
    Prints a stack trace if assertion is false but does not abort.
    @param assertion Test that is asserted to be true.
   */
  public static void println(boolean assertion)
  {
    if (!_enabled)
      return;

    try
    {
      if (!assertion)
        throw new RuntimeException();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
    Prints a message and stack trace as if assertion failed but
    does not abort.
    @param message Printed message.
   */
  public static void println(String message)
  {
    if (!_enabled)
      return;

    println(false, message);
  }

  /**
    Prints a message and stack trace if assertion is false but
    does not abort.
    @param assertion Test that is asserted to be true.
    @param message Printed message.
   */
  public static void println(boolean assertion, String message)
  {
    if (!_enabled)
      return;

    try
    {
      if (!assertion)
        throw new RuntimeException();
    }
    catch (Exception e)
    {
      System.err.println(message);
      e.printStackTrace();
    }
  }

  /**
    Throws a RuntimeException exception as if assertion failed.
   */
  public static void exception() throws RuntimeException
  {
    if (!_enabled)
      return;

    exception(false);
  }

  /**
    Throws a RuntimeException if assertion is false.
    @param assertion Test that is asserted to be true.
   */
  public static void exception(boolean assertion) throws RuntimeException
  {
    if (!_enabled)
      return;

    if (!assertion)
      throw new RuntimeException();
  }

  /**
    Throws a RuntimeException and prints a message as if the
    assertion is false.
    @param message Printed message.
   */
  public static void exception(String message) throws RuntimeException
  {
    if (!_enabled)
      return;

    exception(false, message);
  }

  /**
    Throws a RuntimeException and prints a message if assertion
    is false.
    @param assertion Test that should always be true.
    @param message Printed with exception if assertion fails.
   */
  public static void exception(boolean assertion, String message) throws
      RuntimeException
  {
    if (!_enabled)
      return;

    if (!assertion)
      throw new RuntimeException(message);
  }

  // personal body ===========================================================

  /** True if assertions are enabled. */
  private static boolean _enabled = true;

}
