package org.usfirst.frc.team9256.robot.commands;

import org.usfirst.frc.team9256.robot.subsystems.GearHandler;
import org.usfirst.frc.team9256.robot.subsystems.GearHandler.GearHandlerState;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class StopGearRoller extends InstantCommand {

	GearHandler gearHandler = GearHandler.getInstance();
	
    public StopGearRoller() {
        super();
        requires(gearHandler);
    }

    // Called once when the command executes
    protected void initialize() {
    	gearHandler.setState(GearHandlerState.STOPPED);
    }

}
