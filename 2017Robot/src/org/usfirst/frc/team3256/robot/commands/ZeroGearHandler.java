package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.subsystems.GearHandler;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class ZeroGearHandler extends InstantCommand {
	private GearHandler gearHandler = GearHandler.getInstance();
	
	public ZeroGearHandler() {
		requires(gearHandler);
	}
	
	protected void initialize() {
		gearHandler.setEncoderPosition(Constants.GEAR_PIVOT_GROUND_POS);
	}
}
