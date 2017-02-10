package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.robot.subsystems.Manipulator;
import org.usfirst.frc.team3256.robot.subsystems.Manipulator.IntakeState;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ShootBalls extends Command {

	Manipulator manipulator = Manipulator.getInstance();
	
	//TESTED ON HARDWARE
    public ShootBalls() {
        requires(manipulator);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	manipulator.setIntakeState(IntakeState.SPIT_BALLS);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	manipulator.setIntakeState(IntakeState.HOLD_BALLS);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
