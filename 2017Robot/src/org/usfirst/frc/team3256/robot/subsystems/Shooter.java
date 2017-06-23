package org.usfirst.frc.team3256.robot.subsystems;

import org.usfirst.frc.team3256.robot.Constants;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Shooter extends Subsystem {
	
	private static Shooter instance;
	private CANTalon shooter1, shooter2, shooter3, shooter4;
	private ShooterState shooterState;
	
	//holds cases for shooter state
	public enum ShooterState {
		SHOOTING,
		STOPPED
	}
	
	private Shooter() {
		//initialize each CANTalon with its proper port
		shooter1 = new CANTalon(Constants.SHOOTER_1);
		shooter2 = new CANTalon(Constants.SHOOTER_2);
		shooter3 = new CANTalon(Constants.SHOOTER_3);
		shooter4 = new CANTalon(Constants.SHOOTER_4);
		/*
		 * we want all motors going the same speed, so we will have the rest of the motors follow the first motor
		 * this way we only need to set the speed of the first
		 */
		shooter1.changeControlMode(TalonControlMode.Speed);
		shooter2.changeControlMode(TalonControlMode.Follower);
		shooter2.set(shooter1.getDeviceID());
		shooter3.changeControlMode(TalonControlMode.Follower);
		shooter3.set(shooter1.getDeviceID());
		shooter4.changeControlMode(TalonControlMode.Follower);
		shooter4.set(shooter1.getDeviceID());
	}
	
	/** Method used to get the instance of the Shooter class
	 * @return - if the instance is null, creates a new instance, otherwise returns the instance
	 */
	public static Shooter getInstance() {
		return instance == null ? instance = new Shooter() : instance;
	}
	
	public void update() {
		//update the shooter state and set motor power accordingly
		switch (shooterState) {
		case STOPPED:
			shooter1.set(0);
		case SHOOTING:
			shooter1.set(1);
		}
	}
	
	/** Method to set the shooter state
	 * @param shooterState - the state to set the shooter state to
	 */
	public void setShooterState(ShooterState shooterState) {
		this.shooterState = shooterState;
	}
	
	/** Method to get the shooter state
	 * @return - returns the shooter state
	 */
	public ShooterState getShooterState() {
		return shooterState;
	}
	
	@Override
	protected void initDefaultCommand() {

	}
}