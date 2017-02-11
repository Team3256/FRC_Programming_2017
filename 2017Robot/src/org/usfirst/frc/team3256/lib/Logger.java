package org.usfirst.frc.team3256.lib;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.usfirst.frc.team3256.robot.Constants;

public class Logger {

	private ArrayList<Log> logs;
	private Timer timer;
	
	public Logger(){
		logs = new ArrayList<Log>();
		timer = new Timer();
	}
	
	public void addLog(Log log){
		logs.add(log);
	}
	
	public void start(){
		timer.schedule(new UpdaterTask(), 0, 20);
	}
	
	public void stop(){
		timer.cancel();
		timer.purge();
	}
	
	private class UpdaterTask extends TimerTask{

		@Override
		public void run() {
			for(Log log : logs){
				log.logToDashboard();
			}
		}
		
	}
}
