package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.robot.subsystems.GearHandler;
import org.usfirst.frc.team3256.robot.subsystems.GearHandler.GearHandlerState;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class StowLowGearHandler extends InstantCommand {
	
	GearHandler gearHandler = GearHandler.getInstance();
	
    public StowLowGearHandler() {
        super();
        requires(gearHandler);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	gearHandler.setState(GearHandlerState.START_PIVOT_FOR_STOW_LOW);
    }

}