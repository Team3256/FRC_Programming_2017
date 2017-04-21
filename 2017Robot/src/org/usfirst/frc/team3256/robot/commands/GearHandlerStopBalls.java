package org.usfirst.frc.team3256.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class GearHandlerStopBalls extends CommandGroup {

    public GearHandlerStopBalls() {
        addParallel(new StowLowGearHandler());
        addSequential(new NoGearHandlerStopBalls());
    }
}
