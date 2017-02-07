package org.usfirst.frc.team3256.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class Constants {

	// PWM Motor
	public static final int LEFT_FRONT_DRIVE = 9;
	public static final int LEFT_BACK_DRIVE = 8;
	public static final int RIGHT_FRONT_DRIVE = 7;
	public static final int RIGHT_BACK_DRIVE = 6;
	public static final int HANGER_1 = 4;
	public static final int HANGER_2 = 5;
	public static final int INNER_MOTOR_ROLLER = 3;
	public static final int OUTER_MOTOR_ROLLER = 2;

	// PCM
	public static final int DRIVE_SHIFTER_A = 0;
	public static final int DRIVE_SHIFTER_B = 1;
	public static final int BALL_PIVOT_A = 4;
	public static final int BALL_PIVOT_B = 5;
	public static final int GEAR_PIVOT_A = 6;
	public static final int GEAR_PIVOT_B = 7;

	// Encoder
	public static final int ENCODER_LEFT_A = 3;
	public static final int ENCODER_LEFT_B = 2;
	public static final int ENCODER_RIGHT_A = 1;
	public static final int ENCODER_RIGHT_B = 0;

	// Joystick
	public static final int DRIVER_CONTROLLER = 0;
	public static final int MANIPULATOR_CONTROLLER = 1;
	public static final double DEADBAND_VALUE = 0.25;
}
