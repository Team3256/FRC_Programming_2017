package org.usfirst.frc.team3256.lib;

import org.usfirst.frc.team3256.robot.subsystems.GearHandler;
import org.usfirst.frc.team3256.robot.subsystems.GearHandler.GearHandlerState;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

public class LEDStrip {
	private double lastPickupTime = 0; //holds the last time a gear was picked up
	
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
		GearHandlerState gearHandlerState = GearHandler.getInstance().getGearHandlerState();
		double timeDecimal = Timer.getFPGATimestamp() - (int) Timer.getFPGATimestamp(); //gets decimal portion of time stamp
		boolean flashTime = timeDecimal < 0.25 || (timeDecimal >= 0.5 && timeDecimal < 0.75); //blinks led every quarter second
		
		this.blue.set(gearHandlerState != GearHandlerState.INTAKE && Timer.getFPGATimestamp() - lastPickupTime > 3);
		this.red.set(gearHandlerState == GearHandlerState.INTAKE && flashTime);
		if (gearHandlerState == GearHandlerState.START_PIVOT_FOR_STOW)
			lastPickupTime = Timer.getFPGATimestamp();
		this.green.set(Timer.getFPGATimestamp() - lastPickupTime <= 3 && flashTime);
	}
}
