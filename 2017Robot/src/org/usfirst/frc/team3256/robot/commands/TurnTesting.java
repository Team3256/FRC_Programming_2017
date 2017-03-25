package org.usfirst.frc.team3256.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;


/**
 *
 */
public class TurnTesting extends CommandGroup {

    public TurnTesting() {
        addSequential(new PIDTurn(60,true));
    }
}
