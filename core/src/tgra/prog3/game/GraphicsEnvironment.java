package tgra.prog3.game;

import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.BufferUtils;

public class GraphicsEnvironment {
	
	private static FloatBuffer projectionMatrix;
	public static int projectionMatrixLoc;
	
	public static int modelMatrixLoc;
	public static int positionLoc;
	
	public static int colorLoc;
	
	public static void setColour(float r, float g, float b) {
		Gdx.gl.glUniform4f(GraphicsEnvironment.colorLoc, r, g, b, 1.0f);
	}
	
	public static void setWindow(float left, float right, float bottom, float top) {
		float[] pm = new float[16];
		
		pm[0] = 2.0f / (right - left); pm[4] = 0.0f; 				  pm[8] = 0.0f;  pm[12] = -(right + left) / (right - left);
		pm[1] = 0.0f;				   pm[5] = 2.0f / (top - bottom); pm[9] = 0.0f;  pm[13] = -(top + bottom) / (top - bottom);
		pm[2] = 0.0f; 				   pm[6] = 0.0f; 				  pm[10] = 1.0f; pm[14] = 0.0f;
		pm[3] = 0.0f; 				   pm[7] = 0.0f; 				  pm[11] = 0.0f; pm[15] = 1.0f;
		
		projectionMatrix = BufferUtils.newFloatBuffer(16);
		projectionMatrix.put(pm);
		projectionMatrix.rewind();
		Gdx.gl.glUniformMatrix4fv(projectionMatrixLoc, 1, false, projectionMatrix);
	}

	public static void setShaderModelMatrix(ModelMatrix m) {
		Gdx.gl.glUniformMatrix4fv(modelMatrixLoc, 1, false, m.matrix);
	}
}
