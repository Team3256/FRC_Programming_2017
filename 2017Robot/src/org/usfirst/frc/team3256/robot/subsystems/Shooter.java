package org.usfirst.frc.team3256.robot.subsystems;

import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.commands.FlywheelControl;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Shooter extends Subsystem {
	private static Shooter instance;
	
	private CANTalon flywheelMaster;
	private CANTalon flywheelSlave;
	
	private Shooter(){
		flywheelMaster = new CANTalon(Constants.FLYWHEEL_MASTER);
		flywheelMaster.changeControlMode(TalonControlMode.PercentVbus);
		flywheelMaster.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		flywheelMaster.reverseSensor(true);
		flywheelMaster.reverseOutput(true);
		flywheelMaster.setF(Constants.kShooterF);
		flywheelMaster.setP(Constants.kShooterP);
		flywheelMaster.setI(Constants.kShooterI);
		flywheelMaster.setD(Constants.kShooterD);
		flywheelMaster.setCurrentLimit(Constants.kCurrentLimit);
		flywheelMaster.EnableCurrentLimit(true);

		
		flywheelSlave = new CANTalon(Constants.FLYWHEEL_SLAVE);
		flywheelSlave.changeControlMode(TalonControlMode.Follower);
		flywheelSlave.set(flywheelMaster.getDeviceID());
		flywheelSlave.reverseOutput(false);
		flywheelSlave.setCurrentLimit(Constants.kCurrentLimit);
		flywheelSlave.EnableCurrentLimit(true);
	}

	public static Shooter getInstance() {
		return instance == null ? instance = new Shooter() : instance;
	}
	
	public void setPower(double power) {
		flywheelMaster.changeControlMode(TalonControlMode.PercentVbus);
		flywheelMaster.set(power);
	}
	
	public void setSpeed(double speed) {
		flywheelMaster.changeControlMode(TalonControlMode.Speed);
		flywheelMaster.set(speed * Constants.kGearRatio); 
	}
	
	public double getRPM() {
		return flywheelMaster.getSpeed() / Constants.kGearRatio;
	}
	
	public double getCurrentA() {
		return flywheelMaster.getOutputCurrent();
	}
	
	public double getCurrentB() {
		return flywheelSlave.getOutputCurrent();
	}
	
	@Override
	protected void initDefaultCommand() {
		
	}
}