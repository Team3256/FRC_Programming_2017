package org.usfirst.frc.team3256.robot.subsystems;

import org.usfirst.frc.team3256.robot.RobotMap;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Hanger extends Subsystem {
	private static Hanger instance = new Hanger();
	
	private VictorSP hanger1;
	private VictorSP hanger2;
	
	private Hanger() {
		hanger1 = new VictorSP(RobotMap.HANGER_1);
		hanger2 = new VictorSP(RobotMap.HANGER_2);
	}

    public void initDefaultCommand() {

    }
	
	public static Hanger getInstance() {
		return instance;
	}
    
    public void stopHang() {
    	hanger1.set(0);
    	hanger2.set(0);
    }
}

