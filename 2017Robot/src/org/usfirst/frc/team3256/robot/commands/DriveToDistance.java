package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveToDistance extends Command {

	DriveTrain drive = DriveTrain.getInstance();
	private double setpoint;
	private boolean goForward;
	
	/**
	 * @param setpoint desired distance
	 * @param goForward desired direction
	 */
    public DriveToDistance(double setpoint, boolean goForward) {
        requires(drive);
        this.setpoint = setpoint;
        this.goForward = goForward;
    }

    /**
     * Initializes the drivetrain and this command
     */
    protected void initialize() {
    	drive.shiftUp(true);
    	drive.setDriveStraightSetpoint(setpoint, goForward);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return drive.isFinishedDriveStraight();
    }

    /**
     * Stops the drive after it is finished
     */
    protected void end() {
    	drive.setOpenLoop(0,0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
