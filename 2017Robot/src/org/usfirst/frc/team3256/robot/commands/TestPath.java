package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.lib.control.PathController;
import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TestPath extends Command {

	PathController controller = new PathController();
	DriveTrain drive = DriveTrain.getInstance();
	Notifier notifier;
	
    public TestPath() {
    	requires(drive);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	controller.init("/home/lvuser/CenterGearForwardPath.bin");
    	notifier = new Notifier(new Runnable(){
    		
    		@Override 
    		public void run(){
    			controller.update();
    		}
    	});
    	notifier.startPeriodic(Constants.CONTROL_LOOP_DT);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return controller.isFinished();
    }

    // Called once after isFinished returns true
    protected void end() {
    	drive.setLeftMotorPower(0);
    	drive.setRightMotorPower(0);
    	controller.reset();
    	notifier.stop();
    	notifier = null;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}