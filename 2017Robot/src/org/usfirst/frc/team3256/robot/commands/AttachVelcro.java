package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.robot.subsystems.Hanger;
import org.usfirst.frc.team3256.robot.subsystems.Hanger.HangerState;
import org.usfirst.frc.team3256.robot.subsystems.Roller.RollerState;
import org.usfirst.frc.team3256.robot.subsystems.Roller;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AttachVelcro extends Command {

	Hanger hanger = Hanger.getInstance();
	Roller roller = Roller.getInstance();
	
    public AttachVelcro() {
    	requires(hanger);
    	requires(roller);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	hanger.setHangerState(HangerState.VELCRO);
    	roller.setRollerState(RollerState.STOPPED);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	//stop hang 
    	hanger.setHangerState(HangerState.WINCH_STOP);
    	roller.setRollerState(RollerState.STOPPED);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
