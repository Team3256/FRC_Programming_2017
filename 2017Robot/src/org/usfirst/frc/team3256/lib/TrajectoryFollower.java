package org.usfirst.frc.team3256.lib;

import org.usfirst.frc.team3256.lib.Trajectory.Segment;


/**
 * Class to follow a trajectory and calculate the motor output 
 * based on the trajectory and our current position
 */
public class TrajectoryFollower{
	
	private double kP, kI, kD, kV, kA, dt;
	double PID, error, sumError=0, changeError=0, prevError=0;
	private Trajectory traj;
	private int curr_segment;
	private double output, feedForwardValue, feedBackValue;

	public TrajectoryFollower(){

	}

	/**
	 * @param traj the trajectory for the follower to follow
	 */
	public void setTrajectory(Trajectory traj){
		this.traj = traj;
	}
	
	/**
	 * @param kV Feedforward velocity Gain - usually 1/maxVel
	 * @param kA Feedforward acceleration Gain - increase to increase acceleration
	 * @param kP Feedback proportional Gain 
	 * @param kI Feedback integral Gain
	 * @param kD Feedback derivative Gain
	 */
	public void setGains(double kV, double kA, double kP, double kI, double kD){
		this.kV = kV;
		this.kA = kA;
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
	}

	/**
	 * @param dt the period of the control loop
	 */
	public void setLoopTime(double dt){
		this.dt = dt;
	}

	/**
	 * Rests the PID controller and the current segment of the follower
	 */
	public void resetController(){
		sumError = 0;
		changeError = 0;
		prevError = 0;	
		error = 0;
		curr_segment = 0;
	}
	
	/**
	 * @param currVel the velocity at the current segment
	 * @param currAccel the acceleration at the current segment
	 * @return the calculated feedfoward output
	 */
	private double calcFeedForward(double currVel, double currAccel){
		return kV*currVel + kA*currAccel;
	}	

	/**
	 * @param setpointPos the theoretical position we should be at at the current segment
	 * @param currPos the actual position we are at at the current segment
	 * @param setpointVel the current velocity we should be at
	 * @return the calculated feedback adjustment output
	 */
	private double calcFeedBack(double setpointPos, double currPos, double setpointVel){
		//normal pid error
		error = setpointPos-currPos;
		//integral of error
		sumError+=error*dt;
		//derivative of error: the change in error over time minus the velocity we should be at
		changeError = (error-prevError)/dt - setpointVel;
		//calculate the pid output
		PID = kP*error + kI*sumError + kD*changeError;
		//update previous error
		prevError = error;
		return PID;
	}

	/**
	 * @return true when the current segment has reached the final segment in the trajectory
	 */
	public boolean isFinished(){
		return (curr_segment >= traj.getLength());
	}
	
	/**
	 * @param currPos the current position we are at
	 * @return the motor ouptut with the feedforward and the feedback
	 */
	public double update(double currPos){
		//only update we are not finished yet
		if (!isFinished()){  
			Segment s = traj.getCurrentSegment(curr_segment);
			//calculate ff value
			feedForwardValue = calcFeedForward(s.getVel(), s.getAccel());
			//calculate fb value
			feedBackValue = calcFeedBack(s.getPos(), currPos, s.getVel());
			System.out.println("FF VALUE------------" + feedForwardValue);
			//output = ff + fb
			output = feedForwardValue + feedBackValue;
			//increment current segment 
			curr_segment++;
			//filter the output to be within bounds
			if (output>1) output = 1;
			if (output<-1) output = -1;
			return output;
		}
		return 0;
	}

}
