package org.usfirst.frc.team3256.robot.automodes;

import org.usfirst.frc.team3256.robot.commands.DriveToDistance;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class BaselineCross extends CommandGroup {
	public BaselineCross() {
		addSequential(new DriveToDistance(70, false));
	}
}
