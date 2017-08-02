package org.usfirst.frc.team3256.robot.commands;

import org.usfirst.frc.team3256.robot.OI;
import org.usfirst.frc.team3256.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;

public class FlywheelControl extends Command {
	boolean toggle = false;
	double targetRPM = 0;
	
	public FlywheelControl() {
		requires(Shooter.getInstance());
	}
	
	protected void execute() {
		double leftY = OI.driver.getY(Hand.kLeft);
		if (Math.abs(leftY) > 0.25)
			targetRPM += leftY * 10;
		System.out.println(targetRPM);
		Shooter.getInstance().setSpeed(targetRPM);
	}
	
	@Override
	protected boolean isFinished() {
		return true;
	}

}
