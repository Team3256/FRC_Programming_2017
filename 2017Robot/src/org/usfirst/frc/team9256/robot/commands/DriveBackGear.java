package org.usfirst.frc.team9256.robot.commands;

import org.usfirst.frc.team9256.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;

public class DriveBackGear extends Command {
	DriveTrain drive = DriveTrain.getInstance();
	Notifier notifier;
	private double setpoint;
	
	public DriveBackGear(double setpoint) {
		requires(drive);
		this.setpoint = setpoint;
	}

	protected void initialize() {
		drive.resetEncoders();        
    	drive.shiftUp(true);
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}
}
