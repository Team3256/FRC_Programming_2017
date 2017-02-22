package org.usfirst.frc.team3256.robot.automodes;

import org.usfirst.frc.team3256.lib.DelayedCommand;
import org.usfirst.frc.team3256.robot.commands.DeployGear;
import org.usfirst.frc.team3256.robot.commands.DriveToDistance;
import org.usfirst.frc.team3256.robot.commands.MotionProfiledTurn;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class RedGearCenterAuto extends CommandGroup {
	public RedGearCenterAuto() {
		
		//drive backwards to center gear peg
		addSequential(new DriveToDistance(67, false)); 
		
		//deploy gear
		addParallel(new DeployGear());
		
		//drive forward
		addSequential(new DelayedCommand(1,new DriveToDistance(27,true)));
		
		addSequential(new WaitCommand(0.5));
		
		//turn so the roller side faces the boiler
		addSequential(new MotionProfiledTurn(90,false));
		

		addSequential(new WaitCommand(0.5));

		//drive to the boiler
		addSequential(new DriveToDistance(100,true));
		

		addSequential(new WaitCommand(0.5));
		
		//turn 45 degrees to face boiler
		addSequential(new MotionProfiledTurn(45,true));
		

		addSequential(new WaitCommand(0.5));
		
		//drive up to the boiler
		addSequential(new DriveToDistance(40,true)); 
		//shoot balls
		/*
		addSequential(new ShootBalls());
		*/
	}
}
