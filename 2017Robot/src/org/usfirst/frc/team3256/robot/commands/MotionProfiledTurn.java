package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.lib.TurnInPlaceController;
import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class MotionProfiledTurn extends Command {

	DriveTrain drive = DriveTrain.getInstance();
	TurnInPlaceController turnController;
	Notifier notifier;
	private double setpoint;
	private boolean turnRight;
	
    public MotionProfiledTurn(double setpoint, boolean turnRight) {
        requires(drive);
        this.setpoint = setpoint;
        this.turnRight = turnRight;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        turnController = new TurnInPlaceController();
    	drive.resetGyro();
    	drive.resetEncoders();
    	drive.shiftUp(true);	
    	notifier = new Notifier(new Runnable(){
			@Override
			public void run() {
				double output = turnController.update();
				SmartDashboard.putNumber("OUTPUT MP TURN", output);
				if (turnRight) drive.tankDrive(-output, output, false);
		    	else drive.tankDrive(output, -output, false);
			}
    	});
    	turnController.reset();
    	turnController.setSetpoint(setpoint);
    	notifier.startPeriodic(Constants.CONTROL_LOOP_DT);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return turnController.isFinished();
    }

    // Called once after isFinished returns true
    protected void end() {
    	drive.tankDrive(0, 0, true);
    	notifier.stop();
    	notifier = null;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
