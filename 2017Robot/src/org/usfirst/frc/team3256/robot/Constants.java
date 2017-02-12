package org.usfirst.frc.team3256.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class Constants {

	// PWM Motor
	public static final int LEFT_FRONT_DRIVE = 0;
	public static final int LEFT_BACK_DRIVE = 1;
	public static final int RIGHT_FRONT_DRIVE = 2;
	public static final int RIGHT_BACK_DRIVE = 3;
	public static final int HANGER_1 = 6;
	public static final int HANGER_2 = 7;
	public static final int INNER_MOTOR_ROLLER = 4;
	public static final int OUTER_MOTOR_ROLLER = 5;
	
	//PDP
	public static final int PDP_LEFT_FRONT_DRIVE = 0;
	public static final int PDP_LEFT_BACK_DRIVE = 1;
	public static final int PDP_RIGHT_FRONT_DRIVE = 2;
	public static final int PDP_RIGHT_BACK_DRIVE = 3;
	public static final int PDP_HANGER_1 = 4;
	public static final int PDP_HANGER_2 = 5;
	public static final int PDP_INNER_MOTOR_ROLLER = 6;
	public static final int PDP_OUTER_MOTOR_ROLLER = 7;

	// PCM
	public static final int DRIVE_SHIFTER_A = 0;
	public static final int DRIVE_SHIFTER_B = 7;
	public static final int BALL_PIVOT_A = 2;
	public static final int BALL_PIVOT_B = 3;
	public static final int GEAR_PIVOT_A = 4;
	public static final int GEAR_PIVOT_B = 5;

	// Encoder
	public static final int ENCODER_LEFT_A = 13;
	public static final int ENCODER_LEFT_B = 12;
	public static final int ENCODER_RIGHT_A = 11;
	public static final int ENCODER_RIGHT_B = 10;

	// Joystick
	public static final int DRIVER_CONTROLLER = 0;
	public static final int MANIPULATOR_CONTROLLER = 1;
	public static final double XBOX_DEADBAND_VALUE = 0.18;
	
	//Motor Powers
	public static final double GROUND_INTAKE_POWER = 0.5;
	public static final double SHOOT_BALLS_POWER = -0.5;
	
	//Robot Constants 
	public static final double MAX_VEL_HIGH_GEAR = 18.94; // ft/s
	public static final double MAX_VEL_LOW_GEAR = 6.48;   // ft/s
	public static final double MAX_ACCEL_HIGH_GEAR = 15;
	public static final double MAX_ACCEL_LOW_GEAR = 20;
	public static final int GRAYHILL_TICKS_PER_ROT = 256;
	public static final int WHEEL_DIAMETER = 4; //inches
	public static final double INCHES_PER_TICK = WHEEL_DIAMETER*Math.PI/GRAYHILL_TICKS_PER_ROT;
	
	//Software Constants
	public static final double CONTROL_LOOP_DT = 0.02;
	
	//Turn PID Gains
	public static final double KP_TURN = 0.0125;
	public static final double KI_TURN = 0.0;
	public static final double KD_TURN = 0.055;
	//Distance FeedForward Gains 
	public static final double KV_DISTANCE = 1/MAX_VEL_HIGH_GEAR;
	public static final double KA_DISTANCE = 0.0;
	//Distance PID Gains
	public static final double KP_DISTANCE = 0.0;
	public static final double KI_DISTANCE = 0.0;
	public static final double KD_DISTANCE = 0.0;
	//Distance Straight Gains
	public static final double KP_STRAIGHT = 0.0;
	public static final double KI_STRAIGHT = 0.0;
	public static final double KD_STRAIGHT = 0.0;

}
