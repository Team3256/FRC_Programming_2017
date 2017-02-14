package org.usfirst.frc.team3256.lib;

public class DrivePWM {
	
	double leftPWM, rightPWM;
	
	public DrivePWM(double leftPWM, double rightPWM) {
		this.leftPWM = leftPWM;
		this.rightPWM = rightPWM;
	}
	
	public double getLeftPWM(){
		return leftPWM;
	}
	
	public double getRightPWM(){
		return rightPWM;
	}
}
