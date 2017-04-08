package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.robot.subsystems.GearHandler;
import org.usfirst.frc.team3256.robot.subsystems.GearHandler.GearHandlerState;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class StopFrontGear extends InstantCommand {
	public StopFrontGear() {
		requires(GearHandler.getInstance());
	}
	
	protected void initialize() {
		GearHandler.getInstance().setState(GearHandlerState.STOPPED);
	}
}
