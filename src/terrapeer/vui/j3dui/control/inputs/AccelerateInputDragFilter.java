package terrapeer.vui.j3dui.control.inputs;

import java.awt.event.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import javax.swing.Timer;

import terrapeer.vui.j3dui.utils.Debug;

/**
 An input drag filter that interprets an input drag as an
 acceleration of output change, with the new rate of change
 maintained between drags.  Use InputCancelTarget to reset the
 filter (i.e. zero rate of change).  This filter should only be
 used with sensors monitoring the same display canvas otherwise
 its operation is undefined.  Also, downstream mappers must be
 set for cumulative dragging to realize sustained movement of
 the target object.
 <P>
 Unlike other input drag filters, an input drag to this filter
 does not produce a direct output drag.  Instead, the input drag
 controls the acceleration of the output position (rate of the
 rate of change), with the resulting output velocity (rate of
 change) being maintained between input drags.  Also, each output
 position update is implemented as a separate drag rather than as
 a single continuous drag (which is why cumulative dragging must
 be enabled for continuous movement).  This is to assure that
 intuitive mappers correctly reference the current state of any
 source reference nodes controlled by this filter, such as the
 view itself for DRM.  The reason is that actuators, such as
 those controlling the source nodes, work relative to a reference
 actuation state typically established at the start of a drag.
 <P>
 Because of the indirect and accumulative nature of the output
 change, typically limits are needed on the output rate, which
 can be set using setRateClamp().

 @author Jon Barrilleaux,
 copyright (c) 2000 Jon Barrilleaux,
 All Rights Reserved.
 */

public class AccelerateInputDragFilter implements InputDragTarget, InputCancelTarget
{

  // public interface =========================================

  /**
    Constructs a AccelerateInputDragFilter with an event target.
    @param target Event target.  Never null.
   */
  public AccelerateInputDragFilter(InputDragTarget target)
  {
    if (target == null)
    {
      throw new
          IllegalArgumentException("'target' is null.");
    }
    _eventTarget = target;

    _timer = new Timer((int)(_period * 1000),
                       new ActionListener()
    {

      public void actionPerformed(ActionEvent evt)
      {

        if (Debug.getEnabled())
        {
          Debug.println("AccelerateInputDragFilter.timer",
                        "TIMER:AccelerateInputDragFilter:actionPerformed:" +
                        " isActive=" + _isActive +
                        " targetDelta=" + _targetDelta +
                        " source=" + _source);
        }

        // perform a full drag cycle
        _eventTarget.startInputDrag(
            _source, _targetDelta);
        _eventTarget.doInputDrag(
            _source, _targetDelta);
        _eventTarget.stopInputDrag(
            _source, _targetDelta);
      }
    });

    // when output becomes active, start immediately
    _timer.setInitialDelay(0);
  }

  /**
    Gets the event target.
    @return Reference to the event target.
   */
  public InputDragTarget getEventTarget()
  {
    return _eventTarget;
  }

  /**
    Sets the shape of the rate curve, which is a gamma
    function defined by toOutputRate().
    @param gamma Rate curve gamma factors.  Must be >0.
    Defaults to (1, 1).
    @return Reference to gamma.
   */
  public Vector2d setCurve(Vector2d gamma)
  {
    if (gamma.x <= 0.0)
    {
      throw new
          IllegalArgumentException("'gamma.x' is <= 0.");
    }
    if (gamma.y <= 0.0)
    {
      throw new
          IllegalArgumentException("'gamma.y' is <= 0.");
    }

    _gamma.set(gamma);
    return _gamma;
  }

  /**
    Manually activates and deactivates the output.  The initial
    rate should be set before activation.
    @param enable If true, activates the output.
   */
  public void setActive(boolean enable)
  {
    if (enable)
    {
      _isActive = true;
      _timer.start();
    }
    else
    {
      _timer.stop();
      _isActive = false;
    }
  }

  /**
    Sets the output change rate.  Note that the change rate
    will be clamped.
    @param rate Change rate, in output position units per
    second.  Defaults to zero.  Never null.
    @return Reference to 'rate'.
   */
  public Vector2d setRate(Vector2d rate)
  {
    setTargetDelta(rate.x, rate.y);

    return rate;
  }

  /**
    Gets the output change rate.
    @param rate The ouput rate copy.  Never null.
    @return Reference to 'rate'.
   */
  public Vector2d getRate(Vector2d rate)
  {
    rate.x = _targetDelta.x;
    rate.y = _targetDelta.y;

    return rate;
  }

  /**
    Sets the change rate min/max limits for the X dimension.
    @param clamp Change rate value limits (min, max), in output
    position units per second.  Defaults to negative/positive infinity.
    @return Reference to 'clamp'.
   */
  public Vector2d setRateClampX(Vector2d clamp)
  {
    _deltaClampX.set(clamp);
    setTargetDelta(_targetDelta.x, _targetDelta.y);

    return clamp;
  }

