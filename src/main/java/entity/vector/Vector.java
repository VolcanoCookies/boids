package entity.vector;

public interface Vector {
	
	// Rotation
	
	double angleTowards(Vector vector);
	
	// Distances
	
	double distanceTo(Vector vector);
	
	// Getters
	
	double getX();
	
	double getY();
	
	double getAngle();
	
}
