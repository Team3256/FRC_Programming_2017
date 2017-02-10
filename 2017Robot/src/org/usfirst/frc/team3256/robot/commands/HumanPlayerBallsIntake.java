package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.robot.subsystems.Manipulator;
import org.usfirst.frc.team3256.robot.subsystems.Manipulator.HumanPlayerLoadingState;
import org.usfirst.frc.team3256.robot.subsystems.Manipulator.IntakeState;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class HumanPlayerBallsIntake extends InstantCommand {
	
	Manipulator manipulator = Manipulator.getInstance();
	
    public HumanPlayerBallsIntake() {
        requires(manipulator);
    }

    // Called once when the command executes
    protected void initialize() {
    	manipulator.setHumanLoadingState(HumanPlayerLoadingState.BALLS_INTAKE);
    }

}