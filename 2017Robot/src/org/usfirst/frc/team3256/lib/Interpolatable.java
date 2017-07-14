package org.usfirst.frc.team3256.lib;

public interface Interpolatable<T> {
	
	public T interpolate(T other, double x);

}
