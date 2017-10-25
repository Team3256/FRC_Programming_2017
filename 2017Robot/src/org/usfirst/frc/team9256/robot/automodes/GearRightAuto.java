package org.usfirst.frc.team9256.robot.automodes;

import org.usfirst.frc.team9256.lib.DelayedCommand;
import org.usfirst.frc.team9256.robot.commands.DeployBackGear;
import org.usfirst.frc.team9256.robot.commands.DriveToDistance;
import org.usfirst.frc.team9256.robot.commands.PIDTurn;
import org.usfirst.frc.team9256.robot.commands.ShiftCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class GearRightAuto extends CommandGroup {
	public GearRightAuto(boolean boiler) {
		addSequential(new DriveToDistance(67, false)); //initial drive forward
		addSequential(new DelayedCommand(0.25, new ShiftCommand(false)));
		addSequential(new DelayedCommand(0.25, new PIDTurn(60, false, false))); //turn towards gear
		addSequential(new DelayedCommand(0.5, new DriveToDistance(65, false))); //drive towards gear
		addParallel(new DeployBackGear());
		addSequential(new DelayedCommand(0.6, new DriveToDistance(45, true))); //drive backwards
		addSequential(new DelayedCommand(0.5, new PIDTurn(60, true, false))); //turn to midfield
		addSequential(new DelayedCommand(0.5, new DriveToDistance(60, false))); //drive to midfield
	}
}
