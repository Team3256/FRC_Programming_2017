package org.usfirst.frc.team3256.robot.automodes;

import org.usfirst.frc.team3256.lib.DelayedCommand;
import org.usfirst.frc.team3256.robot.commands.DeployGear;
import org.usfirst.frc.team3256.robot.commands.DriveToDistance;
import org.usfirst.frc.team3256.robot.commands.MotionProfiledTurn;
import org.usfirst.frc.team3256.robot.commands.PIDTurn;
import org.usfirst.frc.team3256.robot.commands.ShootBalls;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * drives to gear with right turn towards gear station
 */
public class GearCenterRightAuto extends CommandGroup {

	public GearCenterRightAuto() {
		// drive backwards to center gear peg
		addSequential(new DriveToDistance(77, false));

		//deploy gear
		addParallel(new DeployGear());
		
		//drive forward
		addSequential(new DelayedCommand(1,new DriveToDistance(30,true)));

		//turn right
		addSequential(new PIDTurn(90, false));
		
		//drive out of the way of the airship
		addSequential(new DriveToDistance(36, false));
		
		//turn left
		addSequential(new PIDTurn(90, true));
		
		//drive towards gear station
		addSequential(new DriveToDistance(120, false));
	}
}
