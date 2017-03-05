package org.usfirst.frc.team3256.robot.subsystems;

import org.usfirst.frc.team3256.lib.Log;
import org.usfirst.frc.team3256.robot.Constants;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Manipulator extends Subsystem implements Log {

	// Singleton instance of the Manipulator Subsystem
	private static Manipulator instance;

	private DoubleSolenoid humanIntakePivot;
	private DoubleSolenoid gearDeployer;

	/**
	 * The states of the human player intake
	 */
	public enum HumanPlayerLoadingState {
		GEAR_INTAKE, GEAR_DEPLOY, GEAR_RETRACT, BALLS_INTAKE;
	}


	// The current states of the systems
	HumanPlayerLoadingState loadingState = HumanPlayerLoadingState.GEAR_INTAKE;

	/**
	 * Cannot be instantiated out of the class and initializes the manipulator
	 * motors and actuators so we will always only have one Manipulator instance
	 * Use the getInstance() method
	 */
	private Manipulator() {
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
			gearDeployer.set(DoubleSolenoid.Value.kReverse);
			humanIntakePivot.set(DoubleSolenoid.Value.kForward);
			break;
		case GEAR_DEPLOY:
			loadingState = HumanPlayerLoadingState.GEAR_DEPLOY;
			gearDeployer.set(DoubleSolenoid.Value.kForward);
			break;
		case GEAR_RETRACT:
			loadingState = HumanPlayerLoadingState.GEAR_RETRACT;
			gearDeployer.set(DoubleSolenoid.Value.kReverse);
			break;
		case BALLS_INTAKE:
			loadingState = HumanPlayerLoadingState.BALLS_INTAKE;
			humanIntakePivot.set(DoubleSolenoid.Value.kReverse);
			gearDeployer.set(DoubleSolenoid.Value.kReverse);
			break;
		default:
			humanIntakePivot.set(DoubleSolenoid.Value.kOff);
			gearDeployer.set(DoubleSolenoid.Value.kOff);
			break;
		}

	}

	

	@Override
	public void logToDashboard() {
		SmartDashboard.putString("HumanPlayerIntakeState", "" + loadingState);
		SmartDashboard.putString("Human Player Intake Pivot: ", "" + humanIntakePivot.get());
	}

}
