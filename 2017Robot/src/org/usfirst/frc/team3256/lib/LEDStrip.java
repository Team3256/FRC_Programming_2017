package org.usfirst.frc.team3256.lib;

import org.usfirst.frc.team3256.robot.subsystems.GearHandler;
import org.usfirst.frc.team3256.robot.subsystems.GearHandler.GearHandlerState;

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
	
	public void update(boolean flash) {
		GearHandlerState gearHandlerState = GearHandler.getInstance().getGearHandlerState();
		boolean hasGear = GearHandler.getInstance().hasGear();
		
		// Gets decimal portion of time
		double timeDecimal = Timer.getFPGATimestamp() - (int) Timer.getFPGATimestamp();
		// Blinks every quarter of a second, or if we want it solid
		//either leave led as solid or blink every quarter second
		//otherwise disable led
		boolean enableLEDs = !flash || (timeDecimal < 0.25 || (timeDecimal >= 0.5 && timeDecimal < 0.75)); 
		
		// Blue = gear handler stowed
		this.blue.set(gearHandlerState != GearHandlerState.INTAKE && (Timer.getFPGATimestamp() - lastPickupTime >= 3 || !hasGear));
	
		// Red = gear handler down, no gear collected
		this.red.set(gearHandlerState == GearHandlerState.INTAKE && enableLEDs);

		// If just detected gear (START_PIVOT_FOR_STOW state only occurs once immediately after gear is collected,
		// goes to STOW state afterwards), then remember this point in time
		if (gearHandlerState == GearHandlerState.START_PIVOT_FOR_STOW)
			lastPickupTime = Timer.getFPGATimestamp();
		
		//enable green for 3 seconds after we pick up a gear
		this.green.set((Timer.getFPGATimestamp() - lastPickupTime < 3 && hasGear) && enableLEDs);
	}
}
