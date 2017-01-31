package org.usfirst.frc.team3256.robot.subsystems;

import org.usfirst.frc.team3256.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Roller extends Subsystem {
	private static Roller instance;
	private VictorSP innerMotor;
	private VictorSP outerMotor;
	private DoubleSolenoid ballPivot;
	private DoubleSolenoid gearPivot;
	
	private Roller() {
		innerMotor = new VictorSP(RobotMap.INNER_MOTOR_ROLLER);
		outerMotor = new VictorSP(RobotMap.OUTER_MOTOR_ROLLER);
		ballPivot = new DoubleSolenoid(RobotMap.BALL_PIVOT_A, RobotMap.BALL_PIVOT_B);
		gearPivot = new DoubleSolenoid(RobotMap.GEAR_PIVOT_A, RobotMap.GEAR_PIVOT_B);
		
	}

    public void initDefaultCommand() {

    }
	
	private static Roller getInstance() {
		return instance==null ? new Roller() : instance;
	}
	
	public void intakeBalls() {
		//TODO: implement
	}
	
	public void outtakeBalls() {
		//TODO: implement
	}
	
	public void setHumanLoadingState(boolean forGear) {
		if (forGear){
			//TODO: implement
		}
		//for ball
		else{
			//TODO:implement
		}
	}
	
	
	
}
