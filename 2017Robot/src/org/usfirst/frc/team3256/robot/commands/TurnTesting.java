package org.usfirst.frc.team3256.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;


/**
 *
 */
public class TurnTesting extends CommandGroup {

    public TurnTesting() {
        for(int i=0;i<1;i++){
        	addSequential(new PIDTurn(90,true));
        }
    }
}
