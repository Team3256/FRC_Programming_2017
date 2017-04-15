package org.usfirst.frc.team3256.robot.automodes;

import org.usfirst.frc.team3256.lib.DelayedCommand;
import org.usfirst.frc.team3256.robot.commands.DeployBackGear;
import org.usfirst.frc.team3256.robot.commands.DriveToDistance;
import org.usfirst.frc.team3256.robot.commands.PIDTurn;
import org.usfirst.frc.team3256.robot.commands.ShiftCommand;
import org.usfirst.frc.team3256.robot.commands.NoGearHandlerShootBalls;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class GearLeftAuto extends CommandGroup {

    public GearLeftAuto(boolean boiler) {
    	addSequential(new DriveToDistance(67, false)); //initial drive forward
		addSequential(new WaitCommand(0.5));
		addSequential(new ShiftCommand(false));
		addSequential(new WaitCommand(0.5));
		addSequential(new PIDTurn(60, true)); //turn towards gear
		addSequential(new WaitCommand(0.5));
		addSequential(new DriveToDistance(65, false)); //drive towards gear
		addParallel(new DeployBackGear());
		addSequential(new DelayedCommand(1, new DriveToDistance(20, true))); //drive backwards to boiler
		if (boiler) {
			addSequential(new PIDTurn(25, false)); //turn towards boiler
			addSequential(new DriveToDistance(70, true)); //drive to boiler
			addSequential(new NoGearHandlerShootBalls()); //shoot balls into boiler
		}
    }
}
