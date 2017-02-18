package org.usfirst.frc.team3256.lib;

import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;

public class TurnInPlaceController {

	private Trajectory trajectory;
	private DriveTrain drive = DriveTrain.getInstance();
	private TrajectoryGenerator trajectoryGenerator;
	private TrajectoryFollower trajectoryFollower;
	private double output;
	private boolean reversed;
	
	public TurnInPlaceController(){
		trajectoryGenerator = new TrajectoryGenerator();
		trajectoryFollower = new TrajectoryFollower();
		trajectoryGenerator.setConfig(Constants.MAX_VEL_HIGH_GEAR_IN, Constants.MAX_ACCEL_HIGH_GEAR, Constants.CONTROL_LOOP_DT);
	}
	
	public void setSetpoint(double setpoint, boolean reversed){
		trajectory = trajectoryGenerator.generateTraj(0, 0, setpoint);
		trajectoryFollower.setTrajectory(trajectory);
		trajectoryFollower.setGains(Constants.KV_DISTANCE, Constants.KA_DISTANCE, 
				Constants.KP_DISTANCE, Constants.KI_DISTANCE, Constants.KD_DISTANCE);
		trajectoryFollower.setLoopTime(Constants.CONTROL_LOOP_DT);
		this.reversed = reversed;
		reset();
	}
	
	public void reset(){
		trajectoryFollower.resetController();
		drive.resetEncoders();
		drive.resetGyro();
	}
	
	public boolean isFinished(){
		return trajectoryFollower.isFinished();
	}
	
	public double update(){
		output = trajectoryFollower.calcMotorOutput(drive.getAngle());
		return output;
	}
}
