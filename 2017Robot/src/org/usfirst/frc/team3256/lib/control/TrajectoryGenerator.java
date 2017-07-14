package org.usfirst.frc.team3256.lib.control;

import org.usfirst.frc.team3256.lib.control.Trajectory.Segment;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * A class to generate a trajectory, given the maximum acceleration, 
 * maximum velocity, and the control loop period.
 */
public class TrajectoryGenerator{
	
	private double maxVel;
	private double maxAccel;
	private double dt;
	
	public TrajectoryGenerator(){
	
	}
	
	/**
	 * @param maxVel The maximum velocity of the robot
	 * @param maxAccel The maximum acceleration of the robot
	 * @param dt The period of the control loop.
	 */
	public void setConfig(double maxVel, double maxAccel, double dt){
		this.maxVel = maxVel;
		this.maxAccel = maxAccel;
		this.dt = dt; 
	}
	
	/**
	 * @param startVel The starting velocity of the trajectory, usually 0
	 * @param endVel The ending velocity of the trajectory, usually 0
	 * @param setpoint The setpoint or ending position of the trajectory
	 * @return the generated trajectory
	 */
	public Trajectory generateTraj(double startVel, double endVel, double setpoint){
		//Trajectory to store the generated trajectory
		Trajectory traj;
		//Calculates the maximum achievable velocity for the parameters 
		//given depending on the distance to travel and the configuration set.
		double startDistOffset = (0.5*startVel*startVel)/maxAccel;
		double endDistOffset = (0.5*endVel*endVel)/maxAccel;
		//CruiseVel - the maximum velocity of the trajectory
		double cruiseVel = Math.min(maxVel, Math.sqrt(maxAccel*setpoint-startDistOffset-endDistOffset));
		SmartDashboard.putNumber("CRUISE VEL", cruiseVel);
		//Acceleration and DeAcceleration time
		//v = at -> t = v/a;
		double accel_time = (cruiseVel-startVel)/maxAccel;
		double deaccel_time = (cruiseVel-endVel)/maxAccel;
		//Acceleration and DeAcceleration Distances
		//x = x0 + v0t + 1/2at^2 -> x = v0t + 1/2at^2
		double accel_dist = (startVel*accel_time+0.5*maxAccel*accel_time*accel_time); 
		double deaccel_dist = (cruiseVel*deaccel_time + (-0.5)*maxAccel*deaccel_time*deaccel_time);
		//Distance at cruise velocity - acceleration is 0
		//x (remaining distance) = total distance - acceleration dist - deacceleration distance
		double cruise_dist = setpoint-(accel_dist+deaccel_dist);
		//Duration of traveling at cruise velocity - acceleration is 0
		//x = vt -> t = x/v
		double cruise_time = cruise_dist/cruiseVel;
		//Total Duration - Sum of acceleration time, cruising time, and deacceleration time
		double total_time = cruise_time+accel_time+deaccel_time;
		//Total number of segments
		int size = (int)(total_time/dt);
		traj = new Trajectory(size);
		//Start iterating
		double curr_time = 0;
		for(int i=0;i<size;i++){
			double curr_pos, curr_vel, curr_accel;
			//If we are accelerating 
			if (curr_time<accel_time){
				//x = x0 + v0t + 1/2at^2
				//x = v0t + 1/2at^2
				curr_pos = startVel*curr_time+(0.5)*maxAccel*curr_time*curr_time;
				//v = v0 + at
				curr_vel = startVel+maxAccel*curr_time;
				curr_accel = maxAccel;
			}	
			//If we are at cruise velocity with no acceleration
			else if (curr_time>accel_time&&curr_time<(accel_time+cruise_time)){
				//x = x0 + v0t
				curr_pos = accel_dist+cruiseVel*(curr_time-accel_time);
				curr_vel = cruiseVel;
				curr_accel = 0;
			}
			//If we are deaccelerating
			else{
				//This part is a mirror of the acceleration
				//Calculate adjusted values and subtract them from the maximum velocity and position
				//instead of adding values to the initial velocity and position
				double temp_curr_time = curr_time-(accel_time+cruise_time);
				double adjusted_curr_time = total_time-accel_time-cruise_time-temp_curr_time;
				double adjusted_curr_pos = (0.5*maxAccel*adjusted_curr_time*adjusted_curr_time); 
				curr_pos = setpoint-adjusted_curr_pos; 
				curr_vel = cruiseVel-(maxAccel*temp_curr_time);
				curr_accel = -maxAccel;
			
			}
			//Construct a segment with the given parameters
			Segment s = new Segment(curr_pos, curr_vel, curr_accel, curr_time);
			//Add the segment to the trajectory
			traj.addSegment(i, s);
			//Increment the current time
			curr_time+=dt;
		}
		//Return the trajectory
		return traj;
	}
}
