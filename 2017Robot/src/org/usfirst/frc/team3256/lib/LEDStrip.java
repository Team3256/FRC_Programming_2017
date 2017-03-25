package org.usfirst.frc.team3256.lib;

import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;

public class LEDStrip {
	
	private Relay power;
	private PWM R;
	private PWM G;
	private PWM B;
	
	public LEDStrip(int powerPort, int rPort, int gPort, int bPort){
		power = new Relay(powerPort);
		R = new PWM(rPort);
		G = new PWM(gPort);
		B = new PWM(bPort);
		this.setColor(BLUE); 
	}
	
	public static class Color{
		private int red, green, blue;
		public Color(int red, int green, int blue){
			this.red = red;
			this.blue = blue;
			this.green = green;
		}
	}

	Color GREEN = new Color(0,255,0);
	Color BLUE = new Color(0,0,255);
	Color RED = new Color(255,0,0);
	
	public void setColor(Color color){
		power.set(Value.kOn);
		R.setRaw(color.red);
		G.setRaw(color.green);
		B.setRaw(color.blue);
	}
	
	public void turnOff(){
		power.set(Value.kOff);
	}
}
