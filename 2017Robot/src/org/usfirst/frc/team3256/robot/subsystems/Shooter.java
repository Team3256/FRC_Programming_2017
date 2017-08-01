package org.usfirst.frc.team3256.robot.subsystems;

import org.usfirst.frc.team3256.robot.Constants;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Shooter extends Subsystem {
	private static Shooter instance;
	
	CANTalon flywheelA;
	CANTalon flywheelB;
	
	private Shooter(){
		flywheelA = new CANTalon(Constants.FLYWHEEL_A);
		flywheelA.changeControlMode(TalonControlMode.PercentVbus);
		flywheelA.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		flywheelA.reverseSensor(true);
		flywheelA.reverseOutput(true);
		flywheelA.setF(0.00832519531);
		flywheelA.setP(0.005);
		flywheelA.setI(flywheelA.getP() / 100D);
		flywheelA.setD(flywheelA.getP() * 15D);
		flywheelA.setCurrentLimit(30);
		
		flywheelB = new CANTalon(Constants.FLYWHEEL_B);
		flywheelB.changeControlMode(TalonControlMode.Follower);
		flywheelB.set(flywheelA.getDeviceID());
		flywheelB.reverseOutput(false);
		flywheelB.setCurrentLimit(30);
	}

	public static Shooter getInstance() {
		return instance == null ? instance = new Shooter() : instance;
	}
	
	public void setPower(double power) {
		flywheelA.changeControlMode(TalonControlMode.PercentVbus);
		flywheelA.set(Math.abs(power) < 0.25 ? 0 : power);
	}
	
	public void setSpeed(double speed) {
		flywheelA.changeControlMode(TalonControlMode.Speed);
		flywheelA.set(speed * 1.5); // gear ratio is 3/2
	}
	
	public double getRPM() {
		return flywheelA.getSpeed() * 2 / 3;
	}
	
	public double getCurrentA() {
		return flywheelA.getOutputCurrent();
	}
	
	public double getCurrentB() {
		return flywheelB.getOutputCurrent();
	}
	
	@Override
	protected void initDefaultCommand() {

	}
}