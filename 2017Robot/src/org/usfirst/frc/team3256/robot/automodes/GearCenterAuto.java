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
public class GearCenterAuto extends CommandGroup {

	public GearCenterAuto() {
		// drive backwards to center gear peg
		addSequential(new DriveToDistance(69, false));

		//deploy gear
		addParallel(new DeployGear());
		
		//drive forward
		addSequential(new DelayedCommand(1,new DriveToDistance(60,true)));

	}
}
