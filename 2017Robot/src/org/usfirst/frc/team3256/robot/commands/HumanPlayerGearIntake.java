package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.robot.subsystems.Manipulator;
import org.usfirst.frc.team3256.robot.subsystems.Manipulator.HumanPlayerLoadingState;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class HumanPlayerGearIntake extends InstantCommand {
	
	Manipulator manipulator = Manipulator.getInstance();
	
	//no motors, switches actuators to intake gear state
	//gears from human loading station fall into robot
    public HumanPlayerGearIntake() {
        requires(manipulator);
    }

    // Called once when the command executes
    protected void initialize() {
    	manipulator.setHumanLoadingState(HumanPlayerLoadingState.GEAR_INTAKE);
    	
    }

}
