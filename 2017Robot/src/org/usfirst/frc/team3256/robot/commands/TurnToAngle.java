package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.lib.PIDController;
import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TurnToAngle extends Command {

	private DriveTrain drive = DriveTrain.getInstance();
	private Notifier notifier;
	private PIDController pid;
	private double setpoint;
	private boolean turnRight;
	
    public TurnToAngle(double setpoint, final boolean turnRight) {
        requires(drive);
        pid = new PIDController(Constants.KP_TURN, Constants.KI_TURN, Constants.KD_TURN);
        pid.setMinMaxOutput(0.2, 0.7);
        pid.setTolerance(0.25);
    	this.setpoint = setpoint;
    	this.turnRight = turnRight;
    }
 
    // Called just before this Command runs the first time
    protected void initialize() {
    	drive.resetGyro();
    	drive.shiftUp(true);
    	pid.setSetpoint(setpoint);  
        notifier = new Notifier(new Runnable(){
			@Override
			public void run() {
	    	/*
	    	 * Perform PID to calculate PWM value
	    	 * Apply PWM value to motors
	    	 */
	    	double output = pid.update(Math.abs(drive.getAngle()));
	    	//hack to make turn left and right work
	    	if (turnRight) drive.tankDrive(-output, output, false);
	    	else drive.tankDrive(output, -output, false);
	    	pid.logToDashboard();
			}
        });
    	notifier.startPeriodic(Constants.CONTROL_LOOP_DT);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
	}	

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return pid.isFinished();
    }

    // Called once after isFinished returns true
    protected void end() {
    	drive.tankDrive(0, 0, false);
    	notifier.stop();
    	notifier = null;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
