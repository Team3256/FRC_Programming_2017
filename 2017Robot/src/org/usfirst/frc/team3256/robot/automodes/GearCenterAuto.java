package org.usfirst.frc.team3256.robot.automodes;

import org.usfirst.frc.team3256.lib.DelayedCommand;
import org.usfirst.frc.team3256.robot.commands.DeployBackGear;
import org.usfirst.frc.team3256.robot.commands.DriveToDistance;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * simply drives to gear and back
 */
public class GearCenterAuto extends CommandGroup {

	public GearCenterAuto() {
		// drive backwards to center gear peg
		addSequential(new DriveToDistance(77, false));

		//deploy gear
		addParallel(new DeployBackGear());
		
		//drive forward
		addSequential(new DelayedCommand(.2,new DriveToDistance(60,true)));

	}
}
