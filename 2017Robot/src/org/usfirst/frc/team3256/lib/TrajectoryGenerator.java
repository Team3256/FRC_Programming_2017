package org.usfirst.frc.team3256.lib;

import org.usfirst.frc.team3256.lib.Trajectory.Segment;

public class TrajectoryGenerator{
	
	double max_vel;
	double accel;
	double dt;
	
	//Generates a feedforward trajectory	
	public TrajectoryGenerator(){
	
	}
	
	public void setConfig(double max_vel, double max_accel, double dt){
		this.max_vel = max_vel;
		this.accel = max_accel;
		this.dt = dt; 
	}
	
	//Generates a trajectory (list of segments) for the given constraints
	public Trajectory generateTraj(double start_vel, double end_vel, double end_pos){
		System.out.println(end_pos);
		Trajectory traj;
		//Calculates the cruise_velocity for the parameters given depending on the distance to travel
		double start_dist_offset = (0.5*start_vel*start_vel)/accel;
		double end_dist_offset = (0.5*end_vel*end_vel)/accel;
		double cruise_vel = Math.min(max_vel, Math.sqrt(accel*end_pos-start_dist_offset-end_dist_offset));
		//cruise_vel = max_vel;
		//-----Calculates other kinematic values
		double accel_time = (cruise_vel-start_vel)/accel;
		double deaccel_time = (cruise_vel-end_vel)/accel;
		//compute dists
		//dist = initial_dist(0) + initial_vel*time + 1/2*accel*time^2 
		//accel = deaccel
		double accel_dist = (start_vel*accel_time+0.5*accel*accel_time*accel_time); 
		double deaccel_dist = (cruise_vel*deaccel_time + (-0.5)*accel*deaccel_time*deaccel_time);
		double cruise_dist = end_pos-(accel_dist+deaccel_dist);
		double cruise_time = cruise_dist/cruise_vel;
		double total_time = cruise_time+accel_time+deaccel_time;
		int size = (int)(total_time/dt);
		traj = new Trajectory(size);
		double curr_time = 0;
		System.out.printf("Total Dist: %f   Total Time: %f, Accel Time: %f   Time at end of Cruise: %f\n",end_pos, total_time,accel_time,(accel_time+cruise_time));
		for(int i=0;i<size;i++){
			double curr_pos, curr_vel, curr_accel;
			if (curr_time<accel_time){
				curr_pos = (0.5)*accel*curr_time*curr_time;
				curr_vel = start_vel+accel*curr_time;
				curr_accel = accel;
			}	
			else if (curr_time>accel_time&&curr_time<(accel_time+cruise_time)){
				curr_pos = accel_dist+cruise_vel*(curr_time-accel_time);
				curr_vel = cruise_vel;
				curr_accel = 0;
			}
			else{
				double temp_curr_time = curr_time-(accel_time+cruise_time);
				double adjusted_curr_time = total_time-accel_time-cruise_time-temp_curr_time;
				double adjusted_curr_pos = (0.5*accel*adjusted_curr_time*adjusted_curr_time); 
				curr_pos = end_pos-adjusted_curr_pos; 
				curr_vel = cruise_vel-(accel*temp_curr_time);
				curr_accel = -accel;
			
			}
			Segment s = new Segment(curr_pos, curr_vel, curr_accel, curr_time);
			traj.addSegment(i, s);
			curr_time+=dt;
		}
		return traj;
	}
}
