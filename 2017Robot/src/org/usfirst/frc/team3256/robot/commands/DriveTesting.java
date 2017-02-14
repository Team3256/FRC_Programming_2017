package org.usfirst.frc.team3256.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DriveTesting extends CommandGroup {

    public DriveTesting() {
        addSequential(new DriveToDistance(12,true));
    }
}
 	