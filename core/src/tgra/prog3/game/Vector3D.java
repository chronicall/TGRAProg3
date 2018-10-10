package tgra.prog3.game;

public class Vector3D {
	public float x;
	public float y;
	public float z;
	
	public Vector3D() {
		this.x = 0.0f;
		this.y = 0.0f;
		this.z = 0.0f;
	}
	
	public Vector3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3D add(Vector3D v) {
		return new Vector3D(this.x + v.x, this.y + v.y, this.z + v.z);
	}
	
	public Vector3D scale(int S) {
		return new Vector3D(this.x * S, this.y * S, this.z * S);
	}
	
	public float length() {
		return (float)(Math.sqrt(x*x+y*y+z*z));
	}
	
	public float dot(Vector3D v) {
		return (this.x * v.x + this.y * v.y + this.z * v.z);
	}
	
	public Vector3D normalize() {
		float len = this.length();
		
		return new Vector3D(this.x / len, this.y / len, this.z / len);
	}
	
	public Vector3D cross(Vector3D v2) {
		return new Vector3D(this.y*v2.z - this.z*v2.y, this.z*v2.x - this.x*v2.z, this.x*v2.y - this.y*v2.x);
	}

	public static Vector3D difference(Point3D P2, Point3D P1) {
		return new Vector3D(P2.x-P1.x, P2.y-P1.y, P2.z-P1.z);
	}
	
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
