package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class FlywheelStop extends InstantCommand {
	protected void initialize() {
		Shooter.getInstance().setSpeed(0);
	}
}