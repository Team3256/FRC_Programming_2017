package org.usfirst.frc.team3256.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**                                                                                                                                                                                                                                                 
 *
 */
public class DriveTesting extends CommandGroup {

    public DriveTesting() {	
    	System.out.print("STARTED DRIVE TESTING-----------------------------");
    	addSequential(new DriveToDistance(2, true));
        //addSequential(new DriveToDistance(60,false));
        //addSequential(new WaitCommand(1));
        //addSequential(new DriveToDistance(60,true));
    }
}
 	