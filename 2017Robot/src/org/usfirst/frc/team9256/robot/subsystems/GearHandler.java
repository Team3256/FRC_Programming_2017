package org.usfirst.frc.team9256.robot.subsystems;

import org.usfirst.frc.team9256.lib.Log;
import org.usfirst.frc.team9256.lib.Loop;
import org.usfirst.frc.team9256.robot.Constants;
import org.usfirst.frc.team9256.robot.OI;

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

/**
 *
 */
public class GearHandler extends Subsystem implements Log, Loop {

	private static GearHandler instance;
	private CANTalon gearRoller;
	private CANTalon pivot;
	private GearHandlerState gearHandlerState;
	private double startDeployTime = 0.0;
	private double manualPivotInput = 0.0;
	private double manualRollerInput = 0.0;
	private boolean currentlyDeploying = false;
	private double startSpikeTime = 0.0;
	private boolean hasSpiked = false;
	private boolean encoderDetected;
	public boolean hasGear = false;
	private TalonControlMode pivotControlMode;
	
	public enum GearHandlerState{
		//manually updating the up/down pivot motion with a joystick axis
		MANUAL_CONTROL,
		//start pivoting the intake downwards
		START_PIVOT_FOR_GEAR_INTAKE,
		//run intake to intake gear
		GEAR_INTAKE,
		//start pivoting the intake upwards to fit in the robot
		START_PIVOT_FOR_STOW_WITH_GEAR,
		//automatically bring up gear handler vertical to stow inside the robot and stop running the intake
		STOW_WITH_GEAR,
		//automatically bring down the gear handler to a set position to deploy the gear
		START_PIVOT_FOR_DEPLOY,
		//deploy the gear by exhausting the rollers
		GEAR_EXHAUST,
		//automatically bring down gear handler so we can start releasing balls
		START_PIVOT_FOR_BALL_CONTROl,
		//start pivoting to stow the intake on the floor
		START_PIVOT_FOR_STOW_LOW,
		//stop the roller
		STOW_LOW,
		// Freeze the gear handler wherever it is (stop all motors),
		STOPPED;
	}
	
	public static GearHandler getInstance(){
		return instance == null ? instance = new GearHandler() : instance;
	}

	private GearHandler(){
		gearRoller = new CANTalon(Constants.GEAR_ROLLER);
		gearRoller.setVoltageRampRate(24);
		pivot = new CANTalon(Constants.GEAR_INTAKE_PIVOT);
		pivotControlMode = TalonControlMode.PercentVbus;
		pivot.changeControlMode(pivotControlMode);
		pivot.setStatusFrameRateMs(StatusFrameRate.Feedback, 100);
		pivot.setPosition(Constants.GEAR_PIVOT_INTAKE_POS);
		pivot.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		if (pivot.isSensorPresent(FeedbackDevice.CtreMagEncoder_Relative) != FeedbackDeviceStatus.FeedbackStatusPresent){
			DriverStation.reportError("GEAR PIVOT ENCODER NOT DETECTED\n\n\n\n\n", false);
			encoderDetected = false;
		}
		else encoderDetected = true;
		pivot.reverseOutput(true);
		pivot.reverseSensor(true);
		pivot.setAllowableClosedLoopErr(5);
		pivot.configPeakOutputVoltage(10, -10);
		pivot.configNominalOutputVoltage(0, 0);
		pivot.setForwardSoftLimit(Constants.GEAR_PIVOT_FORWARD_SOFT_LIMIT_POS); 
		pivot.enableForwardSoftLimit(false);
		pivot.setReverseSoftLimit(Constants.GEAR_PIVOT_REVERSE_SOFT_LIMIT_POS);
		pivot.enableReverseSoftLimit(false);
		pivot.setPID(Constants.KP_PIVOT_POSITION, Constants.KI_PIVOT_POSITION, Constants.KD_PIVOT_POSITION, Constants.KF_PIVOT_POSITION, 
				Constants.IZONE_PIVOT_POSITION, Constants.CLOSED_LOOP_RAMP_RATE_PIVOT_POSITION, Constants.PIVOT_TALON_SLOT_POSITION);
		pivot.setPID(Constants.KP_PIVOT_MAGIC, Constants.KI_PIVOT_MAGIC, Constants.KD_PIVOT_MAGIC, Constants.KF_PIVOT_MAGIC, 
				Constants.IZONE_PIVOT_MAGIC, Constants.CLOSED_LOOP_RAMP_RATE_PIVOT_MAGIC, Constants.PIVOT_TALON_SLOT_MAGIC);
		pivot.setMotionMagicAcceleration(Constants.MAGIC_ACCELERATION);
		pivot.setMotionMagicCruiseVelocity(Constants.MAGIC_CRUISE_VELOCITY);
		pivot.set(0);
	}

