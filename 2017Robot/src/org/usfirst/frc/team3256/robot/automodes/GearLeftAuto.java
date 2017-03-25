package org.usfirst.frc.team3256.robot.automodes;

import org.usfirst.frc.team3256.lib.DelayedCommand;
import org.usfirst.frc.team3256.robot.commands.AlignToVision;
import org.usfirst.frc.team3256.robot.commands.DeployGear;
import org.usfirst.frc.team3256.robot.commands.DriveToDistance;
import org.usfirst.frc.team3256.robot.commands.HumanPlayerBallsIntake;
import org.usfirst.frc.team3256.robot.commands.PIDTurn;
import org.usfirst.frc.team3256.robot.commands.ShootBalls;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class GearLeftAuto extends CommandGroup {

    public GearLeftAuto(boolean boiler) {
    	addSequential(new DriveToDistance(67, false)); //initial drive forward
		addSequential(new WaitCommand(0.5));
		addSequential(new PIDTurn(60, true)); //turn towards gear
		addSequential(new WaitCommand(0.5));
		addSequential(new DriveToDistance(65, false)); //drive towards gear
		addParallel(new DeployGear());
		addSequential(new DelayedCommand(1, new DriveToDistance(20, true))); //drive backwards to boiler
		if (boiler) {
			addSequential(new PIDTurn(25, false)); //turn towards boiler
			addSequential(new DriveToDistance(50, true)); //drive to boiler
			addSequential(new ShootBalls()); //shoot balls into boiler
		}
    }
}
