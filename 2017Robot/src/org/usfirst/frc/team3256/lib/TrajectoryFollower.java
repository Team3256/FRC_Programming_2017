package org.usfirst.frc.team3256.lib;

import org.usfirst.frc.team3256.lib.Trajectory.Segment;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TrajectoryFollower{
	
	private double kP, kI, kD, kV, kA, dt;
	double PID, error, sumError=0, changeError=0, prevError=0;
	private Trajectory traj;
	private int curr_segment;
	private double output, feedForwardValue, feedBackValue;
		
	public TrajectoryFollower(){

	}

	public void setTrajectory(Trajectory traj){
		this.traj = traj;
	}
	
	public void setGains(double kV, double kA, double kP, double kI, double kD){
		this.kV = kV;
		this.kA = kA;
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
	}

	public void setLoopTime(double dt){
		this.dt = dt;
	}

	public void resetController(){
		sumError = 0;
		changeError = 0;
		prevError = 0;	
		error = 0;
		curr_segment = 0;
	}
	
	private double calcFeedForward(double curr_vel, double curr_accel){
		return kV*curr_vel + kA*curr_accel;
	}	

	private double calcFeedBack(double setpoint_pos, double curr_pos, double curr_vel){
		error = setpoint_pos-curr_pos;
		SmartDashboard.putNumber("error", error);
		sumError+=error;
		changeError = (error-prevError)/dt - curr_vel;
		prevError = error;
		PID = kP*error + kI*sumError + kD*changeError;
		return PID;
	}
	
	public boolean isFinished(){
		return curr_segment >= traj.getLength();
	}

	public double calcMotorOutput(double currentTrajPos){
		System.out.println("FINISHED???????? " + isFinished());
		System.out.println(curr_segment + "--" + traj.getLength());
		if (!isFinished()){ 
			Segment s = traj.getCurrentSegment(curr_segment);
			System.out.println("pos " + s.getPos());
			System.out.println("time " + s.getTime());
			SmartDashboard.putNumber("Theoretical Vel", s.getVel());
			feedForwardValue = calcFeedForward(s.getVel(), s.getAccel());
			System.out.println("FF VALUE" + feedForwardValue);
			SmartDashboard.putNumber("Current THeoretcial POs", s.getPos());
			SmartDashboard.putNumber("CURRENT ENCODER POS", currentTrajPos);
			feedBackValue = calcFeedBack(s.getPos(), currentTrajPos, s.getVel());
			output = feedForwardValue + feedBackValue;
			curr_segment++;
			if (output>1) output = 1;
			if (output<-1) output = -1;
			return output;
		}
		return 0;
	}
	
}
