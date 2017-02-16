package org.usfirst.frc.team3256.lib;

public class BangBangController {

	private double output;
	private double setpoint, error;
	private boolean started = false;
	private double tolerance;
	
	public BangBangController(double setpoint, double output){
		this.setpoint = setpoint;
		this.output = output;
		tolerance = 2;
	}
	
	public BangBangController(double setpoint){
		this(setpoint,1.0);
	}
	
	public void setTolerance(double tolerance){
		this.tolerance = tolerance;
	}

	public double update(double current){
		started = true;
		error = setpoint-current;
		if (error > 0) return output;
		else if (error < 0) return -output;
		else return 0;
	}
	
	public boolean isFinished(){
		return started && error <= tolerance;
	}
}
