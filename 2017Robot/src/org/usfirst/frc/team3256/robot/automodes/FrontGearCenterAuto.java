package org.usfirst.frc.team3256.robot.automodes;

import org.usfirst.frc.team3256.lib.DelayedCommand;
import org.usfirst.frc.team3256.robot.commands.AutoDeployFrontGear;
import org.usfirst.frc.team3256.robot.commands.DriveToDistance;
import org.usfirst.frc.team3256.robot.commands.StowGearHandler;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class FrontGearCenterAuto extends CommandGroup {

    public FrontGearCenterAuto() {
    	addSequential(new StowGearHandler());
    	

		addSequential(new DriveToDistance(77, true));
			
		addParallel(new AutoDeployFrontGear());

		addSequential(new DelayedCommand(.2,new DriveToDistance(30,false)));
    }
}
