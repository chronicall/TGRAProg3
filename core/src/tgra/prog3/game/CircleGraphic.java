package tgra.prog3.game;

import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.BufferUtils;

public class CircleGraphic {
	private static FloatBuffer vertexBuffer;
	
	static int pointCount = 32;
	
	public static void create() {
		float[] array = new float[pointCount * 2];
		
		double angle = 0.0;
		double angleChange = (2.0 * Math.PI / (double)(pointCount));
		
		for (int i = 0; i < pointCount; i++) {
			array[i*2] = (float)Math.cos(angle);
			array[i*2+1] = (float)Math.sin(angle);
			
			angle += angleChange;
		}
		
		vertexBuffer = BufferUtils.newFloatBuffer(pointCount * 2);
		vertexBuffer.put(array);
		vertexBuffer.rewind();
	}
	
	public static void drawSolidCircle() {
		Gdx.gl.glVertexAttribPointer(GraphicsEnvironment.positionLoc, 2, GL20.GL_FLOAT, false, 0, vertexBuffer);
		Gdx.gl.glDrawArrays(GL20.GL_TRIANGLE_FAN, 0, pointCount);
	}
	
	public static void drawOutlinedCircle() {
		Gdx.gl.glVertexAttribPointer(GraphicsEnvironment.positionLoc, 2, GL20.GL_FLOAT, false, 0, vertexBuffer);
		Gdx.gl.glDrawArrays(GL20.GL_LINE_LOOP, 0, pointCount);
	}
}
