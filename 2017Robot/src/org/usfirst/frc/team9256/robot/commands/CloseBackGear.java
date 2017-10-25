package org.usfirst.frc.team9256.robot.commands;

import org.usfirst.frc.team9256.robot.subsystems.Manipulator;
import org.usfirst.frc.team9256.robot.subsystems.Manipulator.HumanPlayerLoadingState;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class CloseBackGear extends InstantCommand {

	Manipulator manipulator = Manipulator.getInstance();
	
    public CloseBackGear() {
        super();
        requires(manipulator);
    }

    // Called once when the command executes
    protected void initialize() {
    	manipulator.setHumanLoadingState(HumanPlayerLoadingState.GEAR_RETRACT);
    }

}
