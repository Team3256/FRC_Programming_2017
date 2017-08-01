package org.usfirst.frc.team3256.robot.subsystems;

import org.usfirst.frc.team3256.robot.Constants;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Roller extends Subsystem {
	private VictorSP roller;
	private static Roller instance;
	
	private Roller() {
		roller = new VictorSP(Constants.ROLLER_MOTOR);
	}
	
	public void set(double speed) {
		roller.set(Math.abs(speed) < 0.15 ? 0 : speed);
	}
	
	public static Roller getInstance() {
		return instance == null ? instance = new Roller() : instance;
	}
	
	@Override
	protected void initDefaultCommand() {

	}
}
