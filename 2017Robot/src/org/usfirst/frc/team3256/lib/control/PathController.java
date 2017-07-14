package org.usfirst.frc.team3256.lib.control;

import java.io.File;

import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.DistanceFollower;
import jaci.pathfinder.modifiers.TankModifier;

public class PathController {
	
	private DriveTrain drive;
	private Trajectory traj;
	private TankModifier modifier;
	private DistanceFollower leftFollower, rightFollower;
	
	public PathController(){
		drive = DriveTrain.getInstance();
	}
	
	public void init(String trajectoryName){
		drive.resetEncoders();
		drive.resetGyro();
		traj = Pathfinder.readFromFile(new File(trajectoryName));
		modifier = new TankModifier(traj).modify(Constants.ROBOT_TRACK);
		leftFollower = new DistanceFollower(modifier.getLeftTrajectory());
		rightFollower = new DistanceFollower(modifier.getRightTrajectory());
		leftFollower.configurePIDVA(Constants.KP_PATH, Constants.KI_PATH, Constants.KD_PATH, Constants.KV_PATH, Constants.KA_PATH);
		rightFollower.configurePIDVA(Constants.KP_PATH, Constants.KI_PATH, Constants.KD_PATH, Constants.KV_PATH, Constants.KA_PATH);
	}
	
	public void update(){
		double left = leftFollower.calculate(drive.getLeftPosition());
		double right = rightFollower.calculate(drive.getRightPosition());
		
		double heading = drive.getAngle();
		double headingSetpoint = Pathfinder.r2d(leftFollower.getHeading());
		
		double angleDiff = Pathfinder.boundHalfDegrees(headingSetpoint - heading);
		double turn = 5*0.8 * (-1.0/80.0) * angleDiff;
		left += turn;
		right -= turn;
		SmartDashboard.putNumber("left path pow", left);
		SmartDashboard.putNumber("right path pow", right);
		drive.setLeftMotorPower(left);
		drive.setRightMotorPower(right);
	}
	
	public boolean isFinished(){
		return leftFollower.isFinished() && rightFollower.isFinished();
	}
	
	public void reset() {
		leftFollower.reset();
		rightFollower.reset();
	}
}
