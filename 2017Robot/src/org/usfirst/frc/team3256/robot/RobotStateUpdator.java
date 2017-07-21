package org.usfirst.frc.team3256.robot;

import org.usfirst.frc.team3256.lib.Kinematics;
import org.usfirst.frc.team3256.lib.Loop;
import org.usfirst.frc.team3256.lib.RigidTransform;
import org.usfirst.frc.team3256.lib.Rotation;
import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Timer;

public class RobotStateUpdator implements Loop{

	private static RobotStateUpdator instance;
	
	public static RobotStateUpdator getInstance(){
		return instance == null ? instance = new RobotStateUpdator() : instance;
	}
	
	private RobotState state = RobotState.getInstance();
	private DriveTrain drive = DriveTrain.getInstance();
	double prevLeftDist = 0, prevRightDist = 0;
	
	@Override
	public void initialize() {
		prevLeftDist = drive.getLeftPosition();
		prevRightDist = drive.getRightPosition();
	}

	@Override
	public void update() {
		double time = Timer.getFPGATimestamp();
		double leftDistance = drive.getLeftPosition();
		double rightDistance = drive.getRightPosition();
		Rotation heading = Rotation.fromDegrees(drive.getAngle());
		RigidTransform odomotry = state.generateOdometryFromSensors(leftDistance-prevLeftDist, rightDistance-prevRightDist, heading);
		RigidTransform.Delta velocity = Kinematics.forwardKinematics(drive.getLeftVelocity(), drive.getRightVelocity(), heading.getDegrees());
		state.addObservations(time, odomotry, velocity);
		prevLeftDist = leftDistance;
		prevRightDist = rightDistance;
	}

	@Override
	public void end() {
		
	}

}
