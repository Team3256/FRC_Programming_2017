package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.robot.subsystems.Hanger;
import org.usfirst.frc.team3256.robot.subsystems.Hanger.HangerState;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RunHang extends Command {

	Hanger hanger = Hanger.getInstance();
	
    public RunHang() {
    	requires(hanger);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	hanger.setHangerState(HangerState.WINCH_UP);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	//stop hang 
    	hanger.setHangerState(HangerState.WINCH_STOP);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
