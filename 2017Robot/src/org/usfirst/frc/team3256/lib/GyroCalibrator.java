package org.usfirst.frc.team3256.lib;

import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GyroCalibrator implements Loop{

	private ADXRS453_Gyro gyro;
	private double previousTime = 0.0;
	
	public GyroCalibrator(){
		gyro = DriveTrain.getInstance().getGyro();
	}

	@Override
	public void initialize() {
		
	}

	@Override
	public void update() {
		double now = Timer.getFPGATimestamp();
		if (now-previousTime > ADXRS453_Gyro.kCalibrationSampleTime){
			gyro.endCalibrate();
			previousTime = now;
			SmartDashboard.putNumber("Gyroscope calibration current time", Timer.getFPGATimestamp());
			gyro.startCalibrate();
		}
	}
	
	@Override
	public void end() {
		gyro.cancelCalibrate();
	}
}
