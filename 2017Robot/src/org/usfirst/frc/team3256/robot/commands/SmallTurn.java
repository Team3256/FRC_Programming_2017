package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SmallTurn extends Command {

	private DriveTrain drive = DriveTrain.getInstance();
	private double degrees;
	private boolean turnRight;
	
    public SmallTurn(double degrees, boolean turnRight) {
    	requires(drive);
    	this.degrees = degrees;
    	this.turnRight = turnRight;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	drive.resetGyro();
    	drive.shiftUp(false);
    	drive.setAlignSetpoint(degrees, turnRight);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return drive.isFinishedAlign();
    }

    // Called once after isFinished returns true
    protected void end() {
    	System.out.println("FINISHED");
    	drive.setOpenLoop(0, 0);
    	drive.shiftUp(true);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    	System.out.println("Small turn INTERRUPTED");
    }
}
