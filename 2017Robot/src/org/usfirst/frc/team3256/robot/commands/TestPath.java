package org.usfirst.frc.team3256.robot.commands;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team3256.lib.Kinematics;
import org.usfirst.frc.team3256.lib.RigidTransform;
import org.usfirst.frc.team3256.lib.Translation;
import org.usfirst.frc.team3256.lib.control.AdaptivePurePursuitController;
import org.usfirst.frc.team3256.lib.control.Path;
import org.usfirst.frc.team3256.lib.control.Path.Waypoint;
import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.RobotState;
import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TestPath extends Command {

	private double maxVelocity = 120;
	private double maxAccel = 80;
	private double lookaheadDist = 24;
	private double kV = 1/maxVelocity;
	private Path path;
	private boolean rev;
	
	AdaptivePurePursuitController pathController;
	DriveTrain drive = DriveTrain.getInstance();
	Notifier notifier;
	
    public TestPath(boolean rev) {
    	requires(drive);
    	List<Waypoint> path = new ArrayList<>();
        path.add(new Waypoint(new Translation(0, 0), 48.0));
        path.add(new Waypoint(new Translation(60, 0), 48.0));
    	this.path = new Path(path);
    	this.rev = rev;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	pathController = new AdaptivePurePursuitController(lookaheadDist, maxAccel, 0.01, path, rev, 0.25);
    	notifier = new Notifier(new Runnable(){
    		
    		@Override 
    		public void run(){
    			RigidTransform.Delta command = pathController.update(RobotState.getInstance().getLatestFieldToVehicle().getValue(), Timer.getFPGATimestamp());
    			Kinematics.DriveVelocity setpoint = Kinematics.inverseKinematics(command);
    			double maxV = 0.0;
    			maxV = Math.max(maxV, Math.abs(setpoint.left));
    			maxV = Math.max(maxV, Math.abs(setpoint.left));
    			if (maxV > maxVelocity){
    				double scaling = maxVelocity/maxV;
    				setpoint = new Kinematics.DriveVelocity(setpoint.left*scaling, setpoint.right*scaling);
    			}
    			drive.setLeftMotorPower(-kV * setpoint.left);
    			drive.setRightMotorPower(-kV * setpoint.right);
    		}
    	});
    	notifier.startPeriodic(Constants.CONTROL_LOOP_DT);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return pathController.isDone();
    }

    // Called once after isFinished returns true
    protected void end() {
    	drive.setLeftMotorPower(0);
    	drive.setRightMotorPower(0);
    	notifier.stop();
    	notifier = null;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}