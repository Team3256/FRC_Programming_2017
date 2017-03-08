package org.usfirst.frc.team3256.robot.automodes;

import org.usfirst.frc.team3256.robot.commands.DriveToDistance;
import org.usfirst.frc.team3256.robot.commands.PIDTurn;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class HopperAutoRed extends CommandGroup{
	public HopperAutoRed() {
		addSequential(new DriveToDistance(50, false)); //initial drive forward
		addSequential(new WaitCommand(0.5));
		addSequential(new PIDTurn(90, true)); //turn towards hopper
		addSequential(new WaitCommand(0.5));
		addSequential(new DriveToDistance(30, false)); //trigger hopper
	}
}
