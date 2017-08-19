package org.usfirst.frc.team3256.robot;

import org.usfirst.frc.team3256.lib.GyroCalibrator;
import org.usfirst.frc.team3256.lib.LEDStrip;
import org.usfirst.frc.team3256.lib.Logger;
import org.usfirst.frc.team3256.lib.Looper;
import org.usfirst.frc.team3256.robot.automodes.BaselineCross;
import org.usfirst.frc.team3256.robot.automodes.DoNothingAuto;
import org.usfirst.frc.team3256.robot.automodes.GearCenterAuto;
import org.usfirst.frc.team3256.robot.automodes.GearLeftAuto;
import org.usfirst.frc.team3256.robot.automodes.GearRightAuto;
import org.usfirst.frc.team3256.robot.automodes.HopperAutoBlue;
import org.usfirst.frc.team3256.robot.automodes.HopperAutoRed;
import org.usfirst.frc.team3256.robot.commands.DriveTesting;
import org.usfirst.frc.team3256.robot.commands.TestPath;
import org.usfirst.frc.team3256.robot.commands.TurnTesting;
import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;
import org.usfirst.frc.team3256.robot.subsystems.GearHandler;
import org.usfirst.frc.team3256.robot.subsystems.Hanger;
import org.usfirst.frc.team3256.robot.subsystems.Manipulator;

import edu.wpi.cscore.UsbCamera;
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
	
	Looper disabledLooper;
	Looper enabledLooper;
	RobotStateUpdator robotStateUpdator;
	LEDStrip led;
	DriveTrain driveTrain;
	GearHandler gearHandler;
	Manipulator manipulator;
	Hanger hanger;
	Compressor compressor;
	OI operatorInterface;
	Logger logger;
	SendableChooser<Command> autonomousChooser;
	UsbCamera camera0, camera1;
	Command autonomousCommand;
	double autoStartTime = 0;
	double autoEndTime = 0;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	
	@Override
	public void robotInit() {
		led = LEDStrip.getInstance();
		robotStateUpdator = RobotStateUpdator.getInstance();
		driveTrain = DriveTrain.getInstance();
		driveTrain.resetEncoders();
		driveTrain.shiftUp(true);
		driveTrain.calibrateGyro();
		manipulator = Manipulator.getInstance();
		hanger = Hanger.getInstance();
		gearHandler = GearHandler.getInstance();
		gearHandler.setEncoderPosition(Constants.GEAR_PIVOT_CALIBRATE_POS);
		compressor = new Compressor(0);
		compressor.setClosedLoopControl(true);
		logger = new Logger();
		logger.addLog(driveTrain);
		logger.addLog(manipulator);
		logger.addLog(hanger);
		logger.addLog(gearHandler);
		//logger.addLog(PDP.getInstance());
		logger.start();
		disabledLooper = new Looper();
		//disabledLooper.addLoop(new GyroCalibrator());
		disabledLooper.addLoop(robotStateUpdator);
		enabledLooper = new Looper();
		enabledLooper.addLoop(driveTrain);
		enabledLooper.addLoop(gearHandler);
		enabledLooper.addLoop(manipulator);
		enabledLooper.addLoop(hanger);
		enabledLooper.addLoop(robotStateUpdator);
		/*
		camera0 = CameraServer.getInstance().startAutomaticCapture();
		camera0.setResolution(240, 180);
		camera0.setExposureManual(75);
		camera1 = CameraServer.getInstance().startAutomaticCapture();
		camera1.setResolution(240, 180);
		camera1.setExposureManual(75);
		*/
		driveTrain.calibrateGyro();
		operatorInterface = new OI();
		autonomousChooser = new SendableChooser<>();
		autonomousChooser.addDefault("Do Nothing Auto", new DoNothingAuto());
		autonomousChooser.addObject("Cross Baseline Only", new BaselineCross());
		autonomousChooser.addObject("Center Gear", new GearCenterAuto());
		autonomousChooser.addObject("Left-Side Gear", new GearLeftAuto());
		autonomousChooser.addObject("Left-Side Gear w/ Blue Hopper", new GearLeftAuto());
		autonomousChooser.addObject("Right-Side Gear", new GearRightAuto(false));
		autonomousChooser.addObject("Right-Side Gear w/ Red Hopper", new GearRightAuto(true));
		autonomousChooser.addObject("Hopper Blue", new HopperAutoBlue());
		autonomousChooser.addObject("Hopper Red", new HopperAutoRed());
		autonomousChooser.addObject("Test Path", new TestPath(false));
		autonomousChooser.addObject("Drive Testing", new DriveTesting());
		autonomousChooser.addObject("Turn Testing", new TurnTesting());
		SmartDashboard.putData("Autonomous Chooser", autonomousChooser);
	}

	@Override
	public void disabledInit() {
		enabledLooper.stop();
		disabledLooper.start();
	}

	@Override
	public void disabledPeriodic() {
		RobotState.getInstance().outputToDashboard();
		Scheduler.getInstance().run();
		disabledLooper.log();
	}
	
	@Override
	public void autonomousInit() {
		disabledLooper.stop();
		autoStartTime = Timer.getFPGATimestamp();
		autonomousCommand = autonomousChooser.getSelected();
		SmartDashboard.putString("Choosen Auto", autonomousCommand + "");
		enabledLooper.start();
		autonomousCommand.start();
		System.out.println("AUTO STARTED");
	}

	@Override
	public void autonomousPeriodic() {
		enabledLooper.log();
		RobotState.getInstance().outputToDashboard();
		Scheduler.getInstance().run();
		if (!autonomousCommand.isRunning()){
			autoEndTime = Timer.getFPGATimestamp();
			SmartDashboard.putNumber("AUTONOMOUS ELAPSED TIME", autoEndTime-autoStartTime);
		}
	}

	@Override
	public void teleopInit() {
		disabledLooper.stop();
		enabledLooper.start();
	}

	@Override
	public void teleopPeriodic() {
		RobotState.getInstance().outputToDashboard();
		enabledLooper.log();
		Scheduler.getInstance().run();
		operatorInterface.update();
	}

	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
	
}
