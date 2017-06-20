package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.robot.subsystems.GearHandler;
import org.usfirst.frc.team3256.robot.subsystems.GearHandler.GearHandlerState;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class StowGearHandler extends InstantCommand {

	GearHandler gearHandler = GearHandler.getInstance();
	
    public StowGearHandler() {
        super();
        requires(gearHandler);
    }

    // Called once when the command executes
    protected void initialize() {
    	gearHandler.setState(GearHandlerState.START_PIVOT_FOR_STOW_WITH_GEAR);
    }

}
