package org.usfirst.frc.team3256.robot.subsystems;

import org.usfirst.frc.team3256.lib.PDP;
import org.usfirst.frc.team3256.lib.PIDController;
import org.usfirst.frc.team3256.robot.Constants;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class GearHandler extends Subsystem {

	private static GearHandler instance;
	private VictorSP roller;
	private VictorSP pivot;
	private Encoder pivotEncoder;
	private PDP pdp;
	private PIDController pivotController;
	private GearHandlerState gearHandlerState;
	private final int intakeCurrentThreshhold = 30;
	private double startDeployTime = 0.0;
	private boolean currentlyDeploying = false;
	//in case something fatal happens so disable this subsystem
	private boolean eStopped = false;
	
	public enum GearHandlerState{
		INTAKE,
		STOW,
		PIVOT_FOR_DEPLOY,
		EXHAUST,
		ESTOP;
	}
	
	public static GearHandler getInstance(){
		return instance == null ? instance = new GearHandler() : instance;
	}

	private GearHandler(){
		pdp = PDP.getInstance();
		roller = new VictorSP(Constants.GEAR_ROLLER);
		pivot = new VictorSP(Constants.GEAR_INTAKE_PIVOT);
		pivotEncoder = new Encoder(Constants.ENCODER_GEAR_PIVOT_A, Constants.ENCODER_GEAR_PIVOT_B);
		pivotEncoder.reset();
		pivotController = new PIDController(Constants.KP_PIVOT, Constants.KI_PIVOT, Constants.KD_PIVOT);
		pivotController.setTolerance(1);
		pivotController.setMinMaxOutput(0.2, 0.75);
		gearHandlerState = GearHandlerState.STOW;
	}
	
	public void update(){
		if (eStopped) return;
		pivot.set(pivotController.update(getHandlerAngle()));
		switch (gearHandlerState){
			case INTAKE:
				if (hasGear())
					setState(GearHandlerState.STOW);
				pivotController.setSetpoint(90.0); //horizontal
				roller.set(1.0);
				break;
			case STOW:
				pivotController.setSetpoint(0.0); //vertical
				roller.set(0);
				currentlyDeploying = false;
				break;
			case PIVOT_FOR_DEPLOY:
				startDeployTime = Timer.getFPGATimestamp();
				currentlyDeploying = true;
				pivotController.setSetpoint(20.0); //small angle to tilt on peg
				setState(GearHandlerState.EXHAUST);
				break;
			case EXHAUST:
				roller.set(-1.0);
				if (releasedGear()){
					setState(GearHandlerState.STOW);
					currentlyDeploying = false;
				}
				break;
			case ESTOP:
				eStopped = true;
				break;
		}
	}

	private double getHandlerAngle() {
		return pivotEncoder.get() * Constants.GEAR_HANDLER_TICKS_TO_ANGLE;
	}

	public GearHandlerState getState(){
		return gearHandlerState;
	}

	private boolean releasedGear(){
		return Timer.getFPGATimestamp()-startDeployTime > 2.0 && currentlyDeploying;
	}
	
	public void setState(GearHandlerState wantedState){
		switch (wantedState){
			case INTAKE:
				gearHandlerState = GearHandlerState.INTAKE;
				break;
			case STOW:
				gearHandlerState = GearHandlerState.STOW;
				break;
			case PIVOT_FOR_DEPLOY:
				gearHandlerState = GearHandlerState.PIVOT_FOR_DEPLOY;
				break;
			case EXHAUST:
				gearHandlerState = GearHandlerState.EXHAUST;
				break;
			case ESTOP:
				gearHandlerState = GearHandlerState.ESTOP;
		}
	}
	
	private boolean hasGear(){
		return pdp.getCurrent(Constants.PDP_GEAR_ROLLER) > intakeCurrentThreshhold;
	}
	
	public void initDefaultCommand() {
    	
    }
}

