package org.usfirst.frc.team9256.robot.subsystems;

import org.usfirst.frc.team9256.lib.Log;
import org.usfirst.frc.team9256.lib.Loop;
import org.usfirst.frc.team9256.lib.PDP;
import org.usfirst.frc.team9256.robot.Constants;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Hanger extends Subsystem implements Log, Loop {
	
	//Singleton instance of the Hanger Subsystem
	private static Hanger instance;
	
	private VictorSP hanger;
	
	/**
	 * holds the different states of the hanger 
	 */
	public enum HangerState {
		ATTACH_TO_VELCRO,
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
		hangerState = wantedState;
	}

	@Override
	public void logToDashboard() {
		SmartDashboard.putString("HangerState", "" + hangerState);
		SmartDashboard.putNumber("Hanger PDP 15", PDP.getInstance().getCurrent(15));
		SmartDashboard.putNumber("Hanger PDP 4", PDP.getInstance().getCurrent(4));
	}

	@Override
	public void initialize() {
		setHangerState(HangerState.WINCH_STOP);
	}

	@Override
	public void update() {
		switch(hangerState) {
		case ATTACH_TO_VELCRO:
			hangerState = HangerState.ATTACH_TO_VELCRO;
			hanger.set(Constants.WINCH_ATTACH_TO_VELCRO_POWER);
			break;
		case WINCH_UP:
			hangerState = HangerState.WINCH_UP;
			hanger.set(Constants.WINCH_HANGER_POWER);
			break;
		case WINCH_STOP:
			hangerState = HangerState.WINCH_STOP;
			hanger.set(0);
			break;
		default:
			hanger.set(0);
			break;
		}
	}

	@Override
	public void end() {
		
	}
}

