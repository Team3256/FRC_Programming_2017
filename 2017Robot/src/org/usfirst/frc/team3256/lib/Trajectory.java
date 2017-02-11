package org.usfirst.frc.team3256.lib;

public class Trajectory {
	
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
	
	private Segment[] trajectory;
	private int trajectoryLength;
	
	public Trajectory(int trajectoryLength) {
		this.trajectoryLength = trajectoryLength;
		trajectory = new Segment[trajectoryLength];
	}
	
	public void addSegment(int index, Segment segment){
		trajectory[index] = segment;
	}
	
	public int getLength(){
		return trajectoryLength;
	}
	
	public Segment getCurrentSegment(int index){
		return trajectory[index];
	}
}
