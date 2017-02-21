package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.robot.subsystems.Manipulator;
import org.usfirst.frc.team3256.robot.subsystems.Manipulator.IntakeState;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class StopRollers extends InstantCommand {

	Manipulator manipulator = Manipulator.getInstance();
	
	//TESTED ON HARDWARE
    public StopRollers() {
        super();
        requires(manipulator);
    }

    // Called once when the command executes
    protected void initialize() {
    	manipulator.setIntakeState(IntakeState.LIFT_FOR_DRIVING);
    }

}
