package org.usfirst.frc.team3256.robot.subsystems;

import org.usfirst.frc.team3256.robot.Constants;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Shooter extends Subsystem {
	private static Shooter instance;
	private CANTalon shooter1, shooter2, shooter3, shooter4;
	private ShooterState shooterState;
	
	public enum ShooterState {
		SHOOTING,
		STOPPED
	}
	
	private Shooter() {
		shooter1 = new CANTalon(Constants.SHOOTER_1);
		shooter2 = new CANTalon(Constants.SHOOTER_2);
		shooter3 = new CANTalon(Constants.SHOOTER_3);
		shooter4 = new CANTalon(Constants.SHOOTER_4);
		
		shooter1.changeControlMode(TalonControlMode.Speed);
		shooter2.changeControlMode(TalonControlMode.Follower);
		shooter2.set(shooter1.getDeviceID());
		shooter3.changeControlMode(TalonControlMode.Follower);
		shooter3.set(shooter1.getDeviceID());
		shooter4.changeControlMode(TalonControlMode.Follower);
		shooter4.set(shooter1.getDeviceID());
	}
	
	public static Shooter getInstance() {
		return instance == null ? instance = new Shooter() : instance;
	}
	
	public void update() {
		switch (shooterState) {
		case STOPPED:
			shooter1.set(0);
		case SHOOTING:
			shooter1.set(1);
		}
	}
	
	public void setShooterState(ShooterState shooterState) {
		this.shooterState = shooterState;
	}
	
	public ShooterState getShooterState() {
		return shooterState;
	}
	
	@Override
	protected void initDefaultCommand() {

	}
}
