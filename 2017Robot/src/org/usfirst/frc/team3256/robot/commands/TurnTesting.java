package org.usfirst.frc.team3256.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;


/**
 *
 */
public class TurnTesting extends CommandGroup {

    public TurnTesting() {
    	//addSequential(new MotionProfiledTurn(90, true));
        addSequential(new SmallTurn(3, true));
        addSequential(new WaitCommand(1));
        //addSequential(new SmallTurn(3, false));
    }
}
