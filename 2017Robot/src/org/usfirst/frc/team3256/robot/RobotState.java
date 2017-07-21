package org.usfirst.frc.team3256.robot;

import java.util.Map;

import org.usfirst.frc.team3256.lib.InterpolatingDouble;
import org.usfirst.frc.team3256.lib.InterpolatingTreeMap;
import org.usfirst.frc.team3256.lib.Kinematics;
import org.usfirst.frc.team3256.lib.RigidTransform;
import org.usfirst.frc.team3256.lib.Rotation;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RobotState {
	private int observationBufferSize = 100;
	private static RobotState instance = null;
	private RigidTransform.Delta robotVelocity;
	public static RobotState getInstance(){
		return instance == null ? instance = new RobotState() : instance;
	}
	
	private InterpolatingTreeMap<InterpolatingDouble, RigidTransform> fieldToVehicle;
	
	private RobotState(){
		reset(0, new RigidTransform());
	}
	
	public synchronized void reset(double startTime, RigidTransform initialFieldToVehicle){
		fieldToVehicle = new InterpolatingTreeMap<InterpolatingDouble, RigidTransform>(observationBufferSize);
		fieldToVehicle.put(new InterpolatingDouble(startTime), initialFieldToVehicle);
		robotVelocity = new RigidTransform.Delta(0, 0, 0);
	}
	
	public synchronized RigidTransform getFieldToVehicle(double time){
		return fieldToVehicle.getInterpolatedValue(new InterpolatingDouble(time));
	}
	
	public synchronized Map.Entry<InterpolatingDouble, RigidTransform> getLatestFieldToVehicle(){
		return fieldToVehicle.lastEntry();
	}
	
	public synchronized void addFieldToVehicleObseravation(double time, RigidTransform observation){
		fieldToVehicle.put(new InterpolatingDouble(time), observation);
	}
	
	public synchronized void addObservations(double time, RigidTransform observation, RigidTransform.Delta velocity){
		addFieldToVehicleObseravation(time, observation);
		robotVelocity = velocity;
	}
	
	public RigidTransform generateOdometryFromSensors(double leftDeltaDistance, double rightDeltaDistance, Rotation heading){
		RigidTransform lastMeasurement = getLatestFieldToVehicle().getValue();
		return Kinematics.integrateForwardKinematics(lastMeasurement, leftDeltaDistance, rightDeltaDistance, heading);
	}
	
	public void outputToDashboard(){
        RigidTransform odometry = getLatestFieldToVehicle().getValue();
        SmartDashboard.putNumber("robot_pose_x", odometry.getTranslation().getX());
        SmartDashboard.putNumber("robot_pose_y", odometry.getTranslation().getY());
        SmartDashboard.putNumber("robot_pose_theta", odometry.getRotation().getDegrees());
	}
}
