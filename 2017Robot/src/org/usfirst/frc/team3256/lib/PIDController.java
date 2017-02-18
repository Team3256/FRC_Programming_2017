package org.usfirst.frc.team3256.lib;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PIDController {
	
	private double kP, kI, kD;
	private double error, sumError, changeError, prevError, output;
	private double setpoint, tolerance, maxPower, minPower;
	private boolean started = false;
	
	/**
	 * Sets the tolerance to zero, the minimum output to 0, and the maximum power to 1
	 * @param kP - The P gain for the PIDController
	 * @param kI - The I gain for the PIDController
	 * @param kD - The D gain for the PIDController
	 */
	public PIDController(double kP, double kI, double kD){
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
		tolerance = 0.0;
		maxPower = 1.0;
		minPower = 0.0;
		reset();
	}
	
	/**
	 * @param setpoint or goal for the robot to attempt to travel to
	 */
	public void setSetpoint(double setpoint){
		this.setpoint = setpoint;
	}
	
	/**
	 * @param tolerance - The tolerance of isFinished.
	 * Units is whatever is being controlled by the PID
	 * setTolerance(1) while doing PID on the gyro will finish when robot is within 1 degrees of target
	 */
	public void setTolerance(double tolerance){
		this.tolerance = tolerance;
	}
	
	/**
	 * @param minPower the minimum motor power output
	 * @param maxPower the maximum motor power output
	 */
	public void setMinMaxOutput(double minPower, double maxPower){
		this.maxPower = maxPower;
		this.minPower = minPower;
	}
	
	/**
	 * Rests the PIDController and all the processing values associated with it
	 */
	public void reset(){
		setpoint = 0;
		error = 0;
		sumError = 0;
		changeError = 0;
		prevError = 0;	
		output = 0;
	}
	
	/**
	 * @param current - The current sensor input
	 * @return The calculated output based on sensor input
	 * Should be called in a loop such as using the Notifier class, since this uses discrete time
	 */
	public double update(double current){
		//Tell the controller we have started the PIDController
		if (!started) started = true;
		//Calculate the proportional error
		error = setpoint-current;
		//Integrate the error only if we are not saturated to prevent integral windup
		if (kP*Math.abs(error)<maxPower){
			sumError += error;
		}
		//Calculate the derivative of the error
		changeError = error - prevError;
		//Update the previous error for the loop
		prevError = error;
		//Calculate the motor output
		output = kP*error + kI*sumError + kD*changeError;
		//Filter the output to the max and min powers
		if (output < 0) {
			if (output > -minPower) output = -minPower;
			if (output < -maxPower) output = -maxPower;
		}
		else {
			if (output < minPower) output = minPower;
			if (output > maxPower) output = maxPower;
		}
		//Return the filtered output
		return output;
	}
	
	/**
	 * @return True after the error is less than the tolerance and the controller has started
	 */
	public boolean isFinished() {
		return started && Math.abs(error) <= tolerance;
	}

	/**
	 * Logs the error and other variables to the dashboard to be plotted to help the tuning
	 */
	public void logToDashboard() {
		SmartDashboard.putNumber("PID ERROR" , error);
		SmartDashboard.putNumber("Output", output);
		SmartDashboard.putBoolean("ISFINISHED", isFinished());
	}
}
