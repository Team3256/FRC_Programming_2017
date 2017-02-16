package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.lib.BangBangController;
import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class BangBangDrive extends Command {

	DriveTrain drive = DriveTrain.getInstance();
	BangBangController bangBangController;
	private double setpoint, output;
	private boolean goForward;
	
    public BangBangDrive(double setpoint, double output, boolean goForward) {
        requires(drive);
        this.setpoint = setpoint;
        this.output = output;
        this.goForward = goForward;
        bangBangController = new BangBangController(setpoint, output);
        bangBangController.setTolerance(2);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	drive.resetEncoders();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	drive.tankDrive(output, output, !goForward);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return bangBangController.isFinished();
    }

    // Called once after isFinished returns true
    protected void end() {
    	drive.tankDrive(0, 0, false);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
