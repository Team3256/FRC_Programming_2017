package org.usfirst.frc.team3256.robot.subsystems;

import org.omg.PortableInterceptor.ObjectIdHelper;
import org.usfirst.frc.team3256.lib.Log;
import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.OI;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Manipulator extends Subsystem implements Log {

	// Singleton instance of the Manipulator Subsystem
	private static Manipulator instance;

	private VictorSP innerMotor;
	private VictorSP outerMotor;
	private DoubleSolenoid ballPivot;
	private DoubleSolenoid humanIntakePivot;
	private DoubleSolenoid gearDeployer;

	/**
	 * The states of the human player intake
	 */
	public enum HumanPlayerLoadingState {
		GEAR_INTAKE, GEAR_DEPLOY, BALLS_INTAKE;
	}

	/**
	 * The states of the intake/shooter of the robot
	 */
	public enum IntakeState {
		GROUND_INTAKE, SPIT_BALLS, LIFT_FOR_DRIVING;
	}

	// The current states of the systems
	HumanPlayerLoadingState loadingState = HumanPlayerLoadingState.GEAR_INTAKE;
	IntakeState intakeState = IntakeState.LIFT_FOR_DRIVING;

	/**
	 * Cannot be instantiated out of the class and initializes the manipulator
	 * motors and actuators so we will always only have one Manipulator instance
	 * Use the getInstance() method
	 */
	private Manipulator() {
		innerMotor = new VictorSP(Constants.INNER_MOTOR_ROLLER);
		outerMotor = new VictorSP(Constants.OUTER_MOTOR_ROLLER);
		ballPivot = new DoubleSolenoid(Constants.BALL_PIVOT_A, Constants.BALL_PIVOT_B);
		humanIntakePivot = new DoubleSolenoid(Constants.GEAR_PIVOT_A, Constants.GEAR_PIVOT_B);
		gearDeployer = new DoubleSolenoid(Constants.GEAR_DEPLOY_A, Constants.GEAR_DEPLOY_B);
	}

	/**
	 * Sets the default command of the Manipulator Subsystem There is no default
	 * command for it.
	 */
	public void initDefaultCommand() {

	}

	/**
	 * @return The singleton instance of the Manipulator class
	 */
	public static Manipulator getInstance() {
		return instance == null ? instance = new Manipulator() : instance;
	}

	/**
	 * @param wantedState
	 *            - The wanted human player loading state of the Manipulator
	 *            subsystem.
	 */
	public void setHumanLoadingState(HumanPlayerLoadingState wantedState) {
		switch (wantedState) {
		case GEAR_INTAKE:
			loadingState = HumanPlayerLoadingState.GEAR_INTAKE;
			gearDeployer.set(DoubleSolenoid.Value.kForward);
			humanIntakePivot.set(DoubleSolenoid.Value.kReverse);
			break;
		case GEAR_DEPLOY:
			loadingState = HumanPlayerLoadingState.GEAR_DEPLOY;
			gearDeployer.set(DoubleSolenoid.Value.kReverse);
			break;
		case BALLS_INTAKE:
			loadingState = HumanPlayerLoadingState.BALLS_INTAKE;
			humanIntakePivot.set(DoubleSolenoid.Value.kForward);
			break;
		default:
			humanIntakePivot.set(DoubleSolenoid.Value.kOff);
			gearDeployer.set(DoubleSolenoid.Value.kOff);
			break;
		}

	}

	/**
	 * 
	 * @param wantedState
	 *            - the wanted intake state of the Manipulator subsystem
	 */
	public void setIntakeState(IntakeState wantedState) {
		switch (wantedState) {
		case GROUND_INTAKE: 
			intakeState = IntakeState.GROUND_INTAKE;
			// TODO: tune
			ballPivot.set(DoubleSolenoid.Value.kForward);
			innerMotor.set(Constants.GROUND_INTAKE_POWER);
			outerMotor.set(Constants.GROUND_INTAKE_POWER);
			break;
		case SPIT_BALLS:
			//if joystick value is not marginal, then the state cannot be spit balls
			if (OI.driver.getY(Hand.kLeft) >= Constants.XBOX_DEADBAND_VALUE
					|| OI.driver.getX(Hand.kRight) >= Constants.XBOX_DEADBAND_VALUE) {
				intakeState = IntakeState.LIFT_FOR_DRIVING;
				// TODO: tune
				ballPivot.set(DoubleSolenoid.Value.kForward);
				innerMotor.set(0);
				outerMotor.set(0);
				break;
			}
			intakeState = IntakeState.SPIT_BALLS;
			// TODO: tune
			ballPivot.set(DoubleSolenoid.Value.kReverse);
			innerMotor.set(Constants.SHOOT_BALLS_POWER);
			outerMotor.set(Constants.SHOOT_BALLS_POWER);
			break;
		case LIFT_FOR_DRIVING:
			intakeState = IntakeState.LIFT_FOR_DRIVING;
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

	@Override
	public void logToDashboard() {
		SmartDashboard.putString("HumanPlayerIntakeState", "" + loadingState);
		SmartDashboard.putString("IntakeState", "" + intakeState);
		SmartDashboard.putNumber("Inner Motor: PWM-" + innerMotor.getChannel() + " ", innerMotor.get());
		SmartDashboard.putNumber("Outer Motor: PWM-" + outerMotor.getChannel() + " ", outerMotor.get());
		SmartDashboard.putString("Ball Pivot: ", "" + ballPivot.get());
		SmartDashboard.putString("Human Player Intake Pivot: ", "" + humanIntakePivot.get());
	}

}
