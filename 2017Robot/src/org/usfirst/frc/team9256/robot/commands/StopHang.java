package org.usfirst.frc.team9256.robot.commands;

import org.usfirst.frc.team9256.robot.subsystems.Hanger;
import org.usfirst.frc.team9256.robot.subsystems.Hanger.HangerState;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class StopHang extends InstantCommand {
	
	Hanger hanger = Hanger.getInstance();
	
    public StopHang() {
        super();
        requires(hanger);
    }

    // Called once when the command executes
    protected void initialize() {
    	hanger.setHangerState(HangerState.WINCH_STOP);
    }

}
