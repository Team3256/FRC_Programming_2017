package org.usfirst.frc.team3256.lib;

public class DriveSignal {
	public double leftMotor;
    public double rightMotor;

    public DriveSignal(double left, double right) {
        this.leftMotor = left;
        this.rightMotor = right;
    }

    @Override
    public String toString() {
        return "L: " + leftMotor + ", R: " + rightMotor;
    }
}
