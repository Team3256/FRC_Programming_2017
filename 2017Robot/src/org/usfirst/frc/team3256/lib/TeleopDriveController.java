package org.usfirst.frc.team3256.lib;

import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;

public class TeleopDriveController {
	
	private static double mQuickStopAccumulator;
    private static final double kTurnSensitivity = 1.0;
    private static DriveTrain drive = DriveTrain.getInstance();

	/**
	 * @param left - The left throttle value
	 * @param right - The right throttle value
	 * @param wantsReverse - If true, reverse the front and back of the robot
	 * Tank Drive allows direct control of the left and right drives of the robot
	 */
	public static void tankDrive(double left, double right, boolean wantsReverse){
		left = handleDeadband(left, Constants.XBOX_DEADBAND_VALUE);
		right = handleDeadband(right, Constants.XBOX_DEADBAND_VALUE);
		left = limit(left, 1);
		right = limit(right, 1);
		if (wantsReverse){
			left *= -1;
			right *= -1;
		}
		drive.setOpenLoop(left, right);
	}
    
	/**
	 * @param throttle - The throttle value
	 * @param turn - The turn or wheel value
	 * @param wantsReverse - If true, reverse the front and back of the robot
	 */
	public static void arcadeDrive(double throttle, double turn, boolean wantsReverse){
		throttle = Math.pow(throttle, 3);
		turn = Math.pow(turn, 3);
		if (wantsReverse) throttle *= -1;
		throttle = handleDeadband(throttle, Constants.XBOX_DEADBAND_VALUE);
		turn = handleDeadband(turn, Constants.XBOX_DEADBAND_VALUE);
		double left = throttle + turn;
		double right = throttle - turn;
		left = limit(left, 1);
		right = limit(right, 1);
		drive.setOpenLoop(left, right);
	}
    
    public static void cheesyDrive(double throttle, double wheel, boolean isQuickTurn) {
        wheel = handleDeadband(wheel, Constants.XBOX_DEADBAND_VALUE);
        throttle = handleDeadband(throttle, Constants.XBOX_DEADBAND_VALUE);
        double overPower;
        double angularPower;
        if (isQuickTurn) {
        	//if we want a quick turn, set quick stop accumulator and powers
            if (Math.abs(throttle) < 0.4) {
                double alpha = 0.1;
                mQuickStopAccumulator = (1 - alpha) * mQuickStopAccumulator + alpha * limit(wheel, 1.0) * 2;
            }
            overPower = 1.0;
            angularPower = wheel;
        } else {
        	//if we do not want a quick turn, set powers and update quick stop accumulator
            overPower = 0.0;
            angularPower = Math.abs(throttle) * wheel * kTurnSensitivity - mQuickStopAccumulator;
            if (mQuickStopAccumulator > 1) {
                mQuickStopAccumulator -= 1;
            } else if (mQuickStopAccumulator < -1) {
                mQuickStopAccumulator += 1;
            } else {
                mQuickStopAccumulator = 0.0;
            }
        }
        
        //set right and left motor powers
        double leftPwm = throttle + angularPower;
        double rightPwm = throttle - angularPower;
        
        //keep left and right motor powers between -1.0 and 1.0
        if (leftPwm > 1.0) {
            rightPwm -= overPower * (leftPwm - 1.0);
            leftPwm = 1.0;
        } else if (rightPwm > 1.0) {
            leftPwm -= overPower * (rightPwm - 1.0);
            rightPwm = 1.0;
        } else if (leftPwm < -1.0) {
            rightPwm += overPower * (-1.0 - leftPwm);
            leftPwm = -1.0;
        } else if (rightPwm < -1.0) {
            leftPwm += overPower * (-1.0 - rightPwm);
            rightPwm = -1.0;
        }
        
        drive.setOpenLoop(leftPwm, rightPwm);
    }

    public static double handleDeadband(double val, double deadband) {
        return (Math.abs(val) > Math.abs(deadband)) ? val : 0.0;
    }
    
    public static double limit(double v, double limit) {
        return (Math.abs(v) < limit) ? v : limit * (v < 0 ? -1 : 1);
    }
}
