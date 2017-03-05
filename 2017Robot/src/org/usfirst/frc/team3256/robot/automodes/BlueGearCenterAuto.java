package org.usfirst.frc.team3256.robot.automodes;

import org.usfirst.frc.team3256.lib.DelayedCommand;
import org.usfirst.frc.team3256.robot.commands.DeployGear;
import org.usfirst.frc.team3256.robot.commands.DriveToDistance;
import org.usfirst.frc.team3256.robot.commands.MotionProfiledTurn;
import org.usfirst.frc.team3256.robot.commands.ShootBalls;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class BlueGearCenterAuto extends CommandGroup {

	public BlueGearCenterAuto() {
		// drive backwards to center gear peg
		addSequential(new DriveToDistance(69, false));

		//deploy gear
		addParallel(new DeployGear());
		
		//drive forward
		addSequential(new DelayedCommand(1,new DriveToDistance(60,true)));
		
		/*
		// turn so the roller side faces the boiler
		addSequential(new MotionProfiledTurn(90, true));
		// drive to the boiler
		addSequential(new DriveToDistance(110, true));
		// turn 45 degrees to face boiler
		addSequential(new MotionProfiledTurn(45, false));
		// drive up to the boiler
		addSequential(new DriveToDistance(30, true));
		// shoot balls
		addSequential(new ShootBalls());
		*/
	}
}
