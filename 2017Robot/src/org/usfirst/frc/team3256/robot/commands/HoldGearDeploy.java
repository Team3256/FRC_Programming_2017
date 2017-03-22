package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.robot.subsystems.Manipulator;
import org.usfirst.frc.team3256.robot.subsystems.Manipulator.HumanPlayerLoadingState;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class HoldGearDeploy extends InstantCommand {

	Manipulator manipulator = Manipulator.getInstance();
	
    public HoldGearDeploy() {
        super();
        requires(manipulator);
    }

    // Called once when the command executes
    protected void initialize() {
    	manipulator.setHumanLoadingState(HumanPlayerLoadingState.GEAR_DEPLOY);
    }

}
