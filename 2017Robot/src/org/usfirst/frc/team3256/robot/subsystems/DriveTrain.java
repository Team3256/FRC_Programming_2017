package org.usfirst.frc.team3256.robot.subsystems;

import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.commands.TeleopDrive;
import org.usfirst.frc.team3256.robot.commands.TeleopDrive.TeleopDriveMode;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain extends Subsystem {
	private static DriveTrain instance;
	
	private VictorSP leftFront;
	private VictorSP leftBack;
	private VictorSP rightFront;
	private VictorSP rightBack;
	private Encoder encoderLeft;
	private Encoder encoderRight;
	private ADXRS450_Gyro gyro;
	private DoubleSolenoid shifter;
	
	private DriveTrain() {
		leftFront = new VictorSP(Constants.LEFT_FRONT_DRIVE);
		leftBack = new VictorSP(Constants.LEFT_BACK_DRIVE);
		rightFront = new VictorSP(Constants.RIGHT_FRONT_DRIVE);
		rightBack = new VictorSP(Constants.RIGHT_BACK_DRIVE);
		leftFront.setInverted(true);
		leftBack.setInverted(true);
		rightFront.setInverted(false);
		rightBack.setInverted(false);
		encoderLeft = new Encoder(Constants.ENCODER_LEFT_A, Constants.ENCODER_LEFT_B);
		encoderRight = new Encoder(Constants.ENCODER_RIGHT_A, Constants.ENCODER_RIGHT_B);
		gyro = new ADXRS450_Gyro();
		shifter = new DoubleSolenoid(Constants.DRIVE_SHIFTER_A,Constants.DRIVE_SHIFTER_B);
	}
	
	protected void initDefaultCommand() {
		setDefaultCommand(new TeleopDrive(TeleopDriveMode.ARCADE));
	}
	
	public static DriveTrain getInstance() {
		return instance == null ? instance = new DriveTrain() : instance;
	}
	
	public void logToDashboard(){
		SmartDashboard.putNumber("Gyro Angle - SPI 0 ", getAngle());
		SmartDashboard.putNumber("Left Encoder: - " + Constants.ENCODER_LEFT_A +
				"," + Constants.ENCODER_LEFT_B + " ", getLeftEncoder());
		SmartDashboard.putNumber("Right Encoder: - " + Constants.ENCODER_RIGHT_A + 
				"," + Constants.ENCODER_RIGHT_B + " ", getRightEncoder());
		SmartDashboard.putNumber("Left Front: PWM-" + leftFront.getChannel() + " ", 
				leftFront.get());
		SmartDashboard.putNumber("Right Front: PWM-" + rightFront.getChannel() + " ", 
				rightFront.get());
		SmartDashboard.putNumber("Left Back: PWM-" + leftBack.getChannel() + " ", 
				leftBack.get());
		SmartDashboard.putNumber("Right Back: PWM-" + rightBack.getChannel() + " ", 
				rightBack.get());
	}
	
	public void setLeftMotorPower(double power) {
		leftFront.set(power);
		leftBack.set(power);
	}
	
	public void setRightMotorPower(double power) {
		rightFront.set(power);
		rightBack.set(power);
	}
	
	public void shiftUp(boolean wantsHighGear){
		//TODO: determine which direction is actually high and low gear
		shifter.set(wantsHighGear?DoubleSolenoid.Value.kForward:DoubleSolenoid.Value.kReverse);
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
	
	public double getAngle() {
		return gyro.getAngle();
	}
	
	public void tankDrive(double left, double right, boolean wantsReverse){
		left = processJoystickValue(left);
		right = processJoystickValue(right);
		if (wantsReverse){
			left *= -1;
			right *= -1;
		}
		setLeftMotorPower(left);
		setRightMotorPower(right);
	}
	
	public void arcadeDrive(double throttle, double turn, boolean wantsReverse){
		if (wantsReverse) throttle *= -1;
		double left = throttle - turn;
		double right = throttle + turn;
		left = processJoystickValue(left);
		right = processJoystickValue(right);
		setLeftMotorPower(left);
		setRightMotorPower(right);
	}
	
	public double processJoystickValue(double num){
		if (num < 0){
			//handle deadband
			if (num > -Constants.DEADBAND_VALUE) num = 0;
			//set value lower limit to -1
			if (num < -1) num = -1;
		}
		else if (num > 0){
			//handle deadband
			if (num < Constants.DEADBAND_VALUE) num = 0;
			//set value upper limit to 1
			if (num > 1) num = 1;
		}
		return num;
	}
}
