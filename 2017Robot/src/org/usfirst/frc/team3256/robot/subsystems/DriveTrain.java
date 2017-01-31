package org.usfirst.frc.team3256.robot.subsystems;

import org.usfirst.frc.team3256.robot.RobotMap;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DriveTrain extends Subsystem {
	private static DriveTrain instance = new DriveTrain();
	
	private VictorSP leftFront;
	private VictorSP leftBack;
	private VictorSP rightFront;
	private VictorSP rightBack;
	private Encoder encoderLeft;
	private Encoder encoderRight;
	private ADXRS450_Gyro gyro;
	
	private DriveTrain() {
		this.leftFront = new VictorSP(RobotMap.LEFT_FRONT_DRIVE);
		this.leftBack = new VictorSP(RobotMap.LEFT_BACK_DRIVE);
		this.rightFront = new VictorSP(RobotMap.RIGHT_FRONT_DRIVE);
		this.rightBack = new VictorSP(RobotMap.RIGHT_BACK_DRIVE);
		this.encoderLeft = new Encoder(RobotMap.ENCODER_LEFT_A, RobotMap.ENCODER_LEFT_B);
		this.encoderRight = new Encoder(RobotMap.ENCODER_RIGHT_A, RobotMap.ENCODER_RIGHT_B);
		this.gyro = new ADXRS450_Gyro();
	}
	
	protected void initDefaultCommand() {

	}
	
	public static DriveTrain getInstance() {
		return instance;
	}
	
	public void setLeftMotorSpeed(double speed) {
		this.leftFront.set(speed);
		this.leftBack.set(speed);
	}
	
	public void setRightMotorSpeed(double speed) {
		this.rightFront.set(speed);
		this.rightBack.set(speed);
	}
	
	public double getLeftEncoder() {
		return encoderLeft.get();
	}
	
	public double getRightEncoder() {
		return encoderRight.get();
	}
	
	public double getAverageEncoder() {
		return (this.getLeftEncoder() + this.getRightEncoder()) / 2;
	}
	
	public void calibrateGyro() {
		this.gyro.calibrate();
	}
	
	public void resetGyro() {
		this.gyro.reset();
	}
	
	public double getGyro() {
		return this.gyro.getAngle();
	}
}
