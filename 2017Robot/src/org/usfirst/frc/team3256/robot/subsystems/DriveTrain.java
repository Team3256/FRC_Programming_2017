package org.usfirst.frc.team3256.robot.subsystems;

import org.usfirst.frc.team3256.robot.RobotMap;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DriveTrain extends Subsystem {
	private static DriveTrain instance;
	
	private VictorSP leftFront;
	private VictorSP leftBack;
	private VictorSP rightFront;
	private VictorSP rightBack;
	private Encoder encoderLeft;
	private Encoder encoderRight;
	private ADXRS450_Gyro gyro;
	
	private DriveTrain() {
		leftFront = new VictorSP(RobotMap.LEFT_FRONT_DRIVE);
		leftBack = new VictorSP(RobotMap.LEFT_BACK_DRIVE);
		rightFront = new VictorSP(RobotMap.RIGHT_FRONT_DRIVE);
		rightBack = new VictorSP(RobotMap.RIGHT_BACK_DRIVE);
		encoderLeft = new Encoder(RobotMap.ENCODER_LEFT_A, RobotMap.ENCODER_LEFT_B);
		encoderRight = new Encoder(RobotMap.ENCODER_RIGHT_A, RobotMap.ENCODER_RIGHT_B);
		gyro = new ADXRS450_Gyro();
	}
	
	protected void initDefaultCommand() {

	}
	
	public static DriveTrain getInstance() {
		return instance==null ? new DriveTrain(): instance;
	}
	
	public void setLeftMotorSpeed(double speed) {
		leftFront.set(speed);
		leftBack.set(speed);
	}
	
	public void setRightMotorSpeed(double speed) {
		rightFront.set(speed);
		rightBack.set(speed);
	}
	
	public double getLeftEncoder() {
		return encoderLeft.get();
	}
	
	public double getRightEncoder() {
		return encoderRight.get();
	}
	
	public double getAverageEncoder() {
		return (getLeftEncoder() + getRightEncoder()) / 2;
	}
	
	public void calibrateGyro() {
		gyro.calibrate();
	}
	
	public void resetGyro() {
		gyro.reset();
	}
	
	public double getGyro() {
		return gyro.getAngle();
	}
}
