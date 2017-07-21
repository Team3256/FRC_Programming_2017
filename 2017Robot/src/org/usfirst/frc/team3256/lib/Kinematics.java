package org.usfirst.frc.team3256.lib;

import org.usfirst.frc.team3256.robot.Constants;

public class Kinematics {
	
	private static double ZERO = 1E-9;
	
	public static class DriveVelocity{
		public double left, right;
		
		public DriveVelocity(double left, double right){
			this.left = left;
			this.right = right;
		}
	}
	
	public static RigidTransform.Delta forwardKinematics(double leftDelta, double rightDelta, double rotationDelta){
		return new RigidTransform.Delta((leftDelta+rightDelta)/2, 0, rotationDelta);
	}
	
	public static RigidTransform integrateForwardKinematics(RigidTransform currentPose, double leftDelta, double rightDelta, Rotation currentHeading){
		RigidTransform.Delta transform = forwardKinematics(leftDelta, rightDelta, currentPose.getRotation().inverse().rotateBy(currentHeading).getRadians());
		return currentPose.transformBy(RigidTransform.fromVelocity(transform));
	}
	
	public static DriveVelocity inverseKinematics(RigidTransform.Delta velocity){
		if (Math.abs(velocity.dtheta) < ZERO) return new DriveVelocity(velocity.dx, velocity.dy);
		double deltaV = Constants.ROBOT_TRACK * velocity.dtheta;
		return new DriveVelocity(velocity.dx - deltaV, velocity.dx + deltaV);
	}
}
