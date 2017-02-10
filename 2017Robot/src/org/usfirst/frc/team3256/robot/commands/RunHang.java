package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.robot.subsystems.Hanger;
import org.usfirst.frc.team3256.robot.subsystems.Manipulator;
import org.usfirst.frc.team3256.robot.subsystems.Hanger.HangerState;
import org.usfirst.frc.team3256.robot.subsystems.Manipulator.IntakeState;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RunHang extends Command {

	Hanger hanger = Hanger.getInstance();
	Manipulator manipulator = Manipulator.getInstance();
	PowerDistributionPanel pdp = new PowerDistributionPanel();
	
    public RunHang() {
    	requires(hanger);
    	requires(manipulator);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//when hanging is running, stop other motors that are not being used
    	manipulator.setIntakeState(IntakeState.HOLD_BALLS);
    	hanger.setHangerState(HangerState.WINCH_UP);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false || pdp.getCurrent(12)>30;
    }

    // Called once after isFinished returns true
    protected void end() {
    	//stop hang 
    	hanger.setHangerState(HangerState.WINCH_STOP);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
