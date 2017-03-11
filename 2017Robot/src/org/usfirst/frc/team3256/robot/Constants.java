package org.usfirst.frc.team3256.robot;

public class Constants {

//------------------------------ELECTRICAL PORTS------------------------------
	
	//PWM Motor Controller ports
	public static final int LEFT_DRIVE = 9; 
	public static final int RIGHT_DRIVE = 8; 
	public static final int HANGER = 7;
	public static final int INNER_MOTOR_ROLLER = 4;	
	public static final int OUTER_MOTOR_ROLLER = 3; 
	
	//PDP Motor ports
	public static final int PDP_LEFT_FRONT = 0;  
	public static final int PDP_LEFT_BACK = 1;
	public static final int PDP_RIGHT_FRONT = 3;  
	public static final int PDP_RIGHT_BACK = 2; 
	public static final int PDP_HANGER_1 = 15; 
	public static final int PDP_HANGER_2 = 4;
	public static final int PDP_INNER_MOTOR_ROLLER = 12;  
	public static final int PDP_OUTER_MOTOR_ROLLER = 11; 

	//PCM Solenoid ports
	public static final int DRIVE_SHIFTER_A = 7;
	public static final int DRIVE_SHIFTER_B = 3;
	public static final int BALL_PIVOT_A = 6; 
	public static final int BALL_PIVOT_B = 2; 
	public static final int GEAR_PIVOT_A = 4; 
	public static final int GEAR_PIVOT_B = 0;
	public static final int GEAR_DEPLOY_A = 1; //5 
	public static final int GEAR_DEPLOY_B = 5; //1

	//Encoder ports on the Spartan Board 
	public static final int ENCODER_LEFT_A = 11; //13
	public static final int ENCODER_LEFT_B = 10; //12
	public static final int ENCODER_RIGHT_A = 13; //11
	public static final int ENCODER_RIGHT_B = 12; //10
	
	// Joystick ports and deadband
	public static final int DRIVER_CONTROLLER = 0;
	public static final int MANIPULATOR_CONTROLLER = 1;
	public static final double XBOX_DEADBAND_VALUE = 0.25;

//------------------------------Physical Robot Constants------------------------------

	public static final double ROBOT_TRACK = 26.75;
	public static final double ROBOT_CIRCUMFERENCE = Math.PI*ROBOT_TRACK;
	public static final double MAX_VEL_HIGH_GEAR_IN_SEC = 10.0 * 12.0; // 18.94 theoretical ft/s
	public static final double MAX_ACCEL_HIGH_GEAR_IN_SEC2 = 9.0 * 12.0; 
	public static final double MAX_VEL_TURN_LOW_GEAR_DEG_SEC = 200.0; 
	public static final double MAX_ACCEL_TURN_LOW_GEAR_DEG_SEC2 = 200.0; 
	public static final int GRAYHILL_TICKS_PER_ROT = 256;
	public static final double WHEEL_DIAMETER = 4.09; //inches -- theoretical 4", includes tread
	public static final double INCHES_PER_TICK = WHEEL_DIAMETER*Math.PI/GRAYHILL_TICKS_PER_ROT;
	
//------------------------------SOFTWARE CONSTANTS------------------------------

	//Preset Values
	public static final double GROUND_INTAKE_POWER = 1;
	public static final double SHOOT_BALLS_POWER = -1;
	public static final double WINCH_HANGER_POWER = -1.0;
	
	//Period for all control loops
	public static final double CONTROL_LOOP_DT = 0.02;
	
	//Turn PID Gains - LOW GEAR
	public static final double KP_PID_TURN = 0.075;
	public static final double KI_PID_TURN = 0.006;
	public static final double KD_PID_TURN = 0.01;
	
	//Drive Motion Profile Gains - HIGH GEAR
	public static final double KV_DISTANCE = 1.0/MAX_VEL_HIGH_GEAR_IN_SEC;
	public static final double KA_DISTANCE = 0.0;
	public static final double KP_DISTANCE = 0.32; //0.32
	public static final double KI_DISTANCE = 0.0;
	public static final double KD_DISTANCE = 0.0;
	public static final double KP_STRAIGHT = 0.03;
	public static final double KI_STRAIGHT = 0.0;
	public static final double KD_STRAIGHT = 0.0;
	
	//Turn Motion Profile Gains - HIGH GEAR
	public static final double KV_TURN = 1.0/MAX_VEL_TURN_LOW_GEAR_DEG_SEC;
	public static final double KA_TURN = 0.0;
	public static final double KP_TURN = 0.001;
	public static final double KI_TURN = 0.0;
	public static final double KD_TURN = 0.0;
}
