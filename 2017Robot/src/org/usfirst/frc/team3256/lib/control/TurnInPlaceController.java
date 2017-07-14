package org.usfirst.frc.team3256.lib.control;

import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TurnInPlaceController {

	//Motion Profiling
	private TrajectoryController trajectoryController;

	private DriveTrain drive = DriveTrain.getInstance();
	private double output;
	
	public TurnInPlaceController(){
		trajectoryController = new TrajectoryController();
	}
	
	/**
	 * @param setpoint the setpoint or goal to reach
	 */
	public void setSetpoint(double setpoint){
		reset();
		TrajectoryController.TrajectoryConfig config = new TrajectoryController.TrajectoryConfig();
		config.dt = Constants.CONTROL_LOOP_DT;
		config.max_acc = Constants.MAX_ACCEL_TURN_LOW_GEAR_DEG_SEC2;
		config.max_vel = Constants.MAX_VEL_TURN_LOW_GEAR_DEG_SEC;
		trajectoryController.configure(Constants.KP_TURN, Constants.KI_TURN, Constants.KD_TURN, Constants.KV_TURN, Constants.KA_TURN, config);
		TrajectoryController.TrajectorySetpoint initialState = new TrajectoryController.TrajectorySetpoint();
		initialState.pos = 0;
		initialState.vel = 0;
		initialState.acc = 0;
		trajectoryController.setGoal(initialState, setpoint);
	}
	
	/**
	 * resets the motion profiling controller and the gyro
	 */
	public void reset(){	
		drive.resetGyro();
		drive.resetEncoders();
	}
	
	/**
	 * @return true when the follower is finished
	 */
	public boolean isFinished(){
		return trajectoryController.isFinishedTrajectory();
	}
	
	/**
	 * @return output the calculated motor output of the trajectory follower
	 */
	public double update(){	
		output = trajectoryController.calculate(drive.getAngle(), drive.getAngularVelocity());
		return output;
	}
	
}
