package org.usfirst.frc.team3256.robot.subsystems;

import org.usfirst.frc.team3256.lib.PDP;
import org.usfirst.frc.team3256.robot.Constants;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class GearIntake extends Subsystem {

	private static GearIntake instance;
	private VictorSP roller;
	private Relay popper;
	private PDP pdp;
	private GearIntakeState gearIntakeState;
	private final int intakeCurrentThreshhold = 30;
	
	public enum GearIntakeState{
		INTAKE,
		FLIP_UP,
		DEPLOY,
		EXHAUST,
		STOP;
	}
	
	public static GearIntake getInstance(){
		return instance == null ? instance = new GearIntake() : instance;
	}

	private GearIntake(){
		pdp = PDP.getInstance();
		roller = new VictorSP(Constants.GEAR_ROLLER);
		popper = new Relay(Constants.GEAR_INTAKE_POPPER);
		gearIntakeState = GearIntakeState.STOP;
		popper.set(Value.kForward);
	}
	
	public void update(){
		switch (gearIntakeState){
			case INTAKE:
				roller.set(1);
				popper.set(Value.kReverse);
				if (hasGear()) 
					setState(GearIntakeState.FLIP_UP);
				break;
			case FLIP_UP:
				popper.set(Value.kForward);
				setState(GearIntakeState.STOP);
				break;
			case DEPLOY:
				popper.set(Value.kReverse);
				roller.set(-0.5);
				break;
			case EXHAUST:
				roller.set(-1.0);
				break;
			case STOP:
				roller.set(0);
				break;
			default:
				roller.set(0);
				popper.set(Value.kForward);
				break;
		}
	}
	
	public void setState(GearIntakeState wantedState){
		switch (wantedState){
			case INTAKE:
				gearIntakeState = GearIntakeState.INTAKE;
				break;
			case FLIP_UP:
				gearIntakeState = GearIntakeState.FLIP_UP;
				break;
			case DEPLOY:
				gearIntakeState = GearIntakeState.DEPLOY;
				break;
			case EXHAUST:
				gearIntakeState = GearIntakeState.EXHAUST;
				break;
			case STOP:
				gearIntakeState = GearIntakeState.STOP;
				break;
		}
	}
	
	private boolean hasGear(){
		return pdp.getCurrent(Constants.PDP_GEAR_ROLLER_1) > intakeCurrentThreshhold || 
				pdp.getCurrent(Constants.PDP_GEAR_ROLLER_2)> intakeCurrentThreshhold;
	}
	
    public void initDefaultCommand() {
    	
    }
}

