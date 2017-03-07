package org.usfirst.frc.team3256.robot.automodes;

import org.usfirst.frc.team3256.lib.DelayedCommand;
import org.usfirst.frc.team3256.robot.commands.AlignToVision;
import org.usfirst.frc.team3256.robot.commands.DeployGear;
import org.usfirst.frc.team3256.robot.commands.DriveToDistance;
import org.usfirst.frc.team3256.robot.commands.PIDTurn;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class GearRightAuto extends CommandGroup {
	public GearRightAuto() {
		addSequential(new DriveToDistance(67, false)); //initial drive forward
		addSequential(new WaitCommand(0.5));
		addSequential(new PIDTurn(60, false)); //turn towards gear
		addSequential(new WaitCommand(0.5));
		addSequential(new DriveToDistance(46, false)); //drive towards gear
		addSequential(new WaitCommand(0.5));
		addSequential(new AlignToVision()); //ensure proper alignment
		addSequential(new WaitCommand(0.5));
		addSequential(new DriveToDistance(17, false)); //final drive towards gear
		addParallel(new DeployGear());
		addSequential(new DelayedCommand(1, new DriveToDistance(20, true))); //drive backwards 
	}
}
