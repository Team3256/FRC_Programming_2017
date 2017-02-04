package org.usfirst.frc.team3256.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

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
	public static final int JOYSTICK_1 = 0;
	public static final int JOYSTICK_2 = 1;
}
