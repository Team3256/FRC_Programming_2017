package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.robot.subsystems.GearHandler;
import org.usfirst.frc.team3256.robot.subsystems.GearHandler.GearHandlerState;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoDeployFrontGear extends Command {

	GearHandler gearHandler = GearHandler.getInstance();
	
    public AutoDeployFrontGear() {
    	requires(gearHandler);
    	setTimeout(1);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	gearHandler.setState(GearHandlerState.START_PIVOT_FOR_DEPLOY);
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
    	gearHandler.setState(GearHandlerState.START_PIVOT_FOR_STOW_WITH_GEAR);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
