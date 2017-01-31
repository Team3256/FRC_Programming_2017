package org.usfirst.frc.team3256.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Roller extends Subsystem {
	private static Roller instance = new Roller();
	
	private Roller() {
		
	}

    public void initDefaultCommand() {

    }
	
	private static Roller getInstance() {
		return instance;
	}
}
