package org.usfirst.frc.team3256.robot;

import org.usfirst.frc.team3256.robot.commands.GroundIntakeBalls;
import org.usfirst.frc.team3256.robot.commands.HumanPlayerBallsIntake;
import org.usfirst.frc.team3256.robot.commands.HumanPlayerGearIntake;
import org.usfirst.frc.team3256.robot.commands.RunHang;
import org.usfirst.frc.team3256.robot.commands.ShootBalls;
import org.usfirst.frc.team3256.robot.commands.StopHang;
import org.usfirst.frc.team3256.robot.commands.StopRollers;
import org.usfirst.frc.team3256.robot.triggers.JoystickTrigger;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
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
	public static Trigger leftTrigger1 = new JoystickTrigger(driver,6);
	
	//Controller 2
    public static Button buttonA2 = new JoystickButton(manipulator, 1);
    public static Button buttonB2 = new JoystickButton(manipulator, 2);
    public static Button buttonX2 = new JoystickButton(manipulator, 3);
    public static Button buttonY2 = new JoystickButton(manipulator, 4);
    public static Button leftBumper2 = new JoystickButton(manipulator, 5);
    public static Button rightBumper2 = new JoystickButton(manipulator, 6);
	public static Trigger rightTrigger2 = new JoystickTrigger(manipulator,3);
	public static Trigger leftTrigger2 = new JoystickTrigger(manipulator,6);
	
    public OI() {
    	//TESTED ON HARDWARE
    	buttonA2.whileHeld(new GroundIntakeBalls());
    	buttonA2.whenInactive(new StopRollers());
    	buttonY2.whileHeld(new ShootBalls());
    	buttonY2.whenInactive(new StopRollers());
    	rightTrigger2.toggleWhenActive(new RunHang());
    	rightTrigger2.whenInactive(new StopHang());
    	leftBumper2.whileHeld(new HumanPlayerBallsIntake());
    	leftBumper2.whenReleased(new HumanPlayerGearIntake());
    }
}
