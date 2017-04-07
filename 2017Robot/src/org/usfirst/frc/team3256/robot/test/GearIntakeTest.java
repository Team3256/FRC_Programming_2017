package org.usfirst.frc.team3256.robot.test;

public class GearIntakeTest {

	private static GearIntakeTest instance;
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
	
	public static GearIntakeTest getInstance(){
		return instance == null ? instance = new GearIntakeTest() : instance;
	}

	private GearIntakeTest(){
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
	
	/*
    public static void main(String[] args) {
    	GearIntakeTest test = GearIntakeTest.getInstance();
		System.out.println("TESTING INTAKE SEQUENCE");
		System.out.println("a = exit, s = down, w = up, d = deploy");
		Scanner in = new Scanner(System.in);
		String input;
		while (!(input = in.nextLine()).equals("a")) {
			switch (input) {
				case "s": {
					new GearHandlerDown().start();
					System.out.println("Down");
					break;
				}
				case "w": {
					new GearHandlerUp().start();
					System.out.println("Up");
					break;
				}
				case "d": {
					new GearHandlerDeploy().start();
					System.out.println("Deploy");
					break;
				}
			}
		}
		in.close();
	}
	*/
}

