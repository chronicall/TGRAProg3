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
}
