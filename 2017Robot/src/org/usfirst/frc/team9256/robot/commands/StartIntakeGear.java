package org.usfirst.frc.team9256.robot.commands;

import org.usfirst.frc.team9256.robot.subsystems.GearHandler;
import org.usfirst.frc.team9256.robot.subsystems.GearHandler.GearHandlerState;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class StartIntakeGear extends InstantCommand {

	GearHandler gearHandler = GearHandler.getInstance();
	
    public StartIntakeGear() {
        super();
        requires(gearHandler);
    }

    // Called once when the command executes
    protected void initialize() {
    	gearHandler.setState(GearHandlerState.START_PIVOT_FOR_GEAR_INTAKE);
    }

}
