package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class ShiftCommand extends InstantCommand {

	DriveTrain drive = DriveTrain.getInstance();
	private boolean shiftUp;
	
    public ShiftCommand(boolean shiftUp) {
        super();
        requires(drive);
        this.shiftUp = shiftUp;
    }

    // Called once when the command executes
    protected void initialize() {
    	drive.shiftUp(shiftUp);
    }

}
