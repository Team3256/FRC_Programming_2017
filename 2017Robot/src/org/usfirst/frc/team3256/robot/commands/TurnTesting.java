package org.usfirst.frc.team3256.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;


/**
 *
 */
public class TurnTesting extends CommandGroup {

    public TurnTesting() {
        addSequential(new TurnToAngle(90,false));
        /*
        addSequential(new TurnToAngle(90,true));
        addSequential(new WaitCommand(1));
        addSequential(new TurnToAngle(90,true));
        addSequential(new WaitCommand(1));
        addSequential(new TurnToAngle(90,true));
        */
    }
}
