package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.robot.subsystems.Manipulator;
import org.usfirst.frc.team3256.robot.subsystems.Manipulator.HumanPlayerLoadingState;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DeployGear extends Command {

	Manipulator manipulator = Manipulator.getInstance();
	
    public DeployGear() {
       requires(manipulator);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	manipulator.setHumanLoadingState(HumanPlayerLoadingState.GEAR_DEPLOY);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    	manipulator.setHumanLoadingState(HumanPlayerLoadingState.GEAR_INTAKE);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
