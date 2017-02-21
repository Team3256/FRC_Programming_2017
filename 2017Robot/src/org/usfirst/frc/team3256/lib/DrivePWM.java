package org.usfirst.frc.team3256.lib;

/**
 * Wrapper class for a DriveTrain PWM Pair, used for driving straight in DriveStraightController
 */
public class DrivePWM {
	
	private double leftPWM, rightPWM;
	
	/**
	 * @param leftPWM the PWM signal to the left side of the drivetrain
	 * @param rightPWM the PWM signal to the right side of the drivetrain
	 */
	public DrivePWM(double leftPWM, double rightPWM) {
		this.leftPWM = leftPWM;
		this.rightPWM = rightPWM;
	}
	
	/**
	 * @return the left PWM signal of the drivetrain
	 */
	public double getLeftPWM(){
		return leftPWM;
	}
	
	/**
	 * @return the right PWM signal of the drivetrain
	 */
	public double getRightPWM(){
		return rightPWM;
	}
}
