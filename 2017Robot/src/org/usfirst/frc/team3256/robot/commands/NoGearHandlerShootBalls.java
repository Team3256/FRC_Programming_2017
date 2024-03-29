package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.robot.subsystems.Roller;
import org.usfirst.frc.team3256.robot.subsystems.Roller.RollerState;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class NoGearHandlerShootBalls extends Command {

	Roller roller = Roller.getInstance();
	
	//TESTED ON HARDWARE
    public NoGearHandlerShootBalls() {
        requires(roller);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	roller.setRollerState(RollerState.SPIT_BALLS);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	roller.setRollerState(RollerState.STOPPED);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
