package org.usfirst.frc.team3256.robot.subsystems;

import org.usfirst.frc.team3256.robot.Constants;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Manipulator extends Subsystem {
	private static Manipulator instance;
	private VictorSP innerMotor;
	private VictorSP outerMotor;
	private DoubleSolenoid ballPivot;
	private DoubleSolenoid humanIntakePivot;
	private DoubleSolenoid gearDeployer;
	
	public enum HumanPlayerLoadingState{
		GEAR_INTAKE,
		GEAR_DEPLOY,
		BALLS_INTAKE;
	}
	
	public enum IntakeState{
		GROUND_INTAKE,
		SPIT_BALLS,
		HOLD_BALLS;
	}
	HumanPlayerLoadingState loadingState = HumanPlayerLoadingState.GEAR_INTAKE;
	IntakeState intakeState = IntakeState.HOLD_BALLS;
	
	private Manipulator() {
		innerMotor = new VictorSP(Constants.INNER_MOTOR_ROLLER);
		outerMotor = new VictorSP(Constants.OUTER_MOTOR_ROLLER);
		ballPivot = new DoubleSolenoid(Constants.BALL_PIVOT_A, Constants.BALL_PIVOT_B);
		humanIntakePivot = new DoubleSolenoid(Constants.GEAR_PIVOT_A, Constants.GEAR_PIVOT_B);
	}

    public void initDefaultCommand() {
    	
    }
	
	public static Manipulator getInstance() {
		return instance == null ? instance = new Manipulator() : instance;
	}
	
	public void setHumanLoadingState(HumanPlayerLoadingState wantedState) {
		switch (wantedState){
			case GEAR_INTAKE: 
				loadingState = HumanPlayerLoadingState.GEAR_INTAKE;
				//TODO: tune direction
				humanIntakePivot.set(DoubleSolenoid.Value.kForward);
				break;
			case GEAR_DEPLOY:
				loadingState = HumanPlayerLoadingState.GEAR_DEPLOY;
				//TODO: tune direction
				humanIntakePivot.set(DoubleSolenoid.Value.kForward);
				gearDeployer.set(DoubleSolenoid.Value.kForward);
				break;
			case BALLS_INTAKE:
				loadingState = HumanPlayerLoadingState.BALLS_INTAKE;
				//TODO: tune direction
				humanIntakePivot.set(DoubleSolenoid.Value.kReverse);
				break;
			default:
				humanIntakePivot.set(DoubleSolenoid.Value.kOff);
				break;
			
		}
		
	}
	
	public void setIntakeState(IntakeState wantedState){
		switch (wantedState) {
			case GROUND_INTAKE:
				intakeState = IntakeState.GROUND_INTAKE;
				//TODO: tune
				ballPivot.set(DoubleSolenoid.Value.kForward);
				innerMotor.set(Constants.GROUND_INTAKE_POWER);
				outerMotor.set(Constants.GROUND_INTAKE_POWER);
				break;
			case SPIT_BALLS:
				intakeState = IntakeState.SPIT_BALLS;
				//TODO: tune
				ballPivot.set(DoubleSolenoid.Value.kReverse);
				innerMotor.set(Constants.SHOOT_BALLS_POWER);
				outerMotor.set(Constants.SHOOT_BALLS_POWER);
				break;
			case HOLD_BALLS:
				intakeState = IntakeState.HOLD_BALLS;
				//TODO: tune
				ballPivot.set(DoubleSolenoid.Value.kForward);
				innerMotor.set(0);
				outerMotor.set(0);
				break;
			default:
				ballPivot.set(DoubleSolenoid.Value.kOff);
				innerMotor.set(0);
				outerMotor.set(0);
				break;
		}
	}
	
	public void outputToDashboard(){
		SmartDashboard.putString("HumanPlayerIntakeState", "" + loadingState);
		SmartDashboard.putString("IntakeState", "" + intakeState);
	}
	
}
