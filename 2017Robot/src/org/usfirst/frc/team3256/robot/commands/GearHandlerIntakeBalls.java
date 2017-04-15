package org.usfirst.frc.team3256.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class GearHandlerIntakeBalls extends CommandGroup {

    public GearHandlerIntakeBalls() {
        addSequential(new GearHandlerBallsPos());
        addSequential(new NoGearHandlerGroundIntakeBalls());
    }
}
