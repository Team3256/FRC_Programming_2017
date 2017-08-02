package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.robot.Robot;
import org.usfirst.frc.team3256.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class FlywheelTest extends InstantCommand {
	public FlywheelTest() {
		requires(Shooter.getInstance());
	}
	
	protected void initialize() {
		Shooter.getInstance().setSpeed(2000);
		Robot.startTime = System.currentTimeMillis();
	}
}
