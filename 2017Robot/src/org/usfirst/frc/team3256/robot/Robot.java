package org.usfirst.frc.team3256.robot;

import org.usfirst.frc.team3256.lib.Logger;
import org.usfirst.frc.team3256.lib.PDP;
import org.usfirst.frc.team3256.robot.automodes.BaselineCross;
import org.usfirst.frc.team3256.robot.automodes.DoNothingAuto;
import org.usfirst.frc.team3256.robot.automodes.GearCenterAuto;
import org.usfirst.frc.team3256.robot.automodes.GearLeftAuto;
import org.usfirst.frc.team3256.robot.automodes.GearRightAuto;
import org.usfirst.frc.team3256.robot.commands.AlignToVision;
import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;
import org.usfirst.frc.team3256.robot.subsystems.Hanger;
import org.usfirst.frc.team3256.robot.subsystems.Hanger.HangerState;
import org.usfirst.frc.team3256.robot.subsystems.Manipulator;
import org.usfirst.frc.team3256.robot.subsystems.Manipulator.HumanPlayerLoadingState;
import org.usfirst.frc.team3256.robot.subsystems.Roller;
import org.usfirst.frc.team3256.robot.subsystems.Roller.RollerState;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
	Roller roller;
	Hanger hanger;
	Compressor compressor;
	OI operatorInterface;
	Logger logger;
	SendableChooser<Command> autonomousChooser;
	Command autonomousCommand;
	double autoStartTime = 0;
	double autoEndTime = 0;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	
	@Override
	public void robotInit() {
		driveTrain = DriveTrain.getInstance();
		driveTrain.resetEncoders();
		driveTrain.shiftUp(true);
		manipulator = Manipulator.getInstance();
		roller = Roller.getInstance();
		hanger = Hanger.getInstance();
		compressor = new Compressor(0);
		compressor.setClosedLoopControl(true);
		operatorInterface = new OI();
		logger = new Logger();
		logger.addLog(driveTrain);
		logger.addLog(manipulator);
		logger.addLog(hanger);
		logger.addLog(roller);
		logger.addLog(PDP.getInstance());
		logger.start();
		CameraServer.getInstance().startAutomaticCapture();
		
		autonomousChooser = new SendableChooser<>();
		autonomousChooser.addDefault("Do Nothing Auto", new DoNothingAuto());
		autonomousChooser.addObject("Cross Baseline Only", new BaselineCross());
		autonomousChooser.addObject("Center Gear", new GearCenterAuto());
		autonomousChooser.addObject("Left Gear", new GearLeftAuto());
		autonomousChooser.addObject("Right Gear", new GearRightAuto());
		autonomousChooser.addObject("TURN SUCKAS", new AlignToVision());
		SmartDashboard.putData("Autonomous Chooser", autonomousChooser);
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
		manipulator.setHumanLoadingState(HumanPlayerLoadingState.BALLS_INTAKE);
		autoStartTime = Timer.getFPGATimestamp();
		autonomousCommand = autonomousChooser.getSelected();
		autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		if (!autonomousCommand.isRunning()){
			autoEndTime = Timer.getFPGATimestamp();
			SmartDashboard.putNumber("AUTONOMOUS ELAPSED TIME", autoEndTime-autoStartTime);
		}
	}

	@Override
	public void teleopInit() {
		driveTrain.resetEncoders();
		driveTrain.resetGyro();
		driveTrain.shiftUp(true);
		manipulator.setHumanLoadingState(HumanPlayerLoadingState.BALLS_INTAKE);
		roller.setRollerState(RollerState.STOPPED);
		hanger.setHangerState(HangerState.WINCH_STOP);
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
