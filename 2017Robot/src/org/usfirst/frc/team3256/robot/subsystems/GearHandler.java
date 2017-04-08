package org.usfirst.frc.team3256.robot.subsystems;

import org.usfirst.frc.team3256.lib.LEDStrip;
import org.usfirst.frc.team3256.lib.Log;
import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.OI;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.FeedbackDeviceStatus;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.CANSpeedController.ControlMode;
import edu.wpi.first.wpilibj.GenericHID.Hand;
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
	private LEDStrip ledStrip = LEDStrip.getInstance();
	private DigitalInput gearBumperSwitch;
	private GearHandlerState gearHandlerState;
	private double startIntakePivotTime = 0.0;
	private double startDeployTime = 0.0;
	private double manualInput = 0.0;
	private boolean currentlyDeploying = false;
	private TalonControlMode pivotControlMode;
	private int absolutePosition = 0;
	
	public enum GearHandlerState{
		//manually updating the up/down pivot motion with a joystick axis
		MANUAL_PIVOT,
		//start pivoting the intake downwards
		START_PIVOT_FOR_INTAKE,
		//run intake to intake gear
		INTAKE,
		//start pivoting the intake upwards to fit in the robot
		START_PIVOT_FOR_STOW,
		//automatically bring up gear handler vertical to stow inside the robot and stop running the intake
		STOW,
		//automatically bring down the gear handler to a set position to deploy the gear
		START_PIVOT_FOR_DEPLOY,
		//deploy the gear by exhausting the rollers
		EXHAUST,
		// Freeze the gear handler wherever it is (stop all motors)
		STOPPED;
	}
	
	public static GearHandler getInstance(){
		return instance == null ? instance = new GearHandler() : instance;
	}

	private GearHandler(){
		roller = new VictorSP(Constants.GEAR_ROLLER);
		pivot = new CANTalon(Constants.GEAR_INTAKE_PIVOT);
		pivot.setPID(Constants.KP_PIVOT, Constants.KI_PIVOT, Constants.KD_PIVOT);
		pivot.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		if (pivot.isSensorPresent(FeedbackDevice.CtreMagEncoder_Absolute) != FeedbackDeviceStatus.FeedbackStatusPresent){
			DriverStation.reportError("Did not detect encoder for gear pivot", true);
		}
		absolutePosition = pivot.getEncPosition();
		pivot.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		if (pivot.isSensorPresent(FeedbackDevice.CtreMagEncoder_Relative) != FeedbackDeviceStatus.FeedbackStatusPresent){
			DriverStation.reportError("Did not detect encoder for gear pivot", true);
		}
		pivot.setEncPosition(absolutePosition);
		pivotControlMode = TalonControlMode.Position;
		pivot.changeControlMode(pivotControlMode);
		//pivot.setForwardSoftLimit(); TODO: empirically find the actual desired encoder limits and set them
		pivot.enableForwardSoftLimit(false);
		//pivot.setReverseSoftLimit(); TODO: empirically find the actual desired encoder limits and set them
		pivot.enableReverseSoftLimit(false);
		pivot.set(0);
		gearBumperSwitch = new DigitalInput(Constants.GEAR_BUMPER_SWITCH);
		gearHandlerState = GearHandlerState.STOPPED;
	}
	
	public void setCAN(double outputValue) {
		if (pivot.getControlMode()!= TalonControlMode.PercentVbus){
			pivot.changeControlMode(TalonControlMode.PercentVbus);
		}
		pivot.set(outputValue);
	}
	
	public void update(){
		double timeEnd = Timer.getFPGATimestamp() - (int) Timer.getFPGATimestamp(); //gets decimal portion of time stamp
		//blinks led every half second
		if (hasGear()){
			if (timeEnd < 0.5)
				ledStrip.turnOff();
			else
				ledStrip.green();
		}
		else {
			if (timeEnd < 0.5)
				ledStrip.turnOff();
			else
				ledStrip.red();				
		}
		manualInput = OI.manipulator.getY(Hand.kLeft);
		if (Math.abs(manualInput) > Constants.XBOX_DEADBAND_VALUE){
			gearHandlerState = GearHandlerState.MANUAL_PIVOT;
		}
		
		pivotControlMode = pivot.getControlMode();
		switch (gearHandlerState){
			case MANUAL_PIVOT:
				if (pivotControlMode != TalonControlMode.PercentVbus){
					pivot.changeControlMode(TalonControlMode.PercentVbus);
				}
				if (manualInput > 0){
					pivot.set(0.5);
				}
				else if (manualInput < 0){
					pivot.set(-0.5);
				}
				else pivot.set(0);
				break;
			case START_PIVOT_FOR_INTAKE:
				if (pivotControlMode != TalonControlMode.Position){
					pivot.changeControlMode(TalonControlMode.Position);
				}
				startIntakePivotTime = Timer.getFPGATimestamp();
				pivot.set(90.0);
				setState(GearHandlerState.INTAKE);
				break;
			case INTAKE:
				if (onTarget() || Timer.getFPGATimestamp()-startIntakePivotTime>2.0){
					roller.set(1.0);
				}
				if (hasGear())
					setState(GearHandlerState.STOW);
				break;
			case START_PIVOT_FOR_STOW:
				roller.set(0);
				if (pivotControlMode != TalonControlMode.Position){
					pivot.changeControlMode(TalonControlMode.Position);
				}
				pivot.set(0.0);
				setState(GearHandlerState.STOW);
			case STOW:
				roller.set(0);
				currentlyDeploying = false;
				break;
			case START_PIVOT_FOR_DEPLOY:
				if (pivotControlMode != TalonControlMode.Position){
					pivot.changeControlMode(TalonControlMode.Position);
				}
				startDeployTime = Timer.getFPGATimestamp();
				currentlyDeploying = true;
				pivot.set(20.0); //small angle to tilt on peg
				setState(GearHandlerState.EXHAUST);
				break;
			case EXHAUST:
				if (onTarget() || startDeployTime-Timer.getFPGATimestamp()>2){
					roller.set(-1.0);
				}
				if (releasedGear()){
					setState(GearHandlerState.START_PIVOT_FOR_STOW);
					currentlyDeploying = false;
				}
				break;
			case STOPPED:
				roller.set(0);
				pivot.set(0);
				break;
		}
	}

	public GearHandlerState getState(){
		return gearHandlerState;
	}

	private boolean releasedGear(){
		return Timer.getFPGATimestamp()-startDeployTime > 5.0 && currentlyDeploying && !hasGear();
	}
	
	public void setState(GearHandlerState wantedState){
		switch (wantedState){
			case MANUAL_PIVOT:
				gearHandlerState = GearHandlerState.MANUAL_PIVOT;
				break;
			case START_PIVOT_FOR_INTAKE:
				gearHandlerState = GearHandlerState.START_PIVOT_FOR_INTAKE;
				break;
			case INTAKE:
				gearHandlerState = GearHandlerState.INTAKE;
				break;
			case START_PIVOT_FOR_STOW:
				gearHandlerState = GearHandlerState.START_PIVOT_FOR_STOW;
				break;
			case STOW:
				gearHandlerState = GearHandlerState.STOW;
				break;
			case START_PIVOT_FOR_DEPLOY:
				gearHandlerState = GearHandlerState.START_PIVOT_FOR_DEPLOY;
				break;
			case EXHAUST:
				gearHandlerState = GearHandlerState.EXHAUST;
				break;
		}
	}
	
	//TODO: implement actual numbers
	private boolean onTarget(){
		return getError() < 1;
	}
	
	//TODO: scale values accordingly
	private double getError(){
		return Math.abs(pivot.getSetpoint()-pivot.getEncPosition());
	}
	
	private boolean hasGear(){
		return !gearBumperSwitch.get();
	}
	
	public void initDefaultCommand() {
    	
    }

	@Override
	public void logToDashboard() {
		SmartDashboard.putString("Gear Intake State", "" + gearHandlerState);
		SmartDashboard.putString("Gear Handler Pivot Control Mode", "" + pivotControlMode);
		SmartDashboard.putBoolean("Has gear?",hasGear());
	}
}