  /**
    Sets the change rate min/max limits for the Y dimension.
    @param clamp Change rate value limits (min, max), in output
    position units per second.  Defaults to negative/positive infinity.
    @return Reference to 'clamp'.
   */
  public Vector2d setRateClampY(Vector2d clamp)
  {
    _deltaClampY.set(clamp);
    setTargetDelta(_targetDelta.x, _targetDelta.y);

    return clamp;
  }

  // InputDragTarget implementation

  public void startInputDrag(Canvas3D source, Vector2d pos)
  {

    _source = source;

    if (Debug.getEnabled())
    {
      Debug.println("AccelerateInputDragFilter",
                    "RATE:AccelerateInputDragFilter:startInputDrag:" +
                    " pos=" + pos +
                    " isActive=" + _isActive +
                    " targetDelta=" + _targetDelta +
                    " source=" + _source);
    }

    // if output not active, reset it and make it active
    if (!_isActive)
    {
      setTargetDelta(0, 0);
      setActive(true);
    }
  }

  public void doInputDrag(Canvas3D source, Vector2d pos)
  {

    _source = source;

    // update output position change rate
    setTargetDelta(
        _targetDelta.x + toOutputRate(pos.x, _gamma.x) * _period,
        _targetDelta.y + toOutputRate(pos.y, _gamma.y) * _period
        );

    if (Debug.getEnabled())
    {
      Debug.println("AccelerateInputDragFilter",
                    "RATE:AccelerateInputDragFilter:doInputDrag:" +
                    " pos=" + pos +
                    " gamma=" + _gamma +
                    " period=" + _period +
                    " isActive=" + _isActive +
                    " targetDelta=" + _targetDelta +
                    " source=" + _source);
    }

  }

  public void stopInputDrag(Canvas3D source, Vector2d pos)
  {

    _source = source;

    if (Debug.getEnabled())
    {
      Debug.println("AccelerateInputDragFilter",
                    "RATE:AccelerateInputDragFilter:stopInputDrag:" +
                    " pos=" + pos +
                    " isActive=" + _isActive +
                    " targetDelta=" + _targetDelta +
                    " source=" + _source);
    }

  }

  // InputCancelTarget implementation

  public void setInputCancel()
  {

    if (Debug.getEnabled())
    {
      Debug.println("AccelerateInputDragFilter",
                    "CANCEL:AccelerateInputDragFilter:setInputCancel: " +
                    " isActive=" + _isActive +
                    " targetDelta=" + _targetDelta +
                    " source=" + _source);
    }

    // if output active, stop output
    if (_isActive)
    {
      setActive(false);
    }
  }

  // personal body ============================================

  /** Event target. */
  private InputDragTarget _eventTarget;

  /** The rate curve gamma factors.  Greater than 0. */
  private Vector2d _gamma = new Vector2d(1, 1);

  /** Rate timer. */
  private Timer _timer;

  /** Time between output updates (sec). */
  private final double _period = 1.0 / 30;

  /** Last source. */
  private Canvas3D _source;

  /** True if output update is active. */
  private boolean _isActive = false;

  /** Current target position delta. */
  private Vector2d _targetDelta = new Vector2d();

  /** The min/max X limits for target position delta. */
  private Vector2d _deltaClampX = new Vector2d(
      Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

  /** The min/max Y limits for target position delta. */
  private Vector2d _deltaClampY = new Vector2d(
      Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

  /**
    Computes the output target rate value given the input source
    input position value according to a rate curve that is a
    gamma function of the form:
    <P>
   outval = sgn(inval) * (abs(inval)**(1/gamma))
    <P>
    If gamma is less than 1 then the curve is exponential.  If
    it is greater than 1 then it is logarithmic.
    <P>
    Override to change the transfer function.
    @param pos Input source position.
    @param gamma Rate curve gamma factor.  Greater than 0.
    @return Output target rate.
   */
  protected double toOutputRate(double pos, double gamma)
  {
    double sgn = (pos > 0.0) ? 1.0 : ((pos < 0.0) ? -1.0 : 0.0);
    return sgn * (Math.pow(Math.abs(pos), 1.0 / gamma));
  }

  /**
    Always use this method to set the target delta so that it
    will be clamped.
    @param targetDelta New target delta.
   */
  protected void setTargetDelta(double deltaX, double deltaY)
  {
    _targetDelta.x = Math.max(Math.min(
        deltaX, _deltaClampX.y), _deltaClampX.x);
    _targetDelta.y = Math.max(Math.min(
        deltaY, _deltaClampY.y), _deltaClampY.x);
  }

}
