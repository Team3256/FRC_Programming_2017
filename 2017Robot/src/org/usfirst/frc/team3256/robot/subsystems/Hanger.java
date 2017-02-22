package org.usfirst.frc.team3256.robot.subsystems;

import org.usfirst.frc.team3256.lib.Log;
import org.usfirst.frc.team3256.robot.Constants;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Hanger extends Subsystem implements Log {
	
	//Singleton instance of the Hanger Subsystem
	private static Hanger instance;
	
	private VictorSP hanger1;
	private VictorSP hanger2;
	
	/**
	 * holds the different states of the hanger 
	 */
	public enum HangerState {
		WINCH_UP,
		WINCH_STOP;
	}
	
	//Current state of the Hanger
	HangerState hangerState = HangerState.WINCH_STOP;
	
	/**
	 * Cannot be instantiated out of the class and initializes the hanger motors, so we will always only have one Hanger instance
	 * Use the getInstance() method
	 */
	private Hanger() {
		//VictorSP for the two hanger motors
		hanger1 = new VictorSP(Constants.HANGER_1);
		hanger2 = new VictorSP(Constants.HANGER_2);
	}

	/**
	 * Sets the default command for this subsystem. 
	 * There is no default command for the Hanger
	 */
    public void initDefaultCommand() {

    }
    
	/**
	 * @return The singleton instance of the Hanger class
	 */
	public static Hanger getInstance() {
		return instance == null ? instance = new Hanger() : instance;
	}

	/**
	 * @param wantedState - The wanted HangerState of the Hanger Subsystem. 
	 */
	public void setHangerState(HangerState wantedState) {
		switch(wantedState) {
			case WINCH_UP:
				hangerState = HangerState.WINCH_UP;
				hanger1.set(Constants.WINCH_HANGER_POWER);
				hanger2.set(Constants.WINCH_HANGER_POWER);
				break;
			case WINCH_STOP:
				hangerState = HangerState.WINCH_STOP;
				//TODO: tune
				hanger1.set(0);
				hanger2.set(0);
				break;
			default:
				hanger1.set(0);
				hanger2.set(0);
		}
	}

	@Override
	public void logToDashboard() {
		SmartDashboard.putString("HangerState", "" + hangerState);
		SmartDashboard.putNumber("HangerMotor1: PWM-" + hanger1.getChannel() + " ", 
				hanger1.get());
		SmartDashboard.putNumber("HangerMotor2: PWM-" + hanger2.getChannel() + " ", 
				hanger2.get());
	}
}

