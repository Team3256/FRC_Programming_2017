package org.usfirst.frc.team9256.robot.subsystems;

import org.usfirst.frc.team9256.lib.ADXRS453_Gyro;
import org.usfirst.frc.team9256.lib.DriveSignal;
import org.usfirst.frc.team9256.lib.Log;
import org.usfirst.frc.team9256.lib.Loop;
import org.usfirst.frc.team9256.lib.PDP;
import org.usfirst.frc.team9256.lib.control.BangBangController;
import org.usfirst.frc.team9256.lib.control.DriveStraightController;
import org.usfirst.frc.team9256.lib.control.TurnInPlaceController;
import org.usfirst.frc.team9256.robot.Constants;
import org.usfirst.frc.team9256.robot.commands.TeleopDrive;
import org.usfirst.frc.team9256.robot.commands.TeleopDrive.TeleopDriveMode;

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
	private DoubleSolenoid shifter;
	PDP pdp = PDP.getInstance();
	private DriveControlMode driveControlMode = DriveControlMode.OPEN_LOOP;
	private DriveStraightController driveStraightController;
	private BangBangController alignController;
	private TurnInPlaceController turnController;
	private boolean goForward, turnRight;
	
	public enum DriveControlMode{
		OPEN_LOOP,
		AUTO_ALIGN,
		TURN_TO_ANGLE,
		DRIVE_STRAIGHT,
		PATH_FOLLOWING;
	}
	
	@Override
	public void initialize() {
		driveStraightController = null;
		alignController = null;
		turnController = null;
		setOpenLoop(0,0);
		resetEncoders();
		shiftUp(true);
	}
	
	@Override
	public void update() {
		switch (driveControlMode){
		case OPEN_LOOP:
			//teleop drive is running as the default command
			return;
		case AUTO_ALIGN:
			updateAlign();
			break;
		case TURN_TO_ANGLE:
			break;
		case DRIVE_STRAIGHT:
			updateDriveStraight();
			break;
		case PATH_FOLLOWING:
			break;
		}
	}

	@Override
	public void end() {
		setOpenLoop(0,0);
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
		leftDrive.setInverted(false);
		rightDrive.setInverted(true);
		//Encoders for the left and right side of the drivetrain
		encoderLeft = new Encoder(Constants.ENCODER_LEFT_A, Constants.ENCODER_LEFT_B);
		encoderRight = new Encoder(Constants.ENCODER_RIGHT_A, Constants.ENCODER_RIGHT_B);
		//set the ticks/inch for the encoders
		encoderLeft.setDistancePerPulse(Constants.INCHES_PER_TICK);
		encoderRight.setDistancePerPulse(Constants.INCHES_PER_TICK);
		//set encoder directions
		encoderLeft.setReverseDirection(false);
		encoderRight.setReverseDirection(true);
		//Shifter to shift between high and low gear
		shifter = new DoubleSolenoid(Constants.DRIVE_SHIFTER_A,Constants.DRIVE_SHIFTER_B);
	}
	
	public void setDriveStraightSetpoint(double setpoint, boolean goForward){
		this.goForward = goForward;
		driveStraightController = new DriveStraightController();
		driveStraightController.setSetpoint(setpoint, !goForward);
		if (driveControlMode != DriveControlMode.DRIVE_STRAIGHT){
			driveControlMode = DriveControlMode.DRIVE_STRAIGHT;
		}
	}
	
	public boolean isFinishedDriveStraight(){
        return (driveControlMode == DriveControlMode.DRIVE_STRAIGHT && driveStraightController.isFinished())
        		|| driveControlMode != DriveControlMode.DRIVE_STRAIGHT;
	}
	
	private void updateDriveStraight(){
		DriveSignal signal = driveStraightController.update();
		if (!goForward){
			setLeftMotorPower(signal.leftMotor);
			setRightMotorPower(signal.rightMotor);
		}
		else{
			setLeftMotorPower(-signal.leftMotor);
			setRightMotorPower(-signal.rightMotor);
		}
	}
	
	public void setAlignSetpoint(double setpoint, boolean turnRight){
		this.turnRight = turnRight;
		if (driveControlMode != DriveControlMode.AUTO_ALIGN){
			driveControlMode = DriveControlMode.AUTO_ALIGN;
		}
		alignController = new BangBangController(setpoint, 0.2);
	}
	
	public boolean isFinishedAlign(){
		return (driveControlMode == DriveControlMode.AUTO_ALIGN && alignController.isFinished())
				|| driveControlMode != DriveControlMode.AUTO_ALIGN;
	}
	
	public void updateAlign(){
		double output = 0; //alignController.update(Math.abs(getAngle()));
		if (turnRight){
			setLeftMotorPower(output);
			setRightMotorPower(-output);
		}
		else{
			setLeftMotorPower(-output);
			setRightMotorPower(output);
		}
	}
	
	public void setTurnSetpoint(double setpoint, boolean turnRight){
		this.turnRight = turnRight;
		if (driveControlMode != DriveControlMode.TURN_TO_ANGLE){
			driveControlMode = DriveControlMode.TURN_TO_ANGLE;
		}
		turnController = new TurnInPlaceController();
		turnController.setSetpoint(setpoint);
	}
	
	public void updateTurn(){
		double output = turnController.update();
		if (turnRight){
			setLeftMotorPower(-output);
			setRightMotorPower(output);
		}
    	else{
    		setLeftMotorPower(output);
    		setRightMotorPower(-output);
    	}
	}
	
	public boolean isTurnFinished(){
		return (driveControlMode == DriveControlMode.TURN_TO_ANGLE && turnController.isFinished() || driveControlMode != DriveControlMode.TURN_TO_ANGLE);
	}
	
	/**
	 * Sets the default command of the DriveTrain Subsystem, which is the TeleopDrive Command
	 */
	protected void initDefaultCommand() {
		setDefaultCommand(new TeleopDrive(TeleopDriveMode.ARCADE));
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
		SmartDashboard.putString("Drive Control Mode", "" + driveControlMode);
		SmartDashboard.putNumber("Left Encoder: MXP- " + Constants.ENCODER_LEFT_A +
				"," + Constants.ENCODER_LEFT_B + " ", getLeftPosition());
		SmartDashboard.putNumber("Right Encoder: MXP- " + Constants.ENCODER_RIGHT_A + 
				"," + Constants.ENCODER_RIGHT_B + " ", getRightPosition());
		SmartDashboard.putNumber("Left PWM: PWM-" + leftDrive.getChannel() + " ", 
				leftDrive.get());
		SmartDashboard.putNumber("Right PWM: PWM-" + rightDrive.getChannel() + " ", 
				rightDrive.get());
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
	 * @return The angle of the robot using encoder positions rather than a gyro
	 */
	public double getEncoderAngle() {
		double sideDifference = getLeftPosition() - getRightPosition();
		return (sideDifference / 2) * 360 / (Constants.ROBOT_TRACK * Math.PI);
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
}