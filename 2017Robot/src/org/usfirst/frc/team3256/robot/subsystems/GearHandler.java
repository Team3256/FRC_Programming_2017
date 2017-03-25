package org.usfirst.frc.team3256.robot.subsystems;

import org.usfirst.frc.team3256.lib.PDP;
import org.usfirst.frc.team3256.robot.Constants;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class GearHandler extends Subsystem {

	private static GearHandler instance;
	private VictorSP roller;
	private Relay popper;
	private PDP pdp;
	private GearHandlerState gearHandlerState;
	private final int intakeCurrentThreshhold = 30;
	private double startDeployTime = 0.0;
	private boolean startedDeploy = false;
	
	public enum GearHandlerState{
		INTAKE,
		FLIP_UP,
		DEPLOY,
		EXHAUST,
		STOP;
	}
	
	public static GearHandler getInstance(){
		return instance == null ? instance = new GearHandler() : instance;
	}

	private GearHandler(){
		pdp = PDP.getInstance();
		roller = new VictorSP(Constants.GEAR_ROLLER);
		popper = new Relay(Constants.GEAR_INTAKE_POPPER);
		gearHandlerState = GearHandlerState.STOP;
		popper.set(Value.kForward);
	}
	
	public void update(){
		switch (gearHandlerState){
			case INTAKE:
				if (hasGear()) 
					setState(GearHandlerState.FLIP_UP);
				roller.set(1);
				popper.set(Value.kReverse);
				break;
			case FLIP_UP:
				popper.set(Value.kForward);
				setState(GearHandlerState.STOP);
				break;
			case DEPLOY:
				startDeployTime = Timer.getFPGATimestamp();
				startedDeploy = true;
				popper.set(Value.kReverse);
				setState(GearHandlerState.EXHAUST);
				break;
			case EXHAUST:
				if (releasedGear()){
					setState(GearHandlerState.STOP);
				}
				roller.set(-1.0);
				break;
			case STOP:
				startedDeploy = false;
				roller.set(0);
				popper.set(Value.kForward);
				break;
			default:
				roller.set(0);
				popper.set(Value.kForward);
				break;
		}
	}
	
	public GearHandlerState getState(){
		return gearHandlerState;
	}
	
	public void setState(GearHandlerState wantedState){
		switch (wantedState){
			case INTAKE:
				gearHandlerState = GearHandlerState.INTAKE;
				break;
			case FLIP_UP:
				gearHandlerState = GearHandlerState.FLIP_UP;
				break;
			case DEPLOY:
				gearHandlerState = GearHandlerState.DEPLOY;
				break;
			case EXHAUST:
				gearHandlerState = GearHandlerState.EXHAUST;
				break;
			case STOP:
				gearHandlerState = GearHandlerState.STOP;
				break;
		}
	}
	
	private boolean hasGear(){
		return pdp.getCurrent(Constants.PDP_GEAR_ROLLER) > intakeCurrentThreshhold;
	}
	
	private boolean releasedGear(){
		return Timer.getFPGATimestamp()-startDeployTime > 2.0 && startedDeploy;
	}
	
    public void initDefaultCommand() {
    	
    }
}

