package terrapeer.vui.j3dui.control.inputs;

//import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import javax.swing.Timer;

import terrapeer.vui.j3dui.utils.Debug;

/**
An input drag filter that interprets an input drag as a rate
of output change.  This filter should only be used with sensors
monitoring the same display canvas otherwise its operation is
undefined.

@author Jon Barrilleaux,
copyright (c) 1999 Jon Barrilleaux,
All Rights Reserved.
*/

public class RateInputDragFilter implements InputDragTarget {
	
	// public interface =========================================

	/**
	Constructs a RateInputDragFilter with event target <target>.
	@param target Event target.  Never null.
	*/
	public RateInputDragFilter(InputDragTarget target) {
		if(target==null) throw new
		 IllegalArgumentException("'target' is null.");
		_eventTarget = target;

		_timer = new Timer((int)(_period*1000),
		 new ActionListener() {
		 
			public void actionPerformed(ActionEvent evt) {
				// increment output position by delta
		 		_targetPos.add(_targetDelta);
 
if(Debug.getEnabled()){		
Debug.println("RateInputDragFilter.timer",
"TIMER:RateInputDragFilter:actionPerformed:" +
" targetPos=" + _targetPos);}

				getEventTarget().doInputDrag(_source, _targetPos);
			}    
		});
	}

	/**
	Gets the event target.
	@return Reference to the event target.
	*/
	public InputDragTarget getEventTarget() {
		return _eventTarget;
	}

	/**
	Sets the shape of the rate curve, which is a gamma
	function defined by toOutputRate().
	@param gamma Rate curve gamma factors.  Must be >0.
	Defaults to (1, 1).
	@return Reference to gamma.
	*/
	public Vector2d setCurve(Vector2d gamma) {
		if(gamma.x<=0.0) throw new
		 IllegalArgumentException("'gamma.x' is <= 0.");
		if(gamma.y<=0.0) throw new
		 IllegalArgumentException("'gamma.y' is <= 0.");
		 
		_gamma.set(gamma);
		return _gamma;
	}

	// InputDragTarget implementation
	
	public void startInputDrag(Canvas3D source, Vector2d pos) {

		// minimal setup, wait for doInputDrag	 
		_source = source;
		_targetPos.set(0, 0);
 
if(Debug.getEnabled()){		
Debug.println("RateInputDragFilter",
"RATE:RateInputDragFilter:startInputDrag:" +
" pos=" + pos +
" source=" + _source +
" targetPos=" + _targetPos);}
		
		_eventTarget.startInputDrag(source, _targetPos);
		_timer.start();
	}
	
	public void doInputDrag(Canvas3D source, Vector2d pos) {

		_source = source;
		_targetDelta.set(
		 toOutputRate(pos.x, _gamma.x) * _period,
		 toOutputRate(pos.y, _gamma.y) * _period);
 
if(Debug.getEnabled()){		
Debug.println("RateInputDragFilter",
"RATE:RateInputDragFilter:doInputDrag:" +
" pos=" + pos +
" source=" + _source +
" gamma=" + _gamma +
" period=" + _period +
" targetDelta=" + _targetDelta +
" targetPos=" + _targetPos);}
	}
	
	public void stopInputDrag(Canvas3D source, Vector2d pos) {
 
if(Debug.getEnabled()){		
Debug.println("RateInputDragFilter",
"RATE:RateInputDragFilter:stopInputDrag:" +
" pos=" + pos +
" source=" + _source +
" targetDelta=" + _targetDelta +
" targetPos=" + _targetPos);}
		
		_timer.stop();
		_eventTarget.stopInputDrag(source, _targetPos);
	}
			
	// personal body ============================================
	
	/** Event target. */
	private InputDragTarget _eventTarget;
	
	/** The rate curve gamma factors.  Greater than 0. */
	private Vector2d _gamma = new Vector2d(1, 1);
	
	/** Rate timer. */
	private Timer _timer;
	
	/** Time between output updates (sec). */
	private final double _period = 1.0/30;
	
	/** Last source. */
	private Canvas3D _source;
	
	/** The delta for target position updates. */
	private Vector2d _targetDelta = new Vector2d();
	
	/** The last target pos. */
	private Vector2d _targetPos = new Vector2d();
	
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
	protected double toOutputRate(double pos, double gamma) {

		double sgn = (pos>0.0) ? 1.0 : ((pos<0.0) ? -1.0 : 0.0);
		return sgn * (Math.pow(Math.abs(pos), 1.0/gamma));
	}
	
}