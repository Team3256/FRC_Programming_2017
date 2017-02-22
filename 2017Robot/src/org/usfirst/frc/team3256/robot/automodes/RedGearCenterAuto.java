package org.usfirst.frc.team3256.robot.automodes;

import org.usfirst.frc.team3256.lib.DelayedCommand;
import org.usfirst.frc.team3256.robot.commands.DeployGear;
import org.usfirst.frc.team3256.robot.commands.DriveToDistance;
import org.usfirst.frc.team3256.robot.commands.MotionProfiledTurn;
import org.usfirst.frc.team3256.robot.commands.ShootBalls;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RedGearCenterAuto extends CommandGroup {
	public RedGearCenterAuto() {
		
		//drive backwards to center gear peg
		addSequential(new DriveToDistance(67, false)); 
		
		//deploy gear
		addParallel(new DeployGear());
		
		//drive forward
		addSequential(new DelayedCommand(1,new DriveToDistance(27,true)));
		
		/*
		//turn so the roller side faces the boiler
		addSequential(new MotionProfiledTurn(90,false));
		//drive to the boiler
		addSequential(new DriveToDistance(110,true));
		//turn 45 degrees to face boiler
		addSequential(new MotionProfiledTurn(45,true));
		//drive up to the boiler
		addSequential(new DriveToDistance(30,true));
		//shoot balls
		addSequential(new ShootBalls());
		*/
	}
}
