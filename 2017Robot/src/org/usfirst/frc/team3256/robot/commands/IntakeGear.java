package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.robot.subsystems.GearIntake;
import org.usfirst.frc.team3256.robot.subsystems.GearIntake.GearIntakeState;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class IntakeGear extends Command {

	GearIntake gearIntake = GearIntake.getInstance();
	
    public IntakeGear() {
        requires(gearIntake);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	gearIntake.setState(GearIntakeState.INTAKE);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	gearIntake.setState(GearIntakeState.STOP);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
