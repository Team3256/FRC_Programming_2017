package org.usfirst.frc.team3256.lib;

public class InterpolatingDouble implements Comparable<InterpolatingDouble>, Interpolatable<InterpolatingDouble>, 
												InverseInterpolatable<InterpolatingDouble>{

	private Double value;
	
	public InterpolatingDouble(Double value){
		this.value = value;
	}
	
	public InterpolatingDouble(Integer value){
		this((double)value);
	}
	
	public Double getValue(){
		return value;
	}
	
	@Override
	public InterpolatingDouble interpolate(InterpolatingDouble other, double input) {
		Double delta = other.value - value;
		Double output = delta * input + value;
		return new InterpolatingDouble(output);
	}

	@Override
	public int compareTo(InterpolatingDouble other) {
		if (value > other.value) return 1;
		else if (value < other.value) return -1;
		else return 0;
	}

	@Override
	public double inverseInterpolate(InterpolatingDouble upper, InterpolatingDouble input) {
		double upperToLower = upper.value - value;
		if (upperToLower <= 0) return 0;
		double inputToLower = input.value - value;
		if (inputToLower <= 0) return 0;
		return inputToLower/upperToLower;
	}
	
	@Override
	public String toString(){
		return "" + value;
	}
}
