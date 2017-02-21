package org.usfirst.frc.team3256.robot.automodes;

import org.usfirst.frc.team3256.robot.commands.DeployGear;
import org.usfirst.frc.team3256.robot.commands.DriveToDistance;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class GearCenterAuto extends CommandGroup {
	public GearCenterAuto() {
		//drive backwards to center gear peg
		addSequential(new DriveToDistance(53, false)); 
		addSequential(new DeployGear());
	}
}
