package org.usfirst.frc.team3256.lib;

import org.usfirst.frc.team3256.robot.subsystems.GearHandler;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

public class LEDStrip {
	
	private Solenoid red;
	private Solenoid green;
	private Solenoid blue;
	
	private int pcmID = 1;
	private int rPort = 7;
	private int gPort = 6;
	private int bPort = 5;
	
	private static LEDStrip instance;
	
	public static LEDStrip getInstance(){
		return instance == null ? instance = new LEDStrip() : instance;
	}
	
	private LEDStrip(){
		red = new Solenoid(pcmID, rPort);
		green = new Solenoid(pcmID, gPort);
		blue = new Solenoid(pcmID, bPort);
	}
	
	public void red(){
		red.set(true);
		green.set(false);
		blue.set(false);
	}
	
	public void green(){
		red.set(false);
		green.set(true);
		blue.set(false);
	}
	
	public void blue(){
		red.set(false);
		green.set(false);
		blue.set(true);
	}
	
	public void turnOff(){
		red.set(false);
		green.set(false);
		blue.set(false);
	}
	
	public void set(boolean r, boolean g, boolean b){
		red.set(r);
		green.set(g);
		blue.set(b);
	}
	
	public void update() {
		double timeEnd = Timer.getFPGATimestamp() - (int) Timer.getFPGATimestamp(); //gets decimal portion of time stamp
		boolean properTime = timeEnd < 0.25 || (timeEnd >= 0.5 && timeEnd < 0.75); //blinks led every quarter second
		this.green.set(GearHandler.getInstance().hasGear() && properTime);   // set to green if handler has a gear
		this.red.set(!GearHandler.getInstance().hasGear() && properTime);    // set to red if it doesn't
		this.blue.set(DriverStation.getInstance().getMatchTime() <= 30 && !properTime);  //set to blue if in last 30 seconds, and switch with red/green instead of at the same time
	}
}
