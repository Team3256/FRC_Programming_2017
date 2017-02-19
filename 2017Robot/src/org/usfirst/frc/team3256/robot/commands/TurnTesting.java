package org.usfirst.frc.team3256.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;


/**
 *
 */
public class TurnTesting extends CommandGroup {

    public TurnTesting() {
        addSequential(new MotionProfiledTurn(90,true));
    }
}
