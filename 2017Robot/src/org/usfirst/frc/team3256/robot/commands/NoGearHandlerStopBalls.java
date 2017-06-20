package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.robot.subsystems.Roller;
import org.usfirst.frc.team3256.robot.subsystems.Roller.RollerState;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class NoGearHandlerStopBalls extends InstantCommand {

	Roller roller = Roller.getInstance();
	
    public NoGearHandlerStopBalls() {
        super();
        requires(roller);
    }

    // Called once when the command executes
    protected void initialize() {
    	roller.setRollerState(RollerState.STOPPED);
    }

}
