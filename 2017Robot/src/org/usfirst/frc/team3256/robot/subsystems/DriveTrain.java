package org.usfirst.frc.team3256.robot.subsystems;

import org.usfirst.frc.team3256.lib.Log;
import org.usfirst.frc.team3256.lib.PDP;
import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.commands.TeleopDrive;
import org.usfirst.frc.team3256.robot.commands.TeleopDrive.TeleopDriveMode;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain extends Subsystem implements Log {
	
	private static DriveTrain instance;
	
	private VictorSP leftFront;
	private VictorSP leftBack;
	private VictorSP rightFront;
	private VictorSP rightBack;
	private Encoder encoderLeft;
	private Encoder encoderRight;
	private ADXRS450_Gyro gyro;
	private DoubleSolenoid shifter;
	PDP pdp = PDP.getInstance();
	
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
		encoderLeft.setDistancePerPulse(Constants.INCHES_PER_TICK);
		encoderRight = new Encoder(Constants.ENCODER_RIGHT_A, Constants.ENCODER_RIGHT_B);
		encoderRight.setDistancePerPulse(Constants.INCHES_PER_TICK);
		gyro = new ADXRS450_Gyro();
		shifter = new DoubleSolenoid(Constants.DRIVE_SHIFTER_A,Constants.DRIVE_SHIFTER_B);
	}
	
	protected void initDefaultCommand() {
		setDefaultCommand(new TeleopDrive(TeleopDriveMode.ARCADE));
	}
	
	public static DriveTrain getInstance() {
		return instance == null ? instance = new DriveTrain() : instance;
	}
	
	@Override
	public void logToDashboard(){
		SmartDashboard.putNumber("Gyro Angle - SPI 0 ", getAngle());
		SmartDashboard.putNumber("Left Encoder: MXP- " + Constants.ENCODER_LEFT_A +
				"," + Constants.ENCODER_LEFT_B + " ", getLeftPosition());
		SmartDashboard.putNumber("Right Encoder: MXP- " + Constants.ENCODER_RIGHT_A + 
				"," + Constants.ENCODER_RIGHT_B + " ", getRightPosition());
		SmartDashboard.putNumber("Left Front: PWM-" + leftFront.getChannel() + " ", 
				leftFront.get());
		SmartDashboard.putNumber("Right Front: PWM-" + rightFront.getChannel() + " ", 
				rightFront.get());
		SmartDashboard.putNumber("Left Back: PWM-" + leftBack.getChannel() + " ", 
				leftBack.get());
		SmartDashboard.putNumber("Right Back: PWM-" + rightBack.getChannel() + " ", 
				rightBack.get());
		SmartDashboard.putNumber("Left Front Current: PDP-" + Constants.PDP_LEFT_FRONT_DRIVE + " ", 
				pdp.getCurrent(Constants.PDP_LEFT_FRONT_DRIVE));
		SmartDashboard.putNumber("Right Front Current: PDP-" + Constants.PDP_RIGHT_FRONT_DRIVE + " ", 
				pdp.getCurrent(Constants.PDP_RIGHT_FRONT_DRIVE));
		SmartDashboard.putNumber("Left Back Current: PDP-" + Constants.PDP_LEFT_BACK_DRIVE + " ", 
				pdp.getCurrent(Constants.PDP_LEFT_BACK_DRIVE));
		SmartDashboard.putNumber("Right Back Current: PDP-" + Constants.PDP_RIGHT_BACK_DRIVE + " ", 
				pdp.getCurrent(Constants.PDP_RIGHT_BACK_DRIVE));
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
		shifter.set(wantsHighGear?DoubleSolenoid.Value.kReverse:DoubleSolenoid.Value.kForward);
	}
	
	public double getLeftPosition() {
		return encoderLeft.get();
	}
	
	public double getRightPosition() {
		return encoderRight.get();
	}
	
	public double getAveragePosition() {
		return (getLeftPosition() + getRightPosition()) / 2;
	}
	
	public void resetEncoders() {
		encoderLeft.reset();
		encoderRight.reset();
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
		if (left > 1) left = 1;
		if (left < -1) left = -1;
		if (right > 1) right = 1;
		if (right < -1) right = -1;
		if (wantsReverse){
			left *= -1;
			right *= -1;
		}
		setLeftMotorPower(left);
		setRightMotorPower(right);
	}
	
	public void arcadeDrive(double throttle, double turn, boolean wantsReverse){
		if (wantsReverse) throttle *= -1;
		if (Math.abs(throttle) < Constants.XBOX_DEADBAND_VALUE) throttle = 0;
		if (Math.abs(turn) < Constants.XBOX_DEADBAND_VALUE) turn = 0;
		double left = throttle - turn;
		double right = throttle + turn;
		if (left > 1) left = 1;
		if (left < -1) left = -1;
		if (right > 1) right = 1;
		if (right < -1) right = -1;
		setLeftMotorPower(left);
		setRightMotorPower(right);
	}
}
