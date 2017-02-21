package org.usfirst.frc.team3256.lib;

import java.io.FileWriter;
import java.io.IOException;

import org.usfirst.frc.team3256.robot.Constants;

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
	
	@Override
	public String toString(){
		String ret = "";
		for(int i=0;i<trajectory.length;i++){
			ret += trajectory[i].pos + " 0\n";
		}
		ret += "\n\n";
		for(int i=0;i<trajectory.length;i++){
			ret += trajectory[i].vel + " 0\n";
		}
		ret += "\n\n";
		for(int i=0;i<trajectory.length;i++){
			ret += trajectory[i].accel + " 0\n";
		}
		ret += "\n\n";
		for(int i=0;i<trajectory.length;i++){	
			ret += trajectory[i].time + " " + trajectory[i].pos + " \n";
		}
		return ret;
	}
	
	public static void main(String args[]){
		TrajectoryGenerator generator = new TrajectoryGenerator();
		generator.setConfig(Constants.MAX_VEL_HIGH_GEAR_IN, Constants.MAX_ACCEL_HIGH_GEAR_IN2, Constants.CONTROL_LOOP_DT);
		Trajectory traj = generator.generateTraj(0, 0, 60);
		System.out.println(traj);
	}
}
