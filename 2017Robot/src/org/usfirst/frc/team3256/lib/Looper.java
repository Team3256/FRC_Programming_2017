package org.usfirst.frc.team3256.lib;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team3256.robot.Constants;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Looper {

	private List<Loop> loops;
	private Notifier notifier;
	private boolean enabled;
	private double actualDt = 0.0;
	private double previousTime = 0.0;
	
	public Looper(){
		loops = new ArrayList<Loop>();
		notifier = new Notifier(new Runnable(){
			@Override
			public void run(){
				if (enabled){
					double now = Timer.getFPGATimestamp();
					for(Loop loop : loops){
						loop.update();
					}
					actualDt = now-previousTime;
					previousTime = now;
				}
			}
		});
	}
	
	public void addLoop(Loop loop){
		loops.add(loop);
	}
	
	public void start(){
		enabled = true;
		for(Loop loop : loops){
			loop.initialize();
		}
		notifier.startPeriodic(Constants.CONTROL_LOOP_DT);
	}
	
	public void stop(){
		enabled = false;
		notifier.stop();
		for(Loop loop : loops){
			loop.end();
		}
	}
	
	public void log(){
		SmartDashboard.putNumber("Control Loops DT", actualDt);
	}
}
