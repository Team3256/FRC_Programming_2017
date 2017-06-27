package org.usfirst.frc.team3256.lib;

import java.io.File;

import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

public class PathController {
	
	private DriveTrain drive;
	private Trajectory traj;
	private TankModifier modifier;
	private EncoderFollower leftFollower, rightFollower;
	
	public PathController(){
		drive = DriveTrain.getInstance();
	}
	
	public void init(String trajectoryName){
		drive.resetEncoders();
		drive.resetGyro();
		traj = Pathfinder.readFromFile(new File(trajectoryName));
		modifier = new TankModifier(traj).modify(Constants.ROBOT_TRACK);
		leftFollower = new EncoderFollower(modifier.getLeftTrajectory());
		rightFollower = new EncoderFollower(modifier.getRightTrajectory());
		leftFollower.configureEncoder(drive.getRawLeftTicks(), Constants.GRAYHILL_TICKS_PER_ROT, Constants.WHEEL_DIAMETER);
		rightFollower.configureEncoder(drive.getRawRightTicks(), Constants.GRAYHILL_TICKS_PER_ROT, Constants.WHEEL_DIAMETER);
		leftFollower.configurePIDVA(Constants.KP_PATH, Constants.KI_PATH, Constants.KD_PATH, Constants.KV_PATH, Constants.KA_PATH);
		rightFollower.configurePIDVA(Constants.KP_PATH, Constants.KI_PATH, Constants.KD_PATH, Constants.KV_PATH, Constants.KA_PATH);
	}
	
	public void update(){
		double left = leftFollower.calculate(drive.getRawLeftTicks());
		double right = rightFollower.calculate(drive.getRawRightTicks());
		
		double heading = drive.getAngle();
		double headingSetpoint = Pathfinder.r2d(leftFollower.getHeading());
		
		double angleDiff = Pathfinder.boundHalfDegrees(headingSetpoint - heading);
		double turn = 0.8*(-1.0/80.0) * angleDiff;
		System.out.print(turn);
		left += turn;
		right -= turn;
		drive.setLeftMotorPower(left);
		drive.setRightMotorPower(right);
		
	}
	
	public boolean isFinished(){
		return leftFollower.isFinished() && rightFollower.isFinished();
	}
	
	public void reset() {
		leftFollower.reset();
		rightFollower.reset();
		//drive.resetEncoders();
		//drive.resetGyro();
	}
}
