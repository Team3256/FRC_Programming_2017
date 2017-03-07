package org.usfirst.frc.team3256.robot.automodes;


import org.usfirst.frc.team3256.robot.commands.DriveToDistance;
import org.usfirst.frc.team3256.robot.commands.HumanPlayerBallsIntake;
import org.usfirst.frc.team3256.robot.commands.PIDTurn;


import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class HopperAutoBlue extends CommandGroup {
	public HopperAutoBlue() {
		addSequential(new DriveToDistance(50, false)); //initial drive forward
		addSequential(new WaitCommand(0.5));
		addSequential(new PIDTurn(90, false)); //turn towards hopper
		addSequential(new HumanPlayerBallsIntake()); //make sure the robot can collect the balls from hopper
		addSequential(new WaitCommand(0.5));
		addSequential(new DriveToDistance(30, false)); //trigger hopper
		
	}
}
