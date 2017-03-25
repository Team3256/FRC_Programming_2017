package org.usfirst.frc.team3256.robot.test;

public class GearTest {

	private static GearTest instance;
	private GearHandlerState gearHandlerState;
	private final int intakeCurrentThreshhold = 30;
	private double simulatedCurrent = 0.0;
	private double startDeployTime = 0.0;
	private boolean startedDeploy = false;
	static double time = 0.0;
	
	public enum GearHandlerState{
		INTAKE,
		FLIP_UP,
		DEPLOY,
		EXHAUST,
		STOP;
	}
	
	public static GearTest getInstance(){
		return instance == null ? instance = new GearTest() : instance;
	}

	private GearTest(){
		gearHandlerState = GearHandlerState.STOP;
	}
	
	public void update(){
		switch (gearHandlerState){
			case INTAKE:
				if (hasGear()){
					setState(GearHandlerState.FLIP_UP);
				}
				break;
			case FLIP_UP:
				setState(GearHandlerState.STOP);
				break;
			case DEPLOY:
				startedDeploy = true;
				startDeployTime = time;
				setState(GearHandlerState.EXHAUST);
				break;
			case EXHAUST:
				if (releasedGear()){
					setState(GearHandlerState.STOP);
				}
				break;
			case STOP:
				startedDeploy = false;
				break;
			default:
				break;
		}
	}
	
	public GearHandlerState getState(){
		return gearHandlerState;
	}
	
	public void setState(GearHandlerState wantedState){
		switch (wantedState){
			case INTAKE:
				gearHandlerState = GearHandlerState.INTAKE;
				break;
			case FLIP_UP:
				gearHandlerState = GearHandlerState.FLIP_UP;
				break;
			case DEPLOY:
				gearHandlerState = GearHandlerState.DEPLOY;
				break;
			case EXHAUST:
				gearHandlerState = GearHandlerState.EXHAUST;
				break;
			case STOP:
				gearHandlerState = GearHandlerState.STOP;
				break;
		}
	}
	
	private boolean hasGear(){
		return simulatedCurrent > intakeCurrentThreshhold;
	}
	
	public void setCurrent(double current){
		simulatedCurrent = current;
	}
	
	public double getCurrent(){
		return simulatedCurrent;
	}
	
	private boolean releasedGear(){
		return time-startDeployTime > 2.0 && startedDeploy;
	}
	
	public boolean getStartedDeploy(){
		return startedDeploy;
	}

	public double getStartDeployTime(){
		return startDeployTime;
	}
	
    public static void main(String[] args){
    	GearTest test = GearTest.getInstance();
		System.out.println("TESTING INTAKE SEQUENCE");
		test.setState(GearHandlerState.INTAKE);
    	while(time < 5.0){
    		//simulate intaked gear at time = 2 seconds
    		if (time > 2 && time < 2.1){
    			test.setCurrent(50);
    		}
    		//otherwise the current is very small if we don't have a gear
    		else test.setCurrent(10);
    		System.out.println(test.getState() + "\t Current Time: " + time);
    		test.update();
    		//running at 50 hz
    		time += 0.02;
    	}
    	System.out.println("\n\nTESTING DEPLOY GEAR SEQUENCE");
    	test.setState(GearHandlerState.DEPLOY);
    	time = 0;
    	while(time < 5.0){
    		//simulate intaked gear at time = 2 seconds
    		if (time > 2 && time < 2.1){
    			test.setCurrent(50);
    		}
    		//otherwise the current is very small if we don't have a gear
    		else test.setCurrent(10);
    		System.out.println(test.getState() + "\t Current Time: " + time);
    		test.update();
    		//running at 50 hz
    		time += 0.02;
    	}
    }
}

