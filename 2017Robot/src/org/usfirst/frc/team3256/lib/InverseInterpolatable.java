package org.usfirst.frc.team3256.lib;

public interface InverseInterpolatable<T> {
	
	public double inverseInterpolate(T upper, T query);
}
