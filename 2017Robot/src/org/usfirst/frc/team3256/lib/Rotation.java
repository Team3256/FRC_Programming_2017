package org.usfirst.frc.team3256.lib;

import java.text.DecimalFormat;

public class Rotation implements Interpolatable<Rotation>{
	
	private DecimalFormat fmt = new DecimalFormat("#0.000");
	
	private static final double ZERO = 1E-9;
	
	private double cos;
	private double sin;
	
	public Rotation(){
		this(1,0,false);
	}
	
	public Rotation(double x, double y, boolean normalize){
		cos = x;
		sin = y;
		if (normalize) normalize();
	}
	
	public Rotation(Rotation other){
		this.cos = other.cos;
		this.sin = other.sin;
	}
	
	public static Rotation fromRadians(double radians){
		return new Rotation(Math.cos(radians), Math.sin(radians), false);
	}
	
	public static Rotation fromDegrees(double degrees){
		return fromRadians(Math.toRadians(degrees));
	}
	
	private void normalize(){
		double mag = Math.hypot(cos, sin);
		if (mag > ZERO){
			sin /= mag;
			cos /= mag;
		}
		else{
			sin = 0;
			cos = 1;
		}
	}
	
	public double cos(){
		return cos;
	}
	
	public double sin(){
		return sin;
	}
	
	public double tan(){
		if (cos < ZERO){
			if (sin >= 0) return Double.POSITIVE_INFINITY;
			else return Double.NEGATIVE_INFINITY;
		}
		return sin/cos;
	}
	
	public double getRadians(){
		return Math.atan2(sin, cos);
	}
	
	public double getDegrees(){
		return Math.toDegrees(getRadians());
	}
	
	public Rotation rotateBy(Rotation other){
		return new Rotation(cos*other.cos - sin*other.sin, cos*other.sin + sin*other.cos, true);
	}
	
	public Rotation inverse(){
		return new Rotation(cos, -sin, false);
	}
	
	@Override
	public Rotation interpolate(Rotation other, double x) {
		if (x <= 0) return new Rotation(this);
		else if (x >= 1) return new Rotation(other);
		double angleDiff = inverse().rotateBy(other).getRadians();
		return this.rotateBy(Rotation.fromRadians(angleDiff*x));
	}	
	
	@Override
	public String toString(){
		return "(" + fmt.format(getDegrees()) + " deg)";
	}
}
