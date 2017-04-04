package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.robot.subsystems.GearHandler;
import org.usfirst.frc.team3256.robot.subsystems.GearHandler.GearHandlerState;

import edu.wpi.first.wpilibj.command.Command;

public class GearHandlerDeploy extends Command {
	
	GearHandler gearHandler = GearHandler.getInstance();
	
	public GearHandlerDeploy() {
		requires(gearHandler);
	}
	
	@Override
	protected void execute() {
		gearHandler.setState(GearHandlerState.DEPLOY);
	}

	@Override
	protected boolean isFinished() {
		return gearHandler.gearDeployFinished();
	}
	
	@Override
	protected void end() {
		gearHandler.setState(GearHandlerState.STOP);
	}
}
