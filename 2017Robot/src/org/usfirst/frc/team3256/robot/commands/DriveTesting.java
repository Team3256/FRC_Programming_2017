package org.usfirst.frc.team3256.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class DriveTesting extends CommandGroup {

    public DriveTesting() {	
    	System.out.print("STARTED DRIVE TESTING-----------------------------");
        addSequential(new DriveToDistance(60,false));
        //addSequential(new WaitCommand(1));
        //addSequential(new DriveToDistance(60,true));
    }
}
 	