package tgra.prog3.game;

import java.nio.FloatBuffer;

import com.badlogic.gdx.utils.BufferUtils;

public class Camera {
	Point3D eye;
	Vector3D u;
	Vector3D v;
	Vector3D n;
	
	boolean orthographic;
	
	float left;
	float right;
	float top;
	float bottom;
	float near;
	float far;
	
	private FloatBuffer matrixBuffer;
	
	public Camera() {
		this.matrixBuffer = BufferUtils.newFloatBuffer(16);
		
		this.orthographic = true;
		
		this.eye = new Point3D();
		this.u = new Vector3D(1.0f, 0.0f, 0.0f);
		this.v = new Vector3D(0.0f, 1.0f, 0.0f);
		this.n = new Vector3D(0.0f, 0.0f, 1.0f);
		
		this.left = -1;
		this.right = 1;
		this.bottom = -1;
		this.top = 1;
		this.near = -1;
		this.far = 1;
	}

	public void look(Point3D eye, Point3D center, Vector3D up) {
		this.eye.set(eye.x, eye.y, eye.z);
		
		this.n = Vector3D.difference(eye, center);
		this.u = up.cross(this.n);
		this.n = this.n.normalize();
		this.u = this.u.normalize();
		this.v = this.n.cross(this.u);
	}
	
	public void setEye(float x, float y, float z) {
		this.eye.set(x, y, z);
	}
	
	public void slide(float delU, float delV, float delN) {
		this.eye.x += delU * this.u.x + delV * this.v.x + delN * this.n.x;
		this.eye.y += delU * this.u.y + delV * this.v.y + delN * this.n.y;
		this.eye.z += delU * this.u.z + delV * this.v.z + delN * this.n.z;
	}
	
	public void roll(float angle) {
		float radians = angle * (float)Math.PI / 180.0f;
		float c = (float)Math.cos(radians);
		float s = (float)Math.sin(radians);
		
		Vector3D t = new Vector3D(this.u.x, this.u.y, this.u.z);
		
		this.u.set(t.x * c - this.v.x * s, t.y * c - this.v.y * s, t.z * c - this.v.z * s);
		this.v.set(t.x * s + this.v.x * c, t.y * s + this.v.y * c, t.z * s + this.v.z * c);
	}

	public void yaw(float angle) {
		float radians = angle * (float)Math.PI / 180.0f;
		float c = (float)Math.cos(radians);
		float s = (float)Math.sin(radians);
		
		Vector3D t = new Vector3D(this.u.x, this.u.y, this.u.z);
		
		this.u.set(t.x * c - this.n.x * s, t.y * c - this.n.y * s, t.z * c - this.n.z * s);
		this.n.set(t.x * s + this.n.x * c, t.y * s + this.n.y * c, t.z * s + this.n.z * c);
	}

	public void pitch(float angle) {
		float radians = angle * (float)Math.PI / 180.0f;
		float c = (float)Math.cos(radians);
		float s = (float)Math.sin(radians);
		
		Vector3D t = new Vector3D(this.n.x, this.n.y, this.n.z);
		
		this.n.set(t.x * c - this.v.x * s, t.y * c - this.v.y * s, t.z * c - this.v.z * s);
		this.v.set(t.x * s + this.v.x * c, t.y * s + this.v.y * c, t.z * s + this.v.z * c);
	}
	
	public void orthographicProjection(float left, float right, float bottom, float top, float near, float far) {
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		this.top = top;
		this.near = near;
		this.far = far;
		this.orthographic = true;
	}
	
	public void perspectiveProjection(float fov, float ratio, float near, float far) {
		this.top = near * (float)Math.tan(((double)fov / 2.0) * Math.PI / 180.0);
		this.bottom = -this.top;
		this.right = ratio * this.top;
		this.left = -this.right;
		this.near = near;
		this.far = far;
		this.orthographic = false;
	}
	
	public FloatBuffer getViewMatrix() {
		float[] pm = new float[16];
		
		Vector3D minusEye = new Vector3D(-this.eye.x, -this.eye.y, -this.eye.z);

		pm[0] = this.u.x; pm[4] = this.u.y; pm[8] = this.u.z;  pm[12] = minusEye.dot(this.u);
		pm[1] = this.v.x; pm[5] = this.v.y; pm[9] = this.v.z;  pm[13] = minusEye.dot(this.v);
		pm[2] = this.n.x; pm[6] = this.n.y; pm[10] = this.n.z; pm[14] = minusEye.dot(this.n);
		pm[3] = 0.0f; 	  pm[7] = 0.0f;  	pm[11] = 0.0f; 	   pm[15] = 1.0f;
		
		matrixBuffer.put(pm);
		matrixBuffer.rewind();
		
		return matrixBuffer;
	}
	
	public FloatBuffer getProjectionMatrix() {
		float[] pm = new float[16];
		
		if (this.orthographic) {
			pm[0] = 2.0f / (right - left); 			pm[4] = 0.0f;  				  			pm[8] = 0.0f; 							 pm[12] = -(right + left) / (right - left);
			pm[1] = 0.0f; 				   			pm[5] = 2.0f / (top - bottom); 			pm[9] = 0.0f; 							 pm[13] = -(top + bottom) / (top - bottom);
			pm[2] = 0.0f; 				   			pm[6] = 0.0f; 				  			pm[10] = 2.0f / (near - far); 			 pm[14] = (near + far) / (near - far);
			pm[3] = 0.0f; 				   			pm[7] = 0.0f; 				  			pm[11] = 0.0f; 							 pm[15] = 1.0f;
		}
		else {
			pm[0] = (2.0f * near) / (right - left); pm[4] = 0.0f; 							pm[8] = (right + left) / (right - left); pm[12] = 0.0f;
			pm[1] = 0.0f; 							pm[5] = (2.0f * near) / (top - bottom); pm[9] = (top + bottom) / (top - bottom); pm[13] = 0.0f;
			pm[2] = 0.0f;							pm[6] = 0.0f; 							pm[10] = -(far + near) / (far - near);   pm[14] = -(2.0f * far * near) / (far - near);
			pm[3] = 0.0f; 							pm[7] = 0.0f; 							pm[11] = -1.0f; 					  	 pm[15] = 0.0f;
		}
		
		matrixBuffer.put(pm);
		matrixBuffer.rewind();
		return matrixBuffer;
	}
}
