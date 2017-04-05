package org.usfirst.frc.team3256.robot.triggers;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Button;

public class DualButton extends Button{

	private final GenericHID joystick;
	private final int portButtonOne, portButtonTwo;
	
	public DualButton(GenericHID joystick, int portButtonOne, int portButtonTwo){
		this.joystick = joystick;
		this.portButtonOne = portButtonOne;
		this.portButtonTwo = portButtonTwo;
	}
	
	@Override
	public boolean get() {
		return joystick.getRawButton(portButtonOne) && joystick.getRawButton(portButtonTwo);
	}
	
}
