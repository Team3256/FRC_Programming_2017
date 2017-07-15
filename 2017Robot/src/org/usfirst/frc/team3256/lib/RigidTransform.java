package org.usfirst.frc.team3256.lib;

public class RigidTransform implements Interpolatable<RigidTransform>{

	private static final double ZERO = 1E-9;
	
	private Translation translation;
	private Rotation rotation;
	
	public static class Delta{
		public double dx;
		public double dy;
		public double dtheta;
		
		public Delta(double dx, double dy, double dtheta){
			this.dx = dx;
			this.dy = dy;
			this.dtheta = dtheta;
		}
	}
	
	public RigidTransform(){
		translation = new Translation();
		rotation = new Rotation();
	}
	
	public RigidTransform(Translation translation, Rotation rotation){
		this.translation = translation;
		this.rotation = rotation;
	}
	
	public RigidTransform(RigidTransform other){
		this.translation = other.translation;
		this.rotation = other.rotation;
	}
	
	public static RigidTransform fromTranslation(Translation translation){
		return new RigidTransform(translation, new Rotation());
	}
	
	public static RigidTransform fromRotation(Rotation rotation){
		return new RigidTransform(new Translation(), rotation);
	}
	
	public static RigidTransform fromVelocity(Delta delta){
		double sin = Math.sin(delta.dtheta);
		double cos = Math.cos(delta.dtheta);
		double s, c;
		if (Math.abs(delta.dtheta) < ZERO){
			s = 1.0 - 1.0/6.0 * delta.dtheta*delta.dtheta;
			c = 0.5*delta.dtheta;
		}
		else {
			s = sin/delta.dtheta;
			c = (1.0 - cos)/delta.dtheta;
		}
		return new RigidTransform(new Translation(delta.dx*s - delta.dy*c, delta.dx*c + delta.dy*s), new Rotation(cos, sin, false));
	}
	
	public Translation getTranslation(){
		return translation;
	}
	
	public Rotation getRotation(){
		return rotation;
	}
	
	public void setTranslation(Translation translation){
		this.translation = translation;
	}
	
	public void setRotation(Rotation rotation){
		this.rotation = rotation;
	}

	public RigidTransform transformBy(RigidTransform other){
		return new RigidTransform(translation.translateBy(other.translation.rotateBy(rotation)), rotation.rotateBy(other.rotation));
	}
	
	public RigidTransform inverse(){
		Rotation inverted = rotation.inverse();
		return new RigidTransform(translation.inverse().rotateBy(inverted), inverted);
	}
	
	@Override
	public RigidTransform interpolate(RigidTransform other, double x) {
		if (x <= 0) return new RigidTransform(this);
		else if (x >= 1) return new RigidTransform(other);
		return new RigidTransform(translation.interpolate(other.translation, x), rotation.interpolate(other.rotation, x));
	}
	
	@Override 
	public String toString(){
		return "T: " + translation.toString() + ", R:" + rotation.toString();
	}
}
