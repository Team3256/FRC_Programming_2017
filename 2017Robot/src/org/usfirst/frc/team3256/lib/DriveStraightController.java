package org.usfirst.frc.team3256.lib;

import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;

import javafx.util.Pair;

public class DriveStraightController {
	
	private Trajectory trajectory;
	private TrajectoryGenerator trajectoryGenerator;
	private TrajectoryFollower trajectoryFollower;
	private PIDController headingController;
	private DriveTrain drive = DriveTrain.getInstance();
	
	public DriveStraightController(){
		trajectoryGenerator = new TrajectoryGenerator();
		trajectoryFollower = new TrajectoryFollower();
		trajectoryGenerator.setConfig(Constants.MAX_VEL_HIGH_GEAR, Constants.MAX_ACCEL_HIGH_GEAR, Constants.CONTROL_LOOP_DT);
		headingController = new PIDController(Constants.KP_STRAIGHT, Constants.KI_STRAIGHT, Constants.KD_STRAIGHT);
	}
	
	public void setSetpoint(double setpoint){
		trajectory = trajectoryGenerator.generateTraj(0, 0, setpoint);
		trajectoryFollower.setTrajectory(trajectory);
		trajectoryFollower.setGains(Constants.KV_DISTANCE, Constants.KA_DISTANCE, 
				Constants.KP_DISTANCE, Constants.KI_DISTANCE, Constants.KD_DISTANCE);
		trajectoryFollower.setLoopTime(Constants.CONTROL_LOOP_DT);
		headingController.setSetpoint(0);
		reset();
	}

	
	public void reset(){
		trajectoryFollower.resetController();
		headingController.reset();
		drive.resetEncoders();
		drive.resetGyro();
	}
	
	public boolean isFinished(){
		return trajectoryFollower.isFinished();
	}
	
	public Pair<Double, Double> update(){
		double output = trajectoryFollower.calcMotorOutput(drive.getAveragePosition());
		double headingAdjustment = headingController.update(drive.getAngle());
		double leftOutput = output - headingAdjustment;
		double rightOutput = output + headingAdjustment;
		Pair<Double, Double> driveOutputPair = new Pair<Double, Double>(leftOutput, rightOutput);
		return driveOutputPair;
	}
	
	
}
