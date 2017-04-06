package org.usfirst.frc.team3256.robot.subsystems;

import org.usfirst.frc.team3256.lib.ADXRS453_Gyro;
import org.usfirst.frc.team3256.lib.Log;
import org.usfirst.frc.team3256.lib.PDP;
import org.usfirst.frc.team3256.lib.PIDController;
import org.usfirst.frc.team3256.robot.Constants;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// NOTE: This code depends on the VersaPlanetary Integrated Encoder

/**
 *
 */
public class GearHandler extends Subsystem implements Log {

	private static GearHandler instance;
	private VictorSP roller;
	private CANTalon pivot;
	private PDP pdp;
	//private PIDController pivotController;
	private GearHandlerState gearHandlerState;
	private final int intakeCurrentThreshhold = 30;
	private double startDeployTime = 0.0;
	private boolean currentlyDeploying = false;
	private boolean currentlyHasGear = false;
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
		pivot = new CANTalon(Constants.GEAR_INTAKE_PIVOT);
		pivot.setPID(Constants.KP_PIVOT, Constants.KI_PIVOT, Constants.KD_PIVOT);
		pivot.changeControlMode(TalonControlMode.Position);
		//pivot.setAllowableClosedLoopErr(1);
		gearHandlerState = GearHandlerState.STOW;
	}
	
	public void update(){
		if (eStopped) return;
		if (currentlyHasGear && gearHandlerState == GearHandlerState.INTAKE){
			gearHandlerState = GearHandlerState.STOW;
		}
		switch (gearHandlerState){
			// TODO: set the polarity of all of the motor output commands (i.e. whether 1.0 is positive or negative)
			case INTAKE:
				if (intakedGear())
					currentlyHasGear = true;
					setState(GearHandlerState.STOW);
				pivot.set(90.0); //horizontal
				roller.set(1.0);
				break;
			case STOW:
				pivot.set(0.0); //vertical
				roller.set(0);
				currentlyDeploying = false;
				break;
			case PIVOT_FOR_DEPLOY:
				startDeployTime = Timer.getFPGATimestamp();
				currentlyDeploying = true;
				pivot.set(20.0);; //small angle to tilt on peg
				setState(GearHandlerState.EXHAUST);
				break;
			case EXHAUST:
				roller.set(-1.0);
				if (releasedGear()){
					setState(GearHandlerState.STOW);
					currentlyDeploying = false;
				}
				currentlyHasGear = false;
				break;
			case ESTOP:
				eStopped = true;
				break;
		}
	}

	private double getHandlerAngle() {
		return pivot.getEncPosition() * Constants.GEAR_HANDLER_TICKS_TO_ANGLE;
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
	
	private boolean intakedGear(){
		return pdp.getCurrent(Constants.PDP_GEAR_ROLLER) > intakeCurrentThreshhold;
	}
	
	public void initDefaultCommand() {
    	
    }

	@Override
	public void logToDashboard() {
		SmartDashboard.putString("Gear Intake State", "" + gearHandlerState);
		//SmartDashboard.putString("Has gear?", "" + hasGear());
		SmartDashboard.putString("Roller current", "" + pdp.getCurrent(Constants.PDP_GEAR_ROLLER));
		SmartDashboard.putString("Flipper current", "" + pdp.getCurrent(Constants.PDP_GEAR_FLIPPER));
		SmartDashboard.putString("Angle of intake", "" + getHandlerAngle());
	}
}

