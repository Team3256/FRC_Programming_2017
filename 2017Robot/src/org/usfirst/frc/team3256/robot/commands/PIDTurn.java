package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.lib.PIDController;
import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class PIDTurn extends Command {

	private DriveTrain drive = DriveTrain.getInstance();
	private Notifier notifier;
	private PIDController pid;
	private double setpoint;
	private boolean turnRight;
	
	/**
	 * @param setpoint desired angle
	 * @param turnRight desired direction
	 */
	public PIDTurn(double setpoint, final boolean turnRight) {
        requires(drive);
        pid = new PIDController(Constants.KP_PID_TURN, Constants.KI_PID_TURN, Constants.KD_PID_TURN);
        pid.setMinMaxOutput(0.2, 0.75);
        pid.setTolerance(0.1);
    	this.setpoint = setpoint;
    	this.turnRight = turnRight;
    }
 
    // Called just before this Command runs the first time
    protected void initialize() {
    	drive.resetGyro();
    	drive.shiftUp(false);
    	pid.setSetpoint(setpoint);  
        notifier = new Notifier(new Runnable(){
			@Override
			public void run() {
		    	double output = pid.update(Math.abs(drive.getAngle()));
		    	if (turnRight) drive.tankDrive(output, -output, false);
		    	else drive.tankDrive(-output, output, false);
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

    /**
     * Stops the turn once it is finished
     */
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
