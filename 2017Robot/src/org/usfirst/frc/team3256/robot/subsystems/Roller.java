package org.usfirst.frc.team3256.robot.subsystems;

import org.usfirst.frc.team3256.lib.Log;
import org.usfirst.frc.team3256.lib.PDP;
import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.OI;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Roller extends Subsystem implements Log{

	private static Roller instance;
	
	private VictorSP innerMotor;
	private VictorSP outerMotor;
	private DoubleSolenoid ballPivot;
	private PDP pdp;

	/**
	 * The states of the intake/shooter of the robot
	 */
	public enum RollerState {
		GROUND_INTAKE, SPIT_BALLS, STOPPED;
	}
	
	RollerState rollerState = RollerState.STOPPED;
	
	public static Roller getInstance(){
		return instance == null ? instance = new Roller() : instance;
	}
	
	private Roller(){
		innerMotor = new VictorSP(Constants.INNER_MOTOR_ROLLER);
		outerMotor = new VictorSP(Constants.OUTER_MOTOR_ROLLER);
		ballPivot = new DoubleSolenoid(Constants.BALL_PIVOT_A, Constants.BALL_PIVOT_B);
		pdp = PDP.getInstance();
	}
	
	/**
	 * 
	 * @param wantedState
	 *            - the wanted intake state of the Manipulator subsystem
	 */
	public void setRollerState(RollerState wantedState) {
		switch (wantedState) {
		case GROUND_INTAKE: 
			rollerState = RollerState.GROUND_INTAKE;
			// TODO: tune
			ballPivot.set(DoubleSolenoid.Value.kReverse);
			innerMotor.set(Constants.GROUND_INTAKE_POWER);
			outerMotor.set(-Constants.GROUND_INTAKE_POWER);
			break;
		case SPIT_BALLS:
			rollerState = RollerState.SPIT_BALLS;
			// TODO: tune
			ballPivot.set(DoubleSolenoid.Value.kForward);
			innerMotor.set(Constants.SHOOT_BALLS_POWER);
			outerMotor.set(Constants.SHOOT_BALLS_POWER);
			break;
		case STOPPED:
			rollerState = RollerState.STOPPED;
			// TODO: tune
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
	
    public void initDefaultCommand() {
    	
    }

	@Override
	public void logToDashboard() {
		SmartDashboard.putString("RollerState", "" + rollerState);
		SmartDashboard.putNumber("Inner Motor: PWM-" + innerMotor.getChannel() + " ", innerMotor.get());
		SmartDashboard.putNumber("Outer Motor: PWM-" + outerMotor.getChannel() + " ", outerMotor.get());
		SmartDashboard.putString("Ball Pivot: ", "" + ballPivot.get());
		SmartDashboard.putNumber("Inner Motor PDP", pdp.getCurrent(Constants.PDP_INNER_MOTOR_ROLLER));
		SmartDashboard.putNumber("Outer Motor PDP", pdp.getCurrent(Constants.PDP_OUTER_MOTOR_ROLLER));
	}
}

