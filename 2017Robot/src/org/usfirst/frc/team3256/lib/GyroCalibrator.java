package org.usfirst.frc.team3256.lib;

import org.usfirst.frc.team3256.robot.Constants;
import org.usfirst.frc.team3256.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GyroCalibrator {

	private ADXRS453_Gyro gyro = DriveTrain.getInstance().getGyro();
	private Notifier notifier;
	private double prevTime = 0.0;
	
	public GyroCalibrator(){
		notifier = new Notifier(new Runnable(){

			@Override
			public void run() {
				double currentTime = Timer.getFPGATimestamp();
				if (currentTime-prevTime > ADXRS453_Gyro.kCalibrationSampleTime){
					gyro.endCalibrate();
					prevTime = currentTime;
					gyro.startCalibrate();
				}
			}
			
		});
	}
	
	public void start(){
		notifier.startPeriodic(Constants.SLOW_LOOP_DT);
	}
	
	public void stop(){
		gyro.cancelCalibrate();
		notifier.stop();
	}
}
