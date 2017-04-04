package org.usfirst.frc.team3256.robot.subsystems;

import org.usfirst.frc.team3256.lib.ADXRS453_Gyro;
import org.usfirst.frc.team3256.lib.PDP;
import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.lib.PIDController;

import edu.wpi.first.wpilibj.Encoder;
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
	private VictorSP flipper;
	private Encoder flipperEncoder;
	private PDP pdp;
	private GearHandlerState gearHandlerState;
	private final int intakeCurrentThreshhold = 30;
	private double startDeployTime = 0.0;
	private boolean currentlyDeploying = false;
	
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
		flipper = new VictorSP(Constants.GEAR_INTAKE_FLIPPER);
		flipperEncoder = new Encoder(Constants.ENCODER_GEAR_INTAKE_A, Constants.ENCODER_GEAR_INTAKE_B);
		flipperEncoder.setDistancePerPulse(Constants.INCHES_PER_TICK);
		flipperEncoder.setReverseDirection(false);
		flipperEncoder.reset();
		gearHandlerState = GearHandlerState.STOP;
	}
	
	public void update(){
		switch (gearHandlerState){
			case INTAKE:
				if (getHandlerAngle() > 0) {
					flipper.set(1.0);
				}
				else {
					flipper.set(0.0);
				}
				
				roller.set(1.0);
				
				if (hasGear())
					setState(GearHandlerState.FLIP_UP);
				break;
			case FLIP_UP:
				if (getHandlerAngle() >= 90) { // TODO: 90 is a placeholder value right now
					setState(GearHandlerState.STOP);
				}
				else {
					flipper.set(-1.0);
				}
				break;
			case STOP:
				roller.set(0);
				flipper.set(0);
				currentlyDeploying = false;
				break;
			case DEPLOY:
				startDeployTime = Timer.getFPGATimestamp();
				currentlyDeploying = true;
				setState(GearHandlerState.EXHAUST);
				break;
			case EXHAUST:
				roller.set(-1.0);
				
				if (releasedGear()){
					setState(GearHandlerState.STOP);
					currentlyDeploying = false;
				}
				break;
		}
	}
	
	private double getHandlerAngle() {
		return flipperEncoder.getDistance() * Constants.GEAR_HANDLER_TICKS_TO_ANGLE;
	}

	public GearHandlerState getState(){
		return gearHandlerState;
	}
	
	public boolean isDown() {
		return getHandlerAngle() < 5 && roller.getSpeed() == 1.0;
	}
	
	public boolean isUp() {
		return getHandlerAngle() > 85 && roller.getSpeed() == 0.0;
	}
	
	public boolean gearDeployFinished() {
		return !currentlyDeploying;
	}
	
	private boolean releasedGear(){
		return Timer.getFPGATimestamp()-startDeployTime > 2.0 && currentlyDeploying;
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
	
	public void initDefaultCommand() {
    	
    }
}

