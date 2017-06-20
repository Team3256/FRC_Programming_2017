package org.usfirst.frc.team3256.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class GearHandlerShootBalls extends CommandGroup {

    public GearHandlerShootBalls() {
        addSequential(new GearHandlerBallsPos());
        addSequential(new NoGearHandlerShootBalls());
    }
}
