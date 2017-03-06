package org.usfirst.frc.team3256.robot.automodes;

import org.usfirst.frc.team3256.robot.commands.DriveToDistance;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class GearRightAuto extends CommandGroup {
	public GearRightAuto() {
		addSequential(new DriveToDistance(40, false));
	}
}
