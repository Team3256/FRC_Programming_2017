package org.usfirst.frc.team3256.lib;

import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveStraightController {
	
	//Motion profiling for distance
	private Trajectory trajectory;
	private TrajectoryGenerator trajectoryGenerator;
	private TrajectoryFollower trajectoryFollower;
	//PID for driving straight
	private PIDController headingController;
	private DriveTrain drive = DriveTrain.getInstance();
	private double output, headingAdjustment, leftOutput, rightOutput;
	private boolean reversed;
	
	public DriveStraightController(){
		trajectoryGenerator = new TrajectoryGenerator();
		trajectoryFollower = new TrajectoryFollower();
		trajectoryGenerator.setConfig(Constants.MAX_VEL_HIGH_GEAR_IN, Constants.MAX_ACCEL_HIGH_GEAR_IN2, Constants.CONTROL_LOOP_DT);
		headingController = new PIDController(Constants.KP_STRAIGHT, Constants.KI_STRAIGHT, Constants.KD_STRAIGHT);
	}
	
	/**
	 * @param setpoint the setpoint or goal to reach
	 * @param reversed true if we are driving backwards, false if we are not reversed and driving forward
	 */
	public void setSetpoint(double setpoint, boolean reversed){
		trajectory = trajectoryGenerator.generateTraj(0, 0, setpoint);
		trajectoryFollower.setTrajectory(trajectory);
		trajectoryFollower.setGains(Constants.KV_DISTANCE, Constants.KA_DISTANCE, 
				Constants.KP_DISTANCE, Constants.KI_DISTANCE, Constants.KD_DISTANCE);
		trajectoryFollower.setLoopTime(Constants.CONTROL_LOOP_DT);
		headingController.setSetpoint(0);
		this.reversed = reversed;
		reset();
	}
	
	/**
	 * Resets both the motion profiling and pid controllers
	 * Resets the encoders and the gyro
	 */
	public void reset(){
		trajectoryFollower.resetController();
		headingController.reset();
		drive.resetEncoders();
		drive.resetGyro();
	}
	
	/**
	 * @return true when the follower (distnace controller) has finished 
	 */
	public boolean isFinished(){
		return trajectoryFollower.isFinished();
	}
	
	/**
	 * @return DrivePWM with the left and right velocities of the robot
	 * calculated based on our current position and angle
	 */
	public DrivePWM update(){
		//follower output for distance
		output = trajectoryFollower.update(Math.abs(drive.getAveragePosition()));
		//pid output for driving straight
		headingAdjustment = headingController.update(drive.getAngle() * (reversed ? -1 : 1));
		//adjust values for the left and right sides of the drivetrain
		leftOutput = output - headingAdjustment;
		rightOutput = output + headingAdjustment;
		DrivePWM signal = new DrivePWM(leftOutput, rightOutput);
		return signal;
	}
	
	
}
