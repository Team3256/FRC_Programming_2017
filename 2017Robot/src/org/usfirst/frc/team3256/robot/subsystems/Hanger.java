package org.usfirst.frc.team3256.robot.subsystems;

import org.usfirst.frc.team3256.lib.Log;
import org.usfirst.frc.team3256.lib.PDP;
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
	
	private VictorSP hanger;
	
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
		hanger = new VictorSP(Constants.HANGER);
		hanger.setInverted(true);
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
				hanger.set(Constants.WINCH_HANGER_POWER);
				break;
			case WINCH_STOP:
				hangerState = HangerState.WINCH_STOP;
				//TODO: tune
				hanger.set(0);
				break;
			default:
				hanger.set(0);
		}
	}

	@Override
	public void logToDashboard() {
		SmartDashboard.putString("HangerState", "" + hangerState);
		SmartDashboard.putNumber("Hanger PDP 15", PDP.getInstance().getCurrent(15));
		SmartDashboard.putNumber("Hanger PDP 4", PDP.getInstance().getCurrent(4));
	}
}

