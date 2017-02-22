package org.usfirst.frc.team3256.lib;

import org.usfirst.frc.team3256.robot.Constants;

public class Trajectory {
	
	/**
	 * A segment is a single element of the Trajectory Array, which contains the 
	 * current position, velocity, acceleration, and time.
	 */
	public static class Segment{
		
		private double pos, vel, accel, time;
		
		/**
		 * Segment
		 * @param pos	current position of trajectory
		 * @param vel	current velocity of trajectory
		 * @param accel	current accel of trajectory
		 * @param time	current time of trajectory
		 */
		public Segment(double pos, double vel, double accel, double time){
			this.pos = pos;
			this.vel = vel;
			this.accel = accel;
			this.time = time;
		}	
		
		public double getPos(){
			return pos;
		}
		
		public double getVel(){
			return vel;
		}
		
		public double getAccel(){
			return accel;
		}
		
		public double getTime(){
			return time;
		}
	}
	
	//The Trajectory or Segment Array
	private Segment[] trajectory;
	//The length of the Trajectory or Segment Array
	private int trajectoryLength;
	
	/**
	 * @param trajectoryLength the length of the trajectory
	 */
	public Trajectory(int trajectoryLength) {
		this.trajectoryLength = trajectoryLength;
		trajectory = new Segment[trajectoryLength];
	}
	
	/**
	 * @param index the index to add a segment to the trajectory
	 * @param segment the segment to be added
	 */
	public void addSegment(int index, Segment segment){
		trajectory[index] = segment;
	}
	
	/**
	 * @return the length of the trajectory
	 */
	public int getLength(){
		return trajectoryLength;
	}
	
	/**
	 * @param index the index to get the current segment
	 * @return the segment at the specified index
	 */
	public Segment getCurrentSegment(int index){
		return trajectory[index];
	}
	
	/**
	 * A serialized version of the trajectory, with values for 
	 * position-time, velocity-time, and acceleration-time plots.
	 */
	@Override
	public String toString(){
		String ret = "";
		//Position-Time Values
		for(int i=0;i<trajectory.length;i++){	
			ret += trajectory[i].time + " " + trajectory[i].getPos() + " \n";
		}
		ret += "\n\n";
		//Velocity-Time Values
		for(int i=0;i<trajectory.length;i++){	
			ret += trajectory[i].time + " " + trajectory[i].getVel() + " \n";
		}
		ret += "\n\n";
		//Acceleration-Time Values
		for(int i=0;i<trajectory.length;i++){	
			ret += trajectory[i].time + " " + trajectory[i].getAccel() + " \n";
		}
		return ret;
	}
	
	
	public static void main(String args[]){
		TrajectoryGenerator generator = new TrajectoryGenerator();
		generator.setConfig(Constants.MAX_VEL_HIGH_GEAR_IN_SEC, Constants.MAX_ACCEL_HIGH_GEAR_IN_SEC2, Constants.CONTROL_LOOP_DT);
		Trajectory traj = generator.generateTraj(0, 0, 60);
		System.out.println(traj);
	}
}
