
package org.usfirst.frc.team3256.robot;

import org.usfirst.frc.team3256.lib.Logger;
import org.usfirst.frc.team3256.robot.commands.DriveTesting;
import org.usfirst.frc.team3256.robot.commands.TurnTesting;
import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;
import org.usfirst.frc.team3256.robot.subsystems.Hanger;
import org.usfirst.frc.team3256.robot.subsystems.Manipulator;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	DriveTrain driveTrain;
	Manipulator manipulator;
	Hanger hanger;
	Compressor compressor;
	OI operatorInterface;
	Logger logger;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	
	@Override
	public void robotInit() {
		driveTrain = DriveTrain.getInstance();
		driveTrain.calibrateGyro();
		driveTrain.resetGyro();
		driveTrain.resetEncoders();
		driveTrain.shiftUp(true);
		manipulator = Manipulator.getInstance();
		hanger = Hanger.getInstance();
		compressor = new Compressor(0);
		compressor.setClosedLoopControl(true);
		operatorInterface = new OI();
		logger = new Logger();
		logger.addLog(driveTrain);
		logger.addLog(manipulator);
		logger.addLog(hanger);
		logger.addLog(operatorInterface);
		logger.start();
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		new TurnTesting().start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		driveTrain.resetEncoders();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}
