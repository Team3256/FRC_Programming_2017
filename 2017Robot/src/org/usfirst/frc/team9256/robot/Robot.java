package org.usfirst.frc.team9256.robot;

import org.usfirst.frc.team9256.lib.LEDStrip;
import org.usfirst.frc.team9256.lib.Logger;
import org.usfirst.frc.team9256.lib.Looper;
import org.usfirst.frc.team9256.robot.automodes.BaselineCross;
import org.usfirst.frc.team9256.robot.automodes.DoNothingAuto;
import org.usfirst.frc.team9256.robot.automodes.FrontGearCenterAuto;
import org.usfirst.frc.team9256.robot.automodes.FrontGearLeftAuto;
import org.usfirst.frc.team9256.robot.automodes.FrontGearRightAuto;
import org.usfirst.frc.team9256.robot.commands.AutoDeployFrontGear;
import org.usfirst.frc.team9256.robot.commands.DriveTesting;
import org.usfirst.frc.team9256.robot.commands.TurnTesting;
import org.usfirst.frc.team9256.robot.subsystems.DriveTrain;
import org.usfirst.frc.team9256.robot.subsystems.GearHandler;
import org.usfirst.frc.team9256.robot.subsystems.Hanger;

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
	LEDStrip led;
	DriveTrain driveTrain;
	GearHandler gearHandler;
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
		driveTrain = DriveTrain.getInstance();
		driveTrain.resetEncoders();
		driveTrain.shiftUp(true);
		hanger = Hanger.getInstance();
		gearHandler = GearHandler.getInstance();
		gearHandler.setEncoderPosition(Constants.GEAR_PIVOT_CALIBRATE_POS);
		compressor = new Compressor(0);
		compressor.setClosedLoopControl(true);
		logger = new Logger();
		logger.addLog(driveTrain);
		logger.addLog(hanger);
		logger.addLog(gearHandler);
		// logger.addLog(PDP.getInstance());
		logger.start();
		disabledLooper = new Looper();
		//disabledLooper.addLoop(new GyroCalibrator());
		enabledLooper = new Looper();
		enabledLooper.addLoop(driveTrain);
		enabledLooper.addLoop(gearHandler);
		enabledLooper.addLoop(hanger);
		camera0 = CameraServer.getInstance().startAutomaticCapture();
		camera0.setResolution(160, 120);
		camera0.setFPS(30);
		camera0.setExposureAuto();
		/*
		 * camera1 = CameraServer.getInstance().startAutomaticCapture();
		 * camera1.setResolution(160, 120); camera1.setExposureManual(75);
		 * camera1.setFPS(10);
		 */
		operatorInterface = new OI();
		autonomousChooser = new SendableChooser<>();
		autonomousChooser.addDefault("Do Nothing Auto", new DoNothingAuto());
		autonomousChooser.addObject("Cross Baseline Forward", new BaselineCross());
		autonomousChooser.addObject("Left-Side Gear Forward", new FrontGearLeftAuto());
		autonomousChooser.addObject("Right-Side Gear Forward", new FrontGearRightAuto());
		//autonomousChooser.addObject("Drive Testing", new DriveTesting());
		//autonomousChooser.addObject("Turn Testing", new TurnTesting());
		//autonomousChooser.addObject("Gear Auto Deploy Testing", new AutoDeployFrontGear());
		autonomousChooser.addObject("Center Front Gear Auto", new FrontGearCenterAuto());
		SmartDashboard.putData("Autonomous Chooser", autonomousChooser);
	}

	@Override
	public void disabledInit() {
		enabledLooper.stop();
		disabledLooper.start();
	}

	@Override
	public void disabledPeriodic() {
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
		Scheduler.getInstance().run();
		if (!autonomousCommand.isRunning()) {
			autoEndTime = Timer.getFPGATimestamp();
			SmartDashboard.putNumber("AUTONOMOUS ELAPSED TIME", autoEndTime - autoStartTime);
		}
	}

	@Override
	public void teleopInit() {
		disabledLooper.stop();
		enabledLooper.start();
	}

	@Override
	public void teleopPeriodic() {
		enabledLooper.log();
		led.update(false);
		Scheduler.getInstance().run();
		operatorInterface.update();
	}

	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}

}
