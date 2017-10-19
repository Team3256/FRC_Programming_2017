package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.lib.control.PIDController;
import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class PIDTurn extends Command {

	private DriveTrain drive = DriveTrain.getInstance();
	private Notifier notifier;
	private PIDController pid;
	private double setpoint;
	private boolean turnRight, oneWheel;
	private double startTime, endTime, dt;
	
	/**
	 * @param setpoint desired angle
	 * @param turnRight desired direction
	 */
	public PIDTurn(double setpoint, final boolean turnRight, boolean oneWheel) {
        requires(drive);
        pid = new PIDController(Constants.KP_PID_TURN, Constants.KI_PID_TURN, Constants.KD_PID_TURN);
        pid.setMinMaxOutput(0.15, 0.5);
        pid.setTolerance(0.1);
    	this.setpoint = setpoint;
    	this.turnRight = turnRight;
    	this.oneWheel = oneWheel;
    }
 
    // Called just before this Command runs the first time
    protected void initialize() {
    	drive.shiftUp(false);
    	pid.setSetpoint(setpoint);  
        notifier = new Notifier(new Runnable(){
			@Override
			public void run() {
		    	double output = 0;//pid.update(Math.abs(drive.getAngle()));
		    	if (turnRight){
		    		drive.setLeftMotorPower(output);
		    		drive.setRightMotorPower(oneWheel ? 0 : -output);
		    	}
		    	else{
		    		drive.setLeftMotorPower(oneWheel ? 0 : -output);
		    		drive.setRightMotorPower(output);
		    	}
			}
        });

    	startTime = Timer.getFPGATimestamp();
    	notifier.startPeriodic(Constants.CONTROL_LOOP_DT);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
	}	

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;//(pid.isFinished() && Math.abs(setpoint-drive.getAngle()) <= 1 && Timer.getFPGATimestamp()-startTime > 1) || Timer.getFPGATimestamp()-startTime > 2.5;
    }

    /**
     * Stops the turn once it is finished
     */
    protected void end() {
    	drive.setOpenLoop(0, 0);
    	notifier.stop();
    	notifier = null;
    	endTime = Timer.getFPGATimestamp();
    	dt = endTime-startTime;
    	SmartDashboard.putNumber("FINISHED PID TURN - ELAPSED TIME" , dt);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
