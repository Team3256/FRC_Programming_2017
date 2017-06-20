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
		
		if (gearHandlerState == GearHandlerState.START_PIVOT_FOR_STOW) {
			lastPickupTime = Timer.getFPGATimestamp();
		}
		
		// Gets decimal portion of time
		double timeDecimal = Timer.getFPGATimestamp() - (int) Timer.getFPGATimestamp();
		
		// If the flash 
		boolean flashTime = timeDecimal < 0.25 || (timeDecimal >= 0.5 && timeDecimal < 0.75);
		boolean enableLEDs = !flash || flashTime; 
		
		if (gearHandlerState == GearHandlerState.START_PIVOT_FOR_STOW) {
			lastPickupTime = Timer.getFPGATimestamp();
		}
		
		// These conditionals are expanded into if/else statements rather than boolean algebra
		// for clarity
		// Convert them back into boolean algebra at your own risk :)

		// The encoder case overrides the rest of the cases
		if (!GearHandler.getInstance().hasEncoder()) {
			if (flashTime) {
				this.blue.set(true);
				this.red.set(true);
			}
			else {
				this.blue.set(false);
				this.red.set(false);
			}
			return;
		}
		
		// First make sure if it is even possible for us to turn on LEDs
		// according to our rules for when the LEDs should be turned on
		if (enableLEDs) {
			
			// Blue
			if (gearHandlerState != GearHandlerState.GEAR_INTAKE) {
				if (Timer.getFPGATimestamp() - lastPickupTime >= 3 || !hasGear) {
					this.blue.set(true);
				}
				else {
					this.blue.set(false);
				}
			}
			else {
				this.blue.set(false);
			}
			
			// Red
			if (gearHandlerState == GearHandlerState.GEAR_INTAKE) {
				this.red.set(true);
			}
			else {
				this.red.set(false);
			}
			
			// Green
			if (hasGear) {
				if (Timer.getFPGATimestamp() - lastPickupTime < 3) {
					this.green.set(true);
				}
				else {
					this.green.set(false);
				}
			}
			else {
				this.green.set(false);
			}
		}
		
		
		// Blue = gear handler stowed
		//this.blue.set((gearHandlerState != GearHandlerState.GEAR_INTAKE && (Timer.getFPGATimestamp() - lastPickupTime >= 3 || !hasGear) || (GearHandler.getInstance().hasEncoder() && enableLEDs)));
	
		// Red = gear handler down, no gear collected
		//this.red.set(gearHandlerState == GearHandlerState.GEAR_INTAKE && enableLEDs);

		// If just detected gear (START_PIVOT_FOR_STOW state only occurs once immediately after gear is collected,
		// goes to STOW state afterwards), then remember this point in time
		
		//enable green for 3 seconds after we pick up a gear
		//this.green.set((Timer.getFPGATimestamp() - lastPickupTime < 3 && hasGear) && enableLEDs);
	}
}