	@Override
	public void initialize() {
		gearHandlerState = GearHandlerState.MANUAL_CONTROL;
	}
	
	//uses position
	@Override
	public void update(){
		if (pivot.isSensorPresent(FeedbackDevice.CtreMagEncoder_Relative) != FeedbackDeviceStatus.FeedbackStatusPresent){
			gearHandlerState = GearHandlerState.MANUAL_CONTROL;
			DriverStation.reportError("GEAR PIVOT ENCODER NOT DETECTED\n\n\n\n\n", false);
			encoderDetected = false;
		}
		else {
			encoderDetected = true;
		}
		manualPivotInput = OI.manipulator.getY(Hand.kLeft);
		manualRollerInput = OI.manipulator.getY(Hand.kRight);
		if (Math.abs(manualPivotInput) > Constants.XBOX_DEADBAND_VALUE || Math.abs(manualRollerInput) > Constants.XBOX_DEADBAND_VALUE){
			gearHandlerState = GearHandlerState.MANUAL_CONTROL;
		}
		pivotControlMode = pivot.getControlMode();
		switch (gearHandlerState){
			case MANUAL_CONTROL:
				if (pivotControlMode != TalonControlMode.PercentVbus){
					pivot.changeControlMode(TalonControlMode.PercentVbus);
				}
				if (manualPivotInput > Constants.XBOX_DEADBAND_VALUE){
					pivot.set(0.25);
				}
				else if (manualPivotInput < -Constants.XBOX_DEADBAND_VALUE){
					pivot.set(-0.25);
				}
				else pivot.set(0);
				if (manualRollerInput < -Constants.XBOX_DEADBAND_VALUE){
					gearRoller.set(Constants.GEAR_EXHAUST_POWER);
				}
				else if (manualRollerInput > Constants.XBOX_DEADBAND_VALUE){
					gearRoller.set(Constants.GEAR_INTAKE_POWER);
				}
				else gearRoller.set(0);
				break;
			case START_PIVOT_FOR_GEAR_INTAKE:
				if (hasGear) {
					setState(GearHandlerState.STOW_WITH_GEAR);
					break;
				}
				if (pivotControlMode != TalonControlMode.Position){
					pivot.changeControlMode(TalonControlMode.Position);
					pivot.setProfile(Constants.PIVOT_TALON_SLOT_POSITION);
				}
				pivot.set(Constants.GEAR_PIVOT_INTAKE_POS);
				setState(GearHandlerState.GEAR_INTAKE);
				break;
			case GEAR_INTAKE:
				updateHasGear();
				gearRoller.set(Constants.GEAR_INTAKE_POWER);
				if (hasGear)
					setState(GearHandlerState.START_PIVOT_FOR_STOW_WITH_GEAR);
				break;
			case START_PIVOT_FOR_STOW_WITH_GEAR:
				gearRoller.set(0);
				if (pivotControlMode != TalonControlMode.Position){
					pivot.changeControlMode(TalonControlMode.Position);
					pivot.setProfile(Constants.PIVOT_TALON_SLOT_POSITION);
				}
				pivot.set(Constants.GEAR_PIVOT_STOW_POS);
				setState(GearHandlerState.STOW_WITH_GEAR);
				break;
			case STOW_WITH_GEAR:
				gearRoller.set(0);
				currentlyDeploying = false;
				break;
			case START_PIVOT_FOR_DEPLOY:
				if (pivotControlMode != TalonControlMode.Position){
					pivot.changeControlMode(TalonControlMode.Position);
					pivot.setProfile(Constants.PIVOT_TALON_SLOT_POSITION);
				}
				startDeployTime = Timer.getFPGATimestamp();
				currentlyDeploying = true;
				pivot.set(Constants.GEAR_PIVOT_DEPLOY_POS);
				setState(GearHandlerState.GEAR_EXHAUST);
				break;
			case GEAR_EXHAUST:
				if (releasedGear()){
					setState(GearHandlerState.START_PIVOT_FOR_STOW_LOW);
					currentlyDeploying = false;
				}
				hasGear = false;
				gearRoller.set(Constants.GEAR_EXHAUST_POWER);
				break;
			case STOPPED:
				gearRoller.set(0);
				if (pivotControlMode != TalonControlMode.PercentVbus){
					pivot.changeControlMode(TalonControlMode.PercentVbus);
				}
				pivot.set(0);
				break;
			case START_PIVOT_FOR_STOW_LOW:
				gearRoller.set(0);
				if (hasGear) return;
				if (pivotControlMode != TalonControlMode.Position){
					pivot.changeControlMode(TalonControlMode.Position);
					pivot.setProfile(Constants.PIVOT_TALON_SLOT_POSITION);
				}
				pivot.set(Constants.GEAR_PIVOT_STOW_LOS_POS);
				setState(GearHandlerState.STOW_LOW);
				break;
			case STOW_LOW:
				gearRoller.set(0);
				currentlyDeploying = false;
				break;
		}
	}

