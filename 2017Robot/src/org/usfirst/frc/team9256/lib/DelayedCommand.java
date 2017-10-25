package org.usfirst.frc.team9256.lib;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class DelayedCommand extends CommandGroup {

    public DelayedCommand(double delay, Command c) {
    	addSequential(new WaitCommand(delay));
    	addSequential(c);
    }
}
