package org.usfirst.frc.team9256.lib;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PDP implements Log{

	private static PDP instance;
	PowerDistributionPanel pdp;
	
	/**
	 * Wrapper class for the PDP
	 */
	private PDP(){
		pdp = new PowerDistributionPanel();
	}
	
	public static PDP getInstance(){
		return instance == null ? instance = new PDP() : instance;
	}
	
	public double getCurrent(int port){
		return pdp.getCurrent(port);
	}
	
	public double getTotalCurrent(){
		return pdp.getTotalCurrent();
	}
	
	public double getTemperature(){
		return pdp.getTemperature();
	}
	
	public double getVoltage(){
		return pdp.getVoltage();
	}

	@Override
	public void logToDashboard() {
		SmartDashboard.putNumber("PDP INPUT VOLTAGE", getVoltage());
	}
	
}
