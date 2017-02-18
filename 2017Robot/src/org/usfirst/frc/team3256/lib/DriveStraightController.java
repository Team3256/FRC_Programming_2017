package org.usfirst.frc.team3256.lib;

import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import javafx.util.Pair;

public class DriveStraightController {
	
	private Trajectory trajectory;
	private TrajectoryGenerator trajectoryGenerator;
	private TrajectoryFollower trajectoryFollower;
	private PIDController headingController;
	private DriveTrain drive = DriveTrain.getInstance();
	private double output, headingAdjustment, leftOutput, rightOutput;
	
	public DriveStraightController(){
		trajectoryGenerator = new TrajectoryGenerator();
		trajectoryFollower = new TrajectoryFollower();
		trajectoryGenerator.setConfig(Constants.MAX_VEL_HIGH_GEAR_IN, Constants.MAX_ACCEL_HIGH_GEAR, Constants.CONTROL_LOOP_DT);
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
	
	public DrivePWM update(){
		//drive.getRightPosition() for now because only one encoder is plugged in
		output = trajectoryFollower.calcMotorOutput(Math.abs(drive.getAveragePosition()));
		SmartDashboard.putNumber("MOTION PROFILE OUTPUT", output);
		headingAdjustment = headingController.update(drive.getAngle());
		SmartDashboard.putNumber("Gyro Adjustment", headingAdjustment);
		SmartDashboard.putNumber("GYRO ERROR DRIVE STRAIGHT", 0-drive.getAngle());
		leftOutput = output - headingAdjustment;
		rightOutput = output + headingAdjustment;
		SmartDashboard.putNumber("Left Output", leftOutput);
		SmartDashboard.putNumber("Right Output", rightOutput);
		DrivePWM signal = new DrivePWM(leftOutput, rightOutput);
		return signal;
	}
	
	
}
