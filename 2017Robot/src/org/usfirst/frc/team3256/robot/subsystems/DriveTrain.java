package org.usfirst.frc.team3256.robot.subsystems;

import org.usfirst.frc.team3256.lib.ADXRS453_Gyro;
import org.usfirst.frc.team3256.lib.BangBangController;
import org.usfirst.frc.team3256.lib.DriveSignal;
import org.usfirst.frc.team3256.lib.DriveStraightController;
import org.usfirst.frc.team3256.lib.Log;
import org.usfirst.frc.team3256.lib.Loop;
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

public class DriveTrain extends Subsystem implements Log, Loop {
	
	//Singleton Instance of the DriveTrain Subsystem
	private static DriveTrain instance;
	
	private VictorSP leftDrive;
	private VictorSP rightDrive;
	private Encoder encoderLeft;
	private Encoder encoderRight;
	private ADXRS453_Gyro gyro;
	private DoubleSolenoid shifter;
	PDP pdp = PDP.getInstance();
	private DriveControlMode driveControlMode = DriveControlMode.OPEN_LOOP;
	private DriveStraightController driveStraightController;
	private BangBangController alignController;
	private boolean goForward, turnRight;
	
	public enum DriveControlMode{
		OPEN_LOOP,
		AUTO_ALIGN,
		MOTION_PROFILE,
		SLOW_TURN,
		PATH_FOLLOWING;
	}
	
	@Override
	public void initialize() {
		driveStraightController = null;
		setOpenLoop(0,0);
	}

	@Override
	public void update() {
		switch (driveControlMode){
		case OPEN_LOOP:
			//teleop drive is running as the default command
			return;
		case AUTO_ALIGN:
			break;
		case MOTION_PROFILE:
			updateDriveStraight();
			break;
		case SLOW_TURN:
			
			break;
		case PATH_FOLLOWING:
			break;
		}
	}

	@Override
	public void end() {
		
	}
	
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
	
	public void setDriveStraightSetpoint(double setpoint, boolean goForward){
		this.goForward = goForward;
		if (driveControlMode != DriveControlMode.MOTION_PROFILE){
			driveControlMode = DriveControlMode.MOTION_PROFILE;
		}
		resetEncoders();
		shiftUp(true);
		driveStraightController = new DriveStraightController();
		driveStraightController.setSetpoint(setpoint, !goForward);
	}
	
	public boolean isFinishedDriveStraight(){
        return (driveControlMode == DriveControlMode.MOTION_PROFILE && driveStraightController.isFinished())
        		|| driveControlMode != DriveControlMode.MOTION_PROFILE;
	}
	
	private void updateDriveStraight(){
		DriveSignal signal = driveStraightController.update();
		if (!goForward){
			setLeftMotorPower(-signal.leftMotor);
			setRightMotorPower(-signal.rightMotor);
		}
		else{
			setLeftMotorPower(signal.leftMotor);
			setRightMotorPower(signal.rightMotor);
		}
	}
	
	public void setAlignSetpoint(double setpoint, boolean turnRight){
		this.turnRight = turnRight;
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
	
	public DriveControlMode getDriveMode(){
		return driveControlMode;
	}
	
	/**
	 * @param power - Sends the given power to run the left drive motors in open loop mode
	 */
	public void setLeftMotorPower(double power) {
		leftDrive.set(power);
	}
	
	/**
	 * @param power - Sends the given power to run the sright drive motors in open loop mode
	 */
	public void setRightMotorPower(double power) {
		rightDrive.set(power);
	}
	
	public void setOpenLoop(double left, double right){
		if (driveControlMode != DriveControlMode.OPEN_LOOP){
			driveControlMode = DriveControlMode.OPEN_LOOP;
		}
		setLeftMotorPower(left);
		setRightMotorPower(right);
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

}
