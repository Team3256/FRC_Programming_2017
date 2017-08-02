package org.usfirst.frc.team3256.robot;

public class Constants {

//------------------------------ELECTRICAL PORTS------------------------------
	
	// CAN IDs
	public static final int FLYWHEEL_MASTER = 2;
	public static final int FLYWHEEL_SLAVE = 3;
	
	// Joystick ports
	public static final int DRIVER_CONTROLLER = 0;
	public static final int MANIPULATOR_CONTROLLER = 1;
	
	// Motor ports
	public static final int ROLLER_MOTOR = 3;
	
	//Shooter Constants
	public static final double kShooterF = 0.00832519531;
	public static final double kShooterP = 0.005;
	public static final double kShooterI = kShooterP/100.0;
	public static final double kShooterD = kShooterP*15.0;
	public static final double kGearRatio = 1;
	public static final int kCurrentLimit = 30;
}