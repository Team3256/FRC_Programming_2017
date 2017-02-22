package org.usfirst.frc.team3256.robot;

public class Constants {

//------------------------------ELECTRICAL PORTS------------------------------
	
	//PWM Motor Controller ports
	public static final int LEFT_DRIVE = 9; //0
	public static final int RIGHT_DRIVE = 8; //1
	public static final int HANGER = 7; //6
	public static final int INNER_MOTOR_ROLLER = 4; //4
	public static final int OUTER_MOTOR_ROLLER = 5; //5
	
	//PDP Motor ports
	public static final int PDP_LEFT_FRONT = 1; //0
	public static final int PDP_RIGHT_FRONT = 2; //1
	public static final int PDP_LEFT_BACK = 0; //2 
	public static final int PDP_RIGHT_BACK = 3; //3
	public static final int PDP_HANGER_1 = 4; //4 
	public static final int PDP_HANGER_2 = 5; //5
	public static final int PDP_INNER_MOTOR_ROLLER = 6; //6 
	public static final int PDP_OUTER_MOTOR_ROLLER = 7; //7

	//PCM Solenoid ports
	public static final int DRIVE_SHIFTER_A = 6; //3
	public static final int DRIVE_SHIFTER_B = 3; //7
	public static final int BALL_PIVOT_A = 7; //2
	public static final int BALL_PIVOT_B = 0; //0
	public static final int GEAR_PIVOT_A = 1; //1
	public static final int GEAR_PIVOT_B = 4; //5
	public static final int GEAR_DEPLOY_A = 2;
	public static final int GEAR_DEPLOY_B = 5;

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
	
	public static final double MAX_VEL_HIGH_GEAR_IN_SEC = 15.0 * 12.0; // 18.94 theoretical ft/s
	public static final double MAX_ACCEL_HIGH_GEAR_IN_SEC2 = 20; 
	public static final double MAX_VEL_TURN_LOW_GEAR_DEG_SEC = 75.0;
	public static final double MAX_ACCEL_TURN_LOW_GEAR_DEG_SEC2 = 75.0;
	public static final int GRAYHILL_TICKS_PER_ROT = 256;
	public static final double WHEEL_DIAMETER = 4.09; //inches -- theoretical 4", includes tread
	public static final double INCHES_PER_TICK = WHEEL_DIAMETER*Math.PI/GRAYHILL_TICKS_PER_ROT;
	
//------------------------------SOFTWARE CONSTANTS------------------------------

	//Preset Values
	public static final double GROUND_INTAKE_POWER = 0.5;
	public static final double SHOOT_BALLS_POWER = -0.5;
	public static final double WINCH_HANGER_POWER = 1.0;
	
	//Period for all control loops
	public static final double CONTROL_LOOP_DT = 0.02;
	
	//Turn PID Gains - LOW GEAR
	public static final double KP_PID_TURN = 0.0178;
	public static final double KI_PID_TURN = 0.001;
	public static final double KD_PID_TURN = 0.01;
	
	//Drive Motion Profile Gains - HIGH GEAR
	public static final double KV_DISTANCE = 1.0/115.0;
	public static final double KA_DISTANCE = 0.004; //0.004;
	public static final double KP_DISTANCE = 0.005; //0.17;
	public static final double KI_DISTANCE = 0.0;
	public static final double KD_DISTANCE = 0.0;
	public static final double KP_STRAIGHT = 0.02;
	public static final double KI_STRAIGHT = 0.0;
	public static final double KD_STRAIGHT = 0.0;
	
	//Turn Motion Profile Gains - LOW GEAR
	public static final double KV_TURN = 1.0/MAX_VEL_TURN_LOW_GEAR_DEG_SEC;
	public static final double KA_TURN = 0.003;
	public static final double KP_TURN = 0.02;
	public static final double KI_TURN = 0.0;
	public static final double KD_TURN = 0.0;
}
