package tgra.prog3.game;

public class Point3D {
	public float x;
	public float y;
	public float z;
	
	public Point3D() {
		this.x = 0.0f;
		this.y = 0.0f;
		this.z = 0.0f;
	}
	
	public Point3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point3D add(Vector3D v) {
		return new Point3D(this.x + v.x, this.y + v.y, this.z + v.z);
	}
	
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3D different(Point3D p1) {
		return new Vector3D(this.x - p1.x, this.y - p1.y, this.z - p1.z);
	}
}
