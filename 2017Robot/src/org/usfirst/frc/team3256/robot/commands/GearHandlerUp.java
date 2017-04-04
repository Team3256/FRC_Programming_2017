package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.robot.subsystems.GearHandler;
import org.usfirst.frc.team3256.robot.subsystems.GearHandler.GearHandlerState;

import edu.wpi.first.wpilibj.command.Command;

public class GearHandlerUp extends Command {
	
	GearHandler gearHandler = GearHandler.getInstance();
	
	public GearHandlerUp() {
		requires(gearHandler);
	}
	
	@Override
	protected void execute() {
		gearHandler.setState(GearHandlerState.FLIP_UP);
	}

	@Override
	protected boolean isFinished() {
		return gearHandler.isUp();
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
