package org.usfirst.frc.team3256.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;


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
	public static Button backButton1 = new JoystickButton(driver, 7);
	public static Button startButton1 = new JoystickButton(driver, 8);
	public static Button leftStickButton1 = new JoystickButton(driver, 9);
	public static Button rightStickbutton1 = new JoystickButton(driver, 10);
	public static boolean getRightTriggerOne(){
		return driver.getTriggerAxis(Hand.kRight)>0.5;
	}
	//Controller 2
    public static Button buttonA2 = new JoystickButton(manipulator, 1);
    public static Button buttonB2 = new JoystickButton(manipulator, 2);
    public static Button buttonX2 = new JoystickButton(manipulator, 3);
    public static Button buttonY2 = new JoystickButton(manipulator, 4);
    public static Button leftBumper2 = new JoystickButton(manipulator, 5);
    public static Button rightBumper2 = new JoystickButton(manipulator, 6);
    public static Button backButton2 = new JoystickButton(manipulator, 7);
    public static Button startButton2 = new JoystickButton(manipulator, 8);
    public static Button leftStickButton2 = new JoystickButton(manipulator, 9);
    public static Button rightStickbutton2 = new JoystickButton(manipulator, 10);
    
    public OI(){
    	
    }
}
