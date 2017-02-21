package org.usfirst.frc.team3256.robot.automodes;

import org.usfirst.frc.team3256.robot.commands.DriveToDistance;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class GearCenterAuto extends CommandGroup {
	public GearCenterAuto() {
		addSequential(new DriveToDistance(53, false)); //drive backwards to center gear peg
	}
}
