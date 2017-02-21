package org.usfirst.frc.team3256.lib;

import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TurnInPlaceController {

	//Motion Profiling
	private Trajectory trajectory;
	private TrajectoryGenerator trajectoryGenerator;
	private TrajectoryFollower trajectoryFollower;

	private DriveTrain drive = DriveTrain.getInstance();
	private double output;
	
	public TurnInPlaceController(){
		trajectoryGenerator = new TrajectoryGenerator();
		trajectoryFollower = new TrajectoryFollower();
		trajectoryGenerator.setConfig(Constants.MAX_VEL_TURN_LOW_GEAR_DEG, Constants.MAX_ACCEL_TURN_LOW_GEAR_DEG2, Constants.CONTROL_LOOP_DT);
	}
	
	/**
	 * @param setpoint the setpoint or goal to reach
	 */
	public void setSetpoint(double setpoint){
		reset();
		trajectory = trajectoryGenerator.generateTraj(0, 0, setpoint);
		trajectoryFollower.setTrajectory(trajectory);
		trajectoryFollower.setGains(Constants.KV_TURN, Constants.KA_TURN, 
				Constants.KP_TURN, Constants.KI_TURN, Constants.KD_TURN);
		trajectoryFollower.setLoopTime(Constants.CONTROL_LOOP_DT);
	}
	
	/**
	 * resets the motion profiling controller and the gyro
	 */
	public void reset(){
		trajectoryFollower.resetController();
		drive.resetGyro();
	}
	
	/**
	 * @return true when the follower is finished
	 */
	public boolean isFinished(){
		return trajectoryFollower.isFinished();
	}
	
	/**
	 * @return output the calcalated motor output of the trajectory follower
	 */
	public double update(){
		output = trajectoryFollower.update(Math.abs(drive.getAngle()));
		return output;
	}
	
}
