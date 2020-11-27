package entity.vector;

public class VectorImpl  implements Vector {
	
	protected double x,y;
	
	protected double a;
	
	public double angleTowards(Vector other) {
		return Math.atan2(y - other.getY(), x - other.getX());
	}
	
	public double distanceTo(Vector other) {
		return Math.sqrt(Math.pow(x - other.getX(), 2) + Math.pow(y - other.getY(), 2));
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getAngle() {
		return a;
	}
}
