package org.usfirst.frc.team3256.robot;

import org.usfirst.frc.team3256.lib.Log;
import org.usfirst.frc.team3256.robot.commands.AttachVelcro;

import org.usfirst.frc.team3256.robot.commands.CloseBackGear;
import org.usfirst.frc.team3256.robot.commands.DeployBackGear;
import org.usfirst.frc.team3256.robot.commands.DeployFrontGear;
import org.usfirst.frc.team3256.robot.commands.GroundIntakeBalls;
import org.usfirst.frc.team3256.robot.commands.HoldBackGearDeploy;
import org.usfirst.frc.team3256.robot.commands.HumanPlayerBallsIntake;
import org.usfirst.frc.team3256.robot.commands.HumanPlayerGearIntake;
import org.usfirst.frc.team3256.robot.commands.IntakeGear;
import org.usfirst.frc.team3256.robot.commands.RunHang;
import org.usfirst.frc.team3256.robot.commands.ShootBalls;
import org.usfirst.frc.team3256.robot.commands.StopGearIntake;
import org.usfirst.frc.team3256.robot.commands.StopHang;
import org.usfirst.frc.team3256.robot.commands.StopRollers;
import org.usfirst.frc.team3256.robot.triggers.DualButton;
import org.usfirst.frc.team3256.robot.triggers.JoystickTrigger;

import edu.wpi.first.wpilibj.GenericHID.Hand;
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
    public static Button buttonXB2 = new DualButton(manipulator, 2, 3);
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
    	//buttonX2.whenPressed(new DeployGear());
    	buttonX2.whileHeld(new HoldBackGearDeploy());
    	buttonX2.whenReleased(new CloseBackGear());
    	if (Constants.useGearIntakeSubsystem){
    		leftTrigger2.toggleWhenActive(new IntakeGear());
    		leftTrigger2.whenInactive(new StopGearIntake());
    		buttonB2.whenPressed(new DeployFrontGear());

    	}
    	else{
    		rightTrigger2.toggleWhenActive(new ShootBalls());
        	rightTrigger2.whenInactive(new StopRollers());
        	leftTrigger2.toggleWhenActive(new GroundIntakeBalls());
        	leftTrigger2.whenInactive(new StopRollers());
    	}
    }

	@Override
	public void logToDashboard() {
		SmartDashboard.putNumber("LEFT Y", driver.getY(Hand.kLeft));
		SmartDashboard.putNumber("RIGHT X: ", driver.getX(Hand.kRight));
	}
}
