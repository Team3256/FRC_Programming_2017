package org.usfirst.frc.team3256.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AlignToVision extends Command {
	
	private PIDTurn wrappedTurn;
	
	public AlignToVision() {
		this.wrappedTurn = new PIDTurn(SmartDashboard.getNumber("angle", 0), SmartDashboard.getBoolean("direction", false), false);
	}
	
	@Override
	protected void initialize() {
		this.wrappedTurn.initialize();
	}
	
	@Override
	protected boolean isFinished() {
		return this.wrappedTurn.isFinished();
	}
	
	@Override
	protected void end() {
		this.wrappedTurn.end();
	}
	
	@Override
	protected void interrupted() {
		this.wrappedTurn.interrupted();
	}
}
