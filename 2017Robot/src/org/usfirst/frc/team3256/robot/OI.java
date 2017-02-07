package org.usfirst.frc.team3256.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	private static XboxController joystick1 = new XboxController(RobotMap.JOYSTICK_1);
	private static XboxController joystick2 = new XboxController(RobotMap.JOYSTICK_2);
	//Controller 1
	public static Button buttonA1 = new JoystickButton(joystick1, 1);
	public static Button buttonB1 = new JoystickButton(joystick1, 2);
	public static Button buttonX1 = new JoystickButton(joystick1, 3);
	public static Button buttonY1 = new JoystickButton(joystick1, 4);
	public static Button leftBumper1 = new JoystickButton(joystick1, 5);
	public static Button rightBumper1 = new JoystickButton(joystick1, 6);
	public static Button backButton1 = new JoystickButton(joystick1, 7);
	public static Button startButton1 = new JoystickButton(joystick1, 8);
	public static Button leftStickButton1 = new JoystickButton(joystick1, 9);
	public static Button rightStickbutton1 = new JoystickButton(joystick1, 10);
	//Controller 2
    public static Button buttonA2 = new JoystickButton(joystick2, 1);
    public static Button buttonB2 = new JoystickButton(joystick2, 2);
    public static Button buttonX2 = new JoystickButton(joystick2, 3);
    public static Button buttonY2 = new JoystickButton(joystick2, 4);
    public static Button leftBumper2 = new JoystickButton(joystick2, 5);
    public static Button rightBumper2 = new JoystickButton(joystick2, 6);
    public static Button backButton2 = new JoystickButton(joystick2, 7);
    public static Button startButton2 = new JoystickButton(joystick2, 8);
    public static Button leftStickButton2 = new JoystickButton(joystick2, 9);
    public static Button rightStickbutton2 = new JoystickButton(joystick2, 10);
    
    public OI(){
    	
    }
}
