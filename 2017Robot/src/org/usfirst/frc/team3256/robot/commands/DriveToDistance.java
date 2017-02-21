package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.lib.DrivePWM;
import org.usfirst.frc.team3256.lib.DriveStraightController;
import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveToDistance extends Command {

	DriveTrain drive = DriveTrain.getInstance();
	DriveStraightController controller;
	Notifier notifier;
	private double setpoint;
	private boolean goForward;
	
    public DriveToDistance(double setpoint, boolean goForward) {
        requires(drive);
        this.setpoint = setpoint;
        this.goForward = goForward;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	drive.resetEncoders();
    	drive.resetGyro();        
    	drive.shiftUp(true);
    	controller = new DriveStraightController();
        notifier = new Notifier(new Runnable(){
			@Override
			public void run() {
				DrivePWM signal = controller.update();
				if (!goForward){
					drive.setLeftMotorPower(signal.getLeftPWM());
					drive.setRightMotorPower(signal.getRightPWM());
				}
				else{
					drive.setLeftMotorPower(-signal.getLeftPWM());
					drive.setRightMotorPower(-signal.getRightPWM());
				}
			}
        });
    	controller.setSetpoint(setpoint, !goForward);
    	notifier.startPeriodic(Constants.CONTROL_LOOP_DT);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return controller.isFinished() || Math.abs(setpoint-drive.getAveragePosition()) <= 1;
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
    }
}
