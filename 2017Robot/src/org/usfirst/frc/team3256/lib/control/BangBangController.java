package org.usfirst.frc.team3256.lib.control;

/**
 * BangBangController to make fast drives, where accuracy is not a primary concern,
 * such as driving across the baseline
 */
public class BangBangController {

	private double output;
	private double setpoint, error;
	private boolean started = false;
	private double tolerance;
	
	/**
	 * @param setpoint the setpoint or goal
	 * @param output the constant motor power to be set
	 */
	public BangBangController(double setpoint, double output){
		this.setpoint = setpoint;
		this.output = output;
		tolerance = 0.5;
	}
	
	/**
	 * @param setpoint the setpoint or goal
	 * The power is set to 1, the maximum
	 */
	public BangBangController(double setpoint){
		this(setpoint, 1.0);
	}
	
	/**
	 * @param tolerance the tolerance for isFinished()
	 */
	public void setTolerance(double tolerance){
		this.tolerance = tolerance;
	}

	/**
	 * @param current the current position
	 * @return the motor output determined by the motor position
	 */
	public double update(double current){
		started = true;
		error = setpoint-current;
		if (error > 0) return output;
		else if (error < 0) return -output;
		else return 0;
	}
	
	/**
	 * @return true when the error is less than the tolerance and the controller has started updating
	 */
	public boolean isFinished(){
		return started && error <= tolerance;
	}
}
