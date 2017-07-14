package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.lib.control.TeleopDriveController;
import org.usfirst.frc.team3256.robot.OI;
import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;

public class TeleopDrive extends Command {

	DriveTrain driveTrain = DriveTrain.getInstance();
	
	private TeleopDriveMode wantedMode;
	
	public enum TeleopDriveMode{
		TANK,
		ARCADE,
		CHEESY;
	}
	
    public TeleopDrive(TeleopDriveMode wantedMode) {
        requires(driveTrain);
		this.wantedMode = wantedMode;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if (DriverStation.getInstance().isAutonomous()) return;
    	if (wantedMode == TeleopDriveMode.TANK){
    		TeleopDriveController.tankDrive(OI.driver.getY(Hand.kLeft), 
    				OI.driver.getY(Hand.kRight), OI.rightTrigger1.get());
    	}
    	else if (wantedMode == TeleopDriveMode.ARCADE){
    		TeleopDriveController.arcadeDrive(OI.driver.getY(Hand.kLeft), 
    				OI.driver.getX(Hand.kRight), OI.rightTrigger1.get());
    	} else {
    		TeleopDriveController.cheesyDrive(OI.driver.getY(Hand.kLeft), OI.driver.getX(Hand.kRight), OI.rightTrigger1.get());
    	}
    	driveTrain.shiftUp(!OI.leftTrigger1.get());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}