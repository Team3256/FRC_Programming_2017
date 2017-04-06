package org.usfirst.frc.team3256.robot;

import org.usfirst.frc.team3256.lib.GyroCalibrator;
import org.usfirst.frc.team3256.lib.Logger;
import org.usfirst.frc.team3256.lib.PDP;
import org.usfirst.frc.team3256.robot.automodes.BaselineCross;
import org.usfirst.frc.team3256.robot.automodes.DoNothingAuto;
import org.usfirst.frc.team3256.robot.automodes.GearCenterAuto;
import org.usfirst.frc.team3256.robot.automodes.GearLeftAuto;
import org.usfirst.frc.team3256.robot.automodes.GearRightAuto;
import org.usfirst.frc.team3256.robot.automodes.HopperAutoBlue;
import org.usfirst.frc.team3256.robot.automodes.HopperAutoRed;
import org.usfirst.frc.team3256.robot.commands.DriveTesting;
import org.usfirst.frc.team3256.robot.commands.TurnTesting;
import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;
import org.usfirst.frc.team3256.robot.subsystems.GearHandler;
import org.usfirst.frc.team3256.robot.subsystems.Hanger;
import org.usfirst.frc.team3256.robot.subsystems.Hanger.HangerState;
import org.usfirst.frc.team3256.robot.subsystems.Manipulator;
import org.usfirst.frc.team3256.robot.subsystems.Manipulator.HumanPlayerLoadingState;
import org.usfirst.frc.team3256.robot.subsystems.Roller;
import org.usfirst.frc.team3256.robot.subsystems.GearHandler.GearHandlerState;
import org.usfirst.frc.team3256.robot.subsystems.Roller.RollerState;
import org.usfirst.frc.team3256.robot.test.GearIntakeTest;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
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
	GearHandler gearHandler;
	Manipulator manipulator;
	Roller roller;
	Hanger hanger;
	Compressor compressor;
	OI operatorInterface;
	Logger logger;
	GyroCalibrator gyroCalibrator;
	Notifier gearHandlerLoop;
	SendableChooser<Command> autonomousChooser;
	SendableChooser<Boolean> subsystemChooser;
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
		driveTrain.calibrateGyro();
		manipulator = Manipulator.getInstance();
		hanger = Hanger.getInstance();
		gearHandler = GearHandler.getInstance();
		compressor = new Compressor(0);
		compressor.setClosedLoopControl(true);
		logger = new Logger();
		logger.addLog(driveTrain);
		logger.addLog(manipulator);
		logger.addLog(hanger);
		logger.addLog(PDP.getInstance());
		logger.start();
		gearHandlerLoop = new Notifier(new Runnable(){
			@Override
			public void run() {
				gearHandler.update();
			}
		});
		gyroCalibrator = new GyroCalibrator();
		//CameraServer.getInstance().startAutomaticCapture();
		
		autonomousChooser = new SendableChooser<>();
		autonomousChooser.addDefault("Do Nothing Auto", new DoNothingAuto());
		autonomousChooser.addObject("Cross Baseline Only", new BaselineCross());
		autonomousChooser.addObject("Center Gear", new GearCenterAuto());
		autonomousChooser.addObject("Left-Side Gear", new GearLeftAuto(false));
		autonomousChooser.addObject("Left-Side Gear w/ Blue Hopper", new GearLeftAuto(true));
		autonomousChooser.addObject("Right-Side Gear", new GearRightAuto(false));
		autonomousChooser.addObject("Right-Side Gear w/ Red Hopper", new GearRightAuto(true));
		//autonomousChooser.addObject("TURN SUCKAS", new AlignToVision());
		autonomousChooser.addObject("Hopper Blue", new HopperAutoBlue());
		autonomousChooser.addObject("Hopper Red", new HopperAutoRed());
		autonomousChooser.addObject("TEST TURN", new TurnTesting());
		autonomousChooser.addObject("TEST MOVE STRAIGHT", new DriveTesting());
		SmartDashboard.putData("Autonomous Chooser", autonomousChooser);
		subsystemChooser = new SendableChooser<>();
		subsystemChooser.addObject("GROUND GEAR INTAKE", true);
		subsystemChooser.addObject("BALL SUBSYSTEM", false);
		SmartDashboard.putData("Subsystem Chooser", subsystemChooser);
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		gearHandlerLoop.stop();
		gyroCalibrator.start();
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
		Constants.useGearIntakeSubsystem = subsystemChooser.getSelected();
		if (Constants.useGearIntakeSubsystem){
			gearHandlerLoop.startPeriodic(Constants.CONTROL_LOOP_DT);
		}
		operatorInterface = new OI();
		gyroCalibrator.stop();
		manipulator.setHumanLoadingState(HumanPlayerLoadingState.GEAR_INTAKE);
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
		Constants.useGearIntakeSubsystem = subsystemChooser.getSelected();
		if (Constants.useGearIntakeSubsystem){
			gearHandlerLoop.startPeriodic(Constants.CONTROL_LOOP_DT);
		}
		operatorInterface = new OI();
		gyroCalibrator.stop();
		driveTrain.resetEncoders();
		driveTrain.resetGyro();
		driveTrain.shiftUp(true);
		manipulator.setHumanLoadingState(HumanPlayerLoadingState.GEAR_INTAKE);
		/*
		IntakeType intakeType = intakeChooser.getSelected();
		operatorInterface.setIntakeType(intakeType);
		if (intakeType == IntakeType.GEAR) {
			gearHandler = GearHandler.getInstance();
			gearHandler.setState(GearHandlerState.STOP);
			logger.addLog(gearHandler);
		}
		else if (intakeType == IntakeType.FUEL) {
			roller = Roller.getInstance();
			roller.setRollerState(RollerState.STOPPED);
			logger.addLog(roller);
		}
		*/
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
