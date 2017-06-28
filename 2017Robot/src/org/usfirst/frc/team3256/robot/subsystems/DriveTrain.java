package org.usfirst.frc.team3256.robot.subsystems;

import org.usfirst.frc.team3256.lib.ADXRS453_Gyro;
import org.usfirst.frc.team3256.lib.Log;
import org.usfirst.frc.team3256.lib.PDP;
import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.commands.TeleopDrive;
import org.usfirst.frc.team3256.robot.commands.TeleopDrive.TeleopDriveMode;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain extends Subsystem implements Log {
	
	//Singleton Instance of the DriveTrain Subsystem
	private static DriveTrain instance;
	
	private VictorSP leftDrive;
	private VictorSP rightDrive;
	private Encoder encoderLeft;
	private Encoder encoderRight;
	private ADXRS453_Gyro gyro;
	private DoubleSolenoid shifter;
	PDP pdp = PDP.getInstance();
	
	/**
	 * Cannot be instantiated out of the class, so we will always only have one DriveTrain instance
	 * Use the getInstance() method
	 */
	private DriveTrain() {
		//VictorSP motor controllers for the drivetrain motors
		leftDrive = new VictorSP(Constants.LEFT_DRIVE);
		rightDrive = new VictorSP(Constants.RIGHT_DRIVE);
		//set motor directions
		leftDrive.setInverted(true);
		rightDrive.setInverted(false);
		//Encoders for the left and right side of the drivetrain
		encoderLeft = new Encoder(Constants.ENCODER_LEFT_A, Constants.ENCODER_LEFT_B);
		encoderRight = new Encoder(Constants.ENCODER_RIGHT_A, Constants.ENCODER_RIGHT_B);
		//set the ticks/inch for the encoders
		encoderLeft.setDistancePerPulse(Constants.INCHES_PER_TICK);
		encoderRight.setDistancePerPulse(Constants.INCHES_PER_TICK);
		//set encoder directions
		encoderLeft.setReverseDirection(false);
		encoderRight.setReverseDirection(true);
		//Gyro for the drivetrain
		gyro = new ADXRS453_Gyro();
		//Shifter to shift between high and low gear
		shifter = new DoubleSolenoid(Constants.DRIVE_SHIFTER_A,Constants.DRIVE_SHIFTER_B);
	}
	
	/**
	 * Sets the default command of the DriveTrain Subsystem, which is the TeleopDrive Command
	 */
	protected void initDefaultCommand() {
		setDefaultCommand(new TeleopDrive(TeleopDriveMode.CHEESY));
	}
	
	/**
	 * @return the singleton instance of the DriveTrain class
	 */
	public static DriveTrain getInstance() {
		return instance == null ? instance = new DriveTrain() : instance;
	}
	
	/**
	 * Logs information about the drivetrain components to the SmartDashboard
	 */
	@Override
	public void logToDashboard(){
		if (!DriverStation.getInstance().isDisabled()){
			SmartDashboard.putNumber("Gyro Angle - SPI 0 ", getAngle());
			SmartDashboard.putNumber("GYRO RATE", getAngularVelocity());
			SmartDashboard.putBoolean("GYRO IS CALIBRATING", false);
		}
		else{
			SmartDashboard.putBoolean("GYRO IS CALIBRATING", true);
		}
		SmartDashboard.putNumber("Left Encoder: MXP- " + Constants.ENCODER_LEFT_A +
				"," + Constants.ENCODER_LEFT_B + " ", getLeftPosition());
		SmartDashboard.putNumber("Right Encoder: MXP- " + Constants.ENCODER_RIGHT_A + 
				"," + Constants.ENCODER_RIGHT_B + " ", getRightPosition());
		SmartDashboard.putNumber("Left PWM: PWM-" + leftDrive.getChannel() + " ", 
				leftDrive.get());
		SmartDashboard.putNumber("Right PWM: PWM-" + rightDrive.getChannel() + " ", 
				rightDrive.get());
		SmartDashboard.putNumber("Left Front Current: PDP-" + Constants.PDP_LEFT_FRONT + " ", 
				pdp.getCurrent(Constants.PDP_LEFT_FRONT));
		SmartDashboard.putNumber("Left Back Current: PDP-" +  Constants.PDP_LEFT_BACK + " ",
				pdp.getCurrent(Constants.PDP_LEFT_BACK));
		SmartDashboard.putNumber("Right Front Current: PDP-" + Constants.PDP_RIGHT_FRONT + " ", 
				pdp.getCurrent(Constants.PDP_RIGHT_FRONT));
		SmartDashboard.putNumber("Right Back Current: PDP-" + Constants.PDP_RIGHT_BACK + " ",
				pdp.getCurrent(Constants.PDP_RIGHT_BACK));
		SmartDashboard.putNumber("Left Velocity", getLeftVelocity());
		SmartDashboard.putNumber("Right Velocity", getRightVelocity());
	}
	
	public int getRawLeftTicks(){
		return encoderLeft.get();
	}

	public int getRawRightTicks(){
		return encoderRight.get();
	}
	/**
	 * @param power - Sends the given power to run the left drive motors in open loop mode
	 */
	public void setLeftMotorPower(double power) {
		leftDrive.set(power);
	}
	
	/**
	 * @param power - Sends the given power to run the right drive motors in open loop mode
	 */
	public void setRightMotorPower(double power) {
		rightDrive.set(power);
	}
	
	/**
	 * @param wantsHighGear - Shifts the robot to high gear if we wantsHighGear is true, otherwise it shifts the robot to low gear
	 */
	public void shiftUp(boolean wantsHighGear){
		shifter.set(wantsHighGear?DoubleSolenoid.Value.kForward:DoubleSolenoid.Value.kReverse);
	}
	
	/**
	 * @return The current traveled distance of the left drive encoder since the last reset
	 */
	public double getLeftPosition() {
		return encoderLeft.getDistance();
	}
	
	/**
	 * @return The current traveled distance of the right drive encoder since the last reset in inches
	 */
	public double getRightPosition() {
		return encoderRight.getDistance();
	}
	
	/**
	 * @return The average traveled distances of the left and right drive encoders since their last reset in inches
	 */
	public double getAveragePosition() {
		return (getLeftPosition() + getRightPosition()) / 2;
	}
	
	/**
	 * @return The current velocity of the left side of the drive from the left encoder in inches/second 
	 */
	public double getLeftVelocity(){
		return encoderLeft.getRate();
	}
	
	/**
	 * @return The current velocity of the right side of the drive from the right encoder in inches/second
	 */
	public double getRightVelocity(){
		return encoderRight.getRate();
	}
	
	/**
	 * @return The average velocity of the left and right sides from the encoders in inches/second
	 */
	public double getAverageVelocity(){
		return (getLeftVelocity() + getRightVelocity()) / 2;
	}
	
	/**
	 * Resets both the left and right encoders
	 */
	public void resetEncoders() {
		encoderLeft.reset();
		encoderRight.reset();
	}

	public double degreeToInches(double degrees){
		return degrees/360.0*Constants.ROBOT_CIRCUMFERENCE;
	}
	
	public ADXRS453_Gyro getGyro(){
		return gyro;
	}
	
	/**
	 * Calibrates the ADXRS453 Gyro on the Spartan Board
	 */
	public void calibrateGyro() {
		gyro.calibrate();
	}
	
	/**
	 * Sets the current yaw of the gyro to zero
	 */
	public void resetGyro() {
		gyro.reset();
	}
	
	/**
	 * @return the current angle in degrees of the robot from the gyro since the last reset
	 */
	public double getAngle() {
		return gyro.getAngle();
	}
	
	/**
	 * @return the angular velocity of the robot, in degrees per second
	 */
	public double getAngularVelocity(){
		return gyro.getRate();
	}
	
	/**
	 * @param left - The left throttle value
	 * @param right - The right throttle value
	 * @param wantsReverse - If true, reverse the front and back of the robot
	 * Tank Drive allows direct control of the left and right drives of the robot
	 */
	public void tankDrive(double left, double right, boolean wantsReverse){
		if (Math.abs(left) < Constants.XBOX_DEADBAND_VALUE) left = 0;
		if (Math.abs(right) < Constants.XBOX_DEADBAND_VALUE) right = 0;
		left = Math.max(-1, Math.min(1, left));
		right = Math.max(-1, Math.min(1, right));
		if (wantsReverse){
			left *= -1;
			right *= -1;
		}
		setLeftMotorPower(left);
		setRightMotorPower(right);
	}
	
	/**
	 * @param throttle - The throttle value
	 * @param turn - The turn or wheel value
	 * @param wantsReverse - If true, reverse the front and back of the robot
	 */
	public void arcadeDrive(double throttle, double turn, boolean wantsReverse){
		throttle = Math.pow(throttle, 3);
		turn = Math.pow(turn, 3);
		if (wantsReverse) throttle *= -1;
		if (Math.abs(throttle) < Constants.XBOX_DEADBAND_VALUE) throttle = 0;
		if (Math.abs(turn) < Constants.XBOX_DEADBAND_VALUE) turn = 0;
		double left = throttle + turn;
		double right = throttle - turn;
		left = Math.max(-1, Math.min(1, left));
		right = Math.max(-1, Math.min(1, right));
		setLeftMotorPower(left);
		setRightMotorPower(right);
	}
}
