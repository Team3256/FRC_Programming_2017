package org.usfirst.frc.team3256.lib;

import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TurnInPlaceController {

	private Trajectory trajectory;
	private DriveTrain drive = DriveTrain.getInstance();
	private TrajectoryGenerator trajectoryGenerator;
	private TrajectoryFollower trajectoryFollower;
	private double output;
	private Timer t;
	
	public TurnInPlaceController(){
		trajectoryGenerator = new TrajectoryGenerator();
		trajectoryFollower = new TrajectoryFollower();
		trajectoryGenerator.setConfig(Constants.MAX_VEL_TURN_LOW_GEAR_DEG, Constants.MAX_ACCEL_TURN_LOW_GEAR_DEG2, Constants.CONTROL_LOOP_DT);
	}
	
	public void setSetpoint(double setpoint){
		reset();
		t = new Timer();
		t.start();
		trajectory = trajectoryGenerator.generateTraj(0, 0, setpoint);
		trajectoryFollower.setTrajectory(trajectory);
		trajectoryFollower.setGains(Constants.KV_TURN, Constants.KA_TURN, 
				Constants.KP_TURN, Constants.KI_TURN, Constants.KD_TURN);
		trajectoryFollower.setLoopTime(Constants.CONTROL_LOOP_DT);
	}
	
	public void reset(){
		trajectoryFollower.resetController();
		drive.resetGyro();
	}
	
	public boolean isFinished(){
		return trajectoryFollower.isFinished();
	}
	
	public double update(){
		SmartDashboard.putNumber("CURRENT TIME", t.get());
		output = trajectoryFollower.calcMotorOutput(Math.abs(drive.getAngle()));
		return output;
	}
}
