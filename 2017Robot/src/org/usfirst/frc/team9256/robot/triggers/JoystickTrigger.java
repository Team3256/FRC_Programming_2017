package org.usfirst.frc.team9256.robot.triggers;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Trigger;

/**
 * 
 */
public class JoystickTrigger extends Trigger {

	private final GenericHID joystick;
	private final int port;
	
	public JoystickTrigger(GenericHID joystick, int port){
		this.joystick = joystick;
		this.port = port;
	}
	
	@Override
    public boolean get() {
        return joystick.getRawAxis(port)>0.5;
    }
}
