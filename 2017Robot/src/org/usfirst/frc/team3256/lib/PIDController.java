package org.usfirst.frc.team3256.lib;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PIDController {
	
	private double kP, kI, kD;
	private double error, sumError, changeError, prevError, output;
	private double setpoint, tolerance, maxPower, minPower;
	private boolean started = false;
	
	/**
	 * PIDController
	 * @param kP
	 * @param kI
	 * @param kD
	 */
	public PIDController(double kP, double kI, double kD){
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
		tolerance = 2;
		maxPower = 1;
		minPower = 0;
		reset();
	}
	
	/**
	 * setSetpoint()
	 * @param setpoint the goal for the robot to travel to
	 */
	public void setSetpoint(double setpoint){
		this.setpoint = setpoint;
	}
	
	/**
	 * setTolerance
	 * @param tolerance the tolerance of isFinished
	 * units is whatever is being controlled by the PID
	 * setTolerance(5) while doing PID on gyro will finish when robot is within 5 degrees of target
	 */
	public void setTolerance(double tolerance){
		this.tolerance = tolerance;
	}
	
	/**
	 * setMinMaxOutput
	 * @param minPower the minimum motor power that will turn the robot
	 * @param maxPower the maximum motor power that is controllable
	 */
	public void setMinMaxOutput(double minPower, double maxPower){
		this.maxPower = maxPower;
		this.minPower = minPower;
	}
	
	public void reset(){
		setpoint = 0;
		error = 0;
		sumError = 0;
		changeError = 0;
		prevError = 0;	
		output = 0;
	}
	
	/**
	 * update()
	 * must be called in a consistent loop (notifier)
	 * @param current	current sensor input
	 * @return calculated output based on sensor input
	 */
	public double update(double current){
		if (!started) started = true;
		error = setpoint-current;
		if (kP*Math.abs(error)<maxPower){
			sumError += error;
		}
		else 
			sumError = 0;
		changeError = error - prevError;
		prevError = error;
		output = kP*error + kI*sumError + kD*changeError;
		if (output < 0) {
			if (output > -minPower) output = -minPower;
			if (output < -maxPower) output = -maxPower;
		}
		else {
			if (output < minPower) output = minPower;
			if (output > maxPower) output = maxPower;
		}
		return output;
	}
	
	public boolean isFinished() {
		return started && Math.abs(error) <= tolerance;
	}

	public void logToDashboard() {
		SmartDashboard.putNumber("PID ERROR" , error);
		SmartDashboard.putNumber("Output", output);
	}
}
