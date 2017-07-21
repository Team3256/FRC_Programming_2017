package org.usfirst.frc.team3256.lib;

import java.text.DecimalFormat;

public class Translation implements Interpolatable<Translation>{
	private DecimalFormat fmt = new DecimalFormat("#0.000");
	
	private double x;
	private double y;
	
	public Translation(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public Translation(Translation other){
		this(other.x, other.y);
	}
	
	public Translation(){
		this(0,0);
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public void setX(double x){
		this.x = x;
	}
	
	public void setY(double y){
		this.y = y;
	}
	
	public Translation translateBy(Translation other){
		return new Translation(x + other.x, y + other.y);
	}
	
	public Translation inverse(){
		return new Translation(-x, -y);
	}
	
	public Translation rotateBy(Rotation other){
		return new Translation(x*other.cos() - y*other.sin(), x*other.sin() + y*other.cos());
	}
	
	public double norm(){
		return Math.hypot(x, y);
	}
	
	@Override
	public Translation interpolate(Translation other, double x) {
		if (x <= 0) return new Translation(this);
		if (x >= 1) return new Translation(other);
		return extrapolate(other,x);
	}
	
	public Translation extrapolate(Translation other, double x){
		return new Translation(x * (other.x-this.x) + this.x, x * (other.y-this.y) + this.y);
	}
	
	@Override
	public String toString(){
		return "(" + fmt.format(x) + "," + fmt.format(y) + ")";
	}
}
