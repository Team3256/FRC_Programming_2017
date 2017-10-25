package org.usfirst.frc.team9256.robot.commands;

import org.usfirst.frc.team9256.robot.subsystems.Manipulator;
import org.usfirst.frc.team9256.robot.subsystems.Manipulator.HumanPlayerLoadingState;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DeployBackGear extends Command {

	Manipulator manipulator = Manipulator.getInstance();
	
    public DeployBackGear() {
       requires(manipulator);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	setTimeout(2);
    	manipulator.setHumanLoadingState(HumanPlayerLoadingState.GEAR_DEPLOY);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	manipulator.setHumanLoadingState(HumanPlayerLoadingState.GEAR_RETRACT);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
