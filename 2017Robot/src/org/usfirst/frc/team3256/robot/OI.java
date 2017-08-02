package org.usfirst.frc.team3256.robot;

import org.usfirst.frc.team3256.lib.Log;
import org.usfirst.frc.team3256.robot.commands.AttachVelcro;
import org.usfirst.frc.team3256.robot.commands.CloseBackGear;
import org.usfirst.frc.team3256.robot.commands.DeployFrontGear;
import org.usfirst.frc.team3256.robot.commands.HoldBackGearDeploy;
import org.usfirst.frc.team3256.robot.commands.HumanPlayerBallsIntake;
import org.usfirst.frc.team3256.robot.commands.HumanPlayerGearIntake;
import org.usfirst.frc.team3256.robot.commands.StartIntakeGear;
import org.usfirst.frc.team3256.robot.commands.RunHang;
import org.usfirst.frc.team3256.robot.commands.StowLowGearHandler;
import org.usfirst.frc.team3256.robot.commands.ZeroGearHandler;
import org.usfirst.frc.team3256.robot.subsystems.GearHandler;
import org.usfirst.frc.team3256.robot.commands.StopHang;
import org.usfirst.frc.team3256.robot.triggers.JoystickTrigger;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI implements Log{
	public static XboxController driver = new XboxController(Constants.DRIVER_CONTROLLER);
	public static XboxController manipulator = new XboxController(Constants.MANIPULATOR_CONTROLLER);
	
	boolean rumbling = false;
	boolean hasGear = false;
	double startRumblingTimeStamp;
	//Controller 1
	public static Button buttonA1 = new JoystickButton(driver, 1);
	public static Button buttonB1 = new JoystickButton(driver, 2);
	public static Button buttonX1 = new JoystickButton(driver, 3);
	public static Button buttonY1 = new JoystickButton(driver, 4);
	public static Button leftBumper1 = new JoystickButton(driver, 5);
	public static Button rightBumper1 = new JoystickButton(driver, 6);
	public static Trigger rightTrigger1 = new JoystickTrigger(driver,3);
	public static Trigger leftTrigger1 = new JoystickTrigger(driver,2);
	
	//Controller 2
    public static Button buttonA2 = new JoystickButton(manipulator, 1);
    public static Button buttonB2 = new JoystickButton(manipulator, 2);
    public static Button buttonX2 = new JoystickButton(manipulator, 3);
    public static Button buttonY2 = new JoystickButton(manipulator, 4);
    public static Button leftBumper2 = new JoystickButton(manipulator, 5);
    public static Button rightBumper2 = new JoystickButton(manipulator, 6);
	public static Trigger rightTrigger2 = new JoystickTrigger(manipulator,3);
	public static Trigger leftTrigger2 = new JoystickTrigger(manipulator,2);
	
    public OI() {

    	/*Driver:   Arcade Drive -LEFT Y, RIGHT X
    				Shift Down: Hold Left Trigger
    				Reverse Front/Back: Hold Right Trigger
    				Run Hanger: Hold Left Bumper
    	*/
   
    	
    	rightBumper1.toggleWhenActive(new RunHang());
    	rightBumper1.whenInactive(new StopHang());
    	leftBumper1.toggleWhenActive(new AttachVelcro());
    	leftBumper1.whenInactive(new StopHang());
    	
    	/*Manipulator:	Button Y: Gear HP Intake Mode
    	 				Button A: Balls HP Intake Mode
    	 				Button X: Deploy Gear - Automatically retracts after
    	 				Hold Right Trigger: Spits Balls
    	 				Hold Left Trigger: Intake Balls
    	 */
    	
    	buttonY2.whenPressed(new HumanPlayerGearIntake());
    	buttonA2.whenPressed(new HumanPlayerBallsIntake());
    	buttonX2.whileHeld(new HoldBackGearDeploy());
    	buttonX2.whenReleased(new CloseBackGear());
		leftBumper2.whenPressed(new StartIntakeGear());
		leftBumper2.whenReleased(new StowLowGearHandler());
		buttonB2.whenPressed(new DeployFrontGear());
		buttonB2.whenReleased(new StowLowGearHandler());
		rightBumper2.whenActive(new ZeroGearHandler());
    }
    
    public void update() {
    	if (GearHandler.getInstance().hasGear()) {
    		// If we detect a gear but the flag hasn't been set yet
    		// (this is the first iteration of update() after picking up the gear),
    		if (!hasGear) {
    			// Start rumbling the joysticks and set the hasGear flag to true
    			hasGear = true;
    			rumbling = true;
    			startRumblingTimeStamp = Timer.getFPGATimestamp();
    		}
    	}
    	else {
    		hasGear = false;
    		rumbling = false;
    	}
    	
    	// Stop rumbling after two seconds
    	if (Timer.getFPGATimestamp() - startRumblingTimeStamp > Constants.RUMBLE_TIME) 
			rumbling = false;
		
    	// Rumble if the B button (deploy gear) is pressed
    	// This should run regardless of any of the previous conditions, which is why
    	// it is here at the very bottom
    	if (manipulator.getBButton()) {
    		rumbling = true;
    	}
    	
    	if (rumbling){
			rumbleJoysticks();
		}
		else {
			 stopRumblingJoysticks();
		}
    }
    
    private void rumbleJoysticks() {
		manipulator.setRumble(RumbleType.kLeftRumble, 1);
		manipulator.setRumble(RumbleType.kRightRumble, 1);
		driver.setRumble(RumbleType.kLeftRumble, 1);
		driver.setRumble(RumbleType.kRightRumble, 1);
    }
    
    private void stopRumblingJoysticks() {
    	manipulator.setRumble(RumbleType.kLeftRumble, 0);
		manipulator.setRumble(RumbleType.kRightRumble, 0);
		driver.setRumble(RumbleType.kLeftRumble, 0);
		driver.setRumble(RumbleType.kRightRumble, 0);
    }
    
	@Override
	public void logToDashboard() {
		SmartDashboard.putNumber("LEFT Y", driver.getY(Hand.kLeft));
		SmartDashboard.putNumber("RIGHT X: ", driver.getX(Hand.kRight));
	}
}