package org.usfirst.frc.team3256.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class TurnTesting extends CommandGroup {

    public TurnTesting() {
        addSequential(new TurnToAngle(180,true));
    }
}
