package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.robot.subsystems.GearHandler;
import org.usfirst.frc.team3256.robot.subsystems.GearHandler.GearHandlerState;

import edu.wpi.first.wpilibj.command.Command;

public class GearHandlerDown extends Command {
	
	GearHandler gearHandler = GearHandler.getInstance();
	
	public GearHandlerDown() {
		requires(gearHandler);
	}
	
	@Override
	protected void execute() {
		gearHandler.setState(GearHandlerState.INTAKE);
	}

	@Override
	protected boolean isFinished() {
		return gearHandler.isDown();
	}
	
	@Override
	protected void end() {
		gearHandler.setState(GearHandlerState.STOP);
	}
	
	@Override
	protected void interrupted() {
    	end();
    }
}
