package org.usfirst.frc.team3256.robot.subsystems;

import org.usfirst.frc.team3256.robot.Constants;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Hanger extends Subsystem {
	private static Hanger instance;
	
	private VictorSP hanger1;
	private VictorSP hanger2;
	
	public enum HangerState {
		WINCH_UP,
		WINCH_STOP;
	}
	
	HangerState hangerState = HangerState.WINCH_STOP;
	
	private Hanger() {
		hanger1 = new VictorSP(Constants.HANGER_1);
		hanger2 = new VictorSP(Constants.HANGER_2);
	}

    public void initDefaultCommand() {

    }
	
	public static Hanger getInstance() {
		return instance == null ? instance = new Hanger() : instance;
	}
	
	public void setHangerState(HangerState wantedState) {
		switch(wantedState) {
			case WINCH_UP:
				hangerState = HangerState.WINCH_UP;
				//TODO: tune
				hanger1.set(0.5);
				hanger2.set(0.5);
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

}