	@Override
	public void end() {
		pivot.changeControlMode(TalonControlMode.PercentVbus);
		pivot.set(0);
	}

	
	public void setEncoderPosition(double pos) {
		DriverStation.reportWarning("Gear Handler Rest \n\n\n\n\n\n" + DriverStation.getInstance().getMatchTime(), false);
		pivot.setPosition(pos);
	}
	
	public GearHandlerState getState() {
		return gearHandlerState;
	}

	private boolean releasedGear(){
		return Timer.getFPGATimestamp()-startDeployTime > 5.0 && currentlyDeploying && !hasGear;
	}
	
	public void setState(GearHandlerState wantedState){
		gearHandlerState = wantedState;
	}
	
	public GearHandlerState getGearHandlerState() {
		return gearHandlerState;
	}
	
	public boolean hasEncoder(){
		return encoderDetected;
	}
	
	public boolean updateHasGear(){
		if (getRollerCurrent() > 20 && !hasSpiked){
			hasSpiked = true;
			startSpikeTime = Timer.getFPGATimestamp();
		}
		if (hasSpiked && Timer.getFPGATimestamp() - startSpikeTime > 0.15){
			hasGear = true;
			hasSpiked = false;
			return true;
		}
		return false;
	}
	
	public double getRollerCurrent() {
		return gearRoller.getOutputCurrent();
	}
	
	public void initDefaultCommand() {
    	
    }
	
	@Override
	public void logToDashboard() {
		SmartDashboard.putString("Gear Intake State", "" + gearHandlerState);
		SmartDashboard.putString("Gear Handler Pivot Control Mode", "" + pivotControlMode);
		SmartDashboard.putNumber("Has gear?",!hasGear ? 0 : 1);
		SmartDashboard.putNumber("Pivot Position", pivot.getPosition());
		SmartDashboard.putNumber("Roller Current", getRollerCurrent());
		
	}
}