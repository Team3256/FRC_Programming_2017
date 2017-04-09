package org.usfirst.frc.team3256.robot.subsystems;

import org.usfirst.frc.team3256.lib.Log;
import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.OI;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.FeedbackDeviceStatus;
import com.ctre.CANTalon.StatusFrameRate;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// NOTE: This code depends on the VersaPlanetary Integrated Encoder

/**
 *
 */
public class GearHandler extends Subsystem implements Log {

	private static GearHandler instance;
	private VictorSP gearRoller;
	private CANTalon pivot;
	private DigitalInput gearBumperSwitch;
	private GearHandlerState gearHandlerState;
	private double startDeployTime = 0.0;
	private double manualInput = 0.0;
	private boolean currentlyDeploying = false;
	private TalonControlMode pivotControlMode;
	
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
		gearRoller = new VictorSP(Constants.GEAR_ROLLER);
		pivot = new CANTalon(Constants.GEAR_INTAKE_PIVOT);
		pivotControlMode = TalonControlMode.PercentVbus;
		pivot.changeControlMode(pivotControlMode);
		pivot.setStatusFrameRateMs(StatusFrameRate.Feedback, 100);
		pivot.setPosition(30);
		pivot.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		if (pivot.isSensorPresent(FeedbackDevice.CtreMagEncoder_Relative) != FeedbackDeviceStatus.FeedbackStatusPresent){
			SmartDashboard.putBoolean("Does not detect mag encoder?", false);
		}
		pivot.reverseOutput(true);
		pivot.reverseSensor(true);
		pivot.setAllowableClosedLoopErr(10);
		pivot.configPeakOutputVoltage(8, -8);
		pivot.configNominalOutputVoltage(0, 0);
		//pivot.setForwardSoftLimit(); TODO: empirically find the actual desired encoder limits and set them
		pivot.enableForwardSoftLimit(false);
		//pivot.setReverseSoftLimit(); TODO: empirically find the actual desired encoder limits and set them
		pivot.enableReverseSoftLimit(false);
		pivot.setPID(Constants.KP_PIVOT_POSITION, Constants.KI_PIVOT_POSITION, Constants.KD_PIVOT_POSITION, Constants.KF_PIVOT_POSITION, 
				Constants.IZONE_PIVOT_POSITION, Constants.CLOSED_LOOP_RAMP_RATE_PIVOT_POSITION, Constants.PIVOT_TALON_SLOT_POSITION);
		pivot.setPID(Constants.KP_PIVOT_MAGIC, Constants.KI_PIVOT_MAGIC, Constants.KD_PIVOT_MAGIC, Constants.KF_PIVOT_MAGIC, 
				Constants.IZONE_PIVOT_MAGIC, Constants.CLOSED_LOOP_RAMP_RATE_PIVOT_MAGIC, Constants.PIVOT_TALON_SLOT_MAGIC);
		pivot.setMotionMagicAcceleration(Constants.MAGIC_ACCELERATION);
		pivot.setMotionMagicCruiseVelocity(Constants.MAGIC_CRUISE_VELOCITY);
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
	
	//uses magic profile
	public void update(){
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
				if (manualInput > Constants.XBOX_DEADBAND_VALUE){
					pivot.set(0.5);
				}
				else if (manualInput < -Constants.XBOX_DEADBAND_VALUE){
					pivot.set(-0.5);
				}
				else pivot.set(0);
				break;
			case START_PIVOT_FOR_INTAKE:
				if (pivotControlMode != TalonControlMode.MotionMagic){
					pivot.changeControlMode(TalonControlMode.MotionMagic);
					pivot.setProfile(Constants.PIVOT_TALON_SLOT_MAGIC);
				}
				pivot.set(0.0);
				setState(GearHandlerState.INTAKE);
				break;
			case INTAKE:
				gearRoller.set(-1);
				if (hasGear())
					setState(GearHandlerState.START_PIVOT_FOR_STOW);
				break;
			case START_PIVOT_FOR_STOW:
				gearRoller.set(0);
				if (pivotControlMode != TalonControlMode.MotionMagic){
					pivot.changeControlMode(TalonControlMode.MotionMagic);
					pivot.setProfile(Constants.PIVOT_TALON_SLOT_MAGIC);
				}
				pivot.set(30.0);
				setState(GearHandlerState.STOW);
			case STOW:
				gearRoller.set(0);
				currentlyDeploying = false;
				break;
			case START_PIVOT_FOR_DEPLOY:
				if (pivotControlMode != TalonControlMode.MotionMagic){
					pivot.changeControlMode(TalonControlMode.MotionMagic);
					pivot.setProfile(Constants.PIVOT_TALON_SLOT_MAGIC);
				}
				startDeployTime = Timer.getFPGATimestamp();
				currentlyDeploying = true;
				pivot.set(25.0);
				setState(GearHandlerState.EXHAUST);
				break;
			case EXHAUST:
				gearRoller.set(-1.0);
				if (releasedGear()){
					setState(GearHandlerState.START_PIVOT_FOR_STOW);
					currentlyDeploying = false;
				}
				break;
			case STOPPED:
				gearRoller.set(0);
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
			case STOPPED:
				gearHandlerState = GearHandlerState.STOPPED;
				break;
		}
	}
	
	public boolean hasGear(){
		// Gear bumper switch is set to true when open; gear falling into gear handler sets it to false
		return !gearBumperSwitch.get();
	}
	
	public void initDefaultCommand() {
    	
    }

	public double getPosition(){
		return pivot.getEncPosition();
	}
	
	@Override
	public void logToDashboard() {
		SmartDashboard.putString("Gear Intake State", "" + gearHandlerState);
		SmartDashboard.putString("Gear Handler Pivot Control Mode", "" + pivotControlMode);
		SmartDashboard.putNumber("Gear Handler Encoder Ticks", getPosition());
		SmartDashboard.putBoolean("Has gear?",hasGear());
	}
}

