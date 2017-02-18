package org.usfirst.frc.team3256.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class DriveTesting extends CommandGroup {

    public DriveTesting() {
        //addSequential(new DriveToDistance(120,true));
        //addSequential(new WaitCommand(1));
        //addSequential(new DriveToDistance(120,false));
        for (int i = 0; i < 4; ++i) {
        	addSequential(new TurnToAngle(90, true));
        	addSequential(new WaitCommand(1));
        }
    }
}
 	